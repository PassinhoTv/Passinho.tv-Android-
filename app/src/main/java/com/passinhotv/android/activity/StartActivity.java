package com.passinhotv.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.passinhotv.android.R;
import com.passinhotv.android.adapter.MainSliderAdapter;
import com.passinhotv.android.adapter.PicassoImageLoadingService;
import com.passinhotv.android.ui.auth.Getting_1_Activity;
import com.passinhotv.android.ui.auth.Welcome_1_Activity;

import ss.com.bannerslider.Slider;
import ss.com.bannerslider.indicators.IndicatorShape;

public class StartActivity extends AppCompatActivity {
    Button btn_login;
    LinearLayout lyt_signup;
    Slider slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start);
        Slider.init(new PicassoImageLoadingService(this));
        initView();
    }

    public void initView() {
        slider = findViewById(R.id.desc_slider);
        slider.setAdapter(new MainSliderAdapter());

        btn_login = findViewById(R.id.btn_login);
        lyt_signup = findViewById(R.id.btn_signup);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, Getting_1_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        lyt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, Welcome_1_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
}
