package com.example.britt.walkmydog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

public class ContactActivity extends AppCompatActivity {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Set spinner to be able to choose category.
        spinner = findViewById(R.id.spinner5);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_doginfo_contact,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void SelectOption(View view) {
        String option = spinner.getSelectedItem().toString();
        if(option.equals("Log Out"))
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ContactActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (option.equals("Overview"))
        {
            Intent intent = new Intent(ContactActivity.this, OverviewActivity.class);
            startActivity(intent);
            finish();
        }
        else if (option.equals("Adverts"))
        {
            Intent intent = new Intent(ContactActivity.this, ChooseActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
