package com.devstringx.mytylesstockcheck.startup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.startup.fragment.CreateNewPasswordFragment;
import com.devstringx.mytylesstockcheck.startup.fragment.EnterOtpForLoginFragment;
import com.devstringx.mytylesstockcheck.startup.fragment.EnterOtpForgotPasswordFragment;

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        if (getIntent().getStringExtra("enter_otp_mobile_number")!=null) {
            loadFragment(EnterOtpForLoginFragment.newInstance(getIntent().getStringExtra("enter_otp_mobile_number")));
        }else if (getIntent().getStringExtra("forgot_password_mobile_number")!=null){
            loadFragment(EnterOtpForgotPasswordFragment.newInstance(getIntent().getStringExtra("forgot_password_mobile_number")));
        }else{
            loadFragment(CreateNewPasswordFragment.newInstance(getIntent().getStringExtra("create_new_password")));
        }

    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frag_container, fragment);
        fragmentTransaction.commit();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}