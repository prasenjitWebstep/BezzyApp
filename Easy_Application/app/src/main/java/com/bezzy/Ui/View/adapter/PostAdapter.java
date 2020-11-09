package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.activity.Fragments.VideoDisplayActivity;
import com.bezzy.Ui.View.activity.ImageDisplayActivity;
import com.bezzy.Ui.View.model.PostModel;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    ArrayList<PostModel> postItems;
    Context context;

    public PostAdapter(ArrayList<PostModel> postItems, Context context) {
        this.postItems = postItems;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_container,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {

        for(PostModel model : postItems){

            Log.e("Image",model.getImage());

        }

        Glide.with(context)
                .load(postItems.get(position).getImage())
                .into(holder.imageDisp);

        holder.imageDisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postItems.get(position).getType().equals("image")){
                    Intent intent = new Intent(context, ImageDisplayActivity.class);
                    intent.putExtra("id",postItems.get(position).getId());
                    intent.putExtra("postId",postItems.get(position).getPostId());
                    intent.putExtra("type",postItems.get(position).getType());
                    context.startActivity(intent);
                }
                else  {
                    Intent intent = new Intent(context, VideoDisplayActivity.class);
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
        VideoView videoView;

        public PostViewHolder(@NonNull View itemView) {

            super(itemView);

            imageDisp = itemView.findViewById(R.id.imageDisp);
            videoView=itemView.findViewById(R.id.videoDidp);

        }
    }
}

