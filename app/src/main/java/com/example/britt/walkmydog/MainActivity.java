package com.example.britt.walkmydog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Initialize for database.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;

    // Set tag for logs.
    private static final String TAG = "firebase";

    // Initialize user data.
    String email;
    String password;
    String id;
    String type;

    // Initialize layout.
    Button login;
    Button userEmail;
    EditText getEmail;
    EditText getPassword;

    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set listeners on buttons for logging in and go to creating an account.
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        userEmail = findViewById(R.id.makeAccount);
        userEmail.setOnClickListener(this);

        // Set AuthStateListener to make sure only logged in users can go to next activity.
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get input of user.
        getEmail = findViewById(R.id.getEmail);
        getPassword = findViewById(R.id.getPassword);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    id = user.getUid();
                    sentToNext();
                } else {
                    Log.d(TAG, "onAuthStateChanged:signedIn");
                }
            }
        };


    }


    /**
     * Log in or go to next activity to create account.
     */
    public void onClick(View v) {

        // Set onClick to be able to log in or create an account.
        switch (v.getId()) {
            case R.id.login:
                logIn();
                break;

            case R.id.makeAccount:

                // Go to next activity to register.
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    /**
     * Logs in user with email and password and adds to firebase.
     */
    public void logIn() {

        // Get input from user.
        email = getEmail.getText().toString();
        password = getPassword.getText().toString();

        // Check if email and password are filled in.
        if (email.equals("") || password.equals("")) {
            Toast.makeText(MainActivity.this, "Vul aub alle velden in!",
                    Toast.LENGTH_SHORT).show();
        }

        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Sign in success, update UI with the signed-in user's information and go to next activity.
                            if (task.isSuccessful()) {

                                // Get current user id.
                                mAuth = FirebaseAuth.getInstance();
                                FirebaseUser user = mAuth.getCurrentUser();
                                id = user.getUid();

                                // Get type from database and sent user to next activity.
                                sentToNext();


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Email/wachtwoord klopt niet!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /**
     * Get user data from database and sent user to next corresponding activity
     */
    public void sentToNext() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("hamster", "" + id);
                mUser = dataSnapshot.child("users").child(id).getValue(User.class);
                type = mUser.userType;

                // Check which type the current user is and go to corresponding next activity.
                if (type.equals("owner")) {
                    // Sent owner to activity to make an advert.
                    Intent intent = new Intent(MainActivity.this, AdvertActivity.class);
                    startActivity(intent);
                    finish();
                }

                else if (type.equals("walker")){
                    // Sent walker to activity with an overview of adverts.
                    Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
                    startActivity(intent);
                    finish();
                }

                else {
                    Toast.makeText(MainActivity.this, "Type is fout!!!!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("value failure: ", "Failed to read value.");
            }
        });
    }


    // TODO: commenten
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }
}
