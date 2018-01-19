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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
    String picture;
    String dogNameText;

    ImageView photo;
    TextView dogName;
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

        Intent intent = getIntent();
        picture = intent.getStringExtra("photo");
        dogNameText = intent.getStringExtra("name");
        dogName.setText(dogNameText);

        photo = findViewById(R.id.photo);
        getImage();
    }

    public void getImage() {
        if (picture == null) {
            Log.w("LOGO", "Logo is used");
        }
        else {
            byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            photo.setImageBitmap(decodedByte);
        }
    }

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
