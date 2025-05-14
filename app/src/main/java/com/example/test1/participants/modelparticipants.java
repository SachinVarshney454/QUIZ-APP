package com.example.test1.participants;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.test1.MainActivity;
import com.example.test1.R;
import com.example.test1.authentication.logicscreen;
import com.example.test1.databinder.holdername;
import com.example.test1.databinder.singeltonquizzes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class modelparticipants {
    public modelparticipants(String name, String email, String marks) {
        this.name = name;
        this.email = email;
        this.marks = marks;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    String name;
    String email;
    String marks;


    public static class mainlogo extends AppCompatActivity {
        Handler handler;
        FirebaseFirestore db;
        FirebaseAuth auth;
        LottieAnimationView loading;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_mainlogo);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            singeltonquizzes.getInstance().clearItems();

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            loading =findViewById(R.id.loading);
            loading.playAnimation();

            auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            db = FirebaseFirestore.getInstance();
            if(user!=null){
                getid();
            }
            else{ Intent intent = new Intent(mainlogo.this, logicscreen.class);
                startActivity(intent);
                finish();
            }



            handler = new Handler();

    //
    //        handler.postDelayed(new Runnable() {
    //            @Override
    //            public void run() {
    //            // Transition to another activity after 2 seconds (2000 milliseconds)
    //                if(user!=null) {
    //            Intent intent = new Intent(mainlogo.this, MainActivity.class);
    //            startActivity(intent);
    //                }
    //                else
    //                startActivity(new Intent(mainlogo.this, logicscreen.class));
    //            finish(); // Optionally finish this activity if you don't want to return to it
    //            }
    //        }, 2000);
    //
    //
            }
            public void getid() {
                String email1 = auth.getCurrentUser().getEmail();
                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getString("email").equals(email1)) {
                                            String name = document.getString("name");
                                            holdername.getInstance().setData(name);
                                            Intent intent = new Intent(mainlogo.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }
                                } else {

                                }
                            }
                        });
            }

    }
}
