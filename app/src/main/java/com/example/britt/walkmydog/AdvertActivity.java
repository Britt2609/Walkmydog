package com.example.britt.walkmydog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

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

    private DatabaseReference databaseReference;

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

        EditText dog_name = findViewById(R.id.dogName);
        dogName = dog_name.getText().toString();
        EditText dog_description = findViewById(R.id.description);
        description = dog_description.getText().toString();

        photo = findViewById(R.id.Photo);
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
        startActivity(intent);
    }

    public void makeAdvert(View view) {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//             ODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
//
//        <!DOCTYPE html>
//    <html>
//      <head>
//        <title>Geolocation</title>
//        <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
//        <meta charset="utf-8">
//        <style>
//          /* Always set the map height explicitly to define the size of the div
//           * element that contains the map. */
//          #map {
//                height: 100%;
//            }
//          /* Optional: Makes the sample page fill the window. */
//            html, body {
//                height: 100%;
//                margin: 0;
//                padding: 0;
//            }
//        </style>
//      </head>
//      <body>
//
//         <div id="map"></div>
//        <script>
//                // Note: This example requires that you consent to location sharing when
//                // prompted by your browser. If you see the error "The Geolocation service
//                // failed.", it means you probably did not give permission for the browser to
//                // locate you.
//                var map, infoWindow;
//        function initMap() {
//            map = new google.maps.Map(document.getElementById('map'), {
//                    center: {lat: -34.397, lng: 150.644},
//            zoom: 6
//        });
//            infoWindow = new google.maps.InfoWindow;
//
//            // Try HTML5 geolocation.
//            if (navigator.geolocation) {
//                navigator.geolocation.getCurrentPosition(function(position) {
//                    var pos = {
//                            lat: position.coords.latitude,
//                            lng: position.coords.longitude
//            };
//
//                    infoWindow.setPosition(pos);
//                    infoWindow.setContent('Location found.');
//                    infoWindow.open(map);
//                    map.setCenter(pos);
//                }, function() {
//                    handleLocationError(true, infoWindow, map.getCenter());
//                });
//            } else {
//                // Browser doesn't support Geolocation
//                handleLocationError(false, infoWindow, map.getCenter());
//            }
//        }
//
//        function handleLocationError(browserHasGeolocation, infoWindow, pos) {
//            infoWindow.setPosition(pos);
//            infoWindow.setContent(browserHasGeolocation ?
//                    'Error: The Geolocation service failed.' :
//                    'Error: Your browser doesn\'t support geolocation.');
//            infoWindow.open(map);
//        }
//    </script>
//    <script async defer
//                src="https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyDPGmCEKyE0HstkO90nCP47yTA_RucStsk&callback=initMap">
//    </script>
//  </body>
//</html>

        id = mAuth.getCurrentUser().getUid();
        Log.w("userid", id);
        Dog aDog;
        aDog = new Dog(dogName, description, photo, latitude, longitude);
        databaseReference.child("owner").child(id).child("dog").setValue(aDog);

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
