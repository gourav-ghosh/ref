package com.devstringx.mytylesstockcheck.pushNotification;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyFirebaseInstanceIDService {

    private static final String TAG = "MyFirebaseIIDService";
    private static String DEVICE_REFRESH_TOKEN = "";
    public static Context mContext;
    public void onTokenRefresh() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        DEVICE_REFRESH_TOKEN=token;
                        Log.d(TAG, "Refreshed token: " + token);
                    }
                });


    }
    // [END refresh_token]


//    public void sendRegistrationToServer(Context context, String token) {
//        // TODO: Implement this method to send token to your app server.
//        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
//            HashMap<String, String> map = new HashMap<>();
//            map.put(PreferenceUtils.DEVICE_REFRESH_TOKEN, token);
//            new NetworkRequest(context, this).callWebServices(ServiceMethods.WS_REGISTER_DEVICE, map);
//        }
//    }

    public static String getDeviceRefreshToken() {
        return DEVICE_REFRESH_TOKEN;
    }




}
