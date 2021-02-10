package com.bezzy.Ui.View.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.Fragments.Photo_fragment;
import com.bezzy.Ui.View.adapter.Chatbox_adapter;
import com.bezzy.Ui.View.adapter.ImageViewAdapter;
import com.bezzy.Ui.View.model.ChatMessageModel;
import com.bezzy.Ui.View.model.FriendsPostModelImage;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy.Ui.View.utils.VolleyMultipartRequest;
import com.bezzy.Ui.View.utils.VolleyMultipleMultipartRequest;
import com.bezzy_application.R;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.ViewPropertyTransition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class Massage extends AppCompatActivity {

    String name,image,id;
    TextView title_text,timeshow;
    CircleImageView img_logo;
    RecyclerView reyclerview_message_list;
    /*EditText edittext_chatbox;*/
    ImageView send_msg;
    ChatMessageModel messageModel;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ChatMessageModel> modelArrayList;
    int page,i;
    Chatbox_adapter adapter;
    ProgressBar chatProgress;
    EmojIconActions emojIcon;
    View rootView;
    EmojiconEditText edittext_chatbox;
    ImageView emojiButton,photo_btn;
    String date;
    boolean isScrolling;
    ArrayList<Bitmap> bitmapList;
    int option;
    private AlertDialog fullscreenDialog;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massage);
        checkToken();
        name = getIntent().getExtras().getString("userName");
        image = getIntent().getExtras().getString("userImage");
        id = getIntent().getExtras().getString("FrndId");
        title_text = findViewById(R.id.title_text);
        timeshow = findViewById(R.id.timeshow);
        img_logo = findViewById(R.id.img_logo);
        reyclerview_message_list = findViewById(R.id.reyclerview_message_list);
        edittext_chatbox = findViewById(R.id.edittext_chatbox);
        send_msg = findViewById(R.id.send_msg);
        chatProgress = findViewById(R.id.chatProgress);
        emojiButton = (ImageView) findViewById(R.id.emoji_btn);
        rootView = findViewById(R.id.root_view);
        isScrolling = true;
        photo_btn = findViewById(R.id.photo_btn);
        flag = true;
        /*emojiconEditText=findViewById(R.id.edittext_chatbox);*/

        modelArrayList = new ArrayList<>();
        bitmapList = new ArrayList<>();
        page = 1;

        linearLayoutManager = new LinearLayoutManager(Massage.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(false);
        reyclerview_message_list.setLayoutManager(linearLayoutManager);

        photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Massage.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Massage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }
                pickImageFromGallery();
            }
        });


        reyclerview_message_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(isScrolling == true){

                    if (!recyclerView.canScrollVertically(-1)) {

                        if(Utility.internet_check(Massage.this)){

                            page++;

                            chatProgress.setVisibility(View.VISIBLE);

                            chatList(APIs.BASE_URL+APIs.CHAT_LIST+"/"+Utility.getUserId(Massage.this)+"/"+id+"/"+String.valueOf(page));


                        }
                        else {

                            chatProgress.setVisibility(View.GONE);
                            Toast.makeText(Massage.this,"No Network!",Toast.LENGTH_SHORT).show();
                        }


                    }

                }

            }
        });


        chatList(APIs.BASE_URL+APIs.CHAT_LIST+"/"+Utility.getUserId(Massage.this)+"/"+id+"/"+String.valueOf(page));

        if(flag == true){
            callApi();
        }

        
        title_text.setText(name);

        ViewPropertyTransition.Animator animationObject = new ViewPropertyTransition.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha(0f);

                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(1500);
                fadeAnim.start();
            }
        };

        Glide.with(Massage.this)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(animationObject))
                .into(img_logo);



        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!edittext_chatbox.getText().toString().isEmpty()){
                    try {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    if(Utility.internet_check(Massage.this)) {

                        addchat(APIs.BASE_URL+APIs.ADDCHAT);
                    }
                    else {

                        Toast.makeText(Massage.this,"No Network!",Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });

        emojIcon = new EmojIconActions(this, rootView, edittext_chatbox, emojiButton);
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
        emojIcon.addEmojiconEditTextList(edittext_chatbox);

    }

    private void checkToken() {
        StringRequest request = new StringRequest(Request.Method.GET, APIs.BASE_URL+APIs.MEMBER_TOKEN+"/"+Utility.getUserId(Massage.this), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    if(Utility.getUserToken(Massage.this).equals(object.getString("remember_token"))){


                    }else{
                        Utility.logoutFunction(Massage.this);
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

        RequestQueue queue = Volley.newRequestQueue(Massage.this);
        queue.add(request);
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 101:
                    option = 1001;
                    if (resultCode == RESULT_OK && data != null) {
                        bitmapList = new ArrayList<>();
                        ClipData clipData = data.getClipData();
                        if (clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                Uri imageUri = clipData.getItemAt(i).getUri();
                                try {
                                    Bitmap bitmap = Utility.handleSamplingAndRotationBitmap(Massage.this,imageUri);
                                    if(bitmapList.size()<5){
                                        bitmapList.add(bitmap);
                                    }else{
                                        Toast.makeText(Massage.this,"Should not exceed more than 5 images",Toast.LENGTH_SHORT).show();
                                    }
                                }catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Uri imageUri = null;
                            imageUri = data.getData();
                            bitmapList = new ArrayList<>();
                            bitmapList.clear();
                            try {
                                Bitmap bitmap = Utility.handleSamplingAndRotationBitmap(Massage.this,imageUri);
                                bitmapList.add(bitmap);
                            }catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        showImageFullScreenDialog(bitmapList);
                        /*recyclerDisplayImg.setAdapter(new ImageViewAdapter(Massage.this, bitmapList));*/
                    }
                    break;
            }
        }
    }

    private void showImageFullScreenDialog(ArrayList<Bitmap> bitmapList) {

        RecyclerView recyclerImageShow;
        Button submitButton;

        AlertDialog.Builder builder = new AlertDialog.Builder(Massage.this,R.style.MaterialTheme);
        View v= LayoutInflater.from(Massage.this).inflate(R.layout.message_image_layout,null);
        recyclerImageShow = v.findViewById(R.id.recyclerImageShow);
        submitButton = v.findViewById(R.id.submitButton);

        if(bitmapList.size()<2){
            Log.e("CHECK","Entered");
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Massage.this);
            recyclerImageShow.setLayoutManager(linearLayoutManager);
            recyclerImageShow.setAdapter(new ImageViewAdapter(Massage.this, bitmapList));
        }else{
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
            recyclerImageShow.setLayoutManager(layoutManager);
            recyclerImageShow.setAdapter(new ImageViewAdapter(Massage.this, bitmapList));
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.internet_check(Massage.this)) {

                    new UploadImageTask2().execute();

                } else {

                    Toast.makeText(Massage.this, "No Network!", Toast.LENGTH_SHORT).show();

                }

            }
        });


        builder.setView(v);
        builder.setCancelable(true);
        fullscreenDialog=builder.create();
        fullscreenDialog.show();

    }

    public class UploadImageTask2 extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {

            Utility.notifyUpload(Massage.this,false,"Image Upload","Uploading in Progress");
            fullscreenDialog.dismiss();
            /*Intent intent = new Intent(Massage.this,Profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/

        }
        @Override
        protected Void doInBackground(Void... params) {

            sendImage(APIs.BASE_URL + APIs.ADDCHATIMAGE);

            return null;
        }

        protected void onPostExecute(Void result) {

        }
    }

    public void sendImage(String url) {
        VolleyMultipleMultipartRequest multipartRequest = new VolleyMultipleMultipartRequest(Request.Method.POST,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

              /**/  /*Log.e("Response", response.toString());*/

                try {


                    String resp = response.getString("status");
                    if(resp.equals("success")){
                        Utility.notifyUpload(Massage.this,true,"Image Upload","Image Send Successfully");
                        page = 1;
                        chatList(APIs.BASE_URL+APIs.CHAT_LIST+"/"+Utility.getUserId(Massage.this)+"/"+id+"/"+String.valueOf(page));
                        modelArrayList.clear();
                    }else{
                        String message = response.getString("message");
                        Utility.notifyUpload(Massage.this,true,"Image Upload",message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.notifyUpload(Massage.this,true,"Image Upload","Exception: "+" \n "+e.toString());
                    Log.e("exception",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Utility.notifyUpload(Massage.this,true,"Image Upload","Error: "+" \n "+error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("from_userID",Utility.getUserId(Massage.this));
                params.put("to_userID",id);
                return params;
            }

            @Override
            protected Map<String, ArrayList<DataPart>> getByteData() {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                Map<String, ArrayList<DataPart>> imageList = new HashMap<>();
                ArrayList<DataPart> dataPart = new ArrayList<>();
                long imagename = System.currentTimeMillis();
                for (int i = 0; i < bitmapList.size(); i++) {
                    VolleyMultipleMultipartRequest.DataPart dp = new VolleyMultipleMultipartRequest.DataPart(+imagename + i + ".jpeg", getFileDataFromDrawable(bitmapList.get(i)));
                    dataPart.add(dp);
                    //params.put(imagename, new DataPart(imagename+i, Base64.decode(encodedImageList.get(i), Base64.DEFAULT), "image/jpeg"));
                }
                imageList.put("chat_image[]", dataPart);

                for (DataPart dataPart1 : dataPart) {
                    /*Log.e("Value", dataPart1.getFileName());*/
                }

                return imageList;
            }
        };
        int socketTimeout = 500000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
        RequestQueue rQueue = Volley.newRequestQueue(Massage.this);
        rQueue.add(multipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void callApi() {
        if(Utility.internet_check(Massage.this)) {

            if(Utility.internet_check(Massage.this)) {

                instantChat(APIs.BASE_URL+APIs.INSTANT_MSG+"/"+Utility.getUserId(Massage.this)+"/"+id+"/"+"1");
                /*messageStatUpdate(APIs.BASE_URL+APIs.GET_MESSAGE_SEEN);*/
            }
            else {

                Toast.makeText(Massage.this,"No Network!",Toast.LENGTH_SHORT).show();

            }


        }
        else {

            Toast.makeText(Massage.this,"No Network!",Toast.LENGTH_SHORT).show();

        }

        if(flag == true){
            refresh(2000);
        }

    }

    private void instantChat(String url) {

        /*Log.e("URL",url);*/

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                /*Log.e("InstantChatResponse",response);*/

                try {
                    JSONObject object = new JSONObject(response);
                    String sucess = object.getString("status");
                    if(sucess.equals("success")){
                        page = 1;
                        chatList(APIs.BASE_URL+APIs.CHAT_LIST+"/"+Utility.getUserId(Massage.this)+"/"+id+"/"+String.valueOf(page));
                        modelArrayList.clear();

                        /*JSONArray array = object.getJSONArray("chat_history_list");
                        for(int i = 0;i< array.length(); i++){
                            JSONObject object1 = array.getJSONObject(i);
                            messageModel = new ChatMessageModel(object1.getString("message_by"),
                                    object1.getString("chat_message"),
                                    object1.getString("chat_msg_time"),
                                    object1.getString("chat_read_unread_status"));
                            modelArrayList.add(0,messageModel);
                        }
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        reyclerview_message_list.setLayoutManager(linearLayoutManager);
                        adapter = new Chatbox_adapter(Massage.this,modelArrayList);
                        adapter.notifyItemInserted(0);
                        adapter.notifyItemChanged(0);
                        adapter.notifyDataSetChanged();
                        reyclerview_message_list.setAdapter(adapter);*/
                    }

                } catch (JSONException e) {
                    Log.e("Exception",e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error",error.toString());
            }
        });


        int socketTimeout = 500000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        RequestQueue rQueue = Volley.newRequestQueue(Massage.this);
        rQueue.add(request);

    }


    private void refresh(int i) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                callApi();
            }
        };

        handler.postDelayed(runnable,i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        flag = false;
    }

    private void addchat(String url) {

        /*Log.e("URL",url);*/

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                /*Log.e("AddChatResponse",response);*/

                try {
                    JSONObject object = new JSONObject(response);
                    String sucess = object.getString("status");
                    if(sucess.equals("success")){

                        edittext_chatbox.getText().clear();

                        JSONArray array = object.getJSONArray("chat_history_list");
                        for(int i = 0;i< array.length(); i++){
                            JSONObject object1 = array.getJSONObject(i);
                            messageModel = new ChatMessageModel(object1.getString("message_by"),
                                    object1.getString("chat_message"),
                                    object1.getString("chat_msg_time"),
                                    object1.getString("chat_read_unread_status"),
                                    object1.getString("chat_date_time"),
                                    object1.getString("type"));
                            modelArrayList.add(0,messageModel);
                        }
                        linearLayoutManager = new LinearLayoutManager(Massage.this);
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(false);
                        linearLayoutManager.setSmoothScrollbarEnabled(true);
                        reyclerview_message_list.setLayoutManager(linearLayoutManager);
                        adapter = new Chatbox_adapter(Massage.this,modelArrayList);
                        adapter.notifyItemInserted(0);
                        adapter.notifyItemChanged(0);
                        adapter.notifyDataSetChanged();
                        reyclerview_message_list.setAdapter(adapter);

                        /*JSONArray array = object.getJSONArray("chat_history_list");
                        for(int i = 0;i< array.length(); i++){
                            JSONObject object1 = array.getJSONObject(i);
                            messageModel = new ChatMessageModel(object1.getString("message_by"),
                                    object1.getString("chat_message"),
                                    object1.getString("chat_date_time"));
                            modelArrayList.add(messageModel);
                        }
                        reyclerview_message_list.setAdapter(new Chatbox_adapter(Massage.this,modelArrayList));*//*

                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        reyclerview_message_list.setLayoutManager(linearLayoutManager);
                        adapter = new Chatbox_adapter(Massage.this,modelArrayList);
                        adapter.notifyItemInserted(0);
                        adapter.notifyDataSetChanged();
                        reyclerview_message_list.setAdapter(adapter);*/

                    }

                } catch (JSONException e) {
                    Log.e("Exception",e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("from_userID",Utility.getUserId(Massage.this));
                map.put("to_userID",id);
                map.put("chat_message",edittext_chatbox.getText().toString());
                /*Log.e("CHAT",map.get("chat_message"));*/
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Massage.this);
        queue.add(request);
    }

    private void chatList(String url) {

        /*Log.e("URL",url);*/

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               /* Log.e("ChatResponse",response);*/

                try {
                    JSONObject object = new JSONObject(response);
                    String sucess = object.getString("status");
                    if(sucess.equals("success")){
                        chatProgress.setVisibility(View.GONE);
                        JSONArray array = object.getJSONArray("chat_history_list");
                        for(int i = 0;i< array.length(); i++){
                            JSONObject object1 = array.getJSONObject(i);
                            messageModel = new ChatMessageModel(object1.getString("message_by"),
                                    object1.getString("chat_message"),
                                    object1.getString("chat_msg_time"),
                                    object1.getString("chat_read_unread_status"),
                                    object1.getString("chat_date_time"),
                                    object1.getString("type"));

                            /*date = object1.getString("chat_date_time");*/

                            modelArrayList.add(modelArrayList.size(),messageModel);
                        }

                        if(page != 1){
                            Log.e("Called","If()");
                            linearLayoutManager = new LinearLayoutManager(Massage.this);
                            linearLayoutManager.setReverseLayout(true);
                            linearLayoutManager.setStackFromEnd(true);
                            linearLayoutManager.setSmoothScrollbarEnabled(true);
                            reyclerview_message_list.setLayoutManager(linearLayoutManager);
                        }else{
                            linearLayoutManager = new LinearLayoutManager(Massage.this);
                            linearLayoutManager.setReverseLayout(true);
                            linearLayoutManager.setStackFromEnd(false);
                            reyclerview_message_list.setLayoutManager(linearLayoutManager);
                        }
                        adapter = new Chatbox_adapter(Massage.this,modelArrayList);
                        adapter.notifyDataSetChanged();
                        reyclerview_message_list.setAdapter(adapter);
                    }else{
                        isScrolling = false;
                        chatProgress.setVisibility(View.GONE);
                        /*Toast.makeText(Massage.this,"End of List",Toast.LENGTH_SHORT).show();*/

                    }

                } catch (JSONException e) {
                    Log.e("Exception",e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error",error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(Massage.this);
        queue.add(request);
    }

}