package com.example.touristGuide_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.touristGuide_app.Adapters.PopularAdapter;
import com.example.touristGuide_app.Domains.PointOfInterestDomain;
import com.example.touristGuide_app.Domains.PopularDomain;
import com.example.touristGuide_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListOfEvents extends AppCompatActivity implements OnLocationSelectedListener {
    private RecyclerView.Adapter adapterPopular, adapterCategory, adapterBestStared, adapterOldest;
    private RecyclerView recyclerViewPopular, recyclerViewCategory, recyclerViewBestStared, recyclerViewOldest;
    String serverIp;
    private String userId = "0";
    private int userIdReq = 0;
    private int calendarIdReq = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serverIp = getString(R.string.ip);
        setContentView(R.layout.list_of_events);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        int userIdReq = intent.getIntExtra("userIdReq", 0);
        int calendarIdReq = intent.getIntExtra("calendarIdReq", 0);
        String poiId = intent.getStringExtra("poiId");
        String poiName = intent.getStringExtra("poiName");
        String poiLocationName = intent.getStringExtra("poiLocationName");
        double poiLatitude = intent.getDoubleExtra("poiLatitude", 0);
        double poiLongitude = intent.getDoubleExtra("poiLongitude", 0);
        String poiStreet = intent.getStringExtra("poiStreet");
        String poiPostcode = intent.getStringExtra("poiPostcode");
        String poiDescription = intent.getStringExtra("poiDescription");
        String poiCategory = intent.getStringExtra("poiCategory");
        String poiThumbnail = intent.getStringExtra("poiThumbnail");

        System.out.println("userId: " + userId + " userIdReq: " + userIdReq + " calendarIdReq: " + calendarIdReq +
                " poiId: " + poiId + " poiName: " + poiName + " poiLocationName: " + poiLocationName +
                " poiLatitude: " + poiLatitude + " poiLongitude: " + poiLongitude + " poiStreet: " + poiStreet +
                " poiPostcode: " + poiPostcode + " poiDescription: " + poiDescription + " poiCategory: " + poiCategory +
                " poiThumbnail: " + poiThumbnail);

        initRecyclerView();
    }
    private void initRecyclerView(){
        //////////////FILTROS
        ConstraintLayout filterLayout = findViewById(R.id.btnFiltros);
        filterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterButtonClick(v);
            }
        });
        //////////////////////////// EVENTs
        fetchEventsFromGraphQL();
        ////////////////////////////Person Menu Icon
        LinearLayout myCalendarLayout = findViewById(R.id.myCalendar);
        myCalendarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a página de calendários
                Intent intent = new Intent(ListOfEvents.this, CalendarEmpty.class);
                intent.putExtra("fromDetailActivity", false);
                intent.putExtra("userId", userId);
                intent.putExtra("userIdReq", userIdReq);
                intent.putExtra("calendarIdReq", calendarIdReq);

                startActivity(intent);
            }
        });
    }
    private void initRecyclerEVENTsView(ArrayList<PopularDomain> events) {
        // Configurar RecyclerViews e Adapters para os diferentes tipos de eventos
        recyclerViewPopular = findViewById(R.id.viewPop);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterPopular = new PopularAdapter(events);
        recyclerViewPopular.setAdapter(adapterPopular);

        // Aqui você pode configurar outros RecyclerViews, se necessário
        // Exemplo para recyclerViewBestStared
        recyclerViewBestStared = findViewById(R.id.viewBestStared);
        recyclerViewBestStared.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterBestStared = new PopularAdapter(events); // Adapte conforme necessário
        recyclerViewBestStared.setAdapter(adapterBestStared);
        // Exemplo para recyclerViewOldest
        recyclerViewOldest = findViewById(R.id.viewOldest);
        recyclerViewOldest.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterOldest = new PopularAdapter(events); // Adapte conforme necessário
        recyclerViewOldest.setAdapter(adapterOldest);
    }
    public void onFilterButtonClick(View view) {
        FilterPopup filterPopup = new FilterPopup();
        // Defina este MainActivity como o listener para receber as coordenadas selecionadas
        // filterPopup.setOnLocationSelectedListener(this);
        filterPopup.show(getSupportFragmentManager(), "filter_popup");
    }
    @Override
    public void onLocationSelected(double latitude, double longitude) {
        System.out.println("Main onLocationSelected activity Latitude: " + latitude + ", Longitude: " + longitude);
        Toast.makeText(this, "Retornou latitude: "+latitude+" e longitude: "+longitude, Toast.LENGTH_SHORT).show();
        // Obtém uma referência para o FilterPopup atualmente exibido
        FilterPopup filterPopup = (FilterPopup) getSupportFragmentManager().findFragmentByTag("filter_popup");
        // Verifica se o popup está sendo exibido antes de atualizar as coordenadas
        if (filterPopup != null) {
            filterPopup.updateLocation(latitude, longitude);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            System.out.println("Main activity onActivityResult Latitude: " + latitude + ", Longitude: " + longitude);

            // Obtém uma referência para o FilterPopup atualmente exibido
            FilterPopup filterPopup = (FilterPopup) getSupportFragmentManager().findFragmentByTag("filter_popup");
            // Verifica se o popup está sendo exibido antes de atualizar as coordenadas
            if (filterPopup != null) {
                filterPopup.updateLocation(latitude, longitude);
            }
        }
    }

    private void fetchEventsFromGraphQL() {
        JSONObject jsonBody = new JSONObject();
        try {
            String query = "query findEvents { events { _id name organizer category contact startdate enddate about price currency maxparticipants currentparticipants favorites pointofinterestid pointofinterest { _id name locationName longitude latitude street postcode description category thumbnail } } }";
            jsonBody.put("query", query);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ListOfEvents.this, "Erro ao enviar o request JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://" + serverIp + "/e1/events?limit=25&offset=0", jsonBody,
                response -> {
                    try {
                        if (response.has("errors")) {
                            JSONArray errors = response.getJSONArray("errors");
                            if (errors.length() > 0) {
                                String errorMessage = errors.getJSONObject(0).getString("message");
                                runOnUiThread(() -> Toast.makeText(ListOfEvents.this, errorMessage, Toast.LENGTH_SHORT).show());
                                return;
                            }
                        }

                        JSONArray eventsArray = response.getJSONObject("data").getJSONArray("events");
                        ArrayList<PopularDomain> events = new ArrayList<>();
                        for (int i = 0; i < eventsArray.length(); i++) {
                            JSONObject eventObject = eventsArray.getJSONObject(i);
                            String id = eventObject.getString("_id");
                            String name = eventObject.getString("name");
                            String organizer = eventObject.getString("organizer");
                            String category = eventObject.getString("category");
                            String contact = eventObject.getString("contact");
                            Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(eventObject.getString("startdate"));
                            Date endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(eventObject.getString("enddate"));
                            String about = eventObject.getString("about");
                            double price = eventObject.getDouble("price");
                            String currency = eventObject.getString("currency");
                            int maxParticipants = eventObject.getInt("maxparticipants");
                            int currentParticipants = eventObject.getInt("currentparticipants");
                            int favorites = eventObject.getInt("favorites");
                            String poiId = eventObject.getString("pointofinterestid");

                            JSONObject poiObject = eventObject.getJSONObject("pointofinterest");
                            PointOfInterestDomain pointOfInterest = new PointOfInterestDomain(
                                    poiObject.getString("_id"),
                                    poiObject.getString("name"),
                                    poiObject.getString("locationName"),
                                    poiObject.getDouble("latitude"),
                                    poiObject.getDouble("longitude"),
                                    poiObject.optString("street"),
                                    poiObject.optString("postcode"),
                                    poiObject.getString("description"),
                                    poiObject.getString("category"),
                                    poiObject.getString("thumbnail")
                            );

                            events.add(new PopularDomain(id, name, organizer, category, contact, startDate, endDate, about, price, currency, maxParticipants, currentParticipants, favorites, poiId, pointOfInterest, "pic1"));
                        }

                        runOnUiThread(() -> initRecyclerEVENTsView(events));
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(ListOfEvents.this, "Erro ao processar resposta JSON", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            Toast.makeText(ListOfEvents.this, "Erro na solicitação: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


}