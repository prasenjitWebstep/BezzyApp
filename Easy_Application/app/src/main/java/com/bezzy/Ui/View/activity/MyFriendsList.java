package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
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

public class MyFriendsList extends AppCompatActivity {

    RecyclerView recyclerFriendsList;
    ProgressDialog progressDialog;
    FriendsHolder holderObj;
    ArrayList<FriendsHolder> holderList;
    TextView go_bezzy;
    String friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends_list);

        recyclerFriendsList = findViewById(R.id.recyclerFriendsList);
        go_bezzy = findViewById(R.id.go_bezzy);
       /* progressDialog = new ProgressDialog(MyFriendsList.this);
        progressDialog.setMessage("Loading Please Wait...");
        progressDialog.setCancelable(false);*/
        holderList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(MyFriendsList.this);
        recyclerFriendsList.setLayoutManager(layoutManager);

        if(Utility.internet_check(MyFriendsList.this)) {

            //dialog.show();
           // progressDialog.show();
            Utility.displayLoader(MyFriendsList.this);
            Log.e("Result","1");

            friendList(APIs.BASE_URL+APIs.FOLLOWERSLIST);

        }
        else {

            //dialog.dismiss();
           // progressDialog.dismiss();
            Utility.hideLoader(MyFriendsList.this);
            Toast.makeText(MyFriendsList.this,"No Network!",Toast.LENGTH_SHORT).show();
        }

    }

    private void friendList(String url) {
        holderList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response);

                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("success")){
                        //progressDialog.dismiss();
                        Utility.hideLoader(MyFriendsList.this);
                        go_bezzy.setText(object.getString("login_user_name"));
                        JSONArray array = object.getJSONArray("follower_user_list");
                        for(int i=0;i<array.length();i++){
                            JSONObject object1 = array.getJSONObject(i);
                            holderObj = new FriendsHolder(object1.getString("following_user_id"),object1.getString("name"),object1.getString("image"),"");
                            holderList.add(holderObj);
                        }
                        MyFriendsAdapter adapter = new MyFriendsAdapter(MyFriendsList.this,holderList,"1");
                        recyclerFriendsList.setAdapter(adapter);
                    }else{
                        //progressDialog.dismiss();
                        Utility.hideLoader(MyFriendsList.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Utility.hideLoader(MyFriendsList.this);
                Log.e("Exception",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("loguser_id",Utility.getUserId(MyFriendsList.this));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(MyFriendsList.this);
        queue.add(request);
    }
}