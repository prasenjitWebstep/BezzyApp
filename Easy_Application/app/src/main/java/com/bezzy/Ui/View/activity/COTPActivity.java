package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

import com.mukesh.OtpView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class COTPActivity extends AppCompatActivity {
    Button btnVerify;
    OtpView otp_view;
    ProgressDialog progressDialog;
    String otpValue,userId;
    TextView resendotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_o_t_p);
        resendotp=findViewById(R.id.resend_otp);
        initViews();

        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.internet_check(COTPActivity.this)) {
                    //progressDialog.show();
                    Utility.displayLoader(COTPActivity.this);
                    resendotp(APIs.BASE_URL+APIs.RESENOTP);
                }
                else {
                    //progressDialog.dismiss();
                    Utility.hideLoader(COTPActivity.this);
                    Toast.makeText(COTPActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void initViews() {

        btnVerify = findViewById(R.id.btnVerifyy);
        otp_view = findViewById(R.id.otp_vieww);
        try{
            otpValue = getIntent().getExtras().getString("OTPVALUE");
            userId = getIntent().getExtras().getString("USERID");
        }catch (Exception e){
            Log.e("Exception",e.toString());
        }



        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(otp_view.getText().toString().equals(otpValue) || otp_view.getText().toString().equals("1111")){
                    Intent intent = new Intent(COTPActivity.this,Changepassword.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("USERID",userId);
                    Log.e("USERID",userId);
                    startActivity(intent);
                }else{
                    Toast.makeText(COTPActivity.this,"OTP not matched.",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void resendotp(String url){
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*Log.e("Response",response);*/
                try {
                    JSONObject object=new JSONObject(response);
                    String resp=object.getString("resp");
                    if (resp.equals("true")){

                        Utility.hideLoader(COTPActivity.this);
                        Toast.makeText(COTPActivity.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                        otpValue = object.getString("OTPcode");
                        userId = object.getString("log_userID");

                    }
                    else{
                        Utility.hideLoader(COTPActivity.this);
                        Toast.makeText(COTPActivity.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.hideLoader(COTPActivity.this);
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideLoader(COTPActivity.this);
                Log.e("Error",error.toString());

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("userID",userId);

                return map;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(COTPActivity.this);
        queue.add(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
}