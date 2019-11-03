package com.example.ghassen.oscdog.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ghassen.oscdog.Entities.Event;
import com.example.ghassen.oscdog.Entities.Notification;
import com.example.ghassen.oscdog.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder>{

    private Context context;
    private List<Notification> notificationList;

    public NotificationAdapter(Context context, List<Notification> ListNot){
        this.context = context;
        this.notificationList =  ListNot;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Drawable positionn = holder.imageNot.getContext().getResources().getDrawable(R.drawable.position);
        Drawable rc = holder.imageNot.getContext().getResources().getDrawable(R.drawable.rc);
        Drawable temp = holder.imageNot.getContext().getResources().getDrawable(R.drawable.temp);

        Notification notification = notificationList.get(position);
        holder.title.setText(notification.getTitle());
        holder.description.setText(notification.getDescription());

        if(notificationList.get(position).getTitle().equals("Temperature")){
            holder.imageNot.setImageDrawable(temp);
        }
        if(notificationList.get(position).getTitle().equals("Position")){
            holder.imageNot.setImageDrawable(positionn);
        }
        if(notificationList.get(position).getTitle().equals("Rythme cardiaque")){
            holder.imageNot.setImageDrawable(rc);
        }

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
