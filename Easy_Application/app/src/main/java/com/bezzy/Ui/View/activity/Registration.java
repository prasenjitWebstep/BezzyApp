package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.bezzy.Ui.View.activity.LoginActivity;
import com.bezzy.Ui.View.activity.OTPActivity;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy.Ui.View.utils.VolleyMultipartRequest;
import com.bezzy_application.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextInputEditText ed_name, ed_username, ed_email, ed_password, ed_cnfpasswd,ed_dob;
    TextView ed_gender;
    Spinner spinner;
    private String str_gender;
    Button btn_register;
    ImageView imageView,imageadd,profile_img;
    DatePickerDialog datePickerDialog;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    SpotsDialog progressDialog;
    int day,month,year;
    int MY_SOCKET_TIMEOUT_MS = 10000;
    Uri resultUri;
    Bitmap bitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);
        ed_email = findViewById(R.id.email);
        ed_password = findViewById(R.id.password);
        ed_name = findViewById(R.id.fullname);
        ed_username = findViewById(R.id.username);
        ed_cnfpasswd = findViewById(R.id.cnf_password);
        ed_dob = findViewById(R.id.dob);
        textInputEmail = findViewById(R.id.text_input_email);
        textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);
        spinner = findViewById(R.id.spinner);
        imageView=findViewById(R.id.back_image);
        imageadd=findViewById(R.id.imgAdd);
        profile_img=findViewById(R.id.square_img);

       /* progressDialog = new SpotsDialog(Registration.this);
        progressDialog.setCancelable(false);*/

        str_gender = "null";
        ed_gender = findViewById(R.id.gender);
        btn_register = findViewById(R.id.register);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setOutputCompressQuality(25)
                        .start(Registration.this);

            }
        });

    }

    public void set(){
        DatePickerDialog datePickerDialog=new DatePickerDialog(Registration.this, new DatePickerDialog.OnDateSetListener() {
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

    public void Register() {
        if(ed_email.getText().toString().isEmpty()){
            ed_email.setError("enter email");
        }else if(!Patterns.EMAIL_ADDRESS.matcher(ed_email.getText().toString()).matches()){
            ed_email.setError("enter valid email");
        }else if(ed_password.getText().toString().isEmpty()){
            ed_password.setError("enter password");
        }else if(ed_password.getText().toString().length() < 8){
            ed_password.setError("password must of at least 8 characters");
        }else if(ed_cnfpasswd.getText().toString().isEmpty()){
            ed_cnfpasswd.setError("enter confirm password");
        }else if(!ed_cnfpasswd.getText().toString().equals(ed_password.getText().toString())){
            ed_cnfpasswd.setError("password not matched");
        }else if(ed_dob.getText().toString().isEmpty()){
            ed_dob.setError("Enter Date of Birth");
        }else if(str_gender.equals("null")){
            Toast.makeText(Registration.this, "Please select your gender", Toast.LENGTH_SHORT).show();
        }else{
            if(Utility.internet_check(Registration.this)){
               // progressDialog.show();
                Utility.displayLoader(Registration.this);
                callApiRegistration(APIs.BASE_URL+APIs.REGISTRATION);
            }
            else {
                //progressDialog.dismiss();
                Utility.hideLoader(Registration.this);
                Toast.makeText(Registration.this,"No Network!",Toast.LENGTH_SHORT).show();
            }
        }
    }
   /* private void uploadImage(final Bitmap bitmap, String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("true")){
                        //progressDialog.dismiss();
                        Utility.hideLoader(Registration.this);

                        Toast.makeText(Registration.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(Registration.this,Profile.class));

                    }else{
                        //progressDialog.dismiss();
                        Utility.hideLoader(Registration.this);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressDialog.dismiss();
                    Utility.hideLoader(Registration.this);
                    Log.e("Exception",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Utility.hideLoader(Registration.this);
                Log.e("Error",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                //map.put("userID",Utility.getUserId(Registration.this));
                map.put("user_profile_iamge",getEncoded64ImageStringFromBitmap(bitmap));
                Log.e("image",map.get("profile_picture"));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Registration.this);
        queue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }*/

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK ) {
                resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(Registration.this.getContentResolver(), resultUri);
                    /*if(!progressDialog.isShowing()){
                     *//* progressDialog.setMessage("Uploading Image Please Wait.....");
                        progressDialog.setCancelable(false);
                        progressDialog.show();*//*

                    }*/
                    Utility.displayLoader(Registration.this);
                    callApiRegistration( APIs.BASE_URL+APIs.REGISTRATION);
                    profile_img.setImageBitmap(bitmap);
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


    private void callApiRegistration(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("resp").equals("true")){
                        //progressDialog.dismiss();
                        Utility.hideLoader(Registration.this);
                        Toast.makeText(Registration.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Registration.this, OTPActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Utility.setOtpScreen(Registration.this,"1");
                        Utility.setUserId(Registration.this,object.getString("log_userID"));
                    }else{
                        //progressDialog.dismiss();
                        Utility.hideLoader(Registration.this);
                        Toast.makeText(Registration.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressDialog.dismiss();
                    Utility.hideLoader(Registration.this);
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Utility.hideLoader(Registration.this);
                Log.e("Error",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("username",ed_email.getText().toString());
                map.put("fullname",ed_name.getText().toString());
                map.put("email",ed_email.getText().toString());
                map.put("password",ed_password.getText().toString());
                map.put("dob",ed_dob.getText().toString());
                map.put("gender",str_gender);

                return map;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(Registration.this);
        queue.add(request);
    }
    private  void callapi(String url){
        VolleyMultipartRequest request=new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String response2 = new String(response.data);
                Log.e("RESPONSE2", response2);

                try {
                    JSONObject object = new JSONObject(response2);
                    if(object.getString("resp").equals("true")){
                        //progressDialog.dismiss();
                        Utility.hideLoader(Registration.this);
                        Toast.makeText(Registration.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Registration.this, OTPActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Utility.setOtpScreen(Registration.this,"1");
                        Utility.setUserId(Registration.this,object.getString("log_userID"));
                    }else{
                        //progressDialog.dismiss();
                        Utility.hideLoader(Registration.this);
                        Toast.makeText(Registration.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressDialog.dismiss();
                    Utility.hideLoader(Registration.this);
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideLoader(Registration.this);
                Log.e("Error",error.toString());

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("username",ed_email.getText().toString());
                map.put("fullname",ed_name.getText().toString());
                map.put("email",ed_email.getText().toString());
                map.put("password",ed_password.getText().toString());
                map.put("dob",ed_dob.getText().toString());
                map.put("gender",str_gender);
                return map;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("user_profile_iamge", new DataPart(+imagename + ".jpeg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(Registration.this);
        rQueue.add(request);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        str_gender = parent.getItemAtPosition(position).toString();
        Log.e("Gender",str_gender);
        /*Toast.makeText(parent.getContext(),str_gender,Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture){

    }
}