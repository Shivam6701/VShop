package com.example.locationfindingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class OrderSuccess extends AppCompatActivity {
    TextView tvPayData;
    Button btnTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
        tvPayData=findViewById(R.id.tvPayData);
        btnTrack=findViewById(R.id.btnTrack);
       /* String oid= getIntent().getStringExtra("oid");
        String pid= getIntent().getStringExtra("pid");
        String sig= getIntent().getStringExtra("sig");
        String data="Order id :- "+ oid+"\n"+
                "Payment id :- "+pid+"\n"+
                "Signature:- "+sig;
        tvPayData.setText(data);
   */
        SharedPreferences.Editor pref = getSharedPreferences(MainActivity.MY_PREFS_FILENAME, MODE_PRIVATE).edit();
        pref.clear();
        pref.apply();
        LottieAnimationView animationView=findViewById(R.id.tickSuccess);
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                tvPayData.setVisibility(View.INVISIBLE);
                btnTrack.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationView.setVisibility(View.GONE);
                tvPayData.setVisibility(View.VISIBLE);
                btnTrack.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderSuccess.this, MyOrderActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(OrderSuccess.this, signin_activity.class);
        startActivity(intent);
    }
}