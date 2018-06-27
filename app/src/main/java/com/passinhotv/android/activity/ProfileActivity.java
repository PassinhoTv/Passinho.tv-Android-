package com.passinhotv.android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.passinhotv.android.R;

public class ProfileActivity extends AppCompatActivity {
    Button btn_back, btn_chart, btn_setting, btn_user;
    RelativeLayout lyt_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);
        initUI();
    }

    public void initUI() {
        btn_back = findViewById(R.id.btn_back);
        btn_chart = findViewById(R.id.btn_chart);
        btn_user = findViewById(R.id.btn_user);
        btn_setting = findViewById(R.id.btn_setting);
        lyt_top = findViewById(R.id.lyt_top);
        lyt_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileDetailsActivity.class);
                startActivity(intent);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ProfileActivity.this.finish();
            }
        });
    }
}
