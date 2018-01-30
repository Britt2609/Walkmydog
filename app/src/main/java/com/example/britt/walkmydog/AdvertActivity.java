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
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.Calendar;

import static com.example.britt.walkmydog.DogActivity.getImage;
import static java.lang.Double.valueOf;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.database.ValueEventListener;


public class AdvertActivity extends AppCompatActivity {

    // Initialize dog data.
    String dogName;
    String description;
    String encodedPicture;

    // Initialize user data.
    Boolean advertState;
    String id;
    User mUser;
    Dog mDog;

    // Initialize for layout.
    ImageView getPicture;
    Spinner spinner;
    EditText getDogName;
    EditText getDescription;
    Button makeAdvert;

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

        // Get layout views.
        getDogName = findViewById(R.id.dogName);
        getDescription = findViewById(R.id.description);
        getPicture = findViewById(R.id.picture);
        spinner = findViewById(R.id.spinnerOptions1);
        makeAdvert = findViewById(R.id.makeAdvert);

        // Set spinner to choose option.
        setSpinner(spinner, this, R.array.spinner_advert);

        getLocationPermission();

        // Set database ready to use.
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        id = mAuth.getCurrentUser().getUid();

        // Get current dog from user when existing.
        getDogFromDB();
    }


    /**
     * Set spinner with corresponding options for several activities.
     */
    static void setSpinner(Spinner spinner, Context context, Integer spinner_array) {
        // Set spinner to be able to choose an option to go to.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    /**
     * If not already given, ask for permission to use location.
     * If not already turned on, ask user to turn on location.
     */
    public void getLocationPermission() {
        // Get permission to use location.
        if (ActivityCompat.checkSelfPermission(AdvertActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AdvertActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, myLocationRequestCode);
            showSettingAlert();
        }

        else {
            if (boolLocation) {
                showSettingAlert();
            }
            mLocationPermissionsGranted = true;
            getLocation();
        }
    }


    /**
     * Go to selected option in spinner.
     */
    public void SelectOption(View view) {
        // Log out when button is clicked.
        String option = spinner.getSelectedItem().toString();

        if (option.equals("Log uit")) {
            // Sign out in firebase.
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AdvertActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    /**
     * Get current advert from database when already existing.
     */
    public void getDogFromDB() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 // Get current user and get advert state.
                 mUser = dataSnapshot.child("users").child(id).getValue(User.class);
                 advertState = mUser.advertState;

                 // Check if current user already made an advert.
                 if (advertState) {
                     Log.w("Tagg", "verander bestaande advertentie");

                     // Get dog of current user.
                     mDog = dataSnapshot.child("dogs").child(id).child("dog").getValue(Dog.class);

                     // Set current data of dog in layout.
                     getImage(mDog.photo, getPicture);
                     encodedPicture = mDog.photo;
                     getDogName.setText(mDog.name);
                     getDescription.setText(mDog.description);
                 }
                 else {
                     Toast.makeText(AdvertActivity.this, "Maak een advertentie aan",
                             Toast.LENGTH_SHORT).show();
                 }
             }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("value failure: ", "Failed to read value.");
            }
        });
    }


    /**
     * User is able to take a picture of the dog.
     */
    public void makePicture(View view) {
        // Set booleans for onRequestPermissionResult.
        boolCamera = true;
        boolLocation = false;

        // If not already given, ask for permission to use camera.
        if (ActivityCompat.checkSelfPermission(AdvertActivity.this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AdvertActivity.this,
                    new String[]{android.Manifest.permission.CAMERA}, myCameraRequestCode);
        }

        // If permission already given, go to camera.
        else {
            dispatchTakePictureIntent();
        }
    }


    /**
     * Gets information/result out of location or camera request.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check if result is asked of location or camera.
        checkLocation(requestCode, grantResults);
        checkCamera(requestCode, grantResults);
    }


    /**
     * Check if result is asked of location.
     * If so, get location.
     */
    public void checkLocation(int requestCode, @NonNull int[] grantResults) {
        if (boolLocation) {

            // If permission granted and location determined, get current location of user.
            if (requestCode == myLocationRequestCode) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "location permission granted",
                            Toast.LENGTH_LONG).show();
                    getLocation();
                }
                else {
                    Toast.makeText(this, "location permission denied",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    /**
     * Check if result is asked of camera.
     * If so, go to camera.
     */
    public void checkCamera(int requestCode, @NonNull int[] grantResults) {
        if (boolCamera) {
            // If permission granted, go to camera.
            if (requestCode == myCameraRequestCode) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "camera permission granted",
                            Toast.LENGTH_LONG).show();
                    dispatchTakePictureIntent();

                }
                else {
                    Toast.makeText(this, "camera permission denied",
                            Toast.LENGTH_LONG).show();
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
            getPicture.setImageBitmap(imageBitmap);

            // Encode the picture and save the picture to database.
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
        encodedPicture = imageEncoded;
    }


    /**
     * Get current latitude and longitude of the dog's boss.
     */
    public void getLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){
                makeAdvert.setEnabled(false);
                Toast.makeText(AdvertActivity.this, "Waiting for current location",
                        Toast.LENGTH_SHORT).show();

                // Get last known location of device.
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();

                            if (!(task.getResult() == null)) {

                                // Set longitude and latitude given.
                                latitude = currentLocation.getLatitude();
                                longitude = currentLocation.getLongitude();
                            }
                        }
                        else{
                            Toast.makeText(AdvertActivity.this,"unable to " +
                                            "get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                makeAdvert.setEnabled(true);
            }
        }
        catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }


    /**
     * Show a dialog when GPS is disabled to change GPS settings.
     */
    public void showSettingAlert()
    {
        // Check if GPS is enabled.
        String locationProviders = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (locationProviders == null || locationProviders.equals("")) {

            // Build a dialog with the choice to change GPS settings.
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("GPS instellingen!");
            alertDialog.setMessage("GPS staat niet aan, wil je naar instellingen? ");
            alertDialog.setPositiveButton("Instellingen",
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Go to settings menu.
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    AdvertActivity.this.startActivity(intent);
                }
            });

            // Close dialog without turning on gps.
            alertDialog.setNegativeButton("Weigeren", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
    }


    /**
     * Put advert in database and go to next activity when button clicked.
     */
    public void makeAdvert(View view) {

        // Get user input.
        description = getDescription.getText().toString();
        dogName = getDogName.getText().toString();

        // Get dog's data from database.
        mAuth = FirebaseAuth.getInstance();

        Dog aDog;
        aDog = new Dog(dogName, description, encodedPicture, latitude, longitude, id);

        // Change advert status and set dog into database.
        databaseReference.child("users").child(id).child("advert_status").setValue(true);
        databaseReference.child("dogs").child(id).child("dog").setValue(aDog);

        // Go to next activity to confirm the processing of the advert.
        Intent intent = new Intent(AdvertActivity.this, ConfirmActivity.class);
        intent.putExtra("name", dogName);
        intent.putExtra("photo", encodedPicture);
        startActivity(intent);
    }
}