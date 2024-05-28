package com.example.touristGuide_app.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.touristGuide_app.Adapters.CategoryAdapter;
import com.example.touristGuide_app.Domains.CategoryDomain;
import com.example.touristGuide_app.Domains.OneEventDomain;
import com.example.touristGuide_app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    private TextView nameTxt, organizerTxt, contactTxt, aboutTxt, priceTxt, maxParticipantsTxt, currentParticipantsTxt;
    private TextView startDateTxt, endDateTxt;
    String eventId, calendarIdReq, userIdReq, userId;
    private String serverIp;
    private OneEventDomain item;
    private ImageView thumbnailEventImg, backBtn, favoritesIcon, categoryEventImg;
    private Button btnBookNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId = getIntent().getStringExtra("eventId");
        serverIp = getString(R.string.ip);
//        calendarIdReq = getIntent().getStringExtra("calendarIdReq");
//        userIdReq = getIntent().getStringExtra("userIdReq");
//        userId = getIntent().getStringExtra("userId");
        setContentView(R.layout.activity_detail);
        initView();

        // Inicialize a lista de categorias com os dados corretos

        HashMap<String, Integer> map = new HashMap<>();
        // Adicione entradas para cada categoria e seu ID de recurso de imagem correspondente
        map.put("Nature", R.drawable.cat1);
        map.put("Food", R.drawable.cat2);
        map.put("Culture", R.drawable.cat3);
        map.put("Shopping", R.drawable.cat4);
        map.put("Landmarks", R.drawable.cat5);

        fetchEventDetails(eventId, map);
        setVariable();
    }
    private void initView() {
        nameTxt = findViewById(R.id.nameTxt);
        organizerTxt = findViewById(R.id.organizerTxt);
        categoryEventImg = findViewById(R.id.categoryEventImg);
        contactTxt = findViewById(R.id.contactTxt);

        startDateTxt = findViewById(R.id.startDateTxt);
        endDateTxt = findViewById(R.id.endDateTxt);

        aboutTxt = findViewById(R.id.aboutTxt);
        priceTxt = findViewById(R.id.priceTxt);

        maxParticipantsTxt = findViewById(R.id.maxParticipantsTxt);
        currentParticipantsTxt = findViewById(R.id.currentParticipantsTxt);

        favoritesIcon = findViewById(R.id.favoritesIcon);
        thumbnailEventImg = findViewById(R.id.thumbnailEventImg);
        backBtn = findViewById(R.id.backBtn);
        btnBookNow = findViewById(R.id.btnBookNow);
        // Inicializa o dateFormat
    }
    private void setVariable() {
        item = (OneEventDomain) getIntent().getSerializableExtra("object");

        backBtn.setOnClickListener(v -> finish());

        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
    private void fetchEventDetails(String eventId, HashMap<String, Integer> categoryImageMap) {
        String url = "http://grupo4-egs-deti.ua.pt/e1/events/" + eventId;
        String apiKey = "93489d58-e2cf-4e11-b3ac-74381fee38ac";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the JSON response and update the UI
                            String name = response.getString("name");
                            String organizer = response.getString("organizer");
                            String contact = response.getString("contact");
                            String about = response.getString("about");
                            double price = response.getDouble("price");
                            String currency = response.getString("currency");
                            int maxParticipants = response.getInt("maxparticipants");
                            int currentParticipants = response.getInt("currentparticipants");

                            String startDate = response.getString("startdate");
                            String endDate = response.getString("enddate");
                            String category = response.getJSONObject("pointofinterest").getString("category").toLowerCase();


                            String thumbnail = response.getJSONObject("pointofinterest").getString("thumbnail");

                            nameTxt.setText(name);
                            organizerTxt.setText(organizer);
                            contactTxt.setText(contact);
                            aboutTxt.setText(about);
                            priceTxt.setText(currency + " " + price);
                            maxParticipantsTxt.setText(String.valueOf(maxParticipants));
                            currentParticipantsTxt.setText(String.valueOf(currentParticipants));
                            startDateTxt.setText(startDate);
                            endDateTxt.setText(endDate);

                            Glide.with(DetailActivity.this).load(thumbnail).into(thumbnailEventImg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailActivity.this, "Failed to parse event details", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("accept", "application/json");
                headers.put("SERVICE-API-KEY", apiKey);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
    private void handleErrorResponse(VolleyError error) {
        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            String errorMessage = new String(error.networkResponse.data);
            Log.e("API_ERROR", "Error response: " + errorMessage);

            try {
                JSONObject errorJson = new JSONObject(errorMessage);
                if (errorJson.has("error")) {
                    JSONObject errorObject = errorJson.getJSONObject("error");
                    String message = errorObject.getString("message");

                    switch (statusCode) {
                        case 401:
                            Toast.makeText(DetailActivity.this, "Erro 401: Não autorizado - " + message, Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(DetailActivity.this, "Erro 404: Recurso não encontrado - " + message, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(DetailActivity.this, "Erro " + statusCode + ": " + message, Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Erro JSON inesperado: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(DetailActivity.this, "Erro ao processar resposta de erro JSON", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("API_ERROR", "Network error occurred");
            Toast.makeText(DetailActivity.this, "Erro de rede ao carregar o evento. Verifique sua conexão com a Internet e tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }
}