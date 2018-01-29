package com.example.britt.walkmydog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.britt.walkmydog.DogActivity.getImage;

public class DogAdapter extends ArrayAdapter {

    // Initialize for layout.
    ImageView picture;
    TextView name;


    public DogAdapter(Context context, ArrayList<Dog> dogArray) {
        super(context, R.layout.dog_adapter, dogArray);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.dog_adapter, parent, false);
        }

        // Get layout views.
        name = view.findViewById(R.id.dogName);
        picture = view.findViewById(R.id.dogPicture);

        // Get information of dogs.
        Dog mDog = (Dog) getItem(position);
        String dogPhoto = mDog.photo;
        String dogName = mDog.name;
        String id = mDog.id;

        // Set dog's data in layout.
        name.setText(dogName);
        picture.setTag(id);
        if (dogPhoto != null) {
            getImage(dogPhoto, picture);
        }

        return view;
    }
}
