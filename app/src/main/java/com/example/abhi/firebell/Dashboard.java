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
import android.widget.Button;

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
                                break;
                            case R.id.action_settings:
                                Intent settingsIntent = new Intent(Dashboard.this, Settings.class);
                                startActivity(settingsIntent);
                                finish();
                                break;
                            case R.id.action_map:
                                Intent mapIntent = new Intent(Dashboard.this, Map.class);
                                startActivity(mapIntent);
                                finish();
                                break;
                        }
                        return true;
                    }
                });

        //set button click listener & update address in storages
        Button btnStatus = (Button) findViewById(R.id.buttonStatus);
        btnStatus.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent statusIntent = new Intent(Dashboard.this, Status.class);
                startActivity(statusIntent);
                finish();
            }
        });

        Button btnEmergencies = (Button) findViewById(R.id.buttonEmergencies);
        btnEmergencies.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent emergenciesIntent = new Intent(Dashboard.this, Emergencies.class);
                startActivity(emergenciesIntent);
                finish();
            }
        });
    }
}
