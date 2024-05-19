package com.example.touristGuide_app.Adapters;

import android.content.Intent;
import android.util.Log;
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
import com.example.touristGuide_app.Activities.ListOfEvents;
import com.example.touristGuide_app.Domains.PointOfInterestDomain;
import com.example.touristGuide_app.R;
import java.util.ArrayList;

public class PointOfInterestAdapter extends RecyclerView.Adapter<PointOfInterestAdapter.ViewHolder> {
    ArrayList<PointOfInterestDomain> items;
    public PointOfInterestAdapter(ArrayList<PointOfInterestDomain> items) {
        this.items = items;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("Chegou ao adapter do POI");
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_poi, parent, false);
        return new ViewHolder(inflate);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PointOfInterestDomain item = items.get(position);
        holder.nameTxt.setText(item.getName());
        holder.descriptionTxt.setText(item.getDescription());
        holder.locationTxt.setText(item.getLocationName());

        int resourceId = holder.itemView.getContext().getResources().getIdentifier(item.getThumbnail(), "drawable", holder.itemView.getContext().getPackageName());
        Log.d("POIAdapter", "Loading image with resourceId: " + resourceId);
        Glide.with(holder.itemView.getContext())
                .load(resourceId)
                .transform(new CenterCrop(), new GranularRoundedCorners(20, 20, 20, 20))
                .into(holder.thumbnail);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ListOfEvents.class);
            intent.putExtra("poi", item);
            holder.itemView.getContext().startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, locationTxt, descriptionTxt;
        ImageView thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            descriptionTxt = itemView.findViewById(R.id.descriptionTxt);
            locationTxt = itemView.findViewById(R.id.locationTxt);
            thumbnail = itemView.findViewById(R.id.thumbnailImg);
        }
    }
}