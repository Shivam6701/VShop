package com.example.locationfindingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import static android.content.Context.MODE_PRIVATE;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>  {

    Context context;
    private final ArrayList<ShopData> values;
    ItemClicked activity;
    public interface ItemClicked
    {
        void onItemClicked(int index);
    }

    public DataAdapter(Context context, ArrayList<ShopData> values) {
        this.values = values;
        this.context=context;
        activity=(ItemClicked) context;
    }

    //public List getCount()
    //{
       // return MainActivity.itemCount;
    //}
    // private final Location cl;

    //Geocoder geocoder;
    //List<Address> addresses;

    public  class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName,tvPrice;
        TextView tvDiscript;
        ImageView ivpimg;
        EditText etCountNumber;
        Button btnMinus,btnPlus;

        public ViewHolder(View itemView){
            super(itemView);
            tvName= itemView.findViewById(R.id.tvname);
            ivpimg=itemView.findViewById(R.id.ivpimg);
            btnMinus= itemView.findViewById(R.id.btnMinus);
            btnPlus= itemView.findViewById(R.id.btnPlus);
            tvPrice= itemView.findViewById(R.id.tvPrice);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    activity.onItemClicked(values.indexOf((ShopData) v.getTag()));

                }
            });

        }

    }


   /*
        geocoder=new Geocoder(context, Locale.getDefault());

        try {
            addresses= (List<Address>) geocoder.getFromLocation(Double.parseDouble(values.get(position).getLatitude()),Double.parseDouble(values.get(position).getLongitude()),1);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        tvAdd.setText(addresses.get(0).getLocality()+", "+addresses.get(0).getSubAdminArea());

        try {
            Location dest =new Location("location");
            dest.setLatitude(Double.parseDouble(values.get(position).getLatitude()));
            dest.setLongitude(Double.parseDouble(values.get(position).getLongitude()));

            //int dist = (int)cl.distanceTo(dest);
            //String s = Integer.toString(dist)+"M";
            //tvDist.setText(s);
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Error location", Toast.LENGTH_SHORT).show();
        }

    */

    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        holder.itemView.setTag(values.get(position));
        holder.tvName.setText(values.get(position).getName());
        Picasso.get().load(values.get(position).getImgurl()).into(holder.ivpimg);

        holder.tvPrice.setText(String.valueOf(values.get(position).getPrice()));
        SharedPreferences cartprefs=context.getSharedPreferences(MainActivity.MY_PREFS_FILENAME,MODE_PRIVATE);
        String id=MainActivity.list.get(position).getId();
        int btnType = cartprefs.getInt(id,0);

        if(btnType==0)
        {
            holder.btnMinus.setVisibility(View.GONE);
            holder.btnPlus.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.btnPlus.setVisibility(View.GONE);
            holder.btnMinus.setVisibility(View.VISIBLE);
        }


        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                holder.btnPlus.setVisibility(View.GONE);
                holder.btnMinus.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.MY_PREFS_FILENAME,MODE_PRIVATE).edit();

                editor.putInt(values.get(position).getId(),1);
                editor.apply();
            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnMinus.setVisibility(View.GONE);
                holder.btnPlus.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = context.getSharedPreferences(MainActivity.MY_PREFS_FILENAME,MODE_PRIVATE).edit();
                editor.putInt(values.get(position).getId(),0);
                editor.apply();
            }
        });

    }

    @Override
    public int getItemCount() {

        return values.size();
    }
}
