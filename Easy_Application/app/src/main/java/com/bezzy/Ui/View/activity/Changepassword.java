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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Changepassword extends AppCompatActivity {

    TextInputEditText password,cnf_password;
    Button change;
    SpotsDialog progressDialog;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        password = findViewById(R.id.password);
        cnf_password = findViewById(R.id.cnf_password);
        change = findViewById(R.id.change);
        /*progressDialog = new SpotsDialog(Changepassword.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait..");*/
        userId = getIntent().getExtras().getString("USERID");

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
                    if(Utility.internet_check(Changepassword.this)) {
                        //progressDialog.show();
                        Utility.displayLoader(Changepassword.this);
                        callAPIChangePassword(APIs.BASE_URL+APIs.CHANGEPASSWORD);
                    }
                    else {
                        //progressDialog.dismiss();
                        Utility.hideLoader(Changepassword.this);
                        Toast.makeText(Changepassword.this,"No Network!",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    private void callAPIChangePassword(String url){
        Log.e("URL",url);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("true")){
                        //progressDialog.dismiss();
                        Utility.hideLoader(Changepassword.this);
                        Toast.makeText(Changepassword.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Changepassword.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else{
                        //progressDialog.dismiss();
                        Utility.hideLoader(Changepassword.this);
                        Toast.makeText(Changepassword.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressDialog.dismiss();
                    Utility.hideLoader(Changepassword.this);
                    Log.e("Exception",e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Utility.hideLoader(Changepassword.this);
                Log.e("Error",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("userID",userId);
                map.put("password",cnf_password.getText().toString());
                Log.e("MAPGET",map.get("password")+" "+map.get("userID"));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Changepassword.this);
        queue.add(request);

    }
}