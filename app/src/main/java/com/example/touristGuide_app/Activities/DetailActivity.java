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
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private TextView nameTxt, organizerTxt, contactTxt, aboutTxt, priceTxt, maxParticipantsTxt, currentParticipantsTxt;
    private Date startDateTxt, endDateTxt;
    private PopularDomain item;
    private ImageView thumbnailEventImg, backBtn, favoritesIcon, categoryEventImg;
    private Button btnBookNow;
    private void initView() {
        nameTxt = findViewById(R.id.nameTxt);
        organizerTxt = findViewById(R.id.organizerTxt);
        categoryEventImg = findViewById(R.id.categoryEventImg);
        contactTxt = findViewById(R.id.contactTxt);

        //startDateTxt = findViewById(R.id.startDateTxt);
        //endDateTxt = findViewById(R.id.endDateTxt);

        aboutTxt = findViewById(R.id.aboutTxt);
        priceTxt = findViewById(R.id.priceTxt);

        maxParticipantsTxt = findViewById(R.id.maxParticipantsTxt);
        currentParticipantsTxt = findViewById(R.id.currentParticipantsTxt);

        favoritesIcon = findViewById(R.id.favoritesIcon);
        thumbnailEventImg = findViewById(R.id.thumbnailEventImg);
        backBtn = findViewById(R.id.backBtn);
        btnBookNow = findViewById(R.id.btnBookNow);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        setVariable();
    }

    private void setVariable() {
        item = (PopularDomain) getIntent().getSerializableExtra("object");
        //Toast.makeText(this, "userIdEP: "+item.getUserIdReq()+"\ncalendarIdEP"+item.getCalendarIdReq(), Toast.LENGTH_SHORT).show();

        nameTxt.setText(item.getName());
        organizerTxt.setText(item.getOrganizer());
        contactTxt.setText(item.getContact());

        //priceTxt.setDouble(item.getPrice());
        aboutTxt.setText(item.getAbout());

        maxParticipantsTxt.setText(item.getMaxParticipants());
        currentParticipantsTxt.setText(item.getCurrentParticipants());


        // Exiba a data do evento na descrição
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String startDateEndStr = dateFormat.format(item.getStartDate()); // startDate
        String eventDateEndStr = dateFormat.format(item.getEndDate()); // enddate
        //startDateTxt.setText(startDateEndStr);
        //endDateTxt.setText(eventDateEndStr);

        int drawableResId = getResources().getIdentifier(item.getThumbnailEvent(),"drawable", getPackageName());
        Glide.with(this).load(drawableResId).into(thumbnailEventImg);
        int drawableResId1 = getResources().getIdentifier(item.getCategory(),"drawable", getPackageName());
        Glide.with(this).load(drawableResId1).into(categoryEventImg);

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
                //try {
                    //eventDetailsJson.put("title", item.getTitle());
                    //eventDetailsJson.put("location", item.getLocation());
                    //eventDetailsJson.put("score", item.getScore());
                    // Adicione outros detalhes do evento conforme necessário
                //} catch (JSONException e) {
                //    e.printStackTrace();
                //}

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
                                //intent.putExtra("eventDate", item.getEventDate());

                                intent.putExtra("userId", item.getUserId());
                                intent.putExtra("userIdReq", item.getUserIdReq());
                                intent.putExtra("calendarIdReq", item.getCalendarIdReq());

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


}