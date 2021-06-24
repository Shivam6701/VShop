package com.example.locationfindingapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ActivityDescription extends AppCompatActivity {
    TextView tvdes;
    ImageView ivIImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        tvdes=(TextView)findViewById(R.id.tvdes);
        ivIImg=findViewById(R.id.ivIImg);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;

        int index = (getIntent().getIntExtra("Idclick",-1));

        if(index!=-1) {
            tvdes.setText(MainActivity.list.get(index).getDescription());
            actionBar.setTitle(MainActivity.list.get(index).getName());
            if(MainActivity.list.get(index).getImgurl()!=null)
            Picasso.get().load(MainActivity.list.get(index).getImgurl()).into(ivIImg);
        }
        else
            Toast.makeText(ActivityDescription.this, "Error", Toast.LENGTH_SHORT).show();
    }
}