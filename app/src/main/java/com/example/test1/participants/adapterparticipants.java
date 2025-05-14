package com.example.test1.participants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;

import java.util.ArrayList;


public class adapterparticipants extends RecyclerView.Adapter<adapterparticipants.ViewHolder> {
    ArrayList<modelparticipants>data;

    public adapterparticipants(Context context, ArrayList<modelparticipants> data) {
        this.context = context;
        this.data = data;
    }

    Context context;

    @NonNull
    @Override
    public adapterparticipants.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layoutparticipants, parent, false);
        ViewHolder view = new ViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull adapterparticipants.ViewHolder holder, int position) {
        holder.email.setText(data.get(position).email);
        holder.name.setText(data.get(position).name);
        holder.marks.setText(data.get(position).marks);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,email,marks;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             name = itemView.findViewById(R.id.name);
             email=itemView.findViewById(R.id.email);
             marks=itemView.findViewById(R.id.marks);
        }
    }
}
