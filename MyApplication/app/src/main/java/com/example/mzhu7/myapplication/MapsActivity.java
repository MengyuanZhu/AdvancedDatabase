package com.example.mzhu7.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapsActivity extends FragmentActivity implements OnMarkerClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
         {

             private GoogleMap mMap;
             GoogleApiClient mGoogleApiClient;
             Location mLastLocation;
             Marker mCurrLocationMarker;
             LocationRequest mLocationRequest;
             Socket clientSocket;
             String username;
             String pokemonName;
            MyThread m;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        username=getIntent().getExtras().getString("username");
        Toast.makeText(this, "Welcome back, "+username+"!", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings mUiSettings= mMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(false);
        mUiSettings.setZoomControlsEnabled(false);
        //mUiSettings.setZoomGesturesEnabled(false);
        mUiSettings.setMapToolbarEnabled(false);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMarkerClickListener(this);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(1);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient ,mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

             @Override
             public boolean onMarkerClick(final Marker marker) {

                 // Retrieve the data from the marker.
                 Integer clickCount = (Integer) marker.getTag();

                 // Check if a click count was set, then display the click count.
                 if (clickCount != null) {
                     clickCount = clickCount + 1;
                     marker.setTag(clickCount);
                     if (clickCount<5){
                         Toast.makeText(this,
                                 marker.getTitle() +
                                         " has been caught " + clickCount + " times.",
                                 Toast.LENGTH_SHORT).show();
                     }
                     else{
                         Toast.makeText(this,
                                 marker.getTitle() +
                                         " has been caught finally. Congratulations!",
                                 Toast.LENGTH_SHORT).show();
                         pokemonName=marker.getTitle();
                         m= new MyThread();
                         new Thread(m).start();
                         marker.remove();
                     }
                 }

                 return false;
             }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng latLng1 = new LatLng(location.getLatitude()+Math.random()*0.0016-0.0008, location.getLongitude()+Math.random()*0.001-0.0005);
        LatLng latLng2 = new LatLng(location.getLatitude()+Math.random()*0.0016-0.0008, location.getLongitude()+Math.random()*0.001-0.0005);
        LatLng latLng3 = new LatLng(location.getLatitude()+Math.random()*0.0016-0.0008, location.getLongitude()+Math.random()*0.001-0.0005);
        LatLng[] latLngs =new LatLng[]{latLng1,latLng2,latLng3} ;

        int locationIndex=0;
        while (locationIndex<3){
            int randomNum = 1 + (int)(Math.random() * 151);
            NumberFormat formatter = new DecimalFormat("000");
            String IDString = formatter.format(randomNum).toString();
            String dString="p"+IDString;
            String packageName = getPackageName();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier(dString, "drawable", packageName)));
            markerOptions.position(latLngs[locationIndex]);
            String aString="pokemon"+IDString;
            int resId = getResources().getIdentifier(aString, "string", packageName);
            Resources res = getResources();
            markerOptions.title((res.getString(resId)));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            mCurrLocationMarker.setTag(0);
            locationIndex=locationIndex+1;
        }
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(19));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public void userInformation(View view){
        Intent intent = new Intent(this, UserInformation.class);
        String message = "mzhu7";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

             class MyThread implements Runnable {
                 public void run() {
                     Message msg = new Message();
                     Bundle b = new Bundle();
                     try {
                         Integer randomHP=5 + (int)(Math.random() * 1000);
                         String randomHPString=","+randomHP.toString();
                         String str = "1,"+username+","+pokemonName+randomHPString+randomHPString+randomHPString+randomHPString+randomHPString+randomHPString; //if state is 0

                         Socket s = new Socket("target.gsu.edu", 6970);
                         PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                         out.println(str);

                         BufferedReader input =  new BufferedReader(new InputStreamReader(s.getInputStream()));
                         String answer = input.readLine();



                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                     //b.putString("color", "sb");
                     msg.setData(b);


                 }
             }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
}
