package com.example.kasunchinthaka.alwaysyummy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class Main8Activity extends AppCompatActivity {

    MediaPlayer mySound;
    TextView textblink;
    Animation animBlink;
    ListView lv;
    ListView myListView;
    EditText edit;
    Button myButton;
    ArrayList<String> todoItems;
    ArrayAdapter<String> aa;
    public String body;
    public  String item;
    EditText myEditText;

    ArrayList<HashMap<String, String>> ItemList;
    Animation fade_in, fade_out;
    ViewFlipper viewFlipper;
    Bitmap mIcon_val;

    public static  String URL  ;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);



////        viewFlipper = (ViewFlipper) this.findViewById(R.id.bckgrndViewFlipper1);
//        fade_in = AnimationUtils.loadAnimation(this,
//                android.R.anim.fade_in);
//        fade_out = AnimationUtils.loadAnimation(this,
//                android.R.anim.fade_out);
//        viewFlipper.setInAnimation(fade_in);
//        viewFlipper.setOutAnimation(fade_out);
////sets auto flipping
//        viewFlipper.setAutoStart(true);
//        viewFlipper.setFlipInterval(5000);
//        viewFlipper.startFlipping();







        ItemList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);


        textblink=(TextView)findViewById(R.id.text1);
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        textblink.setVisibility(View.VISIBLE);
        textblink.startAnimation(animBlink);


//         String strobject= getIntent().getStringExtra("item");
        try {
//            JSONArray array = new JSONArray(strobject);

            Bundle bundle = getIntent().getExtras();
             item = bundle.getString("item");

            JSONObject obj = new JSONObject(item);

//            JSONObject jsonObj =  getIntent().getStringExtra("item").getJSONObject(0);

            String rname = obj.getString("Restaurant_Name");
            String of = obj.getString("Ordering_Facility");
            String em = obj.getString("Email");
            String con = obj.getString("Contact");
            String city = obj.getString("Nearest_City");
            String fooditems = obj.getString("fooditems");
            String RImage = obj.getString("RImage");

            URL = RImage;


            JSONArray array_foods = new JSONArray(fooditems);


            for (int i = 0; i < array_foods.length(); i++) {
                JSONObject foods = array_foods.getJSONObject(i);

                String food_name = foods.getString("Name");
                String quantity = foods.getString("Quantity");
                String unit_price = foods.getString("Unit_Price");

                HashMap<String, String> FOOD = new HashMap<>();

                // adding each child node to HashMap key => value
                FOOD.put("Item_Name", food_name);
                FOOD.put("Quantity", quantity);
                FOOD.put("Unit_Price", unit_price);

                ItemList.add(FOOD);


            }
//


            TextView text1 = (TextView)findViewById(R.id.text1);
            text1.setText(rname);
            TextView text2 = (TextView)findViewById(R.id.text2);
            text2.setText("Ordering Facility: "+of);
            TextView text3 = (TextView)findViewById(R.id.text3);
            text3.setText("Email: "+em);
            TextView text4 = (TextView)findViewById(R.id.text4);
            text4.setText("Contact: "+con);
            TextView text5 = (TextView)findViewById(R.id.text5);
            text5.setText("City: "+city);


            ListAdapter adapter = new SimpleAdapter(Main8Activity.this, ItemList,
                    R.layout.list_item2, new String[]{ "Item_Name","Quantity","Unit_Price"},
                    new int[]{R.id.t1, R.id.t2,R.id.t3});
            lv.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }



        ListView myListView = (ListView) findViewById(R.id.myListView);
         myEditText = (EditText) findViewById(R.id.eeee1);


        final ArrayList<String> todoItems = new ArrayList<String>();
//        todoItems.add("YUMMY");
//        todoItems.add("online delivery service");
        final ArrayAdapter<String> aa;
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);

        myListView.setAdapter(aa);

        Button myButton = (Button) findViewById(R.id.but);

        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                body = myEditText.getText().toString();
                Patient.MSGbody=body;
                //Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();//Problem here

                // Add your input text to list as follows
                todoItems.add(body );  // Now list todoItems has been added with new item at end of list.

                aa.notifyDataSetChanged();   // So your list adapter to be refreshed with new item

                Intent intent = new Intent(Main8Activity.this,Main9Activity.class);
                intent.putExtra("item",item);
                startActivity(intent);


            }
        });

        imageView =(ImageView)findViewById(R.id.image1);

        // Create an object for subclass of AsyncTask
        GetXMLTask task = new GetXMLTask();
        // Execute the task
        task.execute(new String[] { URL });






    }
    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }

    public void delivery(View view){


        Intent intent = new Intent(this,Main9Activity.class);
        intent.putExtra("item",item);
        startActivity(intent);
    }

    public void orderprocess(View view){
        LinearLayout ll = (LinearLayout)findViewById(R.id.invisiblelayer);
        ll.setVisibility(view.VISIBLE);
    }

    public void order(View view){
        Toast.makeText(this,getIntent().getStringExtra("You are about to order"),Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(this,SendMailActivity.class);
        intent.putExtra("item",item);
        intent.putExtra("mail_body",body);
        finish();
        startActivity(intent);
    }

    public void onBackPressed(){

        finish();
        Intent intent = new Intent(this,ThirdActivity.class);
        startActivity(intent);


    }





}
