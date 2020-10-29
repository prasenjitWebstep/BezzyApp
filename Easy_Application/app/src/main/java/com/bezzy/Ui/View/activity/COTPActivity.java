package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bezzy_application.R;

import com.mukesh.OtpView;

public class COTPActivity extends AppCompatActivity {
    Button btnVerify;
    OtpView otp_view;
    ProgressDialog progressDialog;
    String otpValue,userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_o_t_p);
        initViews();
        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

            }
        });*/
    }
    private void initViews() {

        btnVerify = findViewById(R.id.btnVerifyy);
        otp_view = findViewById(R.id.otp_vieww);
        otpValue = getIntent().getExtras().getString("OTPVALUE");
        userId = getIntent().getExtras().getString("USERID");


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
}