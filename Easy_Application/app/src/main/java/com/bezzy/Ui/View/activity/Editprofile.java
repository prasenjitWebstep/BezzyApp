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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class Editprofile extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
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
    int day,month,year;
    String str_gender;
    LinearLayout spinnerLayut;
    TextInputLayout textGenderLayout;
    ProgressBar progressBar;

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
        spinner = findViewById(R.id.spinner);
        spinnerLayut = findViewById(R.id.spinnerLayut);
        textGenderLayout = findViewById(R.id.textGenderLayout);
        progressBar =findViewById(R.id.progressBar);

        Glide.with(Editprofile.this).load(getIntent().getExtras().getString("image")).into(profile_image);

        ed_username.setText(getIntent().getExtras().getString("username"));
        ed_name.setText(getIntent().getExtras().getString("name"));
        ed_email.setText(getIntent().getExtras().getString("email"));
        ed_dob.setText(getIntent().getExtras().getString("dob"));
        ed_gender.setText(getIntent().getExtras().getString("gender"));
        if(!getIntent().getExtras().getString("bio").equals("null")){
            ed_bio.setText(getIntent().getExtras().getString("bio"));
        }
        str_gender="null";




        if(getIntent().getExtras().getString("dob").equalsIgnoreCase("null") ||
                getIntent().getExtras().getString("dob").equals("")){
            ed_dob.getText().clear();
            ed_dob.setFocusable(true);
            ed_dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar cal = Calendar.getInstance();
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    month = cal.get(Calendar.MONTH);
                    year = cal.get(Calendar.YEAR);

                    set();
                }
            });
        }

      /*  if(getIntent().getExtras().getString("gender").equalsIgnoreCase("null") ||
                getIntent().getExtras().getString("gender").equals("")){
            textGenderLayout.setVisibility(View.GONE);
            spinnerLayut.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.gender, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
        }*/



        textInputEmail = findViewById(R.id.text_input_email);
        textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);

        imageView=findViewById(R.id.back_image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Editprofile.this,Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });





        if(!Utility.getSocial(Editprofile.this).equals("1")){

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

        }


        Spinner spinner =findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updateprofile();
            }
        });
        ed_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);

                set();

            }
        });


    }

    public void set(){
        DatePickerDialog datePickerDialog=new DatePickerDialog(Editprofile.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                ed_dob.setText(i2+"-"+(i1+1)+"-"+i);
            }
        },year,month,day);

        Calendar cal = Calendar.getInstance();

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        cal.add(Calendar.DATE, 0);

        datePickerDialog.show();
    }

    private void callApiUpdateProfile(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response);

                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("success")){
                        //progressDialog.dismiss();
                        //Utility.hideLoader(Editprofile.this);
                        progressBar.setVisibility(View.GONE);
                        btn_update.setVisibility(View.VISIBLE);
                        Toast.makeText(Editprofile.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Editprofile.this, Profile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);


                    }else{
                        progressBar.setVisibility(View.GONE);
                        btn_update.setVisibility(View.VISIBLE);
                        //Utility.hideLoader(Editprofile.this);
                        //progressDialog.dismiss();
                        //Toast.makeText(Editprofile.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                   // progressDialog.dismiss();
                   // Utility.hideLoader(Editprofile.this);
                    progressBar.setVisibility(View.GONE);
                    btn_update.setVisibility(View.VISIBLE);
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                //Utility.hideLoader(Editprofile.this);
                progressBar.setVisibility(View.GONE);
                btn_update.setVisibility(View.VISIBLE);
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
                if(getIntent().getExtras().getString("gender").equals("null")){
                    map.put("gender",str_gender);
                }else{
                    map.put("gender",ed_gender.getText().toString());
                }
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
                        //Utility.hideLoader(Editprofile.this);
                        progressBar.setVisibility(View.GONE);
                        btn_update.setVisibility(View.VISIBLE);

                        Toast.makeText(Editprofile.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Editprofile.this,Profile.class));

                    }else{
                        //progressDialog.dismiss();
                        //Utility.hideLoader(Editprofile.this);
                        progressBar.setVisibility(View.GONE);
                        btn_update.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressDialog.dismiss();
                    //Utility.hideLoader(Editprofile.this);
                    progressBar.setVisibility(View.GONE);
                    btn_update.setVisibility(View.VISIBLE);
                    Log.e("Exception",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                //Utility.hideLoader(Editprofile.this);
                progressBar.setVisibility(View.GONE);
                btn_update.setVisibility(View.VISIBLE);
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
                    /*if(!progressDialog.isShowing()){
                       *//* progressDialog.setMessage("Uploading Image Please Wait.....");
                        progressDialog.setCancelable(false);
                        progressDialog.show();*//*

                    }*/
                    //Utility.displayLoader(Editprofile.this);
                    progressBar.setVisibility(View.VISIBLE);
                    btn_update.setVisibility(View.GONE);
                    uploadImage(bitmap, APIs.BASE_URL+APIs.PERSONALIMAGEUPDATE);
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
           // Utility.displayLoader(Editprofile.this);
            progressBar.setVisibility(View.VISIBLE);
            btn_update.setVisibility(View.GONE);
            callApiUpdateProfile(APIs.BASE_URL+APIs.UPDATEPROFILE);
        }
        else {
            //progressDialog.dismiss();
            //Utility.hideLoader(Editprofile.this);
            progressBar.setVisibility(View.GONE);
            btn_update.setVisibility(View.VISIBLE);
            Toast.makeText(Editprofile.this,"No Network!",Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        str_gender = parent.getItemAtPosition(position).toString();
        Log.e("Gender",str_gender);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}