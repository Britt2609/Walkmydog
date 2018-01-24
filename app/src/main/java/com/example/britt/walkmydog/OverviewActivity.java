package com.example.britt.walkmydog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class OverviewActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String TAG = "firebase";

    Spinner spinner;

    String id;

    ListView dogList;

    private DatabaseReference databaseReference;

    ArrayList<Dog> dogArray = new ArrayList<>();

    DogAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        dogList = findViewById(R.id.dogList);

        // Set spinner to be able to choose category.
        spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_overview,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = mAuth.getCurrentUser();
        id = user.getUid();

        getFromDB();

        dogList.setOnItemClickListener(new OnItemClickListener());
    }

    public void SelectOption(View view) {
        String option = spinner.getSelectedItem().toString();
        if(option.equals("Log Out"))
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(OverviewActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (option.equals("Adverts"))
        {
            Intent intent = new Intent(OverviewActivity.this, ChooseActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void getFromDB() {

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> ds = dataSnapshot.child("users").child(id).child("favorites").getChildren().iterator();
                while (ds.hasNext()) {

                    DataSnapshot ds1 = ds.next();

                    Dog mDog = ds1.getValue(Dog.class);

                    dogArray.add(mDog);

                }
                mAdapter = new DogAdapter(OverviewActivity.this, dogArray);
                dogList.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(eventListener);
    }

    private class OnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Dog dog = (Dog) parent.getItemAtPosition(position);

            String bossID = dog.getId();
            Log.w("TAGG", "hoi " + bossID);

            Intent intent = new Intent(OverviewActivity.this, DogActivity.class);
            intent.putExtra("bossID", bossID);
            startActivity(intent);
        }
    }

}
