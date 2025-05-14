package com.example.test1.quiz;

import static android.content.ContentValues.TAG;
import static android.provider.SyncStateContract.Helpers.update;
import static android.view.View.GONE;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class quizcreation extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    RadioButton ra,rb,rc,rd;
    Button next,prev,finish;
    int num;    FirebaseFirestore db;
    int i=1;
    FrameLayout loading;
    EditText a,b,c,d;TextView question;
    String choosen;
    int rdid;
    LinearLayout l1,l2,l3,l4;
    boolean cond;
    TextView count;
    String ft="";
    Map<Integer,Integer> map;
    Map<Integer,Integer> lastques;


    boolean lastcond= false,lastcondcond=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quizcreation);

        // Set up window insets listener for padding adjustments
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //creating quiz




        //Intialize UI
        choosen = "";
        l1=findViewById(R.id.l1);
        l2=findViewById(R.id.l2);
        l3=findViewById(R.id.l3);
        l4=findViewById(R.id.l4);
//        setcolor();
        map = new HashMap<>();
         loading = findViewById(R.id.loading);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        question = findViewById(R.id.question);
        next = findViewById(R.id.next);
        prev=findViewById(R.id.prev);
        finish=findViewById(R.id.finish);
        count=findViewById(R.id.count);



        a=findViewById(R.id.a);
        b=findViewById(R.id.b);
        c=findViewById(R.id.c);
        d=findViewById(R.id.d);



        ra=findViewById(R.id.ra);
        rb=findViewById(R.id.rb);
        rc=findViewById(R.id.rc);
        rd=findViewById(R.id.rd);







        //initialize radiobutton
        ra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setback();

                if(a.getText().toString().equals("")){
                    Toast.makeText(quizcreation.this, "Enter Answer", Toast.LENGTH_SHORT).show();
                    return;
                }
                ra.setChecked(true);
                rdid=ra.getId();
                if(a.getText().toString().equals("")){
                    choosen="a";
                }
                else
                    choosen= a.getText().toString();

            }
        });
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setback();

                if(b.getText().toString().equals("")){
                    Toast.makeText(quizcreation.this, "Enter Answer", Toast.LENGTH_SHORT).show();
                    return;
                }
                rb.setChecked(true);
                if(b.getText().toString().equals("")){
                    choosen="b";
                }
                else
                    choosen= b.getText().toString();
            }
        });
        rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setback();

                if(c.getText().toString().equals("")){
                    Toast.makeText(quizcreation.this, "Enter Answer", Toast.LENGTH_SHORT).show();
                    return;
                }
                rc.setChecked(true);
                if(c.getText().toString().equals("")){
                    choosen="c";
                }
                else
                    choosen= c.getText().toString();
            }
        });
        rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setback();

                if(d.getText().toString().equals("")){
                    Toast.makeText(quizcreation.this, "Enter Answer", Toast.LENGTH_SHORT).show();
                    return;
                }
                rd.setChecked(true);
                if(d.getText().toString().equals("")){
                    choosen="d";
                }
                else
                    choosen= d.getText().toString();
            }
        });




        //get data from activity
        Intent intent = getIntent();
        String code = intent.getStringExtra("code");
        String pincode = intent.getStringExtra("pin");
        num = intent.getIntExtra("number", 0);
        cond = false;
        if(num==1){
            prev.setVisibility(GONE);
            next.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

            next.setText("Submit");
        }


//button function
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.setEnabled(false);
                String selectedAnswer = getSelectedOption();
//                prevvisi();
                if(map.containsKey(i+1)){

                    nextdata(code,pincode,"next",selectedAnswer);
                }
                else{
//                    i--;
                    nextdata(code,pincode,"next",selectedAnswer);

                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prev.setEnabled(false);
                previousdata(code,pincode);
//                prevvisi();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!map.containsKey(num)){
                    Toast.makeText(quizcreation.this, "Please FillUp all the Questions", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(quizcreation.this, "Quiz creation complete!", Toast.LENGTH_SHORT).show();
                privatedata(code,pincode);
                Intent intent = new Intent(quizcreation.this, quizcompleted.class);
                intent.putExtra("code",code);
                intent.putExtra("pin",pincode);
                String time =  intent.getStringExtra("time");
                String date = intent.getStringExtra("date");
                intent.putExtra("time",time);
                intent.putExtra("date",date);
                startActivity(intent);


            }
        });
        prev.setVisibility(GONE);
        count.setText(String.valueOf(i)+" of "+String.valueOf(num));


    }


    String select;
    public void nextdata(String code, String pincode, String go, String choosen) {
        if (choosen.equals("")) {
            Toast.makeText(this, "Select the correct answer", Toast.LENGTH_SHORT).show();
            return;
        }
        if(i==num+1){
            map.put(num,num);
            return;
        }
       if(i>1){
            prev.setVisibility(View.VISIBLE);
        }

        select = choosen;


        if (i == num - 1) {
            next.setText("Submit");
        }

        // Prevent moving past the last question
        if (i > num) {
            next.setEnabled(false);
            i--;
            return;
        }
        loading.setVisibility(View.VISIBLE);




        map.put(i, i);
        count.setText(String.valueOf(i) + " of " + String.valueOf(num));

        // Prepare question data for Firestore
        Map<String, Object> questionData = new HashMap<>();
        questionData.put("question", question.getText().toString());
        questionData.put("a", a.getText().toString());
        questionData.put("b", b.getText().toString());
        questionData.put("c", c.getText().toString());
        questionData.put("d", d.getText().toString());
        questionData.put("selected", select);

        // Save question data to Firestore
        db.collection("data").document("quiz").collection(code + pincode).document(String.valueOf(i))
                .set(questionData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override

                    public void onSuccess(Void aVoid) {
                        next.setEnabled(true);
                        prev.setEnabled(true);
                        if(i<num) {
                            a.setText("");
                            b.setText("");
                            c.setText("");
                            d.setText("");
                            question.setText("");


                            prev.setVisibility(View.VISIBLE);
                        }
                        i++;
                        String ss = String.valueOf(i);
//                        Toast.makeText(quizcreation.this,ss, Toast.LENGTH_SHORT).show();
                        if(i>=num+1){
                            if(lastcondcond){

                            }
                            else lastcond=true;
                        }
                        if(i<num){
                            setback();

                        }


                        if(map.containsKey(i)) {
                            loadeddata(code, pincode);
                        }
                        if(i>num){
                            next.setVisibility(GONE);

                        }
                        if(i>num-1){
                        }

                        else {next.setVisibility(View.VISIBLE);}

                            checked(code,pincode);

                        if (i <= num) {
                            count.setText(String.valueOf(i) + " of " + String.valueOf(num));
                        }



                        if (i > num) {
                            Toast.makeText(quizcreation.this, "Quiz creation complete!", Toast.LENGTH_SHORT).show();
                            next.setEnabled(false);
                            i--;
                        }
                        loading.setVisibility(GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(quizcreation.this, "Error saving question. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void previousdata(String code,String pincode){
        i--;
        if(i==1){
            prev.setVisibility(GONE);
        }
        else prev.setVisibility(View.VISIBLE);
        if(i<num){
            next.setVisibility(View.VISIBLE);
        }
        else next.setVisibility(GONE);
        next.setEnabled(true);
        map.put(i,i);
        count.setText(i+" of "+num);
        next.setText("Next");
        next.setBackgroundColor(Color.parseColor("#9D00FF"));
        loading.setVisibility(View.VISIBLE);

        db.collection("data").document("quiz")
                .collection(code + pincode)
                .document(String.valueOf(i))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                selected(document.getString("selected"));
                                a.setText(document.getString("a"));
                                b.setText(document.getString("b"));
                                c.setText(document.getString("c"));
                                d.setText(document.getString("d"));
                                question.setText(document.getString("question"));
                                String select = document.getString("selected");
                                selected(select);
                                loading.setVisibility(GONE);
                                prev.setEnabled(true);
                            } else {
                                Log.w(TAG, "No such document exists.");
                                Toast.makeText(quizcreation.this, code+pincode+"   "+i, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
    public void privatedata(String code,String pin){
        Map<String, Object> questionData = new HashMap<>();
        Intent intent=getIntent();
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String author= intent.getStringExtra("author");
        String subject= intent.getStringExtra("subject");
        questionData.put("date",date);
        questionData.put("time",time);
        questionData.put("author",author);
        questionData.put("subject",subject);
        questionData.put("code",code+pin);
        questionData.put("finish","true");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = sdf.format(new Date());
        ft=formattedTimestamp;
//        questionData.put(String.valueOf(i),answer);
        db.collection("personal")
                .document(user.getEmail())
                .collection("created")
                .document(formattedTimestamp)
                .set(questionData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(quizcreation.this, "Error saving question. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });

    }







    public void loadeddata(String code, String pin) {

        next.setEnabled(false);
        prev.setEnabled(false);

        count.setText(i + " of " + num);
        if (i > 1) {
            prev.setVisibility(View.VISIBLE);
        }

        db.collection("data").document("quiz").collection(code + pin).document(String.valueOf(i))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                a.setText(document.getString("a"));
                                b.setText(document.getString("b"));
                                c.setText(document.getString("c"));
                                d.setText(document.getString("d"));
                                question.setText(document.getString("question"));
                                String select = document.getString("selected");
                                selected(select);
                                loading.setVisibility(GONE);
                            }
                        }
                        // Re-enable buttons after data fetch is complete
                        next.setEnabled(true);
                        prev.setEnabled(true);
                    }
                });
    }








    public void selected(String sel) {
        // Clear all radio buttons first
        setback();

        if (sel == null) return; // Avoid null pointer issues

        if (sel.equals("a") || sel.equals(a.getText().toString())) {
            ra.setChecked(true);
        } else if (sel.equals("b") || sel.equals(b.getText().toString())) {
            rb.setChecked(true);
        } else if (sel.equals("c") || sel.equals(c.getText().toString())) {
            rc.setChecked(true);
        } else if (sel.equals("d") || sel.equals(d.getText().toString())) {
            rd.setChecked(true);
        }
    }
    public void checked(String code ,String pin){
        db.collection("data").document("quiz").collection(code+pin).document(String.valueOf(i))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String choosen = task.getResult().getString("selected");
//                        if(choosen.equals(""))
                        rb(choosen);

                    }
                });
    }
    public void rb(String choosen){
        if (a.getText().toString().equals(choosen)) {
            setback();
            ra.setChecked(true);
        } else if (b.getText().toString().equals(choosen)) {
            setback();
            rb.setChecked(true);

        } else if (c.getText().toString().equals(choosen)) {
            setback();
            rc.setChecked(true);
        } else if (d.getText().toString().equals(choosen)) {
            setback();
            rd.setChecked(true);
        }
        else{
            setback();
        }
    }

    public void setback() {
        if(lastcond){
            lastcond=false;
            lastcondcond=true;
            return;
        }
            ra.setChecked(false);
            rb.setChecked(false);
            rc.setChecked(false);
            rd.setChecked(false);
    }
    public String getSelectedOption() {
        if (ra.isChecked()) return a.getText().toString();
        if (rb.isChecked()) return b.getText().toString();
        if (rc.isChecked()) return c.getText().toString();
        if (rd.isChecked()) return d.getText().toString();
        return "";
    }




    public void prevvisi(){



    }

}

