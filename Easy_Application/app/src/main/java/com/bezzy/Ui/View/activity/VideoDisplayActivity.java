package com.bezzy.Ui.View.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.potyvideo.library.AndExoPlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VideoDisplayActivity extends AppCompatActivity {
    VideoView videoView;
    AndExoPlayerView andExoPlayerView;
    String id,postId,type,screen;
    ImageView back_image,chat_btn,delete_image,favBtn,favBtnfilled;
    TextView servicesText,username,following_num,following_numm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_display);
        back_image = findViewById(R.id.back_image);
        username = findViewById(R.id.username);
        servicesText = findViewById(R.id.servicesText);
        andExoPlayerView=findViewById(R.id.andExoPlayerView);
        following_num = findViewById(R.id.following_num);
        following_numm = findViewById(R.id.following_numm);
        delete_image = findViewById(R.id.delete_image);
        chat_btn =  findViewById(R.id.chat_btn);
        favBtn = findViewById(R.id.favBtn);
        favBtnfilled = findViewById(R.id.favBtnfilled);

        id = getIntent().getExtras().getString("id");
        postId = getIntent().getExtras().getString("postId");
        type = getIntent().getExtras().getString("type");
        screen = getIntent().getExtras().getString("screen");

        if(screen.equals("1")){
            delete_image.setVisibility(View.VISIBLE);
        }else{
            delete_image.setVisibility(View.INVISIBLE);
        }

        if(favBtn.getVisibility() == View.VISIBLE){
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Called","1");
                    favBtnfilled.setVisibility(View.VISIBLE);
                    favBtn.setVisibility(View.INVISIBLE);
                    if(Utility.internet_check(VideoDisplayActivity.this)) {
                        Log.e("POSTID",id+" "+postId);

                        friendsPostLike(APIs.BASE_URL+APIs.LIKEPOST+"/"+Utility.getUserId(VideoDisplayActivity.this)+"/"+id);

                    }
                    else {

                        Toast.makeText(VideoDisplayActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        favBtnfilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Called","2");
                favBtnfilled.setVisibility(View.INVISIBLE);
                favBtn.setVisibility(View.VISIBLE);
                if(Utility.internet_check(VideoDisplayActivity.this)) {

                    Log.e("POSTID",id+" "+postId);

                    friendsPostLike(APIs.BASE_URL+APIs.LIKEPOST+"/"+Utility.getUserId(VideoDisplayActivity.this)+"/"+id);

                }
                else {

                    Toast.makeText(VideoDisplayActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoDisplayActivity.this, Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete() ;

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Utility.internet_check(VideoDisplayActivity.this)) {

            postRequest(APIs.BASE_URL+ APIs.GETIMAGEDETAILS+"/"+postId+"/"+id+"/"+type+"/"+Utility.getUserId(VideoDisplayActivity.this));
        }
        else {

            Toast.makeText(VideoDisplayActivity.this,"No Network!", Toast.LENGTH_SHORT).show();
        }
    }

    private void friendsPostLike(String url) {
        Log.e("URL",url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        String number = object.getJSONObject("activity").getString("number_of_activity");
                        following_num.setText(number);
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


    private void postRequest(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response",response);
                try {
                    final JSONObject object1 = new JSONObject(response);
                    String status = object1.getString("status");
                    if(status.equals("success")){
                        JSONObject object11 = object1.getJSONObject("post_details");
                        username.setText(object11.getString("username"));
                        if(!object11.getString("content").equals("null") || !object11.getString("content").equals("")){
                            servicesText.setText(object11.getString("content"));
                        }else{
                            servicesText.setVisibility(View.GONE);
                        }

                        if(object11.getString("login_user_like").equals("Yes")){
                            favBtnfilled.setVisibility(View.VISIBLE);
                            favBtn.setVisibility(View.INVISIBLE);
                        }else{
                            favBtn.setVisibility(View.VISIBLE);
                        }

                        andExoPlayerView.setSource(object11.getString("url"));

                        following_num.setText(object11.getString("total_like"));
                        following_numm.setText(object11.getString("total_comment"));
                        chat_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(VideoDisplayActivity.this,CommentActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                try {
                                    intent.putExtra("postId",object1.getString("post_id"));
                                    Log.e("PostId",object1.getString("post_id"));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

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

    public void delete(){
        final String url = null;
        AlertDialog.Builder builder=new AlertDialog.Builder(VideoDisplayActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to delete this image ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteimg(APIs.BASE_URL + APIs.DELETEIMGVID);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show() ;
    }

    private void deleteimg(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("status");
                    if (resp.equals("success")) {

                        Toast.makeText(VideoDisplayActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(VideoDisplayActivity.this,Profile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("From","Image");
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Exception", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("imgvideoID", postId);
                map.put("post_type",type );
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(VideoDisplayActivity.this);
        queue.add(request);


    }

}