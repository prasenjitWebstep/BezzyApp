package com.bezzy.Ui.View.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.activity.PostImageVideoViewActivity;
import com.bezzy.Ui.View.model.FriendsPostModelImage;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.ViewPropertyTransition;

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
    public void onBindViewHolder(@NonNull FriendsImagePostHolder holder, final int position) {

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

        holder.imageDisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, PostImageVideoViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("postId",postList.get(position).getPostId());
                context.startActivity(intent);*/
                Utility.fullscreenDialog(context,postList.get(position).getPostId());
            }
        });

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
