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

public class Notifi_Adapter extends RecyclerView.Adapter<Notifi_Adapter.NoyificationViewHolder> {
    Context context;
    ArrayList<Notification_item> dataholder;
    ProgressDialog progressDialog;

    public Notifi_Adapter(Context context, ArrayList<Notification_item> dataholder, ProgressDialog progressDialog) {
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

        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        if(dataholder.get(position).getType().equals("1") && dataholder.get(position).getFriendrequestStatus().equals("3")){
            holder.relativeShow.setVisibility(View.VISIBLE);
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Utility.internet_check(context)){
                        progressDialog.show();
                        callApiAccept(APIs.BASE_URL+APIs.ACCEPTREQUEST+"/"+dataholder.get(position).getFromId()+"/"+ Utility.getUserId(context),holder.relativeShow);
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();
                    }

                }
            });

            holder.rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Utility.internet_check(context)){
                        progressDialog.show();
                        callApiReject(APIs.BASE_URL+APIs.REJECTREQUEST+"/"+dataholder.get(position).getFromId()+"/"+ Utility.getUserId(context),holder.relativeShow);
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();
                    }



                }
            });
        }else{
            holder.relativeShow.setVisibility(View.GONE);
        }

    }

    private void callApiReject(String url, final RelativeLayout relativeShow) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object1 = new JSONObject(response);
                    String status = object1.getString("status");
                    if(status.equals("success")){
                        progressDialog.dismiss();
                        Toast.makeText(context,object1.getString("message"),Toast.LENGTH_SHORT).show();
                        if(object1.getString("friend_request_status").equals("2")){
                            relativeShow.setVisibility(View.GONE);
                        }
                    }else{
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private void callApiAccept(String url, final RelativeLayout relativeShow) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Resposne",response);

                try {
                    JSONObject object1 = new JSONObject(response);
                    String status = object1.getString("status");
                    if(status.equals("success")){
                        progressDialog.dismiss();
                        Toast.makeText(context,object1.getString("message"),Toast.LENGTH_SHORT).show();
                        if(object1.getString("friend_request_status").equals("1")){
                            relativeShow.setVisibility(View.GONE);
                        }
                    }else{
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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
            relativeShow = itemView.findViewById(R.id.relativeAcceptRejectShow);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }
}
