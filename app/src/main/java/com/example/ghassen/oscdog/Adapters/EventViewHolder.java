package com.example.ghassen.oscdog.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ghassen.oscdog.R;

public class EventViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView description;
    public TextView Date;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.Title);
        description = (TextView) itemView.findViewById(R.id.Desc);
        Date = (TextView) itemView.findViewById(R.id.Date);
    }
}
