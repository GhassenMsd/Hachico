package com.example.ghassen.oscdog.Activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.ghassen.oscdog.CurvedBottomNavigationView;
import com.example.ghassen.oscdog.Fragments.AgendaFrag;
import com.example.ghassen.oscdog.Fragments.GpsFrag;
import com.example.ghassen.oscdog.Fragments.HomeFrag;
import com.example.ghassen.oscdog.Fragments.NotificationFrag;
import com.example.ghassen.oscdog.Fragments.StationFrag;
import com.example.ghassen.oscdog.R;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

public class Home extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Toolbar mTopToolbar;

    BottomNavigationView bottomNavigationView;
    VectorMasterView fab,fab1,fab2,fab3;
    PathModel outline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);



        bottomNavigationView =findViewById(R.id.bottom_nav);
        bottomNavigationView.setBackgroundColor(0xFECC3D);




        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //new MyAsyncTask().execute();
        getSupportFragmentManager().beginTransaction().add(R.id.frag_container,new StationFrag()).commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Intent intent = new Intent(Home.this,Profile.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment selectedFragment = null;


        switch (menuItem.getItemId())
        {
            case R.id.Station:
                //drawanimation(fab);
                selectedFragment = new StationFrag();
                break;

            case R.id.Agenda:


                //drawanimation(fab1);
                selectedFragment = new AgendaFrag();
                break;

            case R.id.Notification:

                //drawanimation(fab2);
                selectedFragment = new NotificationFrag();
                break;

            case R.id.Gps:

                //drawanimation(fab3);
                selectedFragment = new GpsFrag();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,selectedFragment).commit();
        return true;
    }


}
