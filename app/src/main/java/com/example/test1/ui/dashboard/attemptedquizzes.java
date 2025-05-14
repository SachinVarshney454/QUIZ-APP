package com.example.test1.ui.dashboard;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.elapsedquizzes.adapterquizzes;
import com.example.test1.databinding.FragmentDashboardBinding;
import com.example.test1.elapsedquizzes.modelquizzes;
import com.example.test1.databinder.singeltonquizzes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class attemptedquizzes extends Fragment {
    adapterquizzes adapter;RecyclerView rview;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    String email;
    String max;
    int i=0;
    static boolean cond=false;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        TextView textView = binding.title;
//        textView.setText("Create Quiz");
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Adjust text size
//        textView.setTypeface(textView.getTypeface(), Typeface.BOLD); // Make text bold

//


        db= FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        email  = user.getEmail();
        FrameLayout loading = binding.loading;
        FrameLayout dataview= binding.dataview;
        loading.setVisibility(VISIBLE);
        dataview.setVisibility(GONE);
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
        loading.setVisibility(GONE);
        dataview.setVisibility(VISIBLE);
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
    String maxmarks;

    public void getdata() {
        singeltonquizzes.getInstance().clearItems();
        db.collection("personal").document(email).collection("attempted")
                .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        singeltonquizzes.getInstance().clearItems();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String statuscheck = Objects.requireNonNullElse(document.getString("status"), "no");




//                            if (statuscheck.equals("no")||document.getString("status").isEmpty() || document.getString("status").equals("no")) {
//
//                            } else {
                                String author = document.getString("author");
                                String subject = document.getString("subject");
                                String time = document.getString("time");
                                String date = document.getString("date");
                                String status = document.getString("status");
                                String marks = document.getString("marks");


                                maxmarks = document.getString("maxmark");
//                                Toast.makeText(getContext(), String.valueOf(maxmarks), Toast.LENGTH_SHORT).show();
                                String finalmark = "";


                                if (Objects.equals(marks, null)) {
                                    finalmark = "no marks available";
                                } else {
                                    finalmark = marks + "/" + maxmarks;
                                    if(maxmarks==("null")) finalmark="sorry no data available";
                                }

                                if (marks == null) {
                                    marks = "nodata";
                                }

                                singeltonquizzes.getInstance().addItem(subject, author, time, date, status, finalmark);
                            }
                            adapter.notifyDataSetChanged();
                        }
//                    }
                });

    }
//    public void getmaxmarks(){
//        db.collection("data").document("quiz").collection(code).document("data")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                         maxmarks = task.getResult().getString("number");
//
//
//
//                    }
//                });
//    }

//    public  void totalmarks(){
//        db.collection("data").document("quiz").collection(code+pin).document("data")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                         max= task.getResult().getString("number");
//                    }
//                });
//    }
    public void onResume(){
        super.onResume();
        getdata();

    }


}