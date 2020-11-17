package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.model.FriendsPostModelImage;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FriendsImagePostAdapter extends RecyclerView.Adapter<FriendsImagePostAdapter.FriendsImagePostHolder> {

    Context context;
    ArrayList<FriendsPostModelImage> postList;

    public FriendsImagePostAdapter(Context context, ArrayList<FriendsPostModelImage> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public FriendsImagePostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendsImagePostHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.postedphoto_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsImagePostHolder holder, int position) {

        Glide.with(context)
                .load(postList.get(position).getPostUrl())
                .into(holder.imageDisp);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class FriendsImagePostHolder extends RecyclerView.ViewHolder{

        ImageView imageDisp;

        public FriendsImagePostHolder(@NonNull View itemView) {
            super(itemView);
            imageDisp = itemView.findViewById(R.id.imageDisp);
        }
    }
}
