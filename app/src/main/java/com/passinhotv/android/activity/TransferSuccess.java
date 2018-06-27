package com.passinhotv.android.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.passinhotv.android.GlobalVar;
import com.passinhotv.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TransferSuccess extends AppCompatActivity implements View.OnClickListener {
    EditText et_receipt, et_desc;
    TextView tx_favelas, tx_transaction, tx_time, tx_consult;
    Button btn_back, btn_transfer;
    String strRecipt, strDesc, strId, strHeight, strAmount;
    Boolean isUnconfirmed = false;
    long nTime;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_transfer_success);
        strRecipt = getIntent().getStringExtra("receipient");
        strId = getIntent().getStringExtra("id");
        strDesc = getIntent().getStringExtra("description");
        nTime = getIntent().getLongExtra("timestamp", 0);
        isUnconfirmed = getIntent().getBooleanExtra("unconfirmed", false);
        strHeight = getIntent().getStringExtra("height");
        strAmount = getIntent().getStringExtra("amount");
//        dialog = ProgressDialog.show(TransferSuccess.this, "",
//                "Checking transaction info...", true);
//        getInfo();
//        getConfirm();
        initView();

    }
    public void initView(){
        et_receipt = findViewById(R.id.edt_address);
        et_desc = findViewById(R.id.edt_desc);

        tx_transaction = findViewById(R.id.tx_site);
        tx_time = findViewById(R.id.tx_time);
        tx_consult = findViewById(R.id.tx_consult);
        tx_favelas = findViewById(R.id.tx_favelas);
        String strfees = "<font color='white'><font color='red'>" + strAmount+ "</font> Favelas</font>";
        tx_favelas.setText(Html.fromHtml(strfees), TextView.BufferType.SPANNABLE);
//        if(isUnconfirmed){
//            tx_consult.setText("Não Confirmado");
//        }else{
//            tx_consult.setText("Confirmado");
//        }
//        tx_confirm.setText(strHeight);

        et_receipt.setText(strRecipt);
        et_desc.setText(strDesc);
        Date mDate = new Date(nTime);
        DateFormat df = new SimpleDateFormat("dd:MM:yy, HH:mm");
        tx_time.setText(df.format(mDate));

        btn_back = findViewById(R.id.btn_back);
        btn_transfer= findViewById(R.id.btn_transfer);
        btn_back.setOnClickListener(this);
        btn_transfer.setOnClickListener(this);
        tx_transaction.setOnClickListener(this);
    }
    public void getInfo(){
        RequestQueue chckConfirmReque = Volley.newRequestQueue(this);
        Intent intent = new Intent(TransferSuccess.this, TransferSuccess.class);
        Bundle mBundle = new Bundle();
        String url = GlobalVar.BASE_URL + "/transactions/info/" + strId;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                long nTimeStamp = 0;
                dialog.dismiss();
                try {
                    long height = response.getLong("height");
                    strHeight = "" + height;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("key", "Value");
                return headers;
            }
        };
        chckConfirmReque.add(req);
    }
    public void getConfirm(){
        RequestQueue chckConfirmReque = Volley.newRequestQueue(TransferSuccess.this);
        String url = GlobalVar.BASE_URL + "/transactions/unconfirmed";
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                dialog.dismiss();
                Log.d("reponse",response.toString());
                initView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.d("error",error.toString());
                initView();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        chckConfirmReque.add(req);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_chat:
                break;
            case R.id.btn_transfer:
                Intent intent = new Intent(TransferSuccess.this, TransferActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.tx_site:
                openWebPage("http://136.33.116.116:3000/tx/" + strId);
                break;
        }
    }

    public void openWebPage(String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request. Please install a web browser or check your URL.",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
