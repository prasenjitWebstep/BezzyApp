package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.FriendsFriendList;
import com.bezzy.Ui.View.activity.FriendsProfileActivity;
import com.bezzy.Ui.View.activity.Massage;
import com.bezzy.Ui.View.activity.MyFriendsList;
import com.bezzy.Ui.View.activity.Profile;
import com.bezzy.Ui.View.model.FriendsHolder;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFriendsAdapter extends RecyclerView.Adapter<MyFriendsAdapter.MyFriendHoler> {

    Context context;
    ArrayList<FriendsHolder> friendsHolder;
    String screen;

    public MyFriendsAdapter(Context context, ArrayList<FriendsHolder> friendsHolder,String screen) {
        this.context = context;
        this.friendsHolder = friendsHolder;
        this.screen = screen;
    }

    @NonNull
    @Override
    public MyFriendHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyFriendHoler(LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_layout,parent,false));


    }

    @Override
    public void onBindViewHolder(@NonNull MyFriendHoler holder, final int position) {

        Glide.with(context)
                .load(friendsHolder.get(position).getImage())
                .into(holder.circularImg);

        holder.userName.setText(friendsHolder.get(position).getName());

        holder.circularImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FriendsProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("friendId",friendsHolder.get(position).getFriendId());
                intent.putExtra("screen","2");
                context.startActivity(intent);
            }
        });

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FriendsProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("friendId",friendsHolder.get(position).getFriendId());
                intent.putExtra("screen","2");
                context.startActivity(intent);
            }
        });

        if(screen.equals("1")){
            Log.e("Screen","1");
            holder.btn.setVisibility(View.VISIBLE);
            holder.addFriend.setVisibility(View.GONE);
            holder.chat.setVisibility(View.GONE);
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Utility.internet_check(context)) {

                        unfriend(APIs.BASE_URL+APIs.UNFRIEND,friendsHolder.get(position).getFriendId());


                    }
                    else {


                        Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }else{
            Log.e("Screen","2");
            Log.e("GETVAL",friendsHolder.get(position).getUser_relation_status());
            holder.btn.setVisibility(View.GONE);
            holder.addFriend.setVisibility(View.VISIBLE);
            holder.chat.setVisibility(View.VISIBLE);
            if(friendsHolder.get(position).getUser_relation_status().equals("1")){
                Log.e("CHECK","VISIBLE OR NOT");
                holder.chat.setVisibility(View.VISIBLE);
                holder.addFriend.setVisibility(View.INVISIBLE);
                holder.chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Massage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("FrndId",friendsHolder.get(position).getFriendId());
                        intent.putExtra("userImage",friendsHolder.get(position).getImage());
                        intent.putExtra("userName",friendsHolder.get(position).getName());
                        context.startActivity(intent);
                    }
                });
            }else{
                Log.e("CHECK","VISIBLE OR NOT2");
                holder.addFriend.setVisibility(View.VISIBLE);
                holder.chat.setVisibility(View.INVISIBLE);
                holder.addFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        callApiFriendRequest(APIs.BASE_URL+APIs.FRIENDREQUEST,friendsHolder.get(position).getFriendId(),position);

                    }
                });
            }
        }

    }

    private void unfriend(String url, final String friendId) {
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response",response);
                try {
                    JSONObject object=new JSONObject(response);
                    String status=object.getString("status");
                    if (status.equals("success")){
                        Toast.makeText(context,object.getString("alert_msg"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MyFriendsList.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);

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

                map.put("loginUserID",Utility.getUserId(context));
                map.put("unfriendID",friendId);


                return map;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(request);
    }

    private void callApiFriendRequest(String url, final String id, final int position) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    String sucess = object.getString("status");
                    if(sucess.equals("success")){
                        Toast.makeText(context,object.getString("message"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, FriendsFriendList.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
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
                map.put("user_one_id", Utility.getUserId(context));
                map.put("user_two_id",id);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    @Override
    public int getItemCount() {
        return friendsHolder.size();
    }

    public class MyFriendHoler extends RecyclerView.ViewHolder{

        CircleImageView circularImg;
        TextView userName;
        Button btn;
        ImageView addFriend,chat;

        public MyFriendHoler(@NonNull View itemView) {
            super(itemView);
            circularImg = itemView.findViewById(R.id.circularImg);
            userName = itemView.findViewById(R.id.userName);
            btn = itemView.findViewById(R.id.btn);
            addFriend = itemView.findViewById(R.id.addFriend);
            chat = itemView.findViewById(R.id.chat);

        }
    }
}
