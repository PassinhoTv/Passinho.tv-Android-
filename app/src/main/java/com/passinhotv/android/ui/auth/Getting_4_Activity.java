package com.passinhotv.android.ui.auth;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.passinhotv.android.GlobalVar;
import com.passinhotv.android.R;
import com.passinhotv.android.ui.auth.EditComponent.EditExtend;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Getting_4_Activity extends AppCompatActivity {

    Button btn_continue,btn_clear;
    EditExtend et_seeds;
    EditExtend et_selected;
    TextView tx_back;
    LinearLayout lyt_signUp;
    public List<String> mSelectedSeeds = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_getting_4_);
        initView();
    }
    public void initView(){
        btn_continue = findViewById(R.id.btn_continue);
        btn_clear = findViewById(R.id.btn_clear);
        tx_back = findViewById(R.id.tx_back);
        et_selected = findViewById(R.id.edit_tag_view_selected);
        et_selected.setEditable(false);
        et_seeds = findViewById(R.id.edit_tag_view);
        et_seeds.setEditable(false);
        lyt_signUp = findViewById(R.id.btn_signup);
        lyt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Getting_4_Activity.this, Welcome_1_Activity.class);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
                setResult(1000);
                Getting_4_Activity.this.finish();
            }
        });
        List<String> tempSeeds = new ArrayList<String>(GlobalVar.mSeeds);
        Random rand = new Random();
        for(int i =0; i < GlobalVar.mSeeds.size(); i ++){
            int nIndex = rand.nextInt(100) % tempSeeds.size();
            String strTemp = tempSeeds.get(nIndex);
            et_seeds.addTag(strTemp);
            tempSeeds.remove(nIndex);
        }

        et_seeds.setSelectedCallback(new EditExtend.SelectedCallback(){
            @Override
            public void onSelected(String strSelected) {
                mSelectedSeeds.add(strSelected);
                et_selected.addTag(strSelected);
                btn_clear.setVisibility(View.VISIBLE);
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanSelect();
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckSeeds();
            }
        });
        tx_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Getting_4_Activity.this.finish();
            }
        });
    }
    public void CheckSeeds(){
        boolean isMatch = true;
        for(int i = 0; i < mSelectedSeeds.size() ; i ++){
            if(!mSelectedSeeds.get(i).equals(GlobalVar.mSeeds.get(i)))
            {
                isMatch = false;
            }
        }
        if(!isMatch){
            Toast.makeText(getApplicationContext(),"Words Not Match. Try again",Toast.LENGTH_SHORT).show();
            cleanSelect();
        }
        else{
            if(mSelectedSeeds.size() == 15) {
                Intent intent = new Intent(Getting_4_Activity.this, Welcome_Activity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"Please select words in correct order.",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void cleanSelect(){
        btn_clear.setVisibility(View.INVISIBLE);
        mSelectedSeeds.clear();
        et_seeds.reset();
        et_selected.clear();
    }
}
