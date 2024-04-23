package com.example.touristGuide_app.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.touristGuide_app.R;
import org.json.JSONException;
import org.json.JSONObject;

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
            // Parse the event details JSON string into a JSONObject
            JSONObject eventDetailsJson = new JSONObject(eventDetailsJsonString);

            // Extract the event details from the JSONObject
            String eventName = eventDetailsJson.optString("title");
            String eventLocation = eventDetailsJson.optString("location");
            int eventScore = eventDetailsJson.optInt("score");
            // Extract other event details as needed

            // Display the event details
            TextView eventNameTextView = findViewById(R.id.eventNameTextView);
            TextView eventLocationTextView = findViewById(R.id.eventLocationTextView);
            TextView eventScoreTextView = findViewById(R.id.eventScoreTextView);
            // Display other event details as needed

            eventNameTextView.setText(eventName);
            eventLocationTextView.setText("Location: " + eventLocation);
            eventScoreTextView.setText("Score: " + eventScore);
            // Display other event details as needed

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

        } catch (JSONException e) {
            // If there is an error parsing the JSON, log an error and finish the activity
            e.printStackTrace();
            Log.e("EventDetailActivity", "Error parsing event details JSON");
            Toast.makeText(this, "Error: Unable to parse event details", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
