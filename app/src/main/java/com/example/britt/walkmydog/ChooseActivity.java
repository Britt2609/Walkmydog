package com.example.britt.walkmydog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class ChooseActivity extends AppCompatActivity {

    // Initialize layout.
    Spinner spinner;
    ListView dogList;

    // Initialize user data.
    String id;

    // Initialize for database.
    private DatabaseReference databaseReference;

    // Initialize for use of listview.
    ArrayList<Dog> dogArray = new ArrayList<>();
    DogAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        // Get layout views.
        dogList = findViewById(R.id.dogList);
        spinner = findViewById(R.id.spinnerOptions3);

        // Set spinner to be able to choose option.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_choose, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get information out of database.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getFromDB();

        // Set listener on all dogs.
        dogList.setOnItemClickListener(new OnItemClickListener());
    }


    /**
     * Go to selected option in spinner.
     */
    public void SelectOption(View view) {
        String option = spinner.getSelectedItem().toString();

        // Log out when button is clicked.
        if (option.equals("Log uit")) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Go to overview when button is clicked.
        else if (option.equals("Overview"))
        {
            Intent intent = new Intent(ChooseActivity.this, OverviewActivity.class);
            startActivity(intent);
            finish();
        }
    }


    /**
     * Get data of all dogs.
     */
    public void getFromDB() {

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Iterate over all adverts of dogs.
                Iterator<DataSnapshot> ds = dataSnapshot.child("dogs").getChildren().iterator();
                while (ds.hasNext()) {

                    // Add al dogs to an array.
                    DataSnapshot ds1 = ds.next();
                    Dog mDog = ds1.child("dog").getValue(Dog.class);
                    dogArray.add(mDog);
                }

                // Show dogs in ListView with custom adapter.
                mAdapter = new DogAdapter(ChooseActivity.this, dogArray);
                dogList.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        databaseReference.addValueEventListener(eventListener);
    }

    /**
     * Get data of selected dog and go to next activity with the selected dog's data.
     */
    private class OnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // Get selected dog.
            Dog dog = (Dog) parent.getItemAtPosition(position);
            String bossID = dog.getId();

            // Go to next activity and give the boss's id to intent.
            Intent intent = new Intent(ChooseActivity.this, DogActivity.class);
            intent.putExtra("bossID", bossID);
            startActivity(intent);
        }
    }
}
