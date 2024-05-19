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

import com.example.touristGuide_app.Adapters.CategoryAdapter;
import com.example.touristGuide_app.Adapters.PointOfInterestAdapter;
import com.example.touristGuide_app.Adapters.PopularAdapter;
import com.example.touristGuide_app.Domains.CategoryDomain;
import com.example.touristGuide_app.Domains.PointOfInterestDomain;
import com.example.touristGuide_app.Domains.PopularDomain;
import com.example.touristGuide_app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListOfPointOfInterest extends AppCompatActivity implements OnLocationSelectedListener {
    private RecyclerView.Adapter adapterPopular, adapterCategory, adapterBestStared, adapterOldest;
    private RecyclerView recyclerViewPoi;
    private RecyclerView.Adapter adapterPoi;
    private RecyclerView recyclerViewPopular, recyclerViewCategory, recyclerViewBestStared, recyclerViewOldest;
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
        ////////////////////////////POIS
        ArrayList<PointOfInterestDomain> pois = new ArrayList<>();
        pois.add(new PointOfInterestDomain("1", "Praia da Marinha", "Algarve, Portugal", 37.08749965, -8.406331708, null, "8400-407", "Praia da Marinha is one of the most emblematic and beautiful beaches in the Algarve region. It features stunning cliffs, crystal-clear waters, and golden sand.", "Nature", "pic1"));

        recyclerViewPoi = findViewById(R.id.recyclerViewPoi);
        recyclerViewPoi.setLayoutManager(new LinearLayoutManager(this));
        System.out.println("Antes do pointofinteredestadapter");
        adapterPoi = new PointOfInterestAdapter(pois);
        recyclerViewPoi.setAdapter(adapterPoi);


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
}