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
import com.example.touristGuide_app.R;
import com.example.touristGuide_app.Domains.PopularDomain;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {
    ArrayList<PopularDomain> items;
    private String userId;
    private int userIdReq;
    private int calendarIdReq;

    public PopularAdapter(ArrayList<PopularDomain> items, String userId, int userIdReq, int calendarIdReq) {
        this.items = items;
        this.userId = userId;
        this.userIdReq = userIdReq;
        this.calendarIdReq = calendarIdReq;
    }

    @NonNull
    @Override
    public PopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.ViewHolder holder, int position) {
        //holder.titleTxt.setText(items.get(position).getTitle());
        //holder.locationTxt.setText(items.get(position).getLocation());
        //holder.scoreTxt.setText(""+items.get(position).getScore());

        //int drawableResId=holder.itemView.getResources().getIdentifier(items.get(position).getPic(),"drawable", holder.itemView.getContext().getPackageName());
        //Glide.with(holder.itemView.getContext()).load(drawableResId)
        //        .transform(new CenterCrop(), new GranularRoundedCorners(40,40,40,40)).into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("object", items.get(position));
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTxt, locationTxt, scoreTxt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            locationTxt = itemView.findViewById(R.id.locationTxt);
            scoreTxt = itemView.findViewById(R.id.scoreTxt);
            pic = itemView.findViewById(R.id.picImg);
        }
    }
}
