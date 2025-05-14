package com.example.test1.ui;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.test1.R;
import com.example.test1.databinder.holderemail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class changepass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_changepass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FirebaseFirestore db =FirebaseFirestore.getInstance();
        EditText old = findViewById(R.id.old);

        EditText newp = findViewById(R.id.newp);
        EditText renew=findViewById(R.id.renew);
        Button change= findViewById(R.id.change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpass = old.getText().toString();
                String newppass=newp.getText().toString();
                String renewpass= renew.getText().toString();
//                test.setText("sas");
//                if(!newppass.equals(renewpass)){
//                    Toast.makeText(changepass.this, "Enter Same New Password", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                String id = holderemail.getInstance().toString();
                db.collection("users").document(id)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null && document.exists()) {
                                        // Get the value from the document
                                        String a= (document.getString("email"));
//                                        if(a.equals("")){
//                                            test.setText("nu");
//                                        }
//                                        else{
//                                            test.setText("5r");
//                                        }
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.w(TAG, "Error getting document.", task.getException());
                                }
                            }
                        });

            }
        });


    }
}