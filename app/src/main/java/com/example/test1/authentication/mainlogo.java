package com.example.test1.authentication;

import android.content.Intent;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.test1.MainActivity;
import com.example.test1.R;
import com.example.test1.databinder.holderemail;
import com.example.test1.databinder.holdername;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.remote.ConnectivityMonitor;

public class mainlogo extends AppCompatActivity {
    Handler handler;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;

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
        auth = FirebaseAuth.getInstance();
         user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Toast.makeText(this, "ustreteer", Toast.LENGTH_SHORT).show();
         if(user!=null){
             Toast.makeText(this, "user", Toast.LENGTH_SHORT).show();
             getid();


         }
         else{
             startActivity(new Intent(mainlogo.this, logicscreen.class));
             Toast.makeText(this, "nouser", Toast.LENGTH_SHORT).show();
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


        }
        public void getid(){
        String email1 = auth.getCurrentUser().getEmail();
            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                  if(document.getString("email").equals(email1)){
                                      String id=document.getId();
                                      holderemail.getInstance().setData(id);
                                      holdername.getInstance().setData(document.getString("name"));
                                          Intent intent = new Intent(mainlogo.this, MainActivity.class);
                                          startActivity(intent);
                                          break;
                                  }
                                  else{
                                      Intent intent = new Intent(mainlogo.this, logicscreen.class);
                                      startActivity(intent);

                                  }
                                }
                            } else {
                                Intent intent = new Intent(mainlogo.this, nointernet.class);
                                startActivity(intent);

                            }
                        }
                    });
        }


}