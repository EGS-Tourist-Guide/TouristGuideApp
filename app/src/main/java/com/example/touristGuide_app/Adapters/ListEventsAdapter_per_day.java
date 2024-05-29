package com.example.touristGuide_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.touristGuide_app.Activities.DetailActivity;
import com.example.touristGuide_app.Domains.ListEventsDomain_per_day;
import com.example.touristGuide_app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ListEventsAdapter_per_day extends RecyclerView.Adapter<ListEventsAdapter_per_day.ViewHolder> {
    ArrayList<ListEventsDomain_per_day> items;
    Context context;

    public ListEventsAdapter_per_day(Context context, ArrayList<ListEventsDomain_per_day> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_events_per_day, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListEventsDomain_per_day event = items.get(position);

        holder.titleTxt.setText(event.getName());

    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, organizerTxt, aboutTxt, locationTxt, contactTxt, categoryOfPoiTxt, maxParticipantsTxt, currentParticipantsTxt, priceOfEventTxt, startDateTxt, endDateTxt;
        ImageView thumbnailImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.nameTxt);
            organizerTxt = itemView.findViewById(R.id.organizer_txt);
            aboutTxt = itemView.findViewById(R.id.about_txt);
            locationTxt = itemView.findViewById(R.id.location_txt);
            contactTxt = itemView.findViewById(R.id.contact_txt);
            categoryOfPoiTxt = itemView.findViewById(R.id.category_of_poi_txt);
            maxParticipantsTxt = itemView.findViewById(R.id.max_participants_txt);
            currentParticipantsTxt = itemView.findViewById(R.id.current_participants_txt);
            priceOfEventTxt = itemView.findViewById(R.id.price_of_event_txt);
            startDateTxt = itemView.findViewById(R.id.startDateTxt);
            endDateTxt = itemView.findViewById(R.id.endDateTxt);
            thumbnailImg = itemView.findViewById(R.id.thumbnailImg);
        }
    }
}

