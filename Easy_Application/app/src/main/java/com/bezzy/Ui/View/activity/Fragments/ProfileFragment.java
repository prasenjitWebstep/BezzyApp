package com.bezzy.Ui.View.activity.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.bezzy.Ui.View.adapter.PostAdapter;
import com.bezzy.Ui.View.model.PostItem;
import com.bezzy.Ui.View.activity.Editprofile;
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

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    Button button;
    CircleImageView circularImg;
    TextView userName,following,follower,Likes,userBio;
    ArrayList<PostItem> postList;
    RecyclerView postRecyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        button=view.findViewById(R.id.edit_btn);
        circularImg = view.findViewById(R.id.circularImg);
        userName = view.findViewById(R.id.userName);
        following = view.findViewById(R.id.following);
        follower = view.findViewById(R.id.follower);
        Likes = view.findViewById(R.id.Likes);
        userBio = view.findViewById(R.id.userBio);
        postList = new ArrayList<>();



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
                        for(int i = 0; i< array.length(); i++){
                            JSONObject object1 = array.getJSONObject(i);
                            postList.add(new PostItem(object1.getString("post_url")));
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
}