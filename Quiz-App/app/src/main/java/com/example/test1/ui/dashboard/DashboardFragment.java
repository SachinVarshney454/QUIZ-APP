package com.example.test1.ui.dashboard;

import static android.content.ContentValues.TAG;

import static com.google.firebase.firestore.DocumentChange.Type.ADDED;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.elapsedquizzes.adapterquizzes;
import com.example.test1.quiz.create;
import com.example.test1.databinding.FragmentDashboardBinding;
import com.example.test1.elapsedquizzes.modelquizzes;
import com.example.test1.databinder.singeltonquizzes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class DashboardFragment extends Fragment {
    adapterquizzes adapter;RecyclerView rview;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    String email;
    int i=0;
    static boolean cond=false;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button quiz=binding.quiz;
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), create.class);
                startActivity(intent);
            }
        });
        db= FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        email  = user.getEmail();

        if(cond==false){
            getdata();
            cond=true;
        }
//        else {
//            db.collection("personal").document(email).collection("attempted")
//                    .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
//                    .addSnapshotListener((snapshots, e) -> {
//                        if (e != null) {
//                            Log.w(TAG, "Listen failed.", e);
//                            return;
//                        }
//
//                        if (snapshots != null) {
//                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
//                                if (dc.getType() == DocumentChange.Type.ADDED) {
//                                    Log.d(TAG, "New document added: " + dc.getDocument().getData());
//
//                                    // Extract data from the added document
//                                    String author = dc.getDocument().getString("author");
//                                    String subject = dc.getDocument().getString("subject");
//                                    String time = dc.getDocument().getString("time");
//                                    String date = dc.getDocument().getString("date");
//                                    String status = dc.getDocument().getString("status");
//
//
//                                        singeltonquizzes.getInstance().addItem(subject, author, time, date, status);
//                                        adapter.notifyDataSetChanged(); // Notify the adapter of the change
//
//                                }
//                            }
//                        }
//                    });

//        }
        ArrayList<modelquizzes> data;
//        getdata();
        data= singeltonquizzes.getInstance().getArrayList();
         rview = binding.rviewquiz;
         adapter = new adapterquizzes(data);
        rview.setLayoutManager(new LinearLayoutManager(getContext()));
        rview.setAdapter(adapter);



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getdata() {
        singeltonquizzes.getInstance().clearItems();
        db.collection("personal").document(email).collection("attempted")
                .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {


                            String author = document.getString("author");
                            String subject = document.getString("subject");
                            String time = document.getString("time");
                            String date = document.getString("date");
                            String status = document.getString("status");

                            singeltonquizzes.getInstance().addItem(subject, author, time, date, status);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // Log error and show a toast if the task fails
                        Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }

}