package com.example.locationfindingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DataAdapter.ItemClicked{
    RecyclerView.LayoutManager layoutManager;
    int RETURN_CODE = 35;
    RecyclerView recyclerView;
    DataAdapter adapter;
    LinearLayout llProgressBar;
    public static  final String MY_PREFS_FILENAME = "com.example.sharedpreferences.Eshop_cart"; //name of file should be unique

    Intent intent;
    public  static String username,userid,userEmail;
    String ADMIN_EMAIL = "shivamkumar67016@gmail.com";
    EditText et;
    int indexg;
    public static ArrayList<ShopData> list;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        llProgressBar=findViewById(R.id.llProgressBar);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onOptionsItemSelected);
        recyclerView =  findViewById(R.id.indiList);
        username = getIntent().getStringExtra("name");
        userid   = getIntent().getStringExtra("uid");
        userEmail= getIntent().getStringExtra("email");

        try {
            Menu menu=navigationView.getMenu();
            MenuItem menuItem= menu.findItem(R.id.userName);
             menuItem.setTitle(username);

            if (!userEmail.equals(ADMIN_EMAIL)) {
                MenuItem badmin=menu.findItem(R.id.admin);
                badmin.setVisible(true);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Login error", Toast.LENGTH_SHORT).show();
        }
        DataSource();
    }
        public void DataSource()
        {llProgressBar.setVisibility(View.VISIBLE);

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("productlist");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    list=new ArrayList<ShopData>();
                    for (DataSnapshot  snapshot :datasnapshot.getChildren()) {
                        ShopData s = new ShopData(snapshot.getKey(),
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
           llProgressBar.setVisibility(View.GONE);
        }


    public  void dataadapter()
    {
        adapter = new DataAdapter(this,list);
        recyclerView.setHasFixedSize(true);
        int orientation =getResources().getConfiguration().orientation;
        if(orientation== Configuration.ORIENTATION_PORTRAIT)
            layoutManager = new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false);
        else
            layoutManager = new GridLayoutManager(this,3, LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);

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
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return  true;
        }
        switch (item.getItemId())
        {
            case R.id.cart:    cart();
                                break;
            case R.id.sign_out: sign_out();
                                break;
            case R.id.admin: adminOption();
                                break;
            case R.id.orders_list: myorder();
                                  break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void myorder() {
        Intent intent=new Intent(MainActivity.this, MyOrderActivity.class);
        startActivity(intent);
    }

    public void cart()
    {

        Intent intent=new Intent(MainActivity.this, ActivityCart.class);
        startActivity(intent);

    }

    @Override
    public void onItemClicked(int index)
    {
        /*
        for(int i=0;i<list.size();i++)
        {
            DataAdapter.ViewHolder vholder = (DataAdapter.ViewHolder)recyclerView.findViewHolderForLayoutPosition(i);
            try {

                EditText et = vholder.itemView.findViewById(R.id.etCountNumber);
                itemCount.set(i,Integer.parseInt(et.getText().toString()));

            }catch (Exception e)
            {
                Log.d("sme",e.getMessage());
            }
        }

         */
        indexg=index;
        Intent intent1=new Intent(MainActivity.this,ActivityDescription.class);
        intent1.putExtra("Idclick",index);

        startActivityForResult(intent1,1);



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1)
        {

            //if(resultCode==RESULT_OK)
            SharedPreferences cartprefs=getSharedPreferences(MainActivity.MY_PREFS_FILENAME,MODE_PRIVATE);
            String id=MainActivity.list.get(indexg).getId();
            int btnType = cartprefs.getInt(id,0);

                DataAdapter.ViewHolder vholder = (DataAdapter.ViewHolder)recyclerView.findViewHolderForLayoutPosition(indexg);

                if(btnType==0)
                {
                    vholder.btnMinus.setVisibility(View.GONE);
                    vholder.btnPlus.setVisibility(View.VISIBLE);
                }
                else
                {
                    vholder.btnPlus.setVisibility(View.GONE);
                    vholder.btnMinus.setVisibility(View.VISIBLE);
                }

        }
    }
}