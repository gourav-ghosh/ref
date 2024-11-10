package com.devstringx.mytylesstockcheck.startup.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentCreateNewPasswordBinding;
import com.devstringx.mytylesstockcheck.datamodal.LoginRequestModal;
import com.devstringx.mytylesstockcheck.datamodal.LoginResponseModal;
import com.devstringx.mytylesstockcheck.startup.BaseActivity;
import com.devstringx.mytylesstockcheck.startup.LoginWithOtpActivity;
import com.devstringx.mytylesstockcheck.startup.LoginWithPasswordActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import java.util.HashMap;

public class CreateNewPasswordFragment extends Fragment implements ResponseListner {

    FragmentCreateNewPasswordBinding createNewPasswordBinding;
    private static String mobileNumber = "";
    private static CreateNewPasswordFragment fragment;
    public static Fragment newInstance(String mobileNumber) {
        if (fragment == null) fragment = new CreateNewPasswordFragment();
        CreateNewPasswordFragment.mobileNumber = mobileNumber;
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
        createNewPasswordBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_new_password, container, false);
        View view = createNewPasswordBinding.getRoot();
        createNewPasswordBinding.loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.intent(getActivity(), LoginWithPasswordActivity.class);
            }
        });
       
        createNewPasswordBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.hideKeyboard(getActivity());
                String new_password = createNewPasswordBinding.newPwd.getText().toString().trim();
                String confirm_password = createNewPasswordBinding.confirmPwd.getText().toString().trim();
                if (new_password.isEmpty()) {
                    Common.showToast(getContext(), "Password cannot be blank !");
                    return;
                } else if (!Common.pwdVsalid(new_password)) {
                    Common.showToast(getContext(), "Your password must include 8 characters,at least 1 lower case, 1 upper case, 1 special character, 1 number and no spaces.");
                    return;
                }else if (confirm_password.isEmpty()) {
                    Common.showToast(getContext(), "Password cannot be blank !");
                    return;
                } else if (!(confirm_password.equals(new_password))) {
                    Common.showToast(getContext(), "Passwords must match.");
                    return;
                }
                
                resetPassword(mobileNumber,new_password,confirm_password);
            }
        });
        createNewPasswordBinding.newPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                verifybtn();
            }
        });
        createNewPasswordBinding.confirmPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                verifybtn();
            }
        });
        return view;
    }

    private void verifybtn() {
        String new_password = createNewPasswordBinding.newPwd.getText().toString().trim();
        String confirm_password = createNewPasswordBinding.confirmPwd.getText().toString().trim();
        if (new_password.isEmpty()) {
            createNewPasswordBinding.saveBtn.setEnabled(false);
        } else if (!Common.pwdVsalid(new_password)) {
            createNewPasswordBinding.saveBtn.setEnabled(false);
        }else if (confirm_password.isEmpty()) {
            createNewPasswordBinding.saveBtn.setEnabled(false);
        } else if (!(confirm_password.equals(new_password))) {
            createNewPasswordBinding.saveBtn.setEnabled(false);
        }else{
            createNewPasswordBinding.saveBtn.setEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        createNewPasswordBinding.newPwd.setText("");
        createNewPasswordBinding.confirmPwd.setText("");
    }

    private void resetPassword(String mobileNumber, String new_password, String confirm_password) {
        LoginRequestModal loginRequestModel = new LoginRequestModal();
        loginRequestModel.setMobileNumer(mobileNumber);
        loginRequestModel.setNew_password(new_password);
        loginRequestModel.setConfirm_password(confirm_password);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.RESETPASSWORD, new Gson().toJson(loginRequestModel));
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_RESETPASSWORD, map, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {

        Common.showLog("RESPONSE==="+responseDO.getResponse());
        if (!responseDO.isError()) {
            if(responseDO.getServiceMethods()==ServiceMethods.WS_RESETPASSWORD){
                LoginResponseModal loginResponse = new Gson().fromJson(responseDO.getResponse(), LoginResponseModal.class);
                Common.showToast(getActivity(),loginResponse.getMessage());
                if (loginResponse.getStatus() == 200){
                Common.intent(getActivity(),LoginWithPasswordActivity.class);
                    ((BaseActivity)(getActivity())).finish();
                }
            }
        }else{
            Common.showToast(getActivity(),responseDO.getResponse());
        }
    }
}