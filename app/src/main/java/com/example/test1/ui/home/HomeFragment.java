package com.example.test1.ui.home;

import static android.content.ContentValues.TAG;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.MainActivity;
import com.example.test1.databinding.FragmentHomeBinding;
import com.example.test1.databinder.holdername;
import com.example.test1.quiz.quizcreation;
import com.example.test1.upcoming.modelupcoming;
import com.example.test1.quiz.quiz;
import com.example.test1.upcoming.rviewupcoming;
import com.example.test1.ui.timer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class HomeFragment extends Fragment {
    RecyclerView rview;
    Button quiz;FirebaseFirestore db;String schedule;
    TextView nodata;
    EditText pin;
    EditText code;
    rviewupcoming adapter;
    Button create;
    String useremail,author,subject,time,date;
    ArrayList<modelupcoming> data;


    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        pin=binding.pin;
        code=binding.code;
        nodata=binding.nodata;
        db= FirebaseFirestore.getInstance();

        quiz=binding.quiz;
        create = binding.create;
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.example.test1.quiz.create.class);
                startActivity(intent);
            }
        });




        data = new ArrayList<modelupcoming>();
        TextView name = binding.name;
        FrameLayout loading =binding.loading;
        loading.setVisibility(View.GONE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        useremail=user.getEmail();
        if(holdername.getInstance().getData()==null||holdername.getInstance().getData().equals("null")){
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        name.setText("Hello, "+ holdername.getInstance().getData());



        rview=binding.rview;
//QUIZ DATA
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
                    loading.setVisibility(View.VISIBLE);

                    db.collection("data").document("quiz").collection(usercode+userpin)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if(task.getResult().isEmpty()){
                                            Toast.makeText(getActivity(), "Code is wrong", Toast.LENGTH_SHORT).show();
                                            loading.setVisibility(View.GONE);
                                            return;
                                        }

                                        Intent intent = new Intent(getActivity(), timer.class);
                                        intent.putExtra("code", usercode);
                                        intent.putExtra("pin", userpin);
                                        getotherdata(usercode,userpin);
                                        loading.setVisibility(View.GONE);
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



                }
        }
        });


        rview.setLayoutManager(new GridLayoutManager(container.getContext(),1,GridLayoutManager.HORIZONTAL,false));
getdata();
//        data.add(new modelupcoming("sa","sa","sa","sa"));
//        data.add(new modelupcoming("ssad","saas","sasd","dfda"));
//

        adapter=new rviewupcoming(getContext(),data);
        rview.setAdapter(adapter);
        rview.smoothScrollToPosition(0);




        return root;
    }


    public void getotherdata(String code,String pin){
        db.collection("data").document("quiz").collection(code+pin).document("data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        date =task.getResult().getString("date");
                        schedule = task.getResult().getString("schedule");


//                        Map<String, Object> user = new HashMap<>();
//                        user.put("schedule", schedule);

//                        db.collection("personal").document(useremail).collection("attempted")
//                                .add(user)
//                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                    @Override
//                                    public void onSuccess(DocumentReference documentReference) {
//                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.w(TAG, "Error adding document", e);
//                                    }
//                                });
//
                    }
                });

                    }

    public void getdata() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(getActivity(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        String email = user.getEmail();

        // Get current date and time
        Date currentDateTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String todayDate = dateFormat.format(currentDateTime);
        String currentTime = timeFormat.format(currentDateTime);

        db.collection("personal").document(email).collection("attempted")
                .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        data.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String timestamp = document.getString("timestamp");
                            if (timestamp != null) {
                                try {

                                    SimpleDateFormat fullFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
                                    Date quizDateTime = fullFormat.parse(timestamp);

                                    if (quizDateTime != null) {
                                        // Split into date and time
                                        String quizDate = dateFormat.format(quizDateTime);
                                        String quizTime = timeFormat.format(quizDateTime);

                                        if (quizDate.equals(todayDate)) {
                                            // Quiz is today, compare times
                                            if (quizTime.compareTo(currentTime) > 0) {
                                                // Future quiz today
                                                String author = document.getString("author");
                                                String subject = document.getString("subject");
                                                String time = document.getString("time");
                                                String date = document.getString("date");
                                                String fcode = document.getString("code");
                                                String pin=document.getString("pin");
                                                String finalcode= fcode+pin;

                                                data.add(new modelupcoming(author, subject, time, date, fcode,pin));
                                            }
                                        } else if (quizDateTime.after(currentDateTime)) {
                                            // Future quiz on another day
                                            String author = document.getString("author");
                                            String subject = document.getString("subject");
                                            String time = document.getString("time");
                                            String date = document.getString("date");
                                            String fcode = document.getString("code");
                                            String pin=document.getString("pin");
                                            String finalcode= fcode+pin;
                                            data.add(new modelupcoming(author, subject, time, date, fcode,pin));
                                        }
                                    }
                                } catch (ParseException e) {
                                    Log.e("Firestore", "Error parsing timestamp: " + timestamp, e);
                                }
                            }
                        }

                        // Update nodata visibility
                        if (data.isEmpty()) {
                            nodata.setVisibility(View.VISIBLE);
                        } else {
                            nodata.setVisibility(View.GONE);
                        }

                        // Notify adapter
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }
    public void onResume() {
        super.onResume();
        getdata();
        if(data.size()==0){
            nodata.setVisibility(View.VISIBLE);
        }
        else {
            nodata.setVisibility(View.GONE);
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}