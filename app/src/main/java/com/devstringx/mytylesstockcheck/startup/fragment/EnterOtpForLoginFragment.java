package com.devstringx.mytylesstockcheck.startup.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentEnterOtpForLoginBinding;
import com.devstringx.mytylesstockcheck.datamodal.LoginRequestModal;
import com.devstringx.mytylesstockcheck.datamodal.LoginResponseModal;
import com.devstringx.mytylesstockcheck.pushNotification.MyFirebaseInstanceIDService;
import com.devstringx.mytylesstockcheck.pushNotification.MyFirebaseMessagingService;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.screens.vendor.VendorDashboardActivity;
import com.devstringx.mytylesstockcheck.startup.BaseActivity;
import com.devstringx.mytylesstockcheck.startup.LoginWithOtpActivity;
import com.devstringx.mytylesstockcheck.startup.LoginWithPasswordActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EnterOtpForLoginFragment extends Fragment implements ResponseListner {
    FragmentEnterOtpForLoginBinding enterOtpForLoginBinding;
    private static String mobileNumber = "";
    private static EnterOtpForLoginFragment fragment;
    private String otp = "";
    private PreferenceUtils utils;

    public static Fragment newInstance(String mobileNumber) {
        if (fragment == null) fragment = new EnterOtpForLoginFragment();
        EnterOtpForLoginFragment.mobileNumber = mobileNumber;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        enterOtpForLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_enter_otp_for_login, container, false);
        View view = enterOtpForLoginBinding.getRoot();
        Common.startTimer(enterOtpForLoginBinding.timeTxt, enterOtpForLoginBinding.resendOtp);
        utils = new PreferenceUtils(getActivity());
        enterOtpForLoginBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).onBackPressed();
            }
        });
        enterOtpForLoginBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.hideKeyboard(getActivity());
                String temp = "";
                for (int i = 0; i < enterOtpForLoginBinding.pinview.length(); i++) {
                    temp += enterOtpForLoginBinding.pinview.getText().charAt(i);
                }
                otp = temp;
                if(otp.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(),"Invalid OTP ! Please enter correct OTP",Toast.LENGTH_SHORT).show();
                    return;
                }
                loginWithOtp(mobileNumber, otp, "login");
            }
        });
        enterOtpForLoginBinding.resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Common.startTimer(enterOtpForLoginBinding.timeTxt, enterOtpForLoginBinding.resendOtp);
                enterOtpForLoginBinding.resendOtp.setEnabled(false);

            }
        });
        enterOtpForLoginBinding.loginWithMobPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.intent(getActivity(), LoginWithPasswordActivity.class);
            }
        });
        enterOtpForLoginBinding.pinview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==6){
                    enterOtpForLoginBinding.loginBtn.setEnabled(true);
                }else{
                    enterOtpForLoginBinding.loginBtn.setEnabled(false);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        enterOtpForLoginBinding.pinview.setText("");
    }

    private void loginWithOtp(String mobileNumber, String otp, String login_type) {
        LoginRequestModal loginRequestModel = new LoginRequestModal();
        loginRequestModel.setMobileNumer(mobileNumber);
        loginRequestModel.setVerify_otp(otp);
        loginRequestModel.setVerification_type(login_type);
        loginRequestModel.setDeviceToken(MyFirebaseInstanceIDService.getDeviceRefreshToken());
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
                    Toast.makeText(getActivity(), loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    utils.saveString(PreferenceUtils.TOKEN, loginResponse.getData().getToken());
                    Common.showLog("TOKEN===" + loginResponse.getData().getToken());
                    utils.saveString(PreferenceUtils.REFRESHTOKEN, loginResponse.getData().getRefershToken());
                    utils.decodeAndSaveToken(loginResponse.getData().getToken());
                    if (utils.getStringFromPreference(PreferenceUtils.USERSTATUS,"").equalsIgnoreCase("Added")) showWelcomeDialog();
                    else {
                        Common.intent(getActivity(), DashboardActivity.class);
                        getActivity().finish();
                    }
                    Common.showLog("RoleId====" + PreferenceUtils.ROLEID);
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_TEMPORARYPASSWORDCHANGE) {
                if (responseDO.getCode()==200){
                    LoginResponseModal loginResponse = new Gson().fromJson(responseDO.getResponse(), LoginResponseModal.class);
                    utils.saveString(PreferenceUtils.TOKEN, loginResponse.getData().getToken());
                    Common.showLog("TOKEN===" + loginResponse.getData().getToken());
                    utils.saveString(PreferenceUtils.REFRESHTOKEN, loginResponse.getData().getRefershToken());
                    utils.decodeAndSaveToken(loginResponse.getData().getToken());
                    Common.intent(getActivity(), DashboardActivity.class);
                    getActivity().finish();
                }
                else Common.showToast(getActivity(),responseDO.getResponse().toString());
            } else {
                Common.showToast(getActivity(), responseDO.getResponse());
            }
        } else {
            Common.showToast(getActivity(), responseDO.getResponse());
        }
    }
    private void showWelcomeDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.welcome_change_password_dialog);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        dialog.getWindow().setLayout(layoutParams.width, layoutParams.height);
        dialog.setCancelable(false);
        TextView new_password=dialog.findViewById(R.id.welcome_newPasswordET);
        TextView confirm_new_password=dialog.findViewById(R.id.welcome_confirmNewPasswordET);
        AppCompatButton save=dialog.findViewById(R.id.welcome_save_pwd);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPwd=new_password.getText().toString();
                String cnfPwd=confirm_new_password.getText().toString();
                if (newPwd.isEmpty()){
                    Toast.makeText(getContext(), "new password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }else if (cnfPwd.isEmpty()){
                    Toast.makeText(getContext(), "confirm new password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!Common.pwdVsalid(newPwd)) {
                    Toast.makeText(getContext(), "password length must be 8 or more and having 1 upper case, 1 lower case, 1 numeric and 1 special character.", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!cnfPwd.equals(newPwd)){
                    Toast.makeText(getContext(), "new password & confirm new password aren't matching", Toast.LENGTH_SHORT).show();
                    return;
                }
                callChangePasswordApi(newPwd,cnfPwd);
            }
        });
//            getModuleByUser();
        dialog.show();
    }

    private void callChangePasswordApi(String newPwd, String cnfPwd) {
        HashMap<String,Object> data=new HashMap<>();
        data.put("new_password",newPwd);
        data.put("confirm_password",cnfPwd);
        HashMap<String,Object> map=new HashMap<>();
        map.put(NKeys.TEMPORARYPASSWORDCHANGE,new Gson().toJson(data));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_TEMPORARYPASSWORDCHANGE, map, true);
    }
}