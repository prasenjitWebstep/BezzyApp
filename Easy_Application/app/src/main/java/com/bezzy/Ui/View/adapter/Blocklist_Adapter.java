package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.activity.FriendsProfileActivity;
import com.bezzy.Ui.View.model.FriendsHolder;
import com.bezzy.Ui.View.model.Unblockholders;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Blocklist_Adapter extends RecyclerView.Adapter<Blocklist_Adapter.Unblockholder> {
    Context context;
    ArrayList<Unblockholders> unblockHolder;

    public Blocklist_Adapter(Context context, ArrayList<Unblockholders> unblockHolder) {
        this.context = context;
        this.unblockHolder = unblockHolder;
    }

    @NonNull
    @Override
    public Unblockholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Blocklist_Adapter.Unblockholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.blocklist_screens,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Unblockholder holder, final int position) {
        Glide.with(context)
                .load(unblockHolder.get(position).getImage())
                .into(holder.circularImg);

        holder.userName.setText(unblockHolder.get(position).getName());

        holder.circularImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FriendsProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("friendId",unblockHolder.get(position).getFriendId());
                intent.putExtra("screen","2");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return unblockHolder.size();
    }

    public class Unblockholder extends RecyclerView.ViewHolder{
        CircleImageView circularImg;
        TextView userName;
        TextView btn;


        public Unblockholder(@NonNull View itemView) {
            super(itemView);
            circularImg = itemView.findViewById(R.id.circularImg);
            userName = itemView.findViewById(R.id.userName);
            btn = itemView.findViewById(R.id.btn);
        }
    }
}
