package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.bezzy.Ui.View.activity.CommentReplyActivity;
import com.bezzy.Ui.View.model.Comment_item;
import com.bezzy.Ui.View.model.Notification_item;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comment_adapter extends RecyclerView.Adapter<Comment_adapter.CommentHolder> {
    Context context;
    ArrayList<Comment_item> dataholder;

    public Comment_adapter(Context context, ArrayList<Comment_item> dataholder) {
        this.context = context;
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.comment_item,parent,false);

        return new Comment_adapter.CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentHolder holder, final int position) {
        holder.username.setText(dataholder.get(position).getUsername());
        holder.time.setText(dataholder.get(position).getPost_comment_time());
        holder.user_comment.setText(dataholder.get(position).getCommentText());

        Glide.with(context)
                .load(dataholder.get(position).getUser_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        holder.following_num.setText(dataholder.get(position).getCommentLikeNo());

        holder.following_numm.setText(dataholder.get(position).getCommentReplyNo());

        if(dataholder.get(position).getUserCommentLikeStatus().equals("No")){
            holder.favBtn.setVisibility(View.VISIBLE);
            holder.favBtnfilled.setVisibility(View.INVISIBLE);
        }else{
            holder.favBtn.setVisibility(View.INVISIBLE);
            holder.favBtnfilled.setVisibility(View.VISIBLE);
        }

        holder.chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentReplyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("image",dataholder.get(position).getUser_image());
                intent.putExtra("name",dataholder.get(position).getUsername());
                intent.putExtra("comment",dataholder.get(position).getCommentText());
                intent.putExtra("time",dataholder.get(position).getPost_comment_time());
                intent.putExtra("id",dataholder.get(position).getCommentid());
                intent.putExtra("postId",dataholder.get(position).getPostID());
                context.startActivity(intent);
            }
        });

        if(holder.favBtn.getVisibility() == View.VISIBLE){
            holder.favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Called","1");
                    holder.favBtnfilled.setVisibility(View.VISIBLE);
                    holder.favBtn.setVisibility(View.INVISIBLE);
                    if(Utility.internet_check(context)) {

                        friendsCommentLike(APIs.BASE_URL+APIs.COMMENTLIKEDISLIKE+"/"+Utility.getUserId(context)+"/"+dataholder.get(position).getCommentid(),holder.following_num);

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
                    friendsCommentLike(APIs.BASE_URL+APIs.COMMENTLIKEDISLIKE+"/"+Utility.getUserId(context)+"/"+dataholder.get(position).getCommentid(),holder.following_num);
                }
                else {

                    Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void friendsCommentLike(String url, final TextView following_num) {
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
        return dataholder.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder{
        TextView username,time,user_comment,following_num,following_numm;
        CircleImageView imageView;
        ImageView favBtn,favBtnfilled,chat_btn;

        public CommentHolder(@NonNull View itemView) {

            super(itemView);
            username=itemView.findViewById(R.id.title_text);
            time=itemView.findViewById(R.id.timeshow);
            imageView=itemView.findViewById(R.id.img_logo);
            user_comment=itemView.findViewById(R.id.comment_user);
            favBtn = itemView.findViewById(R.id.favBtn);
            favBtnfilled = itemView.findViewById(R.id.favBtnfilled);
            chat_btn = itemView.findViewById(R.id.chat_btn);
            following_num = itemView.findViewById(R.id.following_num);
            following_numm = itemView.findViewById(R.id.following_numm);
        }
    }
}
