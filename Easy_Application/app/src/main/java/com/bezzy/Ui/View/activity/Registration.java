package com.bezzy.Ui.View.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.bezzy.Ui.View.model.FriendsPostModelImage;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy.Ui.View.utils.VolleyMultipartRequest;
import com.bezzy.Ui.View.utils.VolleyMultipleMultipartRequest;
import com.bezzy_application.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.potyvideo.library.AndExoPlayerView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
//import dmax.dialog.SpotsDialog;

public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static AlertDialog termsDialog;
    TextInputEditText ed_name, ed_username, ed_email, ed_password, ed_cnfpasswd,ed_dob;
    ImageView square_img;
    TextView ed_gender,textTerms;
    Spinner spinner;
    private String str_gender;
    Button btn_register;
    ImageView imageView;
    DatePickerDialog datePickerDialog;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    int MY_SOCKET_TIMEOUT_MS = 10000;
    CheckBox checkAccept;
   // SpotsDialog progressDialog;
    int day,month,year;
    Uri resultUri;
    Bitmap bitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);
        square_img = findViewById(R.id.square_img);
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
        checkAccept = findViewById(R.id.checkAccept);
        textTerms = findViewById(R.id.textTerms);


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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

        square_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setOutputCompressQuality(25)
                        .start(Registration.this);
            }
        });

        checkAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_register.getVisibility() == View.INVISIBLE){
                    btn_register.setVisibility(View.VISIBLE);
                }else if(btn_register.getVisibility() == View.VISIBLE){
                    btn_register.setVisibility(View.INVISIBLE);
                }
            }
        });

        textTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullscreenDialog(Registration.this);
            }
        });

    }

    public static void fullscreenDialog(Context context){

        TextView textTerms;

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MaterialTheme);
        View v= LayoutInflater.from(context).inflate(R.layout.terms_layout,null);
        textTerms = v.findViewById(R.id.textView);

        if(Utility.internet_check(context)) {

            //progressDialog.show();
            Utility.displayLoader(context);
            termsConditions(APIs.BASE_URL+APIs.TERMSCONDITIONS,textTerms,context);

        }
        else {

            //progressDialog.dismiss();
            Utility.hideLoader(context);

            Toast.makeText(context,"No Network!",Toast.LENGTH_SHORT).show();
        }


        builder.setView(v);
        builder.setCancelable(true);
        termsDialog=builder.create();
        termsDialog.show();
    }

    private static void termsConditions(String url, final TextView textTerms, final Context context) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.hideLoader(context);

                try {
                    JSONObject object = new JSONObject(response);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        textTerms.setText(Html.fromHtml(object.getString("page_content"), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        textTerms.setText(Html.fromHtml(object.getString("page_content")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Exception",e.toString());
                    Utility.hideLoader(context);
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideLoader(context);

            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                    //Utility.displayLoader(Registration.this);
                    //TO:DO
                    //uploadImage(bitmap, APIs.BASE_URL+APIs.PERSONALIMAGEUPDATE);
                    square_img.setImageBitmap(bitmap);
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
        if(square_img.getDrawable() == null){
            Toast.makeText(Registration.this,"Enter your profile picture",Toast.LENGTH_SHORT).show();
        }else if(ed_name.getText().toString().isEmpty()){
            ed_name.setError("Enter name");
        }else if(ed_email.getText().toString().isEmpty()){
            ed_email.setError("Enter email");
        }else if(!Patterns.EMAIL_ADDRESS.matcher(ed_email.getText().toString()).matches()){
            ed_email.setError("Enter valid email");
        }else if(ed_password.getText().toString().isEmpty()){
            ed_password.setError("Enter password");
        }else if(ed_password.getText().toString().length() < 8){
            ed_password.setError("Password must of at least 8 characters");
        }else if(ed_cnfpasswd.getText().toString().isEmpty()){
            ed_cnfpasswd.setError("Enter confirm password");
        }else if(!ed_cnfpasswd.getText().toString().equals(ed_password.getText().toString())){
            ed_cnfpasswd.setError("Password not matched");
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

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void callApiRegistration(String url) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String response2 = new String(response.data);
                        /*Log.e("RESPONSE2", response2);*/
                        try {
                            JSONObject object = new JSONObject(response2);
                            String status = object.getString("resp");
                            if(status.equals("true")){
                                Toast.makeText(Registration.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                                Utility.hideLoader(Registration.this);
                                Utility.setUserId(Registration.this,object.getString("log_userID"));
                                Intent intent = new Intent(Registration.this, OTPActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                Utility.hideLoader(Registration.this);
                                Toast.makeText(Registration.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Utility.hideLoader(Registration.this);
                            Log.e("Exception",e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
                Utility.hideLoader(Registration.this);
                /*Toast.makeText(getApplicationContext().getApplicationContext(), "Please Upload Profile Picture", Toast.LENGTH_LONG).show();*/
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                // params.put("tags", "ccccc");  add string parameters
//                params.put("userID", Utility.getUserId(getActivity()));
//                params.put("post_content", "");
                map.put("username","username1");
                map.put("fullname",ed_name.getText().toString());
                map.put("email",ed_email.getText().toString());
                map.put("password",ed_password.getText().toString());
                map.put("dob",ed_dob.getText().toString());
                map.put("gender",str_gender);

                //Log.e("GETVALUE",map.get("username")+","+map.get("fullname")+","+map.get("email")+","+map.get("password")+","+map.get("dob")+","+map.get("gender"));

                return map;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("user_profile_iamge", new DataPart(+imagename + ".jpeg", getFileDataFromDrawable(bitmap)));
                //Log.e("GETVALUE",params.get("user_profile_iamge").toString());
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue rQueue = Volley.newRequestQueue(Registration.this);
                rQueue.add(volleyMultipartRequest);

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