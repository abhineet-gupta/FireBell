package com.example.abhi.firebell;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class Status extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                Intent homeIntent = new Intent(Status.this, Dashboard.class);
                                startActivity(homeIntent);
                                finish();
                                break;
                            case R.id.action_settings:
                                Intent settingsIntent = new Intent(Status.this, Settings.class);
                                startActivity(settingsIntent);
                                finish();
                                break;
                            case R.id.action_map:
                                Intent mapIntent = new Intent(Status.this, Map.class);
                                startActivity(mapIntent);
                                finish();
                                break;
                        }
                        return true;
                    }
                });
    }
}
