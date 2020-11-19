package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.model.Chatlist_item;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chatlist_adater extends RecyclerView.Adapter<Chatlist_adater.ChatListHolder>{
    private ArrayList<Chatlist_item> chatholder;
    Context context;

    public Chatlist_adater(ArrayList<Chatlist_item> chatholder, Context context) {
        this.chatholder = chatholder;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);
        return  new ChatListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListHolder holder, int position) {
        Chatlist_item chatlistItem=chatholder.get(position);
        holder.tvName.setText(chatlistItem.getUserName());
        holder.tvDate.setText(chatlistItem.getDate());
        holder.tvLastmsg.setText(chatlistItem.getLastmsg());
        Glide.with(context).load(chatlistItem.getUrlProfile()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return chatholder.size();
    }

    public class ChatListHolder extends RecyclerView.ViewHolder{
        private TextView tvName,tvLastmsg,tvDate;
        private CircleImageView image;

        public ChatListHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=itemView.findViewById(R.id.date);
            tvLastmsg=itemView.findViewById(R.id.lastmsg);
            tvName=itemView.findViewById(R.id.user_name);
            image=itemView.findViewById(R.id.chat_image);






        }
    }
}
