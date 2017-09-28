package com.example.abhi.firebell;

/**
 * Created by Wilkins on 18-Sep-17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        // Start home activity
        startActivity(new Intent(Splash.this, SetAddress.class));
        // close splash activity
        finish();
    }
}