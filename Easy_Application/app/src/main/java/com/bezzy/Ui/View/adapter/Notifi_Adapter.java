package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.model.Notification_item;
import com.bezzy_application.R;

import java.util.ArrayList;

public class Notifi_Adapter extends RecyclerView.Adapter<Notifi_Adapter.NoyificationViewHolder> {
    Context context;
    ArrayList<Notification_item> dataholder;

    public Notifi_Adapter(Context context, ArrayList<Notification_item> dataholder) {
        this.context = context;
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public NoyificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(com.bezzy_application.R.layout.notification_item,parent,false);

        return new Notifi_Adapter.NoyificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoyificationViewHolder holder, int position) {
//        holder.img.setImageResource(dataholder.get(position).getImg());
        holder.descrip.setText(dataholder.get(position).getDescrip());

    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public class NoyificationViewHolder extends RecyclerView.ViewHolder{
       // ImageView img;
        TextView descrip;

        public NoyificationViewHolder(@NonNull View itemView) {
            super(itemView);
            descrip = itemView.findViewById(R.id.title_text);
        }
    }
}
