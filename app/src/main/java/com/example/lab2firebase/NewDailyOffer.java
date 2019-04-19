package com.example.lab2firebase;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class NewDailyOffer extends AppCompatActivity {
    public static final String EXTRA_IMAGE="com.example.lab2firebase.EXTRA_IMAGE";
    private EditText edtFoodName,edtPrice,edtDiscount,edtAvailbaleQuantity,edtShortdesc;
    private Button btnAdd,btnView,btnHome;
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

    private DatabaseReference mDatabaseRefrence;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newdailyoffer);
        //*********Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //end of toolbar

        uploadProgress=findViewById(R.id.uploadProgress);
        btnAdd=findViewById(R.id.btnAdd);
        btnView=findViewById(R.id.btnView);
        btnHome=findViewById(R.id.btnHome);
        edtDiscount=findViewById(R.id.edtDiscount);
        edtFoodName=findViewById(R.id.edtFoodName);
        edtPrice=findViewById(R.id.edtPrice);
        edtAvailbaleQuantity=findViewById(R.id.edtAvailableQuantity);
        edtShortdesc=findViewById(R.id.edtShortDescription);
        imgFood=findViewById(R.id.imgFood);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewDailyOffer.this, CircleMenu.class);
                startActivity(i);
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewDailyOffer.this, FoodListActivity.class);
                startActivity(i);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask!=null && mUploadTask.isInProgress()){
                    Toast.makeText(NewDailyOffer.this,"Upload in progress",Toast.LENGTH_LONG).show();
                } else if (!validateName(edtFoodName.getText().toString()))
                {
                    edtFoodName.requestFocus();

                } else if (!validatePrice(edtPrice.getText().toString()))
                {
                    edtPrice.requestFocus();
                } else if (!validateAvailableQuantity(edtAvailbaleQuantity.getText().toString()))
                {
                    edtAvailbaleQuantity.requestFocus();
                }
                else   addInfo();
            }
        });
        //****************************** Camera
        btnSelectPhoto=findViewById(R.id.btnSelectPhoto);
        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });
        // End

        mDatabaseRefrence= FirebaseDatabase.getInstance().getReference("DailyFoods");
        mStorageRef= FirebaseStorage.getInstance().getReference("DailyFoods");
    }
    //********** what toolbar is doing
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.backmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_back) {
            Intent intent= new Intent(this,RestaurantProfile.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
    //End of code related to the toolbar
    private void addInfo(){
        if (image_uri!=null){
            final StorageReference fileReferences=mStorageRef.child(System.currentTimeMillis()+"."+getExtension(image_uri));
            mUploadTask=fileReferences.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    uploadProgress.setProgress(0);
                                }
                            },500);
                            fileReferences.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DailyOffer dailyOffer = new DailyOffer(edtFoodName.getText().toString().trim(), edtPrice.getText().toString().trim(), edtDiscount.getText().toString().trim(), edtAvailbaleQuantity.getText().toString().trim(), edtShortdesc.getText().toString().trim(), uri.toString());
                                    String dailyOfferId = mDatabaseRefrence.push().getKey();
                                    mDatabaseRefrence.child(dailyOfferId).setValue(dailyOffer);
                                    Toast.makeText(NewDailyOffer.this, "Upload Successfully", Toast.LENGTH_LONG).show();
                                    //Make empty all edit texts
                                    edtAvailbaleQuantity.setText("");
                                    edtDiscount.setText("");
                                    edtFoodName.setText("");
                                    edtPrice.setText("");
                                    edtShortdesc.setText("");
                                    //change image to the default one
                                    imgFood.setImageResource(R.drawable.default_food);
                                    edtFoodName.requestFocus();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewDailyOffer.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            uploadProgress.setProgress((int)progress);
                        }
                    });

        }else
            Toast.makeText(NewDailyOffer.this,"no photo has been selected",Toast.LENGTH_LONG).show();


    }
    private String getExtension(Uri uri){
        ContentResolver cr =getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(NewDailyOffer.this);

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
