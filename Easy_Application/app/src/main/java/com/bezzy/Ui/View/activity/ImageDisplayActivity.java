package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.Fragments.ProfileFragment;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import kr.pe.burt.android.lib.androidgradientimageview.AndroidGradientImageView;

public class ImageDisplayActivity extends AppCompatActivity {

    ImageView back_image;
    TextView username,servicesText,following_num,following_numm;
    ImageView imageView,chat_btn;
    String id,postId,type,postId2;
    EmojiconTextView servicesText_t;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        back_image = findViewById(R.id.back_image);
        username = findViewById(R.id.username);
        //servicesText = findViewById(R.id.servicesText);
        servicesText_t=findViewById(R.id.servicesText_t);
        imageView = findViewById(R.id.imageHolder);
        following_num = findViewById(R.id.following_num);
        following_numm = findViewById(R.id.following_numm);
        chat_btn = findViewById(R.id.chat_btn);

        id = getIntent().getExtras().getString("id");
        postId = getIntent().getExtras().getString("postId");
        type = getIntent().getExtras().getString("type");

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageDisplayActivity.this,Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        if(Utility.internet_check(ImageDisplayActivity.this)) {

            postRequest(APIs.BASE_URL+APIs.GETIMAGEDETAILS+"/"+postId+"/"+id+"/"+type);
        }
        else {

            Toast.makeText(ImageDisplayActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
        }
    }

    private void postRequest(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response",response);
                try {
                    final JSONObject object1 = new JSONObject(response);
                    String status = object1.getString("status");
                    if(status.equals("success")){
                        final JSONObject object11 = object1.getJSONObject("post_details");
                        username.setText(object11.getString("username"));
                        if(!object11.getString("content").equals("null")){
                            servicesText_t.setText(object11.getString("content"));
                        }else{
                            servicesText_t.setVisibility(View.GONE);
                        }
                        Glide.with(ImageDisplayActivity.this)
                                .load(object11.getString("url"))
                                .into(imageView);
                        following_num.setText(object11.getString("total_like"));
                        following_numm.setText(object11.getString("total_comment"));
                        chat_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ImageDisplayActivity.this,CommentActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                try {
                                    intent.putExtra("postId",object1.getString("post_id"));
                                    Log.e("PostId",object1.getString("post_id"));
                                    intent.putExtra("screen","1");
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(ImageDisplayActivity.this);
        queue.add(request);
    }
}