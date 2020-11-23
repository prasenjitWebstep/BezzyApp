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

public class FriendsProfileActivity extends AppCompatActivity {

    String friendsId,screen;
    ProgressDialog progressDialog;
    TextView userName,userFriends,userBioHeading,userBio,userFriendsHeading;
    CircleImageView circularImg;
    RecyclerView postRecyclerView;
    ArrayList<PostModel> postList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_profile);

        friendsId = getIntent().getExtras().getString("friendId");
        screen = getIntent().getExtras().getString("screen");
        userName = findViewById(R.id.userName);
        userFriends = findViewById(R.id.userFriends);
        userBioHeading = findViewById(R.id.userBioHeading);
        userBio = findViewById(R.id.userBio);
        circularImg = findViewById(R.id.circularImg);
        userFriendsHeading = findViewById(R.id.userFriendsHeading);
        postRecyclerView = findViewById(R.id.postRecyclerView);
        postList = new ArrayList<>();

        progressDialog = new ProgressDialog(FriendsProfileActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        postRecyclerView.setLayoutManager(layoutManager);

        userFriendsHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsProfileActivity.this, MyFriendsList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        userFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsProfileActivity.this, MyFriendsList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        if(Utility.internet_check(FriendsProfileActivity.this)) {

            progressDialog.show();

            postRequest(APIs.BASE_URL+APIs.GETDATA);
        }
        else {

            progressDialog.show();
            Toast.makeText(FriendsProfileActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
        }

    }

    private void postRequest(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject object = new JSONObject(response);
                    Log.e("Response",response);
                    String resp = object.getString("resp");
                    if(resp.equals("true")){

                        progressDialog.dismiss();

                        userName.setText(object.getJSONObject("usedetails").getString("get_name"));
                        userFriends.setText(object.getJSONObject("usedetails").getString("number_of_friend"));
                        Glide.with(FriendsProfileActivity.this).load(object.getJSONObject("usedetails").getString("profile_pic")).into(circularImg);

                        String bio = object.getJSONObject("usedetails").getString("bio");

                        if(!bio.equals("null")){
                            userBio.setText(object.getJSONObject("usedetails").getString("bio"));
                        }else{
                            userBioHeading.setVisibility(View.GONE);
                            userBio.setVisibility(View.GONE);
                        }

                        JSONArray array = object.getJSONArray("user_all_posts");
                        JSONArray array1 = array.getJSONArray(array.length()-1);
                        Log.e("Array",array1.toString());
                        for(int i=0;i<array1.length();i++){
                            JSONObject object1 = array1.getJSONObject(i);
                            postList.add(new PostModel(object1.getString("post_id"),object1.getString("post_url"),object1.getString("post_type"),object1.getString("id")));
                        }

                        Log.e("Called","Adapter Called");
                        postRecyclerView.setAdapter((new PostAdapter(postList,FriendsProfileActivity.this)));
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
                progressDialog.dismiss();

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