package com.bezzy.Ui.View.activity.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bezzy.Ui.View.adapter.SearchAdapter;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy_application.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SearchFragment extends Fragment {

    SearchView searchView;
    Button button;
    //RecyclerView recyclerView;
    ArrayList<String> nameList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        //recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        button = view.findViewById(R.id.www);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(APIs.BASE_URL+APIs.SEARCH);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(APIs.BASE_URL+APIs.SEARCH);
                return false;
            }
        });
        return view;
    }

    private void search(String url){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String data = object.getString("name");
                    nameList.add(data);
                    Log.e("ASDF",nameList.toString());
                    //recyclerView.setAdapter(new SearchAdapter(getActivity(),nameList));
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
                map.put("searchval","suj");
                //searchView.getQuery().toString()
                return map;
            }
        };
    }
}