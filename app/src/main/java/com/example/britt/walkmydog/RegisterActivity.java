package com.example.britt.walkmydog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {


    // Initialize for database.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String TAG = "firebase";

    private DatabaseReference databaseReference;

    // Initialize user data.
    String email;
    String password;
    String password2;
    String name;
    String id;
    String type = "";

    Button goBack;

    EditText get_email;
    EditText get_password;
    EditText get_password2;
    EditText get_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        goBack = findViewById(R.id.goBack);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        get_email = findViewById(R.id.getEmail);
        get_password = findViewById(R.id.getPassword);
        get_password2 = findViewById(R.id.getPassword2);
        get_name = findViewById(R.id.getName);
        }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.owner:
                if (checked)
                    type = "owner";
                    break;
            case R.id.walker:
                if (checked)
                    type = "walker";
                    break;
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }

    /**
     * Creates user with email and password.
     */
    public void createUser(View view) {

        email = get_email.getText().toString();
        password = get_password.getText().toString();
        password2 = get_password2.getText().toString();
        name = get_name.getText().toString();

        // Check if email and password are filled in.
        if (email.equals("") || name.equals("") || password.equals("") || type.equals("")) {
            Toast.makeText(RegisterActivity.this, "Vul aub alle velden in!",
                    Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(password2)) {
            Toast.makeText(RegisterActivity.this, "Wachtwoorden komen niet overeen!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                addUser();

                                if (type.equals("owner")) {
                                    Intent intent = new Intent(RegisterActivity.this, AdvertActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else if (type.equals("walker")){
                                    Intent intent = new Intent(RegisterActivity.this, ChooseActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "Type gaat fout!",
                                            Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Inloggen niet gelukt!",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }
    }

    /**
     * Add an user with initial score 0.
     */
    public void addUser() {
        id = mAuth.getCurrentUser().getUid();
        Log.w(TAG, "user id" + id);

        User aUser;
        aUser = new User(type, name, email, false, null);
        databaseReference.child("users").child(id).setValue(aUser);
    }

    public void goBack(View view) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

}
