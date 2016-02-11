package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager lm;
    private Boolean NetworkCheck;
    private Boolean GPSCheck;
    private Location location;
    double latitude, longitude;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle bundle = getIntent().getExtras();
        bitmap = (Bitmap)bundle.get("camera_pic");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Geocoder geocoder = new Geocoder(this);

        try {
            lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            GPSCheck = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            NetworkCheck = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (NetworkCheck) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                }
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 1000, this);
                if (lm != null) {
                    location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            } else if (GPSCheck) {
                if (location == null) {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1000, this);
                    if (lm != null) {
                        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                }
            }

            // Add a marker in Sydney and move the camera
            LatLng place = new LatLng(latitude, longitude);
            StringBuilder add = new StringBuilder();

                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                Address address = addresses.get(0);
                add = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    add.append(address.getAddressLine(i)).append("\t");
                }
                add.append(address.getCountryName()).append("\t");
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
                mMap.addMarker(new MarkerOptions().position(place).title(String.valueOf(add)).icon(icon));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
            } catch (Exception e) {
                e.printStackTrace();
            }
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
