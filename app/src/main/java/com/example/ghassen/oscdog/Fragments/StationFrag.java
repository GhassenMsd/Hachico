package com.example.ghassen.oscdog.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghassen.oscdog.Activities.SignUp;
import com.example.ghassen.oscdog.Activities.StationSettings;
import com.example.ghassen.oscdog.Entities.SignInResponse;
import com.example.ghassen.oscdog.Entities.SignUpResponse;
import com.example.ghassen.oscdog.R;
import com.google.gson.JsonObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationFrag extends Fragment {
    private int progressStatus = 0;
    Button buttonStart,buttonStart2,settings,callDog;
    ProgressBar vProgressBar,vProgressBar2;
    String remainingFood,remainingWater;
    private Handler handler = new Handler();

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            SharedPreferences prefs = getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
            final String token = prefs.getString("token", "No token defined");
            final String idStation = prefs.getString("idStation", "No idStation defined");
            final String idCollar = prefs.getString("idCollar", "No idCollar defined");
            remainingFood = prefs.getString("remainingFood", "No remainingFood defined");
            remainingWater = prefs.getString("remainingWater", "No remainingWater defined");
            final String foodQuantity = prefs.getString("foodQuantity", "No foodQuantity defined");
            final String waterQuantity = prefs.getString("waterQuantity", "No waterQuantity defined");
            final String foodTime1 = prefs.getString("foodTime1", "No foodTime1 defined");
            final String foodTime2 = prefs.getString("foodTime2", "No foodTime2 defined");
            final String foodTime3 = prefs.getString("foodTime3", "No foodTime3 defined");
            final String waterTime1 = prefs.getString("waterTime1", "No waterTime1 defined");
            final String waterTime2 = prefs.getString("waterTime2", "No waterTime2 defined");
            final String waterTime3 = prefs.getString("waterTime3", "No waterTime3 defined");
            Log.d("foodwaterfrompre", "onCreateView: "+Integer.parseInt(remainingFood)+" "+Integer.parseInt(remainingWater));
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("foodwaterfromback", "onCreateView: "+Integer.parseInt(remainingFood)+" "+Integer.parseInt(remainingWater));
            //SetProgressBars(Integer.parseInt(remainingFood),Integer.parseInt(remainingWater));
            new asyncTaskUpdateProgress().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new asyncTaskUpdateProgress2().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            return null;
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_station, container, false);



        //SetProgressBars(Integer.parseInt(remainingFood),Integer.parseInt(remainingWater));


        settings = view.findViewById(R.id.button3);
        callDog = view.findViewById(R.id.button2);

        vProgressBar = (ProgressBar) view.findViewById(R.id.food);
        vProgressBar2 = (ProgressBar) view.findViewById(R.id.water);

        new MyAsyncTask().execute();



        /*buttonStart.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                buttonStart.setClickable(false);
                new asyncTaskUpdateProgress().execute();
            }});


        buttonStart2.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                buttonStart2.setClickable(false);
                new asyncTaskUpdateProgress2().execute();
            }});*/
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplication(), StationSettings.class);
                startActivity(intent);
            }
        });

        return view;
    }


    public void SetProgressBars(int food,int water){
        vProgressBar.setProgress(food);
        vProgressBar2.setProgress(water);
    }


    public class asyncTaskUpdateProgress extends AsyncTask<Void, Integer, Void> {

        int progress;

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progress = 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            vProgressBar.setProgress(values[0]);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            while(progress<Integer.parseInt(remainingFood)){
                progress++;
                publishProgress(progress);
                SystemClock.sleep(50);
            }
            return null;
        }

    }

    public class asyncTaskUpdateProgress2 extends AsyncTask<Void, Integer, Void> {

        int progress1;

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progress1 = 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            vProgressBar2.setProgress(values[0]);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            while(progress1<Integer.parseInt(remainingWater)){
                progress1++;
                publishProgress(progress1);
                SystemClock.sleep(50);
            }
            return null;
        }

    }
}
