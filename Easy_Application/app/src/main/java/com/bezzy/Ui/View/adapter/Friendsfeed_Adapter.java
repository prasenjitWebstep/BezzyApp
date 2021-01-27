package com.bezzy.Ui.View.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.FriendsProfileActivity;
import com.bezzy.Ui.View.activity.Friendsfeed;
import com.bezzy.Ui.View.activity.Massage;
import com.bezzy.Ui.View.model.FriendsPostModel;
import com.bezzy.Ui.View.model.Friendsfeed_item;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
//import dmax.dialog.SpotsDialog;

public class Friendsfeed_Adapter extends RecyclerView.Adapter<Friendsfeed_Adapter.FriendsfeedHolder> {

    Context context;
    ArrayList<Friendsfeed_item> friendList;
    ArrayList<FriendsPostModel> postModelList = new ArrayList<>();
    FriendsPostAdapter adapterPost;
    //SpotsDialog progressDialog;
    String cardId;
    String setClick = "0";
    int mExpandedPosition = -1;
    //int i;
    String tempId ;
    int flag = 0;


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
    public void onBindViewHolder(@NonNull final FriendsfeedHolder holder, final int position) {

        Glide.with(context)
                .load(friendList.get(position).getFriendPhoto())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.circularImg);

        holder.circularImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FriendsProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("friendId",friendList.get(position).getFriendId());
                intent.putExtra("screen","1");
                context.startActivity(intent);
            }
        });

        holder.userName.setText(friendList.get(position).getFriendName());

        if(!friendList.get(position).getFriendPostDays().equals("")){
            holder.oldPost.setVisibility(View.VISIBLE);
            holder.noti.setVisibility(View.INVISIBLE);
            holder.video.setVisibility(View.INVISIBLE);
            holder.userPost.setVisibility(View.VISIBLE);
            if(friendList.get(position).getFriendPostDays().equals("1")){
                holder.userPost.setText("Posted "+friendList.get(position).getFriendPostDays()+" day ago");
            }else{
                holder.userPost.setText("Posted "+friendList.get(position).getFriendPostDays()+" days ago");
            }

            holder.userPost.setTextColor(Color.parseColor("#f1b45c"));
        }

        if(!friendList.get(position).getUnreadPostNumber().equals("") || !friendList.get(position).getTodayPost().equals("")){
            holder.noti.setVisibility(View.VISIBLE);
            holder.oldPost.setVisibility(View.INVISIBLE);
            holder.video.setVisibility(View.INVISIBLE);
            if(!friendList.get(position).getUnreadPostNumber().equals("")){
                holder.postBadge.setVisibility(View.VISIBLE);
                holder.postBadge.setText(friendList.get(position).getUnreadPostNumber());
            }else{
                holder.postBadge.setVisibility(View.INVISIBLE);
            }
            holder.userPost.setVisibility(View.VISIBLE);
            if(friendList.get(position).getTodayPost().equals("1")){
                holder.userPost.setText(friendList.get(position).getTodayPost()+" New Post");
            }else {
                holder.userPost.setText(friendList.get(position).getTodayPost()+" New Posts");
            }

            holder.userPost.setTextColor(Color.parseColor("#f93f07"));
        }

        if(friendList.get(position).getFriendPostDays().equals("") && friendList.get(position).getUnreadPostNumber().equals("") && friendList.get(position).getTodayPost().equals("")){
            holder.noti.setVisibility(View.INVISIBLE);
            holder.oldPost.setVisibility(View.INVISIBLE);
            holder.video.setVisibility(View.INVISIBLE);
            holder.userPost.setVisibility(View.INVISIBLE);
        }

        final boolean isExpanded = position==mExpandedPosition;
        holder.friendsPostCards.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);



        if(isExpanded){
            Utility.displayLoader(context);
            holder.friendsPostCards.setVisibility(View.VISIBLE);
            friendsPostList(APIs.BASE_URL+APIs.FRIENDPOSTLIST+"/"+friendList.get(position).getFriendId()+"/"+Utility.getUserId(context),friendList.get(position).getFriendName(),friendList.get(position).getFriendPhoto(),holder.frds_feed);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mExpandedPosition = isExpanded ? -1:position;
                notifyDataSetChanged();

            }
        });

        holder.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Massage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("FrndId",friendList.get(position).getFriendId());
                intent.putExtra("userImage",friendList.get(position).getFriendPhoto());
                intent.putExtra("userName",friendList.get(position).getFriendName());
                context.startActivity(intent);
            }
        });


        /*holder.friendsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*Intent intent = new Intent(context, Friendsfeed.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id",friendList.get(position).getFriendId());
                intent.putExtra("name",friendList.get(position).getFriendName());
                intent.putExtra("image",friendList.get(position).getFriendPhoto());
                context.startActivity(intent);*//*


                if(holder.friendsPostLayout.getVisibility() == View.VISIBLE){

                    holder.friendsPostLayout.setVisibility(View.GONE);

                }
            }
        });*/


    }

    private void friendsPostList(String url, final String friendName, final String friendPhoto, final RecyclerView frds_feed) {
        postModelList.clear();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               /* Log.e("Response",response);*/

                try {
                    JSONObject object1 = new JSONObject(response);
                    String status = object1.getString("status");
                    if(status.equals("success")){
                        //progressDialog.dismiss();
                        Utility.hideLoader(context);
                        JSONArray array = object1.getJSONArray("post_details");
                        for(int i=0;i<array.length();i++){

                            JSONObject object11 = array.getJSONObject(i);

                            FriendsPostModel modelObj = new FriendsPostModel(object11.getString("post_id"),
                                    object11.getString("post_type"),object11.getString("number_of_like"),
                                    object11.getString("number_of_comment"),friendName,
                                    friendPhoto,object11.getString("post_content"),object11.getString("log_user_like_status"),
                                    object11.getString("post_time"),object11.getJSONArray("post_img_video_live"));

                            postModelList.add(modelObj);

                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        frds_feed.setLayoutManager(linearLayoutManager);
                        adapterPost = new FriendsPostAdapter(context,postModelList);
                        frds_feed.setAdapter(adapterPost);
                    }else{
                       // progressDialog.dismiss();
                        Utility.hideLoader(context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressDialog.dismiss();
                    Utility.hideLoader(context);
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Utility.hideLoader(context);
                Log.e("Error",error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class FriendsfeedHolder extends RecyclerView.ViewHolder{

        ImageView circularImg;
        TextView userName,userPost,postBadge;
        FrameLayout noti;
        ImageView video,oldPost,chatButton;
        CardView friendsCard;
        CardView friendsPostCards;
        RecyclerView frds_feed;

        public FriendsfeedHolder(@NonNull View itemView) {
            super(itemView);
            circularImg = itemView.findViewById(R.id.square_img);
            userName = itemView.findViewById(R.id.userName);
            userPost = itemView.findViewById(R.id.userPost);
            postBadge = itemView.findViewById(R.id.postBadge);
            noti = itemView.findViewById(R.id.noti);
            video = itemView.findViewById(R.id.video);
            oldPost = itemView.findViewById(R.id.oldPost);
            friendsCard = itemView.findViewById(R.id.friendsCard);
            friendsPostCards = itemView.findViewById(R.id.friendsPostCards);
            frds_feed = itemView.findViewById(R.id.frds_feed);
            chatButton = itemView.findViewById(R.id.chatButton);
        }
    }
}
