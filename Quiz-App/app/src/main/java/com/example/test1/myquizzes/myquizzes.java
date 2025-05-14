package com.example.test1.authoredquizzes;

import static android.content.ContentValues.TAG;


import androidx.lifecycle.ViewModelProvider;

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
import android.widget.Toast;

import com.example.test1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class myquizzes extends Fragment {

    private MyquizzesViewModel mViewModel;
    private RecyclerView rview; // RecyclerView reference
    private ArrayList<modelmyquizzes> data = new ArrayList<>();
    private adaptermyquizzes adapter;
    String email;
    FirebaseFirestore db ;

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
        FirebaseUser user = auth.getCurrentUser();
        email=user.getEmail();

        rview = rootView.findViewById(R.id.rview);
        rview.setLayoutManager(new LinearLayoutManager(getContext()));
        getdata();
//        data.add(new modelmyquizzes("sa","dsad","ggr","ht","gr"));



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
                                Toast.makeText(getActivity(), "sdasidn", Toast.LENGTH_SHORT).show();
                                data.add(new modelmyquizzes(document.getString("subject"), document.getString("author"), document.getString("time"), document.getString("date"),document.getString("code")));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), email, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Querying Firestore path: /personal/" + email + "/created");

//                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}