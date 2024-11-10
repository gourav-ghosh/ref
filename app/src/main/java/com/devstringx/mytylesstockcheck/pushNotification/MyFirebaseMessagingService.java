package com.devstringx.mytylesstockcheck.pushNotification;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.devstringx.mytylesstockcheck.App;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.screens.NotificationListActivity;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;
import com.devstringx.mytylesstockcheck.screens.QuoteDetailActivity;
import com.devstringx.mytylesstockcheck.screens.vendor.InquiryDetailActivity;
import com.devstringx.mytylesstockcheck.screens.vendor.SingleNotificationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.List;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String title="",body="",id="";
    private PreferenceUtils utils;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        utils=new PreferenceUtils(this);
        Common.showLog("NOTIFICATIONDATA"+new Gson().toJson(message));
        if (message.getNotification() != null) {
            title = (String) message.getNotification().getTitle();
            body= String.valueOf(message.getNotification().getBody());
            id= String.valueOf(message.getData().get("id"));
        }else if (message.getData() != null) {
            title = (String) message.getData().get("title");
            body= String.valueOf(message.getData().get("body"));
            id= String.valueOf(message.getData().get("id"));
            Log.d("title", "body: " + title + " body: " + body);
        }
        Common.trackEvent(this,"notification",title);
        if (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("2")||
                utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("3")||
                utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("4")) {
            if(id==null||id.equalsIgnoreCase("undefined")||id.equalsIgnoreCase("")||id.equalsIgnoreCase("null"))return;
            if (!isAppIsInBackground(MyFirebaseMessagingService.this)) {
                Intent newintent = new Intent(getApplicationContext(), SingleNotificationActivity.class);
                newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                newintent.putExtra("background", false);
                newintent.putExtra("title", title);
                newintent.putExtra("body", body);
                newintent.putExtra("id", id);
                App.mContext.startActivity(newintent);
            } else {
                sendNotification(title, body, id);
            }
        }else if ((utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("6")) ||
                (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("8")) ||
                        (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("9")) ||
                                (utils.getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("10"))){
            String id="",order_id="";
            if(message.getData().get("quotation_id")!=null){
                id=message.getData().get("quotation_id");
            }
            if(message.getData().get("order_id")!=null){
                order_id=message.getData().get("order_id");
            }
            showNotification2(title, body,id,order_id);
        }
    }

    private void sendNotification(String title, String messageBody, String id) {
        Intent intent = new Intent(getApplicationContext(), InquiryDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("inquiry_id", id);
        intent.putExtra("background", true);
        intent.putExtra("title", title);
        intent.putExtra("body", messageBody);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+getApplicationContext().getPackageName()+"/"+R.raw.vendor);

        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                App.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(sound)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
//                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                    .setUsage(AudioAttributes. USAGE_ALARM )
                    .build() ;
            NotificationChannel notificationChannel= new NotificationChannel(App.CHANNEL_ID, App.CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(App.CHANNEL_DESCRIPTION);
            notificationChannel.setSound(sound,audioAttributes);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Random rand = new Random();
        notificationManager.notify((rand.nextInt()+rand.nextInt()), builder.build());

    }
    public static boolean isAppIsInBackground(Context context) {
//        Log.e(TAG, "isAppIsInBackground: "+"show" );
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                        processInfo.processName.equals(context.getPackageName())) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                            break;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Method to display the notifications
    public void showNotification(String title, String messageBody) {
        String id="";
        Intent intent;
        if(messageBody.contains(":")){
            id=messageBody.split(":")[1];
            intent= new Intent(this, QuoteDetailActivity.class);
            intent.putExtra("id", Integer.parseInt(id.trim()));
        }else{
            intent= new Intent(this, NotificationListActivity.class);
        }
        Common.showLog("QUOTATIONID===="+id);

        intent.putExtra("from", "notification");
        String channel_id = "notification_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);

           Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.vendor);

       // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags

        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(sound)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
//                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

            builder = builder.setContentTitle(title)
                    .setContentText(messageBody)
                    .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                    .setUsage(AudioAttributes. USAGE_ALARM )
                    .build() ;
            NotificationChannel notificationChannel= new NotificationChannel(channel_id, "stock-check",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(messageBody);
            Common.showLog(sound.toString());
            notificationChannel.setSound(sound,audioAttributes);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Random rand = new Random();
        notificationManager.notify(rand.nextInt(), builder.build());
    }
    public void showNotification2(String title, String messageBody, String id, String order_id) {
        Intent intent;
        if(!id.isEmpty()){
            intent= new Intent(this, QuoteDetailActivity.class);
            intent.putExtra("id", Integer.parseInt(id.trim()));
        } else if (!order_id.isEmpty()) {
            intent= new Intent(this, OrderDetailActivity.class);
            intent.putExtra("orderId", Integer.parseInt(order_id.trim()));
        } else{
            intent= new Intent(this, NotificationListActivity.class);
        }
        Common.showLog("QUOTATIONID===="+id);
        // Pass the intent to switch to the MainActivity

        intent.putExtra("from", "notification");
        String channel_id = "notification_channel6";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.other);

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags

        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(sound)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
//                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        builder = builder.setContentTitle(title)
                .setContentText(messageBody)
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                    .setUsage(AudioAttributes. USAGE_ALARM )
                    .build() ;
            NotificationChannel notificationChannel= new NotificationChannel(channel_id, "stock-check6",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(messageBody);
            Common.showLog(sound.toString());
            notificationChannel.setSound(sound,audioAttributes);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Random rand = new Random();
        notificationManager.notify(rand.nextInt(), builder.build());
    }
}
