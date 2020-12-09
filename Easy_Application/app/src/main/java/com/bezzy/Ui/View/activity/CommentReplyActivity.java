package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentReplyActivity extends AppCompatActivity {

    CircleImageView img_logo;
    TextView title_text,comment_user,timeshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_reply);

        img_logo = findViewById(R.id.img_logo);
        title_text = findViewById(R.id.title_text);
        comment_user = findViewById(R.id.comment_user);
        timeshow = findViewById(R.id.timeshow);

        Glide.with(CommentReplyActivity.this)
                .load(getIntent().getExtras().getString("image"))
                .into(img_logo);

        title_text.setText(getIntent().getExtras().getString("name"));

        comment_user.setText(getIntent().getExtras().getString("comment"));

        timeshow.setText(getIntent().getExtras().getString("time"));
    }
}