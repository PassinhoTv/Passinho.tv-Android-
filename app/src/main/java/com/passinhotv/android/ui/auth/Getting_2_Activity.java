package com.passinhotv.android.ui.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.common.base.Charsets;
import com.passinhotv.android.GlobalVar;
import com.passinhotv.android.R;
import com.passinhotv.android.auth.WavesWallet;

import me.originqiu.library.EditTag;

public class Getting_2_Activity extends AppCompatActivity {
    Button btn_continue;
    EditTag et_words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_getting_2_);
        initView();
    }

    public void initView() {
        et_words = findViewById(R.id.edit_tag_view);
        btn_continue = findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Getting_2_Activity.this, Getting_3_Activity.class);
                startActivityForResult(intent, 1000);
            }
        });
        et_words.setEditable(false);
        et_words.setTagList(GlobalVar.mSeeds);
        WavesWallet newWallet = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            newWallet = new WavesWallet(GlobalVar.strSeeds.getBytes(Charsets.UTF_8));
        }
        GlobalVar.mWallet = newWallet;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1000) {
            setResult(1000);
            this.finish();
        }
    }
}
