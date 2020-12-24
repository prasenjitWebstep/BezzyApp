package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import kr.pe.burt.android.lib.androidgradientimageview.AndroidGradientImageView;

public class ImageDisplayActivity extends AppCompatActivity {

    ImageView back_image,favBtn,favBtnfilled;
    TextView username,servicesText,following_num,following_numm;
    ImageView imageView,chat_btn,delete_btn;
    String id,postId,type,postId2,screen;
    EmojiconEditText servicesText_t;
    String totalLikes;
    Button button;

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
        delete_btn=findViewById(R.id.delete_image);
        favBtnfilled = findViewById(R.id.favBtnfilled);
        favBtn = findViewById(R.id.favBtn);
        button = findViewById(R.id.button);


        id = getIntent().getExtras().getString("id");
        postId = getIntent().getExtras().getString("postId");
        type = getIntent().getExtras().getString("type");
        screen = getIntent().getExtras().getString("screen");

//        Log.e("GAAARRRR",id);
//        Log.e("GAAARRRR MMMAAAAAAARRRRRRRRRRRR",type);

        if(screen.equals("1")){
            delete_btn.setVisibility(View.VISIBLE);
        }else{
            delete_btn.setVisibility(View.INVISIBLE);
        }

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageDisplayActivity.this,Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();

            }
        });

        if(favBtn.getVisibility() == View.VISIBLE){
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Called","1");
                    favBtnfilled.setVisibility(View.VISIBLE);
                    favBtn.setVisibility(View.INVISIBLE);
                    if(Utility.internet_check(ImageDisplayActivity.this)) {
                        Log.e("POSTID",id+" "+postId);

                        friendsPostLike(APIs.BASE_URL+APIs.LIKEPOST+"/"+Utility.getUserId(ImageDisplayActivity.this)+"/"+id);

                    }
                    else {

                        Toast.makeText(ImageDisplayActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
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
                if(Utility.internet_check(ImageDisplayActivity.this)) {

                    Log.e("POSTID",id+" "+postId);

                    friendsPostLike(APIs.BASE_URL+APIs.LIKEPOST+"/"+Utility.getUserId(ImageDisplayActivity.this)+"/"+id);

                }
                else {

                    Toast.makeText(ImageDisplayActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*following_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageDisplayActivity.this,Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("postId",postId);
                startActivity(intent);


            }
        });
        following_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageDisplayActivity.this,Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("postId",postId);
                startActivity(intent);


            }
        });*/

        following_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalLikes.equals("0")){
                    Toast.makeText(ImageDisplayActivity.this, "NO ONE LIKED THIS POST", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(ImageDisplayActivity.this,Likeslist.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("postId",id);
                    startActivity(intent);
                }



            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String update;
                servicesText_t.setFocusable(false);
//                Log.e("POD",servicesText_t.getText().toString());
                update = servicesText_t.getText().toString();
                postUpdate(APIs.BASE_URL+APIs.UPDATEPOSTCAPTION,update);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utility.internet_check(ImageDisplayActivity.this)) {

            postRequest(APIs.BASE_URL+APIs.GETIMAGEDETAILS+"/"+postId+"/"+id+"/"+type+"/"+Utility.getUserId(ImageDisplayActivity.this));
        }
        else {

            Toast.makeText(ImageDisplayActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
        }
    }

    private void postUpdate(String url, final String caption){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if (resp.equals("success")) {
//                        String message = object.getString("message");
                        Log.e("OYOYOYOYOY", object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Exception", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("post_id", id);
                map.put("post_type",type );
                map.put("post_caption_text",caption );
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ImageDisplayActivity.this);
        queue.add(request);
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

        RequestQueue queue = Volley.newRequestQueue(ImageDisplayActivity.this);
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
                        final JSONObject object11 = object1.getJSONObject("post_details");
                        username.setText(object11.getString("username"));
                        if(!object11.getString("content").equals("null") || !object11.getString("content").equals("")){
                            servicesText_t.setText(object11.getString("content"));
                        }else{
                            servicesText_t.setVisibility(View.GONE);
                        }
                        Glide.with(ImageDisplayActivity.this)
                                .load(object11.getString("url"))
                                .into(imageView);
                        if(object11.getString("login_user_like").equals("Yes")){
                            favBtnfilled.setVisibility(View.VISIBLE);
                            favBtn.setVisibility(View.INVISIBLE);
                        }else{
                            favBtn.setVisibility(View.VISIBLE);
                        }
                        totalLikes =object11.getString("total_like");
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

    public void delete(){
        final String url = null;
        AlertDialog.Builder builder=new AlertDialog.Builder(ImageDisplayActivity.this);
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
        alertDialog.show();
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

                        Toast.makeText(ImageDisplayActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(ImageDisplayActivity.this,Profile.class);
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
        RequestQueue queue = Volley.newRequestQueue(ImageDisplayActivity.this);
        queue.add(request);


    }

}