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

public class ThirdActivity extends AppCompatActivity {

    //    EditText etResponse;
//    TextView tvIsConnected;
    TextView textview;
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // get reference to the views
        // etResponse = (EditText) findViewById(R.id.etResponse);
//        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
        //textview = (TextView) findViewById(R.id.textview);


        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        // check if you are connected or not
//        if (isConnected()) {
//            tvIsConnected.setBackgroundColor(0xFF00CC00);
//            tvIsConnected.setText("You are conncted");
//        } else {
//            tvIsConnected.setText("You are NOT conncted");
//        }

        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute("http://yummy.projects.mrt.ac.lk:8086/RESTfulExample/rest/foodservice/getAllRestaurantsTest");
    }


    public void map(View view){
        Toast.makeText(this, "YUMMY ONLINE MAP SERVICE", Toast.LENGTH_LONG).show();
        finish();
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);

    }
    public void search(View view){
        Toast.makeText(this, "YUMMY ONLINE SEARCH", Toast.LENGTH_LONG).show();
        finish();
        Intent intent = new Intent(this,Main6Activity.class);
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



            try {


                JSONArray jsonArr = new JSONArray(result);
                Log.i("llllllllllllllll", String.valueOf(jsonArr.length()));


                for (int i = 0; i < jsonArr.length(); i++) {
//                    Toast.makeText(ThirdActivity.this, "hello", Toast.LENGTH_SHORT).show( );
                    JSONObject jsonObj = jsonArr.getJSONObject(i);

                    String rname = jsonObj.getString("Restaurant_Name");
                    String of = jsonObj.getString("OFacility");
                    String em = jsonObj.getString("Email");
                    String con = jsonObj.getString("RContact");
                    String city = jsonObj.getString("NearestCity");
                    String Location = jsonObj.getString("Location");
                    String fooditems = jsonObj.getString("AvailableFoodItems");
                    String RImage =jsonObj.getString("RImage");
                    Log.i("hhhhhhhhhhhhhhhhhhh",rname+" "+of+" "+em+" "+con+" "+city+" "+Location);

                    HashMap<String, String> contact = new HashMap<>();

                    // adding each child node to HashMap key => value
                    contact.put("Restaurant_Name", rname);
                    contact.put("Ordering_Facility", of);
                    contact.put("Email", em);
                    contact.put("Contact", con);
                    contact.put("Nearest_City", city);
                    contact.put("fooditems", fooditems);
                    contact.put("RImage",RImage);
                    contact.put("Location", Location);

                    // adding contact to contact list
                    contactList.add(contact);


                }


                ListAdapter adapter = new SimpleAdapter(ThirdActivity.this, contactList,
                        R.layout.list_item, new String[]{ "Restaurant_Name","Nearest_City","Ordering_Facility","Email","Nearest_City"},
                        new int[]{R.id.t1, R.id.t2,R.id.t3,R.id.t4});
                lv.setAdapter(adapter);
//                ArrayList<String> myList = new ArrayList<String>();

//                intent.putExtra("contactList", contactList);
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