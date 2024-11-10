package com.devstringx.mytylesstockcheck.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.pushNotification.MyFirebaseInstanceIDService;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.screens.vendor.InquiryDetailActivity;
import com.devstringx.mytylesstockcheck.screens.vendor.VendorDashboardActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private static final long SPLASH_TIME_OUT = 3000;
    private PreferenceUtils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        utils=new PreferenceUtils(this);
        Common.trackEvent(SplashScreenActivity.this,"open_app","App Open");
        new MyFirebaseInstanceIDService().onTokenRefresh();
//        if (!new PreferenceUtils(SplashScreenActivity.this).getStringFromPreference(PreferenceUtils.TOKEN,"").isEmpty()){
//            Bundle bundle=getIntent().getExtras();
//            if(getIntent().getData()!=null){
////               Intent intent= new Intent(this, InquiryDetailActivity.class);
////               intent.putExtra("inquiry_id","");
////               startActivity(intent);
////               finish();
////               return;
//           }
//        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (new PreferenceUtils(SplashScreenActivity.this).getStringFromPreference(PreferenceUtils.TOKEN,"").isEmpty()){
                    Common.intent(SplashScreenActivity.this, LoginWithPasswordActivity.class);
                } else if (new PreferenceUtils(SplashScreenActivity.this).getStringFromPreference(PreferenceUtils.USERSTATUS,"").equalsIgnoreCase("Added")){
                    Common.intent(SplashScreenActivity.this, LoginWithPasswordActivity.class);
                } else{
                    Common.intent(SplashScreenActivity.this, DashboardActivity.class);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}