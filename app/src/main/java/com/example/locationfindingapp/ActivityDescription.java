package com.example.locationfindingapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ActivityDescription extends AppCompatActivity {
    TextView tvdes;
    ImageView ivIImg;
    Button btnDesMinus,btnDesPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        tvdes=(TextView)findViewById(R.id.tvdes);
        ivIImg=findViewById(R.id.ivIImg);
        btnDesMinus=findViewById(R.id.btnDesMinus);
        btnDesPlus=findViewById(R.id.btnDesPlus);

        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;

        int index = (getIntent().getIntExtra("Idclick",-1));

        if(index!=-1) {
            tvdes.setText(MainActivity.list.get(index).getDescription());
           if(MainActivity.itemCount.get(index)==0)
           {
               btnDesPlus.setVisibility(View.VISIBLE);
           }
           else
               btnDesMinus.setVisibility(View.VISIBLE);
            actionBar.setTitle(MainActivity.list.get(index).getName());
            if(MainActivity.list.get(index).getImgurl()!=null)
            Picasso.get().load(MainActivity.list.get(index).getImgurl()).into(ivIImg);

        btnDesPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.itemCount.set(index,1);
                btnDesPlus.setVisibility(View.GONE);
                btnDesMinus.setVisibility(View.VISIBLE);

            }
        });
        btnDesMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.itemCount.set(index,0);
                btnDesMinus.setVisibility(View.GONE);
                btnDesPlus.setVisibility(View.VISIBLE);

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