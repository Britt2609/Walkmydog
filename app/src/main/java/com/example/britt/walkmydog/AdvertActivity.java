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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import static java.lang.Double.valueOf;

public class AdvertActivity extends AppCompatActivity {

    Spinner spinner;
    String dogName;
    String description;
    String id;

    ImageView photo;

    // Initialize for database.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String TAG = "firebase";

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
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(imageBitmap);
        }
    }

    public void getLocation()throws SecurityException {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // this listener checks what happens with the locationservice
        locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(Location location) {
                // this method is triggered when a user is detected in new location
                Log.d("gps", "updated");

                // get coordinates of user
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                tekst.setText(latitude + "   " + longitude);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // necessity
                Log.w("TAGG", " HELLOOOO");
            }

            @Override
            public void onProviderEnabled(String provider) {
                // necessity
                Log.w("TAGG", " HOOOOOOOOI");

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.w("TAGG", " HAAI");


                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }


    public void makeAdvert(View view) {


        description = dog_description.getText().toString();
        dogName = dog_name.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getCurrentUser().getUid();
        Log.w("userid", id);
        Dog aDog;
        aDog = new Dog(dogName, description, latitude, longitude);

        Log.w("NAMEE", dogName + " " + description);

        databaseReference.child("types").child("owner").child(id).child("dog").setValue(aDog);

        Intent intent = new Intent(AdvertActivity.this, ConfirmActivity.class);
//        intent.putExtra("photo", photo);
        intent.putExtra("dogName", dogName);
        startActivity(intent);
    }

    public void goToNext(View view) {
        Intent intent = new Intent(AdvertActivity.this, ConfirmActivity.class);
        startActivity(intent);
    }
}