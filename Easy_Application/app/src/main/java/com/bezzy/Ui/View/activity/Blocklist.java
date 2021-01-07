package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.bezzy.Ui.View.adapter.Blocklist_Adapter;
import com.bezzy.Ui.View.adapter.FollowingAdapter;
import com.bezzy.Ui.View.adapter.MyFriendsAdapter;
import com.bezzy.Ui.View.model.FriendsHolder;
import com.bezzy.Ui.View.model.Unblockholders;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class Blocklist extends AppCompatActivity {
    RecyclerView recyclerblockList;
    Unblockholders holderObj;
    ArrayList<Unblockholders> holderList;
    TextView go_bezzy;
    String friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocklist);
        recyclerblockList = findViewById(R.id.recyclerblockList);
        go_bezzy = findViewById(R.id.go_bezzy);

        holderList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(Blocklist.this);
        recyclerblockList.setLayoutManager(layoutManager);

//        if(Utility.internet_check(Blocklist.this)) {
//
//            //dialog.show();
//            // progressDialog.show();
//            //Utility.displayLoader(Blocklist.this);
//            Log.e("Result","1");
//
//            //friendList(APIs.BASE_URL+APIs.FOLLOWERSLIST);
//
//        }
//        else {
//
//            //dialog.dismiss();
//            // progressDialog.dismiss();
//            Utility.hideLoader(Blocklist.this);
//            Toast.makeText(Blocklist.this,"No Network!",Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utility.internet_check(Blocklist.this)) {

            //dialog.show();
            /*progressDialog.show();*/
            Utility.displayLoader(Blocklist.this);
            Log.e("Result","1");

            blockList(APIs.BASE_URL+APIs.BLOCKLIST);

        }
        else {

            //dialog.dismiss();
            /*progressDialog.dismiss();*/
            Utility.hideLoader(Blocklist.this);
            Toast.makeText(Blocklist.this,"No Network!",Toast.LENGTH_SHORT).show();
        }
    }


    private void blockList(String url){
        holderList.clear();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.e("DDDDD", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if (resp.equals("success")) {
                        Utility.hideLoader(Blocklist.this);
                        //go_bezzy.setText(object.getString("login_user_name"));
                        JSONArray array = object.getJSONArray("block_user_list");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object1 = array.getJSONObject(i);
                            holderObj = new Unblockholders(object1.getString("block_user_id"), object1.getString("name"), object1.getString("image"), "");
                            holderList.add(holderObj);
                        }
                        Blocklist_Adapter adapter = new Blocklist_Adapter(Blocklist.this, holderList, "1");
                        recyclerblockList.setAdapter(adapter);
                    } else {
                        //progressDialog.dismiss();
                        Utility.hideLoader(Blocklist.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideLoader(Blocklist.this);
                Log.e("Exception",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("loginuserID",Utility.getUserId(Blocklist.this));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Blocklist.this);
        queue.add(request);
    }



    /* private void friendList(String url) {
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
                        Utility.hideLoader(Blocklist.this);
                        go_bezzy.setText(object.getString("login_user_name"));
                        JSONArray array = object.getJSONArray("follower_user_list");
                        for(int i=0;i<array.length();i++){
                            JSONObject object1 = array.getJSONObject(i);
                            holderObj = new FriendsHolder(object1.getString("following_user_id"),object1.getString("name"),object1.getString("image"),"");
                            holderList.add(holderObj);
                        }
                        MyFriendsAdapter adapter = new MyFriendsAdapter(Blocklist.this,holderList,"1");
                        recyclerblockList.setAdapter(adapter);
                    }else{
                        //progressDialog.dismiss();
                        Utility.hideLoader(Blocklist.this);
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
                Utility.hideLoader(Blocklist.this);
                Log.e("Exception",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("loguser_id",Utility.getUserId(Blocklist.this));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Blocklist.this);
        queue.add(request);
    }*/
}