package com.example.ghassen.oscdog.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Collier {

    @SerializedName("_links")
    @Expose
    private Links links;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "Collier{" +
                "links=" + links +
                '}';
    }
}
