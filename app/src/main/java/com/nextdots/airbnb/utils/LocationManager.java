package com.nextdots.airbnb.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Mariexi on 04/12/2016.
 */

public class LocationManager implements android.location.LocationListener {
    private static final String TAG = LocationManager.class.getSimpleName();

    private android.location.LocationManager locationManager;
    private LocationListener mListener;
    private String proveedorActual;
    private Context context;


    public LocationManager(Context context, LocationListener listener) {
        this.context = context;
        mListener = listener;
        locationManager = (android.location.LocationManager) context.getSystemService(
                context.LOCATION_SERVICE
        );
    }

    public void remoteLocationListener() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(this);
            mListener = null;
        }
    }

    public void verificarLocalizacion() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        if( (locationManager != null ) && (mListener !=null)) {
            boolean gpsHabilitado = locationManager
                    .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
            boolean redHabilitada = locationManager
                    .isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);

            if (gpsHabilitado) {
                proveedorActual = locationManager.GPS_PROVIDER;
                Location ultConocido = locationManager.getLastKnownLocation(proveedorActual);
                mListener.newLocation(ultConocido.getLatitude(), ultConocido.getLongitude());
                locationManager.requestLocationUpdates(proveedorActual, 5,
                        10000F, this);

            } else if (redHabilitada) {
                proveedorActual = locationManager.NETWORK_PROVIDER;
                Location ultConocido = locationManager.getLastKnownLocation(proveedorActual);

                mListener.newLocation(ultConocido.getLatitude(), ultConocido.getLongitude());
                locationManager.requestLocationUpdates(proveedorActual, 5,
                        10000F, this);

            } else {
                Location L = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                if (L != null) {
                    mListener.newLocation(L.getLatitude(), L.getLongitude());
                } else {
                    mListener.withoutLocationAvailable();
                }
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if(mListener != null) {
            mListener.newLocation(location.getLatitude(), location.getLongitude());
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    public interface LocationListener {
        void newLocation(Double lat, Double lon);
        void withoutLocationAvailable();
    }
}
