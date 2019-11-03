package com.example.ghassen.oscdog.Entities;

import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("dateString")
    private String date;

    private String type;

    public Event(String title, String description, String date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Event(String title, String description, String date, String type) {

        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
