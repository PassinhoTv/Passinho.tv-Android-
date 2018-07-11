package com.passinhotv.android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.passinhotv.android.GlobalVar;
import com.passinhotv.android.R;
import com.passinhotv.android.ui.auth.EnvironmentManager;
import com.passinhotv.android.ui.auth.LoginActivity;
import com.passinhotv.android.util.AppUtil;
import com.passinhotv.android.util.PrefsUtil;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        AppUtil appUtil = new AppUtil(this);
        EnvironmentManager.init(new PrefsUtil(this), appUtil);
        initApp();
    }

    public void initApp() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String strPwd = prefs.getString(GlobalVar.KEY_INTENT_PASSWORD, "");
        String strPrivate = prefs.getString(GlobalVar.KEY_INTENT_PRIVATE, "");
        String strAddress = prefs.getString(GlobalVar.KEY_INTENT_ADDRESS, "");
        String strPublic = prefs.getString(GlobalVar.KEY_INTENT_PUBLIC, "");
        String strProfileName = prefs.getString(GlobalVar.KEY_INTENT_PROFILE_NAME, "");
        String strProfileEmail = prefs.getString(GlobalVar.KEY_INTENT_PROFILE_EMAIL, "");
        String strProfileAddress = prefs.getString(GlobalVar.KEY_INTENT_PROFILE_ADDRESS, "");
        String strProfilePhone = prefs.getString(GlobalVar.KEY_INTENT_PROFILE_PHONE, "");


        GlobalVar.strAddressEncrypted = strAddress;
        try {
            strPwd = GlobalVar.decryptMsg(strPwd);
            strPrivate = GlobalVar.decryptMsg(strPrivate);
            strAddress = GlobalVar.decryptMsg(strAddress);
            strPublic = GlobalVar.decryptMsg(strPublic);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        GlobalVar.strPwd = strPwd;
        GlobalVar.strPrivate = strPrivate;
        GlobalVar.strAddress = strAddress;
        GlobalVar.strPublic = strPublic;
        //Profile Settings
        GlobalVar.strNameProfile = strProfileName;
        GlobalVar.strEmailProfile = strProfileEmail;
        GlobalVar.strAddressProfile = strProfileAddress;
        GlobalVar.strPhoneProfile = strProfilePhone;


        if (!strPrivate.equals("") && !strPwd.equals("")) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                    SplashActivity.this.finish();
                }
            }, 4000);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                    SplashActivity.this.finish();
                }
            }, 4000);
        }
    }
}
