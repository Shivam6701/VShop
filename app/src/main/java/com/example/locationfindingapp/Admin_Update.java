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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_Update extends AppCompatActivity {
    TextView tvUpdate;
    EditText etUid,etUname,etUprice,etUna,etUdes;
    Button btnUpd;
    String ukey="";
    String uname,uprice,una,udes;
    int uid;

    StringBuilder details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__update);
        etUid=(EditText)findViewById(R.id.etUid);
        etUname=(EditText)findViewById(R.id.etUname);
        etUprice=(EditText)findViewById(R.id.etUprice);
        etUna=(EditText)findViewById(R.id.etUna);
        etUdes=(EditText)findViewById(R.id.etUdes);
        btnUpd=(Button)findViewById(R.id.btnUpd);

        tvUpdate=(TextView)findViewById(R.id.tvUpdate);


        setDetails();
        btnUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update_details();

            }
        });

    }

    private void Update_details() {

        udes=etUdes.getText().toString();
        una=etUna.getText().toString();
        uname=etUname.getText().toString();
        uprice=etUprice.getText().toString();
        ukey=etUid.getText().toString();
        if(ukey!=null)
        {

            try {
                uid=Integer.parseInt(ukey)-1;
                ukey=MainActivity.list.get(uid).getId().toString();
                if(!ukey.isEmpty() && (!una.isEmpty() || !udes.isEmpty() || !uprice.isEmpty() || !uname.isEmpty()))
                {
                    update_database();
                }
                else {
                    Toast.makeText(Admin_Update.this, "Please Enter Atleast one field to be updated", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                Toast.makeText(Admin_Update.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void update_database() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://location-finding-app-d36f0-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference().child("productlist").child(ukey);
        if(!uname.isEmpty())
        {
            myRef.child("name").setValue(uname);
        }
        if(!una.isEmpty())
        {
            myRef.child("numberAvailable").setValue(una);
        }
        if(!uprice.isEmpty())
        {
            myRef.child("price").setValue(uprice);
        }
        if(!udes.isEmpty())
        {
            myRef.child("description").setValue(udes);
        }
        Toast.makeText(Admin_Update.this, "Values updated", Toast.LENGTH_SHORT).show();
        tvUpdate.setText("");


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
        tvUpdate.setMovementMethod(new ScrollingMovementMethod());
        tvUpdate.setText(details);

    }
}