package com.example.locationfindingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class AdminAdd extends AppCompatActivity {
    EditText etAvailable,etItemName,etprice,etdesc;
    private static final String TAG = "Error";
    TextView tvshowlastdata;
    Button btnSignOut,btnSelectImg;
    int CODE_FOR_ST=90;
    String name,desc,price,avai;

    Intent intent;
    String uid,img_d_url;
    private Uri filePath;
    ImageView ivPrev;
    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminarea);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Admin");
        etItemName= findViewById(R.id.etitemname);
        btnSelectImg=(Button)findViewById(R.id.btnSelectImg);
        etprice= findViewById(R.id.etprice);
        ivPrev=(ImageView)findViewById(R.id.ivPrev);
        ivPrev.setVisibility(View.GONE);
        etdesc= findViewById(R.id.etdesc);
        etAvailable= findViewById(R.id.etAvailable);
        tvshowlastdata= findViewById(R.id.tvshowlastdata);
        tvshowlastdata.setVisibility(View.GONE);
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // on pressing btnSelect SelectImage() is called
        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(ContextCompat.checkSelfPermission(AdminAdd.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(AdminAdd.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},CODE_FOR_ST);
                }

                SelectImage();

            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==CODE_FOR_ST)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Thank you!!", Toast.LENGTH_SHORT).show();
            }
            else if (grantResults[0]==PackageManager.PERMISSION_DENIED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(AdminAdd.this,Manifest.permission.READ_EXTERNAL_STORAGE))  //for deny
                {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("This Permission is important!");
                    dialog.setTitle("Important permission required!");

                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AdminAdd.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_FOR_ST);

                        }
                    });
                    dialog.setNegativeButton("NO THANKS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(AdminAdd.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
                else                                     //for never show again + deny
                {
                    Toast.makeText(this, "To use this app provide location", Toast.LENGTH_SHORT).show();
                }
            }


        }
    }

    public void SelectImage(){


        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {


        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            ivPrev.setVisibility(View.VISIBLE);

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                ivPrev.setImageBitmap(bitmap);
                //ivPrev.setVisibility(View.VISIBLE);

            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


    public void btnsubmitdata(View v)
    {

        name=etItemName.getText().toString().toLowerCase().trim();
        desc=etdesc.getText().toString().trim();
        price=etprice.getText().toString().trim();
        avai=etAvailable.getText().toString().trim();
        post_Data(name,desc,price,avai);

    }


    private void post_Data(String name,String desc,String price,String avai) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://location-finding-app-d36f0-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference().child("productlist");
        DatabaseReference newItemRef = myRef.push();
        String key=newItemRef.getKey();
        uploadImage(key,newItemRef);



        //Toast.makeText(this, "Successfully Posted", Toast.LENGTH_SHORT).show();
        etItemName.setText("");
        etdesc.setText("");
        etAvailable.setText("");
        etprice.setText("");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getChildren().toString();
                tvshowlastdata.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG,"Failed to read value.",error.toException());
            }
        });
    }
    private void uploadImage(String key,DatabaseReference newItemRef)
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + key);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            img_d_url = task.getResult().toString();
                                            newItemRef.setValue(new ShopData(name,desc,price,avai,img_d_url));
                                        }
                                    });


                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(AdminAdd.this,
                                                    "Data Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(AdminAdd.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });

            ivPrev.setVisibility(View.GONE);
        }
    }
}