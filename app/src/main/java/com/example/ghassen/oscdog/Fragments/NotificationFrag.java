package com.example.ghassen.oscdog.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ghassen.oscdog.Adapters.EventAdapter;
import com.example.ghassen.oscdog.Adapters.NotificationAdapter;
import com.example.ghassen.oscdog.Entities.Embedded;
import com.example.ghassen.oscdog.Entities.Event;
import com.example.ghassen.oscdog.Entities.Notification;
import com.example.ghassen.oscdog.Entities.NotificationResponse;
import com.example.ghassen.oscdog.R;
import com.example.ghassen.oscdog.retro.RetrofitInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationFrag extends Fragment {
    RecyclerView recyclerView;
    RetrofitInterface api;
    public String typeNot;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        SharedPreferences prefs = getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        final String token = prefs.getString("token", "No token defined");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        //creating the api interface
        api = retrofit.create(RetrofitInterface.class);

        recyclerView = view.findViewById(R.id.recyclerViewNot);
        progressBar = view.findViewById(R.id.homeprogress);




        api.GetNotification("Bearer "+token).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if(response.isSuccessful()){

                    Embedded embedded = response.body().getEmbedded();
                    Log.d("notifications", "onResponse: "+embedded.toString());

                    NotificationAdapter adapter = new NotificationAdapter(getActivity(),embedded.getNotifications());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.INVISIBLE);
                }else{
                    Toast.makeText(getContext(),"response not succes",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Toast.makeText(getContext(),"fail",Toast.LENGTH_SHORT).show();

            }
        });



        return view;
    }

}
