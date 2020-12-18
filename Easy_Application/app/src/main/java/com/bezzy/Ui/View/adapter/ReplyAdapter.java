package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.model.Comment_item;
import com.bezzy_application.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyHolder> {
    Context context;
    ArrayList<Comment_item> replyList;

    public ReplyAdapter(Context context, ArrayList<Comment_item> replyList) {
        this.context = context;
        this.replyList = replyList;
    }

    @NonNull
    @Override
    public ReplyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReplyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.replylist_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

    public class ReplyHolder extends RecyclerView.ViewHolder{

        CircleImageView img_logo;
        TextView title_text,comment_user,timeshow;

        public ReplyHolder(@NonNull View itemView) {
            super(itemView);
            img_logo = itemView.findViewById(R.id.img_logo);
            title_text = itemView.findViewById(R.id.title_text);
            comment_user = itemView.findViewById(R.id.comment_user);
            timeshow = itemView.findViewById(R.id.timeshow);
        }
    }
}
