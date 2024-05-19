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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.touristGuide_app.Adapters.CategoryAdapter;
import com.example.touristGuide_app.Adapters.PointOfInterestAdapter;
import com.example.touristGuide_app.Domains.CategoryDomain;
import com.example.touristGuide_app.Domains.PointOfInterestDomain;
import com.example.touristGuide_app.R;

import java.util.ArrayList;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ListOfPointOfInterest extends AppCompatActivity implements OnLocationSelectedListener {
    private RecyclerView.Adapter adapterCategory;
    private RecyclerView recyclerViewPoi;
    private PointOfInterestAdapter adapterPoi;
    private RecyclerView recyclerViewCategory;
    private String userId = "0";
    private int userIdReq = 0;
    private int calendarIdReq = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_pois);
        userId = getIntent().getStringExtra("userId");
        userIdReq = getIntent().getIntExtra("userIdReq",0);
        calendarIdReq = getIntent().getIntExtra("calendarIdReq",0);

        System.out.println("onCreate?");

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

        fetchPOIsFromGraphQL();
        ////////////////////////////CATEGORIAS
        ArrayList<CategoryDomain> catsList = new ArrayList<>();
        catsList.add(new CategoryDomain("Beaches", "cat1"));
        catsList.add(new CategoryDomain("Museums", "cat2"));
        catsList.add(new CategoryDomain("Forest", "cat3"));
        catsList.add(new CategoryDomain("Festivals", "cat4"));
        catsList.add(new CategoryDomain("Camps", "cat5"));
        
        recyclerViewCategory=findViewById(R.id.viewCat);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterCategory = new CategoryAdapter(catsList);
        recyclerViewCategory.setAdapter(adapterCategory);

        ////////////////////////////Person Menu Icon
        LinearLayout myCalendarLayout = findViewById(R.id.myCalendar);
        myCalendarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a página de calendários
                Intent intent = new Intent(ListOfPointOfInterest.this, CalendarEmpty.class);
                intent.putExtra("fromDetailActivity", false);
                intent.putExtra("userId", userId);
                intent.putExtra("userIdReq", userIdReq);
                intent.putExtra("calendarIdReq", calendarIdReq);

                startActivity(intent);
            }
        });
    }
    public void onFilterButtonClick(View view) {
        FilterPopup filterPopup = new FilterPopup();
        // Defina este MainActivity como o listener para receber as coordenadas selecionadas
        filterPopup.setOnLocationSelectedListener(this);
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


    private void initRecyclerPOIView(ArrayList<PointOfInterestDomain> pois) {
        // Configura o RecyclerView e o adaptador após receber os POIs
        recyclerViewPoi = findViewById(R.id.recyclerViewPoi);
        recyclerViewPoi.setLayoutManager(new LinearLayoutManager(this));
        adapterPoi = new PointOfInterestAdapter(pois);
        recyclerViewPoi.setAdapter(adapterPoi);

        // Aqui você pode adicionar prints para verificar se os POIs foram recebidos corretamente
        Log.d("ListOfPointOfInterest", "POIs recebidos: " + pois.size());
    }

    private void fetchPOIsFromGraphQL() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("query", "query findPOIs { searchPointsOfInterest(searchInput: {}, apiKey: \"Tigas:4712b0a1d771938c04e5cba078b0a889\") { _id name location { coordinates } locationName street postcode description category thumbnail event_ids } }");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://srv2-deti.ua.pt/graphql", jsonBody,
                response -> {
                    try {
                        JSONArray poiArray = response.getJSONObject("data").getJSONArray("searchPointsOfInterest");
                        ArrayList<PointOfInterestDomain> pois = new ArrayList<>();
                        for (int i = 0; i < poiArray.length(); i++) {
                            JSONObject poiObject = poiArray.getJSONObject(i);
                            String id = poiObject.getString("_id");
                            String name = poiObject.getString("name");
                            String locationName = poiObject.getString("locationName");
                            JSONObject location = poiObject.getJSONObject("location");
                            double latitude = location.getJSONArray("coordinates").getDouble(1);
                            double longitude = location.getJSONArray("coordinates").getDouble(0);
                            String street = poiObject.optString("street", null);
                            String postcode = poiObject.optString("postcode", null);
                            String description = poiObject.getString("description");
                            String category = poiObject.getString("category");
                            String thumbnail = poiObject.getString("thumbnail");

                            pois.add(new PointOfInterestDomain(id, name, locationName, latitude, longitude, street, postcode, description, category, thumbnail));
                        }
                        runOnUiThread(() -> initRecyclerPOIView(pois));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ListOfPointOfInterest.this, "Erro ao processar resposta JSON", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            Toast.makeText(ListOfPointOfInterest.this, "Erro na solicitação: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}