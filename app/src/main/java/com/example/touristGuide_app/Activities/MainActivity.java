package com.example.touristGuide_app.Activities;

import android.content.ClipData;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cachecompass_tracker_app.R;
import com.example.touristGuide_app.Adapters.CategoryAdapter;
import com.example.touristGuide_app.Adapters.PopularAdapter;
import com.example.touristGuide_app.Domains.CategoryDomain;
import com.example.touristGuide_app.Domains.PopularDomain;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Locale;

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
                "coffered ceilings. ", 2, true, 4.8, "pic1", true, 1000));
        items.add(new PopularDomain("Mar caible, avendia lago", "Miami beach", "This 2 bed/ 1 bath home boasts an enormous, "+
                "open-living plan, accented by striking "+
                "architectural features and high-end finishes."+
                "Feel inspired by open sight lines that"+
                "embrace the outdoors, crowned by stunning"+
                "coffered ceilings. ", 1, false, 3, "pic2", false, 25000));
        items.add(new PopularDomain("Mar caible, avendia lago", "Miami beach", "This 2 bed/ 1 bath home boasts an enormous, "+
                "open-living plan, accented by striking "+
                "architectural features and high-end finishes."+
                "Feel inspired by open sight lines that"+
                "embrace the outdoors, crowned by stunning"+
                "coffered ceilings. ", 4, true, 5, "pic3", true, 30000));

        recyclerViewPopular=findViewById(R.id.viewPop);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterPopular=new PopularAdapter(items);
        recyclerViewPopular.setAdapter(adapterPopular);

        ArrayList<CategoryDomain> catsList = new ArrayList<>();
        catsList.add(new CategoryDomain("Beaches", "category1"));
        catsList.add(new CategoryDomain("Museums", "category2"));
        catsList.add(new CategoryDomain("Forest", "category3"));
        catsList.add(new CategoryDomain("Festivals", "category4"));
        catsList.add(new CategoryDomain("Camps", "category5"));

        recyclerViewCategory=findViewById(R.id.viewCat);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterCategory = new CategoryAdapter(catsList);
        recyclerViewCategory.setAdapter(adapterCategory);

    }
}
