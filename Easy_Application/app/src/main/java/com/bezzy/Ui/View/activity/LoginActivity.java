package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private View decorView;
    TextView tvRegister;
    Button btnLogin;
    TextView tv_forpas;
    SpotsDialog progressDialog;
    ImageView google_btn;

    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        /*decorView = getWindow().peekDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());

            }
        });*/
        //signInButton = findViewById(R.id.sign_in_button);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        tv_forpas = findViewById(R.id.tv_forpas);
        google_btn=findViewById(R.id.google_btn);
        openForpas();
        openregister();
        progressDialog = new SpotsDialog(LoginActivity.this,R.style.Custom);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please wait...");
       /* tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Registration.class);
                startActivity(intent);
            }
        });*/

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();*/
                if(Utility.internet_check(LoginActivity.this)) {

                    progressDialog.show();
                    postRequest(APIs.BASE_URL+APIs.LOGIN_URL);

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
                }


            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void postRequest(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("true")){
                        progressDialog.dismiss();
                        Utility.setLogin(LoginActivity.this,"1");
                        Utility.setUserId(LoginActivity.this,object.getString("id"));
                        Toast.makeText(LoginActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, Profile.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();
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
                map.put("username",etUsername.getText().toString());
                map.put("password",etPassword.getText().toString());
                map.put("device_token", FirebaseInstanceId.getInstance().getToken());
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(request);
    }

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }*/

    public void openForpas() {
        tv_forpas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Forgot_pas.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void openregister() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Registration.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
            if (acct != null) {

                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();

                callApiSocialLogin(APIs.BASE_URL+APIs.SOCIALLOGINURL+"/"+personEmail+"/"+personName);

            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Google Sign In Error", e.toString());
            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    private void callApiSocialLogin(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("true")){

                        Utility.setLogin(LoginActivity.this,"1");
                        Utility.setUserId(LoginActivity.this,object.getString("id"));
                        Toast.makeText(LoginActivity.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,Profile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(request);

    }

    /*@Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            startActivity(new Intent(LoginActivity.this, showprofile.class));
        }
        super.onStart();
    }*/


}