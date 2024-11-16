package com.example.findblood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder> {

    private List<Donor> donationList;

    // Constructor to accept a list of donors
    public DonationAdapter(List<Donor> donationList) {
        this.donationList = donationList;
    }

    @NonNull
    @Override
    public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donation, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {
        Donor donor = donationList.get(position);
        holder.name.setText(donor.getName());
        holder.contact.setText(donor.getContact());
        holder.age.setText("Tuổi: " + donor.getAge());
        holder.bloodType.setText(donor.getBloodType());

        // Set the first letter of the name as the avatar
        String avatarText = donor.getName().substring(0, 1).toUpperCase();
        holder.avatar.setText(avatarText);
    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder {
        TextView avatar, name, contact, age, bloodType;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.donor_name);
            contact = itemView.findViewById(R.id.donor_contact);
            age = itemView.findViewById(R.id.donor_age);
            bloodType = itemView.findViewById(R.id.donor_blood_type);
        }
    }
}
