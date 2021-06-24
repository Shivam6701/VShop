package com.example.locationfindingapp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DataAdapter.ItemClicked{
    RecyclerView.LayoutManager layoutManager;
    int RETURN_CODE = 35;
    RecyclerView recyclerView;
    DataAdapter adapter;

    Counter count = new Counter();
    Intent intent;
    String username,userid,userEmail;
    String ADMIN_EMAIL = "shivamkumar67016@gmail.com";
    EditText et;
    public static ArrayList<ShopData> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("title");

        recyclerView =  findViewById(R.id.indiList);

        username = getIntent().getStringExtra("name");
        userid   = getIntent().getStringExtra("uid");
        userEmail= getIntent().getStringExtra("email");
        DataSource();
    }

        public void DataSource()
        {

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("productlist");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    list=new ArrayList<ShopData>();
                    for (DataSnapshot  snapshot :datasnapshot.getChildren()) {
                        ShopData s = new ShopData(snapshot.getKey().toString(),
                                snapshot.child("name").getValue().toString(),
                                snapshot.child("description").getValue().toString(),
                                snapshot.child("price").getValue().toString(),
                                snapshot.child("numberAvailable").getValue().toString(),
                                snapshot.child("imgurl").getValue().toString());
                        list.add(s);

                    }

                    dataadapter();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("database",error.getMessage());

                }
            });
        }


    public  void dataadapter()
    {
        for(int i=0;i<list.size();i++)
            count.setCounter(0);
        adapter = new DataAdapter(this,list,this.count.counter);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this,1, LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem item=menu.findItem(R.id.admin);
        if(userEmail.equals(ADMIN_EMAIL))
        {
            item.setVisible(true);
        }
        MenuItem item1=menu.findItem(R.id.userName);
        item1.setTitle(username);
        return super.onCreateOptionsMenu(menu);
    }

    public  void btnLogin()
    {
        intent = new Intent(MainActivity.this,signin_activity.class);
        startActivity(intent);
        finish();
        //Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
    }
    public void sign_out()
    {
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(getApplicationContext(), GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
        btnLogin();
    }
    public void adminOption()
    {
        Intent intent=new Intent(MainActivity.this,Admin_Options.class);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.refresh:
                                break;
            case R.id.sign_out: sign_out();
                                break;
            case R.id.admin: adminOption();
                                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void btnOrder(View v)
    {

        for(int i=0;i<list.size();i++)
        {
            DataAdapter.ViewHolder vholder = (DataAdapter.ViewHolder)recyclerView.findViewHolderForLayoutPosition(i);
            try {

                EditText et = vholder.itemView.findViewById(R.id.etCountNumber);
                count.counter.set(i,Integer.parseInt(et.getText().toString()));
            }catch (Exception e)
            {
                Log.d("sme",e.getMessage());
            }
        }


        Intent intent=new Intent(MainActivity.this,ActivityOrder.class);
        intent.putIntegerArrayListExtra("orderlist", (ArrayList)count.counter);
        startActivity(intent);

    }
    @Override
    public void onItemClicked(int index)
    {
        Intent intent1=new Intent(MainActivity.this,ActivityDescription.class);

        intent1.putExtra("Idclick",index);
        startActivity(intent1);
    }



}