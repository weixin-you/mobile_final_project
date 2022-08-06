package com.cst2335.mobilefinalproject;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DetailsPhoto extends Fragment {
    private AppCompatActivity parentActivity;
    private Bundle dataFromActivity;
    private MyOpenHelper myOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DetailsPhoto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_details_photo, container, false);
        ImageView imageView = (ImageView)result.findViewById(R.id.original_photo);
        myOpenHelper = new MyOpenHelper(getContext());
        sqLiteDatabase = myOpenHelper.getWritableDatabase();


        URL imageUrl = null;
        Bitmap bitmap = null;
        try {
            imageUrl = new URL(dataFromActivity.getString("imageUrlF"));
            bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            imageView.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        TextView height = (TextView)result.findViewById(R.id.height);
        height.setText(String.valueOf(dataFromActivity.getInt("heightF")));
        TextView width = (TextView)result.findViewById(R.id.width);
        width.setText(String.valueOf(dataFromActivity.getInt("widthF")));
        TextView url = (TextView) result.findViewById(R.id.url);
        url.setText(String.valueOf(dataFromActivity.getString("urlF")));

        url.setOnClickListener(click -> {
            String desUrl = (String) url.getText();
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(desUrl));
            startActivity(i);
        });


        Button saveToFavorite = (Button)result.findViewById(R.id.favorite);
        saveToFavorite.setOnClickListener(click -> {
            String url_image=dataFromActivity.getString("imageUrlF");
            ContentValues newRow = new ContentValues();
            newRow.put( MyOpenHelper.COL_IMAGE , url_image);
            long id =sqLiteDatabase.insert( MyOpenHelper.TABLE_NAME, null, newRow );
            Log.i("clicked", "fav clicked");
            Log.i("insert to db", "Add a photoUrl to database");

        });
        return result;
    }
}