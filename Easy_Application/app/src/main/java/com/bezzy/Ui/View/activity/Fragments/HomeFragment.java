package com.bezzy.Ui.View.activity.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.LoginActivity;
import com.bezzy.Ui.View.activity.NotificationActivity;
import com.bezzy.Ui.View.activity.Profile;
import com.bezzy.Ui.View.adapter.Friendsfeed_Adapter;
import com.bezzy.Ui.View.adapter.Friendsnoti_adapter;
import com.bezzy.Ui.View.adapter.Search_adapter;
import com.bezzy.Ui.View.model.Friendsfeed_item;
import com.bezzy.Ui.View.model.Friendsnoti_item;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




public class HomeFragment extends Fragment {


    ArrayList<Friendsnoti_item> dataholder;
    RecyclerView recyclerView;
    Friendsnoti_item ob1;
    Search_adapter adapter;
    FrameLayout noti;
    TextView cart_badge,go_bezzy;
    ArrayList<Friendsfeed_item> friendsfeed_items;
    ImageView chatButton;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

       /* progressDialog = new SpotsDialog(getActivity(),R.style.Custom);
        progressDialog.setCancelable(false);*/
        // Inflate the layout for this fragment

        recyclerView=view.findViewById(R.id.friendsnoti_listf);
        noti = view.findViewById(R.id.noti);
        cart_badge = view.findViewById(R.id.cart_badge);
        go_bezzy = view.findViewById(R.id.go_bezzy);
        chatButton = view.findViewById(R.id.chatButton);

        if(Utility.getNotificationStatus(getActivity().getApplicationContext()).equals("1")){
            cart_badge.setVisibility(View.VISIBLE);
        }else{
            cart_badge.setVisibility(View.GONE);
        }

        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout = view.findViewById(R.id.containers);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mSwipeRefreshLayout.setRefreshing(false);

                if(Utility.internet_check(getActivity())) {

                    //progressDialog.show();
                    Utility.displayLoader(getActivity());

                    friendsBlockList(APIs.BASE_URL+APIs.FRIENDSBLOCKLIST+"/"+Utility.getUserId(getActivity()));

                }
                else {

                    //progressDialog.dismiss();
                    Utility.hideLoader(getActivity());

                    Toast.makeText(getActivity(),"No Network!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dataholder=new ArrayList<>();
        friendsfeed_items = new ArrayList<>();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        callCheckNotificationBadge();

        if(Utility.internet_check(getActivity())) {

            //progressDialog.show();
            Utility.displayLoader(getActivity());

            friendsBlockList(APIs.BASE_URL+APIs.FRIENDSBLOCKLIST+"/"+Utility.getUserId(getActivity()));

        }
        else {

            //progressDialog.dismiss();
            Utility.hideLoader(getActivity());

            Toast.makeText(getActivity(),"No Network!",Toast.LENGTH_SHORT).show();
        }
    }

    private void callCheckNotificationBadge() {

        try{
            if(Utility.getNotificationStatus(getActivity().getApplicationContext()).equals("1")){
                cart_badge.setVisibility(View.VISIBLE);
            }else{
                cart_badge.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Log.e("Exception",e.toString());
        }

        refresh(20000);
    }

    private void refresh(int i) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                callCheckNotificationBadge();
            }
        };

        handler.postDelayed(runnable,i);
    }

    private void friendsBlockList(String url) {
        friendsfeed_items.clear();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response",response);
                try {
                    JSONObject object1 = new JSONObject(response);
                    String status = object1.getString("status");
                    if(status.equals("success")){
                        //progressDialog.dismiss();
                        Utility.hideLoader(getActivity());
                        go_bezzy.setText("Friend's List");
                        JSONArray array = object1.getJSONObject("total_feed_response").getJSONArray("friend_list");
                        for(int i = 0;i<array.length();i++){
                            JSONObject object11 = array.getJSONObject(i);
                            Friendsfeed_item item = new Friendsfeed_item(object11.getString("friend_id"),
                                    object11.getString("friend_name"),
                                    object11.getString("friend_photo"),
                                    object11.getString("past_post_days"),
                                    object11.getString("today_post"),
                                    object11.getString("unread_post_number"));
                            friendsfeed_items.add(item);
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(new Friendsfeed_Adapter(getActivity(),friendsfeed_items));
                    }else{
                        Utility.hideLoader(getActivity());
                        go_bezzy.setText("Add Friends");
                        if(Utility.internet_check(getActivity().getApplicationContext())) {

                            //progressDialog.show();
                            Utility.displayLoader(getActivity());

                            Log.e("Result","1");

                            registerUserResult(APIs.BASE_URL+APIs.REGISTERUSERLIST);

                        }
                        else {

                            //progressDialog.dismiss();
                            Utility.hideLoader(getActivity());

                            Toast.makeText(getActivity(),"No Network!",Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.hideLoader(getActivity());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideLoader(getActivity());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(request);
    }

    private void registerUserResult(String url) {
        dataholder.clear();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response);
               // progressDialog.dismiss();


                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("success")){
                        Utility.hideLoader(getActivity());
                        JSONArray array = object.getJSONArray("all_user_list");
                        for(int i = 0 ;i<array.length();i++){
                            JSONObject object1 = array.getJSONObject(i);

                            ob1 = new Friendsnoti_item(object1.getString("name"),object1.getString("user_bio"),object1.getString("image"),object1.getString("user_id"),object1.getString("user_is_flollowers"));

                            dataholder.add(ob1);

                        }

                        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),3);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        adapter = new Search_adapter(dataholder,getActivity());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    Log.e("Exception",e.toString());
                    e.printStackTrace();
                    Utility.hideLoader(getActivity());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Exception",error.toString());
                //progressDialog.dismiss();
                Utility.hideLoader(getActivity());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("log_userID", Utility.getUserId(getActivity()));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

}