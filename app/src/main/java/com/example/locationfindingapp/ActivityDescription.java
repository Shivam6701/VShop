package com.example.locationfindingapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ActivityDescription extends AppCompatActivity {
    TextView tvdes,tvDesName,tvDesPrice;
    ImageView ivIImg;
    Button btnDesMinus,btnDesPlus,button2cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        tvdes=(TextView)findViewById(R.id.tvdes);
        ivIImg=findViewById(R.id.ivIImg);
        btnDesMinus=findViewById(R.id.btnDesMinus);
        btnDesPlus=findViewById(R.id.btnDesPlus);
        button2cart=findViewById(R.id.button2cart);
        tvDesName=findViewById(R.id.tvDesName);
        tvDesPrice=findViewById(R.id.tvDesPrice);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;

        int index = (getIntent().getIntExtra("Idclick",-1));
        SharedPreferences cartprefs=getSharedPreferences(MainActivity.MY_PREFS_FILENAME,MODE_PRIVATE);
        String id=MainActivity.list.get(index).getId();
        int btnType = cartprefs.getInt(id,0);

        if(index!=-1) {
            tvdes.setText(MainActivity.list.get(index).getDescription());
           if(btnType==0)
           {
               btnDesPlus.setVisibility(View.VISIBLE);
           }
           else
               btnDesMinus.setVisibility(View.VISIBLE);
            //actionBar.setTitle(MainActivity.list.get(index).getName());
            actionBar.hide();
            tvDesName.setText(MainActivity.list.get(index).getName());
            tvDesPrice.setText(new StringBuilder().append(R.string.Rs).append(" ").append(MainActivity.list.get(index).getPrice()).toString());
            if(MainActivity.list.get(index).getImgurl()!=null)
            Picasso.get().load(MainActivity.list.get(index).getImgurl()).into(ivIImg);
            btnDesPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnDesPlus.setVisibility(View.GONE);
                btnDesMinus.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = getSharedPreferences(MainActivity.MY_PREFS_FILENAME,MODE_PRIVATE).edit();
                editor.putInt(id,1);
                editor.apply();

            }
        });
        btnDesMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDesMinus.setVisibility(View.GONE);
                btnDesPlus.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = getSharedPreferences(MainActivity.MY_PREFS_FILENAME,MODE_PRIVATE).edit();
                editor.putInt(id,0);
                editor.apply();
            }
        });

        button2cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityDescription.this, ActivityCart.class);
                startActivity(intent);
            }
        });


        }
        else
            Toast.makeText(ActivityDescription.this, "Error", Toast.LENGTH_SHORT).show();

    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();

        setResult(RESULT_OK,intent);
        finish();
    }



}