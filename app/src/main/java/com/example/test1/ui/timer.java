package com.example.test1.ui;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.test1.MainActivity;
import com.example.test1.R;
import com.example.test1.databinder.holderemail;
import com.example.test1.quiz.quiz;
import com.example.test1.quiz.quizcompleted;
import com.example.test1.quiz.quizperformcompleted;
import com.example.test1.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class timer extends AppCompatActivity {
    FirebaseFirestore db;
    String timestamp;
    TextView timer;
    String code ,pin;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String current = sdf.format(new Date());
    FrameLayout loading;
    String useremail;
    Date quizDate;Date currentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // Hides the ActionBar
        }
        setContentView(R.layout.activity_main);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loading=findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        db=FirebaseFirestore.getInstance();
         timer = findViewById(R.id.timer);
        Intent intent= getIntent();
         code = intent.getStringExtra("code");
         pin = intent.getStringExtra("pin");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
         useremail= user.getEmail();
         ifelegible(code,pin);

//        Toast.makeText(this, code, Toast.LENGTH_SHORT).show();







    }
    public void gettimestamp(){
        db.collection("data").document("quiz").collection(code+pin).document("data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        if(!task.getResult().exists()){
//                            Toast.makeText(timer.this, "Sorry Some Error Occurred", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        timestamp = task.getResult().getString("schedule");
                        String date = task.getResult().getString("date");
//                        Toast.makeText(timer.this, date, Toast.LENGTH_SHORT).show();
                        String time =task.getResult().getString("time");
                        if(task.isSuccessful()){
                            decide(date,time);
                        }


    }
    public void decide(String date,String time){
        if(date==null&&time==null){
            Intent intent1= new Intent(timer.this, quiz.class);
            intent1.putExtra("code",code);
            intent1.putExtra("pin",pin);
//
            startActivity(intent1);
            finish();
        }
        else if(date!=null&&time==null){
            Calendar today = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String todayDate = sdf.format(today.getTime());
//                            Toast.makeText(timer.this, todayDate, Toast.LENGTH_SHORT).show();
            try {
                 quizDate = sdf.parse(date);
                 currentDate = sdf.parse(todayDate);
                Log.d("time",String.valueOf(quizDate)+"      "+String.valueOf(currentDate));
//                                Toast.makeText(timer.this, String.valueOf(quizDate)+String.valueOf(currentDate), Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


            if (date.equals(todayDate)) {
                Intent intent1= new Intent(timer.this, quiz.class);
                intent1.putExtra("code",code);
                intent1.putExtra("pin",pin);
                intent1.putExtra("timestamp",date);
                startActivity(intent1);
                finish();


            }
//            if(quizDate==(null)){
//                Toast.makeText(timer.this, "quizdate", Toast.LENGTH_SHORT).show();
//
//            }
//            if(currentDate==null)
//                Toast.makeText(timer.this, "curertndate", Toast.LENGTH_SHORT).show();


                           else  if(quizDate.before(currentDate)){
//                                 Toast.makeText(timer.this, "shdahsoaihiahfbiudsbciudbscibds", Toast.LENGTH_SHORT).show();


                                loading.setVisibility(View.GONE);
                                timer.setText("Quiz has ended on "+ date);
                                Intent intent= new Intent(timer.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                                return;
//                                new Handler().postDelayed(() -> {
//                                    finish(); // Close the activity after showing the message
//                                }, 4000);

                            }
            else {
                loading.setVisibility(View.GONE);
                timer.setText("Quiz will take place on "+ date);
            }
        }
        else{
            both(timestamp);
        }



    }

                });
    }



    public void both(String timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        try {
            Date fetchedDate = sdf.parse(timestamp); // Timestamp from Firestore
            Date currentDate = new Date(); // Current time

            // Calculate the difference in milliseconds
            long timeDifferenceInMillis = fetchedDate.getTime() - currentDate.getTime();
            loading.setVisibility(View.GONE);
            if (timeDifferenceInMillis < 0) {
                timer.setText("Sorry, the scheduled time has passed.");
                Handler handler = new Handler();
                handler.postDelayed(() -> finish(), 4000);
                return;
            }

            sendinfo(code,pin,timestamp);
            if (timeDifferenceInMillis == 0) {
                Intent intent1= new Intent(timer.this, quiz.class);
                intent1.putExtra("code",code);
                intent1.putExtra("pin",pin);
                intent1.putExtra("timestamp",timestamp);
                startActivity(intent1);
                finish();
            }
            else {
                startCountdownTimer(timeDifferenceInMillis, timer);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            timer.setText("Error parsing timestamp.");
        }

    }
    private void startCountdownTimer(long timeInMillis, TextView timerText) {
        new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Format the remaining time and display it
                long seconds = (millisUntilFinished / 1000) % 60;
                long minutes = (millisUntilFinished / 1000) / 60;
                long hours = minutes / 60;
                minutes = minutes % 60;

                String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                timerText.setText(timeFormatted);
            }

            @Override
            public void onFinish() {
               Intent intent = new Intent(timer.this, quiz.class);
                intent.putExtra("code", code);
                intent.putExtra("pin", pin);
               startActivity(intent);
               finish();
            }
        }.start();
    }
    public void senddata(String code,String pin,String timestamp,String author,String subject,String time,String date){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user1= auth.getCurrentUser();
        String email= user1.getEmail();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
//        Toast.makeText(this, "dkasna", Toast.LENGTH_SHORT).show();
        user.put("code", code);
        user.put("status","no");
        user.put("pin", pin);
        user.put("timestamp",timestamp);
        user.put("subject",subject);
        user.put("author",author);
        user.put("time",time);
        user.put("date",date);
        db.collection("personal").document(email).collection("attempted")
                .document(timestamp)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(timer.this, "done", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }
    public void sendinfo(String code,String pin,String timestamp){
        db.collection("data").document("quiz").collection(code + pin).document("data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                             String   author=document.getString("author");
                              String  subject=document.getString("subject");
                              String  time= document.getString("time");
                             String   date=document.getString("date");
                                senddata(code,pin,timestamp,author,subject,time,date);


                            } else {

                            }
                        } else {
                            Log.w(TAG, "Error getting document.", task.getException());
                        }
                    }
                });

    }
    public void ifelegible(String code,String pin){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("data")
                .document("quiz")
                .collection(code + pin)
                .document("participant")
                .collection("data")
                .document(useremail);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
//                    Toast.makeText(timer.this, "Sorry you have given the quiz before", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(timer.this, quizperformcompleted.class);
                    intent.putExtra("timer","timer");
                    startActivity(intent);
                    finish();

                } else {
                    gettimestamp();
                    return;

                }
            } else {
                Log.e("Firestore", "Error getting document", task.getException());
            }
        });









//        db.collection("data").document("quiz").collection(code+pin).document("participant").collection("data").document(useremail)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                        if(em.equals(em)) Toast.makeText(timer.this, "yes", Toast.LENGTH_SHORT).show();
////                        Toast.makeText(quiz.this, useremail, Toast.LENGTH_SHORT).show();
//                        if(em.equals(useremail)){
//
//
//                        }
//                        else{
//                            gettimestamp();
//                            return;
//
//                        }
//
//
//
//                    }
//                });
    }
    public void onBackPressed() {
        // Handle back press explicitly
        super.onBackPressed();
    }
}

