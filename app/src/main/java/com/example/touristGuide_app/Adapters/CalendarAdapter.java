package com.example.touristGuide_app.Adapters;
import static com.example.touristGuide_app.Activities.CalendarEmpty.savingDatesByID;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.touristGuide_app.Activities.CalendarViewHolder;
import com.example.touristGuide_app.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>{
    private final ArrayList<LocalDate> days;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
            layoutParams.height = parent.getHeight();
        return new CalendarViewHolder(view, onItemListener, days);
    }
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        final LocalDate date = days.get(position);
        if (date == null)
            holder.dayOfMonth.setText("");
        else {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            // Check if the date is within the range of any saved start and end dates
            for (String dateKey : savingDatesByID) {
                String[] dates = dateKey.split("_");
                try {
                    System.out.println("Calendar adapter dates start and end: " + dates[0] + " " + dates[1]);
                    LocalDateTime startDateTime = LocalDateTime.parse(dates[0], formatter);
                    System.out.println("Calendar adapter parse start: " + startDateTime);
                    LocalDateTime endDateTime = LocalDateTime.parse(dates[1], formatter);
                    System.out.println("Calendar adapter parse end: " + endDateTime);

                    // Convert LocalDateTime to LocalDate for comparison
                    LocalDate startDate = startDateTime.toLocalDate();
                    LocalDate endDate = endDateTime.toLocalDate();

                    if (date.isEqual(startDate) || date.isEqual(endDate) || (date.isAfter(startDate) && date.isBefore(endDate))) {
                        holder.parentView.setBackgroundColor(Color.RED);
                        break; // Once marked, no need to check further
                    }
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                    // Handle the exception if the date format is not as expected
                    System.err.println("Error parsing dates: " + dates[0] + " or " + dates[1]);
                }
            }
        }
    }
    @Override
    public int getItemCount() {
        return days.size();
    }
    public interface  OnItemListener {
        void onItemClick(int position, LocalDate date);
    }
}
