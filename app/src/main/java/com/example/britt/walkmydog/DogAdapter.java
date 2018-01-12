package com.example.britt.walkmydog;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by britt on 11-1-2018.
 */

public class DogAdapter extends ResourceCursorAdapter {

    Context c;


    public DogAdapter(Context context, Cursor cursor) {
        super(context, R.layout.dog_adapter, cursor, 0);
        c = context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ImageView picture = view.findViewById(R.id.dogPicture);
        TextView dish = view.findViewById(R.id.name);

        RequestQueue queue =  Volley.newRequestQueue(c);

        Integer index_item = cursor.getColumnIndex("name");
        Integer index_url = cursor.getColumnIndex("image_url");

        String value_name = cursor.getString(index_item);
        String value_url = cursor.getString(index_url);

        dish.setText(value_name);

        ImageRequest imageRequest = new ImageRequest(value_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                picture.setImageBitmap(response);
            }
        }, 0, 0, null, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String ImageNotFound = "Image not found!";
                System.out.println(ImageNotFound);
            }
        }
        );
        queue.add(imageRequest);

    }
}
