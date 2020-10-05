package com.nextdots.airbnb.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nextdots.airbnb.R;
import com.nextdots.airbnb.activities.MainActivity;
import com.nextdots.airbnb.interfaces.MainActivityInterface;
import com.nextdots.airbnb.interfaces.ServicesRequest;
import com.nextdots.airbnb.models.Favorite;
import com.nextdots.airbnb.models.Listing;
import com.nextdots.airbnb.models.ResDetail;
import com.nextdots.airbnb.models.SearchResults;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mariexi on 05/12/16.
 */
public class DetailsFragment extends Fragment {
    public  static final  String TAG = DetailsFragment.class.getSimpleName();
    public  static final  String TAG_ITE = "item";
    private static final String TAG_IS_FAV = "is_fav";

    MainActivityInterface mainActivityInterface;
    FragmentActivity myContext;
    SearchResults item;

    private boolean isFav;
    private Favorite mFavorite;
    MapView mMapView;
    private GoogleMap googleMap;
    @BindView(R.id.image) ImageView image;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.type) TextView type;
    @BindView(R.id.price) TextView price;
    @BindView(R.id.type_room) TextView type_room;
    @BindView(R.id.address) TextView address;
    @BindView(R.id.guest) TextView guest;
    @BindView(R.id.bathrooms) TextView bathrooms;
    @BindView(R.id.beds) TextView beds;
    @BindView(R.id.bedrooms) TextView bedrooms;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.favorite) ImageView favorite;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            isFav = getArguments().getBoolean(TAG_IS_FAV);
            if (isFav){
                mFavorite = (Favorite) getArguments().getSerializable(TAG_ITE);
            }else {
                item = (SearchResults) getArguments().getSerializable(TAG_ITE);
            }
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        mainActivityInterface.setTitulo(getResources().getString(R.string.detail));

        MainActivity activity = (MainActivity) getActivity();

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();



        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        if(!isFav) {
            List<Favorite> favorites = Favorite.find(Favorite.class,"ident = ?", item.getListing().getId()+"");
            if (favorites.size() == 0) {
                favorite.setImageResource(R.drawable.star);
            } else {
                favorite.setImageResource(R.drawable.star_fav);
            }

            serviceRequest();
        }else{
            List<Favorite> favorites = Favorite.find(Favorite.class,"ident = ?", mFavorite.getId()+"");
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

            favorite.setImageResource(R.drawable.star_fav);

            Glide.with(myContext)
                    .load(mFavorite.getPicture_url())
                    .into(image);

            name.setText(mFavorite.getName());
            type.setText(mFavorite.getProperty_type());
            price.setText(mFavorite.getPrice()+" "+mFavorite.getNative_currency());
            type_room.setText(mFavorite.getRoom_type());
            address.setText(mFavorite.getPublic_address());
            guest.setText(mFavorite.getPerson_capacity()+"");
            bathrooms.setText(((int) mFavorite.getBathrooms())+"");
            beds.setText(mFavorite.getBeds()+"");
            bedrooms.setText(mFavorite.getBedrooms()+"");
            description.setText(mFavorite.getDescription()+"");

            MarkerOptions marker = new MarkerOptions().position(new LatLng(mFavorite.getLat(), mFavorite.getLng())).title(mFavorite.getName());

            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            googleMap.addMarker(marker);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(mFavorite.getLat(), mFavorite.getLng())).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Favorite favorite = Favorite.findById(Favorite.class, mFavorite.getId());
                        favorite.delete();

                        myContext.getSupportFragmentManager().popBackStack();
                                        }
            });

        }

        //((MainActivity)getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void serviceRequest(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServicesRequest servicesRequest = retrofit.create(ServicesRequest.class);
        Call<ResDetail> mService = servicesRequest.getListDetails(item.getListing().getId());

        mService.enqueue(new Callback<ResDetail>() {
            @Override
            public void onResponse(Call<ResDetail> call, final Response<ResDetail> response) {
                final Listing resp = response.body().getListing();

                if (response.isSuccessful()){
                        Log.d("--> Item ", String.valueOf(item.getListing().getId()));
                        Log.d("--> Detail ", String.valueOf(response.body().getListing().getId()));

                        progressBar.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);

                        Glide.with(myContext)
                                .load(response.body().getListing().getPicture_url())
                                .into(image);

                        name.setText(resp.getName());
                        type.setText(resp.getProperty_type());
                        price.setText(resp.getPrice()+" "+resp.getNative_currency());
                        type_room.setText(resp.getRoom_type());
                        address.setText(resp.getPublic_address());
                        guest.setText(resp.getPerson_capacity()+"");
                        bathrooms.setText(((int) resp.getBathrooms())+"");
                        beds.setText(resp.getBeds()+"");
                        bedrooms.setText(resp.getBedrooms()+"");
                        description.setText(resp.getDescription()+"");

                        MarkerOptions marker = new MarkerOptions().position(new LatLng(item.getListing().getLat(), item.getListing().getLng())).title(item.getListing().getName());

                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                        googleMap.addMarker(marker);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(item.getListing().getLat(), item.getListing().getLng())).zoom(15).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                    favorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            List<Favorite> favorites = Favorite.find(Favorite.class,"ident = ?", item.getListing().getId()+"");

                            if(favorites.size()==0){
                                Favorite favorit = new Favorite(resp.getId(),R.drawable.star,
                                        resp.getName(),resp.getProperty_type(),resp.getPicture_url(),
                                        resp.getRoom_type(),resp.getPublic_address(),resp.getBathrooms(),
                                        resp.getBedrooms(),resp.getBeds(),resp.getPerson_capacity(),
                                        resp.getLat(),resp.getLng(), resp.getPrice(),resp.getNative_currency(),resp.getDescription());
                                favorit.save();
                                favorite.setImageResource(R.drawable.star_fav);
                            }else{
                                favorites.get(0).delete();
                                favorite.setImageResource(R.drawable.star);
                            }

                        }
                    });

                }

            }

            @Override
            public void onFailure(Call<ResDetail> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                call.cancel();
                Toast.makeText(myContext, myContext.getResources().getString(R.string.failure_network), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onAttach(Context activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
        try {
            mainActivityInterface = (MainActivityInterface) activity;
        } catch (ClassCastException e) {
            Log.e(TAG, activity.toString() + "must implement onCropListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivityInterface = null;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public static DetailsFragment newInstence(SearchResults item) {
        DetailsFragment f = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG_ITE,item);
        bundle.putBoolean(TAG_IS_FAV,false);
        f.setArguments(bundle);
        return f;
    }

    public static DetailsFragment newInstence(Favorite item) {
        DetailsFragment f = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG_ITE,item);
        bundle.putBoolean(TAG_IS_FAV,true);
        f.setArguments(bundle);
        return f;
    }



    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}

