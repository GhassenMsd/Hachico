package com.example.ghassen.oscdog.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ghassen.oscdog.Entities.Event;
import com.example.ghassen.oscdog.R;

import java.util.List;

public class EventAdapter extends  RecyclerView.Adapter<EventViewHolder> {

    private Context context;
    private List<Event> eventList;

    public EventAdapter(Context context, List<Event> ListEvents){
        this.context = context;
        this.eventList =  ListEvents;
    }


    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.title.setText(event.getTitle());
        holder.description.setText(event.getDescription());
        holder.Date.setText(event.getDate());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
