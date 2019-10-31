package com.example.trondev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Splashlogo extends AppCompatActivity {

    LinearLayout l1,l2;
    Animation downtoup,uptodown;
    Button buttonsub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashlogo);
        buttonsub = (Button)findViewById(R.id.buttonsub);

        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);


        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);

        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);
       buttonsub.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent goToLogpage = new Intent(Splashlogo.this,LoginPage.class);
               startActivity(goToLogpage);
           }
       });






    }
}
