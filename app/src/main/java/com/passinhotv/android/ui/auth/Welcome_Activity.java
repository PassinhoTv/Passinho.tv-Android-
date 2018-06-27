package com.passinhotv.android.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.passinhotv.android.GlobalVar;
import com.passinhotv.android.activity.MainFlowActivity;
import com.passinhotv.android.R;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Welcome_Activity extends AppCompatActivity implements View.OnClickListener {
    TextView tx_first, tx_second, tx_third, tx_done;
    CheckBox chk_first, chk_second, chk_third;
    Button btn_continue;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);
        initView();
    }

    public void initView() {

        tx_done = findViewById(R.id.tx_done);
        tx_first = findViewById(R.id.tx_welcom_first);
        tx_second = findViewById(R.id.tx_welcom_sec);
        tx_third = findViewById(R.id.tx_welcom_third);
        tx_first.setOnClickListener(this);
        tx_second.setOnClickListener(this);
        tx_third.setOnClickListener(this);

        chk_first = findViewById(R.id.chk_first);
        chk_second = findViewById(R.id.chk_second);
        chk_third = findViewById(R.id.chk_third);

        btn_continue = findViewById(R.id.btn_continue);

        String strDone = "<font color='#a7ce39'>Passinho.Tv</font> é diferente - Sua conta é armazenada com segurança em seu dispositivo.";
        String strFirst = "Eu entendo que minhas informações são seguras em segurança neste dispositivo, <font color='red'>e não por uma empresa</font>.";
        String strSec = "Eu entendo que, se esse aplicativo for movido para outro dispositivo ou excluído, minha conta <font color='#a7ce39'>Passinho.Tv</font> só pode ser recuperada com a <font color='#007aff'>Frase de backup.</font>";
        String strThird = "Gostaria de ajudar a melhorar a plataforma <font color='#a7ce39'>Passinho.Tv</font> enviando estatísticas de uso anônimo para os desenvolvedores.";
        tx_done.setText(Html.fromHtml(strDone), TextView.BufferType.SPANNABLE);
        tx_first.setText(Html.fromHtml(strFirst), TextView.BufferType.SPANNABLE);
        tx_second.setText(Html.fromHtml(strSec), TextView.BufferType.SPANNABLE);
        tx_third.setText(Html.fromHtml(strThird), TextView.BufferType.SPANNABLE);
        btn_continue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(chk_first.isChecked() && chk_second.isChecked()) {
                    updateFirebase();
                }else{
                    Toast.makeText(getApplicationContext(),"Please accept terms.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void saveDataToLoacal(){
        SharedPreferences myPreferences= PreferenceManager.getDefaultSharedPreferences(Welcome_Activity.this);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        String strPwd = GlobalVar.strPwd;
        String strPrivate = GlobalVar.mWallet.getPrivateKeyStr();
        String strAddress = GlobalVar.mWallet.getAddress();
        String strPublic = GlobalVar.mWallet.getPublicKeyStr();
        GlobalVar.strAddress = strAddress;
        GlobalVar.strPrivate = strPrivate;
        GlobalVar.strPublic = strPublic;
        try {
            strPwd= GlobalVar.encryptMsg(GlobalVar.strPwd);
            strPrivate= GlobalVar.encryptMsg(GlobalVar.mWallet.getPrivateKeyStr());
            strAddress = GlobalVar.encryptMsg(GlobalVar.mWallet.getAddress());
            strPublic = GlobalVar.encryptMsg(GlobalVar.mWallet.getPublicKeyStr());
            GlobalVar.strAddressEncrypted = GlobalVar.encryptMsg(GlobalVar.strAddress);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        myEditor.putString(GlobalVar.KEY_INTENT_PASSWORD, strPwd);
        myEditor.putString(GlobalVar.KEY_INTENT_PRIVATE, strPrivate);
        myEditor.putString(GlobalVar.KEY_INTENT_PUBLIC, strPublic);
        myEditor.putString(GlobalVar.KEY_INTENT_ADDRESS, strAddress);
        myEditor.commit();
        dialog.dismiss();
        Intent intent = new Intent(Welcome_Activity.this, MainFlowActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        Welcome_Activity.this.finish();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tx_welcom_first:
                chk_first.setChecked(!chk_first.isChecked());
                break;
            case R.id.tx_welcom_sec:
                chk_second.setChecked(!chk_second.isChecked());
                break;
            case R.id.tx_welcom_third:
                chk_third.setChecked(!chk_third.isChecked());
                break;

        }
    }

    public void updateFirebase() {
        dialog = ProgressDialog.show(Welcome_Activity.this, "",
                "Please wait...", true);
        GlobalVar.mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        GlobalVar.mStorageRef = FirebaseStorage.getInstance().getReference();
        GlobalVar.mDatabaseRef.child("addresslist/").orderByKey().equalTo(GlobalVar.mWallet.getAddress()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot == null) {
                        Toast.makeText(getApplicationContext(), "No Data Available",
                                Toast.LENGTH_LONG).show();
                        addAddressToFB();
                        return;
                    }
                    Map<String, Object> dataList = (Map<String, Object>) dataSnapshot.getValue();
                    if (dataList == null) {
                        addAddressToFB();
                        return;
                    }
                    saveDataToLoacal();
                } catch (Exception e) {
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
    public void addAddressToFB(){
        DatabaseReference myRef;
        myRef = GlobalVar.mDatabaseRef.child("addresslist").child(GlobalVar.mWallet.getAddress());
        myRef.setValue(GlobalVar.mWallet.getPublicKeyStr());
        saveDataToLoacal();
    }
}
