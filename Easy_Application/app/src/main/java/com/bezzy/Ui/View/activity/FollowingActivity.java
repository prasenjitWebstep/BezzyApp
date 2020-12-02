package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.adapter.FollowingAdapter;
import com.bezzy.Ui.View.adapter.MyFriendsAdapter;
import com.bezzy.Ui.View.model.FriendsHolder;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class FollowingActivity extends AppCompatActivity {

    RecyclerView recyclerFriendsList;
    SpotsDialog progressDialog;
    FriendsHolder holderObj;
    ArrayList<FriendsHolder> holderList;
    TextView go_bezzy;
    String friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        recyclerFriendsList = findViewById(R.id.recyclerFriendsList);
        go_bezzy = findViewById(R.id.go_bezzy);
        progressDialog = new SpotsDialog(FollowingActivity.this);
        progressDialog.setMessage("Loading Please Wait...");
        progressDialog.setCancelable(false);
        holderList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(FollowingActivity.this);
        recyclerFriendsList.setLayoutManager(layoutManager);

        if(Utility.internet_check(FollowingActivity.this)) {

            //dialog.show();
            /*progressDialog.show();*/
            Log.e("Result","1");

            friendList(APIs.BASE_URL+APIs.FOLLOWINGLIST);

        }
        else {

            //dialog.dismiss();
            /*progressDialog.dismiss();*/
            Toast.makeText(FollowingActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
        }

    }

    private void friendList(String url) {
        holderList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("FollowingResponse",response);

                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("success")){
                        /*progressDialog.dismiss();*/
                        go_bezzy.setText(object.getString("login_user_name"));
                        JSONArray array = object.getJSONArray("following_user_list");
                        for(int i=0;i<array.length();i++){
                            JSONObject object1 = array.getJSONObject(i);
                            holderObj = new FriendsHolder(object1.getString("following_user_id"),object1.getString("name"),object1.getString("image"),"");
                            holderList.add(holderObj);
                        }
                        FollowingAdapter adapter = new FollowingAdapter(FollowingActivity.this,holderList,"1");
                        recyclerFriendsList.setAdapter(adapter);

                    }else{
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("Exception",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("loguser_id",Utility.getUserId(FollowingActivity.this));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(FollowingActivity.this);
        queue.add(request);
    }

}