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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.bezzy.Ui.View.adapter.PostAdapter;
import com.bezzy.Ui.View.model.PostModel;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class FriendsProfileActivity extends AppCompatActivity {

    Button button;
    ImageView square_img;
    TextView userName,following,follower,Likes,userBio,edit_btn;
    ArrayList<PostModel> postList;
    ArrayList<String> imgList;
    RecyclerView postRecyclerView;
    SpotsDialog progressDialog;
    String friendsId;
    ImageView imageView;
    RelativeLayout layoutFollowing,layoutFollower;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_profile);

        square_img = findViewById(R.id.square_img);
        userName = findViewById(R.id.userName);
        following = findViewById(R.id.following_num);
        follower = findViewById(R.id.follower_num);
        Likes = findViewById(R.id.like_num);
        userBio = findViewById(R.id.userBio);
        imageView= findViewById(R.id.logout);
        edit_btn = findViewById(R.id.edit_btn);
        postRecyclerView = findViewById(R.id.postRecyclerView);
        layoutFollowing = findViewById(R.id.layoutFollowing);
        layoutFollower = findViewById(R.id.layoutFollower);

        postList = new ArrayList<>();
        imgList = new ArrayList<>();
        /*progressDialog = new SpotsDialog(FriendsProfileActivity.this);
        progressDialog.setMessage("Logging Out Please Wait....");
        progressDialog.setCancelable(false);*/

        friendsId = getIntent().getExtras().getString("friendId");


        layoutFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(!Utility.getFollowing(FriendsProfileActivity.this).equals("0")){
                    Intent intent = new Intent(getActivity(), FollowingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }*/

            }
        });

        layoutFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(!Utility.getFollowers(FriendsProfileActivity.this).equals("0")){
                    Intent intent = new Intent(getActivity(), MyFriendsList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }*/
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        postRecyclerView.setLayoutManager(layoutManager);


        if(Utility.internet_check(FriendsProfileActivity.this)) {

            // progressDialog.show();
            Utility.displayLoader(FriendsProfileActivity.this);

            postRequest(APIs.BASE_URL+APIs.GETDATA);
        }
        else {

            //progressDialog.show();
            Utility.displayLoader(FriendsProfileActivity.this);
            Toast.makeText(FriendsProfileActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
        }

    }

    private void postRequest(String url) {
        postList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject object = new JSONObject(response);
                    Log.e("Response",response);
                    String resp = object.getString("resp");
                    if(resp.equals("true")){

                        //progressDialog.dismiss();
                        Utility.hideLoader(FriendsProfileActivity.this);

                        userName.setText(object.getJSONObject("usedetails").getString("get_name"));
                        following.setText(object.getJSONObject("usedetails").getString("following"));
                        follower.setText(object.getJSONObject("usedetails").getString("followers"));

                        Likes.setText(object.getJSONObject("usedetails").getString("number_of_post"));

                        Glide.with(FriendsProfileActivity.this).load(object.getJSONObject("usedetails").getString("profile_pic")).into(square_img);

                        String bio = object.getJSONObject("usedetails").getString("bio");

                        if(!bio.equals("null")){
                            userBio.setVisibility(View.VISIBLE);
                            userBio.setText(object.getJSONObject("usedetails").getString("bio"));
                        }else{

                            userBio.setVisibility(View.GONE);
                        }

                        JSONArray array = object.getJSONArray("user_all_posts");
                        JSONArray array1 = array.getJSONArray(array.length()-1);
                        Log.e("Array",array1.toString());
                        for(int i=0;i<array1.length();i++){
                            JSONObject object1 = array1.getJSONObject(i);
                            postList.add(new PostModel(object1.getString("post_id"),
                                    object1.getString("post_url"),
                                    object1.getString("post_type"),
                                    object1.getString("id"),
                                    object1.getString("post_time"),
                                    object1.getString("post_date")
                                    ));




                                    /*object1.getString("post_id"),
                                    object1.getString("post_url"),
                                    object1.getString("post_type"),
                                    object1.getString("id")));*/
                        }

                        Log.e("Called","Adapter Called");
                        postRecyclerView.setAdapter((new PostAdapter(postList,FriendsProfileActivity.this,"2")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Exception",error.toString());
                //progressDialog.dismiss();
                Utility.hideLoader(FriendsProfileActivity.this);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("profile_id", friendsId);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(FriendsProfileActivity.this);
        queue.add(request);
    }
}