package com.example.matt.ignitecs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import java.io.File;
import java.io.IOException;

public class DisplayInformation extends AppCompatActivity
        implements OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener
{

    private GoogleApiClient mGoogleApiClient;
    TextView txtLocCoarse;
    Button btnTakePicture;
    ImageView displayPicture;

    int CAMERA_PIC_REQUEST = 1337;
    public static int count = 0;

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

            Location mCurrentLocation;
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            txtLocCoarse.setText(mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude());
        } catch (SecurityException e) {

        }
    }

    /*
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

    /*
    The following methods are for the controlling the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK)
        {
            Log.d("DisplayInformation", "Pic Saved");
        }
//        if (requestCode == CAMERA_PIC_REQUEST)
//        {
//            // data.getExtras()
//            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        }
//        else
//        {
//            Toast.makeText(DisplayInformation.this, "Pictures Not Taken", Toast.LENGTH_LONG);
//        }
//        super.onActivityResult(requestCode, resultCode, data);


    }

} // end class
