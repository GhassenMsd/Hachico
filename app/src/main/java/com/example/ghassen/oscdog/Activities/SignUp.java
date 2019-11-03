package com.example.ghassen.oscdog.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghassen.oscdog.Entities.SignInResponse;
import com.example.ghassen.oscdog.Entities.SignUpResponse;
import com.example.ghassen.oscdog.Entities.User;
import com.example.ghassen.oscdog.R;
import com.example.ghassen.oscdog.retro.RetrofitInterface;
import com.google.gson.JsonObject;

import java.io.File;
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
import retrofit2.http.Headers;

public class SignUp extends AppCompatActivity {

    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    Button SignUp;
    TextView welcomeText, back;
    CircleImageView imageProfil;
    RetrofitInterface api;
    EditText FirstName, LastName, PhoneNum, Email, Password;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private File file;
    Uri uri;
    public String id;
    String masque = "^[a-zA-Z]+[a-zA-Z0-9\\._-]*[a-zA-Z0-9]@[a-zA-Z]+"
            + "[a-zA-Z0-9\\._-]*[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}$";

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            final JsonObject object = new JsonObject();
            object.addProperty("firstName", FirstName.getText().toString());
            object.addProperty("lastName", LastName.getText().toString());
            object.addProperty("phoneNumber", PhoneNum.getText().toString());
            object.addProperty("email", Email.getText().toString());
            object.addProperty("password", Password.getText().toString());

            Log.d("vars", "onCreate: " + FirstName.getText().toString() + " " + LastName.getText().toString() + " " +
                    PhoneNum.getText().toString() + " " + Email.getText().toString() + " " + Password.getText().toString());


            api.SignUp(object).enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {

                    if (response.isSuccessful() && response.body().getMessage().equals("success")) {

                        final JsonObject object1 = new JsonObject();
                        object1.addProperty("email", Email.getText().toString());
                        object1.addProperty("password", Password.getText().toString());


                        api.login(object1).enqueue(new Callback<SignInResponse>() {
                            @Override
                            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                                Toast.makeText(getApplicationContext(), "succes", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                                editor.putString("token", response.body().getToken());
                                editor.putString("idUser",response.headers().get("userid"));
                                editor.putString("nameee",FirstName.getText().toString());

                                editor.apply();

                                okhttp3.Headers headers= response.headers();

                                id = response.headers().get("userid");

                                Log.d("iddd", "onResponse: "+id);

                                UploadImage(id);
                                GetUser(id);

                                ShowDialog(R.layout.dialog_welcome);
                            }

                            @Override
                            public void onFailure(Call<SignInResponse> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "errorlogin", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {





            return null;
        }
    }

    public void UploadImage(String id){

        Log.d("id from upload", "UploadImage: "+id);
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
                Log.d("upload", "onResponse: succes");
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
        setContentView(R.layout.activity_sign_up);


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


        SignUp = findViewById(R.id.button);
        FirstName = findViewById(R.id.FirstName);
        LastName = findViewById(R.id.LastName);
        PhoneNum = findViewById(R.id.Phone);
        Email = findViewById(R.id.EmailS);
        Password = findViewById(R.id.Password);
        imageProfil = findViewById(R.id.Pic);

        back = findViewById(R.id.Logo);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    new MyAsyncTask().execute();
                }
            }
        });

        imageProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void GetUser(String id){
        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        final String token = prefs.getString("token", "No token defined");
        Log.d("iduserfromGetUser", "GetDogByUser: "+token+" "+id);

        api.GetUserById(id,"Bearer "+token).enqueue(new Callback<User>() {
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageProfil.setImageURI(data.getData());
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

    private void ShowDialog(int layout) {

        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        final String name = prefs.getString("nameee","no nameee defined");


        dialogBuilder = new AlertDialog.Builder(SignUp.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        welcomeText = layoutView.findViewById(R.id.welcomeText);
        Button dialogButton = layoutView.findViewById(R.id.c);
        welcomeText.setText("WELCOME "+name.toUpperCase()+"!");
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        alertDialog.show();

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                Intent signUpDog = new Intent(SignUp.this, SignUpDog.class);
                startActivity(signUpDog);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private boolean validateInputs() {
        Pattern pattern = Pattern.compile(masque);
        Matcher controler = pattern.matcher(Email.getText().toString());
        if (!controler.matches()){
            Email.setError("Invalid format");
            Email.requestFocus();
            return false;
        }
        if (FirstName.getText().toString().equals("")) {
            FirstName.setError("required");
            FirstName.requestFocus();
            return false;
        }
        if (LastName.getText().toString().equals("")) {
            LastName.setError("required");
            LastName.requestFocus();
            return false;
        }
        if (PhoneNum.getText().toString().equals("")) {
            PhoneNum.setError("required");
            PhoneNum.requestFocus();
            return false;
        }
        if(PhoneNum.length() != 8)
        {
            PhoneNum.setError("Invalid format");
            PhoneNum.requestFocus();
            return false;
        }
        if (Email.getText().toString().equals("")) {
            Email.setError("required");
            Email.requestFocus();
            return false;
        }
        if (Password.getText().toString().equals("")) {
            Password.setError("required");
            Password.requestFocus();
            return false;
        }
        if(Password.length() < 6)
        {
            Password.setError("Must contains at least 6");
            Password.requestFocus();
            return false;
        }
        return true;

    }

}
