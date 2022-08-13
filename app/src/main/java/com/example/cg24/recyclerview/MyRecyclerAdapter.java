package com.example.cg24.recyclerview;


import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cg24.R;

import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{
    private ArrayList<Data> data;

    @NonNull
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.ViewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    public void setData(ArrayList<Data> list){
        this.data = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (data != null? data.size() : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView point;
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            point = (TextView) itemView.findViewById(R.id.point);
            date = (TextView) itemView.findViewById(R.id.date);
        }

        void onBind(Data item){
            point.setText(item.getPoint());
            date.setText(item.getDate());
        }

    }
}

