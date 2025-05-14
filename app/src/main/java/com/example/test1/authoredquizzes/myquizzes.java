package com.example.test1.authoredquizzes;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.test1.R;
import com.example.test1.quiz.create;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class myquizzes extends Fragment {

    private MyquizzesViewModel mViewModel;
    private RecyclerView rview; // RecyclerView reference
    private ArrayList<modelmyquizzes> data = new ArrayList<>();
    private adaptermyquizzes adapter;
    FrameLayout dataarrived,loading;
    String email;
    FirebaseFirestore db ;
    TextView nodata;

    public static myquizzes newInstance() {
        return new myquizzes();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View rootView = inflater.inflate(R.layout.fragment_myquizzes, container, false);
        db= FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
         loading = rootView.findViewById(R.id.loading);
         dataarrived= rootView.findViewById(R.id.data);
        loading.setVisibility(VISIBLE);
        dataarrived.setVisibility(GONE);
        FirebaseUser user = auth.getCurrentUser();
        email=user.getEmail();
        Button quiz=rootView.findViewById(R.id.quiz);
        quiz.setTextColor(Color.parseColor("#FFFFFF"));
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiz.setEnabled(false);
                Intent intent =new Intent(getActivity(), create.class);
                startActivity(intent);
            }
        });
        nodata=rootView.findViewById(R.id.nodata);


        rview = rootView.findViewById(R.id.rview);
        rview.setLayoutManager(new LinearLayoutManager(getContext()));
        getdata();




        adapter = new adaptermyquizzes(getContext(), data);
        rview.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyquizzesViewModel.class);
        // TODO: Use the ViewModel
    }
    public void getdata(){
        db.collection("personal").document(email).collection("created")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Toast.makeText(getActivity(), "sdasidn", Toast.LENGTH_SHORT).show();
                                if(Objects.equals(document.getString("finish"), "true")) {
                                    data.add(new modelmyquizzes(document.getString("subject"), document.getString("author"), document.getString("time"), document.getString("date"), document.getString("code")));
                                }

                            }
                            loading.setVisibility(GONE);
                            dataarrived.setVisibility(VISIBLE);
                            if(data.size()==0){
                                nodata.setVisibility(VISIBLE);
                            }
                            else {
                                nodata.setVisibility(GONE);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
//                            Toast.makeText(getActivity(), email, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Querying Firestore path: /personal/" + email + "/created");

//                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}