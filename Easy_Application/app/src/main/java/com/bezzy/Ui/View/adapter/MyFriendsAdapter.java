package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.activity.FriendsProfileActivity;
import com.bezzy.Ui.View.model.FriendsHolder;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFriendsAdapter extends RecyclerView.Adapter<MyFriendsAdapter.MyFriendHoler> {

    Context context;
    ArrayList<FriendsHolder> friendsHolder;

    public MyFriendsAdapter(Context context, ArrayList<FriendsHolder> friendsHolder) {
        this.context = context;
        this.friendsHolder = friendsHolder;
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

    }

    @Override
    public int getItemCount() {
        return friendsHolder.size();
    }

    public class MyFriendHoler extends RecyclerView.ViewHolder{

        CircleImageView circularImg;
        TextView userName;
        Button btn;

        public MyFriendHoler(@NonNull View itemView) {
            super(itemView);
            circularImg = itemView.findViewById(R.id.circularImg);
            userName = itemView.findViewById(R.id.userName);
            btn = itemView.findViewById(R.id.btn);
        }
    }
}
