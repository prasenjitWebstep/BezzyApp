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

import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

public class Splash extends AppCompatActivity {
    private View decorView;
    Button button;
    private static int splashtimeout=2000;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        View overlay = findViewById(R.id.splash_layout);
        button = findViewById(R.id.splash_button);
        logo=(ImageView)findViewById(R.id.logo_bezzy);

        Log.e("GETLOGIN",Utility.getLogin(Splash.this));

        if(Utility.getNewLogin(Splash.this).equals("1")){
            if(Utility.getOtpScreen(Splash.this).equals("1")){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Splash.this, OTPActivity.class);
                        startActivity(intent);
                        finish();

                    }
                },splashtimeout);
                Animation mysin= AnimationUtils.loadAnimation(this,R.anim.animation);
                logo.startAnimation(mysin);
            }else{
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
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Splash.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    },splashtimeout);
                    Animation mysin= AnimationUtils.loadAnimation(this,R.anim.animation);
                    logo.startAnimation(mysin);
                }

            }
            button.setVisibility(View.GONE);
        }else {
            button.setVisibility(View.VISIBLE);
        }


        /*decorView = getWindow().peekDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());

            }
        });*/
//        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                          |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                                           |View.SYSTEM_UI_FLAG_FULLSCREEN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.setNewLogin(Splash.this,"1");
                Intent intent = new Intent(Splash.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }*/
}