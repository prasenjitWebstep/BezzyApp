package com.bezzy.Ui.View.activity.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.Editprofile;
import com.bezzy.Ui.View.activity.Profile;
import com.bezzy.Ui.View.adapter.ImageViewAdapter;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy.Ui.View.utils.VolleyMultipartRequest;
import com.bezzy.Ui.View.utils.VolleyMultipleMultipartRequest;
import com.bezzy_application.R;
import com.google.android.material.textfield.TextInputLayout;
import com.hendraanggrian.appcompat.socialview.Mention;
import com.hendraanggrian.appcompat.widget.MentionArrayAdapter;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static android.app.Activity.RESULT_OK;


public class Photo_fragment extends Fragment {
    ImageView imageView, back_image;
    TextView button,uoload;
    SocialAutoCompleteTextView txt;
    String base64String;
    String filePath;
    //Image request code
    private static final int IMAGE_PICK_CODE = 1001;
    private static final int CAMERA_PICK = 101;
    private static final int PERMISSION_CODE = 1001;
    //EditText caption;
    Bitmap bitmap;
    Uri resultUri;
    int MY_SOCKET_TIMEOUT_MS = 10000;
    LinearLayout image_part;
    RecyclerView recyclerDisplayImg;
    ArrayList<Bitmap> bitmapList;
    int option;
    //EmojIconActions emojIcon;
    ImageView emojiButton;
    View rootView;
    //EmojiconEditText emojiconEditText;
    Uri imageuri;
    Context context;
    Uri mUri;

    String MENTION1_USERNAME;
    //ArrayList<String> strings;
    private static final String MENTION2_USERNAME = "sagnik";
    private static final String MENTION3_USERNAME = "prasenjit";
    private static final String MENTION2_DISPLAYNAME = "polman";
    private static final String MENTION3_DISPLAYNAME = "Hendra Anggrian";
    private ArrayAdapter<Mention> defaultMentionAdapter;
    ArrayList<String> idLst;


    public Photo_fragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_fragment, container, false);
        back_image = view.findViewById(R.id.back_image);
        imageView = view.findViewById(R.id.imageView);
        // button = view.findViewById(R.id.choose_image_button);
        //caption = view.findViewById(R.id.ed_content);
        button = view.findViewById(R.id.upload);
        //image_part = view.findViewById(R.id.image_part);

        recyclerDisplayImg = view.findViewById(R.id.recyclerDisplayImg);
        uoload = view.findViewById(R.id.upload_post);

        rootView = view.findViewById(R.id.root_view);
        emojiButton =view.findViewById(R.id.emoji_btn);
        txt =view.findViewById(R.id.ed_content);
        bitmapList = new ArrayList<>();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerDisplayImg.setLayoutManager(layoutManager);

        followingUserList(APIs.BASE_URL+APIs.FOLLOWINGLIST);

//        emojIcon = new EmojIconActions(context, rootView, emojiconEditText, emojiButton);
//        emojIcon.ShowEmojIcon();
//        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
//            @Override
//            public void onKeyboardOpen() {
//                Log.e("Keyboard", "open");
//
//            }
//            @Override
//            public void onKeyboardClose() {
//                Log.e("Keyboard", "close");
//            }
//        });
//        emojIcon.addEmojiconEditTextList(emojiconEditText);

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Profile.class);
                startActivity(i);
                ((Activity) context).overridePendingTransition(0, 0);
            }
        });
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
//                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
//                    requestPermissions(permissions,PERMISSION_CODE);
//                }else{
//                    pickImageFromGallery();
//                }
//            }
//        });
        uoload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }
                pickImageFromGallery();

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt.getText().toString().isEmpty()){
                    Toast.makeText(context,"Please add any content to post",Toast.LENGTH_SHORT).show();
                }else if(bitmapList.size() == 0){
                    Toast.makeText(getContext(), "Please Upload at least one image to Post", Toast.LENGTH_LONG).show();
                }else{
                    if (Utility.internet_check(context)) {

                        switch (option) {
                            case 101:
                                new UploadImageTask().execute();
                                break;
                            case 1001:
                                new UploadImageTask2().execute();
                                break;
                        }

                    } else {

                        Toast.makeText(context, "No Network!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
        return view;
    }

    private void pickImageFromGallery() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
                    startActivityForResult(takePicture, CAMERA_PICK);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, IMAGE_PICK_CODE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA_PICK:
                    option = 101;

                    if (resultCode == RESULT_OK && data != null) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        Bitmap bitmap2 = BitmapFactory.decodeFile(String.valueOf(imageuri), options);
                        bitmapList = new ArrayList<>();
                        bitmap = (Bitmap) data.getExtras().get("data");
                        bitmapList.add(bitmap);
                        bitmapList.add(bitmap2);
                        recyclerDisplayImg.setAdapter(new ImageViewAdapter(context, bitmapList));
                    }
                    break;
                case IMAGE_PICK_CODE:
                    option = 1001;
                    if (resultCode == RESULT_OK && data != null) {
                        bitmapList = new ArrayList<>();
                        ClipData clipData = data.getClipData();
                        if (clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                Uri imageUri = clipData.getItemAt(i).getUri();
                                try {
                                    InputStream is = context.getContentResolver().openInputStream(imageUri);
                                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                                    if(bitmapList.size()<5){
                                        bitmapList.add(bitmap);
                                    }else{
                                        Toast.makeText(context,"Should not exceed more than 5 images",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Uri imageUri = data.getData();
                            try {
                                InputStream is = context.getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(is);
                                bitmapList.add(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                        recyclerDisplayImg.setAdapter(new ImageViewAdapter(context, bitmapList));
                    }
                    break;
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public class UploadImageTask extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {

            Utility.notifyUpload(context,false,"Image Upload","Uploading in Progress");
            Intent intent = new Intent(context,Profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
        @Override
        protected Void doInBackground(Void... params) {

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, APIs.BASE_URL + APIs.POSTIMAGE,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            String response2 = new String(response.data);
                            Log.e("RESPONSE2", response2);
                            try {
                                JSONObject object = new JSONObject(response2);
                                String status = object.getString("resp");
                                if (status.equals("success")) {

                                    final String postId = object.getString("post_id");
                                    Log.e("postId",postId);

                                    callApi(APIs.BASE_URL+APIs.CONTENT_POST,postId);


                                } else {
                                    String message = object.getString("message");
                                    Utility.notifyUpload(context,true,"Image Upload",message);
                                }
//
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Utility.notifyUpload(context,true,"Image Upload","Exception: + \n +"+e.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("GotError", "" + error.getMessage());
                            Utility.notifyUpload(context,true,"Image Upload","Error: + \n +"+error.toString());
                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("userID", Utility.getUserId(context));
                    params.put("post_content", "");

                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("post_image[]", new DataPart(+imagename + ".jpeg", getFileDataFromDrawable(bitmap)));
                    return params;
                }

            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue rQueue = Volley.newRequestQueue(context);
            rQueue.add(volleyMultipartRequest);

            return null;
        }

        protected void onPostExecute(Void result) {

        }
    }

    public class UploadImageTask2 extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {

            Utility.notifyUpload(context,false,"Image Upload","Uploading in Progress");
            Intent intent = new Intent(context,Profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
        @Override
        protected Void doInBackground(Void... params) {

            upload(APIs.BASE_URL + APIs.POSTIMAGE);

            return null;
        }

        protected void onPostExecute(Void result) {

        }
    }

    public void upload(String url) {
        VolleyMultipleMultipartRequest multipartRequest = new VolleyMultipleMultipartRequest(Request.Method.POST,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("Response", response.toString());

                try {


                    String resp = response.getString("resp");
                    if(resp.equals("success")){
                        String postId = response.getString("post_id");
                        Log.e("postId",postId);
                        callApi(APIs.BASE_URL+APIs.CONTENT_POST,postId);
                    }else{
                        String message = response.getString("message");
                        Utility.notifyUpload(context,true,"Image Upload",message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.notifyUpload(context,true,"Image Upload","Exception: + \n +"+e.toString());
                    Log.e("exception",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Utility.notifyUpload(context,true,"Image Upload","Error: + \n +"+error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userID", Utility.getUserId(context));
                params.put("post_content", "");
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
                imageList.put("post_image[]", dataPart);

                for (DataPart dataPart1 : dataPart) {
                    Log.e("Value", imageList.get("post_image[]").toString());
                }

                return imageList;
            }
        };
        int socketTimeout = 500000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(multipartRequest);
    }

    private void callApi(String s, final String postId) {

        StringRequest request = new StringRequest(Request.Method.POST,s , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("PhotoResponse",response);

                try {
                    JSONObject object = new JSONObject(response);

                    String resp = object.getString("resp");
                    if(resp.equals("success")){
                        Utility.notifyUpload(context,true,"Image Upload",object.getString("message"));
                    }else{
                        Utility.notifyUpload(context,true,"Image Upload",object.getString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Exception",e.toString());
                    Utility.notifyUpload(context,true,"Image Upload","Exception: + \n +"+e.toString());
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
                Utility.notifyUpload(context,true,"Image Upload","Error: + \n +"+error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("post_id",postId);
                map.put("post_content",txt.getText().toString());
                Log.e("POSTCONTENT",map.get("post_content"));
                return map;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);

    }

    private void followingUserList(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("REsponse",response);
                //progressDialog.dismiss();


                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("success")){
//                        progressBar.setVisibility(View.GONE);
                        //dataholder.clear();
                        JSONArray array = object.getJSONArray("following_user_list");
                        defaultMentionAdapter = new MentionArrayAdapter<>(getContext());
                        idLst = new ArrayList<>();
                        for(int i = 0 ;i<array.length();i++){
                            JSONObject object1 = array.getJSONObject(i);
                            Log.e("HIHIHIHI",object1.getString("name"));
                            //ob1 = new Friendsnoti_item(object1.getString("name"),object1.getString("user_bio"),object1.getString("image"),object1.getString("user_id"),object1.getString("user_is_flollowers"));
                            //dataholder.add(ob1);
//                            strings = new ArrayList<String>();
//                            strings.add(object1.getString("name"));
                            MENTION1_USERNAME = object1.getString("name");
                            String id = object1.getString("id");
                            defaultMentionAdapter.add(
                                    new Mention(MENTION1_USERNAME)
                            );
                        }

                        txt.setMentionAdapter(defaultMentionAdapter);

                    }
                } catch (JSONException e) {
                    Log.e("Exception",e.toString());
                    e.printStackTrace();
                    //progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Exception",error.toString());
                //progressDialog.dismiss();
                //progressBar.setVisibility(View.GONE);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("loguser_id", Utility.getUserId(getActivity()));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}