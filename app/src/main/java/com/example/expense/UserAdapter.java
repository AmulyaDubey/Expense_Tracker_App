package com.example.expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    
    ArrayList<Expenditure> profiles;
    Context context;
    
     class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView mOption, mDate, mAmt;
        RecyclerView parentlayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            mOption = itemView.findViewById(R.id.mOption);
            mAmt = itemView.findViewById(R.id.mAmt);
            mDate = itemView.findViewById(R.id.mDate);
            parentlayout = itemView.findViewById(R.id.recyclerView);
        }
    }

    public UserAdapter(Context c , ArrayList<Expenditure> p)
    {
        context = c;
        profiles = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.items_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.mOption.setText(profiles.get(position).getOption());
        holder.mDate.setText(profiles.get(position).getDate());
        holder.mAmt.setText(profiles.get(position).getAmt());
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }




}
