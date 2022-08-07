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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Pexels extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private EditText searchField;
    private Button searchButton;
    private String inputValue;
    public static JSONArray retrievedData;
    private TextView deleteBtn;
    ListView listView;
    ArrayList<Image> imageUrlList = new ArrayList<>();
    ProgressBar progressBar;

    SQLiteDatabase db;
    MyOpenHelper MyOpener;
    static MyListAdapter myListAdapter;

    public static class Image {
        String imageUrl;
        long id;

        private Image(String imageUrl, long idIn) {
            this.imageUrl = imageUrl;
            this.id=idIn;
        }
        public String getImageUrl(){
            return imageUrl;
        }
        public long getId() {
            return id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pexels);

        loadDataFromDatabase();
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
        deleteBtn = findViewById(R.id.delete_button);
        listView = findViewById(R.id.listview);
        listView.setAdapter(myListAdapter = new MyListAdapter());
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        listView.setOnItemClickListener( (p, b, pos, id) -> {
            Log.i("delete", "delete data");
            Image whatWasClicked = imageUrlList.get(pos);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this?")

                    //What is the message:
                    .setMessage("The selected row is:"+ pos+"\n"+"The database id is:"+id)
                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {
                        imageUrlList.remove(pos);
                        myListAdapter.notifyDataSetChanged();
                        db.delete(MyOpenHelper.TABLE_NAME,MyOpenHelper.COL_ID+"=?",new String[]{Long.toString(whatWasClicked.getId())});
                    })
                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> { })
                    .create().show();
        });



        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }


        MyHTTPRequest req = new MyHTTPRequest();
        searchField.setText(getSharedPreferences("MySharedPref", MODE_PRIVATE).getString("term", ""));
        searchButton.setOnClickListener(click -> {
            inputValue = searchField.getText().toString();
            req.execute("https://api.pexels.com/v1/search?query="+inputValue);
        });
    }

    private class MyHTTPRequest extends AsyncTask< String, Integer, String> {
        private Bitmap image;
        private String authorName;
        @SuppressLint("WrongThread")
        public String doInBackground(String ... args)
        {
            try {
                //create a url object of what server to contact
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "563492ad6f91700001000001b62248826ada45acb5a07b9db365ec19");
                //wait for data:
                InputStream response = urlConnection.getInputStream();
                onProgressUpdate(100);
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
            }
            catch (Exception e) {
            }
            return "Done";
        }

        public void onProgressUpdate(Integer ... args) {
            Log.i("Activity", "Update: " + args[0]);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(args[0]);
        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            Log.i("HTTP", fromDoInBackground);
            progressBar.setVisibility(View.INVISIBLE);
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("term", String.valueOf(searchField.getText()));
            myEdit.apply();
            Intent goToDisplayData = new Intent(Pexels.this, DisplayData.class);
            startActivity(goToDisplayData);
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

    private void loadDataFromDatabase() {
        //get a database connection:
        MyOpenHelper dbOpener = new MyOpenHelper(this);
        db = dbOpener.getWritableDatabase();

        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String[] columns = {MyOpener.COL_ID, MyOpener.COL_IMAGE};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int imgUrlColIndex = results.getColumnIndex(MyOpener.COL_IMAGE);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            String imgUrl = results.getString(imgUrlColIndex);
            Log.i("image url", imgUrl);
            long id = results.getLong(idColIndex);

            imageUrlList.add(new Image(imgUrl, id));
        }
    }

    public class MyListAdapter extends BaseAdapter {

        public int getCount() { return imageUrlList.size();}
        public Object getItem(int position) { return imageUrlList.get(position).imageUrl; }
        public long getItemId(int position) { return (long) position; }
        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.favorite_image, parent, false);
            ImageView imageView = view.findViewById(R.id.imageview);
            URL imageUrl = null;
            Bitmap bitmap = null;
            try {
                imageUrl = new URL(imageUrlList.get(position).getImageUrl());
                bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                imageView.setImageBitmap(bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return view;
        }
    }
}

