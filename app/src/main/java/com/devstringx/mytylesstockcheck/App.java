package com.devstringx.mytylesstockcheck;


import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;

import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.MyBroadcastReceiver;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.pushNotification.MyFirebaseMessagingService;
import com.devstringx.mytylesstockcheck.screens.DashboardActivity;
import com.devstringx.mytylesstockcheck.socketManage.SocketManager;
import com.devstringx.mytylesstockcheck.startup.LoginWithPasswordActivity;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class App extends Application {

    public static final String CHANNEL_ID = "StockCheck";
    public static final String CHANNEL_NAME = "StockCheck Notifications";
    public static final String CHANNEL_DESCRIPTION = "This is the default notification channel for the StockCheck";
    private static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
    public static final int NOTIFICATION_ID = 9999;

    private static App singleton = null;
    private String SECRETKEY="";
    private MyBroadcastReceiver myReceiver = new MyBroadcastReceiver();

    public static App getInstance() {

        if(singleton == null)
        {
            SocketManager.getInstance();
            SocketManager.getInstance().connect();
            singleton = new App();
            new MyFirebaseMessagingService();
        }
        return singleton;
    }
public static Context mContext;
    public String getSECRETKEY() {
        return SECRETKEY;
    }
    public void setSECRETKEY(String SECRETKEY) {
        this.SECRETKEY = SECRETKEY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext=this;
        getInstance();
//        createNotificationChannel();
        Common.receivedSocketMessage(mContext);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SocketManager.getInstance().disconnect();
        unregisterReceiver(myReceiver);
    }

//    private void createNotificationChannel(){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            //Create a notification channel
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
//                    CHANNEL_IMPORTANCE);
//            channel.setDescription(CHANNEL_DESCRIPTION);
//
//            //Register channel with the System.
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//    }
}
