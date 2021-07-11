package com.example.locationfindingapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Admin_Options extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__options);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Dashboard");
    }
    public void btnAddItem(View v)
    {
        Intent intent = new Intent(Admin_Options.this, AdminAdd.class);
        startActivity(intent);
    }
    public void btnEditItem(View v)
    {
        Intent intent = new Intent(Admin_Options.this,Admin_Update.class);
        startActivity(intent);
    }

    public void btnDeleteItem(View v)
    {
        Intent intent = new Intent(Admin_Options.this,Admin_Delete.class);
        startActivity(intent);
    }


}