package com.example.kasunchinthaka.alwaysyummy;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.VideoView;

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

public class SearchItems extends AppCompatActivity {

    String passID;
    ArrayList<HashMap<String, String>> contactList;
    private ListView lv;
    EditText edit1,edit2,edit11,edit111;
    public static  String food,restau,location;
    public static String city;



    AutoCompleteTextView text,text2,text3,text4;
    private ProgressBar progressBar;
    private VideoView vv;
    private MediaController mediacontroller;
    private Uri uri;
    private boolean isContinuously = false;


    String[] foodData={"Soft Ice Cream Cone","Colonel's Fries"," Chicken Strip","Chicken Bucket","Chicken Submarine","Twister","Veggie Burger","Snacker","Hot Tea","Lemon Ice Tea","Hot Chocalate","Vanilla Latte","Cappucino","Hot Butter Mushroom","French Fries","Three Egg Omlete","Fried kankun","Vegetable Lasagne","Mediterranean salad ","Pizza","Brooklyn beef salad","Calypso calamari salad","Tomato Feta Bruschetta","American Breakfast","English Breakfast","Smoked Salmon","House Salad","Fried Mozzarella","Boneless Buffalo Bites","Beef Lasagne","Breakfast Berry","Chocolate Cake Sundae","Strawberry Sundae","Strawberry Cheesecake","Cereals"};
    String[] locationData={"Kollupitiya","Wellawatta","Battaramulla","Dehiwala","Moratuwa"};
    String[] restaurantData={"TheSizzle","Cricket Club Cafe","KFC","Schakasz","Chapter One","Steam Boat","Sanrich","Pizza Hut","Barracuda"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        passID = bundle.getString("passID");
        if(passID.equals("1")){
        setContentView(R.layout.foodwise);



            //pay a vedio

            WebView htmlWebView = (WebView)findViewById(R.id.webView);
            htmlWebView.setWebViewClient(new CustomWebViewClient());
            WebSettings webSetting = htmlWebView.getSettings();
            webSetting.setJavaScriptEnabled(true);
            webSetting.setDisplayZoomControls(true);
            htmlWebView.loadUrl("https://www.youtube.com/watch?v=h_YmYLYi65k");


            //end


            text=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,foodData);

            text2=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView2);
            ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,locationData);

            text.setAdapter(adapter);
            text.setThreshold(1);


            text2.setAdapter(adapter2);
            text2.setThreshold(1);



         }else if(passID.equals("2")){

            setContentView(R.layout.restaurantwise);
            //pay a vedio

            WebView htmlWebView = (WebView)findViewById(R.id.webView);
            htmlWebView.setWebViewClient(new CustomWebViewClient());
            WebSettings webSetting = htmlWebView.getSettings();
            webSetting.setJavaScriptEnabled(true);
            webSetting.setDisplayZoomControls(true);
            htmlWebView.loadUrl("https://www.youtube.com/watch?v=h_YmYLYi65k");


            //end



            text3=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView3);
            ArrayAdapter adapter3 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,restaurantData);

            text3.setAdapter(adapter3);
            text3.setThreshold(1);



        }else{
            setContentView(R.layout.locationwise);



            //pay a vedio

            WebView htmlWebView = (WebView)findViewById(R.id.webView);
            htmlWebView.setWebViewClient(new CustomWebViewClient());
            WebSettings webSetting = htmlWebView.getSettings();
            webSetting.setJavaScriptEnabled(true);
            webSetting.setDisplayZoomControls(true);
            htmlWebView.loadUrl("https://www.youtube.com/watch?v=h_YmYLYi65k");


            //end


            text4=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView4);

            ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,locationData);

            text4.setAdapter(adapter2);
            text4.setThreshold(1);
        }



        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);



    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }




    public void viewrestaurant(View view){
        Log.i("ssssssssssss","sahjsh");
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
               Log.i("dddds",obj.toString());
                intent.putExtra("item",obj.toString());
                break;
            }
        }
        startActivity(intent);
    }

    //    http://localhost:8080/rest/foodservice/foodWise?fo=Pizza&ci=Moratuwa
    public void Search_By_Foodwise(View view) {


        food = text.getText().toString();
        location = text2.getText().toString();

//        Toast.makeText(getBaseContext(),food+" "+location, Toast.LENGTH_LONG).show();


        new HttpAsyncTask().execute("http://yummy.projects.mrt.ac.lk:8086/RESTfulExample/rest/foodservice/foodWise?fo="+food+"&ci="+location);

    }

    public void restau(View view) {

        restau = text3.getText().toString();


        new SearchItems.HttpAsyncTask().execute("http://yummy.projects.mrt.ac.lk:8086/RESTfulExample/rest/foodservice/restaurantWise?res="+restau);

//
//    }
    }


    public void location(View view) {

        location = text4.getText().toString();

        new SearchItems.HttpAsyncTask().execute("http://yummy.projects.mrt.ac.lk:8086/RESTfulExample/rest/foodservice/locationWise?loc="+location);

//
//    }
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
//            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

            try {


                JSONArray jsonArr = new JSONArray(result);


                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);

                    String rname = jsonObj.getString("Restaurant_Name");
                    String of = jsonObj.getString("OFacility");
                    String em = jsonObj.getString("Email");
                    String con = jsonObj.getString("RContact");
                    String city = jsonObj.getString("NearestCity");
                    String Location = jsonObj.getString("Location");
                    String fooditems = jsonObj.getString("AvailableFoodItems");
                    String RImage =jsonObj.getString("RImage");

                    HashMap<String, String> contact = new HashMap<>();

                    // adding each child node to HashMap key => value
                    contact.put("Restaurant_Name", rname);
                    contact.put("Ordering_Facility", of);
                    contact.put("Email", em);
                    contact.put("Contact", con);
                    contact.put("Nearest_City", city);
                    contact.put("fooditems", fooditems);
                    contact.put("RImage", RImage);

                    // adding contact to contact list
                    contactList.add(contact);


                }

                ListAdapter adapter = new SimpleAdapter(SearchItems.this, contactList,
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
