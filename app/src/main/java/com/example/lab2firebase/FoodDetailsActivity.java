package com.example.lab2firebase;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class FoodDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_IMAGE="com.example.lab2firebase.EXTRA_IMAGE";
    private EditText edtFoodName,edtPrice,edtDiscount,edtAvailbaleQuantity,edtShortdescription;
    private Button btnUpdate,btnView,btnDelete,btnHome;
    private ImageView imgFood;
    private Uri image_uri;
    String myUri;
    private ProgressBar uploadProgress;
    //*********Define variables to read from camera

    private Bitmap imageBitmap;
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;
    //declare views
    ImageButton btnSelectPhoto;

    //define variables for getting from another activity
    String key,foodName,price,discount,availableQuantity,shortDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        key = getIntent().getStringExtra("key");
        foodName = getIntent().getStringExtra("foodName");
        price = getIntent().getStringExtra("price");
        discount = getIntent().getStringExtra("discount");
        availableQuantity = getIntent().getStringExtra("availableQuantity");
        shortDescription = getIntent().getStringExtra("shortDescription");
        //image

        uploadProgress=findViewById(R.id.uploadProgress);
        edtDiscount=findViewById(R.id.edtDiscount);
        edtDiscount.setText(discount);
        edtFoodName=findViewById(R.id.edtFoodName);
        edtFoodName.setText(foodName);
        edtPrice=findViewById(R.id.edtPrice);
        edtPrice.setText(price);
        edtAvailbaleQuantity=findViewById(R.id.edtAvailableQuantity);
        edtAvailbaleQuantity.setText(availableQuantity);
        edtShortdescription=findViewById(R.id.edtShortDescription);
        edtShortdescription.setText(shortDescription);
        imgFood=findViewById(R.id.imgFood);
        //imgFood.setImageURI(image_uri);
        // we have to put also for image
        btnSelectPhoto=findViewById(R.id.btnSelectPhoto);
        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });
        btnUpdate=findViewById(R.id.btnUpdate);
        btnDelete=findViewById(R.id.btnDelete);
        btnView=findViewById(R.id.btnView);
        btnHome=findViewById(R.id.btnHome);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FoodDetailsActivity.this, CircleMenu.class);
                startActivity(i);
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FoodDetailsActivity.this, FoodListActivity.class);
                startActivity(i);
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (!validateName(edtFoodName.getText().toString()))
                {
                    edtFoodName.requestFocus();

                } else if (!validatePrice(edtPrice.getText().toString()))
                {
                    edtPrice.requestFocus();
                } else if (!validateAvailableQuantity(edtAvailbaleQuantity.getText().toString()))
                {
                    edtAvailbaleQuantity.requestFocus();
                }
                else   updateInfo();
                }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseDatabaseHelper().deleteFoods(key, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<DailyOffer> dailyOffers, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(FoodDetailsActivity.this,"Food has"+
                                "been deleted",Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                });
            }
        });

    }
    private void updateInfo(){
        DailyOffer dailyOffer= new DailyOffer();
        dailyOffer.setName(edtFoodName.getText().toString());
        dailyOffer.setPrice(edtPrice.getText().toString());
        dailyOffer.setDiscount(edtDiscount.getText().toString());
        dailyOffer.setAvailablequantity(edtAvailbaleQuantity.getText().toString());
        dailyOffer.setShortdescription(edtShortdescription.getText().toString());
        dailyOffer.setImageUrl(String.valueOf(image_uri));
        //Image

        new FirebaseDatabaseHelper().updateFoods(key, dailyOffer, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<DailyOffer> dailyOffers, List<String> keys) {

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {
                Toast.makeText(FoodDetailsActivity.this,"Food has been updated",Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            @Override
            public void DataIsDeleted() {

            }
        });

    }
    // *****************Camera
    // *****************This part create dialog box
    private void selectImage() {
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Delete" };
        AlertDialog.Builder builder = new AlertDialog.Builder(FoodDetailsActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    openCamera();
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    openGallery();
                }
                else if (options[item].equals("Delete")) {
                    int drawableResource = R.drawable.default_food;
                    Drawable d = getResources().getDrawable(drawableResource);
                    image_uri = Uri.parse("android.resource://com.example.lab2firebase/drawable/" +R.drawable.default_food);
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
        String image=savedInstanceState.getString("image",""); // Value that was saved will restore to variable
        image_uri = Uri.parse(image);
        imgFood.setImageURI(image_uri);
    }
    //...................................
    //************Validate each view
    private boolean validateName(String Name) {
        int characters = Name.trim().length();
        if (characters > 20) {
            edtFoodName.setError("Name is too long ( maximum is 20)");
            return false;
        } else if (characters<1){
            edtFoodName.setError("Name can not be empty");
            return false;
        }
        else {
            edtFoodName.setError(null);
            return true;
        }
    }
    private boolean validatePrice(String Price) {
        int characters = Price.trim().length();
        if (characters<1){
            edtPrice.setError("Price can not be empty");
            return false;
        }
        else {
            edtPrice.setError(null);
            return true;
        }
    }
    private boolean validateAvailableQuantity(String AvailableQuantity) {
        int characters = AvailableQuantity.trim().length();
        if (characters<1){
            edtAvailbaleQuantity.setError("Available quantity can not be empty");
            return false;
        }
        else {
            edtAvailbaleQuantity.setError(null);
            return true;
        }
    }
}
