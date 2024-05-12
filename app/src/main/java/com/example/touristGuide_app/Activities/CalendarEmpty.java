package com.example.touristGuide_app.Activities;

import static com.example.touristGuide_app.Activities.CalendarUtils.daysInMonthArray;
import static com.example.touristGuide_app.Activities.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class CalendarEmpty extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    public static LocalDate newLocalDate;
    public static List<String> savingDatesByID;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();

    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        
        if (date != null) {
            // Check if the clicked date is in savingDatesByID
            if (savingDatesByID.contains(date.toString())) {
                // If the clicked date is in savingDatesByID, open EventDetailActivity
                Intent intent = new Intent(CalendarEmpty.this, EventDetailActivity.class);
                String eventDetailsJsonString = getIntent().getStringExtra("eventDetails");
                intent.putExtra("eventDetails", eventDetailsJsonString);
                startActivity(intent);
            }
        }
    }

    public void weeklyAction(View view) {
        startActivity(new Intent(this, WeekViewActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        // popula o userId consoante o que recebe do Main/Login
        userId = getIntent().getStringExtra("userId");
        Toast.makeText(this, "Recebeu id "+ userId+" no CalendarEmpty", Toast.LENGTH_SHORT).show();
        String prefFileName = "MyDatePref_" + userId;

        // esta condição evita que bugue quando tento abrir à primeira o calendario a partir da main
        if (getIntent().getBooleanExtra("fromDetailActivity", false)) {


            // Limpar a lista antes de adicionar novas datas
            savingDatesByID.clear();

            // Receber a data do evento da Intent

            Date newDate = (Date) getIntent().getSerializableExtra("eventDate");
            // Toast.makeText(this, "Recebeu o" + newDate, Toast.LENGTH_SHORT).show();
            if (newDate != null) {
                // Converter newDate para LocalDate
                newLocalDate = newDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String dateString = newLocalDate.toString();

                // Carregar as datas salvas anteriormente
                loadSavedDates(prefFileName);
                // Verificar se a lista não contém a nova data antes de adicioná-la
                if (!savingDatesByID.contains(dateString)) {
                    savingDatesByID.add(dateString);
                    saveDates(prefFileName); // Salvar a lista atualizada
                }

//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                // Toast.makeText(getApplicationContext(), "Start date is set: " + dateString, Toast.LENGTH_SHORT).show();
                // Toast.makeText(getApplicationContext(), "End date is set: " + dateString, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Event date is set: " + dateString, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "saved: " + savingDatesByID, Toast.LENGTH_SHORT).show();
            }
        } else {
            // Se não vier de DetailActivity, apenas mostre os dias marcados
            // Carregar as datas salvas anteriormente
            loadSavedDates(prefFileName);
        }
    }

    private void loadSavedDates(String prefFileName) {
        SharedPreferences sharedPreferences = getSharedPreferences(prefFileName, MODE_PRIVATE);
        String json = sharedPreferences.getString("savingDates", "");

        if (!json.isEmpty()) {
            Type type = new TypeToken<List<String>>() {}.getType();
            Gson gson = new Gson();
            List<String> dateStringList = gson.fromJson(json, type);

            // Converter as strings de data de volta para LocalDate
            savingDatesByID = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (String dateString : dateStringList) {
                savingDatesByID.add(dateString);
            }
        } else {
            savingDatesByID = new ArrayList<>();
        }
    }

    private void saveDates(String prefFileName) {
        SharedPreferences sharedPreferences = getSharedPreferences(prefFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Converter as LocalDate para strings de data
        List<String> dateStringList = new ArrayList<>();
        for (String date : savingDatesByID) {
            dateStringList.add(date);
        }

        // Salvar a lista de strings no SharedPreferences
        Gson gson = new Gson();
        String json = gson.toJson(dateStringList);
        editor.putString("savingDates", json);
        editor.apply();
    }
}