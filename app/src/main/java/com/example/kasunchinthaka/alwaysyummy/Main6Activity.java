package com.example.kasunchinthaka.alwaysyummy;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Set;

public class Main6Activity extends AppCompatActivity {

//    EditText etResponse;
//    TextView tvIsConnected;
//    TextView textview;
    private ListView lv;
    EditText edit1,edit2,edit11,edit111;
    public static  String food,restau,location;
    public static String city;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);





        // get reference to the views
        // etResponse = (EditText) findViewById(R.id.etResponse);
//        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
        //textview = (TextView) findViewById(R.id.textview);


        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
//
//        // check if you are connected or not
//        if (isConnected()) {
//            tvIsConnected.setBackgroundColor(0xFF00CC00);
//            tvIsConnected.setText("You are conncted");
//        } else {
//            tvIsConnected.setText("You are NOT conncted");
//        }
//
//        // call AsynTask to perform network operation on separate thread



    }


    public void map(View view){
        Toast.makeText(this, "YUMMY ONLINE MAP SERVICE", Toast.LENGTH_LONG).show();
        finish();
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);

    }
    public void restaurant(View view){
        Toast.makeText(this, "YUMMY ONLINE AlL RESTAURANTS", Toast.LENGTH_LONG).show();
        finish();
        Intent intent = new Intent(this,Main7Activity.class);
        startActivity(intent);

    }


    public void viewrestaurant(View view){
        finish();
        View xview= (View) view.getParent();
        TextView tv = (TextView) xview.findViewById(R.id.t4);
        Intent intent = new Intent(this,Main8Activity.class);
        for (HashMap map: contactList) {
            if(map.get("Email").equals(tv.getText())){
                JSONObject obj = new JSONObject();
                Set<String> set = map.keySet();
                for(String key : set){
                    try {
                        obj.put(key,map.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                intent.putExtra("item",obj.toString());
                break;
            }
        }
        startActivity(intent);
    }
    //    http://localhost:8080/rest/foodservice/foodWise?fo=Pizza&ci=Moratuwa
    public void Search_By_Foodwise(View view){

        edit1 = (EditText)findViewById(R.id.edit1) ;
        edit2 = (EditText)findViewById(R.id.edit2) ;

        food = edit1.getText().toString();
        city = edit2.getText().toString();

        new HttpAsyncTask().execute("http://yummy.projects.mrt.ac.lk:8086/RESTfulExample/rest/foodservice/foodWise?fo="+food+"&ci="+city);



    }


    public void location(View view){

        edit111 = (EditText)findViewById(R.id.edit111) ;


        location = edit111.getText().toString();


        new HttpAsyncTask().execute("http://yummy.projects.mrt.ac.lk:8086/RESTfulExample/rest/foodservice/locationWise?loc="+location);
    }


    public void restau(View view){

        edit11 = (EditText)findViewById(R.id.edit11) ;


        restau = edit11.getText().toString();


        new HttpAsyncTask().execute("http://yummy.projects.mrt.ac.lk:8086/RESTfulExample/rest/foodservice/restaurantWise?res="+restau);
    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();

            try {


                JSONArray jsonArr = new JSONArray(result);


                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);

                    String rname = jsonObj.getString("Restaurant_Name");
                    String of = jsonObj.getString("OFacility");
                    String em = jsonObj.getString("Email");
                    String con = jsonObj.getString("RContact");
                    String city = jsonObj.getString("NearestCity");



                    // boolean isFruit = item.getBoolean("isFruit");


                    HashMap<String, String> contact = new HashMap<>();

                    // adding each child node to HashMap key => value
                    contact.put("Restaurant_Name", rname);
                    contact.put("Ordering_Facility", of);
                    contact.put("Email", em);
                    contact.put("Contact", con);
                    contact.put("Nearest_City", city);

                    // adding contact to contact list
                    contactList.add(contact);


                }


                ListAdapter adapter = new SimpleAdapter(Main6Activity.this, contactList,
                        R.layout.list_item, new String[]{ "Restaurant_Name","Nearest_City","Ordering_Facility","Email","Nearest_City"},
                        new int[]{R.id.t1, R.id.t2,R.id.t3,R.id.t4});

//                ArrayList<String> myList = new ArrayList<String>();
//                intent.putExtra("contactList", contactList);


                lv.setAdapter(adapter);
                Log.e("============>",contactList.toString());
//
//
//
//                etResponse.setText(name);
//                textview.setText(name + "  " + "Heiii");
                //etResponse.setText(json.toString(1));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
