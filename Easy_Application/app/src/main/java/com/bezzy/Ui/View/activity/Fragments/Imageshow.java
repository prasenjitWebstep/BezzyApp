package com.bezzy.Ui.View.activity.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bezzy_application.R;

import kr.pe.burt.android.lib.androidgradientimageview.AndroidGradientImageView;

public class Imageshow extends AppCompatActivity {

    AndroidGradientImageView imageView;
    ImageView back_image;
    TextView username,servicesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageshow);
    }
}