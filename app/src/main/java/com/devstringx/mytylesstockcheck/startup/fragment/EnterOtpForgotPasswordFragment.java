package com.devstringx.mytylesstockcheck.startup.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentEnterOtpForgotPasswordBinding;
import com.devstringx.mytylesstockcheck.datamodal.LoginRequestModal;
import com.devstringx.mytylesstockcheck.datamodal.LoginResponseModal;
import com.devstringx.mytylesstockcheck.startup.BaseActivity;
import com.devstringx.mytylesstockcheck.startup.LoginWithPasswordActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import java.util.HashMap;

public class EnterOtpForgotPasswordFragment extends Fragment implements ResponseListner {
    FragmentEnterOtpForgotPasswordBinding enterOtpForgotPasswordBinding;
    private static String mobileNumber = "";
    private static EnterOtpForgotPasswordFragment fragment;
    private String otp = "";
    public static Fragment newInstance(String mobileNumber) {
        if (fragment == null) fragment = new EnterOtpForgotPasswordFragment();
        EnterOtpForgotPasswordFragment.mobileNumber = mobileNumber;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        enterOtpForgotPasswordBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_enter_otp_forgot_password, container, false);
        View view = enterOtpForgotPasswordBinding.getRoot();
        Common.startTimer(enterOtpForgotPasswordBinding.timeTxt,enterOtpForgotPasswordBinding.resendOtp);
        enterOtpForgotPasswordBinding.alreadyHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.intent(getActivity(), LoginWithPasswordActivity.class);
            }
        });
        enterOtpForgotPasswordBinding.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.hideKeyboard(getActivity());
                String temp="";
                for (int i = 0; i < enterOtpForgotPasswordBinding.pinview.length(); i++) {
                    temp += enterOtpForgotPasswordBinding.pinview.getText().charAt(i);
                }
                otp=temp;
                if(otp.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(),"Invalid OTP ! Please enter correct OTP",Toast.LENGTH_SHORT).show();
                    return;
                }
                verify(mobileNumber, otp, "verify");
            }
        });
        enterOtpForgotPasswordBinding.resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Common.startTimer(enterOtpForgotPasswordBinding.timeTxt,enterOtpForgotPasswordBinding.resendOtp);
                enterOtpForgotPasswordBinding.resendOtp.setEnabled(false);

            }
        });
        enterOtpForgotPasswordBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).onBackPressed();
            }
        });
        enterOtpForgotPasswordBinding.pinview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==6){
                    enterOtpForgotPasswordBinding.verifyBtn.setEnabled(true);
                }else{
                    enterOtpForgotPasswordBinding.verifyBtn.setEnabled(false);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        enterOtpForgotPasswordBinding.pinview.setText("");
    }

    private void verify(String mobileNumber, String otp, String login_type) {
        LoginRequestModal loginRequestModel = new LoginRequestModal();
        loginRequestModel.setMobileNumer(mobileNumber);
        loginRequestModel.setVerify_otp(otp);
        loginRequestModel.setVerification_type(login_type);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.OTPVERIFICATION, new Gson().toJson(loginRequestModel));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_OTPVERIFICATION, map, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {

        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_OTPVERIFICATION) {
                LoginResponseModal loginResponse = new Gson().fromJson(responseDO.getResponse(), LoginResponseModal.class);
                Common.showToast(getActivity(), loginResponse.getMessage());
                if (loginResponse.getStatus() == 200) {
                    Intent intent=new Intent(new Intent(getActivity(),BaseActivity.class));
                    intent.putExtra("create_new_password",mobileNumber);
                    startActivity(intent);
                    enterOtpForgotPasswordBinding.pinview.getText().clear();
                    ((BaseActivity)(getActivity())).finish();

                }else{
                    enterOtpForgotPasswordBinding.invalidOtpTxt.setText(loginResponse.getMessage());
                    enterOtpForgotPasswordBinding.invalidOtpTxt.setVisibility(View.VISIBLE);
                    enterOtpForgotPasswordBinding.mobPwdTxt.setVisibility(View.GONE);
                }
            }
        } else {
            Common.showToast(getActivity(), responseDO.getResponse());
        }
    }

}