package com.example.britt.walkmydog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import static com.example.britt.walkmydog.AdvertActivity.setSpinner;

public class OverviewActivity extends AppCompatActivity {

    // Initialize for firebase.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;

    Spinner spinner;
    ListView dogList;

    // Initialize user data.
    String id;

    // Initialize for use of listview.
    ArrayList<Dog> dogArray = new ArrayList<>();
    DogAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        checkAuthentication();

        spinner = findViewById(R.id.spinnerOptions6);
        dogList = findViewById(R.id.dogList);

        // Set spinner to be able to choose option.
        setSpinner(spinner, this, R.array.spinner_overview);

        // Get database ready to use.
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        id = user.getUid();

        // Get current users favorites from database.
        getFromDB();

        // Set listener on dogs to go to activity with dog's information.
        dogList.setOnItemClickListener(new OnItemClickListener());
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
                    Intent intent = new Intent(OverviewActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
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
                Intent intent = new Intent(OverviewActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            // Go to adverts when button is clicked.
            case ("Advertenties"):
                Intent intent2 = new Intent(OverviewActivity.this, ChooseActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }


    /**
     * Get data of all favorite dogs of user.
     */
    public void getFromDB() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Iterate over all adverts of favorite dogs.
                Iterator<DataSnapshot> ds = dataSnapshot.child("users").child(id)
                        .child("favorites").getChildren().iterator();
                while (ds.hasNext()) {

                    // Add al favorite dogs to an array.
                    DataSnapshot ds1 = ds.next();
                    Dog mDog = ds1.getValue(Dog.class);
                    dogArray.add(mDog);
                }

                // Show dogs in ListView with custom adapter.
                mAdapter = new DogAdapter(OverviewActivity.this, dogArray);
                dogList.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(eventListener);
    }


    /**
     * Dialog to ask user confirmation to delete al dogs out of favorite list.
     */
    public void clearList(View view) {

        // Show dialog to confirm deletion of all dogs in list.
        AlertDialog.Builder builder = new AlertDialog.Builder(OverviewActivity.this);
        builder.setMessage("Wilt u deze lijst leeg maken?").setTitle("Doorgaan");

        // Delete dogs from list.
        builder.setPositiveButton("Honden verwijderen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                deleteFavoritesFromDB();
                Toast.makeText(OverviewActivity.this, "Dogs deleted from list!", Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });

        // Cancel deletion.
        builder.setNeutralButton("Annuleren", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * Get data of selected dog and go to next activity with the selected dog's current data.
     */
    private class OnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // Get selected dog.
            Dog dog = (Dog) parent.getItemAtPosition(position);
            String ownerID = dog.id;

            // Go to next activity and give the owner's id to intent.
            Intent intent = new Intent(OverviewActivity.this, DogActivity.class);
            intent.putExtra("ownerID", ownerID);
            startActivity(intent);
        }
    }


    /**
     * Delete all dogs from favorites list.
     */
    public void deleteFavoritesFromDB() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get current user's data.
                User user = dataSnapshot.child("users").child(id).getValue(User.class);

                // Set favorites list empty in database.
                user.favorites = null;
                dogArray = new ArrayList<Dog>();
                databaseReference.child("users").child(id).setValue(user);

                // Update ListView in current activity.
                mAdapter = new DogAdapter(OverviewActivity.this, dogArray);
                dogList.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(eventListener);
    }
}

