package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

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

import kr.pe.burt.android.lib.androidgradientimageview.AndroidGradientImageView;

public class ImageDisplayActivity extends AppCompatActivity {

    ImageView back_image;
    TextView username,servicesText;
    ImageView imageView;
    String id,postId,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        back_image = findViewById(R.id.back_image);
        username = findViewById(R.id.username);
        servicesText = findViewById(R.id.servicesText);
        imageView = findViewById(R.id.imageHolder);

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
                        Glide.with(ImageDisplayActivity.this)
                                .load(object11.getString("url"))
                                .into(imageView);
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