package com.bezzy.Ui.View.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.NotificationActivity;
import com.bezzy.Ui.View.activity.Registration;
import com.bezzy.Ui.View.model.Notification_item;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class Notifi_Adapter extends RecyclerView.Adapter<Notifi_Adapter.NoyificationViewHolder> {
    Context context;
    ArrayList<Notification_item> dataholder;
    SpotsDialog progressDialog;

    public Notifi_Adapter(Context context, ArrayList<Notification_item> dataholder, SpotsDialog progressDialog) {
        this.context = context;
        this.dataholder = dataholder;
        this.progressDialog = progressDialog;
    }

    @NonNull
    @Override
    public NoyificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(com.bezzy_application.R.layout.notification_item,parent,false);

        return new Notifi_Adapter.NoyificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NoyificationViewHolder holder, final int position) {
//        holder.img.setImageResource(dataholder.get(position).getImg());
        holder.descrip.setText(dataholder.get(position).getDescrip());

        Glide.with(context)
                .load(dataholder.get(position).getImg())
                .into(holder.img_logo);

        //progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public class NoyificationViewHolder extends RecyclerView.ViewHolder{
       // ImageView img;
        TextView descrip;
        CircleImageView img_logo;
        RelativeLayout relativeShow;
        Button acceptButton,rejectButton;

        public NoyificationViewHolder(@NonNull View itemView) {
            super(itemView);
            descrip = itemView.findViewById(R.id.title_text);
            img_logo = itemView.findViewById(R.id.img_logo);
        }
    }
}
