package com.example.test1.elapsedquizzes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;

import java.util.ArrayList;

public class adapterquizzes extends RecyclerView.Adapter<adapterquizzes.ViewHolder> {
    public adapterquizzes(ArrayList<modelquizzes> data) {
        this.data = data;
    }

    ArrayList<modelquizzes> data;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pastquizzes1,parent,false);
       ViewHolder view  = new ViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(data.get(position).date);
        holder.time.setText(data.get(position).time);
        holder.author.setText(data.get(position).author);
        holder.name.setText(data.get(position).name);
        holder.marks.setText(data.get(position).marks);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView time;
        TextView author;
        TextView name;
        TextView marks;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            marks=itemView.findViewById(R.id.marks);
            time=itemView.findViewById(R.id.time);
            author=itemView.findViewById(R.id.author);
            name=itemView.findViewById(R.id.name);

        }
    }
}
