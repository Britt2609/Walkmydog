package com.example.britt.walkmydog;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AdvertActivity extends AppCompatActivity {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);

        // Set spinner to be able to choose category.
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_choose,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }




    public void fotoMaken(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
        startActivity(intent);
    }

    public void goToConfirm(View view) {
        Intent intent = new Intent(AdvertActivity.this, ConfirmActivity.class);
        startActivity(intent);
    }
}
