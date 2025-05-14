package com.example.test1.authentication;

import static android.content.ContentValues.TAG;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.example.test1.databinder.holderemail;
import com.example.test1.databinder.holdername;
import com.example.test1.databinder.singeltonquizzes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {
    EditText name; EditText pass; EditText repass;EditText email;
    Button signup;
    FirebaseAuth mAuth; FirebaseFirestore db = FirebaseFirestore.getInstance();
    LottieAnimationView anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        name=findViewById(R.id.name);
        pass=findViewById(R.id.password);
        repass=findViewById(R.id.repass);
        email=findViewById(R.id.email);
        mAuth=FirebaseAuth.getInstance();
        anim=findViewById(R.id.animsign);
        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, logicscreen.class);
                startActivity(intent);
                finish();
            }
        });

        signup=findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nm =  name.getText().toString();
                String em = email.getText().toString();
                String pass1= pass.getText().toString();
                String repass1=repass.getText().toString();
                if(pass1.compareTo(repass1)!=0){
                    Toast.makeText(signup.this, "Enter Same Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    signup.setEnabled(false);

                    signup(em,pass1,nm);
                }
            }
        });

    }
    public void signup(String email,String password,String name){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Create a new user with a first, middle, and last name
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", name);

                            user.put("email", email);
                            user.put("password",password);
                            holdername.getInstance().setData(name);
                            holderemail.getInstance().setData(email);

                            db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            anim.playAnimation();
                                           anim.addAnimatorListener(new Animator.AnimatorListener() {
                                               @Override
                                               public void onAnimationStart(@NonNull Animator animation) {
                                               }
                                               @Override
                                               public void onAnimationEnd(@NonNull Animator animation) {
                                                   singeltonquizzes.getInstance().clearItems();
                                                   Intent intent=new Intent(signup.this, MainActivity.class);
                                                   startActivity(intent);

                                                   finish();

                                               }

                                               @Override
                                               public void onAnimationCancel(@NonNull Animator animation) {

                                               }

                                               @Override
                                               public void onAnimationRepeat(@NonNull Animator animation) {

                                               }
                                           });




                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(signup.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                                            signup.setEnabled(true);

                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });

                            Log.d(TAG, "createUserWithEmail:success");


                        } else {

                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            signup.setEnabled(true);

                        }
                    }
                });

    }

}
