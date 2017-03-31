package com.example.matt.ignitecs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.name;

public class DisplayInformation extends AppCompatActivity
        implements OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener,
        OnMapReadyCallback
{

    private GoogleApiClient mGoogleApiClient;
    TextView txtLocCoarse;
    Button btnTakePicture;
    Button btnContacts;
    ImageView displayPicture;
    Location mCurrentLocation;
    ListView contactsList;
    Cursor cursor;

    ArrayList<String> StoreContacts;
    ArrayAdapter<String> arrayAdapter;

    int CAMERA_PIC_REQUEST = 1337;
    public  static final int RequestPermissionCode  = 1 ;
    public static int count = 0;
    String name, phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_information);

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

        /*
        the following has to do with the camera
         */

        // create directory named igniteCSPics to store pics taken by camera using this application
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/igniteCSPics/";
        File newdir = new File(dir);
        newdir.mkdirs();

        // create button
        btnTakePicture = (Button) findViewById(R.id.btnTakePicture);
        btnTakePicture.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // counter will be incremented each time to save the pics taken by the camera as
                // 1.jpg, 2.jpg, etc.
                count++;
                String file = dir + count + ".jpg";
                File newFile = new File(file);

                // create new file in directory igniteCSPics
                try{
                    newFile.createNewFile();
                }
                catch(IOException e)
                {
                }

                Uri outputFileUri = Uri.fromFile(newFile);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });


        // Create camera
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

        // enable to automatically click button when displayinformation is called
       // btnTakePicture.callOnClick();

        /**
         * Grab Contacts
         */
        contactsList = (ListView) findViewById(R.id.contactsList);
        btnContacts = (Button) findViewById((R.id.btnContacts));
        StoreContacts = new ArrayList<String>();

        // toasts the user that their contacts are now being accessed
        EnableRuntimePermission();

        btnContacts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                GetContactsIntoArrayList();
//
//                arrayAdapter = new ArrayAdapter<String>(
//                        DisplayInformation.this,
//                        R.layout.activity_display_information, // possibly change if it doesn't work
//                        R.id.contactsList, StoreContacts
//                );
//
//                contactsList.setAdapter(arrayAdapter);
//                contactsList.setTextFilterEnabled(true);

            }
        });

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
    public void onLocationChanged(Location loc)
    {

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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

    /**
     * THe following methods are for the contacts
     */
    public void GetContactsIntoArrayList(){

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            StoreContacts.add(name + " "  + ":" + " " + phonenumber);
        }

        cursor.close();

    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                DisplayInformation.this,
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(DisplayInformation.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(DisplayInformation.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(DisplayInformation.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(DisplayInformation.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

} // end class
