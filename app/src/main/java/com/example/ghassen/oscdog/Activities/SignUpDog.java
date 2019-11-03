package com.example.ghassen.oscdog.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghassen.oscdog.Entities.Dog;
import com.example.ghassen.oscdog.Entities.SignInResponse;
import com.example.ghassen.oscdog.Entities.SignUpResponse;
import com.example.ghassen.oscdog.R;
import com.example.ghassen.oscdog.retro.RetrofitInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class SignUpDog extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button SignDog;
    RetrofitInterface api;
    EditText Name,Race,gender,Size,BirthDate;
    TextView back;
    CircleImageView imagedog;
    String genderselected;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private File file;
    Uri uri;
    String idDog,path;
    private Context _context;
    Calendar myCalendar = Calendar.getInstance();

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {


            SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
            final String token = prefs.getString("token", "No token defined");
            final String idUser = prefs.getString("idUser", "No idUser defined");

            Log.d("token", "onCreate: "+token);


            final JsonObject object = new JsonObject();
            object.addProperty("birthDate",BirthDate.getText().toString());
            object.addProperty("gender", genderselected);
            object.addProperty("name",Name.getText().toString());
            object.addProperty("race",Race.getText().toString());
            object.addProperty("userId",idUser);

            Log.d("SignUpDog:", "onResponse: vars "+object.get("race")+object.get("gender"));
            Log.d("token:", "onResponse: token "+token);

            api.RegisterDog(object,"Bearer "+token).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    Log.d("SignUpDog:vars", "onResponse: vars "+response.body().getName()+" "+response.body().getRace()+" "+response.body().getBirthDate()+" "+response.body().getBirthDate());
//                    Log.e("TTTTTTTTTTTTTTTTTTT", "onResponse: "+response.body() );
//                    Log.d("SignUpDog:id", "onResponse: vars "+response.body().getLinks().getSelf().getHref());
                    okhttp3.Headers headers= response.headers();

                    path = response.headers().get("location");
                    String[] separated = path.split("dogs/dogs/");
                    idDog= separated[1];
                    SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                    editor.putString("idDog",idDog);
                    editor.putString("NameDog",Name.getText().toString());
                    editor.putString("Race",Race.getText().toString());
                    editor.putString("Size",Size.getText().toString());
                    editor.putString("gender",genderselected);
                    editor.putString("birthDate",BirthDate.getText().toString());


                    editor.apply();
                    uploadImage(idDog);
                    Log.d("iddog", "onResponse: "+idDog);
                    Intent Hard = new Intent(SignUpDog.this,HardRegister.class);
                    startActivity(Hard);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("TTAGGGGGGG", "onFailure: "+t );
                    Log.e("TTAGGGGGGG", call.request().body().toString() );

                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        protected Void doInBackground(Void... params) {





            return null;
        }

    }


    public void uploadImage(String id){
        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        final String token = prefs.getString("token", "No token defined");


        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(uri)),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        api.uploadImage(id, body, "Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("uploadDog", "onResponse: succes");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("upload", "onResponse: error");
                Log.e("upload", t.getMessage());

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_dog);







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

        SignDog = findViewById(R.id.button);
        Name = findViewById(R.id.Name);
        Race = findViewById(R.id.Race);
        Size = findViewById(R.id.Size);
        BirthDate = findViewById(R.id.BirthDate);
        imagedog = findViewById(R.id.Pic);

        back = findViewById(R.id.Logo);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpDog.this,SignUp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        SignDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputs()){
                    new MyAsyncTask().execute();
                }
            }
        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                BirthDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        BirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUpDog.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        imagedog.setOnClickListener(new View.OnClickListener() {
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


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imagedog.setImageURI(data.getData());
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




    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    private boolean validateInputs() {
        if (Name.getText().toString().equals("")) {
            Name.setError("required");
            Name.requestFocus();
            return false;
        }
        if (Race.getText().toString().equals("")) {
            Race.setError("required");
            Race.requestFocus();
            return false;
        }
        if (Size.getText().toString().equals("")) {
            Size.setError("required");
            Size.requestFocus();
            return false;
        }
        if (BirthDate.getText().toString().equals("")) {
            BirthDate.setError("required");
            BirthDate.requestFocus();
            return false;
        }

        return true;

    }
}
