package com.example.abhi.firebell;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent firstIntent = new Intent(Settings.this, Dashboard.class);
                    Settings.this.startActivity(firstIntent);
                    Settings.this.finish();

//                    mTextMessage.setText(R.string.title_home);
//                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mTextMessage = (TextView) findViewById(R.id.textViewSettAddrDescr);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Get stored address if it exists in storage
        Context context = getApplicationContext();
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.pref_file_key), Context.MODE_PRIVATE);

        //Hardcode address for testing; TODO remove hardcoded address when updated
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_address_key), getString(R.string.default_address));
        editor.commit();

        String addr_key = getString(R.string.pref_address_key);
        final String addr = sharedPref.getString(addr_key, "");
        TextView tv_addr = (TextView) findViewById(R.id.textViewSettAddr);
        if (!addr.equals("")) {
            tv_addr.setText(addr);
        }

        //set button click listener & update address in storage
        Button btnUpdate = (Button) findViewById(R.id.btSettUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
            }
        });
    }
}