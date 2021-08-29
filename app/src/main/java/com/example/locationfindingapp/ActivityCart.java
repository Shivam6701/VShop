package com.example.locationfindingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ActivityCart extends AppCompatActivity  {
    int CODE_FOR_F_LOCATION = 66;
    private static final String TAG = ActivityCart.class.getSimpleName();
    TextView tvdetails,tvTotalPrice,tvAddress;
    Button btnAddress,btnrefreshAdd,btnpay;
    Geocoder geocoder;
    List<Address> addresses;
    public static Location lastLocation;
    HashMap<String,String> item=new HashMap<>();
    LinearLayout llProgressBar;
    float totalPrice=0;
    StringBuilder summary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        tvdetails= findViewById(R.id.tvdetails);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Cart Details...");
        tvTotalPrice= findViewById(R.id.tvTotalPrice);
        btnAddress= findViewById(R.id.btnAddress);
        tvAddress= findViewById(R.id.tvAddress);
        tvAddress.setVisibility(View.INVISIBLE);
        llProgressBar=(LinearLayout)findViewById(R.id.AOllProgressBar);
        btnrefreshAdd= findViewById(R.id.btnRefreshAdd);
        btnpay=findViewById(R.id.btnpay);
        btnpay.setVisibility(View.GONE);

        summary = new StringBuilder();
        btnrefreshAdd.setVisibility(View.INVISIBLE);
        for(int i=0;i<MainActivity.itemCount.size();i++)
        {
            if(MainActivity.itemCount.get(i)>0)
            {
                summary.append(MainActivity.list.get(i).getName()).append(" : ").append(MainActivity.itemCount.get(i)).append("x");
                summary.append(MainActivity.list.get(i).getPrice()).append("\n");
                totalPrice+=((MainActivity.itemCount.get(i))*(Float.parseFloat(MainActivity.list.get(i).getPrice())));
                item.put(MainActivity.list.get(i).getId()+"++"+MainActivity.list.get(i).getName(),MainActivity.itemCount.get(i).toString()+"X"+(MainActivity.list.get(i).getPrice()));
            }
        }
        tvdetails.setText(summary);
        String tp = "Total Price : "+ totalPrice;
        tvTotalPrice.setText(tp);
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlocation();
                btnAddress.setVisibility(View.GONE);
                tvAddress.setVisibility(View.VISIBLE);
                btnrefreshAdd.setVisibility(View.VISIBLE);
                btnpay.setVisibility(View.VISIBLE);
            }
        });
        btnrefreshAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlocation();
            }
        });
        int amount = Math.round(totalPrice)*100;
        final Activity activity = this;
        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                create_order();
                Intent intent=new Intent(ActivityCart.this,OrderSuccess.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void create_order() {
        LinearLayout llProgressBar=(LinearLayout)findViewById(R.id.AOllProgressBar);
        llProgressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://location-finding-app-d36f0-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference().child("orders");
        DatabaseReference newItemRef = myRef.push();
        String k=newItemRef.getKey();

        String ts =String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        DatabaseReference   userorderref=database.getReference().child("user").child(MainActivity.userid).child(ts);
        userorderref.setValue(new myorderKey(k));
        newItemRef.setValue(new OrderData(MainActivity.userid,
                MainActivity.username,
                MainActivity.userEmail,
                String.valueOf(totalPrice),
                String.valueOf(lastLocation.getLatitude()),
                String.valueOf(lastLocation.getLongitude()),
                summary.toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                update_database();
                llProgressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActivityCart.this, "Retry!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void update_database() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://location-finding-app-d36f0-default-rtdb.asia-southeast1.firebasedatabase.app/");
        for(int i=0;i<MainActivity.itemCount.size();i++)
        {
            if(MainActivity.itemCount.get(i)>0)
            {
                String ukey=MainActivity.list.get(i).getId();
                String newNo=String.valueOf(Integer.parseInt(MainActivity.list.get(i).getNumberAvailable())-MainActivity.itemCount.get(i));
                DatabaseReference myRef = database.getReference().child("productlist").child(ukey);

                myRef.child("numberAvailable").setValue(newNo);
            }
        }
    }

    private void Locationgranted(Location location) throws IOException {
        geocoder=new Geocoder(this, Locale.getDefault());
        lastLocation=location;
        addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        tvAddress.setText(addresses.get(0).getLocality()+", "+addresses.get(0).getSubAdminArea());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==CODE_FOR_F_LOCATION)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Thank you!!", Toast.LENGTH_SHORT).show();
            }
            else if (grantResults[0]==PackageManager.PERMISSION_DENIED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(ActivityCart.this,Manifest.permission.ACCESS_FINE_LOCATION))  //for deny
                {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("This Permission is important!");
                    dialog.setTitle("Important permission required!");

                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ActivityCart.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CODE_FOR_F_LOCATION);

                        }
                    });
                    dialog.setNegativeButton("NO THANKS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ActivityCart.this, "Cancelled", Toast.LENGTH_SHORT).show();
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
    public void getlocation()
    {
        {
            if(ContextCompat.checkSelfPermission(ActivityCart.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(ActivityCart.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},CODE_FOR_F_LOCATION);
            }

            else
            {
                FusedLocationProviderClient client =
                        LocationServices.getFusedLocationProviderClient(this);

                client.getLastLocation()
                        .addOnSuccessListener(this, location -> {
                            if (location != null) {
                                try {

                                    Locationgranted(location);
                                } catch (IOException e) {
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        }

    }
}