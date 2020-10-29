package com.bezzy.Ui.View.activity.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.LoginActivity;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {

    ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        // Inflate the layout for this fragment

        if(Utility.internet_check(getActivity())) {

            progressDialog.show();

            postRequest(APIs.BASE_URL+APIs.GETDATA);

        }
        else {

            progressDialog.dismiss();

            Toast.makeText(getActivity(),"No Network!",Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void postRequest(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    Log.e("REsponse",response);
                    String resp = object.getString("resp");
                    if(resp.equals("true")){

                        progressDialog.dismiss();

                        Utility.setUserName(getActivity(),object.getJSONObject("usedetails").getString("get_username"));
                        Utility.setName(getActivity(),object.getJSONObject("usedetails").getString("get_name"));
                        Utility.setEmail(getActivity(),object.getJSONObject("usedetails").getString("get_email"));
                        Utility.setdob(getActivity(),object.getJSONObject("usedetails").getString("get_dateofbirth"));
                        Utility.setGender(getActivity(),object.getJSONObject("usedetails").getString("get_gender"));
                        Utility.setBio(getActivity(),object.getJSONObject("usedetails").getString("bio"));
                        Utility.setImage(getActivity(),object.getJSONObject("usedetails").getString("profile_pic"));
                        Utility.setFollower(getActivity(),object.getJSONObject("usedetails").getString("followers"));
                        Utility.setFollowing(getActivity(),object.getJSONObject("usedetails").getString("following"));
                        Utility.setLike(getActivity(),object.getJSONObject("usedetails").getString("like"));
                    }else{
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Exception",e.toString());
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Exception",error.toString());
                progressDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("profile_id", Utility.getUserId(getActivity()));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}