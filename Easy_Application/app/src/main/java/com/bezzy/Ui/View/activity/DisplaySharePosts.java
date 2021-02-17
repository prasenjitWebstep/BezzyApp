package com.bezzy.Ui.View.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.adapter.FriendsEnlargeImagePostAdapter;
import com.bezzy.Ui.View.adapter.FriendsImagePostAdapter;
import com.bezzy.Ui.View.model.FriendsPostModelImage;
import com.bezzy.Ui.View.utils.Utility;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplaySharePosts extends AppCompatActivity {

    Uri uri;
    CircleImageView img_logo;
    TextView title_text,descrip;
    CardView videoDisp,cardHolder;
    RecyclerView recyclerImageShow;
    ImageView imageShow;
    PlayerView andExoPlayerView;
    ArrayList<FriendsPostModelImage> postModelList;
    SimpleExoPlayer absPlayerInternal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_share_posts);

        img_logo = findViewById(R.id.img_logo);
        title_text = findViewById(R.id.title_text);
        descrip = findViewById(R.id.descrip);
        videoDisp = findViewById(R.id.videoDisp);
        cardHolder = findViewById(R.id.cardHolder);
        recyclerImageShow = findViewById(R.id.recyclerImageShow);
        imageShow = findViewById(R.id.imageShow);
        andExoPlayerView = findViewById(R.id.andExoPlayerView);
        postModelList = new ArrayList<>();

        if(Utility.getLogin(DisplaySharePosts.this).equals("1")){

            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                            // Get deep link from result (may be null if no link is found)
                            Uri deepLink = null;
                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();
                                Log.e("Link",deepLink.toString());
                                Utility.displayLoader(DisplaySharePosts.this);
                                friendsPostLargeView(deepLink.toString());
                            }

                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "getDynamicLink:onFailure", e);
                        }
                    });

        }else{
            Intent intent = new Intent(DisplaySharePosts.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }



    }

    private void friendsPostLargeView(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /* Log.e("Response",response);*/
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        //progressDialog.dismiss();

                        ViewPropertyTransition.Animator animationObject = new ViewPropertyTransition.Animator() {
                            @Override
                            public void animate(View view) {
                                view.setAlpha(0f);

                                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                                fadeAnim.setDuration(1500);
                                fadeAnim.start();
                            }
                        };

                        Utility.hideLoader(DisplaySharePosts.this);
                        final JSONObject object1 = object.getJSONObject("post_details");

                        Glide.with(DisplaySharePosts.this)
                                .load(object1.getString("post_user_image"))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .transition(GenericTransitionOptions.with(animationObject))
                                .into(img_logo);

                        title_text.setText(object1.getString("post_user_name"));

                        descrip.setText(object1.getString("post_content"));

                        String mediaType = object1.getString("post_type");

                        if(mediaType.equals("video")){
                            Log.e("Clicked","1");
                            videoDisp.setVisibility(View.VISIBLE);
                            JSONArray array = object1.getJSONArray("post_img_video_live");
                            for(int i=0; i<array.length(); i++){
                                try {
                                    JSONObject object2 = array.getJSONObject(i);
                                    startPlayingVideo(DisplaySharePosts.this,object2.getString("post_url"),andExoPlayerView,R.string.app_name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("Exception",e.toString());
                                }
                            }
                        }else if(mediaType.equals("image")){
                            JSONArray array = object1.getJSONArray("post_img_video_live");
                            int size = array.length();
                            if(size<2){
                                cardHolder.setVisibility(View.VISIBLE);
                                for(int i=0;i<array.length();i++){

                                    try {

                                        JSONObject object2 = array.getJSONObject(i);

                                        Glide.with(DisplaySharePosts.this)
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
                                LinearLayoutManager layoutManager = new LinearLayoutManager(DisplaySharePosts.this);
                                /*layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);*/
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

                                FriendsImagePostAdapter postImageObj = new FriendsImagePostAdapter(DisplaySharePosts.this, postModelList);
                                recyclerImageShow.setAdapter(postImageObj);
                            }

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressDialog.dismiss();
                    Utility.hideLoader(DisplaySharePosts.this);
                    Log.e("Exception",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Utility.hideLoader(DisplaySharePosts.this);
                Log.e("Error",error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(DisplaySharePosts.this);
        queue.add(request);
    }

    private void startPlayingVideo(Context ctx, String CONTENT_URL, PlayerView playerView, int appNameRes) {

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

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if(andExoPlayerView.getVisibility() == View.VISIBLE){
                absPlayerInternal.stop();
            }
        }catch (Exception e){
            Log.e("Exception",e.toString());
        }

    }
}