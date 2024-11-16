package com.example.findblood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DonateCenterAdapter extends RecyclerView.Adapter<DonateCenterAdapter.DonateCenterViewHolder> {

    private List<DonateCenter> donateCenterList;

    public DonateCenterAdapter(List<DonateCenter> donateCenterList) {
        this.donateCenterList = donateCenterList;
    }

    @NonNull
    @Override
    public DonateCenterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donate_center, parent, false);
        return new DonateCenterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonateCenterViewHolder holder, int position) {
        DonateCenter donateCenter = donateCenterList.get(position);
        holder.name.setText(donateCenter.getName());
        holder.phone.setText(donateCenter.getPhone());
        holder.address.setText(donateCenter.getAddress());

        Glide.with(holder.image.getContext())
             .load(donateCenter.getImage())
             .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return donateCenterList.size();
    }

    public static class DonateCenterViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, phone;
        ImageView image;

        public DonateCenterViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.donate_center_image);
            name = itemView.findViewById(R.id.donate_center_name);
            address = itemView.findViewById(R.id.donate_center_address);
            phone = itemView.findViewById(R.id.donate_center_phone);
        }
    }
}
