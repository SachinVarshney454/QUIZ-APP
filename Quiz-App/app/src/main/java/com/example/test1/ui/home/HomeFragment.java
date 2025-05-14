package com.example.test1.ui.home;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.databinding.FragmentHomeBinding;
import com.example.test1.databinder.holdername;
import com.example.test1.upcoming.modelupcoming;
import com.example.test1.quiz.quiz;
import com.example.test1.upcoming.rviewupcoming;
import com.example.test1.ui.timer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {
    RecyclerView rview;
    Button quiz;FirebaseFirestore db;
    EditText pin;
    EditText code;
    rviewupcoming adapter;
    String useremail;ArrayList<modelupcoming> data;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        pin=binding.pin;
        code=binding.code;
        db= FirebaseFirestore.getInstance();

        quiz=binding.quiz;




        data = new ArrayList<modelupcoming>();
        TextView name = binding.name;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        useremail=user.getEmail();
        name.setText("Hello, "+ holdername.getInstance().getData());



        rview=binding.rview;

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usercode=code.getText().toString();
                String userpin=pin.getText().toString();
                if(usercode.isEmpty() || userpin.isEmpty()){
                    Toast.makeText(getActivity(), "Enter Code and Pin", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                    db.collection("data").document("quiz").collection(usercode+userpin)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if(task.getResult().isEmpty()){
                                            Toast.makeText(getActivity(), "Code is wrong", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        Intent intent = new Intent(getActivity(), quiz.class);
                                        intent.putExtra("code", usercode);
                                        intent.putExtra("pin", userpin);
                                        startActivity(intent);
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, document.getId() + " => " + document.getData());



                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();

                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }
                                }
                            });



                }}
        });

        rview.setLayoutManager(new GridLayoutManager(container.getContext(),1,GridLayoutManager.HORIZONTAL,false));
        getdata();
        data.add(new modelupcoming("sa","sa","sa","sa"));
        data.add(new modelupcoming("ssad","saas","sasd","dfda"));
//

        adapter=new rviewupcoming(data);
        rview.setAdapter(adapter);
        rview.smoothScrollToPosition(0);




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void getdata(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String email =user.getEmail();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String schedule= sdf.format(new Date());
        db.collection("personal").document(email).collection("attempted")
                .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            if(schedule.compareTo(document.getString("timestamp"))<0){
//                                String author = document.getString("author");
//                                String subject = document.getString("subject");
//                                String time = document.getString("time");
//                                String date = document.getString("date");
////                                String status = document.getString("status");
//                                data.add(new modelupcoming(author,subject,time,date));
//                            }
                            if (document.getString("timestamp") != null && schedule.compareTo(document.getString("timestamp")) < 0) {

                                String author = document.getString("author");
                                String subject = document.getString("subject");
                                String time = document.getString("time");
                                String date = document.getString("date");
                                data.add(new modelupcoming(author, subject, time, date));
                            }

                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        // Log error and show a toast if the task fails
                        Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });}
}