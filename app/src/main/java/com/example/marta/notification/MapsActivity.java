package com.example.marta.notification;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.Manifest;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;


import static android.os.Build.VERSION_CODES.M;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    final int PERMISSION_LOCATION = 111;
    private GoogleApiClient mGoogleApiClient;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.container_main);

        if (mapFragment == null){
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_main, mapFragment)
                    .commit();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_LOCATION);
            Log.v("MAPS", "Requesting permissions");
        } else {
            Log.v("MAPS", "Starting location Services from onConnected");
            startLocationServices();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("MAPS", "Long: " + location.getLongitude() + " - Lat: " + location.getLatitude());
        if (mapFragment != null) {
            mapFragment.setUserMarker(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        }



    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_LOCATION: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startLocationServices();
                    Log.v("MAPS", "Permission Granted - starting services");
                } else {
                    //TODO: show dialog to user saing we can't get location unless they give app permision
                    Log.v("MAPS", "Permission not granted");
                }
            }
        }
    }

    public void startLocationServices(){
        Log.v("MAPS", "Starting Location Services Called");

        try {
            LocationRequest req = LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, req, this);
            Log.v("MAPS", "Requesting location updates");
        }catch (SecurityException exception){
            //TODO: show dialog to user saing we can't get location unless they give app permision
            Log.v("MAPS", exception.toString());
        }


    }

}
