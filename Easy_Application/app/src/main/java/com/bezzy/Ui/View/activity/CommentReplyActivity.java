package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
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
import com.bezzy.Ui.View.adapter.Comment_adapter;
import com.bezzy.Ui.View.adapter.ReplyAdapter;
import com.bezzy.Ui.View.model.Comment_item;
import com.bezzy.Ui.View.model.TagModel;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;
import com.bumptech.glide.Glide;
import com.hendraanggrian.appcompat.socialview.Mention;
import com.hendraanggrian.appcompat.widget.MentionArrayAdapter;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class CommentReplyActivity extends AppCompatActivity {

    CircleImageView img_logo;
    TextView title_text,comment_user,timeshow;
    ArrayList<Comment_item> dataholder;
    ArrayList<String> dataholder2;
    RecyclerView recyclerView;
    //EditText comment;
    ImageView imageView;
    RelativeLayout layout_chatbox;
    EmojIconActions emojIcon;
    View rootView;
    EmojiconEditText comment;
    SocialAutoCompleteTextView autoCompleteTextView;
    String mention;
    ImageView emojiButton;
    String id,postId;
    private ArrayAdapter<Mention> defaultMentionAdapter;
    ArrayList<String> idLst;
    ArrayList<TagModel> taglist;
    String MENTION1_USERNAME;
    ArrayList<String> tagId;
    String arrayToString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_reply);

        checkToken();

        img_logo = findViewById(R.id.img_logo);
        title_text = findViewById(R.id.title_text);
        comment_user = findViewById(R.id.comment_user);
        timeshow = findViewById(R.id.timeshow);
        recyclerView = findViewById(R.id.reyclerview_comment_list);
        imageView = findViewById(R.id.send_msg);

        Glide.with(CommentReplyActivity.this)
                .load(getIntent().getExtras().getString("image"))
                .into(img_logo);

        title_text.setText(getIntent().getExtras().getString("name"));

        comment_user.setText(getIntent().getExtras().getString("comment"));

        timeshow.setText(getIntent().getExtras().getString("time"));

        id = getIntent().getExtras().getString("id");

        postId = getIntent().getExtras().getString("postId");

        rootView = findViewById(R.id.root_view);
        autoCompleteTextView=findViewById(R.id.edittext_comment);
        /*emojiButton = (ImageView) findViewById(R.id.emoji_btn);
        comment=findViewById(R.id.edittext_comment);
        emojIcon = new EmojIconActions(this, rootView, comment, emojiButton);
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }
            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });
        emojIcon.addEmojiconEditTextList(comment);*/


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        dataholder = new ArrayList<>();
        dataholder2 = new ArrayList<>();

        tagId = new ArrayList<>();

        arrayToString = "";

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(TagModel model : taglist){
                    Log.e("TAGLISTONBUTTON CLICK",model.getName()+"/"+model.getId());
                    if(autoCompleteTextView.getText().toString().contains(model.getName())){
                        //Toast.makeText(getContext(),model.getId(),Toast.LENGTH_LONG).show();
                        tagId.add(model.getId());
                    }
                    arrayToString = tagId.toString().substring(1, tagId.toString().length() - 1);
                    Log.e("HI BABBY",tagId.toString().substring(1, tagId.toString().length() - 1));

                }

                Log.e("ARRAY",arrayToString);

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                if(Utility.internet_check(CommentReplyActivity.this)) {

                    //dialog.show();

                    commentPost(APIs.BASE_URL+APIs.COMMENT_POST);

                }
                else {

                    //dialog.dismiss();

                    Toast.makeText(CommentReplyActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void checkToken() {
        StringRequest request = new StringRequest(Request.Method.GET, APIs.BASE_URL+APIs.MEMBER_TOKEN+"/"+Utility.getUserId(CommentReplyActivity.this), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    if(Utility.getUserToken(CommentReplyActivity.this).equals(object.getString("remember_token"))){


                    }else{
                        Utility.logoutFunction(CommentReplyActivity.this);
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

        RequestQueue queue = Volley.newRequestQueue(CommentReplyActivity.this);
        queue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();

        taglist = new ArrayList<>();

        followingUserList(APIs.BASE_URL+APIs.FOLLOWINGLIST);

        if(Utility.internet_check(CommentReplyActivity.this)) {

            //dialog.show();

            commentList(APIs.BASE_URL+APIs.COMMENT_REPLY_LIST);

        }
        else {

            //dialog.dismiss();

            Toast.makeText(CommentReplyActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
        }

    }

    private void commentList(String url) {

        dataholder.clear();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Log.e("response",response);

                try {
                    JSONObject object = new JSONObject(response);
                    String status=object.getString("status");
                    if(status.equals("success")){
                        JSONObject object1=object.getJSONObject("comment_list");
                        JSONArray array = object1.getJSONArray("Parent");
                        for(int i=0;i<array.length();i++){

                            JSONObject object11 = array.getJSONObject(i);

                            Comment_item modelObj = new Comment_item(object11.getString("comment_id"),
                                    object11.getString("username"),object11.getString("userimage"),
                                    object11.getString("postcomment_time"),object11.getString("commentText"),
                                    object11.getString("total_like_on_comment"),object11.getString("total_comment_on_comment"),
                                    object11.getString("login_user_like_comment"),"");

                            dataholder.add(modelObj);

                        }

                        recyclerView.setAdapter(new ReplyAdapter(CommentReplyActivity.this,dataholder));


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Exception",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("comment_id",id);
                Log.e("Value",map.get("comment_id"));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(CommentReplyActivity.this);
        queue.add(request);
    }
    private void commentPost(String url) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              //  Log.e("Response",response);

                try {
                    JSONObject object = new JSONObject(response);
                    String status=object.getString("status");
                    if(status.equals("success")){
                        autoCompleteTextView.getText().clear();
                        if(Utility.internet_check(CommentReplyActivity.this)) {

                            //dialog.show();

                            Log.e("Result","1");

                            commentList(APIs.BASE_URL+APIs.COMMENT_REPLY_LIST);

                        }
                        else {

                            //dialog.dismiss();

                            Toast.makeText(CommentReplyActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
                        }
                        JSONObject object1=object.getJSONObject("comment_user");
                        String name=object1.getString("name");
                        String photo=object1.getString("profilePicture");
                        String comment=object.getString("commentText");



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

                map.put("userID",Utility.getUserId(CommentReplyActivity.this));
                map.put("PostId",postId);
                map.put("commentParentId",id);
                map.put("tag_user_id",arrayToString);
                map.put("commentText",comment.getText().toString());

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(CommentReplyActivity.this);
        queue.add(request);
    }

    private void followingUserList(String url) {
        taglist.clear();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Log.e("REsponse",response);
                //progressDialog.dismiss();


                try {
                    JSONObject object = new JSONObject(response);
                    String resp = object.getString("resp");
                    if(resp.equals("success")){
//                        progressBar.setVisibility(View.GONE);
                        //dataholder.clear();
                        JSONArray array = object.getJSONArray("following_user_list");
                        defaultMentionAdapter = new MentionArrayAdapter<>(CommentReplyActivity.this);
                        for(int i = 0 ;i<array.length();i++){
                            JSONObject object1 = array.getJSONObject(i);
                            // Log.e("HIHIHIHI",object1.getString("name"));
                            //ob1 = new Friendsnoti_item(object1.getString("name"),object1.getString("user_bio"),object1.getString("image"),object1.getString("user_id"),object1.getString("user_is_flollowers"));
                            //dataholder.add(ob1);
//                            strings = new ArrayList<String>();
//                            strings.add(object1.getString("name"));

                            taglist.add(new TagModel(object1.getString("name"),object1.getString("following_user_id")));
                            MENTION1_USERNAME = object1.getString("name");
                            defaultMentionAdapter.add(
                                    new Mention(MENTION1_USERNAME)
                            );
                        }

                        autoCompleteTextView.setMentionAdapter(defaultMentionAdapter);
                        for(TagModel model : taglist){
                            Log.e("TAGLIST",model.getName()+"/"+model.getId());
                        }
                        int current = autoCompleteTextView.getSelectionStart();
                        Log.e("WE ARE NOTHING",String.valueOf(current));

                    }
                } catch (JSONException e) {
                    Log.e("Exception",e.toString());
                    e.printStackTrace();
                    //progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Exception",error.toString());
                //progressDialog.dismiss();
                //progressBar.setVisibility(View.GONE);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("loguser_id", Utility.getUserId(CommentReplyActivity.this));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(CommentReplyActivity.this);
        queue.add(request);
    }

}