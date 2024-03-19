package com.example.touristGuide_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.touristGuide_app.R;
import com.example.touristGuide_app.Adapters.CategoryAdapter;
import com.example.touristGuide_app.Adapters.PopularAdapter;
import com.example.touristGuide_app.Domains.CategoryDomain;
import com.example.touristGuide_app.Domains.PopularDomain;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterPopular, adapterCategory;
    private RecyclerView recyclerViewPopular, recyclerViewCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
    }

    private void initRecyclerView(){
        ArrayList<PopularDomain> items = new ArrayList<>();

        items.add(new PopularDomain("Mar caible, avendia lago", "Miami beach", "This 2 bed/ 1 bath home boasts an enormous, "+
                "open-living plan, accented by striking "+
                "architectural features and high-end finishes."+
                "Feel inspired by open sight lines that"+
                "embrace the outdoors, crowned by stunning"+
                "coffered ceilings. ", 2, true, 4.8, "pic1", true, 1000, new Date(2024, 03, 21)));
        items.add(new PopularDomain("Mar caible, avendia lago", "Miami beach", "This 2 bed/ 1 bath home boasts an enormous, "+
                "open-living plan, accented by striking "+
                "architectural features and high-end finishes."+
                "Feel inspired by open sight lines that"+
                "embrace the outdoors, crowned by stunning"+
                "coffered ceilings. ", 1, false, 3, "pic2", false, 25000, new Date(2024, 03, 25)));
        items.add(new PopularDomain("Mar caible, avendia lago", "Miami beach", "This 2 bed/ 1 bath home boasts an enormous, "+
                "open-living plan, accented by striking "+
                "architectural features and high-end finishes."+
                "Feel inspired by open sight lines that"+
                "embrace the outdoors, crowned by stunning"+
                "coffered ceilings. ", 4, true, 5, "pic3", true, 30000, new Date(2024, 03, 26)));

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

        // Exemplo de adição de data ao primeiro item
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.MARCH, 25); // Defina a data do evento aqui
        Date eventDate = calendar.getTime();

        items.get(0).setEventDate(eventDate);

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
                startActivity(intent);
            }
        });


    }
}
