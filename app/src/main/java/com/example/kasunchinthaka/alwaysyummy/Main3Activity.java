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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main3Activity extends AppCompatActivity {

    EditText etResponse;
    TextView tvIsConnected;
    TextView textview;
    private ListView lv;
    EditText edit1, edit2;
    public static String food;
    public static String city;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


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

    //    http://localhost:8080/rest/foodservice/foodWise?fo=Pizza&ci=Moratuwa
    public void register(View view) {

        EditText edit1 = (EditText) findViewById(R.id.fname);
        EditText edit2 = (EditText) findViewById(R.id.phone);
        EditText edit3 = (EditText) findViewById(R.id.email);
        EditText edit4 = (EditText) findViewById(R.id.nic);
        EditText edit5 = (EditText) findViewById(R.id.username);
        EditText edit6 = (EditText) findViewById(R.id.password);
        EditText edit7 = (EditText) findViewById(R.id.conpassword);


        String fname = edit1.getText().toString();
        String phone = edit2.getText().toString();
        String email = edit3.getText().toString();
        String nic = edit4.getText().toString();
        String username = edit5.getText().toString();
        String password = edit6.getText().toString();
        String conpassword = edit7.getText().toString();

        if (fname.equals("") || phone.equals("") || email.equals("") || nic.equals("") || username.equals("") || password.equals("") || conpassword.equals("")) {
            edit1.setText("");
            edit1.setError("field required!");
            edit2.setText("");
            edit2.setError("field required!");
            edit3.setText("");
            edit3.setError("field required!");
            edit4.setText("");
            edit4.setError("field required!");
            edit5.setText("");
            edit5.setError("field required!");
            edit6.setText("");
            edit6.setError("field required!");
            edit7.setText("");
            edit7.setError("field required!");

        } else if (isEmailValid(email)) {

            if (password.equals(conpassword)) {

                new HttpAsyncTask().execute("http://yummy.projects.mrt.ac.lk:8086/RESTfulExample/rest/foodservice/customerSignUp?cname=" + fname + "&phone=" + phone + "&email=" + email + "&nic=" + nic + "&username=" + username + "&pwd=" + password);
            } else {
                edit6.setText("");
                edit6.setError("Field Required!");
                edit7.setText("");
                edit7.setError("Field Required!");
                Toast.makeText(this, "Password Didn't Match", Toast.LENGTH_SHORT).show();

            }
        } else {
            edit3.setText("");
            edit3.setError("field required!");
            Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show();

        }
    }
    public void accoutlog(View view){
        finish();
        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public void homepage() {
        finish();
        Intent intent = new Intent(this, Main4Activity.class);
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


//                JSONArray jsonArr = new JSONArray(result);

//
//                for (int i = 0; i < jsonArr.length(); i++) {
//                    JSONObject jsonObj = jsonArr.getJSONObject(i);

//                    String cname = jsonObj.getString("Customer_Full_Name");


            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            if (!(result.equals("404"))) {

                Toast.makeText(getBaseContext(), "ALMOST DONE! WELCOME TO YUMMY ONLINE", Toast.LENGTH_LONG).show();
                Patient.username = result;
                homepage();


            } else {


                Toast.makeText(getBaseContext(), "SOMETHING WRONG!PLEAE TRY AGAIN.", Toast.LENGTH_LONG).show();

            }


        }
    }
}
