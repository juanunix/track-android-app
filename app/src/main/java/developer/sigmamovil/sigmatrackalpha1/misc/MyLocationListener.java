package developer.sigmamovil.sigmatrackalpha1.misc;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by usuario on 27/09/2015.
 */
public class MyLocationListener implements LocationListener {
    public Context context;
    public Location location;
    public LocationManager locationManager;

    public MyLocationListener(Context context) {
        this.context = context;
    }

    public boolean checkPermission() {
        if (checkLocationPermission()) {
            locationManager = (LocationManager) this.context.getSystemService(this.context.LOCATION_SERVICE);
            return true;
        }
        else {
            return false;
        }
    }

    public void start() {
        if (checkLocationPermission()) {
            Criteria criteria = new Criteria();

            String provider = locationManager.getBestProvider(criteria,false);

            if(provider != null & !provider.equals("")) {
                locationManager.requestLocationUpdates(provider, 1000, 10, this);
                location = locationManager.getLastKnownLocation(provider);

                if(location != null) {
                    onLocationChanged(location);
                }
                else {
                    locationManager.requestLocationUpdates(provider, 2000, 1, this);
                    location = locationManager.getLastKnownLocation(provider);
                    if(location == null) {
                        locationManager.removeUpdates(this);
                        locationManager.requestLocationUpdates(provider, 4000, 30, this);
                        location = locationManager.getLastKnownLocation(provider);
                        if (location == null) {
                            Toast.makeText(this.context, "No se ha podido obtener la ubicación", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            else {
                Toast.makeText(this.context,"Este dispositivo no tiene GPS", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this.context, "Acceso a la ubicación denegado", Toast.LENGTH_LONG).show();
        }
    }


    public Location getLocation() {
        return this.location;
    }

    public boolean checkLocationPermission() {
        PackageManager pm = this.context.getPackageManager();
        int coarse_location = pm.checkPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                this.context.getPackageName());

        int fine_location = pm.checkPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                this.context.getPackageName());

        if (coarse_location != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (fine_location != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    public boolean checkLocationServices() {
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex){
            ex.printStackTrace();
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex){
            ex.printStackTrace();
        }

        if (!gps_enabled && !network_enabled ){
            return false;
        }

        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

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
}
