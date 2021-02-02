package com.bezzy.Ui.View.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.model.ChatMessageModel;
import com.bezzy_application.R;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.ViewPropertyTransition;

import java.util.ArrayList;

public class Chatbox_adapter extends RecyclerView.Adapter<Chatbox_adapter.ReceiveMassageHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private Context mContext;
    private ArrayList<ChatMessageModel> mMessageList;
    private AlertDialog fullscreenDialog;

    public Chatbox_adapter(Context mContext, ArrayList<ChatMessageModel> mMessageList) {
        this.mContext = mContext;
        this.mMessageList = mMessageList;
    }

    public void setData(ArrayList<ChatMessageModel> newData) {
        this.mMessageList.clear();
        mMessageList.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReceiveMassageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ReceiveMassageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiveMassageHolder holder, final int position) {
        if(mMessageList.get(position).getMessage_by().equals("self")){
            if(mMessageList.get(position).getType().equals("text")){
                holder.layoutSender.setVisibility(View.VISIBLE);
                holder.layoutSenderImage.setVisibility(View.GONE);
                holder.send_message_body.setText(mMessageList.get(position).getChat_message());
                holder.send_message_time.setText(mMessageList.get(position).getChat_date_time());
                if(mMessageList.get(position).getChat_read_unread_status().equals("2")){
                    holder.send_tick.setVisibility(View.VISIBLE);
                    holder.delivered_tick.setVisibility(View.INVISIBLE);
                }else{
                    holder.send_tick.setVisibility(View.INVISIBLE);
                    holder.delivered_tick.setVisibility(View.VISIBLE);
                }
            }else{
                holder.layoutSender.setVisibility(View.GONE);
                holder.layoutSenderImage.setVisibility(View.VISIBLE);

                ViewPropertyTransition.Animator animationObject = new ViewPropertyTransition.Animator() {
                    @Override
                    public void animate(View view) {
                        view.setAlpha(0f);

                        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                        fadeAnim.setDuration(2500);
                        fadeAnim.start();
                    }
                };

                Glide.with(mContext)
                        .load(mMessageList.get(position).getChat_message())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transition(GenericTransitionOptions.with(animationObject))
                        .into(holder.send_message_image);

                holder.send_message_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImageFullScreenDialog(mMessageList.get(position).getChat_message());
                    }
                });

                holder.send_message_time.setText(mMessageList.get(position).getChat_date_time());
                if(mMessageList.get(position).getChat_read_unread_status().equals("2")){
                    holder.send_tick.setVisibility(View.VISIBLE);
                    holder.delivered_tick.setVisibility(View.INVISIBLE);
                }else{
                    holder.send_tick.setVisibility(View.INVISIBLE);
                    holder.delivered_tick.setVisibility(View.VISIBLE);
                }
            }

        }else{

            if(mMessageList.get(position).getType().equals("text")){
                holder.layoutreceiver.setVisibility(View.VISIBLE);
                holder.layoutreceiverimage.setVisibility(View.GONE);
                holder.rcv_message_body.setText(mMessageList.get(position).getChat_message());
                holder.rcv_message_time.setText(mMessageList.get(position).getChat_date_time());
            }else{
                holder.layoutreceiver.setVisibility(View.GONE);
                holder.layoutreceiverimage.setVisibility(View.VISIBLE);

                ViewPropertyTransition.Animator animationObject = new ViewPropertyTransition.Animator() {
                    @Override
                    public void animate(View view) {
                        view.setAlpha(0f);

                        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                        fadeAnim.setDuration(2500);
                        fadeAnim.start();
                    }
                };

                Glide.with(mContext)
                        .load(mMessageList.get(position).getChat_message())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transition(GenericTransitionOptions.with(animationObject))
                        .into(holder.send_message_image);

                holder.send_message_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImageFullScreenDialog(mMessageList.get(position).getChat_message());
                    }
                });

                holder.rcv_image_time.setText(mMessageList.get(position).getChat_date_time());
            }
        }

        String date[] = mMessageList.get(position).getDate().split(" ");

        Toast toast= Toast.makeText(mContext,
                ""+date[0], Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 150);
        toast.show();
    }

    private void showImageFullScreenDialog(String chat_message) {

        final ImageView imageShow,rotateIcon;

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MaterialTheme);
        View v= LayoutInflater.from(mContext).inflate(R.layout.imagedisplay_layout,null);
        imageShow = v.findViewById(R.id.imageShow);

        ViewPropertyTransition.Animator animationObject = new ViewPropertyTransition.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha(0f);

                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(2500);
                fadeAnim.start();
            }
        };


        Glide.with(mContext)
                .load(chat_message)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(animationObject))
                .into(imageShow);


        builder.setView(v);
        builder.setCancelable(true);
        fullscreenDialog=builder.create();
        fullscreenDialog.show();

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
        TextView rcv_message_body,rcv_message_time,send_message_body,send_message_time,rcv_image_time;
        RelativeLayout layoutreceiver,layoutSender,layoutreceiverimage,layoutSenderImage;
        ImageView send_tick,delivered_tick,send_message_image;


        public ReceiveMassageHolder(@NonNull View itemView) {
            super(itemView);
            layoutreceiver = itemView.findViewById(R.id.layoutreceiver);
            layoutSender = itemView.findViewById(R.id.layoutSender);
            rcv_message_body = itemView.findViewById(R.id.rcv_message_body);
            rcv_message_time = itemView.findViewById(R.id.rcv_message_time);
            send_message_body = itemView.findViewById(R.id.send_message_body);
            send_message_time = itemView.findViewById(R.id.send_message_time);
            send_tick = itemView.findViewById(R.id.send_tick);
            delivered_tick = itemView.findViewById(R.id.delivered_tick);
            layoutreceiverimage = itemView.findViewById(R.id.layoutreceiverimage);
            layoutSenderImage = itemView.findViewById(R.id.layoutSenderImage);
            send_message_image = itemView.findViewById(R.id.send_message_image);
            rcv_image_time = itemView.findViewById(R.id.rcv_image_time);

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

    @Override
    public int getItemViewType(int position) {

        if(mMessageList.get(position).getMessage_by().equals("self")){
            return R.layout.sender_message_layout;
        }else{
            return R.layout.recived_massage_item;
        }
    }

}
