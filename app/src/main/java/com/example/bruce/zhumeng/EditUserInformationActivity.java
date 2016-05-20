package com.example.bruce.zhumeng;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.bruce.zhumeng.utils.DensityUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by zhang on 2016/5/19.
 */
public class EditUserInformationActivity extends AppCompatActivity {

    private static final String TAG = EditUserInformationActivity.class.getSimpleName();

    private static final int FROM_GALLERY_REQUEST = 1;
    private static final int FROM_CAMERA_REQUEST  = 2;
    private static final int CROP_RESULT_REQUEST  = 3;
    private static final int OUTPUT_X             = DensityUtils.dp2px(64);
    private static final int OUTPUT_Y             = DensityUtils.dp2px(64);

    private static final int UPLOAD_SUCC = 4;
    private static final int UPLOAD_FAILED = 5;

    private static final String CACHE_DIR             = "/zhumeng/cache/image";
    private static final String AVATAR_IMAGE_FILENAME = "tempImage.jpg";
    private static final String AVATAR_CROP_FILENAME  = "tempCropImage.png";

    private Toolbar   toolbar;
    private Button    changeAvatar;
    private EditText  interestEt;
    private ImageView avatar;
    private String    avatarUrl;
    private String    interest;
    private Drawable  avatarDrawable;
    private EditUserInfoHandler userInfoHandler;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_userinformation_mainview);
        avatarUrl = getIntent().getStringExtra("avatar_url");
        interest = getIntent().getStringExtra("interest");
        Log.d(TAG,"avatarUrl="+avatarUrl);
        init();
    }

    private void init() {
        userInfoHandler = new EditUserInfoHandler(this);
        TextView userName = (TextView) findViewById(R.id.user_name);
        userName.setText(AVUser.getCurrentUser().getUsername());
        toolbar = (Toolbar) findViewById(R.id.edit_user_information_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        changeAvatar = (Button) findViewById(R.id.change_avatar);
        interestEt = (EditText) findViewById(R.id.interest);
        if(interest != null) {
            interestEt.setText(interest);
        }
        avatar = (ImageView) findViewById(R.id.avatar);
        if (avatarUrl != null) {
            Picasso.with(this).load(avatarUrl).into(avatar);
        }
        avatarDrawable = avatar.getDrawable();
        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditUserInformationActivity.this);
                builder.setTitle(R.string.change_avatar);
                final String[] changeAvatarChoice = {"拍照", "从图库选择"};
                builder.setItems(changeAvatarChoice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                changeAvatarFromCameraCapture();
                                break;
                            case 1:
                                changeAvatarFromGallery();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        initBroadcast();
    }

    private void initBroadcast() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    private void changeAvatarFromCameraCapture() {
        if (hasSDCard()) {
            //创建文件夹
            File file = new File(Environment.getExternalStorageDirectory() + CACHE_DIR);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    Log.d(TAG, "create cache dir failed");
                }
            }
            // 创建File对象，用于存储拍照后的图片
            File outputImage = new File(Environment.getExternalStorageDirectory() + CACHE_DIR, AVATAR_IMAGE_FILENAME);
            Intent fromCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            fromCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImage));
            startActivityForResult(fromCameraIntent, FROM_CAMERA_REQUEST);
        }
    }

    private void changeAvatarFromGallery() {
        Intent fromGalleryIntent = new Intent();
        fromGalleryIntent.setType("image/*");
        fromGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(fromGalleryIntent, FROM_GALLERY_REQUEST);
    }

    /*
    crop image
     */
    private void cropRawPhoto(Uri uri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", true);

        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);

        cropIntent.putExtra("outputX", OUTPUT_X);
        cropIntent.putExtra("outputY", OUTPUT_Y);
        cropIntent.putExtra("return-data", true);

        startActivityForResult(cropIntent, CROP_RESULT_REQUEST);
    }

    private void setAvatar(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            avatar.setImageBitmap(photo);
            saveCropImage(photo);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case FROM_CAMERA_REQUEST:
                    File temp = new File(Environment.getExternalStorageDirectory() + CACHE_DIR, AVATAR_IMAGE_FILENAME);
                    cropRawPhoto(Uri.fromFile(temp));
                    break;
                case FROM_GALLERY_REQUEST:
                    cropRawPhoto(data.getData());
                    break;
                case CROP_RESULT_REQUEST:
                    Log.d(TAG, "crop result request");
                    setAvatar(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
    保存裁剪后的图片到本地
     */
    private void saveCropImage(Bitmap cropPhoto) {

        File file = new File(Environment.getExternalStorageDirectory() + CACHE_DIR, AVATAR_CROP_FILENAME);
        File deleteFile = new File(Environment.getExternalStorageDirectory() + CACHE_DIR, AVATAR_IMAGE_FILENAME);
        if (deleteFile.exists()) {
            if (!deleteFile.delete()) {
                Log.d(TAG, "delete avatar origin file failed");
            }
        }
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            cropPhoto.compress(Bitmap.CompressFormat.PNG, 30, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveCropImageServer() {
        try {
            final AVFile file = AVFile.withFile(AVATAR_CROP_FILENAME, new File(Environment.getExternalStorageDirectory() + CACHE_DIR, AVATAR_CROP_FILENAME));
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        Log.d(TAG, "file upload succ");
                        saveCropImageForUser(file);
                    } else {
                        Log.d(TAG, "failed");
                        Toast.makeText(EditUserInformationActivity.this, "修改头像失败", Toast.LENGTH_LONG).show();
                    }
                }
            }, new ProgressCallback() {
                @Override
                public void done(Integer integer) {
                    Log.d(TAG, "integer=" + integer);
                }
            });
        } catch (IOException e) {
            Log.d(TAG, "exception");
            e.printStackTrace();
        }
    }

    private void saveCropImageForUser(AVFile file) {
        AVUser user = AVUser.getCurrentUser();
        user.put("avatar", file);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    userInfoHandler.sendEmptyMessage(UPLOAD_SUCC);

                } else {
                    userInfoHandler.sendEmptyMessage(UPLOAD_FAILED);

                }
            }
        });
    }

    private void saveInfoForUser(String interest) {
        AVUser user = AVUser.getCurrentUser();
        user.put("interest",interest);
        user.saveInBackground();
    }

    /*
    check has sdcard
     */
    private boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_user_information_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_done:
                if (avatarDrawable == avatar.getDrawable() &&
                        ( interestEt.getText() == null ||
                                (interest != null && interest.equals(interestEt.getText().toString()))
                        )) {
                    finish();
                } else {
                    if (interestEt.getText() != null ||
                            (interest!=null && !interest.equals(interestEt.getText().toString())
                            )) {
                        saveInfoForUser(interestEt.getText().toString());
                    }

                    if (avatarDrawable != avatar.getDrawable()) {
                        saveCropImageServer();
                    } else {
                        finish();
                    }
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static class EditUserInfoHandler extends Handler {
        WeakReference<EditUserInformationActivity> weakReference;
        public EditUserInfoHandler(EditUserInformationActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final EditUserInformationActivity activity = weakReference.get();
            if(activity != null) {
                switch (msg.what) {
                    case UPLOAD_FAILED:
                        Toast.makeText(activity, "修改头像失败", Toast.LENGTH_LONG).show();
                        break;
                    case UPLOAD_SUCC:
                        Toast.makeText(activity, "修改头像成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent("com.example.bruce.zhumeng.AVATAR_CHANGE");
                        activity.sendBroadcast(intent);
                        Log.d(TAG,"send broadcast");
                        activity.finish();
                        break;
                }
            }
        }
    }
}