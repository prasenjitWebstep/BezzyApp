package com.bezzy.Ui.View.activity.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.Profile;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.potyvideo.library.AndExoPlayerView;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoDisplayActivity extends AppCompatActivity {
    VideoView videoView;
    AndExoPlayerView andExoPlayerView;
    String id,postId,type;
    ImageView back_image;
    TextView servicesText,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_display);
        back_image = findViewById(R.id.back_image);
        username = findViewById(R.id.username);
        servicesText = findViewById(R.id.servicesText);
        andExoPlayerView=findViewById(R.id.andExoPlayerView);

        id = getIntent().getExtras().getString("id");
        postId = getIntent().getExtras().getString("postId");
        type = getIntent().getExtras().getString("type");

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoDisplayActivity.this, Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        if(Utility.internet_check(VideoDisplayActivity.this)) {

            postRequest(APIs.BASE_URL+ APIs.GETIMAGEDETAILS+"/"+postId+"/"+id+"/"+type);
        }
        else {

            Toast.makeText(VideoDisplayActivity.this,"No Network!", Toast.LENGTH_SHORT).show();
        }
    }

    private void postRequest(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response",response);
                try {
                    JSONObject object1 = new JSONObject(response);
                    String status = object1.getString("status");
                    if(status.equals("success")){
                        JSONObject object11 = object1.getJSONObject("post_details");
                        username.setText(object11.getString("username"));
                        if(!object11.getString("content").equals("null")){
                            servicesText.setText(object11.getString("content"));
                        }else{
                            servicesText.setVisibility(View.GONE);
                        }

                        andExoPlayerView.setSource(object11.getString("url"));

                        /*Glide.with(ImageDisplayActivity.this)
                                .load(object11.getString("url"))
                                .into(imageView);*/
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

        RequestQueue queue = Volley.newRequestQueue(VideoDisplayActivity.this);
        queue.add(request);
    }

}