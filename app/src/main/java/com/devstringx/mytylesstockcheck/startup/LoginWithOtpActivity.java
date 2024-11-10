package com.devstringx.mytylesstockcheck.startup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.LoginWithOtpScreenBinding;
import com.devstringx.mytylesstockcheck.datamodal.LoginRequestModal;
import com.devstringx.mytylesstockcheck.datamodal.LoginResponseModal;
import com.devstringx.mytylesstockcheck.pushNotification.MyFirebaseInstanceIDService;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import java.util.HashMap;

public class LoginWithOtpActivity extends AppCompatActivity implements ResponseListner {
    private String mobileNumber="";
    private String otp="";
    private PreferenceUtils utils;
    LoginWithOtpScreenBinding loginWithOtpScreenBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginWithOtpScreenBinding= DataBindingUtil.setContentView(this,R.layout.login_with_otp_screen);
        utils=new PreferenceUtils(this);
        loginWithOtpScreenBinding.alreadyHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetVal();
                Common.intent(LoginWithOtpActivity.this, LoginWithPasswordActivity.class);
            }
        });
        loginWithOtpScreenBinding.getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.hideKeyboard(LoginWithOtpActivity.this);
                mobileNumber = loginWithOtpScreenBinding.mobileEditText.getText().toString().trim();
                if(mobileNumber.equalsIgnoreCase("")){
                    Common.showToast(LoginWithOtpActivity.this,"Incorrect mobile number! Please try again");
                    return;
                }
                getOtp(mobileNumber);
            }
        });
        loginWithOtpScreenBinding.mobileEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()<=9){
                    loginWithOtpScreenBinding.getOtpBtn.setEnabled(false);
                }else{
                    loginWithOtpScreenBinding.getOtpBtn.setEnabled(true);
                }

            }
        });
    }
    private void getOtp(String mobileNumber) {
        LoginRequestModal loginRequestModel=new LoginRequestModal();
        loginRequestModel.setMobileNumer(mobileNumber);
        HashMap<String,Object> map=new HashMap<>();
        map.put(NKeys.GETOTP,new Gson().toJson(loginRequestModel));
        new NetworkRequest(this,this).callWebServices(ServiceMethods.WS_GETOTP,map,true);
    }
    @Override
    public void onResponseReceived(ResponseDO responseDO) {

        Common.showLog("RESPONSE==="+responseDO.getResponse());
        if (!responseDO.isError()) {
            if(responseDO.getServiceMethods()==ServiceMethods.WS_GETOTP){
                LoginResponseModal loginResponse = new Gson().fromJson(responseDO.getResponse(), LoginResponseModal.class);
                Common.showToast(this,loginResponse.getMessage());
                if (loginResponse.getStatus() == 200){
                    Common.userRoleId = new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.ROLEID, "").toString();
                    Intent intent=new Intent(new Intent(this, BaseActivity.class));
                    intent.putExtra("enter_otp_mobile_number",mobileNumber);
                    startActivity(intent);
                }
            }
        }else{
            Common.showToast(this,responseDO.getResponse());
        }
    }
    private void resetVal(){
        loginWithOtpScreenBinding.mobileEditText.setText("");
    }
}