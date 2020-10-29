package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bezzy_application.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class showprofile extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;

    TextView nameTV;
    TextView emailTV;
    ImageView photoIV;
    String personName,personEmail,personPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showprofile);


    }
}