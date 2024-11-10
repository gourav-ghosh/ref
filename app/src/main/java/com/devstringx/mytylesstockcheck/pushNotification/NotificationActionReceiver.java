package com.devstringx.mytylesstockcheck.pushNotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationActionReceiver extends BroadcastReceiver {

    String ACCEPT,REJECT;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(ACCEPT.equals(action)) {
            Log.v("Delivery","Accepted");
        }
        else if(REJECT.equals(action)) {
            Log.v("Delivery","Rejected");
        }

    }

}
