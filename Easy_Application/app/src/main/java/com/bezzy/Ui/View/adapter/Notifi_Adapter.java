package com.bezzy.Ui.View.adapter;

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
import com.bezzy.Ui.View.model.Notification_item;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
    public void onBindViewHolder(@NonNull NoyificationViewHolder holder, final int position) {
//        holder.img.setImageResource(dataholder.get(position).getImg());
        holder.descrip.setText(dataholder.get(position).getDescrip());

        if(dataholder.get(position).getType().equals("1")){
            holder.relativeShow.setVisibility(View.VISIBLE);
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callApiAccept(APIs.BASE_URL+APIs.ACCEPTREQUEST+"/"+dataholder.get(position).getFromId()+"/"+ Utility.getUserId(context));

                }
            });

            holder.rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callApiReject(APIs.BASE_URL+APIs.REJECTREQUEST+"/"+dataholder.get(position).getFromId()+"/"+ Utility.getUserId(context));

                }
            });
        }else{
            holder.relativeShow.setVisibility(View.GONE);
        }

    }

    private void callApiReject(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object1 = new JSONObject(response);
                    String status = object1.getString("status");
                    if(status.equals("success")){
                        Toast.makeText(context,object1.getString("message"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, NotificationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private void callApiAccept(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Resposne",response);

                try {
                    JSONObject object1 = new JSONObject(response);
                    String status = object1.getString("status");
                    if(status.equals("success")){
                        Toast.makeText(context,object1.getString("message"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, NotificationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
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
            relativeShow = itemView.findViewById(R.id.relativeShow);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }
}