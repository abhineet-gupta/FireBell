package com.example.abhi.firebell;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        // Check stored address
        Context context = getApplicationContext();
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.pref_file_key), Context.MODE_PRIVATE);

//        //Hardcode address for testing; TODO remove hardcoded address when updated
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(getString(R.string.pref_address_key), getString(R.string.default_address));
//        editor.commit();

        final String addr_key = getString(R.string.pref_address_key);
        final String addr = sharedPref.getString(addr_key, "");

        // If address is emtpy, prompt user to set it
        if (addr.equals("")) {
            //final Intent mainIntent = new Intent(this, Dashboard.class);
            final Intent mainIntent = new Intent(this, SetAddress.class);
            startActivity(mainIntent);
            finish();
        }
        // otherwise go to Dashboard
        else{
            final Intent mainIntent = new Intent(this, Dashboard.class);
            //final Intent mainIntent = new Intent(this, SetAddress.class);
            startActivity(mainIntent);
            finish();
        }

        //setContentView(R.layout.activity_main);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 5000);
    }
}
