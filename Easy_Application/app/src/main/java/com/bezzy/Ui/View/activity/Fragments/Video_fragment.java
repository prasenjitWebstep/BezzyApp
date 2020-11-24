package com.bezzy.Ui.View.activity.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.Profile;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.UploadHelper;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy.Ui.View.utils.VolleyMultipartRequest;
import com.bezzy_application.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.bezzy.Ui.View.utils.UploadHelper.getFileDataFromDrawable;


public class Video_fragment extends Fragment {

    ImageView imageView,back_image;
    LinearLayout postUpload;
    VideoView videoView;
    TextView bufferText;
    EditText editText;
    Button pickVideo,uploadVideo,Post_upload;
    private static final int REQUEST_PICK_VIDEO = 3;// Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";
    private Uri video;
    private String videoPath;

    ProgressDialog progressDialog;

    // Current playback position (in milliseconds).
    private int mCurrentPosition = 0;
    int MY_SOCKET_TIMEOUT_MS=150000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_video_fragment, container, false);
        videoView = view.findViewById(R.id.video_vieww);
        editText = view.findViewById(R.id.ed_content);
        postUpload = view.findViewById(R.id.uploadd_video);
        Post_upload=view.findViewById(R.id.upload);
        back_image = view.findViewById(R.id.back_image);

//        bufferText = view.findViewById(R.id.bufferingtext);
        uploadVideo = view.findViewById(R.id.upload_post);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Posting Please wait....");
        progressDialog.setCancelable(false);

        videoView.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));

        /*testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickVideoFromgallery();
            }
        });*/

       uploadVideo.setOnClickListener(new View.OnClickListener() {
         @Override
            public void onClick(View v) {
                pickVideoFromgallery();
            }
        });

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Profile.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        Post_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                uploadVideo( APIs.BASE_URL+ APIs.POSTVIDEO);
            }
        });
        //setup the media controller widget and attach it to the video view
        MediaController controller = new MediaController(getContext());
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);

        return view;
    }

    private void pickVideoFromgallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent,REQUEST_PICK_VIDEO);
    }
    @Override
    public void onPause() {
        super.onPause();
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.N){
            videoView.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PLAYBACK_TIME,videoView.getCurrentPosition());
    }

    private void initializePlayer(Uri uri) {
        // Show the "Buffering..." message while the video loads.
        bufferText.setVisibility(VideoView.GONE);
        if (uri != null){
            postUpload.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(uri);
        }
        // Listener for onPrepared() event (runs after the media is prepared).
        videoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {

                        // Hide buffering message.
                        bufferText.setVisibility(VideoView.INVISIBLE);

                        // Restore saved position, if available.
                        if (mCurrentPosition > 0) {
                            videoView.seekTo(mCurrentPosition);
                        } else {
                            // Skipping to 1 shows the first frame of the video.
                            videoView.seekTo(1);
                        }

                        // Start playing!
                        /*videoView.start();*/
                    }
                });

        // Listener for onCompletion() event (runs after media has finished
        // playing).
        videoView.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
//                        Toast.makeText(getActivity(),
//                                "R.string.toast_message",
//                                Toast.LENGTH_SHORT).show();

                        // Return the video position to the start.
                        videoView.seekTo(0);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_PICK_VIDEO) {
                if (data != null) {
                    //Toast.makeText(getActivity(), "Video content URI: " + data.getData(),Toast.LENGTH_LONG).show();
                    video = data.getData();
                    Log.e("FetechedVideo",video.toString());
                    /*videoPath = getPath(video);
                    Log.e("FetechedVideoPath",videoPath);*/
                    initializePlayer(video);
                    // uploadFile(video.getPath());

                }
            }
        }
        else if (resultCode != Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Sorry, there was an error!", Toast.LENGTH_LONG).show();
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void uploadVideo(String url){
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String response2 = new String(response.data);

                Log.e("RESPONSE2", response2);

                try {
                    JSONObject object = new JSONObject(response2);
                    String status = object.getString("resp");
                    if (status.equals("success")) {

                        progressDialog.dismiss();
                        //
                        String msg = object.getString("message");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity().getApplicationContext(),Profile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        progressDialog.dismiss();
                        String message = object.getString("message");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Log.e("ImageUploadException", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("VolleyError",error.toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // params.put("tags", "ccccc");  add string parameters
                params.put("userID", Utility.getUserId(getActivity()));
                params.put("post_content",editText.getText().toString());
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long videoname = System.currentTimeMillis();
                params.put("post_video", new DataPart(videoname + ".mp4", getFileDataFromDrawable(getActivity(),video)));
                Log.e("Value",params.get("post_video").toString());
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(volleyMultipartRequest);
    }

    private void releasePlayer() {
        videoView.stopPlayback();
    }
}