package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.activity.Fragments.ProfileFragment;
import com.bezzy.Ui.View.activity.ImageDisplayActivity;
import com.bezzy.Ui.View.activity.Profile;
import com.bezzy.Ui.View.activity.VideoDisplayActivity;
import com.bezzy.Ui.View.model.FriendsPostModelImage;
import com.bezzy.Ui.View.model.PostModel;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;
import com.potyvideo.library.AndExoPlayerView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    ArrayList<PostModel> postItems;
    Context context;
    String screen;

    public PostAdapter(ArrayList<PostModel> postItems, Context context, String screen) {
        this.postItems = postItems;
        this.context = context;
        this.screen = screen;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_container,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {


        Glide.with(context)
                .load(postItems.get(position).getImage())
                .into(holder.imageDisp);

        if(postItems.get(position).getType().equals("video")){
            holder.play.setVisibility(View.VISIBLE);
        }else {
            holder.play.setVisibility(View.GONE);
        }

        holder.imageDisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postItems.get(position).getType().equals("image")){

                    Intent intent = new Intent(context, ImageDisplayActivity.class);
                    intent.putExtra("screen",screen);
                    intent.putExtra("id",postItems.get(position).getId());
                    intent.putExtra("postId",postItems.get(position).getPostId());
                    intent.putExtra("type",postItems.get(position).getType());
                    context.startActivity(intent);
                }
                else  {
                    Intent intent = new Intent(context, VideoDisplayActivity.class);
                    intent.putExtra("screen",screen);
                    intent.putExtra("id", postItems.get(position).getId());
                    intent.putExtra("postId", postItems.get(position).getPostId());
                    intent.putExtra("type", postItems.get(position).getType());
                    context.startActivity(intent);

                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return postItems.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        ImageView imageDisp;
        TextView play;

        public PostViewHolder(@NonNull View itemView) {

            super(itemView);

            imageDisp = itemView.findViewById(R.id.imageDisp);
            play = itemView.findViewById(R.id.play);

        }
    }
}

