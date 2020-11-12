package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.model.Friendsfeed_item;
import com.bezzy.Ui.View.model.Notification_item;
import com.bezzy_application.R;
import com.potyvideo.library.AndExoPlayerView;

import java.util.ArrayList;


public class Friendsfeed_Adapter extends RecyclerView.Adapter<Friendsfeed_Adapter.FriendsfeedViewHolder>{
    Context context;
    ArrayList<Friendsfeed_item> dataholder;

    public Friendsfeed_Adapter(Context context, ArrayList<Friendsfeed_item> dataholder) {
        this.context = context;
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public FriendsfeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.friendsfeed_item,parent,false);

        return new Friendsfeed_Adapter.FriendsfeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsfeedViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public class FriendsfeedViewHolder extends RecyclerView.ViewHolder {
        TextView descrip;
        ImageView imageView;
        AndExoPlayerView video_view;



        public FriendsfeedViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
