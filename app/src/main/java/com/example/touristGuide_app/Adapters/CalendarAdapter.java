package com.example.touristGuide_app.Adapters;

import static com.example.touristGuide_app.Activities.CalendarEmpty.savingDatesByID;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.touristGuide_app.Activities.CalendarEmpty;
import com.example.touristGuide_app.Activities.CalendarUtils;
import com.example.touristGuide_app.Activities.CalendarViewHolder;
import com.example.touristGuide_app.R;
import com.google.gson.internal.bind.util.ISO8601Utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>{
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener)
    {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size()>15)//month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        else // week view
            layoutParams.height = (int) parent.getHeight();

        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        final LocalDate date = days.get(position);
        if(date == null)
            holder.dayOfMonth.setText("");
        else{
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            // Verifique se a data está na lista de datas salvas e, se estiver, defina um background diferente
            if(date.equals(CalendarUtils.selectedDate)) {
                holder.parentView.setBackgroundColor(Color.LTGRAY);
//                System.out.println("Saved GRAY: " + date);
            }
            // Converter LocalDate para Date
            String dateString = date.toString();
            System.out.println("savingDatesByID = "+ savingDatesByID);
            if (savingDatesByID.contains(dateString)) {
                holder.parentView.setBackgroundColor(Color.RED);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return days.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, LocalDate date);
    }
}
