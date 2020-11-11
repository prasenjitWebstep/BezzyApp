package com.bezzy.Ui.View.activity.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SearchFragment extends Fragment {

    SearchView searchView;
    Button button;
    ImageView imgSearch;
    AutoCompleteTextView autocompleteHeader;
    //RecyclerView recyclerView;
    ArrayList<Friendsnoti_item> dataholder;
    RecyclerView recyclerViewSearchResult;
    Friendsnoti_item ob1;
    String search;
    ProgressDialog progressDialog;
    FrameLayout noti;
    TextView cart_badge;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        //recyclerView = view.findViewById(R.id.recyclerView);
        autocompleteHeader = view.findViewById(R.id.autocompleteHeader);
        imgSearch = view.findViewById(R.id.imgSearch);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Searching People Please wait...");

        noti = view.findViewById(R.id.noti);
        cart_badge = view.findViewById(R.id.cart_badge);

        if(Utility.getNotificationStatus(getActivity().getApplicationContext()).equals("1")){
            cart_badge.setVisibility(View.VISIBLE);
        }else{
            cart_badge.setVisibility(View.GONE);
        }

        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
            }
        });

        recyclerViewSearchResult = view.findViewById(R.id.recyclerViewSearchResult);
        dataholder = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewSearchResult.setLayoutManager(linearLayoutManager);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.internet_check(getActivity().getApplicationContext())) {

                    progressDialog.show();
                    search = autocompleteHeader.getText().toString();
                    search(APIs.BASE_URL+APIs.SEARCH);
                    dataholder.clear();

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity().getApplicationContext(),"No Network!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private void search(String url){
        dataholder.clear();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("success")){
                        progressDialog.dismiss();
                        autocompleteHeader.getText().clear();
                        JSONArray array = object.getJSONArray("search_result");
                        for(int i = 0;i<array.length();i++){

                            JSONObject object1 = array.getJSONObject(i);

                            ob1 = new Friendsnoti_item(object1.getString("name"),object1.getString("user_bio"),object1.getString("image"),object1.getString("user_id"));
                            dataholder.add(ob1);
                        }
                        recyclerViewSearchResult.setAdapter(new Search_adapter(dataholder,getActivity()));

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),object.getString("title"),Toast.LENGTH_SHORT).show();
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
                map.put("searchval",search);
                Log.e("Value",map.get("searchval"));
                //searchView.getQuery().toString()
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(request);
    }
}