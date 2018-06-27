package com.passinhotv.android.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;
import com.passinhotv.android.GlobalVar;
import com.passinhotv.android.R;
import com.passinhotv.android.request.TransferTransactionRequest;
import com.passinhotv.android.util.AddressUtil;
import com.wavesplatform.wavesj.PrivateKeyAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class TransferActivity extends AppCompatActivity implements View.OnClickListener, ZXingScannerView.ResultHandler {
    Button btn_qr, btn_continue, btn_back, btn_chat;
    EditText et_waves, et_reais, et_desc, et_address, et_fee;
    TextView tx_feavelas, tx_reais, tx_option;
    Dialog dialog;
    String strAddressTo = "";
    String strDesc = "";
    long balance, amount;
    long customFee = (long) 2000000;
    String strId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_transfer);
        checkBalance();
        initView();
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }

    public void initView() {
//        Node node = new Node();
        btn_qr = findViewById(R.id.btn_scan_qr);
        btn_continue = findViewById(R.id.btn_continue);
        btn_back = findViewById(R.id.btn_back);
        btn_chat = findViewById(R.id.btn_chat);
        btn_qr.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_chat.setOnClickListener(this);

        tx_feavelas = findViewById(R.id.tx_favelas);
        tx_reais = findViewById(R.id.tx_reais);
        tx_option = findViewById(R.id.tx_option);
        String strCrie = "<font color='white'>As Transações Enviadas <font color='red'>NÃO</font> São Reversíveis.</font>";
        tx_option.setText(Html.fromHtml(strCrie), TextView.BufferType.SPANNABLE);
        et_waves = findViewById(R.id.edt_amount);
        et_address = findViewById(R.id.edt_address);
        et_desc = findViewById(R.id.edt_desc);
        et_reais = findViewById(R.id.edt_converted);
        et_fee = findViewById(R.id.edt_tax);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_continue:
                checkSend();
                break;
            case R.id.btn_chat:
                break;
            case R.id.btn_scan_qr:
                Intent intent = new Intent(TransferActivity.this, ScanQRActivity.class);
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                strAddressTo = intent.getStringExtra("address");
                try {
                    strAddressTo = GlobalVar.decryptMsg(strAddressTo);
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
                } catch (Exception e) {
                }
                et_address.setText(strAddressTo);
            }
        }
    }

    public void checkSend() {
        strAddressTo = String.valueOf(et_address.getText());
        strDesc = String.valueOf(et_desc.getText());
        try {
            amount = (long) (Float.parseFloat(String.valueOf(et_waves.getText())) * 100000000);
        } catch (Exception e) {

        }
//        customFee = 2000000;
        int res = validateTransfer();
        if (res == 0) {
            showDialog(this);
        } else {
            Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
        }
    }

    public void showDialog(Activity activity) {
        dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_transfer);
        Button btn_cancel = dialog.findViewById(R.id.btn_back);
        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);
        TextView tx_favelas = dialog.findViewById(R.id.tx_favelas);
        TextView tx_reais= dialog.findViewById(R.id.tx_reais);
        TextView tx_fees = dialog.findViewById(R.id.tx_fee);
        String strFavels = "<font color='red'>" + String.valueOf(et_waves.getText()) + "</font><font color='black'> Favelas</font>";
        tx_favelas.setText(Html.fromHtml(strFavels), TextView.BufferType.SPANNABLE);
        String strfees = "<font color='black'>Taxa</font><font color='red'> - 0.020000 </font><font color='black'> Favelas</font>";

        tx_fees.setText(Html.fromHtml(strfees), TextView.BufferType.SPANNABLE);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doTransfer();
            }
        });
        dialog.show();
    }

    @SuppressLint("CheckResult")
    public void doTransfer() {
        dialog.dismiss();
        long timestamp = System.currentTimeMillis();
        dialog = ProgressDialog.show(TransferActivity.this, "",
                "Processing Transcation...", true);
        TransferTransactionRequest tx = new TransferTransactionRequest(
                GlobalVar.assetID,
                GlobalVar.strPublic,
                strAddressTo, amount, timestamp, customFee, strDesc);
        PrivateKeyAccount mTemp = PrivateKeyAccount.fromPrivateKey(GlobalVar.strPrivate, 'N');
        byte[] mbyte = mTemp.getPrivateKey();
        tx.sign(mbyte);
        String strSignature = tx.getSignature();
        Log.d("Sign", "Sign");

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String URL = GlobalVar.BASE_URL + "/assets/broadcast/transfer";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("senderPublicKey", GlobalVar.strPublic);
            jsonBody.put("assetId", GlobalVar.assetID);
            jsonBody.put("recipient", strAddressTo);
            jsonBody.put("amount", amount);
            jsonBody.put("fee", customFee);
            jsonBody.put("feeAssetId", GlobalVar.assetID);
            jsonBody.put("timestamp", timestamp);
            jsonBody.put("attachment", tx.attachment);
            jsonBody.put("signature", strSignature);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                URL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.i("Response", String.valueOf(response));
                long nTimeStamp = 0;
                try {
                    strId = response.getString("id");
                    nTimeStamp = response.getLong("timestamp");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(TransferActivity.this, TransferSuccess.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("receipient", strAddressTo);
                mBundle.putString("id", strId);
                mBundle.putLong("timestamp", nTimeStamp);
                mBundle.putString("description", strDesc);
                mBundle.putBoolean("unconfirmed", false);
                mBundle.putString("amount",String.valueOf(et_waves.getText()));
                intent.putExtras(mBundle);
                startActivity(intent);
                TransferActivity.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Transaction Failed! Try again later", Toast.LENGTH_SHORT).show();
                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void checkBalance(){
        dialog = ProgressDialog.show(TransferActivity.this, "",
                "Please wait...", true);
        RequestQueue chckConfirmReque = Volley.newRequestQueue(this);
        String url = GlobalVar.BASE_URL + "/assets/balance/" + GlobalVar.strAddress;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray mbalance = response.getJSONArray("balances");
                    JSONObject mdata = mbalance .getJSONObject(0);
                    String strBalance = mdata.getString("balance");
                    balance = Long.parseLong(strBalance);
                    double dBalance = (double)balance / 100000000;
                    String strBa = String.format("%.3f", dBalance);
                    TransferActivity.this.tx_feavelas.setText(strBa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
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

    public void chckInfo(String url, long nTimeStamp) {
        RequestQueue chckConfirmReque = Volley.newRequestQueue(this);
        Intent intent = new Intent(TransferActivity.this, TransferSuccess.class);
        Bundle mBundle = new Bundle();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mBundle.putString("receipient", strAddressTo);
                mBundle.putString("id", strId);
                mBundle.putLong("timestamp", nTimeStamp);
                mBundle.putString("description", strDesc);
                mBundle.putBoolean("unconfirmed", false);
                intent.putExtras(mBundle);
                startActivity(intent);
                TransferActivity.this.finish();
                TransferActivity.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mBundle.putString("receipient", strAddressTo);
                mBundle.putString("id", strId);
                mBundle.putLong("timestamp", nTimeStamp);
                mBundle.putString("description", strDesc);
                mBundle.putBoolean("unconfirmed", false);
                intent.putExtras(mBundle);
                startActivity(intent);
                TransferActivity.this.finish();
            }
        });
        chckConfirmReque.add(req);
    }

    private int validateTransfer() {
        if (!AddressUtil.isValidAddress(strAddressTo)) {
            return R.string.invalid_address;
        } else if (strDesc.length() > TransferTransactionRequest.MaxAttachmentSize) {
            return R.string.attachment_too_long;
        } else if (amount <= 0) {
            return R.string.invalid_amount;
        } else if (amount > Long.MAX_VALUE - customFee) {
            return R.string.invalid_amount;
        } else if (customFee <= 0 || customFee < TransferTransactionRequest.MinFee) {
            return R.string.insufficient_fee;
        } else if (GlobalVar.strAddress.equals(strAddressTo)) {
            return R.string.send_to_same_address_warning;
        }
        return 0;
    }

    @Override
    public void handleResult(Result result) {

    }
}
