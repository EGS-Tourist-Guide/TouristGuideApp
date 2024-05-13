package com.example.touristGuide_app.Activities;

import static com.example.touristGuide_app.Activities.CalendarUtils.daysInMonthArray;
import static com.example.touristGuide_app.Activities.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.touristGuide_app.Adapters.CalendarAdapter;
import com.example.touristGuide_app.R;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class CalendarEmpty extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private LocalDate newLocalDate;
    public static List<String> savingDatesByID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();

        // Inicialize o Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Obtenha o ID do usuário atualmente autenticado
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        }

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
            DatabaseReference userRef = databaseReference.child(userId).child("dates");
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
        userId = getIntent().getStringExtra("userId");
        Toast.makeText(this, "Recebeu id " + userId + " no CalendarEmpty", Toast.LENGTH_SHORT).show();

        if (getIntent().getBooleanExtra("fromDetailActivity", false)) {
            Date newDate = (Date) getIntent().getSerializableExtra("eventDate");
            if (newDate != null) {
                newLocalDate = newDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String dateString = newLocalDate.toString();
                System.out.println("onResume: eventDate -> "+dateString);

                // Salvar a data na Firebase
                DatabaseReference userRef = databaseReference.child(userId).child("dates");
                userRef.child(dateString).setValue(true);
                System.out.println("userRef -> "+userRef); // userRef -> https://touristguidefirebase-default-rtdb.firebaseio.com/users/6kTDJPmhULWcy2uSculYd1XyVv93/dates
                // link say -> Database lives in a different region. Please change your database URL to https://touristguidefirebase-default-rtdb.europe-west1.firebasedatabase.app
            }
        }


        // Carregar as datas salvas da Firebase
        DatabaseReference userRef = databaseReference.child(userId).child("dates");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                savingDatesByID.clear(); // Limpar a lista antes de adicionar as novas datas
                System.out.println("dataSnapshot.getChildren() -> "+dataSnapshot.getChildren());
                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    String date = dateSnapshot.getKey();
                    System.out.println("Antes de dar add ao savingDatesById -> "+date);
                    savingDatesByID.add(date);
                }
                // Após carregar os dados, atualize a exibição do mês
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
