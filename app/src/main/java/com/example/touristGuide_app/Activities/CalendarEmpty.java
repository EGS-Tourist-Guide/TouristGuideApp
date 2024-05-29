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
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.touristGuide_app.Adapters.CalendarAdapter;
import com.example.touristGuide_app.R;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Map;


public class CalendarEmpty extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private DatabaseReference databaseReference;
    private String startDateString, endDateString;
    private int userIdReq;
    private int calendarIdReq;
    private LocalDate newLocalDate;
    public static List<String> savingDatesByID = new ArrayList<>();
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        fetchEventData();
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

    public void weeklyAction(View view) {
        startActivity(new Intent(this, WeekViewActivity.class));
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            Log.d("CalendarEmpty", "Date clicked: " + date.toString());

            // Start EventDetailActivity and pass the necessary data
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra("calendarIdReq", calendarIdReq);
            intent.putExtra("userIdReq", userIdReq);
            intent.putExtra("currentDate", date.toString()); // Pass the clicked date as startDate

            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        userIdReq = intent.getIntExtra("userIdReq", 0);
        calendarIdReq = intent.getIntExtra("calendarIdReq", 0);
        Toast.makeText(this, "Recebeu userIdReq: " + userIdReq + "\ncalendarIdReq: " + calendarIdReq + " no CalendarEmpty", Toast.LENGTH_SHORT).show();
        if (intent.getBooleanExtra("fromDetailActivity", false)) {
            if (intent != null) {
                startDateString = intent.getStringExtra("startDate");
                endDateString = intent.getStringExtra("endDate");

                Log.d("CalendarEmpty", "Received startDate: " + startDateString);
                Log.d("CalendarEmpty", "Received endDate: " + endDateString);
                Log.d("CalendarEmpty", "Received userIdReq: " + userIdReq);
                Log.d("CalendarEmpty", "Received calendarIdReq: " + calendarIdReq);
            } else {
                Log.e("CalendarEmpty", "Intent is null");
            }

            if (startDateString != null && endDateString != null) {
                DatabaseReference userRef = databaseReference.child(String.valueOf(userIdReq)).child("dates");
                String dateKey = startDateString + "_" + endDateString;
                userRef.child(dateKey).child("start").setValue(startDateString);
                userRef.child(dateKey).child("end").setValue(endDateString);
            }
        }

        DatabaseReference userRef = databaseReference.child(String.valueOf(userIdReq)).child("dates");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                savingDatesByID.clear();
                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    String date = dateSnapshot.getKey();
                    savingDatesByID.add(date);
                }
                setMonthView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CalendarEmpty.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        fetchEventData();
    }

    private void fetchEventData() {
        String url = "http://grupo4-egs-deti.ua.pt/e1/events?limit=25&offset=0&calendarid=" + calendarIdReq;
        String apiKey = "93489d58-e2cf-4e11-b3ac-74381fee38ac";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("CalendarEmpty", "Response received: " + response.toString());
                        parseAndSaveEventData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CalendarEmpty.this, "Failed to fetch data from the endpoint", Toast.LENGTH_SHORT).show();
                        Log.e("CalendarEmpty", "Error fetching data: ", error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("accept", "application/json");
                headers.put("SERVICE-API-KEY", apiKey);
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    private void parseAndSaveEventData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);

                String startDate = jsonData.getString("startdate");
                String endDate = jsonData.getString("enddate");
                String description = jsonData.getString("about");
                JSONObject poiObject = jsonData.getJSONObject("pointofinterest");
                String address = poiObject.getString("street") + ", " + poiObject.getString("location");

                DatabaseReference userRef = databaseReference.child(String.valueOf(userIdReq)).child("dates");
                String dateKey = startDate + "_" + endDate;
                userRef.child(dateKey).child("start").setValue(startDate);
                userRef.child(dateKey).child("end").setValue(endDate);
                userRef.child(dateKey).child("description").setValue(description);
                userRef.child(dateKey).child("address").setValue(address);

                Log.d("CalendarEmpty", "Event data saved: " + startDate + ", " + endDate + ", " + description + ", " + address);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to parse JSON data", Toast.LENGTH_SHORT).show();
        }
    }
}
