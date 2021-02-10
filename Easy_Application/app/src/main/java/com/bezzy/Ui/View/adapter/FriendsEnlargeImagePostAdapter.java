package com.bezzy.Ui.View.adapter;

import android.animation.ObjectAnimator;
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
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.ViewPropertyTransition;

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

        ViewPropertyTransition.Animator animationObject = new ViewPropertyTransition.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha(0f);

                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(1500);
                fadeAnim.start();
            }
        };

        Glide.with(context)
                .load(postList.get(position).getPostUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(animationObject))
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
