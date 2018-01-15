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
    private static final String TAG = "firebase";

    private DatabaseReference databaseReference;

    // Initialize user data.
    String email;
    String password;
    String id;

    String type;

    Button login;
    Button useremail;

    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set listeners on buttons for logging in and go to creating an account.
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        useremail = findViewById(R.id.make_account);
        useremail.setOnClickListener(this);

        // Set AuthStateListener to make sure only logged in users can go to next activity.
        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signedIn" + user.getUid());
                    Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d(TAG, "onAuthStateChanged:signedIn");
                }
            }
        };

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signedIn" + user.getUid());
                    Intent intent = new Intent(MainActivity.this, AdvertActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d(TAG, "onAuthStateChanged:signedIn");
                }
            }
        };
    }
    public void getFromDB() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.child("users").child(id).getValue(User.class);

                if (mUser.userType.equals("owner")) {
                    Intent intent = new Intent(MainActivity.this, AdvertActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (mUser.userType.equals("walker")){
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



    /**
     * Logs in user with email and password.
     */
    public void logIn() {

        EditText get_email = findViewById(R.id.getEmail);
        EditText get_password = findViewById(R.id.getPassword);

        email = get_email.getText().toString();
        password = get_password.getText().toString();

        // Check if email and password are filled in.
        if (email.equals("")) {
            Toast.makeText(MainActivity.this, "Vul aub een emailadres in!",
                    Toast.LENGTH_SHORT).show();
        }
        else if (password.equals("")) {
            Toast.makeText(MainActivity.this, "Vul aub een wachtwoord in!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information and go to next activity.
                                Log.d(TAG, "signInWithEmail:success");

                                mAuth = FirebaseAuth.getInstance();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.w("USERR", "User " + user);
                                id = user.getUid();


                                getFromDB();


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Email en wachtwoord kloppen niet!",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }
    }

    public void onClick(View v) {
        // Set onClick to be able to log in or create an account.
        switch (v.getId()) {
            case R.id.login:
                logIn();
                break;

            case R.id.make_account:
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void goToNext(View view) {
        Intent intent = new Intent(MainActivity.this, AdvertActivity.class);
        startActivity(intent);
    }

}
