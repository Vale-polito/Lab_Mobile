package com.example.lab2firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

        //************ViewFlipper
        int images[]={R.drawable.abbasi_rest_1,R.drawable.abbasi_rest_2,R.drawable.abbasi_rest_3,R.drawable.abbasi_rest_4};
        viewFlipper=findViewById(R.id.viewFlipper);
        for (int image:images){
            flipperImages(image);
        }
        //***********open new activity to add daily offers
        ImageButton btnDailyOffer = findViewById(R.id.btnDailyOffer);
        btnDailyOffer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openDailyOffer();
            }
        });
        //End of btn Daily offers


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
    //.....End daily offer
    public void flipperImages(int image){
        ImageView imageView=new ImageView(this);
        imageView.setBackgroundResource(image);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
}
