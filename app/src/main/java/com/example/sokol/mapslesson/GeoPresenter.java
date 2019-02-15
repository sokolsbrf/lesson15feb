package com.example.sokol.mapslesson;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoPresenter {

    private LocationListener listener = null;
    private GeoView geoView;
    private String address = "";

    private Context applicationContext;
    private Geocoder geocoder;

    public GeoPresenter(Context context) {
        this.applicationContext = context.getApplicationContext();
        geocoder = new Geocoder(applicationContext, Locale.forLanguageTag("ru"));

        LocationManager lm = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    new GeocodeTask(location).execute();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);

            lm.requestLocationUpdates(lm.getBestProvider(criteria, true), 1000, 1f, listener);
        }
    }

    public void attach(GeoView view) {
        geoView = view;
        view.showLocation(address);
    }

    public void detach() {
        geoView = null;
    }

    public void destroy() {
        LocationManager lm = (LocationManager) applicationContext
                .getSystemService(Context.LOCATION_SERVICE);
        lm.removeUpdates(listener);
    }

    private class GeocodeTask extends AsyncTask<Void, Void, String> {

        private Location location;

        private GeocodeTask(Location location) {
            this.location = location;
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (Geocoder.isPresent()) {
                try {
                    List<Address> results = geocoder.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);

                    if (!results.isEmpty()) {
                        return results.get(0).getAddressLine(0);
                    }

                    return applicationContext.getString(R.string.no_results);

                } catch (IOException e) {
                    e.printStackTrace();
                    return applicationContext.getString(R.string.no_network);
                }
            }

            return applicationContext.getString(R.string.no_geocoder);
        }

        @Override
        protected void onPostExecute(String s) {
            address = s;

            if (geoView != null) {
                geoView.showLocation(address);
            }
        }
    }
}
