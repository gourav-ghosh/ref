package com.devstringx.mytylesstockcheck.screens;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityImageCropBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCropActivity extends AppCompatActivity {

    ActivityImageCropBinding activityImageCropBinding;
    private Context mContext;
    private Common common;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);

        mContext = this;
        common = new Common();
        Button save = findViewById(R.id.save);
        ImageView imageViewback = findViewById(R.id.back);
        CropImageView cropImageView = findViewById(R.id.cropImageView);

        Uri uri = Uri.fromFile(new File(getIntent().getStringExtra("FILEPATH")));

        cropImageView.setCropShape(CropImageView.CropShape.OVAL);
        cropImageView.setAspectRatio(1,1);
        cropImageView.setAutoZoomEnabled(false);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setMultiTouchEnabled(false);
        cropImageView.setGuidelines(CropImageView.Guidelines.OFF);
        cropImageView.setFitsSystemWindows(true);
        cropImageView.setScaleType(CropImageView.ScaleType.FIT_CENTER);
        cropImageView.setCornerShape(CropImageView.CropCornerShape.OVAL);
        cropImageView.setImageUriAsync(uri);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = cropImageView.getCroppedImage();
                Intent intent = new Intent().setData(common.getImageUri(mContext,bitmap));
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}