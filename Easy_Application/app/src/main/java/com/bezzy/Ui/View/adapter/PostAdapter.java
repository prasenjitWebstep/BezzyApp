package com.bezzy.Ui.View.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bezzy.Ui.View.activity.Fragments.ProfileFragment;
import com.bezzy.Ui.View.activity.ImageDisplayActivity;
import com.bezzy.Ui.View.activity.Profile;
import com.bezzy.Ui.View.activity.VideoDisplayActivity;
import com.bezzy.Ui.View.model.FriendsPostModelImage;
import com.bezzy.Ui.View.model.PostModel;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
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

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    ArrayList<PostModel> postItems;
    Context context;
    String screen;

    public PostAdapter(ArrayList<PostModel> postItems, Context context, String screen) {
        this.postItems = postItems;
        this.context = context;
        this.screen = screen;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_container,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {

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
                .load(postItems.get(position).getImage())
                .transition(GenericTransitionOptions.with(animationObject))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageDisp);


        if(postItems.get(position).getType().equals("video")){
            holder.play.setVisibility(View.VISIBLE);
        }else {
            holder.play.setVisibility(View.GONE);
        }


        holder.date_time.setText(postItems.get(position).getPostTime() + " " + postItems.get(position).getPostDate());


        holder.imageDisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postItems.get(position).getType().equals("image")){

                    Intent intent = new Intent(context, ImageDisplayActivity.class);
                    intent.putExtra("screen",screen);
                    intent.putExtra("id",postItems.get(position).getId());
                    intent.putExtra("postId",postItems.get(position).getPostId());
                    intent.putExtra("type",postItems.get(position).getType());
                    intent.putExtra("postTag",postItems.get(position).getPostTag());
                    context.startActivity(intent);
                }
                else  {
                    Intent intent = new Intent(context, VideoDisplayActivity.class);
                    intent.putExtra("screen",screen);
                    intent.putExtra("id", postItems.get(position).getId());
                    intent.putExtra("postId", postItems.get(position).getPostId());
                    intent.putExtra("type", postItems.get(position).getType());
                    intent.putExtra("postTag",postItems.get(position).getPostTag());
                    context.startActivity(intent);

                }

            }
        });



    }

    private static void startPlayingVideo(Context ctx, String CONTENT_URL, PlayerView playerView, int appNameRes) {

        PlayerView pvMain = playerView;

        //BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        //TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        //TrackSelector trackSelectorDef = new DefaultTrackSelector(videoTrackSelectionFactory);
        TrackSelector trackSelectorDef = new DefaultTrackSelector();

        DefaultLoadControl loadControl = new DefaultLoadControl.Builder().setBufferDurationsMs(32*1024, 64*1024, 1024, 1024).createDefaultLoadControl();

        SimpleExoPlayer absPlayerInternal = ExoPlayerFactory.newSimpleInstance(ctx, trackSelectorDef);

        String userAgent = Util.getUserAgent(ctx, ctx.getString(appNameRes));

        DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(ctx,userAgent);
        Uri uriOfContentUrl = Uri.parse(CONTENT_URL);
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(uriOfContentUrl);

        absPlayerInternal.prepare(mediaSource);
        absPlayerInternal.setVolume(0f);
        absPlayerInternal.setPlayWhenReady(true);
        absPlayerInternal.setRepeatMode(Player.REPEAT_MODE_ALL);
        pvMain.setPlayer(absPlayerInternal);

    }

    @Override
    public int getItemCount() {
        return postItems.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        ImageView imageDisp;
        TextView play,date_time;

        public PostViewHolder(@NonNull View itemView) {

            super(itemView);

            imageDisp = itemView.findViewById(R.id.imageDisp);
            date_time=itemView.findViewById(R.id.date_time);
            play = itemView.findViewById(R.id.play);

        }
    }
}

