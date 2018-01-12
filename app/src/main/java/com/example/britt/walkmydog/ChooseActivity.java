package com.example.britt.walkmydog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ChooseActivity extends AppCompatActivity {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        // Set spinner to be able to choose category.
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_choose,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * Give selected category to next activity and go to next activity.
     */
    public void SelectOption(View view) {
        String option = spinner.getSelectedItem().toString();
        if (option.equals("Log Out")) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (option.equals("Overview"))
        {
            Intent intent = new Intent(ChooseActivity.this, OverviewActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void goToNext(View view) {
        Intent intent = new Intent(ChooseActivity.this, DogActivity.class);
        startActivity(intent);
    }

}
