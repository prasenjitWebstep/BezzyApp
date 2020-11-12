package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bezzy.Ui.View.model.Friendsfeed_item;
import com.bezzy.Ui.View.model.Notification_item;
import com.bezzy_application.R;

import java.util.ArrayList;

public class Friendsfeed extends AppCompatActivity {
    ArrayList<Friendsfeed_item> dataholder;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendsfeed);
    }
}