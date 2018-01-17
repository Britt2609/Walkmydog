package com.example.britt.walkmydog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ConfirmActivity extends AppCompatActivity {

    // Initialize for database.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String TAG = "firebase";

    private DatabaseReference databaseReference;

    String id;


    User mUser;


    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);


        // Set spinner to be able to choose category.
        spinner = findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_confirm,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

//    public void getFromDB() {
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                mUser = dataSnapshot.child("dogs").child(id).child("dog").getValue("picture");
//
//                if (mUser.userType.equals("owner")) {
//
//                }
//                else if (mUser.userType.equals("walker")){
//                    Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//                else {
//                    Toast.makeText(MainActivity.this, "Type is fout!!!!",
//                            Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("value failure: ", "Failed to read value.");
//            }
//        });
//    }

    /**
     * Give selected category to next activity and go to next activity.
     */
    public void SelectOption(View view) {
        String option = spinner.getSelectedItem().toString();
        if(option.equals("Log Out"))
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ConfirmActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (option.equals("Change advert"))
        {
            Intent intent = new Intent(ConfirmActivity.this, AdvertActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
