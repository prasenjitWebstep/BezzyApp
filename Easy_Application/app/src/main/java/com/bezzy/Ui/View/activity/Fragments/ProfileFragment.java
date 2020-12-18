package com.bezzy.Ui.View.activity.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.activity.FollowingActivity;
import com.bezzy.Ui.View.activity.LoginActivity;
import com.bezzy.Ui.View.activity.MyFriendsList;
import com.bezzy.Ui.View.activity.Profile;
import com.bezzy.Ui.View.adapter.PostAdapter;
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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;


public class ProfileFragment extends Fragment {
    Button button;
    ImageView square_img;
    TextView userName,following,follower,Likes,userBio,edit_btn,block_btn;
    ArrayList<PostModel> postList;
    ArrayList<String> imgList;
    RecyclerView postRecyclerView;
    SpotsDialog progressDialog;
    ImageView imageView;
    RelativeLayout layoutFollowing,layoutFollower;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_new_profile, container, false);
        square_img = view.findViewById(R.id.square_img);
        userName = view.findViewById(R.id.userName);
        following = view.findViewById(R.id.following_num);
        follower = view.findViewById(R.id.follower_num);
        Likes = view.findViewById(R.id.like_num);
        userBio = view.findViewById(R.id.userBio);
        imageView=view.findViewById(R.id.logout);
        edit_btn = view.findViewById(R.id.edit_btn);
        block_btn = view.findViewById(R.id.block_btn);
        postRecyclerView = view.findViewById(R.id.postRecyclerView);
        layoutFollowing = view.findViewById(R.id.layoutFollowing);
        layoutFollower = view.findViewById(R.id.layoutFollower);

        postList = new ArrayList<>();
        imgList = new ArrayList<>();
       /* progressDialog = new SpotsDialog(getActivity());
        progressDialog.setMessage("Logging Out Please Wait....");
        progressDialog.setCancelable(false);*/
        block_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"FUCK",Toast.LENGTH_LONG).show();
                block(APIs.BASE_URL+APIs.BLOCK);
            }
        });




        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


        return view;

    }

    private void block( String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("status");
                    if (resp.equals("success")) {
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
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
            protected Map<String,String> getParams() throws AuthFailureError{
                HashMap<String,String> map = new HashMap<>();
                map.put("loginUserID",Utility.getUserId(getActivity()));
                map.put("blockuserID","107");
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("Called","GridCalled");
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        postRecyclerView.setLayoutManager(layoutManager);

        if(Utility.internet_check(getActivity())) {

            Utility.displayLoader(getActivity());

            postRequest(APIs.BASE_URL+APIs.GETDATA);
        }
        else {

            Toast.makeText(getActivity(),"No Network!",Toast.LENGTH_SHORT).show();
        }

    }



    private void postRequest(String url) {
        postList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    final JSONObject object = new JSONObject(response);
                    Log.e("Response",response);
                    String resp = object.getString("resp");
                    if(resp.equals("true")){

                        Glide.with(ProfileFragment.this).load(object.getJSONObject("usedetails").getString("profile_pic")).into(square_img);

                        userName.setText(object.getJSONObject("usedetails").getString("get_name"));
                        following.setText(object.getJSONObject("usedetails").getString("following"));
                        follower.setText(object.getJSONObject("usedetails").getString("followers"));
                        Likes.setText(object.getJSONObject("usedetails").getString("number_of_post"));

                        String bio = object.getJSONObject("usedetails").getString("bio");
                        if(!bio.equals("null")){
                            userBio.setVisibility(View.VISIBLE);
                            userBio.setText(bio);
                        }

                        layoutFollowing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if(!object.getJSONObject("usedetails").getString("following").equals("0")){
                                        Intent intent = new Intent(getActivity(), FollowingActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        layoutFollower.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if(!object.getJSONObject("usedetails").getString("followers").equals("0")){
                                        Intent intent = new Intent(getActivity(), MyFriendsList.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Utility.hideLoader(getActivity());

                        edit_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getActivity(), Editprofile.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                try {
                                    intent.putExtra("username",object.getJSONObject("usedetails").getString("get_username"));
                                    intent.putExtra("name",object.getJSONObject("usedetails").getString("get_name"));
                                    intent.putExtra("image",object.getJSONObject("usedetails").getString("profile_pic"));
                                    intent.putExtra("email",object.getJSONObject("usedetails").getString("get_email"));
                                    intent.putExtra("gender",object.getJSONObject("usedetails").getString("get_gender"));
                                    intent.putExtra("dob",object.getJSONObject("usedetails").getString("get_dateofbirth"));
                                    intent.putExtra("bio",object.getJSONObject("usedetails").getString("bio"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                            }
                        });

                        JSONArray array = object.getJSONArray("user_all_posts");
                        JSONArray array1 = array.getJSONArray(array.length()-1);
                        Log.e("Array",array1.toString());
                        for(int i=0;i<array1.length();i++){
                            JSONObject object1 = array1.getJSONObject(i);
                            postList.add(new PostModel(object1.getString("post_id"),object1.getString("post_url"),object1.getString("post_type"),object1.getString("id")));
                        }

                        Log.e("Called","Adapter Called");
                        postRecyclerView.setAdapter((new PostAdapter(postList,getActivity())));

                    }else{
                        Utility.hideLoader(getActivity());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.hideLoader(getActivity());
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideLoader(getActivity());
                Log.e("VolleyError",error.toString());

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
                //progressDialog.show();
                Utility.displayLoader(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.BASE_URL+APIs.LOGOUT, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String resp = object.getString("resp");
                            if(resp.equals("success")){

                                //progressDialog.dismiss();
                                Utility.hideLoader(getActivity());
                                Toast.makeText(getActivity(),object.getString("message"),Toast.LENGTH_SHORT).show();
                                callApi(APIs.BASE_URL+APIs.GET_USER_ACTIVE_STATUS,"false");
                                Intent i = new Intent(getActivity(), LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                getActivity().startActivity(i);
                                Utility.setLogin(getActivity(),"0");
                                if(Utility.getSocial(getActivity()).equals("1")){
                                    Utility.setSocial(getActivity(),"0");
                                }
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
                        map.put("profile_id",Utility.getUserId(getActivity()));
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
                map.put("userID",Utility.getUserId(getActivity().getApplicationContext()));
                map.put("user_active_status",online);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(request);
    }
}