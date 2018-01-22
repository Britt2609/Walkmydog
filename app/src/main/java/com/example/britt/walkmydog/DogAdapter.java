package com.example.britt.walkmydog;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by britt on 11-1-2018.
 */

public class DogAdapter extends ArrayAdapter {

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

        name = view.findViewById(R.id.dogName);
        picture = view.findViewById(R.id.dogPicture);

        Dog mDog = (Dog) getItem(position);

        String dogPhoto = mDog.photo;
        String dogName = mDog.name;
        String id = mDog.id;

        if (dogPhoto == null) {
            Log.d("LOGO", "Logo is used");
        }

        else {
            getImage(dogPhoto);
        }

        name.setText(dogName);
        Log.w("TAGG", id);
        picture.setTag(id);

        return view;
    }

    public void getImage(String photo) {
        if (photo == null) {
            Log.w("LOGO", "Logo is used");
        }
        else {
            byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            picture.setImageBitmap(decodedByte);
        }
    }
}
