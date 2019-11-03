package com.example.ghassen.oscdog.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PositionDog {
    @SerializedName("x")
    @Expose
    private float x;

    @SerializedName("y")
    @Expose
    private float y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public PositionDog(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "PositionDog{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
