package com.example.kasunchinthaka.alwaysyummy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SendMailActivity extends Activity {

    Button buttonSend;
    TextView textTo;
    TextView textSubject;
    TextView textMessage;
    String item,mail_body;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        Bundle bundle = getIntent().getExtras();
        item = bundle.getString("item");
//        mail_body = bundle.getString("mail_body");


        buttonSend = (Button) findViewById(R.id.buttonSend);
        textTo = (TextView) findViewById(R.id.editTextTo);
        textSubject = (TextView) findViewById(R.id.editTextSubject);
        textMessage = (TextView) findViewById(R.id.editTextMessage);


        textTo.setText("kasunchinthaka555@gmail.com");
        textSubject.setText("YUMMY ONLIE ORDERING");
        final String body = "CUSTOMER: "+Patient.username +"\n"+ "Order: "+Patient.MSGbody+"\n"+"DELIVERY COST: "+Patient.deliverycost+"\n"+"CUSTOMER CONTACT NUMBER: "+Patient.Contact_Number;
        textMessage.setText(body);

        buttonSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {



                String to = "kasunchinthaka555@gmail.com";
                String subject ="YUMMY ONLIE ORDERING";
                String message = body;

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));

            }
        });
    }
}