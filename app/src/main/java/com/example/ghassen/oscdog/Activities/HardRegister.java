package com.example.ghassen.oscdog.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ghassen.oscdog.Entities.HardRegisterResponse;
import com.example.ghassen.oscdog.Entities.Station;
import com.example.ghassen.oscdog.R;
import com.example.ghassen.oscdog.retro.RetrofitInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HardRegister extends AppCompatActivity {

    Button next,back;
    RetrofitInterface api;
    EditText CodeCollar,CodeStation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_register);


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson)) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        //creating the api interface
        api = retrofit.create(RetrofitInterface.class);

        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        final String token = prefs.getString("token","no token defined");
        final String idDog = prefs.getString("idDog", "No id defined");



        next = findViewById(R.id.button4);
        back = findViewById(R.id.button);
        CodeCollar = findViewById(R.id.FullName);
        CodeStation = findViewById(R.id.Phone);




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject object = new JsonObject();
                object.addProperty("collarId","5d654074210d7200043705c4");
                object.addProperty("dogId",idDog);
                object.addProperty("stationId","5d653a2392b65c0004e62437");

                api.PostCollarSattion(object,"Bearer "+token).enqueue(new Callback<HardRegisterResponse>() {
                    @Override
                    public void onResponse(Call<HardRegisterResponse> call, Response<HardRegisterResponse> response) {
                        if(response.isSuccessful()){
                            HardRegisterResponse hard = response.body();
                            SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();

                            editor.putString("idCollar",hard.getCollarId());
                            editor.putString("idStation",hard.getStationId());
                            editor.apply();
                            GetStationByDog();

                            Log.d("bodyHard", "onResponse: "+hard);
                            Intent intent = new Intent(HardRegister.this,Home.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        }else {
                            Toast.makeText(getApplicationContext(),"Wrong Code",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<HardRegisterResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });



/*        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HardRegister.this,SignUpDog.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });*/
    }

    private void GetStationByDog() {
        SharedPreferences prefss = getSharedPreferences("User", MODE_PRIVATE);
        final String tokenn = prefss.getString("token", "No token defined");
        final String idDogg = prefss.getString("idDog", "No idDog defined");

        api.GetStationById(idDogg,"Bearer "+tokenn).enqueue(new Callback<Station>() {
            @Override
            public void onResponse(Call<Station> call, Response<Station> response) {
                Station station = response.body();
                Log.d("Station", "onResponse: "+station);


                SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                editor.putString("remainingFood",station.getRemainingFood());
                editor.putString("remainingWater",station.getRemainingWater());
                editor.putString("foodQuantity",station.getFoodQuantity());
                editor.putString("waterQuantity",station.getWaterQuantity());
                editor.putString("foodTime1",station.getFoodTime1());
                editor.putString("foodTime2",station.getFoodTime2());
                editor.putString("foodTime3",station.getFoodTime3());
                editor.putString("waterTime1",station.getWaterTime1());
                editor.putString("waterTime2",station.getWaterTime2());
                editor.putString("waterTime3",station.getWaterTime3());
                editor.apply();
            }

            @Override
            public void onFailure(Call<Station> call, Throwable t) {

                Toast.makeText(HardRegister.this, "Error Station", Toast.LENGTH_SHORT).show();            }
        });



    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
