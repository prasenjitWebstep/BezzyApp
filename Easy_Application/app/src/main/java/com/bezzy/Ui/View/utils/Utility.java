package com.bezzy.Ui.View.utils;

import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.CommentActivity;
import com.bezzy.Ui.View.activity.Fragments.Photo_fragment;
import com.bezzy.Ui.View.activity.LoginActivity;
import com.bezzy.Ui.View.adapter.FriendsEnlargeImagePostAdapter;
import com.bezzy.Ui.View.model.FriendsPostModelImage;
import com.bezzy_application.R;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.potyvideo.library.AndExoPlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utility {

    private static AlertDialog topupDialog,fullscreenDialog;
    public static String globalData = "0";
    public static SimpleExoPlayer absPlayerInternal;
    public static String videoURL;
    private static int mCurrentPosition = 0;
    static ProgressDialog pDialog;
    private static Context mContext;
    private static VideoView mandExoPlayerView;
    private static MediaController mediaC;

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

    public static void setUserToken(Context mContext, String type) {
        SharedPreferences preferences = mContext.getSharedPreferences("Bezzy", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", type);
        editor.apply();
    }

    public static String getUserToken(Context mContext) {
        SharedPreferences memIdPreferences = mContext.getSharedPreferences("Bezzy", 0); // 0 - for private mode
        return memIdPreferences.getString("token", "");
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

        final PlayerView andExoPlayerView;
        ImageView imageShow,fav_btn,favBtnfilled,chat_btn,back_image;
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
        back_image = v.findViewById(R.id.back_image);
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

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(andExoPlayerView.getVisibility() == View.VISIBLE){
                    absPlayerInternal.stop();
                    absPlayerInternal.setVolume(0f);
                    /*andExoPlayerView.stopPlayback();*/
                    fullscreenDialog.dismiss();
                }else{
                    fullscreenDialog.dismiss();
                }
            }
        });


        builder.setView(v);
        builder.setCancelable(false);
        fullscreenDialog=builder.create();
        fullscreenDialog.show();
    }

    private static void friendsPostLargeView(String url, final Context context,
                                             final PlayerView andExoPlayerView,
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
               /* Log.e("Response",response);*/
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
                                    /*videoURL = object2.getString("post_url");
                                    mContext = context;
                                    mandExoPlayerView = andExoPlayerView;
                                    MyTaskParams params = new MyTaskParams(videoURL);
                                    DownloadXML myTask = new DownloadXML();
                                    myTask.execute(params);*/
                                    /*initializePlayer(context,videoURL,andExoPlayerView);*/
                                    startPlayingVideo(context,object2.getString("post_url"),andExoPlayerView,R.string.app_name);
                                    /*andExoPlayerView.setSource(object2.getString("post_url"));
                                    andExoPlayerView.setShowFullScreen(false);*/
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
                                                .load(object2.getString("post_url"))
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .transition(GenericTransitionOptions.with(animationObject))
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

    private static class MyTaskParams {
        String videoUrl;

        public MyTaskParams(String videoUrl) {
            this.videoUrl = videoUrl;
        }
    }

    static class DownloadXML extends AsyncTask<MyTaskParams, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            displayLoader(mContext);
            Log.e("EXECUTIONSTARTED","TRUE");
        }

        @Override
        protected Void doInBackground(MyTaskParams... myTaskParams) {
            String videoUrl = myTaskParams[0].videoUrl;

            try {
                Log.e("EXECUTIONProcessing","TRUE");
                mandExoPlayerView.setVideoURI(Uri.parse(videoUrl));
                //videoView.setVideoURI(Uri.parse(videofilename));
                mandExoPlayerView.requestFocus();
                mediaC  = new MediaController(mContext);
                mediaC.setAnchorView(mandExoPlayerView);
                mandExoPlayerView.setMediaController(mediaC);
                /* andExoPlayerView.setMediaController(new MediaController(mContext));*/

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            hideLoader(mContext);
            Log.e("STARTED","TRUE");
            mandExoPlayerView.start();
        }

    }

    private static void startPlayingVideo(Context ctx, String CONTENT_URL, PlayerView playerView, int appNameRes) {

        PlayerView pvMain = playerView;

        //BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        //TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        //TrackSelector trackSelectorDef = new DefaultTrackSelector(videoTrackSelectionFactory);
        TrackSelector trackSelectorDef = new DefaultTrackSelector();

        DefaultLoadControl loadControl = new DefaultLoadControl.Builder().setBufferDurationsMs(32*1024, 64*1024, 1024, 1024).createDefaultLoadControl();

        absPlayerInternal = ExoPlayerFactory.newSimpleInstance(ctx, trackSelectorDef);

        String userAgent = Util.getUserAgent(ctx, ctx.getString(appNameRes));

        DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(ctx,userAgent);
        Uri uriOfContentUrl = Uri.parse(CONTENT_URL);
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(uriOfContentUrl);

        absPlayerInternal.prepare(mediaSource);
      /*  absPlayerInternal.setVolume(0f);*/
        absPlayerInternal.setPlayWhenReady(true);
        absPlayerInternal.setRepeatMode(Player.REPEAT_MODE_ALL);
        pvMain.setPlayer(absPlayerInternal);

    }



    public static void notifyUpload(Context context, boolean flag,String text1, String text2){

        int notificationId = 101;

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_upgrade)
                .setContentTitle(text1)
                .setContentText(text2)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if(flag == false){
            notificationBuilder.setProgress(0,0,true);
            notificationManager.notify(notificationId,notificationBuilder.build());

        }else if(flag == true){
            notificationBuilder.setSmallIcon(R.drawable.ic_check);
            notificationBuilder.setContentText(text2);
            notificationBuilder.setProgress(0,0,false);
            notificationManager.notify(notificationId,notificationBuilder.build());
        }

        flag = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("1001", "BEZZY", importance);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            notificationBuilder.setChannelId("1001");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());

    }

    public static void logoutFunction(final Context context){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.BASE_URL+APIs.LOGOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("success")){

                        //progressDialog.dismiss();
                        Toast.makeText(context,object.getString("message"),Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);
                        Utility.setLogin(context,"0");
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
                map.put("profile_id",Utility.getUserId(context));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);

    }

    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            final float totalPixels = width * height;

            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

}
