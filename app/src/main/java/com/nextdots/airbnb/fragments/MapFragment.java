package com.nextdots.airbnb.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nextdots.airbnb.R;
import com.nextdots.airbnb.interfaces.MainActivityInterface;
import com.nextdots.airbnb.interfaces.ServicesRequest;
import com.nextdots.airbnb.models.ResHotel;
import com.nextdots.airbnb.models.SearchResults;
import com.nextdots.airbnb.utils.LocationManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mariexi on 04/12/2016.
 */
public class MapFragment extends Fragment implements LocationManager.LocationListener {
    public static final String TAG = MapFragment.class.getSimpleName();
    MainActivityInterface mainActivityInterface;
    FragmentActivity myContext;
    MapView mMapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private static final int LIMIT_ITEM = 30;
    private ArrayList<SearchResults> items;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = new LocationManager(getContext(), this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        mainActivityInterface.setTitulo(getResources().getString(R.string.map));

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

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
                        items = response.body().getSearch_results();

                        for (int i=0; i<items.size();i++){
                            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(items.get(i).getListing().getLat(), items.get(i).getListing().getLng())).title(items.get(i).getListing().getName());

                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                            Marker marker = googleMap.addMarker(markerOptions);
                            items.get(i).setMarkerId(marker.getId());

                            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(response.body().getSearch_results().get(i).getListing().getLat(), response.body().getSearch_results().get(i).getListing().getLng())).zoom(12).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                    for(SearchResults item : items){
                                        if(item.getMarkerId().equals(marker.getId())){
                                            FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                                            DetailsFragment fragment = DetailsFragment.newInstence(item);
                                            fragmentManager.beginTransaction()
                                                    .replace(R.id.content_frame, fragment, fragment.TAG)
                                                    .addToBackStack(fragment.TAG)
                                                    .commit();
                                        }
                                    }
                                }
                            });
                        }
                }
            }

            @Override
            public void onFailure(Call<ResHotel> call, Throwable t) {
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
         serviceRequest("Los"+"Angeles");
    }

}

