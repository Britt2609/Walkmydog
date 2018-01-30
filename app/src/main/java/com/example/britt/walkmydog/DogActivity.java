package com.example.britt.walkmydog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.britt.walkmydog.AdvertActivity.setSpinner;

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
    User mUser;

    // Set values for use of location.
    private GoogleMap mMap;
    LatLng location;
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
        setSpinner(spinner, this, R.array.spinner_doginfo_contact);

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
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }


    /**
     * Go to selected option in spinner.
     */
    public void SelectOption(View view) {
        String option = spinner.getSelectedItem().toString();
        switch(option) {
            // Log out when button is clicked.
            case ("Log uit"):
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DogActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            // Go to overview when button is clicked.
            case ("Uitgelaten honden"):
                Intent intent2 = new Intent(DogActivity.this, OverviewActivity.class);
                startActivity(intent2);
                finish();
                break;

            // Go to adverts when button is clicked.
            case ("Advertenties"):
                Intent intent3 = new Intent(DogActivity.this, ChooseActivity.class);
                startActivity(intent3);
                finish();
                break;
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
}
