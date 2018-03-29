package com.example.liushanpu.mycamerademo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //define the requestCode of the start camera
    private static int REQ_CODE_CAMERA = 1;
    private static int REQ_CODE_CAMERA_2 = 2;
    private static final int REQ_CODE_EXTERNAL_STOREAGE = 3;

    private String mFilePath;

    private ImageView mImageView;

    public static final String TAG = "liusp-MyCameraDemo";

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.iv_showPicture);
        //mFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        mFilePath = Environment.getExternalStorageDirectory() + "/images/" + System.currentTimeMillis() + ".jpg";
        android.util.Log.d(TAG, "mFilePath=" + mFilePath);
    }


    public void startCamera1(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQ_CODE_CAMERA);
    }

    public void startCamera2(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //如下方式在Android 7.0上及以后会造成FileUriExposedException，建议使用FileProvider
        //Uri photoUri = Uri.fromFile(new File(mFilePath));

        File outputFile = new File(mFilePath);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdir();
        }
        Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".myfileprovider", outputFile);
        android.util.Log.d(TAG, "contentUri=" + contentUri.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        startActivityForResult(intent, REQ_CODE_CAMERA_2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE_CAMERA) {
                Bundle bundle = data.getExtras();
                //The data from data is Thumbnail image
                Bitmap bitmap = (Bitmap) bundle.get("data");
                mImageView.setImageBitmap(bitmap);
            } else if (requestCode == REQ_CODE_CAMERA_2) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(mFilePath);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    mImageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQ_CODE_EXTERNAL_STOREAGE);
        }
    }

}
