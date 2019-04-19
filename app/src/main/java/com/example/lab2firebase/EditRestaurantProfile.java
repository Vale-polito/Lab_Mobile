package com.example.lab2firebase;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class EditRestaurantProfile extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("Resturants");
    //saving hours variables
    private TextView txt1, txt2, txt3, txt4;
    //saving name phone mobile address
    private TextView txt5, txt6, txt7, txt8;
    //saving totall info
    private TextView txt9, txt10, txt11, txt12, txt13, txt14, txt15;
    private Button savebutton;
    private String day;
    boolean a, b, c, d;
    private Bitmap imageBitmap;
    private ImageView imgFood;
    private Uri image_uri;
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;
    //declare views
    ImageButton btnSelectPhoto1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant_profile);
        Button btnCancel= findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditRestaurantProfile.this, CircleMenu.class);
                startActivity(i);
            }
        });

        btnSelectPhoto1 = findViewById(R.id.btnSelectPhoto1);
        btnSelectPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });

        Spinner weekdays = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.weekdays, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        weekdays.setAdapter(adapter);
        weekdays.setOnItemSelectedListener(this);


        txt1 = (TextView) findViewById(R.id.txthourfrom);
        txt2 = (TextView) findViewById(R.id.txthourto);
        txt3 = (TextView) findViewById(R.id.txthourfrom2);
        txt4 = (TextView) findViewById(R.id.txthourto2);
        txt5 = (TextView) findViewById(R.id.edt_nameRestaurant);
        txt6 = (TextView) findViewById(R.id.edt_phoneRestaurant);
        txt7 = (TextView) findViewById(R.id.edt_mobileresturant);
        txt8 = (TextView) findViewById(R.id.edt_address);
        txt9 = (TextView) findViewById(R.id.txt_monday);
        txt10 = (TextView) findViewById(R.id.txt_tuesday);
        txt11 = (TextView) findViewById(R.id.txt_wednesday);
        txt12 = (TextView) findViewById(R.id.txt_thursday);
        txt13 = (TextView) findViewById(R.id.txt_friday);
        txt14 = (TextView) findViewById(R.id.txt_saturday);
        txt15 = (TextView) findViewById(R.id.txt_sunday);
        savebutton = (Button) findViewById(R.id.btnsave);
        imgFood=(ImageView) findViewById(R.id.imgFood);
        ImageButton btntimepicker1, btntimepicker2, btntimepicker3, btntimepicker4;
        final DialogFragment timepicker1, timepicker2, timepicker3, timepicker4;
        a = b = c = d = false;
        timepicker1 = timepicker2 = timepicker3 = timepicker4 = new TimePicker();
        btntimepicker1 = findViewById(R.id.btntimepicker1);
        btntimepicker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timepicker1.show(getSupportFragmentManager(), "timepicker");
                a = true;

            }
        });
        btntimepicker2 = findViewById(R.id.btntimepicker2);
        btntimepicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a = false;
                b = true;
                timepicker2.show(getSupportFragmentManager(), "timepicker");
            }
        });
        btntimepicker3 = findViewById(R.id.btntimepicker3);
        btntimepicker3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a = false;
                b = false;
                c = true;
                timepicker3.show(getSupportFragmentManager(), "timepicker");
            }
        });
        btntimepicker4 = findViewById(R.id.btntimepicker4);
        btntimepicker4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a = false;
                b = false;
                c = false;
                d = true;
                timepicker4.show(getSupportFragmentManager(), "timepicker");
            }
        });
        savebutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (Validateusername()&& Validatephone()&&Validmobilephone()&&Validmobilephone()&&Validaddress()&&Validatehour1()&&Validatehour2()&&Validatehour3()&&Validatehour4()){
                    storeuserinf();
                }

            }
        });


        //*********Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //end of toolbar
        // Write  to the database


    }
    private boolean Validateusername() {
        int usernameinput;
        usernameinput = txt5.getText().toString().length();
        if (usernameinput==0) {
            txt5.setError("Field can not be empty!");
            Toast.makeText(this, "Please Correct The Errors", Toast.LENGTH_SHORT).show();
            return false;
        } else if (usernameinput > 20) {
            txt5.setError("username too long!");
            Toast.makeText(this, "Please Correct The Errors", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            txt5.setError(null);
            return true;

        }


    }
    private boolean Validatephone() {
        int usernameinput;
        usernameinput = txt6.getText().toString().length();
        if (usernameinput==0) {
            txt6.setError("Field can not be empty!");
            Toast.makeText(this, "Please Correct The Errors", Toast.LENGTH_SHORT).show();
            return false;
        } else if (usernameinput > 14 || usernameinput<10) {
            txt6.setError("phone number is not correct!");
            Toast.makeText(this, "Please Correct The Errors", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            txt6.setError(null);
            return true;

        }


    }
    private boolean Validmobilephone() {
        int usernameinput;
        usernameinput = txt7.getText().toString().length();
        if (usernameinput==0) {
            txt7.setError("Field can not be empty!");
            Toast.makeText(this, "Please Correct The Errors", Toast.LENGTH_SHORT).show();
            return false;
        } else if (usernameinput > 14 || usernameinput<10) {
            txt7.setError("phone number is not correct!");
            Toast.makeText(this, "Please Correct The Errors", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            txt7.setError(null);
            return true;

        }


    }
    private boolean Validaddress() {
        int usernameinput;
        usernameinput = txt8.getText().toString().length();
        if (usernameinput==0) {
            txt8.setError("Field can not be empty!");
            Toast.makeText(this, "Please Correct The Errors", Toast.LENGTH_SHORT).show();
            return false;
        } else if (usernameinput > 200) {
            txt8.setError("Address is Too long! <200!");
            Toast.makeText(this, "Please Correct The Errors", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            txt8.setError(null);
            return true;

        }


    }
    private boolean Validatehour1() {
        int hour;
        hour = txt1.getText().toString().length();
        if (hour == 0) {
            txt1.setError("Please Pick an hour!");
            Toast.makeText(this, "Please Pick an hour", Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }
    private boolean Validatehour2() {
        int hour;
        hour = txt2.getText().toString().length();
        if (hour == 0) {
            txt2.setError("Please Pick an hour!");
            Toast.makeText(this, "Please Pick an hour", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
    private boolean Validatehour3() {
        int hour;
        hour = txt3.getText().toString().length();
        if (hour == 0) {
            txt3.setError("Please Pick an hour!");
            Toast.makeText(this, "Please Pick an hour", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
    private boolean Validatehour4() {
        int hour;
        hour = txt4.getText().toString().length();
        if (hour == 0) {
            txt4.setError("Please Pick an hour!");
            Toast.makeText(this, "Please Pick an hour", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }




    @Override
    public void onTimeSet(android.widget.TimePicker View, int hourOfDay, int minute) {
        TextView textView1 = (TextView) findViewById(R.id.txthourfrom);
        TextView textView2 = (TextView) findViewById(R.id.txthourto);
        TextView textView3 = (TextView) findViewById(R.id.txthourfrom2);
        TextView textView4 = (TextView) findViewById(R.id.txthourto2);
        if (a == true) {
            textView1.setText("" + hourOfDay + ":" + minute);

        } else if (b == true) {

            textView2.setText("" + hourOfDay + ":" + minute);
        } else if (c == true) {

            textView3.setText("" + hourOfDay + ":" + minute);
        } else if (d == true) {

            textView4.setText("" + hourOfDay + ":" + minute);
        } else return;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void storeuserinf() {
        String weekday, hour1, hour2, hour3, hour4, name, phone, mobile, address, Monday, Tuesday, Wednesday;
        String Thursday, Friday, Saturday, Sunday;

        Monday = "Monday";
        Tuesday = "Tuesday";
        Wednesday = "Wednesday";
        Thursday = "Thursday";
        Friday = "Friday";
        Saturday = "Saturday";
        Sunday = "Sunday";
        weekday = day;
        hour1 = txt1.getText().toString().trim();
        hour2 = txt2.getText().toString().trim();
        hour3 = txt3.getText().toString().trim();
        hour4 = txt4.getText().toString().trim();
        name = txt5.getText().toString().trim();
        phone = txt6.getText().toString().trim();
        mobile = txt7.getText().toString().trim();
        address = txt8.getText().toString().trim();


        saveinformation saveinformation = new saveinformation(name, phone, mobile, address,String.valueOf(image_uri));

        savedaysandhours savedaysandhours = new savedaysandhours(weekday, hour1, hour2, hour3, hour4);
        reference.child(name).child("working days and hours").child(weekday).setValue(savedaysandhours);
        reference.child(name).child("info").setValue(saveinformation);

        if (weekday.equals(Monday)) {
            txt9.setText(weekday + ":  From: " + hour1 + " - " + hour2 + "   And  from: " + hour3 + " - " + hour4);
        } else if (weekday.equals(Tuesday)) {
            txt10.setText(weekday + ":  From: " + hour1 + " - " + hour2 + "   And  from: " + hour3 + " - " + hour4);
        } else if (weekday.equals(Wednesday)) {
            txt11.setText(weekday + ":  From: " + hour1 + " - " + hour2 + "   And  from: " + hour3 + " - " + hour4);
        } else if (weekday.equals(Thursday)) {
            txt12.setText(weekday + ":  From: " + hour1 + " - " + hour2 + "   And  from: " + hour3 + " - " + hour4);
        } else if (weekday.equals(Friday)) {
            txt13.setText(weekday + ":  From: " + hour1 + " - " + hour2 + "   And  from: " + hour3 + " - " + hour4);
        } else if (weekday.equals(Saturday)) {
            txt14.setText(weekday + ":  From: " + hour1 + " - " + hour2 + "   And  from: " + hour3 + " - " + hour4);
        } else if (weekday.equals(Sunday)) {
            txt15.setText(weekday + ":  From: " + hour1 + " - " + hour2 + "   And  from: " + hour3 + " - " + hour4);
        }

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        day = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void readFromDatabase() {

    }

    private void selectImage() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditRestaurantProfile.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    openCamera();
                } else if (options[item].equals("Choose from Gallery")) {
                    openGallery();
                } else if (options[item].equals("Delete")) {
                    int drawableResource = R.drawable.default_food;
                    Drawable d = getResources().getDrawable(drawableResource);
                    image_uri = Uri.parse("android.resource://com.example.lab2firebase/drawable/" + R.drawable.default_food);
                    imgFood.setImageDrawable(d);
                    dialog.dismiss();
                }
            }
        });


        builder.show();
    }

    //..........................
    //*****Open Gallery
    public void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    //.....................

    //***********Open Camera
    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //........................
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            image_uri = data.getData();
            imgFood.setImageURI(image_uri);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            image_uri = getImageUri(this, imageBitmap);
            imgFood.setImageURI(image_uri);
        }
    }

    //..................
    //*****
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    //..................
    //******* We want when we rotate screen image does not change
    //We use these 2 below functions
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (image_uri != null) {
            outState.putString("image", image_uri.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String image = savedInstanceState.getString("image", ""); // Value that was saved will restore to variable
        image_uri = Uri.parse(image);
        imgFood.setImageURI(image_uri);
    }


}


