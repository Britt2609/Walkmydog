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
    private DatabaseReference databaseReference;

    // Use tag for logs.
    private static final String TAG = "firebase";


    // Initialize user data.
    String email;
    String password;
    String password2;
    String name;
    String id;
    String type = "";

    // Initialize for layout.
    Button goBack;
    EditText getEmail;
    EditText getPassword;
    EditText getPassword2;
    EditText getName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set database .
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get layout views.
        goBack = findViewById(R.id.goBack);
        getEmail = findViewById(R.id.getEmail);
        getPassword = findViewById(R.id.getPassword);
        getPassword2 = findViewById(R.id.getPassword2);
        getName = findViewById(R.id.getName);
    }

    /**
     * Get type from user
     */
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


    /**
     * Creates user with email and password.
     */
    public void createUser(View view) {

        checkInput();

        // Register to firebase and login.
        if (checkInput()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");

                                // Add user to database and go to next activity.
                                addUserToDB();
                                goToNext();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Inloggen niet gelukt!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public boolean checkInput() {
        // Get data out of input.
        email = getEmail.getText().toString();
        password = getPassword.getText().toString();
        password2 = getPassword2.getText().toString();
        name = getName.getText().toString();

        // Check if email and password are filled in.
        if (email.equals("") || name.equals("") || password.equals("") || type.equals("")) {
            Toast.makeText(RegisterActivity.this, "Vul aub alle velden in!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if passwords match.
        else if (!password.equals(password2)) {
            Toast.makeText(RegisterActivity.this, "Wachtwoorden komen niet overeen!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * Add an user with initial values.
     */
    public void addUserToDB() {

        // Get current user's id.
        id = mAuth.getCurrentUser().getUid();

        // Create new user and put in database.
        // TODO: Delete advert_status.
        User aUser;
        aUser = new User(type, name, email, false, null);
        databaseReference.child("users").child(id).setValue(aUser);
    }


    /**
     * Check which type the current user is and go to corresponding next activity.
     */
    public void goToNext() {
        if (type.equals("owner")) {
            // Sent owner to activity to make an advert.
            Intent intent = new Intent(RegisterActivity.this, AdvertActivity.class);
            startActivity(intent);
            finish();
        }

        else if (type.equals("walker")) {
            // Sent walker to activity with an overview of adverts.
            Intent intent = new Intent(RegisterActivity.this, ChooseActivity.class);
            startActivity(intent);
            finish();
        }
        // TODO: Deze else weghalen????
        else {
            Toast.makeText(RegisterActivity.this, "Type gaat fout!",
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Go back to login page when clicked on button.
     */
    public void goBack(View view) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // TODO: is dit nodig?
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (authStateListener != null) {
//            mAuth.removeAuthStateListener(authStateListener);
//        }
//    }

}
