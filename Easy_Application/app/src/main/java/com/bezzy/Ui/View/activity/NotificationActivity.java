package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bezzy.Ui.View.adapter.Notifi_Adapter;
import com.bezzy.Ui.View.model.Notification_item;
import com.bezzy.Ui.View.utils.APIs;
import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class NotificationActivity extends AppCompatActivity {

    ArrayList<Notification_item> dataholder;
    RecyclerView recyclerView;
    Notification_item ob;
    ImageView back_image;
    SpotsDialog dialog;
    TextView clearAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView=findViewById(R.id.noti_listf);
        back_image = findViewById(R.id.back_image);
        clearAll = findViewById(R.id.clearAll);
        /*dialog = new SpotsDialog(NotificationActivity.this);
        dialog.setMessage("Loading Please Wait...");
        dialog.setCancelable(false);*/

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this,Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        Utility.setNotificationStatus(NotificationActivity.this,"0");

        dataholder=new ArrayList<>();

        if(Utility.internet_check(NotificationActivity.this)) {

            //dialog.show();
            Utility.displayLoader(NotificationActivity.this);

            Log.e("Result","1");

            show(APIs.BASE_URL+APIs.NOTIFICATION+"/"+ Utility.getUserId(NotificationActivity.this));

        }
        else {

           // dialog.dismiss();
            Utility.hideLoader(NotificationActivity.this);

            Toast.makeText(NotificationActivity.this,"No Network!",Toast.LENGTH_SHORT).show();
        }

    }

    private void show(String url){
        dataholder.clear();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        //dialog.dismiss();
                        Utility.hideLoader(NotificationActivity.this);
                        JSONArray array = object.getJSONArray("notification_list");

                        for(int i=0;i<array.length();i++){

                            JSONObject object1 = array.getJSONObject(i);

                            String type = object1.getString("notification_type");
                            String fromId,friendRequestStatus;
                            if(type.equals("1")){
                                fromId =  object1.getString("from_id");
                                friendRequestStatus = object1.getString("friend_request_status");
                            }else{
                                fromId = "0";
                                friendRequestStatus = "0";
                            }
                            String description = object1.getString("activity_message");
                            String userImage = object1.getString("userimage");
                            String postId = object1.getString("respostID");
                            String postType = object1.getString("respost_type");

                            ob = new Notification_item(userImage,type,description,fromId,friendRequestStatus,postId,postType);
                            dataholder.add(ob);


                            /*String time = object1.getString("created_at");
                            //textView.setText(object1.getString("created_at"));
//                            Date current = Calendar.getInstance().getTime();
//                            Log.e("TIME",current.toString());

                            Calendar rightnow = Calendar.getInstance();
                            int current = rightnow.get(Calendar.HOUR_OF_DAY); //24 hrs
                            int twelve = rightnow.get(Calendar.HOUR); //12 hr

                            //if()
                            //check using if else one hour*/
                        }

                        recyclerView.setAdapter(new Notifi_Adapter(getApplicationContext(),dataholder));
                    }else{
                        //dialog.dismiss();
                        Utility.hideLoader(NotificationActivity.this);
                    }
                } catch (JSONException e) {
                    //dialog.dismiss();
                    Utility.hideLoader(NotificationActivity.this);
                    e.printStackTrace();
                    Log.e("Exception",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //dialog.dismiss();
                Utility.hideLoader(NotificationActivity.this);
                Log.e("Error",error.toString());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}