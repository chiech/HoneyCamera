package com.xinlan.honeydew;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


//import com.cp.plugin.Plugin;
//import com.cp.plugin.event.SubEvent;
import com.bumptech.glide.Glide;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.utils.BitmapUtils;
import com.xinlan.imageeditlibrary.picchooser.SelectPictureActivity;
import com.youth.banner.loader.ImageLoader;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_PERMISSON_SORAGE = 1;
    public static final int REQUEST_PERMISSON_CAMERA = 2;

    public static final int SELECT_GALLERY_IMAGE_CODE = 7;
    public static final int TAKE_PHOTO_CODE = 8;
    public static final int ACTION_REQUEST_EDITIMAGE = 9;
    private MainActivity context;
    private ImageView imgView;
    private View OpenPhoto;
    private View editImage;//
    private Bitmap mainBitmap;
    private int imageWidth, imageHeight;//
    private String path;
    private BGABanner bgaBanner;
    private View mTakenPhoto;//拍摄照片用于编辑
    private View mFilter, btn_magic, btn_font;
    private Uri photoURI = null;
    private static final String TAG = "MainActivity";
    private int MODE = 0;
    //首页图片资源文件
    private Integer[] images= {R.mipmap.home_banner1,R.mipmap.home_banner2,R.mipmap.home_banner3,R.mipmap.home_banner4,
            R.mipmap.home_banner5,
            R.mipmap.home_banner6,
            R.mipmap.home_banner7,
            R.mipmap.home_banner8,
            R.mipmap.home_banner9,
            R.mipmap.home_banner10,
            R.mipmap.home_banner11,
            R.mipmap.home_banner12,
            R.mipmap.home_banner13};
    private List<Integer[]> list = new ArrayList<Integer[]>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        Plugin.init(this,this);
//        Plugin.buildNotificationAlert("Title", "The app requires authorization to read notification permissions", "Ok","Cancel");
        initPhotoError();
    }

    private void initView() {
        context = this;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = metrics.widthPixels;
        imageHeight = metrics.heightPixels;

        OpenPhoto = findViewById(R.id.select_ablum);
        OpenPhoto.setOnClickListener(this);
        mTakenPhoto = findViewById(R.id.take_photo);
        mTakenPhoto.setOnClickListener(this);

        list.add(images);

        bgaBanner = (BGABanner) findViewById(R.id.main_banner);

        BGALocalImageSize bgaLocalImageSize = new BGALocalImageSize(720, 1280, 320, 640);
        Random random = new Random();
        int i = random.nextInt(12);
        bgaBanner.setData(bgaLocalImageSize,null,images[i]);//随机在资源中选一页显示
        bgaBanner.setIsNeedShowIndicatorOnOnlyOnePage(false);//只有一页时不显示小圆点

    }

    private void initPhotoError() {
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                takePhotoClick();
                break;
            case R.id.select_ablum:
                selectFromAblum();
                break;
        }//end switch
    }


    /**
     * 拍摄照片
     */
    protected void takePhotoClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestTakePhotoPermissions();
        } else {
            TakePhoto();
        }//end if
    }

    /**
     * 请求拍照权限
     */
    private void requestTakePhotoPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSON_CAMERA);
            return;
        }
        TakePhoto();
    }

    /**
     * 拍摄照片
     */
    private void TakePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = FileUtils.genEditFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PHOTO_CODE);
            }

            //startActivityForResult(takePictureIntent, TAKE_PHOTO_CODE);
        }
    }

    /**
     * 编辑选择的图片
     *
     * @author panyi
     */
    private void editImageClick() {
        File outputFile = FileUtils.genEditFile();
        EditImageActivity.start(this, path, outputFile.getAbsolutePath(), ACTION_REQUEST_EDITIMAGE, MODE);
    }

    /**
     * 从相册选择编辑图片
     */
    private void selectFromAblum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            OpenPhotoWithPermissionsCheck();
        } else {
                    OpenPhoto();
        }//end if
    }

    private void OpenPhoto() {
                MainActivity.this.startActivityForResult(new Intent(
                                MainActivity.this, SelectPictureActivity.class),
                        SELECT_GALLERY_IMAGE_CODE);
    }

    private void OpenPhotoWithPermissionsCheck() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSON_SORAGE);
            return;
        }
        OpenPhoto();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSON_SORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            OpenPhoto();
            return;
        }//end if

        if (requestCode == REQUEST_PERMISSON_CAMERA
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            TakePhoto();
            return;
        }//end if
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // System.out.println("RESULT_OK");
            switch (requestCode) {
                case SELECT_GALLERY_IMAGE_CODE://
                    String filepath = data.getStringExtra("imgPath");
                    path = filepath;
                    editImageClick();
                    break;
                case TAKE_PHOTO_CODE://拍照返回
                    if (photoURI != null) {
                        path = photoURI.getPath();
                    }
                    editImageClick();
                    break;
            }// end switch
        }
        Log.d(TAG, "onActivityResult: requesetcode is " + requestCode);
    }


}//end class
