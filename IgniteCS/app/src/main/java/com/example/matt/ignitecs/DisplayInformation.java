package com.example.matt.ignitecs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

// Maps (coarse location)
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

// Maps (fine location)
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;


public class DisplayInformation extends AppCompatActivity
        implements OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener,
        OnMapReadyCallback, FragmentContacts.OnFragmentInteractionListener,
        FragmentCamera.OnFragmentInteractionListener
{

    private GoogleApiClient mGoogleApiClient;
    TextView txtLocCoarse;
    Location mCurrentLocation;


    private static String mFileName = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS};

    int CAMERA_PIC_REQUEST = 1337;
    public static final int REQUEST_CONTACTS_PERMISSION = 1 ;
    public static int count = 0;
    String name, phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_information);

        /**
         * The following code is for the Google Maps API
         */

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        txtLocCoarse = (TextView) findViewById(R.id.txtLocCoarse);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    } // end onCreate

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently

        // ...
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            LocationRequest mCoarseLocationRequest = new LocationRequest();
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mCoarseLocationRequest, this);

            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            txtLocCoarse.setText(mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude());
        } catch (SecurityException e) {
            // ERROR
        }
    }

    /**
     * The following are for google api
     */
    @Override
    public void onConnectionSuspended(int cause) {
        //your code goes here
    }

    @Override
    public void onLocationChanged(Location loc) {

    }


    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        LatLng gb = new LatLng(44.531854, -87.916980);

        googleMap.addMarker(new MarkerOptions().position(gb)
                .title("You are here"));
        // googleMap.moveCamera(CameraUpdateFactory.newLatLng(gb));
        float zoomLevel = 16;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gb, zoomLevel));
    }

    /**
     * The following methods are for the controlling the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK)
        {
            Log.d("DisplayInformation", "Pic Saved");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CONTACTS_PERMISSION:
                permissionAccepted = (grantResults[1] == PackageManager.PERMISSION_GRANTED);
                Toast.makeText(DisplayInformation.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();
                break;
        }
        if (!permissionAccepted ) finish();
    }



    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

} // end class
