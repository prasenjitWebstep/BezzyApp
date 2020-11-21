package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.model.Comment_item;
import com.bezzy.Ui.View.model.Notification_item;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comment_adapter extends RecyclerView.Adapter<Comment_adapter.CommentHolder> {
    Context context;
    ArrayList<Comment_item> dataholder;

    public Comment_adapter(Context context, ArrayList<Comment_item> dataholder) {
        this.context = context;
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.comment_item,parent,false);

        return new Comment_adapter.CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.username.setText(dataholder.get(position).getUsername());
        holder.time.setText(dataholder.get(position).getPost_comment_time());
        holder.user_comment.setText(dataholder.get(position).getCommentText());

        Glide.with(context)
                .load(dataholder.get(position).getUser_image())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder{
        TextView username,time,user_comment;
        CircleImageView imageView;

        public CommentHolder(@NonNull View itemView) {

            super(itemView);
            username=itemView.findViewById(R.id.title_text);
            time=itemView.findViewById(R.id.timeshow);
            imageView=itemView.findViewById(R.id.img_logo);
            user_comment=itemView.findViewById(R.id.comment_user);
        }
    }
}
