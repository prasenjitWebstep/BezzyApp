package com.bezzy.Ui.View.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.bezzy.Ui.View.activity.Fragments.ChatFragment;
import com.bezzy.Ui.View.activity.Fragments.HomeFragment;
import com.bezzy.Ui.View.activity.Fragments.ProfileFragment;
import com.bezzy.Ui.View.activity.Fragments.SearchFragment;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    String url = "http://bezzy.websteptech.co.uk/api/logout";
    ProgressDialog progressDialog;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_scrns);


        //SessionManager.getInstance(getApplicationContext()).userLogout();
        BottomNavigationView btmnav = findViewById(R.id.bottomnav);
        btmnav.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        btmnav.getMenu().getItem(2).setEnabled(false);
        btmnav.setOnNavigationItemSelectedListener(navlistner);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();
        floatingActionButton = findViewById(R.id.floatingActionButton);

       /* NavigationView navigationView = findViewById(R.id.bottomnav);
        //navigationView.setNavigationItemSelectedListener(this);
        navigateToFragment(new HomeFragment());*/
       /* Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setMessage("Logging Out Please Wait...");
        progressDialog.setCancelable(false);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,Upload.class));
            }
        });

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navlistner=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.menu_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.menu_chat:
                    selectedFragment = new ChatFragment();
                    break;
                case R.id.menu_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.menu_profile:
                    selectedFragment = new ProfileFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, selectedFragment).commit();
            return true;
        }
    };




   /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        switch (item.getItemId()) {
            case R.id.menu_home:
                if (!currentFragment.getClass().isInstance(new HomeFragment())) {
                    //toolbar.setTitle("Home");
                    navigateToFragment(new HomeFragment());
                }
                break;
            case R.id.chat:
                if (!currentFragment.getClass().isInstance(new ChatFragment())) {
                    //toolbar.setTitle("My Account");
                    navigateToFragment(new ChatFragment());
                }
                break;
            case R.id.search:
                if (!currentFragment.getClass().isInstance(new SearchFragment())) {
                    // toolbar.setTitle("Dashboard");
                    navigateToFragment(new SearchFragment());
                }
                break;
            case R.id.profile:
                if (!currentFragment.getClass().isInstance(new ProfileFragment())) {
                    //toolbar.setTitle("Subcription");
                    navigateToFragment(new ProfileFragment());
                }
                break;


        }
        return true;
    }*/

   /* public void navigateToFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        //fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        fragmentTransaction.commit();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.top_menu_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutt:
                logout();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Log out");
        builder.setMessage("Are you sure to Log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String resp = object.getString("resp");
                            if(resp.equals("success")){
                                progressDialog.dismiss();
                                Toast.makeText(Profile.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Profile.this,LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                Utility.setLogin(Profile.this,"0");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> map = new HashMap<>();
                        map.put("profile_id", Utility.getUserId(Profile.this));
                        return map;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(Profile.this);
                queue.add(stringRequest);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}

