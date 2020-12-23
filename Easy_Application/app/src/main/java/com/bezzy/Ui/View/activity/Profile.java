package com.bezzy.Ui.View.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Profile extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    Boolean isInBackground;
    boolean doubleBackToExitPressedOnce = false;
    boolean isClickedTwice = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_scrns);






        //SessionManager.getInstance(getApplicationContext()).userLogout();
        BottomNavigationView btmnav = findViewById(R.id.bottomnav);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        try{
            Log.e("Called","TRY");
            String from = getIntent().getExtras().getString("From");

            if(from.equals("Image")){
                Log.e("Called","TRY2");
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new ProfileFragment()).commitNow();
                btmnav.setSelectedItemId(R.id.menu_profile);
                btmnav.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
                btmnav.getMenu().getItem(4).setEnabled(true);
                btmnav.setOnNavigationItemSelectedListener(navlistner);
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.frame_layout, new ProfileFragment());
//                fragmentTransaction.commitNow();
//                btmnav.setSelectedItemId(R.id.menu_profile);
            }
        }catch (Exception e){
            Log.e("Called","EXCEPTION");
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();
            btmnav.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
            btmnav.getMenu().getItem(2).setEnabled(false);
            btmnav.setOnNavigationItemSelectedListener(navlistner);
            Log.e("Exeption",e.toString());
        }

       /* NavigationView navigationView = findViewById(R.id.bottomnav);
        //navigationView.setNavigationItemSelectedListener(this);
        navigateToFragment(new HomeFragment());*/
       /* Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,Upload.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utility.internet_check(Profile.this)) {


            callApi(APIs.BASE_URL+APIs.GET_USER_ACTIVE_STATUS,"true");
            /*messageStatUpdate(APIs.BASE_URL+APIs.GET_MESSAGE_SEEN);*/
        }
        else {

            Toast.makeText(Profile.this,"No Network!",Toast.LENGTH_SHORT).show();

        }
    }
    private void callApi(String url, final String online) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("userID",Utility.getUserId(Profile.this));
                map.put("user_active_status",online);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Profile.this);
        queue.add(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("STOP","CALLED");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistner=new
            BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.menu_home:
                    selectedFragment = new HomeFragment();
                   /* if (doubleBackToExitPressedOnce) {
                        finishAffinity();
                    }*/
                   /* this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);*/
                    if (isClickedTwice) {
                        this.finish();
                    }
                    isClickedTwice = true;
                    break;
                case R.id.menu_chat:
                    selectedFragment = new ChatFragment();
                    if (isClickedTwice) {
                        this.finish();
                    }
                    isClickedTwice = true;

                    break;
                case R.id.menu_search:
                    selectedFragment = new SearchFragment();
                    if (isClickedTwice) {
                        this.finish();
                    }
                    isClickedTwice = true;

                    break;
                case R.id.menu_profile:
                    selectedFragment = new ProfileFragment();

                    break;

            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, selectedFragment).commit();
            return true;
        }

                private void finish() {
                    Toast.makeText(Profile.this, "Press again to exit", Toast.LENGTH_SHORT).show();
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


}

