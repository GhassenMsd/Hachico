package com.example.ghassen.oscdog.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ghassen.oscdog.Entities.Collier;
import com.example.ghassen.oscdog.Entities.Dog;
import com.example.ghassen.oscdog.Entities.SignInResponse;
import com.example.ghassen.oscdog.Entities.Station;
import com.example.ghassen.oscdog.Entities.User;
import com.example.ghassen.oscdog.R;
import com.example.ghassen.oscdog.retro.RetrofitInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Login extends AppCompatActivity {

    Button SignUp;
    RetrofitInterface api;
    Button SignIn;
    String path,idStation,idCollar;
    EditText Username,Password;
    String id;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Gson gson = new GsonBuilder().setLenient().create();
        progressDialog = new ProgressDialog(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        //creating the api interface
        api = retrofit.create(RetrofitInterface.class);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SignIn = findViewById(R.id.button);
        SignUp = findViewById(R.id.SignUp);
        Username = findViewById(R.id.UserName);
        Password = findViewById(R.id.Password);

        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        final String token = prefs.getString("token", "No token defined");
        Log.d("token", "onCreate: "+token);









        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SignUp = new Intent(Login.this, com.example.ghassen.oscdog.Activities.SignUp.class);
                startActivity(SignUp);
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Please wait");
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();




                Log.d("variables", "onClick: "+Username.getText().toString()+" "+Password.getText().toString());


                JsonObject object = new JsonObject();
                object.addProperty("email",Username.getText().toString());
                object.addProperty("password",Password.getText().toString());


                api.login(object).enqueue(new Callback<SignInResponse>() {
                    @Override
                    public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {

                        if(response.isSuccessful())
                        {
                            Log.d("aman: ", "onResponse: "+response.body().getToken()+"response: "+response.errorBody());

                            okhttp3.Headers headers= response.headers();



                            SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                            editor.putString("idUser",response.headers().get("userid"));
                            editor.putString("token", response.body().getToken());
                            editor.apply();

                            GetDogByUser();
                            GetUser();

                            progressDialog.dismiss();

                            Intent accueil = new Intent(Login.this, Home.class);
                            startActivity(accueil);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Please verify your credentials", Toast.LENGTH_LONG).show();
                        }
                    }



                    @Override
                    public void onFailure(Call<SignInResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("SignInActivity", "onFailure: " + t.getMessage());
                        progressDialog.dismiss();
                    }
                });


            }
        });


    }

    private void GetStationByDog() {
        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        final String token = prefs.getString("token", "No token defined");
        final String idDog = prefs.getString("idDog", "No idDog defined");

        api.GetStationById(idDog,"Bearer "+token).enqueue(new Callback<Station>() {
            @Override
            public void onResponse(Call<Station> call, Response<Station> response) {
                if(response.isSuccessful()){
                    Station station = response.body();
                    Log.d("Station", "onResponse: "+station);
                    path = station.get_links().getSelf().getHref();
                    String[] separated = path.split("/stations/");
                    idStation= separated[1];

                    SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                    editor.putString("idStation",idStation);
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

                }else {
                    Toast.makeText(Login.this, "No station", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Station> call, Throwable t) {

                Toast.makeText(Login.this, "Error Station", Toast.LENGTH_SHORT).show();            }
        });



    }


    private void GetCollarByDog() {
        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        final String token = prefs.getString("token", "No token defined");
        final String idDog = prefs.getString("idDog", "No idDog defined");


        api.GetCollarById(idDog,"Bearer "+token).enqueue(new Callback<Collier>() {
            @Override
            public void onResponse(Call<Collier> call, Response<Collier> response) {
                if(response.isSuccessful()){
                    Collier collier = response.body();
                    String path1 = collier.getLinks().getSelf().getHref();
                    String[] separated = path1.split("/collars/");
                    idCollar= separated[1];
                    Log.d("idCollar", "onResponse: "+idCollar);
                    SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                    editor.putString("idCollar",idCollar);
                    editor.apply();
                }else {
                    Toast.makeText(Login.this, "No collar", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Collier> call, Throwable t) {
                Toast.makeText(Login.this, "error getting coller", Toast.LENGTH_SHORT).show();
            }
        });


    }



    public void GetDogByUser(){
        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        final String token = prefs.getString("token", "No token defined");
        final String idUser = prefs.getString("idUser", "No idUser defined");
        Log.d("iduserfrom", "GetDogByUser: "+token+" "+idUser);

        api.GetDogByUser(idUser,"Bearer "+token).enqueue(new Callback<List<Dog>>() {
            @Override
            public void onResponse(Call<List<Dog>> call, Response<List<Dog>> response) {
                Dog dog = response.body().get(0);
                SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();

                editor.putString("idDog",dog.getId());
                editor.putString("NameDog",dog.getName());
                editor.putString("Race",dog.getRace());
                editor.putString("gender",dog.getGender());
                editor.putString("birthDate",dog.getBirthDate());
                editor.apply();

                GetStationByDog();
                GetCollarByDog();

                SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
                Log.d("dogfromlogin", "GetDogByUser: "+dog.getName());
                Log.d("dogfromlogin", "GetDogByUser: "+prefs.getString("NameDog", "No idUser defined"));

            }

            @Override
            public void onFailure(Call<List<Dog>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT);
            }
        });
    }

    public void GetUser(){
        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        final String token = prefs.getString("token", "No token defined");
        final String idUser = prefs.getString("idUser", "No idUser defined");
        Log.d("iduserfromGetUser", "GetDogByUser: "+token+" "+idUser);

        api.GetUserById(idUser,"Bearer "+token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();

                editor.putString("firstName",user.getFirstname());
                editor.putString("lastName",user.getLastname());
                editor.putString("email",user.getEmail());
                editor.putString("password",user.getPassword());
                editor.putString("phoneNumber",user.getPhoneNumber());
                editor.apply();
                Log.d("userfromgetUser", "onResponse: "+user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"error getUser",Toast.LENGTH_SHORT).show();
            }
        });

    }


}
