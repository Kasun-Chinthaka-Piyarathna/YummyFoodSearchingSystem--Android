package com.example.kasunchinthaka.alwaysyummy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Main4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
    }


    public void welcome(View view){

        finish();
        Intent intent = new Intent(this,TabBarActivity.class);
        startActivity(intent);

    }
}
