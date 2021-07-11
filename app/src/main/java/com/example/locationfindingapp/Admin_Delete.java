package com.example.locationfindingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Admin_Delete extends AppCompatActivity {
    TextView tvDelete;
    StringBuilder details;
    EditText etDid;
    Button btndelid;
    String ukey;
    int uid;
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__delete);
        etDid=findViewById(R.id.etDId);
        btndelid=findViewById(R.id.btndelid);
        tvDelete=findViewById(R.id.tvdelete);
        setDetails();
        btndelid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update_details();
            }
        });

    }

    private void Update_details() {


        ukey = etDid.getText().toString();
        if(ukey!=null)
        {

            try {
                uid = Integer.parseInt(ukey) - 1;
                ukey=MainActivity.list.get(uid).getId().toString();
                if(!ukey.isEmpty())
                {
                    update_database();
                }
                else {
                    Toast.makeText(Admin_Delete.this, "Please Enter Id", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                Toast.makeText(Admin_Delete.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void update_database() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://location-finding-app-d36f0-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference().child("productlist").child(ukey);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(MainActivity.list.get(uid).getImgurl());
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("Picture","#deleted");
                myRef.removeValue();
                Toast.makeText(Admin_Delete.this, "Values updated", Toast.LENGTH_SHORT).show();
                etDid.setText("");
            }
        });

    }

    public void setDetails()
    {
        details = new StringBuilder();
        for(int i=0;i<MainActivity.list.size();i++)
        {
            details.append(i+1);
            details.append(" ")
                    .append(MainActivity.list.get(i).getName())
                    .append("   ")
                    .append(MainActivity.list.get(i).getPrice())
                    .append("  ")
                    .append(MainActivity.list.get(i).getNumberAvailable())
                    .append("\n   ")
                    .append(MainActivity.list.get(i).getDescription())
                    .append("\n");
        }
        tvDelete.setMovementMethod(new ScrollingMovementMethod());
        tvDelete.setText(details);

    }
}