package com.bezzy.Ui.View.activity.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.LoginActivity;
import com.bezzy.Ui.View.activity.NotificationActivity;
import com.bezzy.Ui.View.adapter.SearchAdapter;
import com.bezzy.Ui.View.adapter.Search_adapter;
import com.bezzy.Ui.View.model.Friendsnoti_item;
import com.bezzy.Ui.View.model.Searchnoti_item;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class SearchFragment extends Fragment {

    ImageButton imgSearchBtn;
    //RecyclerView recyclerView;
    ArrayList<Friendsnoti_item> dataholder;
    RecyclerView recyclerViewSearchResult;
    Friendsnoti_item ob1;
    String search;
    SpotsDialog progressDialog;
    TextInputEditText searchName;
    CardView cardSearch;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container,false);
        //recyclerView = view.findViewById(R.id.recyclerView);
        imgSearchBtn = view.findViewById(R.id.imgSearchBtn);
        cardSearch = view.findViewById(R.id.cardSearch);
        searchName = view.findViewById(R.id.searchName);
        recyclerViewSearchResult = view.findViewById(R.id.recyclerViewSearchResult);
        progressBar = view.findViewById(R.id.progressBar);
        search = "null";

        /*progressDialog = new SpotsDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait....");*/

        imgSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cardSearch.getVisibility() == View.VISIBLE){
                    cardSearch.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_up));
                    cardSearch.setVisibility(View.GONE);
                }else{
                    cardSearch.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_out_down));
                    cardSearch.setVisibility(View.VISIBLE);
                }

            }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),3);
        recyclerViewSearchResult.setLayoutManager(linearLayoutManager);

        dataholder = new ArrayList<>();

        if(Utility.internet_check(getActivity())) {

            checkToken(APIs.BASE_URL+APIs.MEMBER_TOKEN+"/"+Utility.getUserId(getActivity()));

        }
        else {
            Toast.makeText(getActivity(),"No Network!",Toast.LENGTH_SHORT).show();
        }



        searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!searchName.getText().toString().isEmpty()){
                    if(Utility.internet_check(getActivity().getApplicationContext())) {

                        search = searchName.getText().toString();
                        dataholder.clear();
                        search(APIs.BASE_URL+APIs.SEARCH);

                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(),"No Network!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    search = "null" ;
                }

            }
        });

    }

    private void checkToken(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    if(Utility.getUserToken(getActivity()).equals(object.getString("remember_token"))){

                        if(Utility.internet_check(getActivity())) {

                            // progressDialog.show();

                            progressBar.setVisibility(View.VISIBLE);

                            Log.e("Result","1");

                            registerUserResult(APIs.BASE_URL+APIs.REGISTERUSERLIST);

                        }
                        else {

                            //progressDialog.dismiss();
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(getActivity(),"No Network!",Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Utility.logoutFunction(getActivity());
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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void registerUserResult(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               // Log.e("REsponse",response);
                //progressDialog.dismiss();

                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("success")){
                        progressBar.setVisibility(View.GONE);
                        dataholder.clear();
                        JSONArray array = object.getJSONArray("all_user_list");
                        for(int i = 0 ;i<array.length();i++){
                            JSONObject object1 = array.getJSONObject(i);

                            ob1 = new Friendsnoti_item(object1.getString("name"),object1.getString("user_bio"),object1.getString("image"),object1.getString("user_id"),object1.getString("user_is_flollowers"));
                            dataholder.add(ob1);

                        }

                        recyclerViewSearchResult.setAdapter(new Search_adapter(dataholder,getActivity()));

                    }
                } catch (JSONException e) {
                    Log.e("Exception",e.toString());
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Exception",error.toString());
                //progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("log_userID", Utility.getUserId(getActivity()));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void search(String url){
        dataholder.clear();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   Log.e("Response",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("success")){
                        progressBar.setVisibility(View.GONE);
                        dataholder.clear();
                        JSONArray array = object.getJSONArray("search_result");
                        for(int i = 0;i<array.length();i++){

                            JSONObject object1 = array.getJSONObject(i);

                            ob1 = new Friendsnoti_item(object1.getString("name"),object1.getString("user_bio"),object1.getString("image"),object1.getString("user_id"),object1.getString("user_is_flollowers"));
                            dataholder.add(ob1);
                        }
                        recyclerViewSearchResult.setAdapter(new Search_adapter(dataholder,getActivity()));

                    }else{
                        Toast.makeText(getActivity(),object.getString("title"),Toast.LENGTH_SHORT).show();
                        if(Utility.internet_check(getActivity())) {

                            //progressDialog.show();
                            progressBar.setVisibility(View.VISIBLE);

                            Log.e("Result","1");

                            registerUserResult(APIs.BASE_URL+APIs.REGISTERUSERLIST);

                        }
                        else {

                            //progressDialog.dismiss();
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(getActivity(),"No Network!",Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    Log.e("Exception",e.toString());
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
                map.put("searchval",search);
                map.put("loguser_id",Utility.getUserId(getActivity()));
                Log.e("Value",map.get("searchval"));
                //searchView.getQuery().toString()
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(request);
    }
}