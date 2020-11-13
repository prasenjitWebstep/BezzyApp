package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.model.Friendsfeed_item;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friendsfeed_Adapter extends RecyclerView.Adapter<Friendsfeed_Adapter.FriendsfeedHolder> {

    Context context;
    ArrayList<Friendsfeed_item> friendList;

    public Friendsfeed_Adapter(Context context, ArrayList<Friendsfeed_item> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendsfeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendsfeedHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.firendblockview_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsfeedHolder holder, int position) {

        Glide.with(context)
                .load(friendList.get(position).getFriendPhoto())
                .into(holder.circularImg);

        holder.userName.setText(friendList.get(position).getFriendName());

        if(!friendList.get(position).getFriendPostDays().equals("")){
            holder.oldPost.setVisibility(View.VISIBLE);
            holder.userPost.setVisibility(View.VISIBLE);
            holder.userPost.setText("Posted "+friendList.get(position).getFriendPostDays()+" days ago");
            holder.userPost.setTextColor(Color.parseColor("#f1b45c"));
        }

        if(!friendList.get(position).getTodayPost().equals("")){
            holder.noti.setVisibility(View.VISIBLE);
            holder.postBadge.setText(friendList.get(position).getTodayPost());
            holder.userPost.setVisibility(View.VISIBLE);
            holder.userPost.setText(friendList.get(position).getTodayPost()+" new Posts");
            holder.userPost.setTextColor(Color.parseColor("#f93f07"));
        }


    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class FriendsfeedHolder extends RecyclerView.ViewHolder{

        CircleImageView circularImg;
        TextView userName,userPost,postBadge;
        FrameLayout noti;
        ImageView video,oldPost;

        public FriendsfeedHolder(@NonNull View itemView) {
            super(itemView);
            circularImg = itemView.findViewById(R.id.circularImg);
            userName = itemView.findViewById(R.id.userName);
            userPost = itemView.findViewById(R.id.userPost);
            postBadge = itemView.findViewById(R.id.postBadge);
            noti = itemView.findViewById(R.id.noti);
            video = itemView.findViewById(R.id.video);
            oldPost = itemView.findViewById(R.id.oldPost);
        }
    }
}
