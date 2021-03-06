package com.example.abhi.firebell;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Map extends FragmentActivity implements
        OnMyLocationButtonClickListener,
        OnMapReadyCallback {

    //retrieve api endpoint
    private String URL;
    int MY_LOCATION_REQUEST_CODE = 10;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        URL = getString(R.string.retrieveURL);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //retrieve bottom navigation
        BottomNavigationView navigation =
                findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    //Bottom navigation control
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                // map one is GOOD! it works, not mixed up
                case R.id.action_home:
                    Intent homeIntent = new Intent(Map.this, Dashboard.class);
                    startActivity(homeIntent);
                    finish();
                    break;
                case R.id.action_map:
                    break;
                case R.id.action_settings:
                    Intent settingsIntent = new Intent(Map.this, Settings.class);
                    startActivity(settingsIntent);
                    finish();
                    break;
            }
            return false;
        }

    };

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Get permission to display the users location data
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            //Request permission

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_LOCATION_REQUEST_CODE);

        }

        // Get stored address if it exists in storage

        Context context = getApplicationContext();
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.pref_file_key), Context.MODE_PRIVATE);
        String addr_key = getString(R.string.pref_address_key);
        final String addr = sharedPref.getString(addr_key, "");
        LatLng myLocation = getLocationFromAddress(this,addr);


        //Retreieve location data from server and store in alarms

        //instantiate alarms list from server
        List<Alarm> alarms = new ArrayList<Alarm>();

        //String to place our result in
        String result;

        //Instantiate new instance of our class
        SensorData getRequest = new SensorData();

        //Perform the doInBackground method, passing in our url
        try {
            //gets JSON data from server
            result = getRequest.execute(URL).get();
            //parse as JSONArray
            JSONArray alarmList = new JSONArray(result);

            for(int i = 0; i < alarmList.length(); i++){
                 //creates JSON object for each alarm
                JSONObject alarm = alarmList.getJSONObject(i);

                //set alarm variables
                int sensor_id = alarm.getInt("sensor_id");
                double latitude = alarm.getDouble("latitude");
                double longitude = alarm.getDouble("longitude");
                int level = alarm.getInt("level");
                int smoke = alarm.getInt("smoke");
                int carbonMonoxide = alarm.getInt("co");
                int temperature = alarm.getInt("temp");

                //stores an alarm object in a list
                alarms.add(new Alarm(sensor_id,latitude,longitude,smoke,level,temperature,carbonMonoxide));

            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        //Location data
        mMap.setOnMyLocationButtonClickListener(this);

        //Creates the icons for the list
        for(int i = 0; i < alarms.size(); i++){
            Alarm smokeAlarm = alarms.get(i);
            if(smokeAlarm.smoke == 1 ||
                    smokeAlarm.carbonMonoxide >= 5 ||
                    smokeAlarm.temperature >= 50)
            {
                LatLng fireAlarm = new LatLng(smokeAlarm.latitude, smokeAlarm.longitude);
                mMap.addMarker(new MarkerOptions()
                    .position(fireAlarm)
                    .title("ID: " + smokeAlarm.index +
                            " Temp: " + smokeAlarm.temperature +
                            " Level: " + smokeAlarm.level)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.alertcircle)));
            }
            else {
                LatLng fireAlarm = new LatLng(smokeAlarm.latitude, smokeAlarm.longitude);
                mMap.addMarker(new MarkerOptions()
                        .position(fireAlarm)
                        .title("ID: " + smokeAlarm.index +
                                " Temp: " + smokeAlarm.temperature +
                                " Level: " + smokeAlarm.level)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.normalcircle)));
            }
        }

        //sets google maps zoom and centres map on current position
        mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
    }

    // Turn address into Lat and Long
    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return p1;
    }

    //request permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    //create Alarm object
    class Alarm{
        int index;
        double longitude;
        double latitude;
        int smoke;
        int temperature;
        int carbonMonoxide;
        int level;

        Alarm(int index,double latitude,double longitude, int smoke, int level, int temperature, int carbonMonoxide){
            this.index = index;
            this.longitude = longitude;
            this.latitude = latitude;
            this.smoke = smoke;
            this.level = level;
            this.temperature =temperature;
            this.carbonMonoxide = carbonMonoxide;
        }
    }

    //request to server to get information about alarms
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