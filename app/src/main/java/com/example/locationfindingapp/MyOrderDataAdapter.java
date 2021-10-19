package com.example.locationfindingapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class MyOrderDataAdapter extends RecyclerView.Adapter<MyOrderDataAdapter.ViewHolder>{
    private  ArrayList<OrderData> values;

    public MyOrderDataAdapter(Context context, ArrayList<OrderData> values) {
        this.values = values;
        //this.context=context;
        //activity=(MyOrderDataAdapter.ItemClicked) context;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvOItem,tvOId,tvOPrice;
        public ViewHolder(View itemView){
            super(itemView);

           tvOId=itemView.findViewById(R.id.tvOId);
           tvOItem=itemView.findViewById(R.id.tvOItemDet);
           tvOPrice=itemView.findViewById(R.id.tvOPrice);

        }

    }



    @NonNull
    @Override
    public MyOrderDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_carddesign,parent,false);

        return new MyOrderDataAdapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyOrderDataAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(values.get(position));
        holder.tvOId.setText(values.get(position).getOrderId());
        holder.tvOPrice.setText("Price: "+(values.get(position).getPrice()));
        holder.tvOItem.setText(values.get(position).getItemDetail());
    }

    @Override
    public int getItemCount() {

        return values.size();
    }

}
