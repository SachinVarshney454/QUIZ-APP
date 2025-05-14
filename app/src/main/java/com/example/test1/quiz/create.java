package com.example.test1.quiz;

import static android.graphics.Color.convert;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.test1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class create extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth auth;
    EditText pin, weightage, author, subject;
    String a;
    String selectedTime, selectedDate;String durationtime;
    MaterialDatePicker<Long> datePicker;
    TextView connection;
    FrameLayout loading;



    private Spinner numberOfQuestionsSpinner;
    Spinner duration;
    private Button selectDateButton, selectTimeButton;
    private TextView selectedDateText, selectedTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create);

        db = FirebaseFirestore.getInstance();
        loading=findViewById(R.id.loading);
//        Toast.makeText(this, "sdjaisa1", Toast.LENGTH_SHORT).show();
        auth = FirebaseAuth.getInstance();

        pin = findViewById(R.id.pin);
//        weightage = findViewById(R.id.weightage);
        connection=findViewById(R.id.connection);
        connection.setVisibility(View.GONE);
        author = findViewById(R.id.author);
        subject = findViewById(R.id.subject);

        numberOfQuestionsSpinner = findViewById(R.id.number_of_questions_spinner);
        duration=findViewById(R.id.spinduration);
        selectDateButton = findViewById(R.id.select_date_button);
        selectTimeButton = findViewById(R.id.select_time_button);
        selectedDateText = findViewById(R.id.selected_date_text);
        selectedTimeText = findViewById(R.id.selected_time_text);
        selectTimeButton.setEnabled(false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.number_of_questions_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numberOfQuestionsSpinner.setAdapter(adapter);

//duration slector
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.number_of_questions_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duration.setAdapter(adapter1);


        // Set up the date picker
        selectDateButton.setOnClickListener(v -> {

            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
            constraintsBuilder.setValidator(DateValidatorPointForward.now());
            datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select a date")
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build();

            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                selectedDate = datePicker.getHeaderText();
                if (selectedDate == null || selectedDate.equals("")) {
                    selectTimeButton.setEnabled(false);
                } else {
                    selectTimeButton.setEnabled(true); // Enable time button if date is valid
                }
                selectedDateText.setText("Selected Date: " + selectedDate);
            });
        });

        selectTimeButton.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);

            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTitleText("Select Time")
                    .setHour(currentHour) // Default to current hour
                    .setMinute(currentMinute) // Default to current minute
                    .build();

            timePicker.show(getSupportFragmentManager(), "TIME_PICKER");

            timePicker.addOnPositiveButtonClickListener(v1 -> {
                int selectedHour = timePicker.getHour();
                int selectedMinute = timePicker.getMinute();

                // Validate that the selected time is in the future
                if (selectedHour > currentHour || (selectedHour == currentHour && selectedMinute > currentMinute)) {
                    selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute); // Store selected time
                    selectedTimeText.setText("Selected Time: " + selectedTime);
                } else {
                    // Notify the user about invalid selection
                    Toast.makeText(v.getContext(), "Please select a time in the future.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        Button button = findViewById(R.id.button);

        button.setOnClickListener(v -> {
            button.setEnabled(false);
            boolean cond = false;
            if(subject.getText().toString().isEmpty()||subject.getText().equals("")){
                Toast.makeText(this, "Please Enter Subject Name", Toast.LENGTH_SHORT).show();
                subject.setBackgroundColor(Color.parseColor("#FFCCCC"));
                cond = true;
            }

            if(author.getText().toString().isEmpty()||author.getText().equals("")){
                Toast.makeText(this, "Please Enter Author's Name", Toast.LENGTH_SHORT).show();
                author.setBackgroundColor(Color.parseColor("#FFCCCC"));
                cond=true;

            }
//

            if(pin.getText().toString().isEmpty()||pin.getText().equals("")){
                Toast.makeText(this, "Please Enter Pincode", Toast.LENGTH_SHORT).show();
                pin.setBackgroundColor(Color.parseColor("#FFCCCC"));
                cond = true;
            }
            if(cond){
                button.setEnabled(true);
                return;
            }
            loading.setVisibility(View.VISIBLE);

            String code = generateRandomCode().toUpperCase();
            int number;

            String pincode = pin.getText().toString();
            if(pincode.length()!=4){
                Toast.makeText(this, "Pincode length should be 4", Toast.LENGTH_SHORT).show();
                button.setEnabled(true);
                return;
            }
            if (numberOfQuestionsSpinner.getSelectedItem() != null) {
                number = Integer.parseInt(numberOfQuestionsSpinner.getSelectedItem().toString());
            } else {
                Toast.makeText(create.this, "Please select a number of questions", Toast.LENGTH_SHORT).show();
                return;
            }
            if(duration.getSelectedItem()!=null){
                durationtime= duration.getSelectedItem().toString();
            }
            if (isnetworkavailable()) {

                Map<String, Object> user = new HashMap<>();
                user.put("number", String.valueOf(number));
                user.put("author", author.getText().toString());
                user.put("subject", subject.getText().toString());
                user.put("time",selectedTime);
                user.put("duration",durationtime);
                    user.put("date",selectedDate);
                user.put("timestamp",convert(selectedDate,selectedTime));
                String timed = selectedDate+" "+selectedTime;
                if(timed.equals("null null")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String sch = sdf.format(new Date());
                    timed = sch;
                }
                user.put("schedule",timed);

                db.collection("data").document("quiz").collection(code + pincode)
                        .document("data")
                        .set(user)
                        .addOnSuccessListener(aVoid -> {
                            Intent intent = new Intent(create.this, quizcreation.class);
                            intent.putExtra("number", number);
                            intent.putExtra("code", code);
                            intent.putExtra("pin", pincode);
                            intent.putExtra("time",selectedTime);
                            intent.putExtra("date",selectedDate);
                            intent.putExtra("author", author.getText().toString());
                            intent.putExtra("subject", subject.getText().toString());
                            finish();
                            startActivity(intent);

                        });
            }
            else{
                connection.setVisibility(View.VISIBLE);
                button.setEnabled(true);
                loading.setVisibility(View.GONE);
                return;
            }
            button.setEnabled(true);
        });
    }

    private String generateRandomCode() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int randomIndex = (int) (Math.random() * alphabet.length());
            code.append(alphabet.charAt(randomIndex));
        }
        return code.toString();
    }

    public String convert(String selectedDate, String selectedTime) {
        try {
            // Combine the date and time into a single string
            String dateTime = selectedDate + " " + selectedTime;

            // Define the format of the input date and time
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

            // Parse the combined string into a Date object
            Date date = inputFormat.parse(dateTime);

            // Define the desired output format
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            // Convert the Date object into the desired format string
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null or handle the exception as needed
        }
    }
    public boolean isnetworkavailable() {
        ConnectivityManager cmanager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cmanager != null) {
            NetworkInfo networkInfo = cmanager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
}
