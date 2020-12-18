package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.google.android.material.textfield.TextInputEditText;

import dmax.dialog.SpotsDialog;

public class Passwordchange extends AppCompatActivity {
    TextInputEditText password,cnf_password,new_password;
    Button change;
    SpotsDialog progressDialog;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordchange);

        password = findViewById(R.id.password);
        cnf_password = findViewById(R.id.cnf_password);
        change = findViewById(R.id.change);
        new_password=findViewById(R.id.new_password);
        /*progressDialog = new SpotsDialog(Changepassword.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait..");*/
        //userId = getIntent().getExtras().getString("USERID");

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(password.getText().toString().length() < 8){
                    password.setError("password must of at least 8 characters");
                }else if(cnf_password.getText().toString().isEmpty()){
                    cnf_password.setError("enter confirm password");
                }else if(!cnf_password.getText().toString().equals(password.getText().toString())) {
                    cnf_password.setError("password not matched");
                }else{
                    Log.e("Called","1");
                    if(Utility.internet_check(Passwordchange.this)) {
                        //progressDialog.show();
                        Utility.displayLoader(Passwordchange.this);
                        //callAPIChangePassword(APIs.BASE_URL+APIs.CHANGEPASSWORD);
                    }
                    else {
                        //progressDialog.dismiss();
                        Utility.hideLoader(Passwordchange.this);
                        Toast.makeText(Passwordchange.this,"No Network!",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}