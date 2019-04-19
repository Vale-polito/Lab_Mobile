package com.example.lab2firebase;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

public class RestaurantProfile extends AppCompatActivity {

    ViewFlipper viewFlipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);

        ImageButton btnAddOffer=findViewById(R.id.btnDailyOffer);

        btnAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RestaurantProfile.this, NewDailyOffer.class);
                startActivity(i);
            }
        });


        //***********Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //end Toolbar

    }
    //**************Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle toolbar item clicks here.
        int id = item.getItemId();
        //If Edit_button has been pressed go to the Edit activity
        if (id == R.id.btn_edit) {
            Intent i = new Intent(this, EditRestaurantProfile.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    //End toolbar

    //***********open Daily offers page
    public void openDailyOffer(){
        Intent intent= new Intent(this, NewDailyOffer.class);
        startActivity(intent);
    }

}
