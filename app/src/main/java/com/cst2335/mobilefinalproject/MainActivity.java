package com.cst2335.mobilefinalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    Button pexels;
    Button owlbotDictionary;
    Button cocktailDatabase;
    Button foodNutriDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent goToPexels = new Intent(MainActivity.this, Pexels.class);
        Button pexelsButton = findViewById(R.id.pexelsBtn);
        pexelsButton.setOnClickListener(click-> startActivity(goToPexels));


        pexels = findViewById(R.id.pexelsBtn);
        owlbotDictionary = findViewById(R.id.owlbotDictBtn);
        cocktailDatabase = findViewById(R.id.cocktailDbBtn);
        foodNutriDB = findViewById(R.id.foodNutriBtn);
        /**
         * when user clicks on the songster button it will go to the topic navigation page
         */

        pexels.setOnClickListener(click -> {
            Intent intent = new Intent(MainActivity.this, Pexels.class);
            startActivity(intent);
        });
        owlbotDictionary.setOnClickListener(click -> {
            Intent intent = new Intent(MainActivity.this, OwlbotDictionary.class);
            startActivity(intent);
        });

        cocktailDatabase.setOnClickListener(click -> {
            Intent intent = new Intent(MainActivity.this, CocktailDatabase.class);
            startActivity(intent);

        });

        foodNutriDB.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FoodNutritionDatabase.class);
            startActivity(intent);
        });

    }

    /**
     * @param menu used to inflate the menu layout created
     * @return boolean returning true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_project_main_toolbar, menu);
        return true;
    }


    /**
     * @param item: returns the message inside the item depending on which item the user selected.
     *              also if songster is clicked it will for to the songster navigation page
     * @return returns true.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;

    }
}

