package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.model.ChatMessageModel;
import com.bezzy_application.R;
import com.like.Utils;

import java.util.ArrayList;
import java.util.List;

public class Chatbox_adapter extends RecyclerView.Adapter {
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
       /* if (viewType == mMessageList.get(viewType).getMessage_by().equals("self")) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.send_massage_item, parent, false);
            return new SentMassageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recived_massage_item, parent, false);
            return new ReceiveMassageHolder(view);
        }*/

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        /*if (mMessageList.get(position).getMessage_by().equals("self")){
        }
       *//* UserMessage message = (UserMessage) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMassageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceiveMassageHolder) holder).bind(message);
        }*/

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
    @Override
    public int getItemViewType(int position) {
        /*UserMessage message = (UserMessage) mMessageList.get(position);

        if (message.getSender().getUserId().equals(SendBird.getCurrentUser().getUserId())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }*/
        return  position;
    }


    private class ReceiveMassageHolder extends RecyclerView.ViewHolder{
        TextView rcvmsg,rcvtime;


        public ReceiveMassageHolder(@NonNull View itemView) {
            super(itemView);
            rcvmsg=itemView.findViewById(R.id.rcv_message_body);
            rcvtime=itemView.findViewById(R.id.rcv_message_time);
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
    private class SentMassageHolder extends RecyclerView.ViewHolder{
        TextView sendmsg,sendtime;

        public SentMassageHolder(@NonNull View itemView) {

            super(itemView);
            sendmsg=itemView.findViewById(R.id.text_message_body);
            sendtime=itemView.findViewById(R.id.text_message_time);
        }
    }

}
