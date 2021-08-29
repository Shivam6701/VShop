package com.example.locationfindingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderFailure extends AppCompatActivity {
   String noUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_failure);

        LottieAnimationView animationView=findViewById(R.id.cancelFailure);
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                add_in_database();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationView.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    public void check_Action_after_Failure()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Payment was Cancelled");
        dialog.setTitle("What's next!");

        dialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(OrderFailure.this, "Try again", Toast.LENGTH_SHORT).show();

            }
        });
        dialog.setNegativeButton("Return Home", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(OrderFailure.this,signin_activity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }
    public void add_in_database()
    {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://location-finding-app-d36f0-default-rtdb.asia-southeast1.firebasedatabase.app/");

            //llProgressBar.setVisibility(View.VISIBLE);
            for (int i = 0; i < MainActivity.itemCount.size(); i++) {
                if (MainActivity.itemCount.get(i) > 0) {
                    String ukey = MainActivity.list.get(i).getId();
                    DatabaseReference myRef = database.getReference().child("productlist").child(ukey);
                    DatabaseReference myRef2 = database.getReference().child("productlist").child(ukey).child("numberAvailable");
                    int finalI = i;
                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            noUpdate = snapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            noUpdate = MainActivity.list.get(finalI).getNumberAvailable();
                        }
                    });

                    if (!(noUpdate.equals("") || noUpdate == null)) {
                        String newNo = String.valueOf(Integer.parseInt(noUpdate) + MainActivity.itemCount.get(i));
                        myRef.child("numberAvailable").setValue(newNo);
                    } else {
                        String newNo = String.valueOf(Integer.parseInt(MainActivity.list.get(i).getNumberAvailable()) + MainActivity.itemCount.get(i));
                        myRef.child("numberAvailable").setValue(newNo);
                    }
                }
            }
            check_Action_after_Failure();
            //llProgressBar.setVisibility(View.GONE);
        }
        catch (Exception e)
        {
            Log.d("PaymentF",e.getMessage());
        }

    }
}