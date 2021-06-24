package com.example.locationfindingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActivityOrder extends AppCompatActivity {
    int CODE_FOR_F_LOCATION = 66;
    TextView tvdetails,tvTotalPrice,tvAddress;
    ArrayList<Integer> dataNum;
    Button btnAddress,btnrefreshAdd;
    Geocoder geocoder;
    List<Address> addresses;
    public static Location lastLocation;

    float totalPrice=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        tvdetails= findViewById(R.id.tvdetails);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Order Details...");
        tvTotalPrice= findViewById(R.id.tvTotalPrice);
        btnAddress= findViewById(R.id.btnAddress);
        tvAddress= findViewById(R.id.tvAddress);
        tvAddress.setVisibility(View.INVISIBLE);
        btnrefreshAdd= findViewById(R.id.btnRefreshAdd);
        dataNum=getIntent().getIntegerArrayListExtra("orderlist");
        StringBuilder summary = new StringBuilder();
        btnrefreshAdd.setVisibility(View.INVISIBLE);
        for(int i=0;i<dataNum.size();i++)
        {
            if(dataNum.get(i)>0)
            {
                summary.append(MainActivity.list.get(i).getName()).append(" : ").append(dataNum.get(i)).append("x");
                summary.append(MainActivity.list.get(i).getPrice()).append("\n");
                totalPrice+=((dataNum.get(i))*(Float.parseFloat(MainActivity.list.get(i).getPrice())));
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

            }
        });
        btnrefreshAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlocation();
            }
        });


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
                if(ActivityCompat.shouldShowRequestPermissionRationale(ActivityOrder.this,Manifest.permission.ACCESS_FINE_LOCATION))  //for deny
                {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("This Permission is important!");
                    dialog.setTitle("Important permission required!");

                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ActivityOrder.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CODE_FOR_F_LOCATION);

                        }
                    });
                    dialog.setNegativeButton("NO THANKS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ActivityOrder.this, "Cancelled", Toast.LENGTH_SHORT).show();
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

            if(ContextCompat.checkSelfPermission(ActivityOrder.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(ActivityOrder.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},CODE_FOR_F_LOCATION);
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