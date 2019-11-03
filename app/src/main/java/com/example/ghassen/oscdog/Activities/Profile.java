package com.example.ghassen.oscdog.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.example.ghassen.oscdog.Fragments.AgendaFrag;
import com.example.ghassen.oscdog.Fragments.GpsFrag;
import com.example.ghassen.oscdog.Fragments.NotificationFrag;
import com.example.ghassen.oscdog.Fragments.StationFrag;
import com.example.ghassen.oscdog.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    ListView listView ;
    CircleImageView imageDog;
    TextView name;

    Button back;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        imageDog = findViewById(R.id.Pic);
        progressBar = findViewById(R.id.homeprogress);
        name = findViewById(R.id.textView4);
        back = findViewById(R.id.button5);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,Home.class);
                startActivity(intent);            }
        });

        listView = (ListView) findViewById(R.id.list);
        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        final String idUser = prefs.getString("idUser", "No id defined");
        final String token = prefs.getString("token","no token defined");
        final String firstName = prefs.getString("firstName", "No name defined");

        name.setText(firstName);


        Picasso.get().load("https://oprex-backend.herokuapp.com/"+idUser+"/image").into(imageDog, new Callback() {
            @Override
            public void onSuccess() {

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        String[] values = new String[] { "Account",
                "Setting",
                "Dog's profile",
                "About Us",
                "Log Out"
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.item_profile, android.R.id.text1, values);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;



                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);


                switch (itemValue){
                    case "Account":
                        Intent intent2 = new Intent(Profile.this,ProfileUser.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case "Setting":
                        Intent intent3 = new Intent(Profile.this,Setting.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case "Dog's profile":
                        Intent intent = new Intent(Profile.this,ProfileDog.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case "About Us":

                        break;
                    case "Log Out":
                        Intent intent1 = new Intent(Profile.this,Login.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                }



            }

        });


    }
}
