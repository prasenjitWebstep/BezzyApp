package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

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

                if(password.getText().toString().isEmpty()){
                    password.setError("enter your current password");
                }else if(new_password.getText().toString().isEmpty()){
                    new_password.setError("enter your new password");
                }else if(new_password.getText().toString().length()<8){
                    new_password.setError("please enter at least 8 characters");
                }else if(cnf_password.getText().toString().isEmpty()){
                    cnf_password.setError("enter confirm password");
                }else if(!cnf_password.getText().toString().equals(new_password.getText().toString())) {
                    cnf_password.setError("password not matched");
                }else{
                    Log.e("Called","1");
                    if(Utility.internet_check(Passwordchange.this)) {
                        //progressDialog.show();
                        Utility.displayLoader(Passwordchange.this);
                        callAPIChangePassword(APIs.BASE_URL+APIs.CHANGELOGINPASSWORD);
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
                        Utility.hideLoader(Passwordchange.this);
                        Toast.makeText(Passwordchange.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Passwordchange.this,Profile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("From","Image");
                        startActivity(intent);
                    }else{
                        //progressDialog.dismiss();
                        Utility.hideLoader(Passwordchange.this);
                        Toast.makeText(Passwordchange.this,object.getString("reg_msg"),Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressDialog.dismiss();
                    Utility.hideLoader(Passwordchange.this);
                    Log.e("Exception",e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Utility.hideLoader(Passwordchange.this);
                Log.e("Error",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("userID",Utility.getUserId(Passwordchange.this));
                map.put("current_password",password.getText().toString());
                map.put("password",cnf_password.getText().toString());
                Log.e("MAPGET",map.get("password")+" "+map.get("userID"));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Passwordchange.this);
        queue.add(request);

    }
}