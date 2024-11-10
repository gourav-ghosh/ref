package com.devstringx.mytylesstockcheck.common;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.devstringx.mytylesstockcheck.pushNotification.MyFirebaseInstanceIDService;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static MyBroadcastReceiver receiver;
    public static MyBroadcastReceiver getInstance(){
        if(receiver==null)receiver=new MyBroadcastReceiver();
        return receiver;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String response=intent.getStringExtra("data");
            Common.showLog("===MyBroadcastReceiver===" + response);
            JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("userId");
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.getString(i).equalsIgnoreCase(new PreferenceUtils(context).getStringFromPreference(PreferenceUtils.USERID, ""))) {
                        if (jsonObject.getBoolean("is_logout")) {
                            Common.showStatusDialog(context, jsonObject.getString("message"), jsonObject.getBoolean("is_logout"));
                        } else {
                            Common.showStatusDialog(context, jsonObject.getString("message"), jsonObject.getBoolean("is_logout"));
                        }
                        break;
                    }
                }

        } catch (JSONException e) {
            Common.showLog("Socket Error===" + e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
