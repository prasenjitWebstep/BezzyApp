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

    private static AlertDialog fullscreenDialog;
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
                /*if(postItems.get(position).getType().equals("image")){
                    fullscreenDialog(postItems.get(position).getId(), postItems.get(position).getPostId(), postItems.get(position).getType(), context);
                    *//*Intent intent = new Intent(context, ImageDisplayActivity.class);
                    intent.putExtra("id",postItems.get(position).getId());
                    intent.putExtra("postId",postItems.get(position).getPostId());
                    intent.putExtra("type",postItems.get(position).getType());
                    context.startActivity(intent);*//*
                }
                else  {
                    Intent intent = new Intent(context, VideoDisplayActivity.class);
                    intent.putExtra("id", postItems.get(position).getId());
                    intent.putExtra("postId", postItems.get(position).getPostId());
                    intent.putExtra("type", postItems.get(position).getType());
                    context.startActivity(intent);

                }*/

                fullscreenDialog(postItems.get(position).getId(),postItems.get(position).getPostId(),postItems.get(position).getType(),context);
            }
        });



    }

    public static void fullscreenDialog(String id, String postId, String type, final Context context){

        AndExoPlayerView andExoPlayerView;
        ImageView imageShow,fav_btn,favBtnfilled,delete_image;
        RecyclerView recyclerImageShow;
        TextView servicesText,following_num,following_numm;
        ArrayList<FriendsPostModelImage> postModelList;

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MaterialTheme);
        View v= LayoutInflater.from(context).inflate(R.layout.activity_image_display,null);
        andExoPlayerView = v.findViewById(R.id.andExoPlayerView);
        imageShow = v.findViewById(R.id.imageShow);
        recyclerImageShow = v.findViewById(R.id.recyclerImageShow);
        servicesText = v.findViewById(R.id.servicesText);
        following_num = v.findViewById(R.id.following_num);
        following_numm = v.findViewById(R.id.following_numm);
        fav_btn = v.findViewById(R.id.fav_btn);
        favBtnfilled = v.findViewById(R.id.favBtnfilled);
        delete_image = v.findViewById(R.id.delete_image);
        postModelList = new ArrayList<>();

        /*if(Utility.internet_check(context)) {

            //progressDialog.show();
            Utility.displayLoader(context);
            friendsPostLargeView(APIs.BASE_URL+APIs.FRIENDSBLOCKDETAILS+"/"+post_id+"/"+Utility.getUserId(context),context,
                    andExoPlayerView,imageShow,
                    recyclerImageShow,servicesText,
                    following_num,following_numm,
                    fav_btn,favBtnfilled,postModelList);

        }
        else {

            //progressDialog.dismiss();
            Utility.hideLoader(context);

            Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();
        }*/

        delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullscreenDialog.dismiss();
                Utility.globalData = "1";
                Intent intent = new Intent(context, ProfileFragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);
            }
        });


        builder.setView(v);
        builder.setCancelable(true);
        fullscreenDialog=builder.create();
        fullscreenDialog.show();
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

