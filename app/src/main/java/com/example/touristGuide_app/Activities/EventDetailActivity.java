package com.example.touristGuide_app.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.touristGuide_app.R;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        // Receive the event details JSON string from the Intent
        String eventDetailsJsonString = getIntent().getStringExtra("eventDetails");

        if (eventDetailsJsonString == null || eventDetailsJsonString.isEmpty()) {
            // If event details JSON string is null or empty, log an error and finish the activity
            Log.e("EventDetailActivity", "Event details JSON string is null or empty");
            Toast.makeText(this, "Error: Event details JSON string is null or empty", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            // Parse the event details JSON string into a List
            Gson gson = new Gson();
            List<String> eventDetails = gson.fromJson(eventDetailsJsonString, new TypeToken<List<String>>() {}.getType());

            // Log the event details received
            Log.d("EventDetailActivity", "Event details: " + eventDetails);

            // Display the event details
            TextView eventDetailsTextView = findViewById(R.id.eventNameTextView);

            StringBuilder eventDetailsStringBuilder = new StringBuilder();
            for (String eventDetail : eventDetails) {
                eventDetailsStringBuilder.append(eventDetail).append("\n");
            }
            eventDetailsTextView.setText(eventDetailsStringBuilder.toString());

            // Attach click listener to the delete button
            Button deleteButton = findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("EventDetailActivity", "Delete button clicked");
                    // Finish this activity
                    finish();
                }
            });

        } catch (JsonSyntaxException e) {
            // If there is an error parsing the JSON, log an error and finish the activity
            e.printStackTrace();
            Log.e("EventDetailActivity", "Error parsing event details JSON");
            Toast.makeText(this, "Error: Unable to parse event details", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
