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
import android.view.View;
import android.widget.ProgressBar;
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

//import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;

public class Map extends FragmentActivity implements
        OnMyLocationButtonClickListener,
//        OnMyLocationClickListener,
        OnMapReadyCallback {

    private String URL = "http://13.72.243.229/retrieve_loc.php";

    int MY_LOCATION_REQUEST_CODE = 10;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        BottomNavigationView navigation =
                findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Step 1: grant permission

        //Step 2: Get data


        //Step 3: display
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

//    @Override
//    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
//    }

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

        //Step 1: Get the permission data
        //Get permission to display the users location data
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }else{
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


//        Retreieve location data from server


        //instantiate alarms list from server
        List<Alarm> alarms = new ArrayList<Alarm>();

        //String to place our result in
        String result;

        //Instantiate new instance of our class
        SensorData getRequest = new SensorData();

        //Perform the doInBackground method, passing in our url
        try {
            result = getRequest.execute(URL).get();
            JSONArray alarmList = new JSONArray(result);
            System.out.println("Length: " + alarmList.length());
            for(int i = 0; i < alarmList.length(); i++){

                JSONObject alarm = alarmList.getJSONObject(i);
                int sensor_id = alarm.getInt("sensor_id");
                double latitude = alarm.getDouble("latitude");
                double longitude = alarm.getDouble("longitude");
                int level = alarm.getInt("level");
                int smoke = alarm.getInt("smoke");
                int carbonMonoxide = alarm.getInt("co");
                int temperature = alarm.getInt("temp");
                String time = (String) alarm.get("time");

                alarms.add(new Alarm(sensor_id,latitude,longitude,smoke,level,temperature,carbonMonoxide));

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Location data
        mMap.setOnMyLocationButtonClickListener(this);
//        mMap.setOnMyLocationClickListener(this);

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
                    .title("Temp: " + smokeAlarm.temperature + " Level: " + smokeAlarm.level)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.alertcircle)));
            }
            else {
                LatLng fireAlarm = new LatLng(smokeAlarm.latitude, smokeAlarm.longitude);
                mMap.addMarker(new MarkerOptions()
                        .position(fireAlarm)
                        .title("Temperature: " + smokeAlarm.temperature + " Level: " + smokeAlarm.level)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.normalcircle)));
            }
        }
        mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
    }



    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

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