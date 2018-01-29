package com.example.britt.walkmydog;

import android.*;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DogActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Initialize layout.
    Spinner spinner;
    TextView nameText;
    TextView descriptionText;
    ImageView picture;

    // Initialize dog's data.
    String bossID;
    Double lat;
    Double lon;
    String dog;
    String id;
    Dog mDog;

    // Initialize for user's data.
    Double latitude;
    Double longitude;
    User mUser;

    // Set values for use of location.
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    LatLng location;
    int myLocationRequestCode = 100;
    private static final float DEFAULT_ZOOM = 15f;

    // Initialize for database.
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog);

        // Initialize layout views
        nameText = findViewById(R.id.dogName);
        descriptionText = findViewById(R.id.description);
        picture = findViewById(R.id.photo);
        spinner = findViewById(R.id.spinnerOptions4);

        // Initialize for database.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getCurrentUser().getUid();

        // Set spinner to be able to choose option.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_doginfo_contact,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get data from intent.
        Intent intent = getIntent();
        bossID = intent.getStringExtra("bossID");

        // Get dog's data from database.
        getFromDB();
    }


    /**
     * Create map when all data for map is ready.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Initialize map for layout.
        mMap = googleMap;

        // Check if location is available.
        if (lat == 0 && lon == 0) {
            Toast.makeText(this, "location not found", Toast.LENGTH_SHORT).show();
        }

        // If location available, add marker to map of location.
        else {
            location = new LatLng(lat, lon);

            mMap.addMarker(new MarkerOptions().position(location)
                    .title("Location of the dog's boss"));

            // Zoom to the location.
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,
                    DEFAULT_ZOOM));
        }
        if (ActivityCompat.checkSelfPermission(DogActivity.this,
              android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DogActivity.this,
              new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, myLocationRequestCode);
        }
        showSettingAlert();
        Log.d("TAGG", "LOCATIEEEE ");
        mLocationPermissionsGranted = true;
        getLocation();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 21));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }


    /**
     * Go to selected option in spinner.
     */
    public void SelectOption(View view) {
        String option = spinner.getSelectedItem().toString();

        // Log out when button is clicked.
        if (option.equals("Log uit")) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DogActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Go to overview when button is clicked.
        else if (option.equals("Uitgelaten honden")) {
            Intent intent = new Intent(DogActivity.this, OverviewActivity.class);
            startActivity(intent);
            finish();
        }

        // Go to adverts when button is clicked.
        else if (option.equals("Advertenties")) {
            Intent intent = new Intent(DogActivity.this, ChooseActivity.class);
            startActivity(intent);
            finish();
        }
    }


    /**
     * Get information about the dog and show in layout.
     */
    public void getFromDB() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get dog's information from database.
                mDog = dataSnapshot.child("dogs").child(bossID).child("dog").getValue(Dog.class);
                dog = mDog.name;

                // Set data in layout.
                nameText.setText(dog);
                descriptionText.setText(mDog.description);
                getImage(mDog.photo, picture);
                lat = mDog.lat;
                lon = mDog.lon;

                // When location is set, initialize map.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(DogActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("value failure: ", "Failed to read value.");
            }
        });
    }


    /**
     * Add dog to favorites when appointment is made.
     */
    public void setFavorites() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get current user's data.
                mUser = dataSnapshot.child("users").child(id).getValue(User.class);

                // Get current favorites list.
                ArrayList<Dog> favo;
                favo = mUser.favorites;

                // If list is not empty, add the dog.
                if (favo != null) {
                    if (!favo.contains(mDog)) {
                        favo.add(mDog);
                    }
                }

                // If list is empty, create a list and add the dog.
                else {
                    Log.d("else array", mDog.getName());
                    favo = new ArrayList<>();
                    favo.add(mDog);
                }

                // Update favorites in database.
                databaseReference.child("users").child(id).child("favorites").setValue(favo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("value failure: ", "Failed to read value.");
            }
        });
    }


    /**
     * Decode string to image with use of base 64.
     */
    static void getImage(String photo, ImageView picture) {
        // Check if picture is available.
        if (photo != null) {
            byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            // Set picture to ImageView.
            picture.setImageBitmap(decodedByte);
        }
    }


    /**
     * Go to next activity to get contact information.
     * Set dog in favorites list.
     */
    public void makeAppointment(View view) {
        Intent intent = new Intent(DogActivity.this, ContactActivity.class);
        setFavorites();
        intent.putExtra("bossID", bossID);
        intent.putExtra("dog", dog);
        startActivity(intent);
    }


    /**
     * Get location of current user.
     */
    public void getLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){
                Toast.makeText(DogActivity.this, "Waiting for current location",
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
                            Toast.makeText(DogActivity.this,
                                    "unable to get current location",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        catch (SecurityException e){
            Log.e("location", "getDeviceLocation: SecurityException: " + e.getMessage() );
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
                            DogActivity.this.startActivity(intent);
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
     * Gets information/result out of location or camera request.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // If permission granted and location determined, get current location of user.
        if (requestCode == myLocationRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "location permission granted",
                        Toast.LENGTH_LONG).show();
                getLocation();
            } else {
                Toast.makeText(this, "location permission denied",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
