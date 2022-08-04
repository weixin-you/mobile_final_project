package com.cst2335.mobilefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Pexels extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private EditText searchField;
    private Button searchButton;
    private String inputValue;
    private String[] photoData2={"one", "two", "three"};
    private String[] authorNames;
    public static JSONArray retrievedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pexels);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,myToolbar,
                R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        searchField = findViewById(R.id.search_field);
        searchButton = findViewById(R.id.search_button);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }


        MyHTTPRequest req = new MyHTTPRequest();

        searchButton.setOnClickListener(click -> {
            inputValue = searchField.getText().toString();
            req.execute("https://api.pexels.com/v1/search?query="+inputValue);
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < photoData2.length; i++) {
                sb.append(photoData2[i]).append(",");
            }

            Log.i("85", "in call back");
            myEdit.putString("data", sb.toString());
            myEdit.apply();

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent goToDisplayData = new Intent(Pexels.this, DisplayData.class);
            startActivity(goToDisplayData);
        });


    }

    private class MyHTTPRequest extends AsyncTask< String, Integer, String> {
        private Bitmap image;
        private String authorName;
        public String doInBackground(String ... args)
        {
            try {
                //create a url object of what server to contact
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "563492ad6f91700001000001e02a0abf4b14465abc178c6a7821b6b1");
                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //Build the entire string response
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                // result is the whole String
                String result = sb.toString();

                //convert String to JSON
                JSONObject jsonResult = new JSONObject(result);
                JSONArray photosArray = jsonResult.getJSONArray("photos");
                retrievedData = photosArray;
                Log.i("121", photosArray.toString());
//                for (int i = 0; i < photosArray.length(); i++) {
//                    Log.i("start", "start");
//                    JSONObject objFromArray=photosArray.getJSONObject(i);
//                    String pictureUrl = objFromArray.getString("url");
//                    String authorName = objFromArray.getString("name");
//                    retrievedData[i]=authorName;
//                    int width = objFromArray.getInt("width");
//                    int height = objFromArray.getInt("height");
//                }
                Log.i("end", "end");


            }
            catch (Exception e)
            {

            }

            return "Done";
        }

        public void onProgressUpdate(Integer ... args)
        {

        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            Log.i("HTTP", fromDoInBackground);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item1:
                Toast.makeText(this, "You clicked on Homepage", Toast.LENGTH_LONG).show();
                break;

            case R.id.item2:
                Toast.makeText(this, "You clicked on Cocktail Database", Toast.LENGTH_LONG).show();
                break;

            case R.id.item3:
                Toast.makeText(this, "You clicked on Food Nutrition Database", Toast.LENGTH_LONG).show();
                break;

            case R.id.item4:
                Toast.makeText(this, "You clicked on the Owlbot Database", Toast.LENGTH_LONG).show();
                break;

            case R.id.item5:
                Toast.makeText(this, "You clicked on the Pexels", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.go_pexels:
                Intent goPexels = new Intent (getApplicationContext(), Pexels.class);
                startActivity(goPexels);
                break;

            case R.id.go_cocktail:
                Intent goCockTail = new Intent (getApplicationContext(), CocktailDatabase.class);
                startActivity(goCockTail);
                break;

            case R.id.go_food:
                Intent goFood = new Intent (getApplicationContext(), FoodNutritionDatabase.class);
                startActivity(goFood);
                break;

            case R.id.go_owlbot:
                Intent goOwlbot = new Intent(getApplicationContext(), OwlbotDictionary.class);
                startActivity(goOwlbot);
                break;

            case R.id.go_homepage:
                finish();
                Intent homepage=new Intent(this,MainActivity.class);
                startActivity(homepage);
                break;

            case R.id.helpItem:
                AlertDialog.Builder triviaHelpDialog = new AlertDialog.Builder(this);
                triviaHelpDialog.setTitle(getResources().getString(R.string.HelpTile))
                        //What is the message:
                        .setMessage(getResources().getString(R.string.pexelsInstructions1) + "\n"
                                + getResources().getString(R.string.pexelsInstructions2) + "\n"
                                + getResources().getString(R.string.pexelsInstructions3) + "\n"
                                + getResources().getString(R.string.pexelsInstructions4) + "\n"
                                + getResources().getString(R.string.pexelsInstructions5))
                        //What the No button does:
                        .setNegativeButton(getResources().getString(R.string.closeHelpDialog), (click, arg) -> {
                        })
                        //Show the dialog
                        .create().show();
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }


}

