package com.bezzy.Ui.View.activity.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.Profile;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.UploadHelper;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy.Ui.View.utils.VolleyMultipartRequest;
import com.bezzy_application.R;
import com.iceteck.silicompressorr.SiliCompressor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static com.bezzy.Ui.View.utils.UploadHelper.getFileDataFromDrawable;


public class Video_fragment extends Fragment {

    ImageView  back_image;
    VideoView videoView;
    TextView bufferText;
    EmojiconEditText emojiconEditText;
    TextView upload_post, uploadVideo;
    private static final int REQUEST_PICK_VIDEO = 3;// Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";
    private Uri video;
    private String videoPath;
    Boolean allow;

    // Current playback position (in milliseconds).
    private int mCurrentPosition = 0;
    int MY_SOCKET_TIMEOUT_MS = 150000;
    EmojIconActions emojIcon;
    View rootView;
    //EmojiconEditText emojiconEditText;
    ImageView emojiButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_fragment, container, false);
        videoView = view.findViewById(R.id.video_view);
        emojiconEditText = view.findViewById(R.id.ed_content);
        upload_post = view.findViewById(R.id.upload_post);
        back_image = view.findViewById(R.id.back_image);
        emojiButton=view.findViewById(R.id.emoji_btn);
        rootView=view.findViewById(R.id.root_view);

        uploadVideo = view.findViewById(R.id.upload);

        emojIcon = new EmojIconActions(getActivity(), rootView, emojiconEditText, emojiButton);
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }
            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });
        emojIcon.addEmojiconEditTextList(emojiconEditText);

        videoView.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));

        upload_post.setOnClickListener(new View.OnClickListener() {
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

        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emojiconEditText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),"Please add any content to post",Toast.LENGTH_SHORT).show();
                }else if(allow == true){
                    if (Utility.internet_check(getActivity())) {

                        Utility.displayLoader(getActivity());
                        uploadVideo(APIs.BASE_URL + APIs.POSTVIDEO);

                    } else {

                        Utility.hideLoader(getActivity());
                        Toast.makeText(getActivity(), "No Network!", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(getContext(),"Please choose a video less than 35Mb",Toast.LENGTH_LONG).show();
                }

            }
        });
        //setup the media controller widget and attach it to the video view
        MediaController controller = new MediaController(getContext());

        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);

        return view;
    }

    private void pickVideoFromgallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/mp4");
        startActivityForResult(intent, REQUEST_PICK_VIDEO);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
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

        outState.putInt(PLAYBACK_TIME, videoView.getCurrentPosition());
    }

    private void initializePlayer(Uri uri) {
        // Show the "Buffering..." message while the video loads.
        //bufferText.setVisibility(VideoView.GONE);
        if (uri != null) {
            upload_post.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(uri);
        }
        // Listener for onPrepared() event (runs after the media is prepared).
        videoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {

                        // Hide buffering message.
                        //bufferText.setVisibility(VideoView.INVISIBLE);

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
                    /*String filePath = SiliCompressor.with(getActivity()).compressVideo(data.getData(), destinationDirectory);*/
                    Log.e("FetechedVideo", video.toString());
                    /*videoPath = getPath(video);
                    Log.e("FetechedVideoPath",videoPath);*/


                    initializePlayer(video);
                    checkSize(video);

                }
            }
        } else if (resultCode != Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Sorry, there was an error!", Toast.LENGTH_LONG).show();
        }
    }

    public void checkSize(Uri uri){
        Cursor returnCursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String fileSize = returnCursor.getString(sizeIndex);
        int convertToInt = Integer.parseInt(fileSize);
        int sizeToKb = convertToInt/1024;
        if(sizeToKb>35000){
            allow = false;
        }else{
            allow = true;
        }
    }



    private void uploadVideo(String url) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String response2 = new String(response.data);

                Log.e("RESPONSE2", response2);


                try {
                    JSONObject object = new JSONObject(response2);
                    String status = object.getString("resp");
                    if (status.equals("success")) {

                        String postId = object.getString("post_id");
                        Log.e("postId",postId);
                        callApi(APIs.BASE_URL+APIs.CONTENT_VIDEO,postId);

                    } else {
                        Utility.hideLoader(getActivity());
                        String message = object.getString("message");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.hideLoader(getActivity());
                    Log.e("ImageUploadException", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext().getApplicationContext(), "Please Upload at least one video to Post", Toast.LENGTH_LONG).show();
                Utility.hideLoader(getActivity());
                Log.e("VolleyError", error.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // params.put("tags", "ccccc");  add string parameters
                params.put("userID", Utility.getUserId(getActivity()));
                params.put("post_content", "");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long videoname = System.currentTimeMillis();
                params.put("post_video", new DataPart(videoname + ".mp4", getFileDataFromDrawable(getActivity(), video)));
                Log.e("Value", params.get("post_video").toString());
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

    private void callApi(String s, final String postId) {
        StringRequest request = new StringRequest(Request.Method.POST, s, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("PhotoResponse",response);

                try {
                    JSONObject object = new JSONObject(response);

                    String resp = object.getString("resp");

                    if(resp.equals("success"));
                    {
                        Utility.hideLoader(getActivity());
                        Toast.makeText(getActivity(),object.getString("title"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity().getApplicationContext(), Profile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.hideLoader(getActivity());

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideLoader(getActivity());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("post_id",postId);
                map.put("post_content",emojiconEditText.getText().toString());
                Log.e("POSTCONTENT",map.get("post_content"));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(request);
    }

    private void releasePlayer() {
        videoView.stopPlayback();
    }
}