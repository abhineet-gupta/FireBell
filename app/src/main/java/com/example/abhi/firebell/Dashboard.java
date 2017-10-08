package com.example.abhi.firebell;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {

    private String NEWS_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        NEWS_URL = getString(R.string.news_URL);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Populate bottom navigation bar
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
        Button btnStatus = findViewById(R.id.buttonStatus);
        btnStatus.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent statusIntent = new Intent(Dashboard.this, Status.class);
                startActivity(statusIntent);
            }
        });

        // Visit news website when clicked
        Button fireNewsBtn = findViewById(R.id.fireNews);
        fireNewsBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent newsInt = new Intent();
                newsInt.setAction(Intent.ACTION_VIEW);
                newsInt.addCategory(Intent.CATEGORY_BROWSABLE);
                newsInt.setData(Uri.parse(NEWS_URL));
                startActivity(newsInt);
            }
        });

        // Launch dialer with the emergency number populated
        final Button callEmergencyBtn = findViewById(R.id.buttonEmergency);
        callEmergencyBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent callEmergencyInt = new Intent(Intent.ACTION_DIAL);
                callEmergencyInt.setData(Uri.parse(getString(R.string.emergency_tel_link)));
                startActivity(callEmergencyInt);
            }
        });
    }
}
