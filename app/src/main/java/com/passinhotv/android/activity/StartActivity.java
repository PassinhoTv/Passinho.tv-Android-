package com.passinhotv.android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.passinhotv.android.R;
import com.passinhotv.android.ui.auth.Getting_1_Activity;
import com.passinhotv.android.ui.auth.Welcome_1_Activity;

import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.views.BannerSlider;

public class StartActivity extends AppCompatActivity {
    Button btn_login;
    LinearLayout lyt_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start);
        initView();
    }
    public void initView(){
        BannerSlider bannerSlider = (BannerSlider) findViewById(R.id.desc_slider);
        List<Banner> banners=new ArrayList<>();
        banners.add(new DrawableBanner(R.drawable.slider_login_1));
        banners.add(new DrawableBanner(R.drawable.slider_login_2));
        banners.add(new DrawableBanner(R.drawable.slider_login_3));
        banners.add(new DrawableBanner(R.drawable.slider_login_4));
        bannerSlider.setBanners(banners);

        btn_login = findViewById(R.id.btn_login);
        lyt_signup = findViewById(R.id.btn_signup);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, Getting_1_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        lyt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, Welcome_1_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
    }
}
