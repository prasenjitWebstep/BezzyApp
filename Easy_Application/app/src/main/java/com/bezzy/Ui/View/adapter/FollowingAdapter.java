package com.bezzy.Ui.View.adapter;

import android.app.ProgressDialog;
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
import com.bezzy.Ui.View.activity.FollowingActivity;
import com.bezzy.Ui.View.activity.FriendsFriendList;
import com.bezzy.Ui.View.activity.FriendsProfileActivity;
import com.bezzy.Ui.View.activity.Massage;
import com.bezzy.Ui.View.activity.MyFriendsList;
import com.bezzy.Ui.View.activity.Profile;
import com.bezzy.Ui.View.activity.Registration;
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
import dmax.dialog.SpotsDialog;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.MyFriendHoler> {

    Context context;
    ArrayList<FriendsHolder> friendsHolder;
    String screen;

    public FollowingAdapter(Context context, ArrayList<FriendsHolder> friendsHolder,String screen) {
        this.context = context;
        this.friendsHolder = friendsHolder;
        this.screen = screen;
    }

    @NonNull
    @Override
    public MyFriendHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyFriendHoler(LayoutInflater.from(parent.getContext()).inflate(R.layout.followingfriends_layout,parent,false));


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

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.internet_check(context)) {

                   /* progressDialog = new SpotsDialog(context);
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();*/
                    Utility.displayLoader(context);

                    unfollow(APIs.BASE_URL+APIs.UNFOLLOW,friendsHolder.get(position).getFriendId());


                }
                else {

                    //progressDialog.dismiss();
                    Utility.hideLoader(context);
                    Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();

                }

            }
        });
        holder.btn_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.internet_check(context)) {

                    Utility.displayLoader(context);

                    block(APIs.BASE_URL+APIs.BLOCK,friendsHolder.get(position).getFriendId());


                }
                else {

                    //progressDialog.dismiss();
                    Utility.hideLoader(context);
                    Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void unfollow(String url, final String friendId) {
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response",response);
                //progressDialog.dismiss();
                Utility.hideLoader(context);
                try {
                    JSONObject object=new JSONObject(response);
                    String status=object.getString("status");
                    if (status.equals("success")){
                        Toast.makeText(context,object.getString("alert_msg"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, FollowingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);

                    }
                } catch (JSONException e) {
                    //progressDialog.dismiss();
                    Utility.hideLoader(context);
                    e.printStackTrace();
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Utility.hideLoader(context);
                Log.e("Exception",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();

                map.put("loginUserID",Utility.getUserId(context));
                map.put("unblockuserID",friendId);


                return map;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(request);
    }
    private void block(String url,final String s){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response);

                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("status");
                    if (resp.equals("success")) {
                        Utility.hideLoader(context);
                        Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, FollowingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }else{
                        Utility.hideLoader(context);
                    }
                } catch (JSONException e) {
                    Utility.hideLoader(context);
                    e.printStackTrace();
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideLoader(context);
                Log.e("Exception",error.toString());
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                HashMap<String,String> map = new HashMap<>();
                map.put("loginUserID",Utility.getUserId(context));
                map.put("blockuserID",s);
                Log.e("GETID",map.get("blockuserID"));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);

    }


    @Override
    public int getItemCount() {
        return friendsHolder.size();
    }

    public class MyFriendHoler extends RecyclerView.ViewHolder{

        CircleImageView circularImg;
        TextView userName;
        TextView btn,btn_block;
        ImageView addFriend,chat;

        public MyFriendHoler(@NonNull View itemView) {
            super(itemView);
            circularImg = itemView.findViewById(R.id.circularImg);
            userName = itemView.findViewById(R.id.userName);
            btn = itemView.findViewById(R.id.btn);
            btn_block=itemView.findViewById(R.id.btn_block);
            addFriend = itemView.findViewById(R.id.addFriend);
            chat = itemView.findViewById(R.id.chat);

        }
    }
}
