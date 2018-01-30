package com.example.britt.walkmydog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.britt.walkmydog.AdvertActivity.setSpinner;

public class ContactActivity extends AppCompatActivity {

    // Initialize for layout.
    Spinner spinner;
    TextView bossName;
    TextView dogName;
    TextView email;

    // Initialize user data.
    String bossID;
    String dog;
    User mUser;

    // Initialize for database.
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Get layout views.
        bossName = findViewById(R.id.bossName);
        dogName = findViewById(R.id.dogName);
        email = findViewById(R.id.bossEmail);
        spinner = findViewById(R.id.spinnerOptions5);


        // Set spinner to be able to choose option.
        setSpinner(spinner, this, R.array.spinner_doginfo_contact);

        // Get data from intent.
        Intent intent = getIntent();
        bossID = intent.getStringExtra("bossID");
        dog = intent.getStringExtra("dog");

        // Get data from database for layout.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getFromDB();
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
                Intent intent = new Intent(ContactActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            // Go to overview when button is clicked.
            case ("Uitgelaten honden"):
                Intent intent2 = new Intent(ContactActivity.this, OverviewActivity.class);
                startActivity(intent2);
                finish();
                break;

            // Go to adverts when button is clicked.
            case ("Advertenties"):
                Intent intent3 = new Intent(ContactActivity.this, ChooseActivity.class);
                startActivity(intent3);
                finish();
                break;
        }
    }


    /**
     * Get data from the dog's boss.
     */
    public void getFromDB() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the boss's data from database.
                mUser = dataSnapshot.child("users").child(bossID).getValue(User.class);

                // Set values in layout.
                bossName.setText("Naam baasje: " + mUser.name);
                email.setText("Email baasje: " + mUser.email);
                dogName.setText("Maak contact met het baasje van " + dog);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("value failure: ", "Failed to read value.");
            }
        });
    }
}
