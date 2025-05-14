package com.example.test1.upcoming;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.example.test1.ui.timer;

import java.util.ArrayList;

public class rviewupcoming extends RecyclerView.Adapter<rviewupcoming.ViewHolder> {
    public rviewupcoming(Context context, ArrayList<modelupcoming> data) {
        this.context = context;
        this.data = data;
    }

    Context context;


    ArrayList<modelupcoming>data ;
    @NonNull
    @Override
    public rviewupcoming.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_upcominglayout,parent,false);
        ViewHolder view  = new ViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull rviewupcoming.ViewHolder holder, int position) {
        holder.date.setText(data.get(position).data);
        holder.time.setText(data.get(position).time);
        holder.author.setText(data.get(position).author);
        holder.name.setText(data.get(position).name);
        holder.code.setText(data.get(position).code);
        holder.pin.setText(data.get(position).pin);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = holder.code.getText().toString();
                String pin=holder.pin.getText().toString();
                Intent intent = new Intent(context, timer.class);
                intent.putExtra("code",code);
                intent.putExtra("pin",pin);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView date,time,author,name,code,pin;
        Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            code = itemView.findViewById(R.id.code);
            time=itemView.findViewById(R.id.time);
            button=itemView.findViewById(R.id.button);
            pin=itemView.findViewById(R.id.pin);


            author=itemView.findViewById(R.id.author);
            name=itemView.findViewById(R.id.name);
        }
    }
}
