package com.example.abhi.firebell;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                Log.d("made it","correct");
//                                Intent myIntent = new Intent(Dashboard.this, Dashboard.class);
//                                startActivity(myIntent);

                            case R.id.action_dummy:

                            case R.id.action_settings:
                                Log.d("made it","correct");
//                                Intent secondIntent = new Intent(Dashboard.this, Settings.class);
//                                startActivity(secondIntent);


                        }
                        return true;
                    }
                });

    }

}
