package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.CommentActivity;
import com.bezzy.Ui.View.activity.Friendsfeed;
import com.bezzy.Ui.View.activity.ImageDisplayActivity;
import com.bezzy.Ui.View.activity.Likeslist;
import com.bezzy.Ui.View.activity.PostImageVideoViewActivity;
import com.bezzy.Ui.View.model.FriendsPostModel;
import com.bezzy.Ui.View.model.FriendsPostModelImage;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;
import com.potyvideo.library.AndExoPlayerView;
import com.rishabhharit.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class FriendsPostAdapter extends RecyclerView.Adapter<FriendsPostAdapter.FriendsPostHolder> {

    Context context;
    ArrayList<FriendsPostModel> friendsModelList;
    FriendsPostModelImage postModelObj;
    ArrayList<FriendsPostModelImage> postModelList = new ArrayList<>();
    FriendsImagePostAdapter postImageObj;
    private static AlertDialog topupDialog;
    String postId;


    public FriendsPostAdapter(Context context, ArrayList<FriendsPostModel> friendsModelList) {
        this.context = context;
        this.friendsModelList = friendsModelList;
    }

    @NonNull
    @Override
    public FriendsPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendsPostHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.friendsfeed_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendsPostHolder holder, final int position) {

        holder.title_text.setText(friendsModelList.get(position).getName());

        Glide.with(context)
                .load(friendsModelList.get(position).getPhoto())
                .into(holder.img_logo);

        holder.descrip.setText(friendsModelList.get(position).getPostTime());

        if(friendsModelList.get(position).getUserLikeStatus().equals("No")){
            holder.favBtn.setVisibility(View.VISIBLE);
            holder.favBtnfilled.setVisibility(View.INVISIBLE);
        }else{
            holder.favBtn.setVisibility(View.INVISIBLE);
            holder.favBtnfilled.setVisibility(View.VISIBLE);
        }

        if(!friendsModelList.get(position).getContents().equals("") || !friendsModelList.get(position).getContents().equals("null") || !friendsModelList.get(position).getContents().equals(null)){
            holder.post_status.setVisibility(View.VISIBLE);
            holder.post_status.setText(Html.fromHtml(friendsModelList.get(position).getContents(), Html.FROM_HTML_MODE_COMPACT));
        }else{
            holder.post_status.setVisibility(View.GONE);
        }

        holder.following_num.setText(friendsModelList.get(position).getNumber_of_likes());

        holder.following_numm.setText(friendsModelList.get(position).getNumber_of_comment());

        if(friendsModelList.get(position).getPost_type().equals("video")){
            holder.videoDisp.setVisibility(View.VISIBLE);
            holder.imageDisp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.fullscreenDialog(context,friendsModelList.get(position).getPost_id());
                    /*Intent intent = new Intent(context, PostImageVideoViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("postId",friendsModelList.get(position).getPost_id());
                    context.startActivity(intent);*/
                }
            });
            holder.imageShow.setVisibility(View.GONE);
            holder.recyclerImageShow.setVisibility(View.GONE);
            JSONArray array = friendsModelList.get(position).getPost_image_video();
            for(int i=0; i<array.length(); i++){
                try {
                    JSONObject object = array.getJSONObject(i);
                    Glide.with(context)
                            .load(object.getString("post_url"))
                            .into(holder.imageDisp);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Exception",e.toString());
                }
            }
        }else if(friendsModelList.get(position).getPost_type().equals("image")){
            JSONArray array = friendsModelList.get(position).getPost_image_video();
            int size = array.length();
            if(size<2){
                holder.cardHolder.setVisibility(View.VISIBLE);
                holder.imageShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utility.fullscreenDialog(context,friendsModelList.get(position).getPost_id());
                        /*Intent intent = new Intent(context, PostImageVideoViewActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("postId",friendsModelList.get(position).getPost_id());
                        context.startActivity(intent);*/
                    }
                });
                holder.recyclerImageShow.setVisibility(View.GONE);
                holder.videoDisp.setVisibility(View.GONE);
                for(int i=0;i<array.length();i++){

                    try {
                        JSONObject object = array.getJSONObject(i);
                        Glide.with(context)
                                .load(object.getString("post_url"))
                                .into(holder.imageShow);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Exception",e.toString());
                    }

                }

            }else{
                holder.recyclerImageShow.setVisibility(View.VISIBLE);
                holder.imageShow.setVisibility(View.GONE);
                holder.videoDisp.setVisibility(View.GONE);
                GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
                /*layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);*/
                holder.recyclerImageShow.setLayoutManager(layoutManager);
                postModelList.clear();
                for(int i=0;i<array.length();i++){

                    try {
                        JSONObject object = array.getJSONObject(i);
                        postModelObj = new FriendsPostModelImage(object.getString("post_url"),friendsModelList.get(position).post_id,object.getString("post_type"));
                        postModelList.add(postModelObj);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Exception",e.toString());
                    }

                }

                postImageObj = new FriendsImagePostAdapter(context,postModelList);
                holder.recyclerImageShow.setAdapter(postImageObj);
            }


        }

        if(holder.favBtn.getVisibility() == View.VISIBLE){
            holder.favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Called","1");
                    holder.favBtnfilled.setVisibility(View.VISIBLE);
                    holder.favBtn.setVisibility(View.INVISIBLE);
                    if(Utility.internet_check(context)) {

                        friendsPostLike(APIs.BASE_URL+APIs.LIKEPOST+"/"+Utility.getUserId(context)+"/"+friendsModelList.get(position).getPost_id(),holder.following_num);

                    }
                    else {

                        Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        holder.favBtnfilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Called","2");
                holder.favBtnfilled.setVisibility(View.INVISIBLE);
                holder.favBtn.setVisibility(View.VISIBLE);
                if(Utility.internet_check(context)) {

                    friendsPostLike(APIs.BASE_URL+APIs.LIKEPOST+"/"+Utility.getUserId(context)+"/"+friendsModelList.get(position).getPost_id(),holder.following_num);

                }
                else {

                    Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("postId",friendsModelList.get(position).getPost_id());
                context.startActivity(intent);
            }
        });

        holder.following_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Likeslist.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("postId",friendsModelList.get(position).getPost_id());
                context.startActivity(intent);
            }
        });
        /*holder.imageShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageDisplayActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("postId",friendsModelList.get(position).getPost_id());
                context.startActivity(intent);
            }
        });*/

    }

    private void friendsPostLike(String url, final TextView following_num) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*Log.e("Response",response);*/
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        String number = object.getJSONObject("activity").getString("number_of_activity");
                        following_num.setText(number);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    @Override
    public int getItemCount() {
        return friendsModelList.size();
    }

    public  class FriendsPostHolder extends RecyclerView.ViewHolder{

        CircleImageView img_logo;
        TextView title_text,post_status,following_num,following_numm,descrip;
        ImageView favBtn,favBtnfilled,chat_btn,imageDisp,imageShow;
        RecyclerView recyclerImageShow;
        CardView videoDisp;
        CardView cardHolder;

        public FriendsPostHolder(@NonNull View itemView) {
            super(itemView);
            img_logo = itemView.findViewById(R.id.img_logo);
            title_text = itemView.findViewById(R.id.title_text);
            post_status = itemView.findViewById(R.id.post_status);
            following_num = itemView.findViewById(R.id.following_num);
            following_numm = itemView.findViewById(R.id.following_numm);
            favBtn = itemView.findViewById(R.id.favBtn);
            favBtnfilled = itemView.findViewById(R.id.favBtnfilled);
            chat_btn = itemView.findViewById(R.id.chat_btn);
            recyclerImageShow = itemView.findViewById(R.id.recyclerImageShow);
            imageShow = itemView.findViewById(R.id.imageShow);
            descrip = itemView.findViewById(R.id.descrip);
            imageDisp = itemView.findViewById(R.id.imageDisp);
            videoDisp = itemView.findViewById(R.id.videoDisp);
            cardHolder = itemView.findViewById(R.id.cardHolder);
        }
    }
}
