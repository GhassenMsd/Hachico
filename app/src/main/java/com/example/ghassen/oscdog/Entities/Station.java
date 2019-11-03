package com.example.ghassen.oscdog.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Station {
    @SerializedName("remainingFood")
    @Expose
    private String remainingFood;

    @SerializedName("remainingWater")
    @Expose
    private String remainingWater;

    @SerializedName("foodQuantity")
    @Expose
    private String foodQuantity;

    @SerializedName("waterQuantity")
    @Expose
    private String waterQuantity;

    @SerializedName("foodTime1")
    @Expose
    private String foodTime1;

    @SerializedName("foodTime2")
    @Expose
    private String foodTime2;

    @SerializedName("foodTime3")
    @Expose
    private String foodTime3;

    @SerializedName("waterTime1")
    @Expose
    private String waterTime1;

    @SerializedName("waterTime2")
    @Expose
    private String waterTime2;

    @SerializedName("waterTime3")
    @Expose
    private String waterTime3;

    @SerializedName("_links")
    @Expose
    private Links _links;

    public String getRemainingFood() {
        return remainingFood;
    }

    public void setRemainingFood(String remainingFood) {
        this.remainingFood = remainingFood;
    }

    public String getRemainingWater() {
        return remainingWater;
    }

    public void setRemainingWater(String remainingWater) {
        this.remainingWater = remainingWater;
    }

    public String getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(String foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public String getWaterQuantity() {
        return waterQuantity;
    }

    public void setWaterQuantity(String waterQuantity) {
        this.waterQuantity = waterQuantity;
    }

    public String getFoodTime1() {
        return foodTime1;
    }

    public void setFoodTime1(String foodTime1) {
        this.foodTime1 = foodTime1;
    }

    public String getFoodTime2() {
        return foodTime2;
    }

    public void setFoodTime2(String foodTime2) {
        this.foodTime2 = foodTime2;
    }

    public String getFoodTime3() {
        return foodTime3;
    }

    public void setFoodTime3(String foodTime3) {
        this.foodTime3 = foodTime3;
    }

    public String getWaterTime1() {
        return waterTime1;
    }

    public void setWaterTime1(String waterTime1) {
        this.waterTime1 = waterTime1;
    }

    public String getWaterTime2() {
        return waterTime2;
    }

    public void setWaterTime2(String waterTime2) {
        this.waterTime2 = waterTime2;
    }

    public String getWaterTime3() {
        return waterTime3;
    }

    public void setWaterTime3(String waterTime3) {
        this.waterTime3 = waterTime3;
    }

    public Links get_links() {
        return _links;
    }

    public void set_links(Links _links) {
        this._links = _links;
    }

    @Override
    public String toString() {
        return "Station{" +
                "remainingFood='" + remainingFood + '\'' +
                ", remainingWater='" + remainingWater + '\'' +
                ", foodQuantity='" + foodQuantity + '\'' +
                ", waterQuantity='" + waterQuantity + '\'' +
                ", foodTime1='" + foodTime1 + '\'' +
                ", foodTime2='" + foodTime2 + '\'' +
                ", foodTime3='" + foodTime3 + '\'' +
                ", waterTime1='" + waterTime1 + '\'' +
                ", waterTime2='" + waterTime2 + '\'' +
                ", waterTime3='" + waterTime3 + '\'' +
                ", _links=" + _links +
                '}';
    }
}
