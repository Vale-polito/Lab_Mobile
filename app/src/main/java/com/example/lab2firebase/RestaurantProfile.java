package com.example.lab2firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class RestaurantProfile extends AppCompatActivity {
TextView name,addrees,phone,mobile,Mon,sat,sun,tue,wed,thu,fri;
String resname,resphone,resmobile,resaddress,monday,saturday,sunday,tuesday,wednesday,thursday,friday;
    public SharedPreferences result;
    ViewFlipper viewFlipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);
        result= PreferenceManager.getDefaultSharedPreferences(this);
        name= (TextView) findViewById(R.id.txtNameRestaurant);
        phone=(TextView) findViewById(R.id.txtPhone);
        mobile=(TextView)findViewById(R.id.txtCell);
        addrees=(TextView)findViewById(R.id.txtAddress);
        Mon=(TextView)findViewById(R.id.txt_monday);
        sat=(TextView)findViewById(R.id.txt_saturday);
        sun=(TextView)findViewById(R.id.txt_sunday);
        tue=(TextView)findViewById(R.id.txt_tuesday);
        wed=(TextView)findViewById(R.id.txt_wednesday);
        thu=(TextView)findViewById(R.id.txt_thursday);
        fri=(TextView)findViewById(R.id.txt_friday);




        resname=result.getString("name","");
        resphone=result.getString("phone","");
        resmobile=result.getString("mobile","");
        resaddress=result.getString("address","");
        monday=result.getString("Monday","");
        saturday=result.getString("Saturday","");
        sunday=result.getString("Sunday","");
        tuesday=result.getString("Tuesday","");
        wednesday=result.getString("Wednesday","");
        thursday=result.getString("Thursday","");
        friday=result.getString("Friday","");
        name.setText(resname);
        phone.setText(resphone);
        mobile.setText(resmobile);
        addrees.setText(resaddress);
        Mon.setText(monday);
        tue.setText(tuesday);
        wed.setText(wednesday);
        thu.setText(thursday);
        sat.setText(saturday);
        sun.setText(sunday);
        fri.setText(friday);



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

