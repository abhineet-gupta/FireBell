package com.example.abhi.firebell;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Status extends AppCompatActivity {
    private String URL = "http://13.72.243.229/retrieve.php?sid=1&limit=2";

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

            JSONObject res1 = result_arr.getJSONObject(0);
            Log.d("JSON 1: ", res1.toString());

            TextView smokeTV = (TextView) findViewById(R.id.smokeTV);
            if (res1.getInt(getString(R.string.SmokeJSONKey)) == 1){
                smokeTV.setText("Yes");
                smokeTV.setTextColor(getResources().getColor(R.color.warningRed));
            } else {
                smokeTV.setText("No");
                smokeTV.setTextColor(getResources().getColor(R.color.okayGreen));
            }

            TextView coTV = (TextView) findViewById(R.id.coTV);
            coTV.setText(Integer.toString(res1.getInt("co")));
            if (res1.getInt("co") > 4) {
                coTV.setTextColor(getResources().getColor(R.color.warningRed));
            } else {
                coTV.setTextColor(getResources().getColor(R.color.okayGreen));
            }

            TextView tempTV = (TextView) findViewById(R.id.tempTV);
            tempTV.setText(res1.getString("temp"));

            TextView alertTV = (TextView) findViewById(R.id.alertsTV);
            if (smokeTV.getText().equals("Yes") ||
                    Integer.parseInt(coTV.getText().toString()) > 4)
            {
                alertTV.setText("DANGER!");
                alertTV.setTextColor(getResources().getColor(R.color.warningRed));
            } else {
                alertTV.setText("Normal");
                alertTV.setTextColor(getResources().getColor(R.color.okayGreen));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
