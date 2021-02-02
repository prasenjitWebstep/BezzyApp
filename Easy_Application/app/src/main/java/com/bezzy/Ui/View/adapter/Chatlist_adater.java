package com.bezzy.Ui.View.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.Massage;
import com.bezzy.Ui.View.activity.MyFriendsList;
import com.bezzy.Ui.View.model.Chatlist_item;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.ViewPropertyTransition;

import org.json.JSONException;
import org.json.JSONObject;

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
    public void onBindViewHolder(@NonNull ChatListHolder holder, final int position) {
        final Chatlist_item chatlistItem=chatholder.get(position);
        holder.tvName.setText(chatlistItem.getUserName());
        if(chatlistItem.getActiveStatus().equals("true")){
            holder.imgActive.setVisibility(View.VISIBLE);
        }else{
            holder.imgActive.setVisibility(View.GONE);
        }
        if(!chatlistItem.getUnreadmsg().equals("0")){
            holder.tvunread.setVisibility(View.VISIBLE);
            holder.tvunread.setText(chatlistItem.getUnreadmsg());
        }else{
            holder.tvunread.setVisibility(View.GONE);
        }

        String s[] = chatlistItem.getDate().split(" ",2);
        String date = s[0];
        holder.tvDate.setText(date);
        holder.tvLastmsg.setText(chatlistItem.getLastmsg());

        ViewPropertyTransition.Animator animationObject = new ViewPropertyTransition.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha(0f);

                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(2500);
                fadeAnim.start();
            }
        };

        Glide.with(context)
                .load(chatlistItem.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(animationObject))
                .into(holder.image);

        holder.relativeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Clicked","1");

                if(Utility.internet_check(context)) {

                    Utility.displayLoader(context);

                    readChatNoti(APIs.BASE_URL+APIs.CHAT_NOTIFICATION_READ+"/"+Utility.getUserId(context)+"/"+chatlistItem.getUserID(),chatlistItem.getUserID(),chatlistItem.getImage(),chatlistItem.getUserName(),chatlistItem.getActiveStatus());

                }
                else {

                    Utility.hideLoader(context);

                    Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    private void readChatNoti(final String url, final String userID, final String image, final String name, final String activeStatus) {
        /*Log.e("URL",url);*/
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                /*Log.e("Response",response);*/

                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        Utility.hideLoader(context);
                        Intent intent = new Intent(context, Massage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("FrndId",userID);
                        intent.putExtra("userImage",image);
                        intent.putExtra("userName",name);
                        intent.putExtra("online",activeStatus);
                        context.startActivity(intent);

                    }else{
                        Utility.hideLoader(context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.hideLoader(context);
                    /*Log.e("Exception",e.toString());*/
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utility.hideLoader(context);
                /*Log.e("Error",error.toString());*/

            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    @Override
    public int getItemCount() {
        return chatholder.size();
    }

    public class ChatListHolder extends RecyclerView.ViewHolder{
        private TextView tvName,tvLastmsg,tvDate,tvunread;
        private CircleImageView image;
        RelativeLayout relativeHolder;
        ImageView imgActive;

        public ChatListHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=itemView.findViewById(R.id.date);
            tvLastmsg=itemView.findViewById(R.id.lastmsg);
            tvName=itemView.findViewById(R.id.user_name);
            tvunread=itemView.findViewById(R.id.msg_number);
            image=itemView.findViewById(R.id.chat_image);
            relativeHolder = itemView.findViewById(R.id.relativeHolder);
            imgActive = itemView.findViewById(R.id.imgActive);




        }
    }
}
