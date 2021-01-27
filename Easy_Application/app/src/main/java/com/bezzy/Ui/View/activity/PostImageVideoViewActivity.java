package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.adapter.FriendsEnlargeImagePostAdapter;
import com.bezzy.Ui.View.adapter.FriendsImagePostAdapter;
import com.bezzy.Ui.View.model.FriendsPostModelImage;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.potyvideo.library.AndExoPlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class PostImageVideoViewActivity extends AppCompatActivity {

    String postId;
    ImageView back_image,imageShow,fav_btn,favBtnfilled,comment_btn;
    TextView following_num,following_numm,servicesText;
    AndExoPlayerView andExoPlayerView;
    RecyclerView recyclerImageShow;
    SpotsDialog progressDialog;
    ArrayList<FriendsPostModelImage> postModelList = new ArrayList<>();
    FriendsEnlargeImagePostAdapter postImageObj;
    FriendsPostModelImage postModelObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image_video_view);

        postId = getIntent().getExtras().getString("postId");
        Log.e("POstID",postId);
        back_image = findViewById(R.id.back_image);
        imageShow = findViewById(R.id.imageShow);
        fav_btn = findViewById(R.id.fav_btn);
        favBtnfilled = findViewById(R.id.favBtnfilled);
        following_num = findViewById(R.id.following_num);
        following_numm = findViewById(R.id.following_numm);
        andExoPlayerView = findViewById(R.id.andExoPlayerView);
        recyclerImageShow = findViewById(R.id.recyclerImageShow);
        comment_btn=findViewById(R.id.chat_btn);
        servicesText = findViewById(R.id.servicesText);
       /* progressDialog =new SpotsDialog(PostImageVideoViewActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);*/

        if(Utility.internet_check(PostImageVideoViewActivity.this)) {

            //progressDialog.show();
            Utility.displayLoader(PostImageVideoViewActivity.this);
            friendsPostLargeView(APIs.BASE_URL+APIs.FRIENDSBLOCKDETAILS+"/"+postId+"/"+Utility.getUserId(PostImageVideoViewActivity.this));

        }
        else {

            //progressDialog.dismiss();
            Utility.hideLoader(PostImageVideoViewActivity.this);

            Toast.makeText(PostImageVideoViewActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
        }
        /*comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostImageVideoViewActivity.this, CommentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("postId",postId);
                startActivity(intent);
            }
        });*/

    }

    private void friendsPostLargeView(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*Log.e("Response",response);*/
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        //progressDialog.dismiss();
                        Utility.hideLoader(PostImageVideoViewActivity.this);
                        JSONObject object1 = object.getJSONObject("post_details");
                        if(object1.getString("log_user_like_status").equals("Yes")){
                            favBtnfilled.setVisibility(View.VISIBLE);
                        }else{
                            fav_btn.setVisibility(View.VISIBLE);
                        }

                        following_num.setText(object1.getString("number_of_like"));

                        following_numm.setText(object1.getString("number_of_comment"));

                        servicesText.setText(object1.getString("post_content"));

                        String mediaType = object1.getString("post_type");

                        if(mediaType.equals("video")){
                            Log.e("Clicked","1");
                            andExoPlayerView.setVisibility(View.VISIBLE);
                            imageShow.setVisibility(View.GONE);
                            recyclerImageShow.setVisibility(View.GONE);
                            JSONArray array = object1.getJSONArray("post_img_video_live");
                            for(int i=0; i<array.length(); i++){
                                try {
                                    JSONObject object2 = array.getJSONObject(i);
                                    andExoPlayerView.setSource(object2.getString("post_url"));
                                    andExoPlayerView.setShowFullScreen(false);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("Exception",e.toString());
                                }
                            }
                        }else if(mediaType.equals("image")){
                            JSONArray array = object1.getJSONArray("post_img_video_live");
                            int size = array.length();
                            if(size<2){
                                imageShow.setVisibility(View.VISIBLE);
                                recyclerImageShow.setVisibility(View.GONE);
                                andExoPlayerView.setVisibility(View.GONE);
                                for(int i=0;i<array.length();i++){

                                    try {

                                        JSONObject object2 = array.getJSONObject(i);
                                        Glide.with(PostImageVideoViewActivity.this)
                                                .load(object2.getString("post_url"))
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(imageShow);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("Exception",e.toString());
                                    }

                                }

                            }else{
                                recyclerImageShow.setVisibility(View.VISIBLE);
                                imageShow.setVisibility(View.GONE);
                                andExoPlayerView.setVisibility(View.GONE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(PostImageVideoViewActivity.this,RecyclerView.HORIZONTAL,false);
                                recyclerImageShow.setLayoutManager(layoutManager);
                                postModelList.clear();
                                for(int i=0;i<array.length();i++){

                                    try {
                                        JSONObject object2 = array.getJSONObject(i);
                                        postModelObj = new FriendsPostModelImage(object2.getString("post_url"),"",object2.getString("post_type"));
                                        postModelList.add(postModelObj);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("Exception",e.toString());
                                    }

                                }

                                postImageObj = new FriendsEnlargeImagePostAdapter(PostImageVideoViewActivity.this,postModelList);
                                recyclerImageShow.setAdapter(postImageObj);
                            }

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressDialog.dismiss();
                    Utility.hideLoader(PostImageVideoViewActivity.this);
                    Log.e("Exception",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Utility.hideLoader(PostImageVideoViewActivity.this);
                Log.e("Error",error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(PostImageVideoViewActivity.this);
        queue.add(request);
    }
}