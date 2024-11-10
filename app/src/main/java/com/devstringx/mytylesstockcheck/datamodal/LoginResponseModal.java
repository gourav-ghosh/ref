package com.devstringx.mytylesstockcheck.datamodal;

import com.google.gson.annotations.SerializedName;

public class LoginResponseModal {
    @SerializedName("status")
    private int status ;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    private String message ;
    @SerializedName("data")
    private Data data;

}
