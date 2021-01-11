package com.bezzy.Ui.View.activity.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.adapter.Chatlist_adater;
import com.bezzy.Ui.View.model.Chatlist_item;
import com.bezzy.Ui.View.model.Notification_item;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {
    RecyclerView chatlist;
    ArrayList<Chatlist_item> dataholder;
    ProgressDialog progressDialog;
    ProgressBar progressBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        chatlist=view.findViewById(R.id.chatlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        chatlist.setLayoutManager(linearLayoutManager);
        progressBar = view.findViewById(R.id.progressBar);

        dataholder=new ArrayList<>();
        /*if(Utility.internet_check(getActivity())) {




            chatNotiList(APIs.BASE_URL+APIs.CHAT_NOTI_LIST+"/"+Utility.getUserId(getActivity()));

        }
        else {



            Toast.makeText(getActivity(),"No Network!",Toast.LENGTH_SHORT).show();
        }*/
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        dataholder=new ArrayList<>();
        if(Utility.internet_check(getActivity())) {

            checkToken(APIs.BASE_URL+APIs.MEMBER_TOKEN+"/"+Utility.getUserId(getActivity()));

        }
        else {
            Toast.makeText(getActivity(),"No Network!",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkToken(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    try{

                        if(Utility.getUserToken(getActivity()).equals(object.getString("remember_token"))){

                            if(Utility.internet_check(getActivity())) {

                                progressBar.setVisibility(View.VISIBLE);
                                chatNotiList(APIs.BASE_URL+APIs.CHAT_NOTI_LIST+"/"+Utility.getUserId(getActivity()));

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(),"No Network!",Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Utility.logoutFunction(getActivity());
                        }

                    }catch (Exception e){
                        Log.e("Exception",e.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void chatNotiList(String url){
        dataholder.clear();
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.e("Response",response);
                try {
                    JSONObject object=new JSONObject(response);
                    String status=object.getString("status");
                    if (status.equals("success")){
                        JSONArray array=object.getJSONArray("chat_notification_list");
                        for (int i=0;i<array.length();i++){
                            JSONObject object11=array.getJSONObject(i);
                            Chatlist_item chatlist_item=new Chatlist_item(
                                    object11.getString("friendID"),
                                    object11.getString("username"),
                                    object11.getString("chat_message"),
                                    object11.getString("chat_date_time"),
                                    object11.getString("unread_msg"),
                                    object11.getString("userimage"),
                                    object11.getString("user_active_status")
                            );
                            dataholder.add(chatlist_item);
                            progressBar.setVisibility(View.GONE);

                        }
                        chatlist.setAdapter(new Chatlist_adater(dataholder, getActivity()));


                    }else{
                        progressBar.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
                progressBar.setVisibility(View.GONE);

            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(request);
    }
}