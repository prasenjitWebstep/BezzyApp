package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.mukesh.OtpView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class OTPActivity extends AppCompatActivity {

    Button btnVerify;
    OtpView otp_view;
    SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p2);

        initViews();
    }

    private void initViews() {

        btnVerify = findViewById(R.id.btnVerify);
        otp_view = findViewById(R.id.otp_view);
        progressDialog = new SpotsDialog(OTPActivity.this);
        //progressDialog.setMessage("Verifying Please Wait...");
        progressDialog.setCancelable(false);


        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utility.internet_check(OTPActivity.this)) {

                    progressDialog.show();
                    callApiVerifyOtp(APIs.BASE_URL+APIs.OTPVERIFICATION);

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(OTPActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void callApiVerifyOtp(String url) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response);

                try {
                    JSONObject object = new JSONObject(response);

                    String status = object.getString("resp");

                    if(status.equals("true")){

                        progressDialog.dismiss();

                        String message = object.getString("message");

                        Toast.makeText(OTPActivity.this,message,Toast.LENGTH_SHORT).show();

                        Utility.setOtpScreen(OTPActivity.this,"0");
                        Intent intent = new Intent(OTPActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }else{
                        String message = object.getString("message");
                        progressDialog.dismiss();
                        Toast.makeText(OTPActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Log.e("Exception",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("Error",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("otp_code",otp_view.getText().toString());
                map.put("userID",Utility.getUserId(OTPActivity.this));
                map.put("device_token", FirebaseInstanceId.getInstance().getToken());
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(OTPActivity.this);
        queue.add(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
}