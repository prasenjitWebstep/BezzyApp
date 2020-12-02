package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.material.textfield.TextInputEditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Forgot_pas extends AppCompatActivity {
    ImageView back_image;
    TextInputEditText email_send;
    Button button;
    SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_screen);
        back_image=findViewById(R.id.back_image);
        email_send=findViewById(R.id.email_send);
        button=findViewById(R.id.btn_logedin);
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forgot_pas.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Emailsend();

            }
        });
        progressDialog = new SpotsDialog(Forgot_pas.this);
        progressDialog.setCancelable(false);
        //progressDialog.setMessage("Loading Please Wait..");
    }
    public void Emailsend(){
        if(Utility.internet_check(Forgot_pas.this)) {
            progressDialog.show();
            callAPIEmailSend(APIs.BASE_URL+APIs.FORGETPASSWORDSEND);
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(Forgot_pas.this,"No Network!",Toast.LENGTH_SHORT).show();
        }


    }
    private void callAPIEmailSend(String url){
        final StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("resp").equals("true")){
                        progressDialog.dismiss();
                        Toast.makeText(Forgot_pas.this,jsonObject.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Forgot_pas.this,COTPActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("OTPVALUE",jsonObject.getString("OTPcode"));
                        intent.putExtra("USERID",jsonObject.getString("userID"));
                        Log.e("USERID",jsonObject.getString("userID"));
                        startActivity(intent);
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(Forgot_pas.this,jsonObject.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("Error",error.toString());

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("email",email_send.getText().toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Forgot_pas.this);
        queue.add(stringRequest);

    }
}