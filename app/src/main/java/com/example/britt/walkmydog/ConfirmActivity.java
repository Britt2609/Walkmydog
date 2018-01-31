package com.example.britt.walkmydog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.britt.walkmydog.AdvertActivity.setSpinner;
import static com.example.britt.walkmydog.DogActivity.getImage;

public class ConfirmActivity extends AppCompatActivity {

    String picture;
    String dogNameText;

    ImageView photo;
    TextView dogName;
    Spinner spinner;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        checkAuthentication();

        spinner = findViewById(R.id.spinnerOptions2);
        dogName = findViewById(R.id.dogName);
        photo = findViewById(R.id.photo);


        // Set spinner to be able to choose option.
        setSpinner(spinner, this, R.array.spinner_confirm);

        // Get data from intent en set to layout.
        Intent intent = getIntent();
        picture = intent.getStringExtra("photo");
        dogNameText = intent.getStringExtra("name");
        dogName.setText(dogNameText);
        getImage(picture, photo);
    }


    /**
     * Check if user is signed and has access to this activity.
     */
    public void checkAuthentication() {
        // Check if user is signed in.
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(ConfirmActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }


    /**
     * Give selected category to next activity and go to next activity.
     */
    public void SelectOption(View view) {
        String option = spinner.getSelectedItem().toString();
        switch(option) {
            // Log out when button is clicked.
            case ("Log uit"):
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ConfirmActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;


            // Go to change advert when button is clicked.
            case ("Verander advertentie"):
                Intent intent2 = new Intent(ConfirmActivity.this, AdvertActivity.class);
                startActivity(intent2);
                finish();
                break;

        }
    }
}
