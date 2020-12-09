package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.activity.CommentReplyActivity;
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
    public void onBindViewHolder(@NonNull CommentHolder holder, final int position) {
        holder.username.setText(dataholder.get(position).getUsername());
        holder.time.setText(dataholder.get(position).getPost_comment_time());
        holder.user_comment.setText(dataholder.get(position).getCommentText());

        Glide.with(context)
                .load(dataholder.get(position).getUser_image())
                .into(holder.imageView);

        holder.chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentReplyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("image",dataholder.get(position).getUser_image());
                intent.putExtra("name",dataholder.get(position).getUsername());
                intent.putExtra("comment",dataholder.get(position).getCommentText());
                intent.putExtra("time",dataholder.get(position).getPost_comment_time());
                intent.putExtra("id",dataholder.get(position).getCommentid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder{
        TextView username,time,user_comment;
        CircleImageView imageView;
        ImageView favBtn,favBtnfilled,chat_btn;

        public CommentHolder(@NonNull View itemView) {

            super(itemView);
            username=itemView.findViewById(R.id.title_text);
            time=itemView.findViewById(R.id.timeshow);
            imageView=itemView.findViewById(R.id.img_logo);
            user_comment=itemView.findViewById(R.id.comment_user);
            favBtn = itemView.findViewById(R.id.favBtn);
            favBtnfilled = itemView.findViewById(R.id.favBtnfilled);
            chat_btn = itemView.findViewById(R.id.chat_btn);
        }
    }
}
