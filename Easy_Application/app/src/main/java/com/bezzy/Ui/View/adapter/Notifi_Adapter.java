package com.bezzy.Ui.View.adapter;

import android.animation.ObjectAnimator;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.NotificationActivity;
import com.bezzy.Ui.View.activity.PostImageVideoViewActivity;
import com.bezzy.Ui.View.activity.Registration;
import com.bezzy.Ui.View.model.Notification_item;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.ViewPropertyTransition;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class Notifi_Adapter extends RecyclerView.Adapter<Notifi_Adapter.NoyificationViewHolder> {
    Context context;
    ArrayList<Notification_item> dataholder;
    SpotsDialog progressDialog;

    public Notifi_Adapter(Context context, ArrayList<Notification_item> dataholder) {
        this.context = context;
        this.dataholder = dataholder;
        //this.progressDialog = progressDialog;
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

        ViewPropertyTransition.Animator animationObject = new ViewPropertyTransition.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha(0f);

                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(2500);
                fadeAnim.start();
            }
        };

        Glide.with(context)
                .load(dataholder.get(position).getImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(animationObject))
                .into(holder.img_logo);

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataholder.get(position).getPostType().equals("post")){
                    Intent intent = new Intent(context, PostImageVideoViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("postId",dataholder.get(position).getPostId());
                    context.startActivity(intent);
                }

            }
        });

       /* progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);*/

    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public class NoyificationViewHolder extends RecyclerView.ViewHolder{
       // ImageView img;
        TextView descrip;
        CircleImageView img_logo;
        CardView cardClick;
        RelativeLayout relativeShow;
        Button acceptButton,rejectButton;

        public NoyificationViewHolder(@NonNull View itemView) {
            super(itemView);
            descrip = itemView.findViewById(R.id.title_text);
            img_logo = itemView.findViewById(R.id.img_logo);
            cardClick = itemView.findViewById(R.id.cardClick);
        }
    }
}
