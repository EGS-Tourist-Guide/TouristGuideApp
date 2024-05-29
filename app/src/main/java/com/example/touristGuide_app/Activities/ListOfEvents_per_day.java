package com.example.touristGuide_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.touristGuide_app.Adapters.ListEventsAdapter_per_day;
import com.example.touristGuide_app.Domains.ListEventsDomain_per_day;
import com.example.touristGuide_app.Domains.PointOfInterestDomain;
import com.example.touristGuide_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ListOfEvents_per_day extends AppCompatActivity  {
    private RecyclerView.Adapter adapterPopular;
    private RecyclerView recyclerViewPopular;
    private String serverIp;
    private int userId;
    private int calendarId;
    private String eventId, currentDay;
    private List<String> eventIds;
    private RequestQueue requestQueue;
    private String apiKey = "93489d58-e2cf-4e11-b3ac-74381fee38ac";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("AHHHHHHHHHHHHIN?");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_events_per_day);

        Intent intent = getIntent();
        requestQueue = Volley.newRequestQueue(this);
        calendarId = intent.getIntExtra("calendarIdReq", 0);
        userId = intent.getIntExtra("userIdReq", 0);
        currentDay =intent.getStringExtra("currentDate");

        serverIp = getString(R.string.ip);

        System.out.println("No list of event per day -> userId: " + userId + " calendarId: " + calendarId + "currentDay: "+currentDay);
        initRecyclerView();
        Log.d("API_RESPONSE", "passou aqui3: " );
    }
    private void initRecyclerView() {
        //////////////FILTROS
        ConstraintLayout filterLayout = findViewById(R.id.btnFiltros);
        ////////////////////////////Person Menu Icon
        LinearLayout myCalendarLayout = findViewById(R.id.myCalendar);
        myCalendarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a página de calendários
                Intent intent = new Intent(ListOfEvents_per_day.this, CalendarEmpty.class);
                intent.putExtra("fromDetailActivity", false);
                intent.putExtra("userIdReq", userId);
                intent.putExtra("calendarIdReq", calendarId);
                startActivity(intent);
            }
        });
        //////////////////////////// EVENTs
        fetchEventsFromAPI();
    }


    private void initRecyclerEVENTsView(ArrayList<ListEventsDomain_per_day> events) {
        recyclerViewPopular = findViewById(R.id.viewEvent);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this));
        adapterPopular = new ListEventsAdapter_per_day(this, events);
        recyclerViewPopular.setAdapter(adapterPopular);
        Log.d("API_RESPONSE", "RecyclerView initialized with events: " + events.size());
    }
    private void fetchEventsFromAPI() {
        String url = "http://" + serverIp + "/e1/events?limit=25&offset=0";
        String apiKey = "93489d58-e2cf-4e11-b3ac-74381fee38ac";
        System.out.println("fetchEventsFromAPIfetchEventsFromAPIfetchEventsFromAPI");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray eventsArray = response;
                        ArrayList<ListEventsDomain_per_day> events = new ArrayList<>();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                        for (int i = 0; i < eventsArray.length(); i++) {
                            JSONObject eventObject = eventsArray.getJSONObject(i);

                            // Extract and parse the data from JSON
                            String id = eventObject.getString("_id");
                            String name = eventObject.getString("name");
                            String organizer = eventObject.getString("organizer");
                            String category = eventObject.getString("category");
                            String contact = eventObject.getString("contact");

                            Date startDate = null;
                            Date endDate = null;
                            try {
                                startDate = dateFormat.parse(eventObject.getString("startdate"));
                                endDate = dateFormat.parse(eventObject.getString("enddate"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                Log.e("DATE_FORMAT_ERROR", "Erro ao analisar a data", e);
                                Toast.makeText(ListOfEvents_per_day.this, "Erro ao analisar a data", Toast.LENGTH_SHORT).show();
                            }

                            String about = eventObject.getString("about");
                            double price = eventObject.getDouble("price");
                            String currency = eventObject.getString("currency");
                            int maxParticipants = eventObject.getInt("maxparticipants");
                            int currentParticipants = eventObject.getInt("currentparticipants");
                            int favorites = eventObject.getInt("favorites");
                            String poiId = eventObject.getString("pointofinterestid");

                            JSONObject poiObject = eventObject.getJSONObject("pointofinterest");
                            PointOfInterestDomain pointOfInterest = new PointOfInterestDomain(
                                    poiObject.optString("_id", ""),
                                    poiObject.getString("name"),
                                    poiObject.getString("location"),
                                    poiObject.getDouble("latitude"),
                                    poiObject.getDouble("longitude"),
                                    poiObject.optString("street", ""),
                                    poiObject.optString("postcode", ""),
                                    poiObject.getString("description"),
                                    poiObject.getString("category"),
                                    poiObject.getString("thumbnail")
                            );

                            events.add(new ListEventsDomain_per_day(id, name, organizer, category, contact, startDate, endDate, about, price, currency, maxParticipants, currentParticipants, favorites, poiId, pointOfInterest, "pic1", userId, calendarId));
                        }

                        runOnUiThread(() -> initRecyclerEVENTsView(events));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("API_ERROR", "Erro ao processar resposta JSON: " + e.getMessage());
                        Toast.makeText(ListOfEvents_per_day.this, "Erro ao processar resposta JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle network errors
                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        String errorMessage = new String(error.networkResponse.data);
                        Log.e("API_ERROR", "Error response: " + errorMessage);

                        try {
                            JSONObject errorJson = new JSONObject(errorMessage);
                            if (errorJson.has("error")) {
                                JSONObject errorObject = errorJson.getJSONObject("error");
                                String errorCode = errorObject.getString("code");
                                String message = errorObject.getString("message");

                                switch (statusCode) {
                                    case 401:
                                        Toast.makeText(ListOfEvents_per_day.this, "Erro 401: Não autorizado - " + message, Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(ListOfEvents_per_day.this, "Erro 404: Recurso não encontrado - " + message, Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(ListOfEvents_per_day.this, "Erro " + statusCode + ": " + message, Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            } else {
                                Toast.makeText(ListOfEvents_per_day.this, "Erro JSON inesperado: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ListOfEvents_per_day.this, "Erro ao processar resposta de erro JSON", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("API_ERROR", "Network error occurred");
                        Toast.makeText(ListOfEvents_per_day.this, "Erro de rede ao carregar os eventos. Verifique sua conexão com a Internet e tente novamente.", Toast.LENGTH_SHORT).show();
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
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }
}