package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.potyvideo.library.AndExoPlayerView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class Editprofile extends AppCompatActivity {
    TextInputEditText ed_name, ed_username, ed_email,ed_dob,ed_bio,ed_gender;
    Spinner spinner;
    Button btn_update;
    ImageView imageView;
    DatePickerDialog datePickerDialog;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    SpotsDialog progressDialog;
    CircleImageView profile_image;
    int MY_SOCKET_TIMEOUT_MS = 10000;
    Uri resultUri;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        profile_image = findViewById(R.id.profile_image);
        btn_update=findViewById(R.id.update);
        ed_dob = findViewById(R.id.dob);
        ed_gender = findViewById(R.id.ed_gender);
        ed_email = findViewById(R.id.email);
        ed_name = findViewById(R.id.fullname);
        ed_username = findViewById(R.id.username);
        ed_bio = findViewById(R.id.bio);

        Glide.with(Editprofile.this).load(Utility.getImage(Editprofile.this)).into(profile_image);

        ed_username.setText(Utility.getUserName(Editprofile.this));
        ed_name.setText(Utility.getName(Editprofile.this));
        ed_email.setText(Utility.getEmail(Editprofile.this));
        ed_dob.setText(Utility.getdob(Editprofile.this));
        ed_gender.setText(Utility.getGender(Editprofile.this));
        if(!Utility.getBio(Editprofile.this).equals("null")){
            ed_bio.setText(Utility.getBio(Editprofile.this));
        }
        //str_gender.equals(Utility.getGender(Editprofile.this));


       /* progressDialog = new SpotsDialog(Editprofile.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait..");*/


        textInputEmail = findViewById(R.id.text_input_email);
        textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);

        imageView=findViewById(R.id.back_image);

        if(!Utility.getSocial(Editprofile.this).equals("1")){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Editprofile.this,Profile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }




        /*ed_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog=new DatePickerDialog(Editprofile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = month + " - " + dayOfMonth + " - " + year;
                        ed_dob.setText(date);

                    }
                },month,day,year);
                datePickerDialog.show();

            }
        });*/

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setOutputCompressQuality(25)
                        .start(Editprofile.this);

            }
        });

       /* Spinner spinner = (Spinner) findViewById(R.id.spinnerr);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);*/


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updateprofile();
            }
        });

    }
    private void callApiUpdateProfile(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("success")){
                        //progressDialog.dismiss();
                        Utility.hideLoader(Editprofile.this);
                        Toast.makeText(Editprofile.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Editprofile.this, Profile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);


                    }else{
                        Utility.hideLoader(Editprofile.this);
                        //progressDialog.dismiss();
                        //Toast.makeText(Editprofile.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                   // progressDialog.dismiss();
                    Utility.hideLoader(Editprofile.this);
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Utility.hideLoader(Editprofile.this);
                Log.e("Error",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("userID",Utility.getUserId(Editprofile.this));
                map.put("username",ed_username.getText().toString());
                map.put("fullname",ed_name.getText().toString());
                map.put("email",ed_email.getText().toString());
                map.put("dob",ed_dob.getText().toString());
                map.put("gender",ed_gender.getText().toString());
                map.put("user_bio",ed_bio.getText().toString());

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Editprofile.this);
        queue.add(request);


    }

    private void uploadImage(final Bitmap bitmap, String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("true")){
                        //progressDialog.dismiss();
                        Utility.hideLoader(Editprofile.this);

                        Toast.makeText(Editprofile.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Editprofile.this,Profile.class));

                    }else{
                        //progressDialog.dismiss();
                        Utility.hideLoader(Editprofile.this);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressDialog.dismiss();
                    Utility.hideLoader(Editprofile.this);
                    Log.e("Exception",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Utility.hideLoader(Editprofile.this);
                Log.e("Error",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("userID",Utility.getUserId(Editprofile.this));
                map.put("profile_picture",getEncoded64ImageStringFromBitmap(bitmap));
                Log.e("image",map.get("profile_picture"));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Editprofile.this);
        queue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK ) {
                resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(Editprofile.this.getContentResolver(), resultUri);
                    if(!progressDialog.isShowing()){
                       /* progressDialog.setMessage("Uploading Image Please Wait.....");
                        progressDialog.setCancelable(false);
                        progressDialog.show();*/
                        Utility.displayLoader(Editprofile.this);
                        uploadImage(bitmap, APIs.BASE_URL+APIs.PERSONALIMAGEUPDATE);
                    }
                    profile_image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Exception",e.toString());
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("ExceptionError",error.toString());
            }
        }
    }

    public void Updateprofile(){
        if(Utility.internet_check(Editprofile.this)) {
            //progressDialog.show();
            Utility.displayLoader(Editprofile.this);
            callApiUpdateProfile(APIs.BASE_URL+APIs.UPDATEPROFILE);
        }
        else {
            //progressDialog.dismiss();
            Utility.hideLoader(Editprofile.this);
            Toast.makeText(Editprofile.this,"No Network!",Toast.LENGTH_SHORT).show();
        }


    }


}