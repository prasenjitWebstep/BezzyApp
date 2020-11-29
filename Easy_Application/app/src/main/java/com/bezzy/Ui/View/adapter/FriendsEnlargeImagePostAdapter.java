package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.activity.PostImageVideoViewActivity;
import com.bezzy.Ui.View.model.FriendsPostModelImage;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FriendsEnlargeImagePostAdapter extends RecyclerView.Adapter<FriendsEnlargeImagePostAdapter.FriendsEnlargeImagePostHolder> {

    Context context;
    ArrayList<FriendsPostModelImage> postList;

    public FriendsEnlargeImagePostAdapter(Context context, ArrayList<FriendsPostModelImage> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public FriendsEnlargeImagePostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendsEnlargeImagePostHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.posted_photo_enlarge_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsEnlargeImagePostHolder holder, int position) {

        Glide.with(context)
                .load(postList.get(position).getPostUrl())
                .into(holder.imageDisp);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    public class FriendsEnlargeImagePostHolder extends RecyclerView.ViewHolder{

        ImageView imageDisp;

        public FriendsEnlargeImagePostHolder(@NonNull View itemView) {
            super(itemView);
            imageDisp = itemView.findViewById(R.id.imageDisp);
        }
    }

}
