package com.example.ghassen.oscdog.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghassen.oscdog.Activities.SignUpDog;
import com.example.ghassen.oscdog.Adapters.EventAdapter;
import com.example.ghassen.oscdog.Entities.Embedded;
import com.example.ghassen.oscdog.Entities.EventsResponse;
import com.example.ghassen.oscdog.R;
import com.example.ghassen.oscdog.retro.RetrofitInterface;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AgendaFrag extends Fragment {
    RetrofitInterface api;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    RecyclerView recyclerView;
    Button AddEvent;
    CalendarView calendarView;
    String selectedDate;
    EditText title,description,date;
    Calendar myCalendar = Calendar.getInstance();
    TextView noEvents;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_agenda, container, false);
        SharedPreferences prefs = getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        final String token = prefs.getString("token", "No token defined");

        calendarView = view.findViewById(R.id.calendar);
        recyclerView = view.findViewById(R.id.recyclerView);
        AddEvent = view.findViewById(R.id.AddEvent);
        noEvents = view.findViewById(R.id.noEvent);
        noEvents.setText("No event in this date");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        //creating the api interface
        api = retrofit.create(RetrofitInterface.class);

        api.GetEvents("Bearer "+token).enqueue(new Callback<EventsResponse>() {
            @Override
            public void onResponse(Call<EventsResponse> call, Response<EventsResponse> response) {
                Embedded embedded = response.body().getEmbedded();
                List<Calendar> events = new ArrayList<>();
//                for(int i=0;i<response.body().getEmbedded().getEvents().size();i++){
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                    try {
//                        Date date = sdf.parse(response.body().getEmbedded().getEvents().get(i).getDate());
//                        Calendar cal = Calendar.getInstance();
//                        cal.setTime(date);
//                        events.add(cal);
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }

                Log.d("ListDates", "onResponse: "+events.toString());

                Log.d("events", "onResponse: "+embedded);
            }

            @Override
            public void onFailure(Call<EventsResponse> call, Throwable t) {
                Toast.makeText(getContext(),"No Events",Toast.LENGTH_SHORT).show();

            }
        });



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                int month1 = month+1;

                String testMonth = String.valueOf(month1);
                String testDay = String.valueOf(dayOfMonth);

                Log.d("month", "onSelectedDayChange: "+testMonth);
                if(month1 <10){
                    testMonth = "0"+testMonth;
                    Log.d("month", "onSelectedDayChange: "+testMonth);
                }
                if(dayOfMonth <10){
                    testDay = "0"+testDay;
                    Log.d("month", "onSelectedDayChange: "+testDay);
                }

                selectedDate=year+"-"+ testMonth +"-"+testDay;
                //Toast.makeText(getContext(),selectedDate,Toast.LENGTH_SHORT).show();

                api.GetEventsDate(selectedDate,"Bearer eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6InN3YWdnZXIifQ.65xDHqTg0QY_KmWF2d_n89b7cdotHc0szpRN3H3Buytg9IGQ3W7LFqSOsMqBelKThdA8LldHzFFZukc4H0AOeA").enqueue(new Callback<EventsResponse>() {
                    @Override
                    public void onResponse(Call<EventsResponse> call, Response<EventsResponse> response) {
                        if(response.isSuccessful()){
                            //Toast.makeText(getContext(),"succes",Toast.LENGTH_SHORT).show();

                            noEvents.setVisibility(View.INVISIBLE);
                            Embedded embedded = response.body().getEmbedded();
                            EventAdapter adapter = new EventAdapter(getActivity(),embedded.getEvents());
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(adapter);
                            if(embedded.getEvents().size() == 0){
                                noEvents.setVisibility(View.VISIBLE);
                            }
                        }else{
                            Toast.makeText(getContext(),"No Events on this date",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EventsResponse> call, Throwable t) {
                        Toast.makeText(getContext(),"fail",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        final DatePickerDialog.OnDateSetListener datee = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                date.setText(sdf.format(myCalendar.getTime()));
            }

        };





        AddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    View layoutView = getLayoutInflater().inflate(R.layout.dialog_add_event, null);


                    title = layoutView.findViewById(R.id.Title);
                    description = layoutView.findViewById(R.id.Description);
                    date = layoutView.findViewById(R.id.Date);
                    Button dialogButton = layoutView.findViewById(R.id.Save);
                    dialogBuilder.setView(layoutView);
                    alertDialog = dialogBuilder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                    alertDialog.show();

                    
                    date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(getContext(), datee, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });


                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            JsonObject object = new JsonObject();
                            object.addProperty("title",title.getText().toString());
                            object.addProperty("description",description.getText().toString());
                            object.addProperty("dateString",date.getText().toString());

                            api.AddEvent(object,"Bearer "+token).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Toast.makeText(getActivity(),"succes",Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(getActivity(),"fail",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                }
        });


        // Inflate the layout for this fragment
        return view;
    }


}
