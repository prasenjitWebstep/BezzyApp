package com.bezzy.Ui.View.activity.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bezzy.Ui.View.activity.Profile;
import com.bezzy.Ui.View.adapter.ImageViewAdapter;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy.Ui.View.utils.VolleyMultipartRequest;
import com.bezzy.Ui.View.utils.VolleyMultipleMultipartRequest;
import com.bezzy_application.R;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class Photo_fragment extends Fragment {
    ImageView imageView,back_image;
    Button button;
    String base64String;
    String filePath;
    String caption_upload;
    //Image request code
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    EditText caption;
    Bitmap bitmap;
    Uri resultUri;
    int MY_SOCKET_TIMEOUT_MS=10000;
    LinearLayout image_part;
    ProgressDialog progressDialog;
    RecyclerView recyclerDisplayImg;
    ArrayList<Bitmap> bitmapList;



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
        caption = view.findViewById(R.id.ed_content);
        caption_upload = caption.getText().toString();
        button = view.findViewById(R.id.upload);
        //image_part = view.findViewById(R.id.image_part);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Posting Please wait....");
        progressDialog.setCancelable(false);
        recyclerDisplayImg = view.findViewById(R.id.recyclerDisplayImg);
        bitmapList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        recyclerDisplayImg.setLayoutManager(linearLayoutManager);

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Profile.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setType("image/*");
                startActivityForResult(intent,1);

            }
        });




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(caption.getText().toString().isEmpty()){
                    caption_upload = "null";
                }
                if(Utility.internet_check(getActivity())) {

                    progressDialog.show();

                    upload(APIs.BASE_URL+APIs.POSTIMAGE);

                }
                else {

                    progressDialog.dismiss();

                    Toast.makeText(getActivity(),"No Network!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 & resultCode == RESULT_OK) {
            bitmapList = new ArrayList<>();
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    try {
                        InputStream is = getActivity().getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bitmapList.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Uri imageUri = data.getData();
                try {
                    InputStream is = getActivity().getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    bitmapList.add(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            recyclerDisplayImg.setAdapter(new ImageViewAdapter(getActivity(),bitmapList));

        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void upload(String url){
        VolleyMultipleMultipartRequest multipartRequest = new VolleyMultipleMultipartRequest(Request.Method.POST,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("Response",response.toString());

                try {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity().getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(),Profile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError",error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity().getApplicationContext(), "Unable to upload", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userID", Utility.getUserId(getActivity().getApplicationContext()));
                params.put("post_content", caption_upload);
                return params;
            }

            @Override
            protected Map<String, ArrayList<DataPart>> getByteData() {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                Map<String, ArrayList<DataPart>> imageList = new HashMap<>();
                ArrayList<DataPart> dataPart = new ArrayList<>();
                long imagename = System.currentTimeMillis();
                for (int i=0; i<bitmapList.size(); i++){
                    VolleyMultipleMultipartRequest.DataPart dp = new VolleyMultipleMultipartRequest.DataPart(+imagename+i+".jpeg", getFileDataFromDrawable(bitmapList.get(i)));
                    dataPart.add(dp);
                    //params.put(imagename, new DataPart(imagename+i, Base64.decode(encodedImageList.get(i), Base64.DEFAULT), "image/jpeg"));
                }
                imageList.put("post_image[]", dataPart);

                for(DataPart dataPart1 : dataPart){
                    Log.e("Value",imageList.get("post_image[]").toString());
                }

                return imageList;
            }
        };
        int socketTimeout = 500000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
        RequestQueue rQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        rQueue.add(multipartRequest);
    }
}