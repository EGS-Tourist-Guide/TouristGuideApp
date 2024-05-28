package com.example.touristGuide_app.Activities;

import static com.example.touristGuide_app.Activities.CalendarUtils.daysInMonthArray;
import static com.example.touristGuide_app.Activities.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.touristGuide_app.Adapters.CalendarAdapter;
import com.example.touristGuide_app.R;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CalendarEmpty extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private DatabaseReference databaseReference;
    private String startDateString, endDateString;
    private int userIdReq;
    private int calendarIdReq;
    private LocalDate newLocalDate;
    public static List<String> savingDatesByID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");


    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            // Check if the clicked date is in Firebase
            DatabaseReference userRef = databaseReference.child(String.valueOf(userIdReq)).child("dates");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(date.toString())) {
                        // If the clicked date is in Firebase, open EventDetailActivity
                        Intent intent = new Intent(CalendarEmpty.this, EventDetailActivity.class);
                        String eventDetailsJsonString = getIntent().getStringExtra("eventDetails");
                        intent.putExtra("eventDetails", eventDetailsJsonString);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle potential errors
                    Toast.makeText(CalendarEmpty.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void weeklyAction(View view) {
        startActivity(new Intent(this, WeekViewActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        userIdReq = getIntent().getIntExtra("userIdReq", 0);
        calendarIdReq = getIntent().getIntExtra("calendarIdReq", 0);

        Toast.makeText(this, "Recebeu userIdReq: " + userIdReq + "\ncalendarIdReq: " + calendarIdReq + " no CalendarEmpty", Toast.LENGTH_SHORT).show();

        if (getIntent().getBooleanExtra("fromDetailActivity", false)) {
            Intent intent = getIntent();
            if (intent != null) {
                startDateString = intent.getStringExtra("startDate");
                endDateString = intent.getStringExtra("endDate");
                userIdReq = intent.getIntExtra("userIdReq", 0);
                calendarIdReq = intent.getIntExtra("calendarIdReq", 0);

                // Add logs to check the received values
                Log.d("CalendarEmpty", "Received startDate: " + startDateString);
                Log.d("CalendarEmpty", "Received endDate: " + endDateString);
                Log.d("CalendarEmpty", "Received userIdReq: " + userIdReq);
                Log.d("CalendarEmpty", "Received calendarIdReq: " + calendarIdReq);

            } else {
                Log.e("CalendarEmpty", "Intent is null");
            }

            if (startDateString != null && endDateString != null) {
                // Save start and end dates as a single entry in Firebase
                DatabaseReference userRef = databaseReference.child(String.valueOf(userIdReq)).child("dates");
                String dateKey = startDateString + "_" + endDateString; // Use a unique key
                userRef.child(dateKey).child("start").setValue(startDateString);
                userRef.child(dateKey).child("end").setValue(endDateString);
            }
        }

        // Load saved dates from Firebase
        DatabaseReference userRef = databaseReference.child(String.valueOf(userIdReq)).child("dates");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                savingDatesByID.clear(); // Clear the list before adding new dates
                System.out.println("dataSnapshot.getChildren() -> " + dataSnapshot.getChildren());
                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    String date = dateSnapshot.getKey();
                    System.out.println("Antes de dar add ao savingDatesById -> " + date);
                    savingDatesByID.add(date);
                }
                // After loading the data, update the month view
                setMonthView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
                Toast.makeText(CalendarEmpty.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

