package com.devstringx.mytylesstockcheck.datamodal;

import com.google.gson.annotations.SerializedName;

public class LoginRequestModal {
    @SerializedName("password")
    private String password;

    @SerializedName("login_from")
    private String loginFrom="Mobile";
    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    @SerializedName("login_type")
    private String login_type;
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @SerializedName("phone_number")
    private String phone_number;
    public void setMobileNumer(String phone_number) {
        this.phone_number = phone_number;
    }
    public String getMobileNumer() {
        return phone_number;
    }

    public String getVerify_otp() {
        return verify_otp;
    }

    public void setVerify_otp(String verify_otp) {
        this.verify_otp = verify_otp;
    }

    @SerializedName("verify_otp")
    private String verify_otp;

    public String getVerification_type() {
        return verification_type;
    }

    public void setVerification_type(String verification_type) {
        this.verification_type = verification_type;
    }

    @SerializedName("verification_type")
    private String verification_type;

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    @SerializedName("new_password")
    private String new_password;

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    @SerializedName("confirm_password")
    private String confirm_password;

    @SerializedName("device_token")
    private String deviceToken;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
