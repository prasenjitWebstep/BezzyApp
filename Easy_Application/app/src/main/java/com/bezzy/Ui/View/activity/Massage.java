package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.adapter.Chatbox_adapter;
import com.bezzy.Ui.View.model.ChatMessageModel;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Massage extends AppCompatActivity {

    String name,image,id;
    TextView title_text,timeshow;
    CircleImageView img_logo;
    RecyclerView reyclerview_message_list;
    EditText edittext_chatbox;
    ImageView send_msg;
    ChatMessageModel messageModel;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ChatMessageModel> modelArrayList;
    int page,i;
    Chatbox_adapter adapter;
    ProgressBar chatProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massage);

        name = getIntent().getExtras().getString("userName");
        image = getIntent().getExtras().getString("userImage");
        id = getIntent().getExtras().getString("FrndId");
        title_text = findViewById(R.id.title_text);
        timeshow = findViewById(R.id.timeshow);
        img_logo = findViewById(R.id.img_logo);
        reyclerview_message_list = findViewById(R.id.reyclerview_message_list);
        edittext_chatbox = findViewById(R.id.edittext_chatbox);
        send_msg = findViewById(R.id.send_msg);
        chatProgress = findViewById(R.id.chatProgress);

        modelArrayList = new ArrayList<>();
        page = 1;

        linearLayoutManager = new LinearLayoutManager(Massage.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(false);
        reyclerview_message_list.setLayoutManager(linearLayoutManager);

        reyclerview_message_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(-1)) {

                    if(Utility.internet_check(Massage.this)){

                        page++;

                        chatProgress.setVisibility(View.VISIBLE);

                        chatList(APIs.BASE_URL+APIs.CHAT_LIST+"/"+Utility.getUserId(Massage.this)+"/"+id+"/"+String.valueOf(page));


                    }
                    else {
                        chatProgress.setVisibility(View.GONE);
                        Toast.makeText(Massage.this,"No Network!",Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });


        chatList(APIs.BASE_URL+APIs.CHAT_LIST+"/"+Utility.getUserId(Massage.this)+"/"+id+"/"+String.valueOf(page));

        callApi();
        
        title_text.setText(name);
        Glide.with(Massage.this)
                .load(image)
                .into(img_logo);



        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!edittext_chatbox.getText().toString().isEmpty()){
                    try {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    if(Utility.internet_check(Massage.this)) {

                        addchat(APIs.BASE_URL+APIs.ADDCHAT);
                    }
                    else {

                        Toast.makeText(Massage.this,"No Network!",Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });

    }

    private void callApi() {
        if(Utility.internet_check(Massage.this)) {

            if(Utility.internet_check(Massage.this)) {


                instantChat(APIs.BASE_URL+APIs.INSTANT_MSG+"/"+Utility.getUserId(Massage.this)+"/"+id+"/"+"1");
                /*messageStatUpdate(APIs.BASE_URL+APIs.GET_MESSAGE_SEEN);*/
            }
            else {

                Toast.makeText(Massage.this,"No Network!",Toast.LENGTH_SHORT).show();

            }


        }
        else {

            Toast.makeText(Massage.this,"No Network!",Toast.LENGTH_SHORT).show();

        }

        refresh(2000);
    }

    private void instantChat(String url) {

        /*Log.e("URL",url);*/

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                /*Log.e("InstantChatResponse",response);*/

                try {
                    JSONObject object = new JSONObject(response);
                    String sucess = object.getString("status");
                    if(sucess.equals("success")){
                        page = 1;
                        chatList(APIs.BASE_URL+APIs.CHAT_LIST+"/"+Utility.getUserId(Massage.this)+"/"+id+"/"+String.valueOf(page));
                        modelArrayList.clear();

                        /*JSONArray array = object.getJSONArray("chat_history_list");
                        for(int i = 0;i< array.length(); i++){
                            JSONObject object1 = array.getJSONObject(i);
                            messageModel = new ChatMessageModel(object1.getString("message_by"),
                                    object1.getString("chat_message"),
                                    object1.getString("chat_msg_time"),
                                    object1.getString("chat_read_unread_status"));
                            modelArrayList.add(0,messageModel);
                        }
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        reyclerview_message_list.setLayoutManager(linearLayoutManager);
                        adapter = new Chatbox_adapter(Massage.this,modelArrayList);
                        adapter.notifyItemInserted(0);
                        adapter.notifyItemChanged(0);
                        adapter.notifyDataSetChanged();
                        reyclerview_message_list.setAdapter(adapter);*/
                    }

                } catch (JSONException e) {
                    Log.e("Exception",e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error",error.toString());
            }
        });


        RequestQueue queue = Volley.newRequestQueue(Massage.this);
        queue.add(request);

    }


    private void messageStatUpdate(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("loguserID",Utility.getUserId(Massage.this));
                map.put("userID",id);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Massage.this);
        queue.add(request);
    }

    private void refresh(int i) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                callApi();
            }
        };

        handler.postDelayed(runnable,i);
    }


    private void addchat(String url) {

        /*Log.e("URL",url);*/

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                /*Log.e("AddChatResponse",response);*/

                try {
                    JSONObject object = new JSONObject(response);
                    String sucess = object.getString("status");
                    if(sucess.equals("success")){

                        edittext_chatbox.getText().clear();

                        JSONArray array = object.getJSONArray("chat_history_list");
                        for(int i = 0;i< array.length(); i++){
                            JSONObject object1 = array.getJSONObject(i);
                            messageModel = new ChatMessageModel(object1.getString("message_by"),
                                    object1.getString("chat_message"),
                                    object1.getString("chat_msg_time"),
                                    object1.getString("chat_read_unread_status"));
                            modelArrayList.add(0,messageModel);
                        }
                        linearLayoutManager = new LinearLayoutManager(Massage.this);
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setSmoothScrollbarEnabled(true);
                        reyclerview_message_list.setLayoutManager(linearLayoutManager);
                        adapter = new Chatbox_adapter(Massage.this,modelArrayList);
                        adapter.notifyItemInserted(0);
                        adapter.notifyItemChanged(0);
                        adapter.notifyDataSetChanged();
                        reyclerview_message_list.setAdapter(adapter);

                        /*JSONArray array = object.getJSONArray("chat_history_list");
                        for(int i = 0;i< array.length(); i++){
                            JSONObject object1 = array.getJSONObject(i);
                            messageModel = new ChatMessageModel(object1.getString("message_by"),
                                    object1.getString("chat_message"),
                                    object1.getString("chat_date_time"));
                            modelArrayList.add(messageModel);
                        }
                        reyclerview_message_list.setAdapter(new Chatbox_adapter(Massage.this,modelArrayList));*//*

                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        reyclerview_message_list.setLayoutManager(linearLayoutManager);
                        adapter = new Chatbox_adapter(Massage.this,modelArrayList);
                        adapter.notifyItemInserted(0);
                        adapter.notifyDataSetChanged();
                        reyclerview_message_list.setAdapter(adapter);*/

                    }

                } catch (JSONException e) {
                    Log.e("Exception",e.toString());
                    e.printStackTrace();
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
                map.put("from_userID",Utility.getUserId(Massage.this));
                map.put("to_userID",id);
                map.put("chat_message",edittext_chatbox.getText().toString());
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Massage.this);
        queue.add(request);
    }

    private void chatList(String url) {

        Log.e("URL",url);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                /*Log.e("ChatResponse",response);*/

                try {
                    JSONObject object = new JSONObject(response);
                    String sucess = object.getString("status");
                    if(sucess.equals("success")){
                        chatProgress.setVisibility(View.GONE);
                        JSONArray array = object.getJSONArray("chat_history_list");
                        for(int i = 0;i< array.length(); i++){
                            JSONObject object1 = array.getJSONObject(i);
                            messageModel = new ChatMessageModel(object1.getString("message_by"),
                                    object1.getString("chat_message"),
                                    object1.getString("chat_msg_time"),
                                    object1.getString("chat_read_unread_status"));
                            modelArrayList.add(modelArrayList.size(),messageModel);
                        }

                        if(page == 1){
                            Log.e("Called","If()");
                            linearLayoutManager = new LinearLayoutManager(Massage.this);
                            linearLayoutManager.setReverseLayout(true);
                            linearLayoutManager.setStackFromEnd(true);
                            linearLayoutManager.setSmoothScrollbarEnabled(true);
                            reyclerview_message_list.setLayoutManager(linearLayoutManager);
                        }
                        adapter = new Chatbox_adapter(Massage.this,modelArrayList);
                        adapter.notifyDataSetChanged();
                        reyclerview_message_list.setAdapter(adapter);
                    }else{
                        chatProgress.setVisibility(View.GONE);
                        Toast.makeText(Massage.this,"End of List",Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Log.e("Exception",e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error",error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(Massage.this);
        queue.add(request);
    }

}