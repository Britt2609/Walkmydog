package com.example.britt.walkmydog;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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

    Spinner spinner;
    String dogName;
    String description;
    String id;
    String picture;

    ImageView photo;

    // Initialize for database.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    int myCameraRequestCode = 100;
    int myLocationRequestCode = 100;

    private DatabaseReference databaseReference;

    double longitude = 0.0000 ;
    double latitude = 0.0000 ;

    Boolean boolCamera = false;
    Boolean boolLocation = true;

    TextView tekst;

    private LocationListener locationListener;
    private LocationManager locationManager;

    EditText dog_name;
    EditText dog_description;

    Bitmap imageBitmap;

    private static final String TAG = "MapActivity";

//    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
//    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
//    private static final float DEFAULT_ZOOM = 15f;

    //vars
    private Boolean mLocationPermissionsGranted = false;
//    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

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

        dog_name = findViewById(R.id.dogName);
        dog_description = findViewById(R.id.description);

        photo = findViewById(R.id.Photo);

        tekst = findViewById(R.id.useLogo);

        if (ActivityCompat.checkSelfPermission(AdvertActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AdvertActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, myLocationRequestCode);
        }
        else {
            Log.d("TAGG", "LOCATIEEEE");
            mLocationPermissionsGranted = true;
            getLocation();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Give selected category to next activity and go to next activity.
     */
    public void SelectOption(View view) {
        String option = spinner.getSelectedItem().toString();
        if (option.equals("Log Out")) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AdvertActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void makePicture(View view) {
        // Here, thisActivity is the current activity

        boolCamera = true;
        boolLocation = false;

        if (ActivityCompat.checkSelfPermission(AdvertActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AdvertActivity.this, new String[]{android.Manifest.permission.CAMERA}, myCameraRequestCode);
        }
        else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (boolLocation) {
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


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(imageBitmap);
            encodeBitmapAndSaveToFirebase(imageBitmap);
        }
    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        picture = imageEncoded;
    }

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

                            latitude = currentLocation.getLatitude();
                            longitude = currentLocation.getLongitude();

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



    public void makeAdvert(View view) {

        description = dog_description.getText().toString();
        dogName = dog_name.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getCurrentUser().getUid();
        Log.w("userid", id);
        Dog aDog;
        aDog = new Dog(dogName, description, picture, latitude, longitude, id);

        Log.w("NAMEE", dogName + " " + description);

        databaseReference.child("users").child(id).child("advert_status").setValue(true);

        databaseReference.child("dogs").child(id).child("dog").setValue(aDog);

        Intent intent = new Intent(AdvertActivity.this, ConfirmActivity.class);
//        intent.putExtra("photo", picture);
        intent.putExtra("name", dogName);
        intent.putExtra("photo", picture);
        startActivity(intent);
        finish();
    }
}