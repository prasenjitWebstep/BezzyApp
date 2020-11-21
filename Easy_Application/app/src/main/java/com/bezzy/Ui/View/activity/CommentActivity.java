package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.adapter.Comment_adapter;
import com.bezzy.Ui.View.adapter.Notifi_Adapter;
import com.bezzy.Ui.View.model.Comment_item;
import com.bezzy.Ui.View.model.FriendsPostModel;
import com.bezzy.Ui.View.model.Notification_item;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    String id;
    ArrayList<Comment_item> dataholder;
    RecyclerView recyclerView;
    EditText comment;
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        recyclerView=findViewById(R.id.reyclerview_comment_list);
        comment=findViewById(R.id.edittext_comment);
        imageView=findViewById(R.id.send_msg);
        id = getIntent().getExtras().getString("postId");




        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        Utility.setNotificationStatus(this,"0");

        dataholder=new ArrayList<>();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.internet_check(CommentActivity.this)) {

                    //dialog.show();

                    Log.e("Result","1");

                    commentPost(APIs.BASE_URL+APIs.COMMENT_POST);

                }
                else {

                    //dialog.dismiss();

                    Toast.makeText(CommentActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        if(Utility.internet_check(CommentActivity.this)) {

            //dialog.show();

            Log.e("Result","1");

            commentList(APIs.BASE_URL+APIs.COMMENT_LIST);

        }
        else {

            //dialog.dismiss();

            Toast.makeText(CommentActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
        }

    }
    private void commentList(String url) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);

                try {
                    JSONObject object = new JSONObject(response);
                    String status=object.getString("status");
                    if(status.equals("success")){
                        JSONObject object1=object.getJSONObject("comment_list");
                        JSONArray array = object1.getJSONArray("Parent");
                        for(int i=0;i<array.length();i++){

                            JSONObject object11 = array.getJSONObject(i);

                            Comment_item modelObj = new Comment_item(object11.getString("comment_id"),
                                    object11.getString("username"),object11.getString("userimage"),
                                    object11.getString("postcomment_time"),object11.getString("commentText")
                                    );

                            dataholder.add(modelObj);

                        }

                        recyclerView.setAdapter(new Comment_adapter(CommentActivity.this,dataholder));


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();

                map.put("post_id",id);

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(CommentActivity.this);
        queue.add(request);
    }
    private void commentPost(String url) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response);

                try {
                    JSONObject object = new JSONObject(response);
                    String status=object.getString("status");
                    if(status.equals("success")){
                        if(Utility.internet_check(CommentActivity.this)) {

                            //dialog.show();

                            Log.e("Result","1");

                            commentList(APIs.BASE_URL+APIs.COMMENT_LIST);

                        }
                        else {

                            //dialog.dismiss();

                            Toast.makeText(CommentActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
                        }
                        comment.getText().clear();
                        JSONObject object1=object.getJSONObject("comment_user");
                        String name=object1.getString("name");
                        String photo=object1.getString("profilePicture");
                        String comment=object.getString("commentText");



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();

                map.put("userID",Utility.getUserId(CommentActivity.this));
                map.put("PostId",id);
                map.put("commentParentId","0");
                map.put("commentText",comment.getText().toString());

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(CommentActivity.this);
        queue.add(request);
    }
    public void showSoftKeyboard(View view) {
        if(view.requestFocus()){
            InputMethodManager imm =(InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }
    }

}