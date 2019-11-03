package com.example.ghassen.oscdog.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ghassen.oscdog.Entities.Station;
import com.example.ghassen.oscdog.Fragments.StationFrag;
import com.example.ghassen.oscdog.R;
import com.example.ghassen.oscdog.retro.RetrofitInterface;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationSettings extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText FoodQuantity,WaterQuantity;
    Spinner FoodsTime1,FoodsTime2,FoodsTime3,WaterTime1,WaterTime2,WaterTime3;
    String token,idStation,idCollar,foodQuantity,waterQuantity,foodTime1,foodTime2,foodTime3,waterTime1,waterTime2,waterTime3;
    String FoodsTime1Selected,FoodsTime2Selected,FoodsTime3Selected,WaterTime1Selected,WaterTime2Selected,WaterTime3Selected;
    RetrofitInterface api;
    ProgressDialog progressDialog;

    Button save,back,annuler;
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        FoodsTime1Selected = FoodsTime1.getSelectedItem().toString();
        FoodsTime2Selected = FoodsTime2.getSelectedItem().toString();
        FoodsTime3Selected = FoodsTime3.getSelectedItem().toString();
        WaterTime1Selected = WaterTime1.getSelectedItem().toString();
        WaterTime2Selected = WaterTime2.getSelectedItem().toString();
        WaterTime3Selected = WaterTime3.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            SharedPreferences prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
            token = prefs.getString("token", "No token defined");
            idStation = prefs.getString("idStation", "No idStation defined");
            idCollar = prefs.getString("idCollar", "No idCollar defined");
            foodQuantity = prefs.getString("foodQuantity", "No foodQuantity defined");
            waterQuantity = prefs.getString("waterQuantity", "No waterQuantity defined");
            foodTime1 = prefs.getString("foodTime1", "No foodTime1 defined");
            foodTime2 = prefs.getString("foodTime2", "No foodTime2 defined");
            foodTime3 = prefs.getString("foodTime3", "No foodTime3 defined");
            waterTime1 = prefs.getString("waterTime1", "No waterTime1 defined");
            waterTime2 = prefs.getString("waterTime2", "No waterTime2 defined");
            waterTime3 = prefs.getString("waterTime3", "No waterTime3 defined");
            Log.d("foodwaterqu", "onPreExecute: "+waterTime1+" "+waterTime2);

            ConfigureSpinner(foodTime1,foodTime2,foodTime3,waterTime1,waterTime2,waterTime3);

        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("foodwaterqufromback", "onPreExecute: "+foodQuantity+" "+waterQuantity);
            FoodQuantity.setText(foodQuantity);
            WaterQuantity.setText(waterQuantity);
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_settings);
        progressDialog = new ProgressDialog(this);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        //creating the api interface
        api = retrofit.create(RetrofitInterface.class);


        FoodQuantity = findViewById(R.id.Race);
        WaterQuantity = findViewById(R.id.BirthDate);
        FoodsTime1 = findViewById(R.id.Gender);
        FoodsTime2 = findViewById(R.id.Gender1);
        FoodsTime3 = findViewById(R.id.Gender2);
        WaterTime1 = findViewById(R.id.time1);
        WaterTime2 = findViewById(R.id.time2);
        WaterTime3 = findViewById(R.id.time3);
        save = findViewById(R.id.button);
        back = findViewById(R.id.button5);
        annuler = findViewById(R.id.button6);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StationSettings.this,Home.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StationSettings.this,Home.class);
                startActivity(intent);
            }
        });

        new MyAsyncTask().execute();



        // Spinner click listener
        FoodsTime1.setOnItemSelectedListener(this);
        FoodsTime2.setOnItemSelectedListener(this);
        FoodsTime3.setOnItemSelectedListener(this);
        WaterTime1.setOnItemSelectedListener(this);
        WaterTime2.setOnItemSelectedListener(this);
        WaterTime3.setOnItemSelectedListener(this);






        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Updating station");
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();


                JsonObject object = new JsonObject();
                object.addProperty("foodQuantity",FoodQuantity.getText().toString());
                object.addProperty("waterQuantity",WaterQuantity.getText().toString());
                object.addProperty("foodTime1",FoodsTime1Selected);
                object.addProperty("foodTime2",FoodsTime2Selected);
                object.addProperty("foodTime3",FoodsTime3Selected);
                object.addProperty("waterTime1",WaterTime1Selected);
                object.addProperty("waterTime2",WaterTime2Selected);
                object.addProperty("waterTime3",WaterTime3Selected);


                api.UpdateStation(idStation,object,"Bearer "+token).enqueue(new Callback<Station>() {
                    @Override
                    public void onResponse(Call<Station> call, Response<Station> response) {
                        if(response.isSuccessful()){
                            Station station = response.body();

                            SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                            assert station != null;
                            editor.putString("foodQuantity",station.getFoodQuantity());
                            editor.putString("waterQuantity",station.getWaterQuantity());
                            editor.putString("foodTime1",station.getFoodTime1());
                            editor.putString("foodTime2",station.getFoodTime2());
                            editor.putString("foodTime3",station.getFoodTime3());
                            editor.putString("waterTime1",station.getWaterTime1());
                            editor.putString("waterTime2",station.getWaterTime2());
                            editor.putString("waterTime3",station.getWaterTime3());
                            editor.apply();

                            Log.d("updatedStation", "onResponse: "+station);
                            Intent intent = new Intent(StationSettings.this,Home.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }else {
                            Toast.makeText(StationSettings.this, "Error on response", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Station> call, Throwable t) {
                        Toast.makeText(StationSettings.this, "Error", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });
            }
        });





    }

    public void ConfigureSpinner(String Ft1,String Ft2,String Ft3,String Wt1,String Wt2,String Wt3){
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();


        initSpinner(categories);


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        int FspinnePos1 = dataAdapter.getPosition(Ft1);
        int FspinnePos2 = dataAdapter.getPosition(Ft2);
        int FspinnePos3 = dataAdapter.getPosition(Ft3);
        int WspinnePos1 = dataAdapter.getPosition(Wt1);
        int WspinnePos2 = dataAdapter.getPosition(Wt2);
        int WspinnePos3 = dataAdapter.getPosition(Wt3);
        Log.d("posspinner", "ConfigureSpinner: "+FspinnePos1+" "+FspinnePos2+" "+FspinnePos3);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        FoodsTime1.setAdapter(dataAdapter);
        FoodsTime1.setSelection(FspinnePos1);



        FoodsTime2.setAdapter(dataAdapter);
        FoodsTime2.setSelection(FspinnePos2);



        FoodsTime3.setAdapter(dataAdapter);
        FoodsTime3.setSelection(FspinnePos3);



        WaterTime1.setAdapter(dataAdapter);
        WaterTime1.setSelection(WspinnePos1);



        WaterTime2.setAdapter(dataAdapter);
        WaterTime2.setSelection(WspinnePos2);



        WaterTime3.setAdapter(dataAdapter);
        WaterTime3.setSelection(WspinnePos3);

    }


    public void initSpinner(List<String> categories){
        categories.add("1h");
        categories.add("2h");
        categories.add("3h");
        categories.add("4h");
        categories.add("5h");
        categories.add("6h");
        categories.add("7h");
        categories.add("8h");
        categories.add("9h");
        categories.add("10h");
        categories.add("11h");
        categories.add("12h");
        categories.add("13h");
        categories.add("14h");
        categories.add("15h");
        categories.add("16h");
        categories.add("17h");
        categories.add("18h");
        categories.add("19h");
        categories.add("20h");
        categories.add("21h");
        categories.add("22h");
        categories.add("23h");
    }
}
