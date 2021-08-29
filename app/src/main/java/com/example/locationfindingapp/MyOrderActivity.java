package com.example.locationfindingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOrderActivity extends AppCompatActivity {
    public ArrayList<String> orderkeylist;
    public ArrayList<OrderData> orderlist;
    TextView tvOrderData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        tvOrderData=findViewById(R.id.tvOrderData);

        order();
    }
    private void order() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(MainActivity.userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                orderkeylist=new ArrayList<String>();
                for (DataSnapshot  snapshot :datasnapshot.getChildren()) {
                    String s =
                            snapshot.child("key").getValue().toString();
                    orderkeylist.add(s);
                }
                if(orderkeylist!=null) {
                    updatescreen();

                }
                else
                    Toast.makeText(MyOrderActivity.this, "NO ORDER!!", Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyOrderActivity.this, "Retry", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updatescreen() {
       orderlist=new ArrayList<OrderData>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        for(int i=0;i<orderkeylist.size();i++)
        {

            DatabaseReference databaseReference = firebaseDatabase.getReference().child("orders").child(orderkeylist.get(i));

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String E = snapshot.child("userEmail").getValue().toString();
                        String s = snapshot.child("price").getValue().toString();
                        String it= snapshot.child("itemDetail").getValue().toString();
                        orderlist.add(new OrderData(E,s,it));
                        if (orderlist.size() ==orderkeylist.size())
                        {
                            StringBuilder sb= new StringBuilder(" ");
                            for(int i=0;i<orderkeylist.size();i++)
                            {
                                sb.append(orderlist.get(i).getUserEmail()).append("\n");
                                sb.append(orderlist.get(i).getItemDetail()).append("\n");
                                sb.append("Price : ");
                                sb.append(orderlist.get(i).getPrice()).append("\n").append("\n");
                            }
                            tvOrderData.setText(sb);
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }
    }
}