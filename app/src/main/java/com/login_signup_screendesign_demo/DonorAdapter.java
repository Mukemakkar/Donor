package com.login_signup_screendesign_demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DonorViewHolder> {

    private Context mCtx;
    private List<Donor> DonorList;

    public DonorAdapter(Context mCtx, List<Donor> DonorList) {
        this.mCtx = mCtx;
        this.DonorList = DonorList;
    }

    @NonNull
    @Override
    public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_list, parent, false);
        return new DonorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonorViewHolder holder, int position) {
        Donor Donor = DonorList.get(position);
        holder.textViewName.setText(Donor.name);
        holder.textViewGenre.setText("Email: " + Donor.email);
        holder.textViewAge.setText("Phone: " + Donor.phone);
        holder.textViewCountry.setText("Address: " + Donor.address);
    }

    @Override
    public int getItemCount() {
        return DonorList.size();
    }

    class DonorViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewGenre, textViewAge, textViewCountry;

        public DonorViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.text_view_name);
            textViewGenre = (TextView) itemView.findViewById(R.id.text_view_email);
            textViewAge = (TextView) itemView.findViewById(R.id.text_view_phone);
            textViewCountry = (TextView) itemView.findViewById(R.id.text_view_address);
        }
    }
}
