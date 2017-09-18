package com.example.abhi.firebell;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        final Intent mainIntent = new Intent(MainActivity.this, Dashboard.class);
        MainActivity.this.startActivity(mainIntent);
        MainActivity.this.finish();

        //setContentView(R.layout.activity_main);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 5000);
    }
}
