    package com.example.test1.quiz;

    import static android.content.ContentValues.TAG;
    import static android.view.View.GONE;

    import android.content.Intent;
    import android.os.Bundle;
    import android.os.CountDownTimer;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.FrameLayout;
    import android.widget.RadioButton;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.activity.EdgeToEdge;
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import com.example.test1.R;
    import com.example.test1.databinder.holdername;
    import com.example.test1.ui.home.HomeFragment;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.SetOptions;

    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Objects;

    public class quiz extends AppCompatActivity {
        FirebaseFirestore db; FirebaseAuth auth;  FirebaseUser user;
        String useremail;
        String choosen;
    //    ProgressBar progress= findViewById(R.id.progress);
        Button next,prev;
        String usercode;String userpin;
        TextView question,test,a,b,c,d,count,subname;
        RadioButton optiona; RadioButton optionb; RadioButton optionc;  RadioButton optiond;
        String author,subject,time,date;String timestamp;
        String strsubname;
        String duration;
        Button submit,finish;

        int i=0; Map<Integer,String> map;
        int marks=0;Map<Integer,Integer> marksdata= new HashMap<>();
        String numdata,email;
        int numques;
        String maxmarks="null";
        String formattedTimestamp;
        TextView timer; //
        CountDownTimer countDownTimer;
        FrameLayout loading;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_quiz);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            //performing quiz

            prev=findViewById(R.id.prev);
            submit=findViewById(R.id.submit);
            question=findViewById(R.id.question);
            next=findViewById(R.id.next);
            subname= findViewById(R.id.subname);
            loading=findViewById(R.id.loading);
    //       5

            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            useremail=user.getEmail();
            timer = findViewById(R.id.timer);
            finish=findViewById(R.id.finish);
            count= findViewById(R.id.count);



            Intent intent= getIntent();
            usercode=  intent.getStringExtra("code");
            userpin=  intent.getStringExtra("pin");
    //        timestamp = intent.getStringExtra("timestamp");
            db= FirebaseFirestore.getInstance();



            otherdata(usercode,userpin);

            optiona=findViewById(R.id.ra);
            optionb=findViewById(R.id.rb);
            optionc=findViewById(R.id.rc);
            optiond=findViewById(R.id.rd);
            a=findViewById(R.id.a);
            b=findViewById(R.id.b);
            c=findViewById(R.id.c);
            d=findViewById(R.id.d);
            map = new HashMap<>();
            map.clear();
            optiona.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setback();
                    optiona.setChecked(true);
                }
            });
            optionb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setback();
                    optionb.setChecked(true);
                }
            });
            optionc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setback();
                    optionc.setChecked(true);
                }
            });
            optiond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setback();
                    optiond.setChecked(true);
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    submit.performClick();

                    submit.setText("Submit");
                    nextques(usercode,userpin,"next");
                    next.setEnabled(false);
                    prev.setEnabled(false);

                }
            });
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prev.setEnabled(false);
                    nextques(usercode,userpin,"prev");
                }
            });
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(optiona.isChecked()){
                        choosen = a.getText().toString();
                    }
                    else if(optionb.isChecked()){
                        choosen = b.getText().toString();

                    }
                    else if(optionc.isChecked()){
                        choosen = c.getText().toString();
                    }
                    else if(optiond.isChecked()){
                        choosen = d.getText().toString();
                    }
                    else{
                        Toast.makeText(quiz.this, "Please check any answer", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    submit.setText("Submitted");
    //                Toast.makeText(quiz.this, "map "+String.valueOf(i), Toast.LENGTH_SHORT).show();
                    map.put(i,"yes");

                    personalchoice(choosen,usercode,userpin,useremail);
                    checkanswer(choosen,usercode,userpin,useremail);
                }
            });
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit.performClick();
                    finish.setEnabled(false);

                    sendmarks(usercode,userpin);
                    datatopersonal("","","","","finish");
//                    sendmarkstopersonal(usercode,userpin);
                    storepersonaldatatoauthor(usercode,userpin,useremail);
                    sendfinish(usercode,userpin,"ff");

                }
            });

        }

        public void otherdata(String code, String pin) {
            db.collection("data").document("quiz").collection(code + pin).document("data")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                numdata = document.getString("number");
                                timestamp = document.getString("schedule");
                                duration = document.getString("duration");
                                numques = Integer.parseInt(numdata);
                                maxmarks = document.getString("number");
                                strsubname = document.getString("subject");

                                subname.setText(strsubname);

                                sendinfo(usercode, userpin);
                                startTimer(duration);
                                sendfinish(usercode, userpin, "");
                                nextques(code, pin, "");
                            }
                        }
                    });
        }

        // Navigate Questions
        public void nextques(String usercode, String userpin, String ref) {
            i = ref.equals("prev") ? i - 1 : i + 1;

            count.setText(i + " of " + numques);
            next.setVisibility(i == numques ? GONE : View.VISIBLE);
            prev.setVisibility(i > 1 ? View.VISIBLE : GONE);

            if (map.containsKey(i)) {
//                Toast.makeText(this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                chechpreviousmarked(i);
            } else {
                submit.setText("Submit");
                setback();
            }

            db.collection("data").document("quiz").collection(usercode + userpin).document(String.valueOf(i))
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                question.setText(document.getString("question"));
                                a.setText(document.getString("a"));
                                b.setText(document.getString("b"));
                                c.setText(document.getString("c"));
                                d.setText(document.getString("d"));

                                loading.setVisibility(GONE);
                                next.setEnabled(true);
                                prev.setEnabled(true);
                            } else {
                                i--;
                                next.setVisibility(GONE);
                            }
                        }
                    });
        }

        // Submit user's selected option
        public void personalchoice(String choice, String usercode, String userpin, String email) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user1 = auth.getCurrentUser();
            email = user1.getEmail();

            Map<String, Object> user = new HashMap<>();
            user.put(String.valueOf(i), choice);

            String dt = timestamp.equals("null null") ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) : timestamp;

            db.collection("personal").document(email).collection("attempted").document(timestamp)
                    .set(user, SetOptions.merge());
        }

        // Store result in author's dataset
        public void storepersonaldatatoauthor(String code, String pin, String email) {
            Map<String, Object> user = new HashMap<>();
            user.put("email", email);
            user.put("marks", String.valueOf(marks));
            user.put("name", holdername.getInstance().getData());

            db.collection("data").document("quiz").collection(code + pin).document("participant")
                    .collection("data").document(useremail).set(user);
        }

        // Reset options
        public void setback() {
            optiona.setChecked(false);
            optionb.setChecked(false);
            optionc.setChecked(false);
            optiond.setChecked(false);
        }

        // Save quiz info to personal record
        public void sendinfo(String code, String pin) {
            db.collection("data").document("quiz").collection(code + pin).document("data")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                author = document.getString("author");
                                subject = document.getString("subject");
                                time = document.getString("time");
                                date = document.getString("date");
                                datatopersonal(author, subject, time, date, "");
                            }
                        }
                    });
        }

        // Check selected answer
        public void checkanswer(String choosen, String code, String pin, String email) {
            db.collection("data").document("quiz").collection(code + pin).document(String.valueOf(i))
                    .get()
                    .addOnCompleteListener(task -> {
                        String selected = task.getResult().getString("selected");
                        if (selected.equals(choosen)) {
                            if (marksdata.containsKey(i) && marksdata.get(i) == 0) {
                                marks++;
                                marksdata.put(i, 1);
                            } else if (!marksdata.containsKey(i)) {
                                marks++;
                                marksdata.put(i, 1);
                            }
                        }
                    });
        }

        // Save quiz meta or result to personal collection
        public void datatopersonal(String author, String subject, String time, String date, String ref) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user1 = auth.getCurrentUser();
            String email = user1.getEmail();

            Map<String, Object> user = new HashMap<>();
            if (ref.equals("")) {
                user.put("author", author);
                user.put("subject", subject);
                user.put("time", time);
                user.put("date", date);
                user.put("maxmark", maxmarks);
                user.put("code", usercode);
            } else {
                user.put("marks", String.valueOf(marks));
            }

            db.collection("personal").document(email).collection("attempted").document(timestamp)
                    .set(user, SetOptions.merge());
        }

        // Save marks to quiz collection
        public void sendmarks(String code, String pin) {
            Map<String, Object> user = new HashMap<>();
            user.put("marks", String.valueOf(marks));
            user.put("status", "yes");

            db.collection("data").document("quiz").collection(code + pin).document("participant")
                    .collection("data").document(useremail).set(user);
        }

        public void sendfinish(String code, String pin, String ref) {
            Map<String, Object> user = new HashMap<>();
            user.put("status", ref.equals("") ? "no" : "completed");

            db.collection("personal").document(useremail).collection("attempted").document(timestamp)
                    .set(user, SetOptions.merge())
                    .addOnCompleteListener(task -> {
                        if (!ref.equals("")) {
                            Intent intent1 = new Intent(quiz.this, quizperformcompleted.class);
                            startActivity(intent1);
                            finish();
                        }
                    });
        }

        public void chechpreviousmarked(int i) {
            db.collection("personal").document(useremail).collection("attempted").document(timestamp)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String choosed = task.getResult().getString(String.valueOf(i));
                            check(choosed);
                        }
                    });
        }

        public void check(String chosed) {
            setback();
            if (a.getText().toString().equals(chosed)) optiona.setChecked(true);
            else if (b.getText().toString().equals(chosed)) optionb.setChecked(true);
            else if (c.getText().toString().equals(chosed)) optionc.setChecked(true);
            else if (d.getText().toString().equals(chosed)) optiond.setChecked(true);
        }

        // Start quiz timer
        private void startTimer(String duration) {
            try {
                int durationMinutes = Integer.parseInt(duration);
                long durationMillis = durationMinutes * 60 * 1000;

                countDownTimer = new CountDownTimer(durationMillis, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long minutes = millisUntilFinished / (60 * 1000);
                        long seconds = (millisUntilFinished / 1000) % 60;
                        String timeLeft = String.format("%02d:%02d", minutes, seconds);
                        timer.setText(timeLeft);
                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(quiz.this, quizperformcompleted.class);
                        startActivity(intent);
                        finish();
                    }
                }.start();

            } catch (NumberFormatException e) {
                Log.e(TAG, "Invalid duration format: " + duration, e);
                timer.setText("Error: Invalid duration");
            }
        }

        @Override
        public void onBackPressed() {
            return;
        }

        @Override
        protected void onPause() {
            sendfinish(usercode, userpin, "");
            startActivity(new Intent(quiz.this, quizperformcompleted.class));
            finish();
            super.onPause();
        }

        @Override
        protected void onStop() {
            sendfinish(usercode, userpin, "");
            startActivity(new Intent(quiz.this, quizperformcompleted.class));
            finish();
            super.onStop();
        }
    }