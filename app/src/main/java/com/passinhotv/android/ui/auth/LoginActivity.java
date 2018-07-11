package com.passinhotv.android.ui.auth;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.passinhotv.android.GlobalVar;
import com.passinhotv.android.activity.MainFlowActivity;
import com.passinhotv.android.R;

public class LoginActivity extends AppCompatActivity {

    TextView tx_login;
    EditText ed_pwd;
    Button btn_continue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        initView();
    }
    public void initView(){
        tx_login = findViewById(R.id.tx_login);
        ed_pwd = findViewById(R.id.edt_pwd);
        btn_continue = findViewById(R.id.btn_continue);
        tx_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Getting_1_Activity.class);
                startActivity(intent);
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strPwd = String.valueOf(ed_pwd.getText());
                if(strPwd.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter password.",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(strPwd.equals(GlobalVar.strPwd)){
                        Intent intent = new Intent(LoginActivity.this, MainFlowActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Password not match.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
