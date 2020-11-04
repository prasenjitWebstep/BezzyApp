package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bezzy.Ui.View.activity.Fragments.ProfileFragment;
import com.bezzy.Ui.View.model.PostItem;
import com.bezzy.Ui.View.model.PostModel;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

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
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        for(PostModel model : postItems){

            Log.e("Image",model.getImage());

        }

        Glide.with(context)
                .load(postItems.get(position).getImage())
                .into(holder.imageDisp);

    }

    @Override
    public int getItemCount() {
        return postItems.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        ImageView imageDisp;

        public PostViewHolder(@NonNull View itemView) {

            super(itemView);

            imageDisp = itemView.findViewById(R.id.imageDisp);

        }
    }
}

