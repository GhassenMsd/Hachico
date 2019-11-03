package com.example.ghassen.oscdog.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ghassen.oscdog.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
public TextView title;
public TextView description;
public CircleImageView imageNot;

public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.Title);
        description = (TextView) itemView.findViewById(R.id.Desc);
        imageNot = (CircleImageView) itemView.findViewById(R.id.circleImageView2);
        }
}
