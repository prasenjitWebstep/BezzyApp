package com.bezzy.Ui.View.activity.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.adapter.Notifi_Adapter;
import com.bezzy.Ui.View.model.Friendsnoti_item;
import com.bezzy.Ui.View.model.Notification_item;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class NotificationFragment extends Fragment {
    ArrayList<Notification_item> dataholder;
    RecyclerView recyclerView;
    Notification_item ob;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView=view.findViewById(R.id.noti_listf);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        dataholder=new ArrayList<>();
        show(APIs.BASE_URL+APIs.NOTIFICATION+"/"+ Utility.getUserId(getContext()));
        return view;

    }

    private void show(String url){
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RSESS",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONArray array = object.getJSONArray("notification_list");
                        Log.e("ARRAY",array.toString());
                        for(int i=0;i<array.length();i++){
                            JSONObject object1 = array.getJSONObject(i);
                            Log.e("OBJECT",object1.toString());

                            ob = new Notification_item(object1.getString("activity_message"));
                            dataholder.add(ob);

                            //String time = object1.getString("created_at");
                            //textView.setText(object1.getString("created_at"));
//                            Date current = Calendar.getInstance().getTime();
//                            Log.e("TIME",current.toString());

                            Calendar rightnow = Calendar.getInstance();
                            int current = rightnow.get(Calendar.HOUR_OF_DAY); //24 hrs
                            int twelve = rightnow.get(Calendar.HOUR); //12 hr

                            //if()
                            //check using if else one hour
                        }
                        recyclerView.setAdapter(new Notifi_Adapter(getContext(),dataholder));
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
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}