package com.devstringx.mytylesstockcheck.common;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

import java.text.ParseException;
import java.util.ArrayList;


public class PreferenceUtils {

    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;

    public static String LOGINDATA = "login_data";
    public static String ROLEPERMISSIONDATA = "rolepermission_data";
    public static String TOKEN = "token";
    public static String REFRESHTOKEN = "refresh_token";
    public static String USERID = "user_id";
    public static String NAME = "fullname";
    public static String EMAIL = "email_id";
    public static String PHONENUMBER = "phoneNumber";
    public static String ROLENAME = "roleName";
    public static String USERSTATUS = "userStatus";
    public static String ROLEID = "roleId";
    public static boolean ISVERIFIED = false;
    private Context context;

    public PreferenceUtils(Context context) {
        preferences = context.getSharedPreferences("IndiaTv_MyPref",MODE_PRIVATE);
        edit = preferences.edit();
    }
    public void decodeAndSaveToken(String jwtToken) {
        try {
            JWT jwt = null;
            try {
                jwt = JWTParser.parse(jwtToken);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            JWTClaimsSet claims = jwt.getJWTClaimsSet();

            // Extract values from the claims and save them using PreferenceUtils
            saveString(USERID, String.valueOf(claims.getLongClaim("userId")));
            saveString(NAME, claims.getStringClaim("firstName") + " " + claims.getStringClaim("lastName"));
            saveString(EMAIL, claims.getStringClaim("email"));
            saveString(ROLEID, String.valueOf(claims.getLongClaim("roleId")));
//            ROLEID=String.valueOf(claims.getLongClaim("roleId"));
            saveString(ROLENAME, claims.getStringClaim("roleName"));
            saveString(USERSTATUS, claims.getStringClaim("userStatus"));
            // Add more fields as needed

        } catch (ParseException e) {
            Log.e("JWTDecode", "Error decoding JWT token: " + e.getMessage());
        }
    }


    public void clearAll() {
       edit.putString(USERID,"");
        edit.putString(NAME,"");
        edit.putString(EMAIL,"");
        edit.putString(TOKEN,"");
        edit.putString(USERSTATUS,"");
        edit.putString(REFRESHTOKEN,"");
        edit.clear();
        edit.apply();
        edit.commit();
    }

    public void resetAll() {
        edit.clear();
        edit.apply();
        edit.commit();

    }

    public void saveString(String strKey, String strValue) {
        edit.putString(strKey, strValue);
        edit.commit();
    }

    public void saveInt(String strKey, int value) {
        edit.putInt(strKey, value);
        edit.commit();
    }

    public void saveLong(String strKey, Long value) {
        edit.putLong(strKey, value);
        edit.commit();
    }

    public void saveFloat(String strKey, float value) {
        edit.putFloat(strKey, value);
        edit.commit();
    }

    public void saveDouble(String strKey, String value) {
        edit.putString(strKey, value);
        edit.commit();
    }

    public void saveBoolean(String strKey, boolean value) {
        edit.putBoolean(strKey, value);
        edit.commit();
    }

    public void removeFromPreference(String strKey) {
        edit.remove(strKey);
    }

    public String getStringFromPreference(String strKey, String defaultValue) {
        return preferences.getString(strKey, defaultValue);
    }

    public boolean getbooleanFromPreference(String strKey, boolean defaultValue) {
        return preferences.getBoolean(strKey, defaultValue);
    }

    public int getIntFromPreference(String strKey, int defaultValue) {
        return preferences.getInt(strKey, defaultValue);
    }


    public long getLongFromPreference(String strKey) {
        return preferences.getLong(strKey, 0);
    }

    public float getFloatFromPreference(String strKey, float defaultValue) {
        return preferences.getFloat(strKey, defaultValue);
    }

}
