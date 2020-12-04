package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ImageHolder> {

    Context context;
    ArrayList<Bitmap> bitList;

    public ImageViewAdapter(Context context, ArrayList<Bitmap> bitList) {
        this.context = context;
        this.bitList = bitList;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.img_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {

        Glide.with(context)
                .load(bitList.get(position))
                .into(holder.imageDisp);


    }

    @Override
    public int getItemCount() {
        return bitList.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder{
        TextView tvtime;

        ImageView imageDisp;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            imageDisp = itemView.findViewById(R.id.imageDisp);
        }
    }
}
