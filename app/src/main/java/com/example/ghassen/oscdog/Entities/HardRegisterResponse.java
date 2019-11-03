package com.example.ghassen.oscdog.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HardRegisterResponse {
    @SerializedName("collarId")
    @Expose
    private String collarId;

    @SerializedName("dogId")
    @Expose
    private String dogId;

    @SerializedName("stationId")
    @Expose
    private String stationId;

    public String getCollarId() {
        return collarId;
    }

    public void setCollarId(String collarId) {
        this.collarId = collarId;
    }

    public String getDogId() {
        return dogId;
    }

    public void setDogId(String dogId) {
        this.dogId = dogId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    @Override
    public String toString() {
        return "HardRegisterResponse{" +
                "collarId='" + collarId + '\'' +
                ", dogId='" + dogId + '\'' +
                ", stationId='" + stationId + '\'' +
                '}';
    }
}
