package com.passinhotv.android.ui.auth;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.passinhotv.android.GlobalVar;
import com.passinhotv.android.R;
import com.passinhotv.android.auth.WavesWallet;
import com.passinhotv.android.ui.auth.EditComponent.EditSpacebar;

import java.util.Arrays;
import java.util.List;

import me.originqiu.library.EditTag;

public class Welcome_1_Activity extends AppCompatActivity {
    TextView tx_login;
    Button btn_contiue;
    EditText edt_address;
    EditSpacebar et_words;
    String strSeeds = "";
    WavesWallet newWallet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome_1_);
        initView();
    }

    public void initView() {
        tx_login = findViewById(R.id.tx_login);
        btn_contiue = findViewById(R.id.btn_continue);
        et_words = findViewById(R.id.edit_tag_view);
        et_words.setTagAddCallBack(new EditTag.TagAddCallback() {
            @Override
            public boolean onTagAdd(String s) {
                strSeeds = "";
                List<String> mTemp = et_words.getTagList();
                for(int i = 0; i < mTemp.size(); i ++){
                    strSeeds += mTemp.get(i);
                    strSeeds += " ";
                }
                strSeeds += s;
                if (strSeeds != null && strSeeds.length() > 0 && strSeeds.charAt(strSeeds.length() - 1) == ' ') {
                    strSeeds = strSeeds.substring(0, strSeeds.length() - 1);
                }
                getAddress();
                return true;
            }
        });
        et_words.setTagDeletedCallback(new EditTag.TagDeletedCallback() {
            @Override
            public void onTagDelete(String s) {
                strSeeds = "";
                List<String> mTemp = et_words.getTagList();
                for(int i = 0; i < mTemp.size(); i ++){
                    strSeeds += mTemp.get(i);
                    strSeeds += " ";
                }
                if (strSeeds != null && strSeeds.length() > 0 && strSeeds.charAt(strSeeds.length() - 1) == ' ') {
                    strSeeds = strSeeds.substring(0, strSeeds.length() - 1);
                }
                if(strSeeds != "")
                    getAddress();
                else{
                    newWallet = null;
                    edt_address.setText("");
                }
            }
        });
        edt_address = findViewById(R.id.edt_address);
        tx_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome_1_Activity.this, Getting_1_Activity.class);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                Welcome_1_Activity.this.finish();
            }
        });
        btn_contiue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newWallet != null) {
                    int nCount = et_words.getTagList().size();
                    if(nCount   != 15) {
                        Toast.makeText(getApplicationContext(),"Should be 15 words",Toast.LENGTH_SHORT).show();
                    }else{
                        GlobalVar.mWallet = newWallet;
                        GlobalVar.strSeeds = strSeeds;
                        GlobalVar.mSeeds = et_words.getTagList();
                        Intent intent = new Intent(Welcome_1_Activity.this, Welcome_2_Activity.class);
                        startActivityForResult(intent, 999);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Please enter 15 words",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 999) {
            this.finish();
        }
    }

    public void getAddress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            newWallet = new WavesWallet(strSeeds.getBytes(Charsets.UTF_8));
        }
        edt_address.setText(newWallet.getAddress());
    }
}
