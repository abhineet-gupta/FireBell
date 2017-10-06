package com.example.abhi.firebell;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_map:
                    Intent mapIntent = new Intent(Settings.this, Map.class);
                    startActivity(mapIntent);
                    finish();
                    break;
                case R.id.action_home:
                    Intent homeIntent = new Intent(Settings.this, Dashboard.class);
                    startActivity(homeIntent);
                    finish();
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

        // Get widgets from layout file
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.content);
        mTextMessage = (TextView) findViewById(R.id.textViewSettAddrDescr);
        displayAddress();

        Switch switch_PushNotif = (Switch) findViewById(R.id.switchSettPushNotf);
        Switch switch_PhoneAlarm = (Switch) findViewById(R.id.switchSettPhoneAlarm);

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


        // get shared preferences file for the switches in settings page.
        Context context = getApplicationContext();
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.pref_file_key), Context.MODE_PRIVATE);

        //set notif switch listener and update to storage
        switch_PushNotif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // If the switch button is on, test...
                    // rl.setBackgroundColor(Color.parseColor("#FF80DFB0"));

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.pref_notifications_key), true);
                    editor.commit();

                    // Show the switch button checked Status as toast message
                    Toast.makeText(getApplicationContext(),
                            "Push Notifications is on", Toast.LENGTH_LONG).show();
                }
                else {
                    // If the switch button is off, test ...
                // rl.setBackgroundColor(Color.parseColor("#ed252f"));

                SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.pref_notifications_key), false);
        editor.commit();

        // Show the switch button checked Status as toast message
        Toast.makeText(getApplicationContext(),
                "Push Notifications off", Toast.LENGTH_LONG).show();
    }
}
        });

        //set alarms switch listener and update to storage
        switch_PhoneAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // If the switch button is on, test...
                    // rl.setBackgroundColor(Color.parseColor("#FF80DFB0"));

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.pref_alarm_key), true);
                    editor.commit();

                    // Show the switch button checked Status as toast message
                    Toast.makeText(getApplicationContext(),
                            "Alarms on", Toast.LENGTH_LONG).show();
                }
                else {
                    // If the switch button is off, test ...
                    // rl.setBackgroundColor(Color.parseColor("#ed252f"));

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.pref_alarm_key), false);
                    editor.commit();

                    // Show the switch button checked Status as toast message
                    Toast.makeText(getApplicationContext(),
                            "Alarm off", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayAddress();
    }
}