package com.passinhotv.android.ui.auth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.passinhotv.android.GlobalVar;
import com.passinhotv.android.R;
import com.passinhotv.android.auth.WalletManager;
import com.pkmmte.view.CircularImageView;

import java.util.Arrays;


public class Getting_1_Activity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1889;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    CircularImageView img_avatar;


    TextView tx_crie;
    TextView tx_local;
    TextView tx_signup;
    EditText edt_pwd,edt_confirm,edt_avatar;
    Button btn_continue, btn_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_getting_1_);
        initView();
    }
    public void initView(){
        tx_crie = findViewById(R.id.tx_crie);
        tx_local = findViewById(R.id.tx_local);
        tx_signup = findViewById(R.id.tx_signup);
        edt_pwd = findViewById(R.id.edt_pwd);
        edt_confirm= findViewById(R.id.edt_confirm);
        edt_avatar= findViewById(R.id.edt_avatar);
        img_avatar = findViewById(R.id.img_avatar);
        btn_continue = findViewById(R.id.btn_continue);
        btn_upload = findViewById(R.id.btn_upload);

        String strCrie = "<font color='white'>Crie uma conta gratuita abaixo ou </font>";
        tx_crie.setText(Html.fromHtml(strCrie), TextView.BufferType.SPANNABLE);
        String strLocal = "<font color='red'>Escolha seu próprio AvatarEsta é uma senha </font><font color='#a7ce39'>local.\n" +
                "</font><font color='red'>Esta senha é local para o seu dispositivo e </font><font color='#a7ce39'>NÃO</font><font color='red'> será carregada na Rede Descentralizada Passinho.</font>";
        tx_local.setText(Html.fromHtml(strLocal), TextView.BufferType.SPANNABLE);
        tx_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Getting_1_Activity.this, Welcome_1_Activity.class);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
                Getting_1_Activity.this.finish();
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strPwd = String.valueOf(edt_pwd.getText());
                String strConfirm = String.valueOf(edt_confirm.getText());
                if (strPwd.length() < 6)
                    Toast.makeText(getApplicationContext(),"Password must longer than 6 characters",Toast.LENGTH_SHORT).show();
                else if(strPwd.equals(strConfirm)) {
                    GlobalVar.strPwd = String.valueOf(edt_pwd.getText());
                    Intent intent = new Intent(Getting_1_Activity.this, Getting_2_Activity.class);
                    startActivityForResult(intent, 1000);
                }else if (!strPwd.equals(strConfirm)){
                    Toast.makeText(getApplicationContext(),"Password not match",Toast.LENGTH_SHORT).show();
                }
            }
        });
        GlobalVar.strSeeds = WalletManager.createWalletSeed(this);

        GlobalVar.mSeeds = Arrays.asList(GlobalVar.strSeeds.split(" "));
    }
    public void changeAvatar(View v){
        showPictureDialog();
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @SuppressLint("NewApi")
    private void takePhotoFromCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1000) {
            setResult(1000);
            this.finish();
        }
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img_avatar.setImageBitmap(photo);
        }
        if(requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            img_avatar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }
}
