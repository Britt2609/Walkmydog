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

public class ContactActivity extends AppCompatActivity {

    // Initialize for layout.
    Spinner spinner;
    TextView boss_name;
    TextView dog_name;
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
        boss_name = findViewById(R.id.bossName);
        dog_name = findViewById(R.id.dogName);
        email = findViewById(R.id.bossEmail);
        spinner = findViewById(R.id.spinnerOptions5);

        // Set spinner to be able to choose option.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_doginfo_contact,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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

        // Log out when button is clicked.
        if(option.equals("Log uit"))
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ContactActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Go to overview when button is clicked.
        else if (option.equals("Uitgelaten honden"))
        {
            Intent intent = new Intent(ContactActivity.this, OverviewActivity.class);
            startActivity(intent);
            finish();
        }

        // Go to adverts when button is clicked.
        else if (option.equals("Advertenties"))
        {
            Intent intent = new Intent(ContactActivity.this, ChooseActivity.class);
            startActivity(intent);
            finish();
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
                boss_name.setText("Naam baasje: " + mUser.name);
                email.setText("Email baasje: " + mUser.email);
                dog_name.setText("Maak contact met het baasje van " + dog);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("value failure: ", "Failed to read value.");
            }
        });
    }
}
