package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
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
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.ViewPropertyTransition;

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

        ViewPropertyTransition.Animator animationObject = new ViewPropertyTransition.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha(0f);

                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(2500);
                fadeAnim.start();
            }
        };

        Glide.with(Splash.this)
                .load(R.drawable.logo)
                .transition(GenericTransitionOptions.with(animationObject))
                .into(logo);

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