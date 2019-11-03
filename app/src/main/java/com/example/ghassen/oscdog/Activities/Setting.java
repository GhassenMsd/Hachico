package com.example.ghassen.oscdog.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.ghassen.oscdog.R;

public class Setting extends AppCompatActivity {
    ListView listView ;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        back = findViewById(R.id.button5);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Setting.this,Profile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        listView = (ListView) findViewById(R.id.list);


        String[] values = new String[] { "Code Collar",
                "Code Station",
                "Setting"
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
                    case "Code Collar":

                        break;
                    case "Code Station":

                        break;
                    case "Setting":
                        Intent intent = new Intent(Setting.this,StationSettings.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                }



            }

        });

    }
}
