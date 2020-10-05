package com.nextdots.airbnb.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nextdots.airbnb.R;
import com.nextdots.airbnb.adapters.ListingAdapter;
import com.nextdots.airbnb.interfaces.MainActivityInterface;
import com.nextdots.airbnb.interfaces.ServicesRequest;
import com.nextdots.airbnb.models.ResHotel;
import com.nextdots.airbnb.models.SearchResults;
import com.nextdots.airbnb.utils.LocationManager;
import com.nextdots.airbnb.utils.RecyclerItemClickListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mariexi on 04/12/2016.
 */
public class HomeFragment extends Fragment implements LocationManager.LocationListener {
    public  static final  String TAG = HomeFragment.class.getSimpleName();
    private static final int LIMIT_ITEM = 30;
    MainActivityInterface mainActivityInterface;
    FragmentActivity myContext;
    RecyclerView list;
    private LocationManager locationManager;
    private ListingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = new LocationManager(getContext(), this);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        mainActivityInterface.setTitulo(getResources().getString(R.string.home));

        list = (RecyclerView) view.findViewById(R.id.list);

        mLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        list.setLayoutManager(mLayoutManager);

        locationManager.verificarLocalizacion();

        return view;
    }


    public void serviceRequest(String city){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServicesRequest servicesRequest = retrofit.create(ServicesRequest.class);
        Call<ResHotel> mService = servicesRequest.getListSearch(LIMIT_ITEM, city);

        mService.enqueue(new Callback<ResHotel>() {
            @Override
            public void onResponse(Call<ResHotel> call, Response<ResHotel> response) {

                if (response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);

                    mAdapter = new ListingAdapter(response.body().getSearch_results());
                    list.setAdapter(mAdapter);

                    list.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            SearchResults item = mAdapter.getItemPosition(position);

                            FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                            DetailsFragment fragment = DetailsFragment.newInstence(item);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.content_frame, fragment, fragment.TAG)
                                    .addToBackStack(fragment.TAG)
                                    .commit();

                        }
                    }));

                }
            }

            @Override
            public void onFailure(Call<ResHotel> call, Throwable t) {
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void newLocation(Double lat, Double lon) {
        try {
            Log.d(TAG+"-->", "lat = "+lat+ ", lon="+lon);
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> address = null;
            address = geocoder.getFromLocation(lat, lon, 1);
            String city = (address.get(0).getLocality()+"").replace(" ","+");
            serviceRequest(city);
            locationManager.remoteLocationListener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void withoutLocationAvailable() {
        //call service with the Angeles location
        serviceRequest("Los+Angeles");
        //lat":34.02991065328165,"lng":-118.39945477165404

    }
}
