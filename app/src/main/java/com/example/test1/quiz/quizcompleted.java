package com.example.test1.quiz;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.test1.MainActivity;
import com.example.test1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class quizcompleted extends AppCompatActivity {
    TextView tcode,tpin,tdate,ttime,texttime,textdate;
    FirebaseFirestore db;
    String time,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quizcreationcompleted);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LottieAnimationView anim = findViewById(R.id.doneanim);
        anim.playAnimation();
        db= FirebaseFirestore.getInstance();
        Intent intent =getIntent();
       String code =  intent.getStringExtra("code");
      String pin=  intent.getStringExtra("pin");
//      String time = intent.getStringExtra("time");
//
//      String date = intent.getStringExtra("date");
        getdata(code,pin);

      tcode = findViewById(R.id.code);
      textdate=findViewById(R.id.textdate);
      texttime=findViewById(R.id.texttime);
      tpin= findViewById(R.id.pin);
      tdate = findViewById(R.id.date);
      ttime= findViewById(R.id.time);






      if( time == null){
          ttime.setVisibility(GONE);
          texttime.setVisibility(GONE);
      }
      else ttime.setText(time);

      if(date == null){
          tdate.setVisibility(GONE);
          textdate.setVisibility(GONE);
      }
      else tdate.setText(date);

      tcode.setText(code);
      tpin.setText(pin);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(quizcompleted.this, MainActivity.class); // Replace MainActivity with your target activity
        startActivity(intent);
        quizcompleted.this.finishAffinity();

    }
    public void getdata(String code,String pin){
        db.collection("data").document("quiz").collection(code + pin).document("data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                time = document.getString("time");
                                date=document.getString("date");


                            } else {

                            }
                        } else {
                            Log.w(TAG, "Error getting document.", task.getException());
                        }
                    }
                });

    }

}