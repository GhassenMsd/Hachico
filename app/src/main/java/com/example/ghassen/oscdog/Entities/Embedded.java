package com.example.ghassen.oscdog.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Embedded {

    @SerializedName("events")
    @Expose
    private List<Event> events = null;

    @SerializedName("notifications")
    @Expose
    private List<Notification> notifications = null;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "Embedded{" +
                "events=" + events +
                '}';
    }
}
