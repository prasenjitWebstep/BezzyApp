package com.bezzy.Ui.View.activity.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.bezzy.Ui.View.activity.LoginActivity;
import com.bezzy.Ui.View.activity.Profile;
import com.bezzy.Ui.View.adapter.PostAdapter;
import com.bezzy.Ui.View.model.PostItem;
import com.bezzy.Ui.View.activity.Editprofile;
import com.bezzy.Ui.View.model.PostModel;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    Button button;
    CircleImageView circularImg;
    TextView userName,following,follower,Likes,userBio;
    ArrayList<PostModel> postList;
    ArrayList<String> imgList;
    RecyclerView postRecyclerView;
    ProgressDialog progressDialog;
    String url = "http://bezzy.websteptech.co.uk/api/logout";
    ImageView imageView;
    ImageButton imageButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        button=view.findViewById(R.id.edit_btn);
        circularImg = view.findViewById(R.id.circularImg);
        userName = view.findViewById(R.id.userName);
        following = view.findViewById(R.id.following_num);
        follower = view.findViewById(R.id.follower_num);
        Likes = view.findViewById(R.id.like_num);
        userBio = view.findViewById(R.id.userBio);
        imageView=view.findViewById(R.id.logout);
        imageButton=view.findViewById(R.id.imageButton);
        postList = new ArrayList<>();
        imgList = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Logging Out Please Wait....");
        progressDialog.setCancelable(false);



        Glide.with(ProfileFragment.this).load(Utility.getImage(getActivity())).into(circularImg);

        userName.setText(Utility.getUserName(getActivity()));

        following.setText(Utility.getFollowing(getActivity()));
        follower.setText(Utility.getFollowers(getActivity()));
        Likes.setText(Utility.getLike(getActivity()));
        if(!Utility.getBio(getActivity()).equals("null")){
            userBio.setVisibility(View.VISIBLE);
            userBio.setText(Utility.getBio(getActivity()));
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Editprofile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), VideoDisplayActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        postRecyclerView = view.findViewById(R.id.postRecyclerView);

        Log.e("Called","GridCalled");
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL);
        postRecyclerView.setLayoutManager(layoutManager);

        if(Utility.internet_check(getActivity())) {

            postRequest(APIs.BASE_URL+APIs.GETDATA);
        }
        else {

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
                    Log.e("Response",response);
                    String resp = object.getString("resp");
                    if(resp.equals("true")){


                        JSONArray array = object.getJSONArray("user_all_posts");
                        JSONArray array1 = array.getJSONArray(array.length()-1);
                        Log.e("Array",array1.toString());
                        for(int i=0;i<array1.length();i++){
                            JSONObject object1 = array1.getJSONObject(i);
                            postList.add(new PostModel(object1.getString("post_id"),object1.getString("post_url"),object1.getString("post_type"),object1.getString("id")));
                        }

                        Log.e("Called","Adapter Called");
                        postRecyclerView.setAdapter((new PostAdapter(postList,getActivity())));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Exception",error.toString());

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
    public void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                                Toast.makeText(getActivity(),object.getString("message"),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(),LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                Utility.setLogin(getActivity(),"0");
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
                        map.put("profile_id", Utility.getUserId(getActivity()));
                        return map;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(getActivity());
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