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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.touristGuide_app.Adapters.ListEventsAdapter;
import com.example.touristGuide_app.Adapters.PopularAdapter;
import com.example.touristGuide_app.Domains.ListEventsDomain;
import com.example.touristGuide_app.Domains.PointOfInterestDomain;
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
        fetchEventsFromAPI();
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
    private void initRecyclerEVENTsView(ArrayList<ListEventsDomain> events) {
        // Configurar RecyclerViews e Adapters para os diferentes tipos de eventos
        recyclerViewPopular = findViewById(R.id.viewEvent);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterPopular = new ListEventsAdapter(events, userId, userIdReq, calendarIdReq);
        recyclerViewPopular.setAdapter(adapterPopular);
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

    private void fetchEventsFromAPI() {
        String url = "http://" + serverIp + "/e1/events?limit=25&offset=0";
        String authToken = "93489d58-e2cf-4e11-b3ac-74381fee38ac";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Convertendo a resposta para JSONArray diretamente
                        JSONArray eventsArray = response;
                        ArrayList<ListEventsDomain> events = new ArrayList<>();
                        for (int i = 0; i < eventsArray.length(); i++) {
                            JSONObject eventObject = eventsArray.getJSONObject(i);
                            String id = eventObject.getString("_id");
                            String name = eventObject.getString("name");
                            String organizer = eventObject.getString("organizer");
                            String category = eventObject.getString("category");
                            String contact = eventObject.getString("contact");
                            Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(eventObject.getString("startdate")); // Ajustando o formato da data
                            Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(eventObject.getString("enddate")); // Ajustando o formato da data
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
                                    poiObject.getString("location"),
                                    poiObject.getDouble("latitude"),
                                    poiObject.getDouble("longitude"),
                                    poiObject.optString("street"),
                                    poiObject.optString("postcode"),
                                    poiObject.getString("description"),
                                    poiObject.getString("category"),
                                    poiObject.getString("thumbnail")
                            );
                            Toast.makeText(this, "name: "+name+" organizer: "+organizer, Toast.LENGTH_SHORT).show();
                            events.add(new ListEventsDomain(id, name, organizer, category, contact, startDate, endDate, about, price, currency, maxParticipants, currentParticipants, favorites, poiId, pointOfInterest, "pic1", userId, userIdReq, calendarIdReq));
                        }
                        runOnUiThread(() -> initRecyclerEVENTsView(events));
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(ListOfEvents.this, "Erro ao processar resposta JSON", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            // Verificar se houve erro de rede
            if (error.networkResponse != null) {
                // Processar a resposta como um erro do servidor
                int statusCode = error.networkResponse.statusCode;
                String errorMessage = new String(error.networkResponse.data);
                try {
                    JSONObject errorJson = new JSONObject(errorMessage);
                    if (errorJson.has("error")) {
                        JSONObject errorObject = errorJson.getJSONObject("error");
                        String errorCode = errorObject.getString("code");
                        String message = errorObject.getString("message");
                        String details = errorObject.optString("details");

                        // Aqui você pode lidar com diferentes códigos de erro conforme necessário
                        switch (statusCode) {
                            case 401:
                                // Código de erro 401: Unauthorized
                                Toast.makeText(ListOfEvents.this, "Erro 401: Não autorizado - " + message, Toast.LENGTH_SHORT).show();
                                break;
                            case 404:
                                // Código de erro 404: Not Found
                                Toast.makeText(ListOfEvents.this, "Erro 404: Recurso não encontrado - " + message, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                // Lidar com outros códigos de erro conforme necessário
                                Toast.makeText(ListOfEvents.this, "Erro " + statusCode + ": " + message, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } else {
                        // Erro JSON inesperado
                        Toast.makeText(ListOfEvents.this, "Erro JSON inesperado: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ListOfEvents.this, "Erro ao processar resposta de erro JSON", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Erro de rede
                Toast.makeText(ListOfEvents.this, "Erro de rede ao carregar os eventos. Verifique sua conexão com a Internet e tente novamente.", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }
}