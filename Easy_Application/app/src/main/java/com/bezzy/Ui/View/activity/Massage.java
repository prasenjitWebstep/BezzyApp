package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    NestedScrollView nestedview;
    int page;
    ProgressBar chatProgress;
    Chatbox_adapter adapter;

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
        nestedview = findViewById(R.id.nestedview);
        chatProgress = findViewById(R.id.chatProgress);
        modelArrayList = new ArrayList<>();
        page = 1;
        linearLayoutManager = new LinearLayoutManager(Massage.this);

        callApi();
        
        title_text.setText(name);
        Glide.with(Massage.this)
                .load(image)
                .into(img_logo);



       /* nestedview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){

                    page++;

                    if(Utility.internet_check(Massage.this)) {


                        chatList(APIs.BASE_URL+APIs.CHAT_LIST+"/"+Utility.getUserId(Massage.this)+"/"+id+"/"+String.valueOf(page));
                    }
                    else {


                        Toast.makeText(Massage.this,"No Network!",Toast.LENGTH_SHORT).show();

                    }


                }
            }
        });*/

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

            chatList(APIs.BASE_URL+APIs.CHAT_LIST+"/"+Utility.getUserId(Massage.this)+"/"+id/*+"/"+String.valueOf(page)*/);
        }
        else {

            Toast.makeText(Massage.this,"No Network!",Toast.LENGTH_SHORT).show();

        }

        refresh(5000);
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

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("ChatResponse",response);

                try {
                    JSONObject object = new JSONObject(response);
                    String sucess = object.getString("status");
                    if(sucess.equals("success")){

                        edittext_chatbox.getText().clear();

                        callApi();

                        /*JSONArray array = object.getJSONArray("chat_history_list");
                        for(int i = 0;i< array.length(); i++){
                            JSONObject object1 = array.getJSONObject(i);
                            messageModel = new ChatMessageModel(object1.getString("message_by"),
                                    object1.getString("chat_message"),
                                    object1.getString("chat_date_time"));
                            modelArrayList.add(messageModel);
                        }
                        reyclerview_message_list.setAdapter(new Chatbox_adapter(Massage.this,modelArrayList));*/
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

        modelArrayList.clear();

        Log.e("URL",url);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("ChatResponse",response);


                try {
                    JSONObject object = new JSONObject(response);
                    String sucess = object.getString("status");
                    if(sucess.equals("success")){
                        JSONArray array = object.getJSONArray("chat_history_list");
                        for(int i = 0;i< array.length(); i++){
                            JSONObject object1 = array.getJSONObject(i);
                            messageModel = new ChatMessageModel(object1.getString("message_by"),
                                    object1.getString("chat_message"),
                                    object1.getString("chat_date_time"));
                            modelArrayList.add(messageModel);
                        }

                        linearLayoutManager.setStackFromEnd(true);
                        reyclerview_message_list.setLayoutManager(linearLayoutManager);
                        adapter = new Chatbox_adapter(Massage.this,modelArrayList);
                        adapter.notifyDataSetChanged();
                        reyclerview_message_list.setAdapter(adapter);
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