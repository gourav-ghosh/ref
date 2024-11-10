package com.devstringx.mytylesstockcheck.datamodal;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("token")
    String token;
    @SerializedName("refershToken")
    String refershToken;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefershToken() {
        return refershToken;
    }

    public void setRefershToken(String refershToken) {
        this.refershToken = refershToken;
    }
}
