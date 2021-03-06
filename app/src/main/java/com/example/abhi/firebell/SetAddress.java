package com.example.abhi.firebell;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SetAddress extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_address);

    }

    public void setAddress(View view){
        // Get address typed by user
        EditText et_addr = findViewById(R.id.editTextSetAddress);
        String addr = et_addr.getText().toString();

        // Write to saved data
        Context context = getApplicationContext();
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.pref_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_address_key), addr);
        editor.commit();

        final Intent mainIntent = new Intent(this, Dashboard.class);
        startActivity(mainIntent);
        finish();
    }
}
