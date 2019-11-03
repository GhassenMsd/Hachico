package com.example.ghassen.oscdog.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghassen.oscdog.Activities.DogDetails;
import com.example.ghassen.oscdog.R;
import com.example.ghassen.oscdog.retro.RetrofitInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class HomeFrag extends Fragment {
    CircleMenu circleMenu;
    RetrofitInterface api;
    CircleImageView imageDog;
    TextView nameDog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        //creating the api interface
        api = retrofit.create(RetrofitInterface.class);



        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("User", MODE_PRIVATE);
        final String idDog = prefs.getString("idDog", "No id defined");
        final String NameDog = prefs.getString("NameDog", "No name defined");
        final String token = prefs.getString("token", "No token defined");


        circleMenu = (CircleMenu) view.findViewById(R.id.circle_menu);
        imageDog = (CircleImageView) view.findViewById(R.id.Pic);
        nameDog = view.findViewById(R.id.textView15);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.mipmap.icon_menu, R.mipmap.icon_cancel);
        circleMenu.addSubMenu(Color.parseColor("#258CFF"), R.drawable.ic_more_horiz_black_24dp)
                .addSubMenu(Color.parseColor("#30A400"), R.drawable.ic_update_black_24dp)
                .addSubMenu(Color.parseColor("#FF4B32"), R.drawable.ic_delete_black_24dp);



        circleMenu.setOnMenuSelectedListener(new OnMenuSelectedListener() {

             @Override
             public void onMenuSelected(int index) {
              switch (index) {
                  case 0:
                      Intent intent = new Intent(getActivity(),DogDetails.class);
                      startActivity(intent);
                      break;
                  case 1:
                      Toast.makeText(getContext(), "Search button Clicked", Toast.LENGTH_SHORT).show();
                      break;
                  case 2:
                      Toast.makeText(getContext(), "Notify button Clciked", Toast.LENGTH_SHORT).show();
                      break;
                  case 3:
                      Toast.makeText(getContext(), "Settings button Clcked", Toast.LENGTH_SHORT).show();
                      break;
                  case 4:
                      Toast.makeText(getContext(), "GPS button Clicked", Toast.LENGTH_SHORT).show();
                      break;
              }
          }
      }

        );

        circleMenu.setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

                                                     @Override
                                                     public void onMenuOpened() {

                                                     }

                                                     @Override
                                                     public void onMenuClosed() {

                                                     }
                                                 }
        );

        Picasso.get().load("https://oprex-backend.herokuapp.com/"+idDog+"/image").into(imageDog);
        nameDog.setText(NameDog);





        return view;
    }


}
