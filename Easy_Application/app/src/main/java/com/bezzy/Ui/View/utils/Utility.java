package com.bezzy.Ui.View.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.CommentActivity;
import com.bezzy.Ui.View.adapter.FriendsEnlargeImagePostAdapter;
import com.bezzy.Ui.View.model.FriendsPostModelImage;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;
import com.potyvideo.library.AndExoPlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Utility {

    private static AlertDialog topupDialog,fullscreenDialog;
    public static String globalData = "0";

    public static boolean internet_check(Context context){
        //Test for Connection
        ConnectivityManager cmgr=(ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cmgr.getActiveNetworkInfo()!= null && cmgr.getActiveNetworkInfo().isAvailable() && cmgr.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void setLogin(Context mContext, String type) {
        SharedPreferences preferences = mContext.getSharedPreferences("Bezzy", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login", type);
        editor.apply();
    }

    public static String getLogin(Context mContext) {
        SharedPreferences memIdPreferences = mContext.getSharedPreferences("Bezzy", 0); // 0 - for private mode
        return memIdPreferences.getString("login", "");
    }

    public static void setSocial(Context mContext, String type) {
        SharedPreferences preferences = mContext.getSharedPreferences("Bezzy", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("google", type);
        editor.apply();
    }

    public static String getSocial(Context mContext) {
        SharedPreferences memIdPreferences = mContext.getSharedPreferences("Bezzy", 0); // 0 - for private mode
        return memIdPreferences.getString("google", "");
    }

    public static void setOtpScreen(Context mContext, String type) {
        SharedPreferences preferences = mContext.getSharedPreferences("Bezzy", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("otpscreen", type);
        editor.apply();
    }

    public static String getOtpScreen(Context mContext) {
        SharedPreferences memIdPreferences = mContext.getSharedPreferences("Bezzy", 0); // 0 - for private mode
        return memIdPreferences.getString("otpscreen", "");
    }

    public static void setUserId(Context mContext, String type) {
        SharedPreferences preferences = mContext.getSharedPreferences("Bezzy", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userId", type);
        editor.apply();
    }

    public static String getUserId(Context mContext) {
        SharedPreferences memIdPreferences = mContext.getSharedPreferences("Bezzy", 0); // 0 - for private mode
        return memIdPreferences.getString("userId", "");
    }


    public static void setNotificationStatus(Context mContext, String type) {
        SharedPreferences preferences = mContext.getSharedPreferences("Bezzy", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("status", type);
        editor.apply();
    }

    public static String getNotificationStatus(Context mContext) {
        SharedPreferences memIdPreferences = mContext.getSharedPreferences("Bezzy", 0); // 0 - for private mode
        return memIdPreferences.getString("status", "0");
    }

    public static void displayLoader(Context context){

        customProgressDialog(context);

    }

    public static void hideLoader(Context context){

        customProgressDialog2(context);

    }

    public static void customProgressDialog(Context context){

        ProgressBar imageView;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v= LayoutInflater.from(context).inflate(R.layout.popup_dialog,null);
        imageView = v.findViewById(R.id.logo_bezzy);

        builder.setView(v);
        builder.setCancelable(false);
        globalData = "1";
        topupDialog=builder.create();
        topupDialog.show();
        topupDialog.getWindow().setLayout(200, 200);
    }

    public static void customProgressDialog2(Context context){
        globalData = "0";
        topupDialog.dismiss();
    }

    public static void fullscreenDialog(Context context, String post_id){

        AndExoPlayerView andExoPlayerView;
        ImageView imageShow,fav_btn,favBtnfilled,chat_btn;
        RecyclerView recyclerImageShow;
        TextView servicesText,following_num,following_numm;
        ArrayList<FriendsPostModelImage> postModelList;

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MaterialTheme);
        View v= LayoutInflater.from(context).inflate(R.layout.photo_image_dialog_layout,null);
        andExoPlayerView = v.findViewById(R.id.andExoPlayerView);
        imageShow = v.findViewById(R.id.imageShow);
        recyclerImageShow = v.findViewById(R.id.recyclerImageShow);
        servicesText = v.findViewById(R.id.servicesText);
        following_num = v.findViewById(R.id.following_num);
        following_numm = v.findViewById(R.id.following_numm);
        fav_btn = v.findViewById(R.id.fav_btn);
        favBtnfilled = v.findViewById(R.id.favBtnfilled);
        chat_btn = v.findViewById(R.id.chat_btn);
        postModelList = new ArrayList<>();

        if(Utility.internet_check(context)) {

            //progressDialog.show();
            Utility.displayLoader(context);
            friendsPostLargeView(APIs.BASE_URL+APIs.FRIENDSBLOCKDETAILS+"/"+post_id+"/"+Utility.getUserId(context),context,
                    andExoPlayerView,imageShow,
                    recyclerImageShow,servicesText,
                    following_num,following_numm,
                    fav_btn,favBtnfilled,postModelList,chat_btn);

        }
        else {

            //progressDialog.dismiss();
            Utility.hideLoader(context);

            Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();
        }


        builder.setView(v);
        builder.setCancelable(true);
        fullscreenDialog=builder.create();
        fullscreenDialog.show();
    }

    private static void friendsPostLargeView(String url, final Context context,
                                             final AndExoPlayerView andExoPlayerView,
                                             final ImageView imageShow,
                                             final RecyclerView recyclerImageShow,
                                             final TextView servicesText,
                                             final TextView following_num,
                                             final TextView following_numm,
                                             final ImageView fav_btn,
                                             final ImageView favBtnfilled,
                                             final ArrayList<FriendsPostModelImage> postModelList,
                                             final ImageView chat_btn) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        //progressDialog.dismiss();
                        Utility.hideLoader(context);
                        final JSONObject object1 = object.getJSONObject("post_details");
                        if(object1.getString("log_user_like_status").equals("Yes")){
                            favBtnfilled.setVisibility(View.VISIBLE);
                        }else{
                            fav_btn.setVisibility(View.VISIBLE);
                        }

                       /* if(fav_btn.getVisibility() == View.VISIBLE){
                            fav_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e("Called","1");
                                    favBtnfilled.setVisibility(View.VISIBLE);
                                    fav_btn.setVisibility(View.INVISIBLE);
                                    if(Utility.internet_check(context)) {
                                        try {
                                            Log.e("POSTID",object1.getString("post_id"));
                                            friendsPostLike(APIs.BASE_URL+APIs.LIKEPOST+"/"+Utility.getUserId(context)+"/"+object1.getString("post_id"),following_num,context);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    else {

                                        Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        favBtnfilled.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("Called","2");
                                favBtnfilled.setVisibility(View.INVISIBLE);
                                fav_btn.setVisibility(View.VISIBLE);
                                if(Utility.internet_check(context)) {

                                    try {
                                        Log.e("POSTID",object1.getString("post_id"));
                                        friendsPostLike(APIs.BASE_URL+APIs.LIKEPOST+"/"+Utility.getUserId(context)+"/"+object1.getString("post_id"),following_num, context);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {

                                    Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });*/

                        following_num.setText(object1.getString("number_of_like"));

                        following_numm.setText(object1.getString("number_of_comment"));

                        /*chat_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, CommentActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                try {
                                    intent.putExtra("postId",object1.getString("post_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                context.startActivity(intent);
                            }
                        });*/

                        servicesText.setText(object1.getString("post_content"));

                        String mediaType = object1.getString("post_type");

                        if(mediaType.equals("video")){
                            Log.e("Clicked","1");
                            andExoPlayerView.setVisibility(View.VISIBLE);
                            imageShow.setVisibility(View.GONE);
                            recyclerImageShow.setVisibility(View.GONE);
                            JSONArray array = object1.getJSONArray("post_img_video_live");
                            for(int i=0; i<array.length(); i++){
                                try {
                                    JSONObject object2 = array.getJSONObject(i);
                                    andExoPlayerView.setSource(object2.getString("post_url"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("Exception",e.toString());
                                }
                            }
                        }else if(mediaType.equals("image")){
                            JSONArray array = object1.getJSONArray("post_img_video_live");
                            int size = array.length();
                            if(size<2){
                                imageShow.setVisibility(View.VISIBLE);
                                recyclerImageShow.setVisibility(View.GONE);
                                andExoPlayerView.setVisibility(View.GONE);
                                for(int i=0;i<array.length();i++){

                                    try {

                                        JSONObject object2 = array.getJSONObject(i);
                                        Glide.with(context)
                                                .load(object2.getString("post_url"))
                                                .into(imageShow);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("Exception",e.toString());
                                    }

                                }

                            }else{
                                recyclerImageShow.setVisibility(View.VISIBLE);
                                imageShow.setVisibility(View.GONE);
                                andExoPlayerView.setVisibility(View.GONE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
                                recyclerImageShow.setLayoutManager(layoutManager);
                                postModelList.clear();
                                for(int i=0;i<array.length();i++){

                                    try {
                                        JSONObject object2 = array.getJSONObject(i);
                                        FriendsPostModelImage postModelObj = new FriendsPostModelImage(object2.getString("post_url"),"",object2.getString("post_type"));
                                        postModelList.add(postModelObj);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("Exception",e.toString());
                                    }

                                }

                                FriendsEnlargeImagePostAdapter postImageObj = new FriendsEnlargeImagePostAdapter(context, postModelList);
                                recyclerImageShow.setAdapter(postImageObj);
                            }

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressDialog.dismiss();
                    Utility.hideLoader(context);
                    Log.e("Exception",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Utility.hideLoader(context);
                Log.e("Error",error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private static void friendsPostLike(String url, final TextView following_num, Context context) {
        Log.e("URL",url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        String number = object.getJSONObject("activity").getString("number_of_activity");
                        following_num.setText(number);
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


}
