package com.passinhotv.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.passinhotv.android.GlobalVar;
import com.passinhotv.android.R;
import com.passinhotv.android.components.CircleTransform;
import com.squareup.picasso.Picasso;

import static android.graphics.Color.TRANSPARENT;

public class MainFlowActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, PopupMenu.OnDismissListener {
    Button btn_qr, btn_updte;
    Bitmap bmp_qr = null;
    ProgressDialog dialog;
    ImageButton btn_first, btn_second, btn_third, btn_fourth, btn_fifth;
    View v_first, v_second, v_search;
    ImageView img_second_profile;
    int nBtnIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_flow);
        initUI();
//        initQRCode();
    }

    public void initUI() {
        btn_qr = findViewById(R.id.btn_qr);
        btn_updte = findViewById(R.id.btn_update);
        btn_qr.setOnClickListener(this);
        btn_updte.setOnClickListener(this);
        btn_first = findViewById(R.id.btn_first);
        btn_second = findViewById(R.id.btn_second);
        btn_third = findViewById(R.id.btn_third);
        btn_fourth = findViewById(R.id.btn_fourth);
        btn_fifth = findViewById(R.id.btn_fifth);
        btn_first.setOnClickListener(this);
        btn_second.setOnClickListener(this);
        btn_third.setOnClickListener(this);
        btn_fourth.setOnClickListener(this);
        btn_fifth.setOnClickListener(this);
        v_first = findViewById(R.id.view_first);
        v_second = findViewById(R.id.view_second);
        v_search = findViewById(R.id.view_search);
        img_second_profile = v_second.findViewById(R.id.img_profile);
        nBtnIndex = 1;
        btn_first.setImageResource(R.drawable.tab_first_on);

        btn_first.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                if (nBtnIndex == 1) {
                    btn_first.setImageResource(R.drawable.tab_first_menu);
                    showMenu(v);
                }
                return true;
            }
        });
        btn_second.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                if (nBtnIndex == 2) {
                    btn_second.setImageResource(R.drawable.tab_second_menu);
                    showMenu(v);
                }
                return true;
            }
        });
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);// to implement on click event on items of menu
        popup.setOnDismissListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.type_menu, popup.getMenu());
        popup.show();
    }

    public void initQRCode() {
        dialog = ProgressDialog.show(MainFlowActivity.this, "",
                "Please wait...", true);
        new Thread(new Runnable() {
            public void run() {
                String strAddress = GlobalVar.strAddressEncrypted;
                try {
                    bmp_qr = encodeAsBitmap(strAddress);
                    dialog.dismiss();
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        int WIDTH = 500;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? TRANSPARENT : getResources().getColor(R.color.color_green);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_qr:
                showDialog(this);
                break;
            case R.id.btn_update:
                Intent intent = new Intent(MainFlowActivity.this, BoxActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_first:
                btn_first.setImageResource(R.drawable.tab_first_on);
                btn_second.setImageResource(R.drawable.tab_second_off);
                btn_fourth.setImageResource(R.drawable.tab_fourth_off);
                v_first.setVisibility(View.VISIBLE);
                v_second.setVisibility(View.GONE);
                v_search.setVisibility(View.GONE);
                nBtnIndex = 1;
                break;
            case R.id.btn_second:
                btn_first.setImageResource(R.drawable.tab_first_off);
                btn_second.setImageResource(R.drawable.tab_second_on);
                btn_fourth.setImageResource(R.drawable.tab_fourth_off);
                v_first.setVisibility(View.GONE);
                v_second.setVisibility(View.VISIBLE);
                v_search.setVisibility(View.GONE);
                nBtnIndex = 2;
                break;
            case R.id.btn_third:
                nBtnIndex = 3;
                Intent mIntent = new Intent(MainFlowActivity.this, PostActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_fourth:
                btn_first.setImageResource(R.drawable.tab_first_off);
                btn_second.setImageResource(R.drawable.tab_second_off);
                btn_fourth.setImageResource(R.drawable.tab_fourth_on);
                v_first.setVisibility(View.GONE);
                v_second.setVisibility(View.GONE);
                v_search.setVisibility(View.VISIBLE);
                nBtnIndex = 4;
                break;
            case R.id.btn_fifth:
                mIntent = new Intent(MainFlowActivity.this, ProfileActivity.class);
                startActivity(mIntent);
                nBtnIndex = 5;
                break;
            default:
                break;
        }
    }


    public void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_qrcode);
        LinearLayout lyt_dlg = dialog.findViewById(R.id.lyt_qr);
        ImageView img_qr = dialog.findViewById(R.id.img_qr);
        img_qr.setImageBitmap(bmp_qr);
        lyt_dlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_all:
                break;
            case R.id.menu_text:
                break;
            case R.id.menu_photo:
                break;
        }
        if (nBtnIndex == 1) {
            btn_first.setImageResource(R.drawable.tab_first_on);
        } else if (nBtnIndex == 2) {
            btn_second.setImageResource(R.drawable.tab_second_on);
        }
        return true;
    }

    @Override
    public void onDismiss(PopupMenu menu) {
        if (nBtnIndex == 1) {
            btn_first.setImageResource(R.drawable.tab_first_on);
        } else if (nBtnIndex == 2) {
            btn_second.setImageResource(R.drawable.tab_second_on);
        }
    }
}
