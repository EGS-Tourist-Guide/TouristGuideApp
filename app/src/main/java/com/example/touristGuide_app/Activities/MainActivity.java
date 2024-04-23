package com.example.touristGuide_app.Activities;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.touristGuide_app.Activities.OnLocationSelectedListener;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.touristGuide_app.Adapters.CategoryAdapter;
import com.example.touristGuide_app.Adapters.PopularAdapter;
import com.example.touristGuide_app.Domains.CategoryDomain;
import com.example.touristGuide_app.Domains.PopularDomain;
import com.example.touristGuide_app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnLocationSelectedListener {
    private RecyclerView.Adapter adapterPopular, adapterCategory;
    private RecyclerView recyclerViewPopular, recyclerViewCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        ArrayList<PopularDomain> items = new ArrayList<>();
        ArrayList<Date> dates = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2024, Calendar.MARCH, 23);
        Date date1 = calendar1.getTime();
        dates.add(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2024, Calendar.MARCH, 25);
        Date date2 = calendar2.getTime();
        dates.add(date2);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(2024, Calendar.MARCH, 26);
        Date date3 = calendar3.getTime();
        dates.add(date3);
        items.add(new PopularDomain("Mar caible, avendia lago", "Miami beach", "This 2 bed/ 1 bath home boasts an enormous, "+
                "open-living plan, accented by striking "+
                "architectural features and high-end finishes."+
                "Feel inspired by open sight lines that"+
                "embrace the outdoors, crowned by stunning"+
                "coffered ceilings. ", 2, true, 4.8, "pic1", true, 1000, date1));
        items.add(new PopularDomain("Mar caible, avendia lago", "Miami beach", "This 2 bed/ 1 bath home boasts an enormous, "+
                "open-living plan, accented by striking "+
                "architectural features and high-end finishes."+
                "Feel inspired by open sight lines that"+
                "embrace the outdoors, crowned by stunning"+
                "coffered ceilings. ", 1, false, 3, "pic2", false, 25000, date2));
        items.add(new PopularDomain("Mar caible, avendia lago", "Miami beach", "This 2 bed/ 1 bath home boasts an enormous, "+
                "open-living plan, accented by striking "+
                "architectural features and high-end finishes."+
                "Feel inspired by open sight lines that"+
                "embrace the outdoors, crowned by stunning"+
                "coffered ceilings. ", 4, true, 5, "pic3", true, 30000, date3));
        recyclerViewPopular=findViewById(R.id.viewPop);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterPopular=new PopularAdapter(items);
        recyclerViewPopular.setAdapter(adapterPopular);
        ArrayList<CategoryDomain> catsList = new ArrayList<>();
        catsList.add(new CategoryDomain("Beaches", "cat1"));
        catsList.add(new CategoryDomain("Museums", "cat2"));
        catsList.add(new CategoryDomain("Forest", "cat3"));
        catsList.add(new CategoryDomain("Festivals", "cat4"));
        catsList.add(new CategoryDomain("Camps", "cat5"));
        for (int i = 0; i < items.size(); i++) {
            // Obter a data correspondente ao índice do loop
            Date eventDate = dates.get(i);
            items.get(i).setEventDate(eventDate);
        }
        recyclerViewCategory=findViewById(R.id.viewCat);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterCategory = new CategoryAdapter(catsList);
        recyclerViewCategory.setAdapter(adapterCategory);
        LinearLayout myCalendarLayout = findViewById(R.id.myCalendar);
        myCalendarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a página de calendários
                Intent intent = new Intent(MainActivity.this, CalendarEmpty.class);
                intent.putExtra("fromDetailActivity", false);
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