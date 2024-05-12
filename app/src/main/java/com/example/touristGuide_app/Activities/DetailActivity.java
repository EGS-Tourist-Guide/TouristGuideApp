package com.example.touristGuide_app.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.touristGuide_app.Domains.PopularDomain;
import com.example.touristGuide_app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

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
        // Exiba a data do evento na descrição
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String eventDateStr = dateFormat.format(item.getEventDate()); // startDate
        String descriptionWithDate = item.getDescription() + "\n\nEvent Date: " + eventDateStr;
        descriptionTxt.setText(descriptionWithDate);
        // String eventDateStartStr = dateFormat.format(item.getStartDateTime()); // startDate
        // String eventDateEndStr = dateFormat.format(item.getEndDateTime()); // endDate
        // String descriptionWithStartAndEndDate = item.getDescription() + "\n\nEvent Start Date: " + eventDateStartStr + "\n\nEvent End Date: " + eventDateEndStr;
        // descriptionTxt.setText(descriptionWithStartAndEndDate);
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
                // Criar um objeto JSON contendo os detalhes do evento
                JSONObject eventDetailsJson = new JSONObject();
                try {
                    eventDetailsJson.put("title", item.getTitle());
                    eventDetailsJson.put("location", item.getLocation());
                    eventDetailsJson.put("score", item.getScore());
                    // Adicione outros detalhes do evento conforme necessário
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Criar um AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                // Definir título e mensagem do popup
                builder.setTitle("Booking Successful")
                        .setMessage("Your booking has been successfully made.")
                        // Adicionar botão de OK e definir seu comportamento
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Iniciar a atividade do calendário e passar os detalhes do evento como uma string extra
                                Intent intent = new Intent(DetailActivity.this, CalendarEmpty.class);
                                intent.putExtra("eventDetails", eventDetailsJson.toString());
                                // Toast.makeText(DetailActivity.this, "A passar: "+ item.getEventDate(), Toast.LENGTH_SHORT).show();
                                // System.out.println("A passar: "+ item.getEventDate());
                                intent.putExtra("eventDate", item.getEventDate());
                                intent.putExtra("userId", item.getUserId());
                                
                                intent.putExtra("fromDetailActivity", true); // Adiciona esta flag
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                // Criar e exibir o AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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