package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.model.ChatMessageModel;
import com.bezzy_application.R;

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
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.send_massage_item, parent, false);
            return new SentMassageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recived_massage_item, parent, false);
            return new ReceiveMassageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       /* UserMessage message = (UserMessage) mMessageList.get(position);

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
        return 0;
    }
   /* @Override
    public int getItemViewType(int position) {
        UserMessage message = (UserMessage) mMessageList.get(position);

        if (message.getSender().getUserId().equals(SendBird.getCurrentUser().getUserId())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }*/


    private class ReceiveMassageHolder extends RecyclerView.ViewHolder{

        public ReceiveMassageHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    private class SentMassageHolder extends RecyclerView.ViewHolder{

        public SentMassageHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
