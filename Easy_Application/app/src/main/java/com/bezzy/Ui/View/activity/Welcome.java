package com.bezzy.Ui.View.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bezzy.Ui.View.utils.Utility;
import com.bezzy_application.R;

public class Welcome extends AppCompatActivity {
    Button btn_welcome;
    TextView welcome_user;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_scrns);
        btn_welcome=findViewById(R.id.welcome_btn);
        welcome_user=findViewById(R.id.welcome_user);
        name=getIntent().getStringExtra("name");
        welcome_user.setText(name);
        btn_welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.setLogin(Welcome.this,"1");
                startActivity(new Intent(getApplicationContext(),Profile.class));
                finish();


            }
        });
    }
}