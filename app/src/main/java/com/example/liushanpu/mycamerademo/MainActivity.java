package com.example.liushanpu.mycamerademo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //define the requestCode of the start camera
    private static int REQ_CODE_CAMERA = 1;
    private static int REQ_CODE_CAMERA_2 = 2;
    private static final int REQ_CODE_EXTERNAL_STOREAGE = 3;
    private static final int REQ_CAMERA_PERMISSION = 4;

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
        verifyStoragePermission(this);

        //mFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        mFilePath = Environment.getExternalStorageDirectory() + "/liusp_images/" + System.currentTimeMillis() + ".jpg";
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, CustomCamera.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Pls grant camera permission to use Camera", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    public void customCamera(View view) {
        //startActivity(new Intent(this, CustomCamera.class));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQ_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, CustomCamera.class);
            startActivity(intent);
        }
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
                //This will get origin image, not thumbnail photo.
                FileInputStream fis = null;
                Bitmap bitmap = null;
                try {
                    fis = new FileInputStream(mFilePath);
                    bitmap = BitmapFactory.decodeStream(fis);
                    if (bitmap != null)
                        mImageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fis != null)
                            fis.close();
                        if (!bitmap.isRecycled()) {
                            //使用recycle和gc会crash，   Canvas: trying to use a recycled bitmap android.graphics.Bitmap@4fc3650
                            //bitmap.recycle();
                            bitmap = null;
                            //System.gc();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //add the Runtime Permission after Android L
    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQ_CODE_EXTERNAL_STOREAGE);
        }
    }

}
