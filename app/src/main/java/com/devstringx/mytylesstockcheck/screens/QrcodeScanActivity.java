package com.devstringx.mytylesstockcheck.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.ActivityQrcodeScanBinding;

public class QrcodeScanActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private ActivityQrcodeScanBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        binding= DataBindingUtil.setContentView(this,R.layout.activity_qrcode_scan);
        binding.scanner.setOnBarcodeListener(result -> {
            // This listener is called from the Camera thread.
//            textView.post(() -> textView.setText(result.getText()));
            // Return true to keep scanning for barcodes.

            Intent intent=new Intent();
            intent.putExtra("product_data",result.getText());
            setResult(RESULT_OK,intent);
            finish();
            return true;
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        binding.scanner.openAsync();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.scanner.close();
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {
        if (requestCode == REQUEST_CAMERA &&
                grantResults.length > 0 &&
                grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.error_camera,
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permission = android.Manifest.permission.CAMERA;
            if (checkSelfPermission(permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{permission}, REQUEST_CAMERA);
            }
        }
    }
}