package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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

public class FriendsFriendList extends AppCompatActivity {

    RecyclerView recyclerFriendsList;
    ProgressDialog progressDialog;
    FriendsHolder holderObj;
    ArrayList<FriendsHolder> holderList;
    TextView go_bezzy;
    String friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_friend_list);

        recyclerFriendsList = findViewById(R.id.recyclerFriendsList);
        go_bezzy = findViewById(R.id.go_bezzy);
        progressDialog = new ProgressDialog(FriendsFriendList.this);
        progressDialog.setMessage("Loading Please Wait...");
        progressDialog.setCancelable(false);
        holderList = new ArrayList<>();
        friendId = getIntent().getExtras().getString("FriendId");

        LinearLayoutManager layoutManager = new LinearLayoutManager(FriendsFriendList.this);
        recyclerFriendsList.setLayoutManager(layoutManager);

        if(Utility.internet_check(FriendsFriendList.this)) {

            //dialog.show();
            progressDialog.show();
            Log.e("Result","1");

            friendList(APIs.BASE_URL+APIs.PROFILE_FRIEND_LIST+"/"+Utility.getUserId(FriendsFriendList.this)+"/"+friendId);

        }
        else {

            //dialog.dismiss();
            progressDialog.dismiss();
            Toast.makeText(FriendsFriendList.this,"No Network!",Toast.LENGTH_SHORT).show();
        }

    }

    private void friendList(String url) {
        holderList.clear();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("success")){
                        progressDialog.dismiss();
                        go_bezzy.setText(object.getString("profile_user_name"));
                        JSONArray array = object.getJSONArray("friend_list");
                        for(int i=0;i<array.length();i++){
                            JSONObject object1 = array.getJSONObject(i);
                            holderObj = new FriendsHolder(object1.getString("friend_id"),object1.getString("name"),object1.getString("image"),object1.getString("user_relation_status"));
                            holderList.add(holderObj);
                        }
                        MyFriendsAdapter adapter = new MyFriendsAdapter(FriendsFriendList.this,holderList,"2");
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
        });

        RequestQueue queue = Volley.newRequestQueue(FriendsFriendList.this);
        queue.add(request);
    }
}