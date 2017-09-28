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
                case R.id.action_home:
                    Intent firstIntent = new Intent(Settings.this, Dashboard.class);
                    startActivity(firstIntent);
                    finish();
                case R.id.action_dummy:
                    break;
                case R.id.action_settings:
                    break;
            }
            return false;
        }

    };

    private void displayAddress(){
        // Get stored address if it exists in storage
        Context context = getApplicationContext();
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.pref_file_key), Context.MODE_PRIVATE);

//        //Hardcode address for testing; TODO remove hardcoded address when updated
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(getString(R.string.pref_address_key), getString(R.string.default_address));
//        editor.commit();

        String addr_key = getString(R.string.pref_address_key);
        final String addr = sharedPref.getString(addr_key, "");
        TextView tv_addr = (TextView) findViewById(R.id.textViewSettAddr);
        if (!addr.equals("")) {
            tv_addr.setText(addr);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mTextMessage = (TextView) findViewById(R.id.textViewSettAddrDescr);
        displayAddress();

        BottomNavigationView navigation =
                (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //set button click listener & update address in storage
        Button btnUpdate = (Button) findViewById(R.id.btSettUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, SetAddress.class);
                startActivity(intent);
            }
        });

        //SharedPreferences sharedPreferences = getSharedPreferences(fileNameString, MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayAddress();
    }
}