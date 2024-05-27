package com.example.touristGuide_app.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.touristGuide_app.Activities.DetailActivity;
import com.example.touristGuide_app.Domains.ListEventsDomain;
import com.example.touristGuide_app.R;
import com.example.touristGuide_app.Domains.ListEventsDomain;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListEventsAdapter extends RecyclerView.Adapter<ListEventsAdapter.ViewHolder> {
    ArrayList<ListEventsDomain> items;
    private String userId;
    private int userIdReq;
    private int calendarIdReq;

    public ListEventsAdapter(ArrayList<ListEventsDomain> items, String userId, int userIdReq, int calendarIdReq) {
        this.items = items;
        this.userId = userId;
        this.userIdReq = userIdReq;
        this.calendarIdReq = calendarIdReq;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_events, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListEventsDomain event = items.get(position);

        holder.titleTxt.setText(event.getName());
        holder.organizerTxt.setText(event.getOrganizer());
        holder.aboutTxt.setText(event.getAbout());
        holder.locationTxt.setText(event.getPointOfInterest().getLocationName()); // Usando o locationName
        holder.contactTxt.setText(event.getContact());
        holder.categoryOfPoiTxt.setText(event.getPointOfInterest().getCategory());
        holder.maxParticipantsTxt.setText(String.valueOf(event.getMaxParticipants()));
        holder.currentParticipantsTxt.setText(String.valueOf(event.getCurrentParticipants()));
        holder.priceOfEventTxt.setText(String.valueOf(event.getPrice()) + event.getCurrency());

        // Aqui você precisa carregar a imagem do thumbnail usando uma biblioteca como o Glide ou Picasso
        Glide.with(holder.itemView.getContext()).load(event.getPointOfInterest().getThumbnail()).into(holder.thumbnailImg);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("event", event);
            holder.itemView.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTxt, organizerTxt, aboutTxt, locationTxt, contactTxt, categoryOfPoiTxt, maxParticipantsTxt, currentParticipantsTxt, priceOfEventTxt;
        ImageView thumbnailImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.nameTxt);
            organizerTxt = itemView.findViewById(R.id.organizerTxt);
            aboutTxt = itemView.findViewById(R.id.about_txt);
            locationTxt = itemView.findViewById(R.id.location_txt);
            contactTxt = itemView.findViewById(R.id.contact_txt);
            categoryOfPoiTxt = itemView.findViewById(R.id.category_of_poi_txt);
            maxParticipantsTxt = itemView.findViewById(R.id.max_participants_txt);
            currentParticipantsTxt = itemView.findViewById(R.id.current_participants_txt);
            priceOfEventTxt = itemView.findViewById(R.id.price_of_event_txt);
            thumbnailImg = itemView.findViewById(R.id.thumbnailImg);
        }
    }
}

