package com.example.britt.walkmydog;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import static java.lang.Double.valueOf;

import com.google.android.gms.location.FusedLocationProviderClient;


public class AdvertActivity extends AppCompatActivity {

    // Initialize dog data.
    String dog_name;
    String description;
    String id;
    String encoded_picture;

    // Initialize for layout.
    ImageView get_picture;
    Spinner spinner;
    TextView get_text;
    EditText get_dog_name;
    EditText get_description;

    // Initialize for database.
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    // Set values for use of camera.
    static final int REQUEST_IMAGE_CAPTURE = 1;
    int myCameraRequestCode = 100;
    Boolean boolCamera = false;
    Bitmap imageBitmap;

    // Set values for use of location.
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    int myLocationRequestCode = 100;
    double longitude = 0.0000 ;
    double latitude = 0.0000 ;
    Boolean boolLocation = true;

    // Set tag for logs.
    private static final String TAG = "MapActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);

        // Set spinner to be able to choose category.
        spinner = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_advert,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get layout views.
        get_dog_name = findViewById(R.id.dog_name);
        get_description = findViewById(R.id.description);
        get_picture = findViewById(R.id.picture);
        get_text = findViewById(R.id.use_logo);

        // If not already given, ask for permission to use location.
        // If not already turned on, ask user to turn on location.
        if (ActivityCompat.checkSelfPermission(AdvertActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AdvertActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, myLocationRequestCode);
            showSettingAlert();
        }

        else {

            if (boolLocation) {
                showSettingAlert();
            }
            mLocationPermissionsGranted = true;
            getLocation();
        }

        // Get database.
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    /**
     * Go to selected option in spinner.
     */
    public void SelectOption(View view) {
        // Log out when button is clicked.
        String option = spinner.getSelectedItem().toString();

        if (option.equals("Log Out")) {
            // Sign out in firebase.
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AdvertActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    /**
     * User is able to take a picture of the dog.
     */
    public void makePicture(View view) {
        // Set booleans for onRequestPermissionResult.
        boolCamera = true;
        boolLocation = false;

        // If not already given, ask for permission to use camera.
        if (ActivityCompat.checkSelfPermission(AdvertActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AdvertActivity.this, new String[]{android.Manifest.permission.CAMERA}, myCameraRequestCode);
        }
        else {
            dispatchTakePictureIntent();
        }
    }


    /**
     * Gets information/result out of location or camera request.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check if result is asked of location or camera.
        if (boolLocation) {

            // If permission granted and location determined, get current location of user.
            if (requestCode == myLocationRequestCode) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "location permission granted", Toast.LENGTH_LONG).show();
                    getLocation();

                } else {
                    Toast.makeText(this, "location permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }

        if (boolCamera) {
            // If permission granted, go to camera.
            if (requestCode == myCameraRequestCode) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                    dispatchTakePictureIntent();

                } else {
                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    /**
     * Go to camera and take picture.
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    /**
     * Get image taken by camera.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            // Show image in current activity.
            get_picture.setImageBitmap(imageBitmap);

            // Encode the picture and save the picture to firebase.
            encodeBitmap(imageBitmap);
        }
    }

    /**
     * Encode picture to a string.
     */
    public void encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        // Use base64 to encode the picture.
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        encoded_picture = imageEncoded;
    }

    // TODO: comment vanaf hier verder.
    public void getLocation() {

        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();

                            if (!(task.getResult() == null)) {
                                latitude = currentLocation.getLatitude();
                                longitude = currentLocation.getLongitude();
                            }

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(AdvertActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    public void showSettingAlert()
    {
        String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (locationProviders == null || locationProviders.equals("")) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("GPS setting!");
            alertDialog.setMessage("GPS is not enabled, Do you want to go to settings menu? ");
            alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    AdvertActivity.this.startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
    }

    public void makeAdvert(View view) {

        description = get_description.getText().toString();
        dog_name = get_dog_name.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getCurrentUser().getUid();
        Log.w("userid", id);
        Dog aDog;
        aDog = new Dog(dog_name, description, encoded_picture, latitude, longitude, id);

        Log.w("NAMEE", dog_name + " " + description);

        databaseReference.child("users").child(id).child("advert_status").setValue(true);

        databaseReference.child("dogs").child(id).child("dog").setValue(aDog);

        Intent intent = new Intent(AdvertActivity.this, ConfirmActivity.class);
        intent.putExtra("name", dog_name);
        intent.putExtra("photo", encoded_picture);
        startActivity(intent);
        finish();
    }
}