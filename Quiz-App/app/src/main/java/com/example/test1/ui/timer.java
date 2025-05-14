package com.example.test1.ui;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.test1.R;
import com.example.test1.quiz.quiz;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class timer extends AppCompatActivity {
    FirebaseFirestore db;
    String timestamp;
    String code ,pin;


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
        db=FirebaseFirestore.getInstance();
        TextView timer = findViewById(R.id.timer);
        Intent intent= getIntent();
         code = intent.getStringExtra("code");
         pin = intent.getStringExtra("pin");
        db.collection("data").document("quiz").collection(code+pin).document("data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        timestamp = task.getResult().getString("timestamp");


                        if(timestamp==null){
                            Intent intent1 = new Intent(timer.this, quiz.class);
                            intent1.putExtra("code", code);
                            intent1.putExtra("pin", pin);
                            startActivity(intent);
                            finish();
                        }





                        if (timestamp != null) {
                            // Parse the timestamp from Firestore and the current time
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date fetchedDate = sdf.parse(timestamp); // Timestamp from Firestore
                                Date currentDate = new Date(); // Current time

                                // Calculate the difference in milliseconds
                                long timeDifferenceInMillis = fetchedDate.getTime() - currentDate.getTime();
                                if(timeDifferenceInMillis<0){
                                    timer.setText("sorry");
                                    Handler handler= new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();



                                        }
                                    },4000);
                                    return;
                                }
                                else {
                                    startCountdownTimer(timeDifferenceInMillis, timer);
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                                timer.setText("Error parsing timestamp.");
                            }
                        }
                    }
                });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String current = sdf.format(new Date());





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
}

