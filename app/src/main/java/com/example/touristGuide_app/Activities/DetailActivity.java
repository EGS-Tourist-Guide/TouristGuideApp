package com.example.touristGuide_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.touristGuide_app.Domains.PopularDomain;
import com.example.touristGuide_app.R;

public class DetailActivity extends AppCompatActivity {
    private TextView titleTxt, locationTxt, bedTxt, guideTxt, wifiTxt, descriptionTxt, scoreTxt;
    private PopularDomain item;
    private ImageView picImg, backBtn;
    private Button btnBookNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        setVariable();
    }

    private void setVariable() {
        item = (PopularDomain) getIntent().getSerializableExtra("object");

        titleTxt.setText(item.getTitle());
        scoreTxt.setText(""+ item.getScore());
        locationTxt.setText(item.getLocation());
        bedTxt.setText(item.getBed()+" Bed");
        descriptionTxt.setText(item.getDescription());

        if (item.isGuide()){
            guideTxt.setText("Guide");
        }else {
            guideTxt.setText("No-Guide");
        }

        if(item.isWifi()){
            wifiTxt.setText("WiFi");
        }else {
            wifiTxt.setText("No-WiFi");
        }

        int drawableResId = getResources().getIdentifier(item.getPic(),"drawable", getPackageName());

        Glide.with(this).load(drawableResId).into(picImg);

        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Código para iniciar a activity_calendar quando btnBookNow for clicado
                Intent intent = new Intent(DetailActivity.this, CalendarEmpty.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        titleTxt = findViewById(R.id.titleTxt);
        locationTxt = findViewById(R.id.locationTxt);
        bedTxt = findViewById(R.id.bedTxt);
        guideTxt = findViewById(R.id.guideTxt);
        wifiTxt = findViewById(R.id.wifiTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        scoreTxt = findViewById(R.id.scoreTxt);
        picImg = findViewById(R.id.picImg);
        backBtn = findViewById(R.id.backBtn);
        btnBookNow = findViewById(R.id.btnBookNow);
    }
}