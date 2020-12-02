package com.bezzy.Ui.View.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bezzy.Ui.View.activity.Fragments.HomeFragment;
import com.bezzy.Ui.View.activity.FriendsProfileActivity;
import com.bezzy.Ui.View.activity.Massage;
import com.bezzy.Ui.View.activity.Profile;
import com.bezzy.Ui.View.model.Friendsnoti_item;
import com.bezzy.Ui.View.model.Searchnoti_item;
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

public class Search_adapter extends RecyclerView.Adapter<Search_adapter.searchViewHolder>  {
    ArrayList<Friendsnoti_item> dataholder;
    Context context;
    ProgressDialog progressDialog;

    public Search_adapter(ArrayList<Friendsnoti_item> dataholder, Context context) {
        this.dataholder = dataholder;
        this.context = context;
    }

    @NonNull
    @Override
    public searchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.searchlist_item,parent,false);

        return new searchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull searchViewHolder holder, final int position) {
        Glide.with(context).load(dataholder.get(position).getImg()).into(holder.img);
        holder.header.setText(dataholder.get(position).getHeader());
        if(!dataholder.get(position).getDesc().equalsIgnoreCase("NULL") || !dataholder.get(position).getDesc().equals(null)){
            holder.bio.setVisibility(View.VISIBLE);
            holder.bio.setText(dataholder.get(position).getDesc());
        }else{
            holder.bio.setVisibility(View.INVISIBLE);
        }



        if(dataholder.get(position).getUser_relation_status().equals("1")){
            holder.chat.setVisibility(View.VISIBLE);
            holder.addFriend.setVisibility(View.INVISIBLE);
            holder.chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Massage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("FrndId",dataholder.get(position).getId());
                    intent.putExtra("userImage",dataholder.get(position).getImg());
                    intent.putExtra("userName",dataholder.get(position).getHeader());
                    context.startActivity(intent);
                }
            });
        }else{
            holder.addFriend.setVisibility(View.VISIBLE);
            holder.chat.setVisibility(View.INVISIBLE);
            holder.addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = new ProgressDialog(context);
                    //progressDialog.setMessage("Please Wait...");
                    progressDialog.setCancelable(false);
                    callApiFollow(APIs.BASE_URL+APIs.FOLLOWINGREQUEST,dataholder.get(position).getId(),position);


                }
            });
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FriendsProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("friendId",dataholder.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    private void callApiFollow(String url, final String id, final int position) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    String sucess = object.getString("status");
                    if(sucess.equals("success")){
                        progressDialog.dismiss();
                        Toast.makeText(context,object.getString("message"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Profile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else{
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("Exception",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("user_one_id",Utility.getUserId(context));
                map.put("user_two_id",id);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }


    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public  class searchViewHolder extends RecyclerView.ViewHolder{
         CircleImageView img;
         TextView header,bio;
         ImageView addFriend,chat;

        public searchViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img_logo);
            header=itemView.findViewById(R.id.title_text);
            addFriend = itemView.findViewById(R.id.addFriend);
            chat = itemView.findViewById(R.id.chat);
            bio=itemView.findViewById(R.id.bio_text);
        }
    }

}
