package com.example.britt.walkmydog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import static com.example.britt.walkmydog.AdvertActivity.setSpinner;
import static com.example.britt.walkmydog.DogActivity.getImage;

public class ConfirmActivity extends AppCompatActivity {

    // Initialize dog data.
    String picture;
    String dogNameText;

    // Initialize for layout.
    ImageView photo;
    TextView dogName;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        // Get layout views.
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
