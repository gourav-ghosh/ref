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
import com.devstringx.mytylesstockcheck.databinding.ForgotPasswordScreenBinding;
import com.devstringx.mytylesstockcheck.datamodal.LoginRequestModal;
import com.devstringx.mytylesstockcheck.datamodal.LoginResponseModal;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity implements ResponseListner {
    ForgotPasswordScreenBinding forgotPasswordScreenBinding;
    private String mobileNumber="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgotPasswordScreenBinding= DataBindingUtil.setContentView(this,R.layout.forgot_password_screen);
        forgotPasswordScreenBinding.loginWithOtpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetVal();
                Common.intent(ForgotPasswordActivity.this, LoginWithOtpActivity.class);
            }
        });
        forgotPasswordScreenBinding.getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.hideKeyboard(ForgotPasswordActivity.this);
                mobileNumber=forgotPasswordScreenBinding.mobileEditText.getText().toString().trim();
                if(mobileNumber.equalsIgnoreCase("")){
                    Toast.makeText(ForgotPasswordActivity.this,"Incorrect mobile number! Please try again",Toast.LENGTH_SHORT).show();
                    return;
                }
                    getOtp(mobileNumber);
            }
        });
        forgotPasswordScreenBinding.mobileEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()<=9){
                    forgotPasswordScreenBinding.getOtpBtn.setEnabled(false);
                }else{
                    forgotPasswordScreenBinding.getOtpBtn.setEnabled(true);
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
                    Intent intent=new Intent(new Intent(this, BaseActivity.class));
                    intent.putExtra("forgot_password_mobile_number",mobileNumber);
                    startActivity(intent);
                }
            }
        }else{
            Common.showToast(this,responseDO.getResponse());
        }
    }
    private void resetVal(){
        forgotPasswordScreenBinding.mobileEditText.setText("");
    }
}