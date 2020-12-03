package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.adapter.FriendsPostAdapter;
import com.bezzy.Ui.View.model.FriendsPostModel;
import com.bezzy.Ui.View.model.Friendsfeed_item;
import com.bezzy.Ui.View.model.Notification_item;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class Friendsfeed extends AppCompatActivity {
    ArrayList<FriendsPostModel> postModelList;
    RecyclerView recyclerView;
    String id,name,photo;
    SpotsDialog progressDialog;
    FriendsPostAdapter adapterPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendsfeed);

        recyclerView = findViewById(R.id.frds_feed);
        id = getIntent().getExtras().getString("id");
        name = getIntent().getExtras().getString("name");
        photo = getIntent().getExtras().getString("image");

        postModelList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Friendsfeed.this);
        recyclerView.setLayoutManager(linearLayoutManager);

       /* progressDialog = new SpotsDialog(Friendsfeed.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);*/

        if(Utility.internet_check(Friendsfeed.this)) {

            //progressDialog.show();
            Utility.displayLoader(Friendsfeed.this);

            friendsPostList(APIs.BASE_URL+APIs.FRIENDPOSTLIST+"/"+id+"/"+Utility.getUserId(Friendsfeed.this));

        }
        else {

            //progressDialog.dismiss();
            Utility.hideLoader(Friendsfeed.this);

            Toast.makeText(Friendsfeed.this,"No Network!",Toast.LENGTH_SHORT).show();
        }

    }

    private void friendsPostList(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response);

                try {
                    JSONObject object1 = new JSONObject(response);
                    String status = object1.getString("status");
                    if(status.equals("success")){
                        //progressDialog.dismiss();
                        Utility.hideLoader(Friendsfeed.this);
                        JSONArray array = object1.getJSONArray("post_details");
                        for(int i=0;i<array.length();i++){

                            JSONObject object11 = array.getJSONObject(i);

                            FriendsPostModel modelObj = new FriendsPostModel(object11.getString("post_id"),
                                    object11.getString("post_type"),object11.getString("number_of_like"),
                                    object11.getString("number_of_comment"),name,
                                    photo,object11.getString("post_content"),object11.getString("log_user_like_status"),
                                    object11.getString("post_time"),object11.getJSONArray("post_img_video_live"));

                            postModelList.add(modelObj);

                        }
                        adapterPost = new FriendsPostAdapter(Friendsfeed.this,postModelList);
                        recyclerView.setAdapter(adapterPost);
                    }else{
                        //progressDialog.dismiss();
                        Utility.hideLoader(Friendsfeed.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressDialog.dismiss();
                    Utility.hideLoader(Friendsfeed.this);
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Utility.hideLoader(Friendsfeed.this);
                Log.e("Error",error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(Friendsfeed.this);
        queue.add(request);
    }
}