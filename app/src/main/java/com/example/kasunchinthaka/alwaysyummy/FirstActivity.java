package com.example.kasunchinthaka.alwaysyummy;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FirstActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker myyy;
    String lati, longi, tank_name;


    ArrayList<HashMap<String, String>> contactList3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_test);

//        new FirstActivity.HttpAsyncTask( ).execute("http://yummy.projects.mrt.ac.lk:8086/phpset/tank.php");

        contactList3 = new ArrayList<>( );

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager( )
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//
//        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            public void onInfoWindowClick(Marker marker) {
//                Toast.makeText(FirstActivity.this, "Heiiiiiiiiiiiiiiiii", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(FirstActivity.this,ThirdActivity.class);
//                startActivity(intent);
//
//
//            }
//        });


    }

    public boolean onMarkerClick(final Marker marker) {


        if (marker.getTitle( ).equals("Beira Lake")) {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }

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

//        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(6.830118, 79.880083);
        mMap.addMarker(new MarkerOptions( ).position(new LatLng(6.904590, 79.852794)).title("TheSizzle"));
        mMap.addMarker(new MarkerOptions( ).position(new LatLng(6.911120, 79.856389)).title("Cricket Club Cafe"));
        mMap.addMarker(new MarkerOptions( ).position(new LatLng(6.878100, 79.857114)).title("KFC"));
        mMap.addMarker(new MarkerOptions( ).position(new LatLng(6.877136, 79.873084)).title("Schakasz"));
        mMap.addMarker(new MarkerOptions( ).position(new LatLng(6.904746, 79.905207)).title("Chapter One"));
        mMap.addMarker(new MarkerOptions( ).position(new LatLng(6.785296, 79.884699)).title("Pizza Hut"));
        mMap.addMarker(new MarkerOptions( ).position(new LatLng(6.861901, 79.859862)).title("Barracuda"));
        myyy = mMap.addMarker(new MarkerOptions( ).position(new LatLng(6.778601, 79.882393)).title("Steam Boat"));
        mMap.addMarker(new MarkerOptions( ).position(new LatLng(6.792281, 79.886927)).title("Sanrich"));


//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        float zoomLevel = (float) 11.0; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));


    }
//
//    @Override
//    public boolean onMarkerClick(final Marker marker) {
//        Toast.makeText(this, "FUCK", Toast.LENGTH_SHORT).show();
//
//        if (marker.equals(myyy))
//        {
//            Intent intent=new Intent(FirstActivity.this,Main2Activity.class);
//            startActivity(intent);
//        }
//        return false;
//    }


    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener( ) {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(FirstActivity.this, Main3Activity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener( ) {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel( );
                    }
                });
        AlertDialog alert = builder.create( );
        alert.show( );
    }


    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient( );

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity( ).getContent( );

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage( ));
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine( )) != null)
            result += line;

        inputStream.close( );
        return result;

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo( );
        return networkInfo != null && networkInfo.isConnected( );
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(FirstActivity.this);

        /** progress dialog to show user that the backup is processing. */
        /**
         * application context.
         */
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show( );
        }


        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
//            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

            try {


                JSONArray jsonArr = new JSONArray(result);


                for (int i = 0; i < jsonArr.length( ); i++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);

                    String device_id = jsonObj.getString("device_id");
                    tank_name = jsonObj.getString("tank_name");
                    longi = jsonObj.getString("longi");
                    lati = jsonObj.getString("lati");
                    String MaxWater = jsonObj.getString("MaxWater");
                    String MinWater = jsonObj.getString("MinWater");
                    String AvgRain = jsonObj.getString("AvgRain");
                    String Capacity = jsonObj.getString("Capacity");
                    String occupancy = jsonObj.getString("occupancy");
                    String AvgArea = jsonObj.getString("AvgArea");
                    String created_at = jsonObj.getString("created_at");
                    String updated_at = jsonObj.getString("updated_at");


                    float longii = Float.valueOf(longi);
                    float langii = Float.valueOf(lati);

                    mMap.addMarker(new MarkerOptions( ).position(new LatLng(longii, langii)).title(tank_name));


                    HashMap<String, String> contact = new HashMap<>( );

                    // adding each child node to HashMap key => value
                    contact.put("DEVICE_ID", device_id);
                    contact.put("TABK_NAME", tank_name);
                    contact.put("LONGI", longi);
                    contact.put("LATI", lati);
                    contact.put("MAXWATER", MaxWater);
                    contact.put("MINWATER", MinWater);
                    contact.put("AVGRAIN", AvgRain);
                    contact.put("CAPACITY", Capacity);
                    contact.put("OCCUPANCY", occupancy);
                    contact.put("AVGAREA", AvgArea);
                    contact.put("CREATEDAT", created_at);
                    contact.put("UPDATEDAT", updated_at);

                    // adding contact to contact list
                    contactList3.add(contact);


                }


                if (dialog.isShowing( )) {
                    dialog.dismiss( );
                }


            } catch (JSONException e) {
                Toast.makeText(getBaseContext( ), "NOT FOUND", Toast.LENGTH_LONG).show( );
            }
        }
    }


}