package com.bezzy.Ui.View.activity.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bezzy_application.R;

public class VideoDisplayActivity extends AppCompatActivity {
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_display);
        videoView=findViewById(R.id.video_display);
        Uri uri=Uri.parse("ur");
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
        MediaController mediaController=new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }
}