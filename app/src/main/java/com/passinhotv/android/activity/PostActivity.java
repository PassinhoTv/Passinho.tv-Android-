package com.passinhotv.android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.passinhotv.android.R;

public class PostActivity extends AppCompatActivity {
    Button btn_back;
    ImageView img_logo;
    TextView tx_warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_post);
        initUI();
    }
    public void initUI(){
        btn_back = findViewById(R.id.btn_back);
        img_logo = findViewById(R.id.img_logo);
        tx_warning = findViewById(R.id.tx_warning);
        String strCrie = "<font color='white'>Não sofra de</font><font color='red'> Síndrome do Vídeo Vertical</font><font color='white'>. (VVS)<br>Gire o seu telefone 90 ° durante a gravação.</font>";
        tx_warning.setText(Html.fromHtml(strCrie), TextView.BufferType.SPANNABLE);

        btn_back.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                PostActivity.this.finish();
            }
        });
        img_logo.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

            }
        });
    }
    public void openCamera(View v){
        Intent intent = new Intent(this, PublishActivity.class);
        startActivity(intent);
    }
}
