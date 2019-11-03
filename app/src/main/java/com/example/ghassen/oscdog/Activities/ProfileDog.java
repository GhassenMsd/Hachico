package com.example.ghassen.oscdog.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ghassen.oscdog.Entities.Dog;
import com.example.ghassen.oscdog.R;
import com.example.ghassen.oscdog.retro.RetrofitInterface;
import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ProfileDog extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText name,race,size,BirthDate;
    CircleImageView imageDog;
    Button Save;
    RetrofitInterface api;
    String genderselected;
    ProgressBar progressBar;
    String idDog;
    ProgressDialog progressDialog;
    Button back;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private File file;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_dog);
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

        progressBar = findViewById(R.id.homeprogress);
        name = findViewById(R.id.Name);
        race = findViewById(R.id.Race);
        size = findViewById(R.id.Size);
        BirthDate = findViewById(R.id.BirthDate);
        imageDog = findViewById(R.id.Pic);
        Save = findViewById(R.id.button);
        back = findViewById(R.id.button5);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileDog.this,Profile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });


        final Spinner gender = (Spinner) findViewById(R.id.Gender);

        // Spinner click listener
        gender.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("MALE");
        categories.add("FEMALE");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        gender.setAdapter(dataAdapter);

        genderselected = gender.getSelectedItem().toString();



        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        final String token = prefs.getString("token","no token defined");
        idDog = prefs.getString("idDog", "No id defined");
        final String NameDog = prefs.getString("NameDog", "No name defined");
        final String Race = prefs.getString("Race", "No Race defined");
        final String Size = prefs.getString("Size", "No Size defined");
        final String birthDate = prefs.getString("birthDate", "No birthDate defined");


        name.setText(NameDog);
        race.setText(Race);
        size.setText(Size);
        BirthDate.setText(birthDate);
        Log.d("iddogggfromgal", "onClick: "+idDog);
        Picasso.get().load("https://oprex-backend.herokuapp.com/"+idDog+"/image").memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(imageDog, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        imageDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }

                } else {
                    pickImageFromGallery();
                }
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Updating profile");
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                final JsonObject object = new JsonObject();
                object.addProperty("birthDate",BirthDate.getText().toString());
                object.addProperty("gender", genderselected);
                object.addProperty("name",name.getText().toString());
                object.addProperty("race",race.getText().toString());
                Log.d("iddoggg", "onClick: "+idDog);


                api.UpdateDog(idDog,object,"Bearer "+token,"application/json","no-cache").enqueue(new Callback<Dog>() {
                    @Override
                    public void onResponse(Call<Dog> call, Response<Dog> response) {
                        if (response.isSuccessful()) {
                            api.GetDogById(idDog,"Bearer "+token).enqueue(new Callback<Dog>() {
                                @Override
                                public void onResponse(Call<Dog> call, Response<Dog> response) {
                                    Dog dog = response.body();
                                    Log.d("DogUpdate", "onResponse: " + dog);
                                    SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                                    editor.putString("NameDog",dog.getName());
                                    editor.putString("Race",dog.getRace());
                                    editor.putString("birthDate",dog.getBirthDate());
                                    editor.apply();
                                    uploadImage(idDog);
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(ProfileDog.this,Profile.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                }

                                @Override
                                public void onFailure(Call<Dog> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "error get Dog", Toast.LENGTH_SHORT).show();

                                }
                            });

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Dog> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageDog.setImageURI(data.getData());
            uri = data.getData();
            file = new File(getPath(uri));
        }
    }


    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }
    public void uploadImage(String id){
        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        final String token = prefs.getString("token", "No token defined");


        if(uri != null){
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse(getContentResolver().getType(uri)),
                            file
                    );
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            api.uploadImage(id, body, "Bearer " + token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("uploadUser", "onResponse: succes");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("upload", "onResponse: error");
                    Log.e("upload", t.getMessage());

                }
            });
        }else {
            Log.d("testImage", "uploadImage: No image");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
