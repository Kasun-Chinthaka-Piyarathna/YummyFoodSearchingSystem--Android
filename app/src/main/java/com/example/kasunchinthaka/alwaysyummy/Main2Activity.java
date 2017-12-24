package com.example.kasunchinthaka.alwaysyummy;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

    public class Main2Activity extends AppCompatActivity {




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);






            // check if you are connected or not
            if (isConnected()) {

                Toast.makeText(getApplicationContext(), "YOU ARE CONNECTED TO YUMMY ONLINE", Toast.LENGTH_LONG).show();


            } else {

                Toast.makeText(getApplicationContext(), "YOU ARE NOT CONNECTED TO YUMMY ONLINE", Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("PLEASE ENABLE YOUR INTERNET CONNECTION")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }

            // call AsynTask to perform network operation on separate thread



        }



        public void login(View view) {
            EditText edit1 = (EditText) findViewById(R.id.edit1);
            EditText edit2 = (EditText) findViewById(R.id.edit2);

            String username = edit1.getText().toString();
            String password = edit2.getText().toString();


            if (username.equals("") || password.equals("")) {
                edit1.setText("");
                edit1.setError("field required!");
                edit2.setText("");
                edit2.setError("field required!");
//                Toast.makeText(this, " Error-have to fill all ", Toast.LENGTH_SHORT).show();

            } else {

                new HttpAsyncTask().execute("http://yummy.projects.mrt.ac.lk:8086/RESTfulExample/rest/foodservice/customerSignIn?cname=" + username + "&pwd=" + password);
            }

        }

        public void homepage(){
            finish();
            Intent intent = new Intent(this,Main4Activity.class);
            startActivity(intent);
        }

        public void reg(View view){
            finish();
            Intent intent = new Intent(this,Main3Activity.class);
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
//                Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();

                try {


                    JSONArray jsonArr = new JSONArray(result);


                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);

                        String rname = jsonObj.getString("Customer_Full_Name");
                        String contact = jsonObj.getString("Contact_Number");
                        String email = jsonObj.getString("Email");
                        String customer_id = jsonObj.getString("Customer_ID");

                        Toast.makeText(getBaseContext(),rname, Toast.LENGTH_LONG).show();
                        if(!(rname.equals("404") || rname.equals("0"))){


                            Toast.makeText(getBaseContext(), "YOU ARE SUCCESSFULLY SIGN IN", Toast.LENGTH_LONG).show();
                            Patient.username= rname;
                            Patient.Contact_Number=contact;
                            Patient.Email=email;
                            Patient.Customer_ID=customer_id;
                            homepage();


                        }else{
                            Toast.makeText(getBaseContext(), "USERNAME & PASSWORD INVALID", Toast.LENGTH_LONG).show();


                        }

                    }


                } catch (JSONException e) {


                    Toast.makeText(getBaseContext(), "USERNAME & PASSWORD INVALID", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
//                    e.printStackTrace();
                }
            }
        }
    }
