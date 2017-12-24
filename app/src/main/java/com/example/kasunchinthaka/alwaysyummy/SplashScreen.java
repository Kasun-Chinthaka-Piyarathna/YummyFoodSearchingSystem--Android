package com.example.kasunchinthaka.alwaysyummy;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    Animation animFadeIn;
    TextView txtFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.splash);
        txtFadeIn=(TextView)findViewById(R.id.txt_fade_in);
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in2);

        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in2);

        txtFadeIn.setVisibility(View.VISIBLE);
        txtFadeIn.startAnimation(animFadeIn);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                finish();
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

            }
        }, SPLASH_TIME_OUT);

    }
}
