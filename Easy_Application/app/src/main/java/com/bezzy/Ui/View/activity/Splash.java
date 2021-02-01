package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

public class Splash extends AppCompatActivity {
    private View decorView;
    TextView button;
    private static int splashtimeout=3000;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        View overlay = findViewById(R.id.splash_layout);
        button = findViewById(R.id.splash_button);
        logo=(ImageView)findViewById(R.id.logo_bezzy);

        Log.e("GETLOGIN",Utility.getLogin(Splash.this));

        if(Utility.getLogin(Splash.this).equals("1")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(Splash.this, Profile.class);
                    startActivity(intent);
                    finish();

                }
            },splashtimeout);
            Animation mysin= AnimationUtils.loadAnimation(this,R.anim.animation);
            logo.startAnimation(mysin);
        }else{
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Splash.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            });
        }

    }

}