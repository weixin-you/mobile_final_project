package com.cst2335.mobilefinalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

public class Pexels extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pexels);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,myToolbar,
                R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
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
                Toast.makeText(this, "You clicked on item1", Toast.LENGTH_LONG).show();
                break;

            case R.id.item2:
                Toast.makeText(this, "You clicked on item2", Toast.LENGTH_LONG).show();
                break;

            case R.id.item3:
                Toast.makeText(this, "You clicked on item3", Toast.LENGTH_LONG).show();
                break;

            case R.id.item4:
                Toast.makeText(this, "You clicked on the overflow menu", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.go_cocktail:
                Intent goCockTail = new Intent (getApplicationContext(), CocktailDatabase.class);
                startActivity(goCockTail);
                break;

            case R.id.go_food:
                Intent goFood = new Intent(getApplicationContext(), FoodNutritionDatabase.class);
                startActivity(goFood);
                break;

            case R.id.go_owlbot:
                Intent goOwlbot = new Intent(getApplicationContext(), OwlbotDictionary.class);
                startActivity(goOwlbot);
                break;

            case R.id.go_homepage:
                finish();
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }


}

