package com.example.ghassen.oscdog.retro;

public class ApiUtil {

    private static final String BASE_URL = "https://oprex-backend.herokuapp.com/";

    public static RetrofitInterface getServiceClass(){
        return RetrofitAPI.getRetrofit(BASE_URL).create(RetrofitInterface.class);
    }
}