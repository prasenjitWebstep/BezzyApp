package com.bezzy.Ui.View.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.model.Likes_name;
import com.bezzy_application.R;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.ViewPropertyTransition;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LikesName_Adapter extends RecyclerView.Adapter<LikesName_Adapter.Likesname> {

    Context context;
    ArrayList<Likes_name> dataholder;

    public LikesName_Adapter(Context context, ArrayList<Likes_name> dataholder) {
        this.context = context;
        this.dataholder = dataholder;

    }


    @NonNull
    @Override
    public Likesname onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.likesname_item, parent, false);
        return new Likesname(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Likesname holder, int position) {
        holder.username.setText(dataholder.get(position).getUsername());

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
                .load(dataholder.get(position).getUser_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(animationObject))
                .into(holder.image);


    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public class Likesname extends RecyclerView.ViewHolder {
        TextView username;
        CircleImageView image;

        public Likesname(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.userName);
            image = itemView.findViewById(R.id.circularImg);
        }
    }
}
