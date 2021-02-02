package com.bezzy.Ui.View.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.Blocklist;
import com.bezzy.Ui.View.activity.FollowingActivity;
import com.bezzy.Ui.View.activity.Fragments.HomeFragment;
import com.bezzy.Ui.View.activity.FriendsProfileActivity;
import com.bezzy.Ui.View.model.FriendsHolder;
import com.bezzy.Ui.View.model.Unblockholders;
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
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Blocklist_Adapter extends RecyclerView.Adapter<Blocklist_Adapter.Unblockholder> {
    Context context;
    ArrayList<Unblockholders> unblockHolder;
    String screen;


    public Blocklist_Adapter(Context context, ArrayList<Unblockholders> unblockHolder,String screen) {
        this.context = context;
        this.unblockHolder = unblockHolder;
        this.screen = screen;
    }

    @NonNull
    @Override
    public Unblockholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Blocklist_Adapter.Unblockholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.blocklist_screens,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Unblockholder holder, final int position) {

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
                .load(unblockHolder.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(animationObject))
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

        holder.btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if(Utility.internet_check(context)) {

                    Utility.displayLoader(context);



                    unblock(APIs.BASE_URL+APIs.UNBLOCK,unblockHolder.get(position).getFriendId());



                }
                else {

                    //progressDialog.dismiss();
                    Utility.hideLoader(context);
                    Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

    private void unblock(String url,final String s){
        //Toast.makeText(context,"FUCKING FUCK FUCK FUCK",Toast.LENGTH_LONG).show();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("status");
                    if (resp.equals("success")) {
                        Utility.hideLoader(context);
                        Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, Blocklist.class);
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();

                map.put("loginUserID",Utility.getUserId(context));
                map.put("unblockuserID",s);


                return map;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(request);
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
