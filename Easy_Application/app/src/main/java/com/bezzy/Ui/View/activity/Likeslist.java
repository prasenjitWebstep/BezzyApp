package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.adapter.LikesName_Adapter;
import com.bezzy.Ui.View.model.Likes_name;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Likeslist extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Likes_name> dataholder;
    String post_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likeslist);
        recyclerView=findViewById(R.id.like_listnames);
        post_id= getIntent().getExtras().getString("postId");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        dataholder=new ArrayList<>();


        if(Utility.internet_check(Likeslist.this)) {

            //dialog.show();
            /*progressDialog.show();*/
            Utility.displayLoader(Likeslist.this);
            //Log.e("Result","1");

            Likesname(APIs.BASE_URL+APIs.LIKES_NAME+"/"+post_id);

        }
        else {

            //dialog.dismiss();
            /*progressDialog.dismiss();*/
            Utility.hideLoader(Likeslist.this);
            Toast.makeText(Likeslist.this,"No Network!",Toast.LENGTH_SHORT).show();
        }
    }
    private void Likesname(String url){
        dataholder.clear();
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response);
                try {
                    JSONObject object=new JSONObject(response);
                    String status=object.getString("status");
                    if (status.equals("success")){
                        JSONArray array=object.getJSONArray("userlist");
                        Utility.hideLoader(Likeslist.this);
                        for(int i=0;i<array.length();i++){
                            JSONObject object11=array.getJSONObject(i);
                            Likes_name obj = new Likes_name();
                           String id = object11.getString("id");
                           String username=object11.getString("name");
                           String user_image=object11.getString("profilePicture");


                           obj.setId(id);
                           obj.setUsername(username);
                            obj.setUser_image(user_image);
                           dataholder.add(obj);
                        }
                        LikesName_Adapter adapter=new LikesName_Adapter(Likeslist.this,dataholder);
                        recyclerView.setAdapter(adapter);


                    }
                    else {
                        Utility.hideLoader(Likeslist.this);
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
                Utility.hideLoader(Likeslist.this);

            }
        });
        RequestQueue queue= Volley.newRequestQueue(Likeslist.this);
        queue.add(request);



    }
}