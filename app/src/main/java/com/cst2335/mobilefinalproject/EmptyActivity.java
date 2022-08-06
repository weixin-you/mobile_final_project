package com.cst2335.mobilefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        Bundle dataToPass = getIntent().getExtras();
        Log.i("data", dataToPass.getString("urlF"));

        DetailsPhoto dFragment = new DetailsPhoto();
        dFragment.setArguments(dataToPass);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment,dFragment)
                .commit();

    }
}