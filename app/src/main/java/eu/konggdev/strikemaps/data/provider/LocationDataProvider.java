package eu.konggdev.strikemaps.data.provider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.List;

public class LocationDataProvider implements Provider {
    private LocationManager locationManager;

    private List<String> locationManagerProviders = List.of(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER);
    public LocationDataProvider(AppCompatActivity activity, LocationListener listener) {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        //TODO: Move permission request to UI
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location initLocation = null;

            for (String provider : locationManagerProviders) {
                if(locationManager.isProviderEnabled(provider)) {
                    if (initLocation == null) {
                        initLocation = locationManager.getLastKnownLocation(provider);
                        if (initLocation != null)
                            listener.onLocationChanged(initLocation);
                    }

                    locationManager.requestLocationUpdates(
                            provider,
                            1000,
                            1,
                            listener
                    );
                }
            }
        }
    }
}
