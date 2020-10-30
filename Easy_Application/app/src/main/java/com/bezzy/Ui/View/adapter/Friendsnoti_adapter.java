package com.bezzy.Ui.View.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.model.Friendsnoti_item;
import com.bezzy_application.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friendsnoti_adapter extends RecyclerView.Adapter<Friendsnoti_adapter.FriendsnotiViewHolder>{
    String data1[],data2[];
    int images[];
    Context context;
    ArrayList<Friendsnoti_item> dataholder;
    /*public Friendsnoti_adapter(Context ct,String s1[],String s2[],int img[]){
        context=ct;
        data1=s1;
        data2=s2;
        images=img;

    }*/
   /* public Friendsnoti_adapter(ArrayList<Friendsnoti_item> dataholder){
        this.dataholder=dataholder;
    }*/

    public Friendsnoti_adapter(Context context, ArrayList<Friendsnoti_item> dataholder) {
        this.context = context;
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public FriendsnotiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.friensnoti_item,parent,false);

        return new FriendsnotiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsnotiViewHolder holder, int position) {
       /* holder.mytext1.setText(data1[position]);
        holder.mytext2.setText(data2[position]);
        holder.myimages.setImageResource(images[position]);*/
        holder.img.setImageResource(dataholder.get(position).getImg());
        holder.header.setText(dataholder.get(position).getHeader());
        holder.desc.setText(dataholder.get(position).getDesc());

    }

    @Override
    public int getItemCount() {

        //return images.length;
        return dataholder.size();
    }

    public class FriendsnotiViewHolder extends RecyclerView.ViewHolder {
        /*TextView mytext1,mytext2;
        ImageView myimages;*/
        CircleImageView img;
        TextView header,desc;

        public FriendsnotiViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img_logo);
            header=itemView.findViewById(R.id.title_text);
            desc=itemView.findViewById(R.id.descrip);
        }
    }
}
