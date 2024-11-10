    package com.devstringx.mytylesstockcheck.startup;

    import android.annotation.SuppressLint;
    import android.app.Dialog;
    import android.graphics.drawable.Drawable;
    import android.os.Bundle;
    import android.text.Editable;
    import android.text.InputType;
    import android.text.TextWatcher;
    import android.view.MotionEvent;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.AppCompatButton;
    import androidx.databinding.DataBindingUtil;

    import com.devstringx.mytylesstockcheck.R;
    import com.devstringx.mytylesstockcheck.common.Common;
    import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
    import com.devstringx.mytylesstockcheck.databinding.ActivityLoginScreenBinding;
    import com.devstringx.mytylesstockcheck.datamodal.LoginRequestModal;
    import com.devstringx.mytylesstockcheck.datamodal.LoginResponseModal;
    import com.devstringx.mytylesstockcheck.pushNotification.MyFirebaseInstanceIDService;
    import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
    import com.devstringx.mytylesstockcheck.screens.vendor.VendorDashboardActivity;
    import com.devstringx.mytylesstockcheck.webservices.NKeys;
    import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
    import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
    import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
    import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
    import com.google.gson.Gson;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.HashMap;

    public class LoginWithPasswordActivity extends AppCompatActivity implements ResponseListner{
        private boolean passwordVisible = false;
        private PreferenceUtils utils;
        ActivityLoginScreenBinding loginScreenBinding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            loginScreenBinding= DataBindingUtil.setContentView(LoginWithPasswordActivity.this,R.layout.activity_login_screen);
            View view=loginScreenBinding.getRoot();
            utils=new PreferenceUtils(this);
            loginScreenBinding.loginWithOtpTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetVal();
                    Common.intent(LoginWithPasswordActivity.this, LoginWithOtpActivity.class);
                }
            });

            loginScreenBinding.passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_eye_crossed,0);
            // Set onTouchListener to toggle password visibility
            loginScreenBinding.passwordEditText.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Check if touch is on the drawableRight icon
                    Drawable drawableRight = loginScreenBinding.passwordEditText.getCompoundDrawables()[2];
                    if (drawableRight != null && event.getRawX() >= (loginScreenBinding.passwordEditText.getRight() - drawableRight.getBounds().width())) {
                        togglePasswordVisibility();
                        return true; // Consume the touch event
                    }
                }
                return false;
            });
            loginScreenBinding.forgotPwdTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetVal();
                    Common.intent(LoginWithPasswordActivity.this, ForgotPasswordActivity.class);
                }
            });
            loginScreenBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.hideKeyboard(LoginWithPasswordActivity.this);
                    String mobileNumber = loginScreenBinding.mobileEditText.getText().toString().trim();
                    String password = loginScreenBinding.passwordEditText.getText().toString().trim();
                    if(mobileNumber.equalsIgnoreCase("")||password.equalsIgnoreCase("")){
                        Toast.makeText(LoginWithPasswordActivity.this,getString(R.string.login_pass_error),Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!Common.pwdVsalid(password)){
                        Toast.makeText(LoginWithPasswordActivity.this,getString(R.string.login_pass_error),Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!(mobileNumber.length()==10)){
                        Toast.makeText(LoginWithPasswordActivity.this, getString(R.string.login_pass_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    loginWithMobileNumber(mobileNumber,password,"password");

                }
            });

            verifyBtn();
        }

        private void verifyBtn() {
            loginScreenBinding.mobileEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().length()<=9){
                        loginScreenBinding.loginBtn.setEnabled(false);
                        return;
                    }
                    if(loginScreenBinding.passwordEditText.getText().toString().length()>0){
                        loginScreenBinding.loginBtn.setEnabled(true);
                    }else{
                        loginScreenBinding.loginBtn.setEnabled(false);
                    }
                }
            });
            loginScreenBinding.passwordEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().length()==0){
                        loginScreenBinding.loginBtn.setEnabled(false);
                        return;
                    }
                    if(loginScreenBinding.mobileEditText.getText().toString().length()==10){
                        loginScreenBinding.loginBtn.setEnabled(true);
                    }else{
                        loginScreenBinding.loginBtn.setEnabled(false);
                    }
                }
            });
        }

        private void togglePasswordVisibility() {
            if (passwordVisible) {
                // Password is currently visible, hide it
                loginScreenBinding.passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                loginScreenBinding.passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_eye_crossed,0);
                passwordVisible = false;
            } else {
                // Password is currently hidden, show it
                loginScreenBinding.passwordEditText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                loginScreenBinding.passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_eye,0);
                passwordVisible = true;
            }

            // Move cursor to the end of the text
            loginScreenBinding.passwordEditText.setSelection(loginScreenBinding.passwordEditText.getText().length());
        }

        public void loginWithMobileNumber(String email, String password,String login_type){
            Common.trackEvent(LoginWithPasswordActivity.this,"device-token",MyFirebaseInstanceIDService.getDeviceRefreshToken());

            LoginRequestModal loginRequestModel=new LoginRequestModal();
            loginRequestModel.setMobileNumer(email);
            loginRequestModel.setPassword(password);
            loginRequestModel.setLogin_type(login_type);
            loginRequestModel.setDeviceToken(MyFirebaseInstanceIDService.getDeviceRefreshToken());
            HashMap<String,Object> map=new HashMap<>();
            map.put(NKeys.LOGINDATA,new Gson().toJson(loginRequestModel));
            new NetworkRequest(this,this).callWebServices(ServiceMethods.WS_LOGINWITHMOBILEPASS,map,true);
        }


        @SuppressLint("SuspiciousIndentation")
        @Override
        public void onResponseReceived(ResponseDO responseDO) {
            Common.showLog("RESPONSE==="+responseDO.getResponse());
            if (!responseDO.isError()) {
                if (responseDO.getServiceMethods() == ServiceMethods.WS_LOGINWITHMOBILEPASS) {
                    LoginResponseModal loginResponse = new Gson().fromJson(responseDO.getResponse(), LoginResponseModal.class);
                    Common.showToast(this, loginResponse.getMessage());
                    if (loginResponse.getStatus() == 200) {
                            utils.saveString(PreferenceUtils.TOKEN, loginResponse.getData().getToken());
                            Common.showLog("TOKEN===" + loginResponse.getData().getToken());
                            utils.saveString(PreferenceUtils.REFRESHTOKEN, loginResponse.getData().getRefershToken());
                            utils.decodeAndSaveToken(loginResponse.getData().getToken());
                            Common.userRoleId = new PreferenceUtils(this).getStringFromPreference(PreferenceUtils.ROLEID, "").toString();
                        if (utils.getStringFromPreference(PreferenceUtils.USERSTATUS,"").equalsIgnoreCase("Added")) showWelcomeDialog();
                        else {
                            Common.intent(LoginWithPasswordActivity.this, DashboardActivity.class);
                            finish();
                        }
                    } else {
                        loginScreenBinding.credentialErrorTxt.setText(loginResponse.getMessage());
                        loginScreenBinding.credentialErrorTxt.setVisibility(View.VISIBLE);
                    }
                } else if (responseDO.getServiceMethods() == ServiceMethods.WS_TEMPORARYPASSWORDCHANGE) {
                   if (responseDO.getCode()==200){
                       LoginResponseModal loginResponse = new Gson().fromJson(responseDO.getResponse(), LoginResponseModal.class);
                       utils.saveString(PreferenceUtils.TOKEN, loginResponse.getData().getToken());
                       Common.showLog("TOKEN===" + loginResponse.getData().getToken());
                       utils.saveString(PreferenceUtils.REFRESHTOKEN, loginResponse.getData().getRefershToken());
                       utils.decodeAndSaveToken(loginResponse.getData().getToken());
                       Common.intent(LoginWithPasswordActivity.this, DashboardActivity.class);
                       finish();
                   }
                   else Common.showToast(this,responseDO.getResponse().toString());
                } else {
                    Common.showToast(this, responseDO.getResponse());
                }
            } else {
                Common.showToast(this, responseDO.getResponse());
            }
        }

        private void showWelcomeDialog() {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.welcome_change_password_dialog);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            dialog.getWindow().setLayout(layoutParams.width, layoutParams.height);
            dialog.setCancelable(false);
            TextView newPassword=dialog.findViewById(R.id.welcome_newPasswordET);
            TextView confirm_new_password=dialog.findViewById(R.id.welcome_confirmNewPasswordET);
            AppCompatButton save=dialog.findViewById(R.id.welcome_save_pwd);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.hideKeyboard(LoginWithPasswordActivity.this);
                    String new_password=newPassword.getText().toString();
                    String confirm_password=confirm_new_password.getText().toString();
                    if (new_password.isEmpty()) {
                        Common.showToast(LoginWithPasswordActivity.this, "Password cannot be blank !");
                        return;
                    } else if (!Common.pwdVsalid(new_password)) {
                        Common.showToast(LoginWithPasswordActivity.this, "Your password must include 8 characters,at least 1 lower case, 1 upper case, 1 special character, 1 number and no spaces.");
                        return;
                    }else if (confirm_password.isEmpty()) {
                        Common.showToast(LoginWithPasswordActivity.this, "Password cannot be blank !");
                        return;
                    } else if (!(confirm_password.equals(new_password))) {
                        Common.showToast(LoginWithPasswordActivity.this, "Passwords must match.");
                        return;
                    }
                    callChangePasswordApi(new_password,confirm_password);
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
            new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_TEMPORARYPASSWORDCHANGE, map, true);
        }
        private void resetVal(){
            loginScreenBinding.mobileEditText.setText("");
            loginScreenBinding.passwordEditText.setText("");
        }
    }