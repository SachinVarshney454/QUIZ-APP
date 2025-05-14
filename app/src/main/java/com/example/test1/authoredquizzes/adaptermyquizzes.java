package com.example.test1.authoredquizzes;

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
import com.example.test1.participants.participants;

import java.util.ArrayList;

public class adaptermyquizzes extends RecyclerView.Adapter<adaptermyquizzes.ViewHolder> {
    Context context;

    ArrayList<modelmyquizzes> data;

    public adaptermyquizzes(Context context, ArrayList<modelmyquizzes> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public adaptermyquizzes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(context).inflate(R.layout.layoutmyquizzes,parent,false);
        adaptermyquizzes.ViewHolder view = new ViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull adaptermyquizzes.ViewHolder holder, int position) {
        holder.subject.setText(data.get(position).getSubject());
        holder.author.setText(data.get(position).getAuthor());
        holder.date.setText(data.get(position).date);
        holder.time.setText(data.get(position).time);
        holder.code.setText(data.get(position).code);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, participants.class);
             String code = holder.code.getText().toString();
             intent.putExtra("code",code);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
       return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView code;TextView subject;TextView author;TextView time;TextView date;
        Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             subject=itemView.findViewById(R.id.subject);
             code=itemView.findViewById(R.id.code);
             author=itemView.findViewById(R.id.author);
             time=itemView.findViewById(R.id.time);
             date=itemView.findViewById(R.id.date);
             button=itemView.findViewById(R.id.button);

        }
    }
}
