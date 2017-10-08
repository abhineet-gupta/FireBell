package com.example.abhi.firebell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class Dashboard extends AppCompatActivity {

    private String NEWS_URL = "http://www.mfb.vic.gov.au/News.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

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

        Button btnEmergencies = findViewById(R.id.buttonEmergencies);
        btnEmergencies.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent emergenciesIntent = new Intent(Dashboard.this, Emergencies.class);
                startActivity(emergenciesIntent);
            }
        });

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

        final Button callEmergencyBtn = findViewById(R.id.buttonEmergency);
        callEmergencyBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent callEmergencyInt = new Intent(Intent.ACTION_DIAL);
                callEmergencyInt.setData(Uri.parse("tel:000"));
                startActivity(callEmergencyInt);
            }
        });
    }
}
