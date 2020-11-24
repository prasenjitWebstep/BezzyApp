package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bezzy.Ui.View.model.ChatMessageModel;
import com.bezzy_application.R;

import java.util.ArrayList;

public class Chatbox_adapter extends RecyclerView.Adapter<Chatbox_adapter.ReceiveMassageHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private Context mContext;
    private ArrayList<ChatMessageModel> mMessageList;

    public Chatbox_adapter(Context mContext, ArrayList<ChatMessageModel> mMessageList) {
        this.mContext = mContext;
        this.mMessageList = mMessageList;
    }

    @NonNull
    @Override
    public ReceiveMassageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recived_massage_item, parent, false);
        return new ReceiveMassageHolder(view);
        /*view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recived_massage_item, parent, false);
        return new ReceiveMassageHolder(view);*/
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiveMassageHolder holder, int position) {
        if(mMessageList.get(position).getMessage_by().equals("self")){
            holder.layoutSender.setVisibility(View.VISIBLE);
            holder.send_message_body.setText(mMessageList.get(position).getChat_message());
        }else{
            holder.layoutreceiver.setVisibility(View.VISIBLE);
            holder.rcv_message_body.setText(mMessageList.get(position).getChat_message());

        }
    }

    /*@Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(mMessageList.get(position).getMessage_by().equals("self")){

        }

        *//*if (mMessageList.get(position).getMessage_by().equals("self")){
        }
       *//**//* UserMessage message = (UserMessage) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMassageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceiveMassageHolder) holder).bind(message);
        }*//*

    }*/

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }



    public class ReceiveMassageHolder extends RecyclerView.ViewHolder{
        TextView rcv_message_body,rcv_message_time,send_message_body,send_message_time;
        LinearLayout layoutreceiver,layoutSender;


        public ReceiveMassageHolder(@NonNull View itemView) {
            super(itemView);
            layoutreceiver = itemView.findViewById(R.id.layoutreceiver);
            layoutSender = itemView.findViewById(R.id.layoutSender);
            rcv_message_body = itemView.findViewById(R.id.rcv_message_body);
            rcv_message_time = itemView.findViewById(R.id.rcv_message_time);
            send_message_body = itemView.findViewById(R.id.send_message_body);
            send_message_time = itemView.findViewById(R.id.send_message_time);
        }

        /*void bind(UserMessage message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
            nameText.setText(message.getSender().getNickname());

            // Insert the profile image from the URL into the ImageView.
            Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);

        }*/
    }

    /*private class SentMassageHolder extends RecyclerView.ViewHolder{
        TextView sendmsg,sendtime;

        public SentMassageHolder(@NonNull View itemView) {

            super(itemView);
            sendmsg=itemView.findViewById(R.id.text_message_body);
            sendtime=itemView.findViewById(R.id.text_message_time);
        }
    }*/

    /*@Override
    public int getItemViewType(int position) {

        if(mMessageList.get(position).getMessage_by().equals("self")){
            return R.layout.send_massage_item;
        }else{
            return R.layout.recived_massage_item;
        }
    }*/

}
