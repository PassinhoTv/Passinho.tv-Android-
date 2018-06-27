package com.passinhotv.android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.passinhotv.android.R;

public class ProfileDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile_details);
    }
    public void goBack(View v){
        finish();
    }
    public void saveData(View v){
        finish();
    }
    public void goDonate(View v){
        Intent intent = new Intent(ProfileDetailsActivity.this, DonateActivity.class);
        startActivity(intent);
    }
    public void changeAvatar(View v){

    }
}
