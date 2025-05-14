package com.example.test1.participants;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.example.test1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class participants extends AppCompatActivity {
    adapterparticipants adapter;
    RecyclerView rview;
    String code;
    String quanity;
    ArrayList<modelparticipants>data;
    FirebaseFirestore db;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_participants);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        data=new ArrayList<>();
        button=findViewById(R.id.button);
        db=FirebaseFirestore.getInstance();
        rview= findViewById(R.id.rview);
        Intent intent = getIntent();
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Context Context;
//                if (ContextCompat.checkSelfPermission(Context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                        || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//                    // If permission is not granted, request permissions
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            1); // 1 is the request code
//                } else {
//                    // Permission already granted, proceed with storage operations
//                    createExcelFile();
//                }
//
//            }
//        });
        rview.setLayoutManager(new LinearLayoutManager(this));
         code = intent.getStringExtra("code");
        getmaxmarks(code);

        adapter = new adapterparticipants(this,data);
        rview.setAdapter(adapter);


    }
    public void getmarks(String code,String maxmarks){
        db.collection("data").document("quiz").collection(code).document("participant").collection("data")
                .orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String marks = document.getString("marks");


                               data.add(new modelparticipants(document.getString("name"),document.getString("email"),marks+"/"+maxmarks));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
    public void getmaxmarks(String code){
        db.collection("data").document("quiz").collection(code).document("data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String marksString = task.getResult().getString("number");
                        if (marksString != null) {
                            getmarks(code,marksString);

                        }


                    }
                });




    }
//    private void createExcelFile() {
//

//
//        getdata(code);
//
//
//        XSSFWorkbook workbook = new XSSFWorkbook();
//
//        // Create a sheet in the workbook
//        XSSFSheet sheet = workbook.createSheet("Sample Data");
//
//        // Add rows and cells to the sheet
//        int rowNum = 0;
//        for (modelparticipants item : data) {
//            Row row = sheet.createRow(rowNum++);
//            row.createCell(0).setCellValue(item.getName()); // Assuming getName() returns a String
//            row.createCell(1).setCellValue(item.getEmail());  // Assuming getAge() returns an int or double
//            row.createCell(2).setCellValue(item.getMarks());
//        }
//
//        // Save the workbook to a file
//        String fileName = "SampleExcelFile.xlsx";
//        File file = new File(Environment.getExternalStorageDirectory(), fileName);
//
//        try (FileOutputStream fileOut = new FileOutputStream(file)) {
//            workbook.write(fileOut); // Write workbook data to file
//            Toast.makeText(this, "Excel file created: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Failed to create Excel file", Toast.LENGTH_SHORT).show();
//        }
//
//        // Close the workbook to release resources
//        try {
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}