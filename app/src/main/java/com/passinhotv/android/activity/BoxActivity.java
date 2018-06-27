package com.passinhotv.android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.passinhotv.android.R;

public class BoxActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_back, btn_chat, btn_transfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_box);
        initView();
    }
    public void initView(){
        btn_back = findViewById(R.id.btn_back);
        btn_chat = findViewById(R.id.btn_chat);
        btn_transfer = findViewById(R.id.btn_transfer);
        btn_back.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        btn_transfer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_chat:
                break;
            case R.id.btn_transfer:
                Intent intent = new Intent(BoxActivity.this, TransferActivity.class);
                startActivity(intent);
                break;
        }
    }
}
