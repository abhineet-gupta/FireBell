package com.example.abhi.firebell;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Status extends AppCompatActivity {
    private String URL;
    private Integer INFO_RADIUS_METRES = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        URL = getString(R.string.retrieveURL);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

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

        TableLayout table = findViewById(R.id.sensor_data_table1);
        //Retrieve sensor data
        //String to place our result in
        String result;
            //Instantiate new instance of our class
            SensorData getRequest = new SensorData();
            //Perform the doInBackground method, passing in our url
            try {
                result = getRequest.execute(URL).get();
                JSONArray result_arr = new JSONArray(result);
                Log.d("JSON Result: ", result_arr.toString());

                // Add row headers
                table.removeAllViewsInLayout();
                TableRow tr_head = new TableRow(this);
                tr_head.setBackgroundColor(Color.GRAY);
                tr_head.setLayoutParams(new AppBarLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));

                //Add data to the header row
                TextView label_sensorID = new TextView(this);
                label_sensorID.setText("Dev ID");
                label_sensorID.setTextColor(Color.WHITE);
                label_sensorID.setTextSize(16);
                label_sensorID.setPadding(20, 5, 5, 5);
                tr_head.addView(label_sensorID);

                TextView label_temp = new TextView(this);
                label_temp.setText("Temp (C)");
                label_temp.setTextColor(Color.WHITE);
                label_temp.setTextSize(16);
                label_temp.setPadding(20, 5, 5, 5);
                tr_head.addView(label_temp);

                TextView label_co = new TextView(this);
                label_co.setText("CO (ppm)");
                label_co.setTextColor(Color.WHITE);
                label_co.setTextSize(16);
                label_co.setPadding(20, 5, 5, 5);
                tr_head.addView(label_co);

                TextView label_smoke = new TextView(this);
                label_smoke.setText("Smoke?");
                label_smoke.setTextColor(Color.WHITE);
                label_smoke.setTextSize(16);
                label_smoke.setPadding(20, 5, 5, 5);
                tr_head.addView(label_smoke);

                table.addView(tr_head);

                Context context = getApplicationContext();
                final SharedPreferences sharedPref = context.getSharedPreferences(
                        getString(R.string.pref_file_key), Context.MODE_PRIVATE);
                String addr_key = getString(R.string.pref_address_key);
                final String addr = sharedPref.getString(addr_key, "");
                LatLng myLocation = Map.getLocationFromAddress(this,addr);
                Location loc = new Location("");

                loc.setLatitude(myLocation.latitude);
                loc.setLongitude(myLocation.longitude);

                for (int i = 0; i < result_arr.length(); i++){
                    JSONObject rowJ = result_arr.getJSONObject(i);

                    Double s_lat = Double.parseDouble(rowJ.getString("latitude"));
                    Double s_long = Double.parseDouble(rowJ.getString("longitude"));
                    Location s_loc = new Location("");
                    s_loc.setLongitude(s_long);
                    s_loc.setLatitude(s_lat);

                    Float dist = s_loc.distanceTo(loc);
                    Log.d("Distance: ", dist.toString());

                    if (dist <= INFO_RADIUS_METRES){
                        TableRow row = new TableRow(this);
                        row.setBackgroundColor(Color.WHITE);
                        row.setLayoutParams(new AppBarLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));

                        TextView devID = new TextView(this);
                        devID.setText(rowJ.getString("sensor_id"));
                        devID.setTextColor(Color.BLACK);
                        devID.setTextSize(16);
                        devID.setPadding(20, 5, 5, 5);
                        row.addView(devID);

                        TextView temp = new TextView(this);
                        Double temperature = rowJ.getDouble("temp");
                        if (temperature > 50){
                            temp.setTextColor(Color.RED);
                            row.setBackgroundColor(Color.LTGRAY);
                        } else {
                            temp.setTextColor(Color.BLACK);
                        }
                        temp.setText(temperature.toString());
                        temp.setTextSize(16);
                        temp.setPadding(20, 5, 5, 5);
                        row.addView(temp);

                        TextView co = new TextView(this);
                        Integer coxide = rowJ.getInt("co");
                        if (coxide >= 5){
                            co.setTextColor(Color.RED);
                            row.setBackgroundColor(Color.LTGRAY);
                        } else {
                            co.setTextColor(Color.BLACK);
                        }
                        co.setText(coxide.toString());
                        co.setTextSize(16);
                        co.setPadding(20, 5, 5, 5);
                        row.addView(co);

                        TextView sm = new TextView(this);
                        Integer smoke = rowJ.getInt("smoke");
                        if (smoke == 1) {
                            sm.setText("Yes!");
                            sm.setTextColor(Color.RED);
                            row.setBackgroundColor(Color.LTGRAY);
                        }
                        else {
                            sm.setText("No");
                            sm.setTextColor(Color.BLACK);
                        }

                        sm.setTextSize(16);
                        sm.setPadding(20, 5, 5, 5);
                        row.addView(sm);

                        table.addView(row);
                    }
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }
    }

    public class SensorData extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params){
            String stringUrl = params[0];
            String result;
            String inputLine;

            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);

                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();

                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());

                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();

                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }
}
