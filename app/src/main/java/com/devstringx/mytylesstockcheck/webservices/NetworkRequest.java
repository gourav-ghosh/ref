package com.devstringx.mytylesstockcheck.webservices;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.devstringx.mytylesstockcheck.BuildConfig;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.pushNotification.MyFirebaseInstanceIDService;
import com.devstringx.mytylesstockcheck.startup.LoginWithOtpActivity;
import com.devstringx.mytylesstockcheck.startup.LoginWithPasswordActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.builder.LoadBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import kotlin.NumbersKt;

/**
 * Created by venkatarao on 2/3/2017.
 */

public class NetworkRequest {
    private static LoadBuilder<Builders.Any.B> loadBuilder;
    Context context;
    ResponseListner responseListner;
    private DProgressbar loaderUtils;
    private PreferenceUtils utils;
    Dialog dialog;

    public NetworkRequest(Context context, ResponseListner responseListner) {
        this.context = context;
        this.responseListner = responseListner;
        utils = new PreferenceUtils(context);
        dialog = new Dialog(context);
        //init loader
        initLoader(context);
    }

    private void initLoader(Context context) {
        loaderUtils = new DProgressbar(context);
    }


    //For all webservices
    public void callWebServices(final ServiceMethods serviceMethods, HashMap<String, Object> map) {
        callWebServices(serviceMethods, map, true);
    }

    //    private void sendFile() {
//        // Assuming you have the file path
//        String filePath = "path/to/a-user.png";
//
//        File file = new File(filePath);
//
//        Ion.with(this)
//                .load("POST", API_URL)
//                .setMultipartParameter("fieldname", "file")
//                .setMultipartFile("file", "image/png", file)
//                .asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        // Handle the response here
//                        if (e != null) {
//                            // Error occurred
//                            e.printStackTrace();
//                        } else {
//                            // Successful response
//                            if (result != null) {
//                                // Process the result
//                                String response = result.toString();
//                                // Handle the response as needed
//                            }
//                        }
//                    }
//                });
//    }
    public void callWebServices(final ServiceMethods serviceMethods, HashMap<String, Object> map, boolean showLoader) {
        final ResponseDO responseDO = new ResponseDO();
        responseDO.setServiceMethods(serviceMethods);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!NetworkUtils.isNetworkConnectionAvailable(context)) {
                Toast.makeText(context, "INTERNET ERROR", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Common.showLog("SERVICEMETHOD NAME===" + serviceMethods);
        if (showLoader)
            loaderUtils.show();

        loadBuilder = Ion.with(context);

        String auth = "Bearer " + utils.getStringFromPreference(PreferenceUtils.TOKEN, "");
        Common.showLog("AUTH===" + auth);

        Builders.Any.B b = null;

        if (ServiceMethods.WS_UPLOADATTACHMENT == serviceMethods) {

            Common.showLog("=====" + new Gson().toJson(map));
            b = Ion.with(context)
                    .load("POST", BuildConfig.BASE_URL + "addUploadMultipleAttachments")
                    .addHeader("Authorization", auth);
            b.setMultipartParameter("id", map.get("id").toString().trim());

            ArrayList<String> imgs = (ArrayList<String>) map.get(NKeys.QUOTATIONDATA);
            for (int i = 0; i < imgs.size(); i++) {
                Common.showLog("API CALL IMAGE FILE===" + imgs.get(i));
                b.setMultipartFile("file", "image/png", new File(imgs.get(i)));
            }

            b.asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // Handle the response here
                            try {
                                if (loaderUtils != null)
                                    loaderUtils.dismiss();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            if (result == null) return;
                            if (e != null) {
                                // Error occurred
                                e.printStackTrace();
                            } else {
                                try {
                                    Common.showLog("STATUS CODE : " + result.toString());

                                    JSONObject jsonObject = new JSONObject(result.toString());
                                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                                        responseDO.setError(false);
                                    } else if (jsonObject.optString("status").equalsIgnoreCase("401")  && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data").optString("logout").equalsIgnoreCase("true")) {                                HashMap<String, Object> map = new HashMap<>();
                                        map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
                                        Common.showToast(context, "Maximum device limit reached, Login again.");
                                        Common.logout(context, dialog, new Gson().toJson(map));
                                    } else {
                                        responseDO.setError(true);
                                    }
                                    responseDO.setResponse(jsonObject.toString());
                                    responseListner.onResponseReceived(responseDO);
                                } catch (Exception e2) {
                                    Log.e("Error", e2.getMessage().toString());
                                    ResponseDO responseDO = new ResponseDO();
                                    responseDO.setError(true);
                                    responseDO.setResponse("Please try again");
                                    responseListner.onResponseReceived(responseDO);
                                }
                            }
                        }
                    });

        } else if (ServiceMethods.WS_CREATECOMPLAINT == serviceMethods) {

            Common.showLog("=====" + new Gson().toJson(map));
            b = Ion.with(context)
                    .load("POST", BuildConfig.BASE_URL + "createComplaint")
                    .addHeader("Authorization", auth);
            b.setMultipartParameter("sale_order_no", map.get("sale_order_no").toString().trim());
            b.setMultipartParameter("financial_year", map.get("financial_year").toString().trim());
            b.setMultipartParameter("customer_fullname", map.get("customer_fullname").toString().trim());
            b.setMultipartParameter("customer_mobile_number", map.get("customer_mobile_number").toString().trim());
            b.setMultipartParameter("complaint_type", map.get("complaint_type").toString().trim());
            b.setMultipartParameter("other_complaint_type", map.get("other_complaint_type").toString().trim());
            b.setMultipartParameter("estimate_resolve_date", map.get("estimate_resolve_date").toString().trim());
            b.setMultipartParameter("support_executive", map.get("support_executive").toString().trim());
            b.setMultipartParameter("comment", map.get("comment").toString().trim());

            ArrayList<String> imgs = (ArrayList<String>) map.get("file");
            for (int i = 0; i < imgs.size(); i++) {
                Common.showLog("API CALL IMAGE FILE===" + imgs.get(i));
                String fileExtension = Common.getFileExtension(imgs.get(i));
                String contentType = Common.getContentType(fileExtension);
                Common.showLog(contentType);
                b.setMultipartFile("file", contentType, new File(imgs.get(i)));
            }

            b.asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // Handle the response here
                            try {
                                if (loaderUtils != null)
                                    loaderUtils.dismiss();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            if (result == null) return;
                            if (e != null) {
                                // Error occurred
                                e.printStackTrace();
                            } else {
                                try {
                                    Common.showLog("STATUS CODE : " + result.toString());

                                    JSONObject jsonObject = new JSONObject(result.toString());
                                    if (jsonObject.optString("status").equalsIgnoreCase("201")) {
                                        responseDO.setError(false);
                                    } else if (jsonObject.optString("status").equalsIgnoreCase("401") && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data").optString("logout").equalsIgnoreCase("true")) {                                HashMap<String, Object> map = new HashMap<>();
                                        map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
                                        Common.showToast(context, "Maximum device limit reached, Login again.");
                                        Common.logout(context, dialog, new Gson().toJson(map));
                                    } else {
                                        responseDO.setError(true);
                                    }
                                    responseDO.setResponse(jsonObject.toString());
                                    responseListner.onResponseReceived(responseDO);
                                } catch (Exception e2) {
                                    Log.e("Error", e2.getMessage().toString());
                                    ResponseDO responseDO = new ResponseDO();
                                    responseDO.setError(true);
                                    responseDO.setResponse("Please try again");
                                    responseListner.onResponseReceived(responseDO);
                                }
                            }
                        }
                    });

        } else if (ServiceMethods.WS_RESOLVECOMPLAINT == serviceMethods) {

            Common.showLog("=====" + new Gson().toJson(map));
            b = Ion.with(context)
                    .load("POST", BuildConfig.BASE_URL + "complaintResolve")
                    .addHeader("Authorization", auth);
            b.setMultipartParameter("complaint_id", map.get("complaint_id").toString().trim());
            b.setMultipartParameter("solution", map.get("solution").toString().trim());
            if (map.get("solution").toString().trim().equalsIgnoreCase("Others"))
                b.setMultipartParameter("other_solution", map.get("other_solution").toString().trim());
            b.setMultipartParameter("comment", map.get("comment").toString().trim());
            b.setMultipartParameter("cost_to_mytyles", map.get("cost_to_mytyles").toString().trim());

            ArrayList<String> imgs = (ArrayList<String>) map.get("file");
            if (imgs != null) {
                for (int i = 0; i < imgs.size(); i++) {
                    Common.showLog("API CALL IMAGE FILE===" + imgs.get(i));
                    b.setMultipartFile("file", "image/png", new File(imgs.get(i)));
                }
            }

            b.asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // Handle the response here
                            try {
                                if (loaderUtils != null)
                                    loaderUtils.dismiss();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            if (result == null) return;
                            if (e != null) {
                                // Error occurred
                                e.printStackTrace();
                            } else {
                                try {
                                    Common.showLog("STATUS CODE : " + result.toString());

                                    JSONObject jsonObject = new JSONObject(result.toString());
                                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                                        responseDO.setError(false);
                                    } else if (jsonObject.optString("status").equalsIgnoreCase("401") && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data").optString("logout").equalsIgnoreCase("true")) {                                HashMap<String, Object> map = new HashMap<>();
                                        map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
                                        Common.showToast(context, "Maximum device limit reached, Login again.");
                                        Common.logout(context, dialog, new Gson().toJson(map));
                                    } else {
                                        responseDO.setError(true);
                                    }
                                    responseDO.setResponse(jsonObject.toString());
                                    responseListner.onResponseReceived(responseDO);
                                } catch (Exception e2) {
                                    Log.e("Error", e2.getMessage().toString());
                                    ResponseDO responseDO = new ResponseDO();
                                    responseDO.setError(true);
                                    responseDO.setResponse("Please try again");
                                    responseListner.onResponseReceived(responseDO);
                                }
                            }
                        }
                    });

        } else if (ServiceMethods.WS_UPLOADPROFILEIMAGE == serviceMethods) {
            b = Ion.with(context)
                    .load("POST", BuildConfig.BASE_URL + "uploadProfileImage");
            b.setMultipartParameter("id", map.get("id").toString().trim());
            String imgs = map.get("file").toString();
            b.setMultipartFile("file", "image/png", new File(imgs));


            b.asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // Handle the response here
                            try {
                                if (loaderUtils != null)
                                    loaderUtils.dismiss();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            if (result == null) return;
                            if (e != null) {
                                // Error occurred
                                e.printStackTrace();
                            } else {
                                try {
                                    Common.showLog("STATUS CODE : " + result.toString());

                                    JSONObject jsonObject = new JSONObject(result.toString());
                                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                                        responseDO.setError(false);
                                    } else if (jsonObject.optString("status").equalsIgnoreCase("401") && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data").optString("logout").equalsIgnoreCase("true")) {                                HashMap<String, Object> map = new HashMap<>();
                                        map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
                                        Common.showToast(context, "Maximum device limit reached, Login again.");
                                        Common.logout(context, dialog, new Gson().toJson(map));
                                    } else {
                                        responseDO.setError(true);
                                    }
                                    responseDO.setResponse(jsonObject.toString());
                                    responseListner.onResponseReceived(responseDO);
                                } catch (Exception e2) {
                                    Log.e("Error", e2.getMessage().toString());
                                    ResponseDO responseDO = new ResponseDO();
                                    responseDO.setError(true);
                                    responseDO.setResponse("Please try again");
                                    responseListner.onResponseReceived(responseDO);
                                }
                            }
                        }
                    });

        } else if (ServiceMethods.WS_ORDERDISPATCHED == serviceMethods) {
            b = Ion.with(context)
                    .load("POST", BuildConfig.BASE_URL + "orderDispatched")
                    .addHeader("Authorization", auth);
            Common.showLog("ERoor " + map.get("sale_order_no").toString().trim());
            if (map.get("delivery_type").toString().equalsIgnoreCase("HomeDelivery")) {
                b.setMultipartParameter("delivery_time", map.get("delivery_time").toString().trim());
            } else {
                b.setMultipartParameter("direct_ready_for_pickup", map.get("direct_ready_for_pickup").toString().trim());
            }
            b.setMultipartParameter("id", map.get("id").toString().trim());
            b.setMultipartParameter("comment", map.get("comment").toString().trim());
            b.setMultipartParameter("delivery_type", map.get("delivery_type").toString().trim());
            b.setMultipartParameter("delivery_date", map.get("delivery_date").toString().trim());
            b.setMultipartParameter("delivery_location_type", map.get("delivery_location_type").toString().trim());
            b.setMultipartParameter("order_type", map.get("order_type").toString().trim());
            b.setMultipartParameter("sale_order_no", map.get("sale_order_no").toString().trim());
            b.setMultipartParameter("customer_phone_number", map.get("customer_phone_number").toString().trim());
            b.setMultipartParameter("quote_order_amount", map.get("quote_order_amount").toString().trim());
            b.setMultipartParameter("sales_person", map.get("sales_person").toString().trim());
            b.setMultipartParameter("dispatch_manager", map.get("dispatch_manager").toString().trim());
            b.setMultipartParameter("architect", map.get("architect").toString().trim());
            if (map.containsKey("links"))
                map.put("links", map.get("links").toString().trim());

            String inst = String.valueOf(map.put("instructions", map.get("instructions").toString().trim()));
            String[] allIns = inst.split(",");
            for (int i = 0; i < allIns.length; i++) {
                Common.showLog("API CALL IMAGE FILE=instructions==" + allIns[i]);
                if (!allIns[i].isEmpty())
                    b.setMultipartFile("instructions", "image/png", new File(allIns[i]));
            }
            ArrayList<String> imgs = (ArrayList<String>) map.get("proof");
            for (int i = 0; i < imgs.size(); i++) {
                Common.showLog("API CALL IMAGE FILE=proof==" + imgs.get(i));
                if (!imgs.get(i).isEmpty())
                    b.setMultipartFile("proof", "image/png", new File(imgs.get(i)));
            }
            b.asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // Handle the response here
                            try {
                                if (loaderUtils != null)
                                    loaderUtils.dismiss();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            if (result == null) return;
                            if (e != null) {
                                // Error occurred
                                e.printStackTrace();
                            } else {
                                try {
                                    Common.showLog("STATUS CODE : " + result.toString());

                                    JSONObject jsonObject = new JSONObject(result.toString());
                                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                                        responseDO.setError(false);
                                    } else if (jsonObject.optString("status").equalsIgnoreCase("401") && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data").optString("logout").equalsIgnoreCase("true")) {                                HashMap<String, Object> map = new HashMap<>();
                                        map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
                                        Common.showToast(context, "Maximum device limit reached, Login again.");
                                        Common.logout(context, dialog, new Gson().toJson(map));
                                    } else {
                                        responseDO.setError(true);
                                    }
                                    responseDO.setResponse(jsonObject.toString());
                                    responseListner.onResponseReceived(responseDO);
                                } catch (Exception e2) {
                                    Log.e("Error", e2.getMessage().toString());
                                    ResponseDO responseDO = new ResponseDO();
                                    responseDO.setError(true);
                                    responseDO.setResponse("Please try again");
                                    responseListner.onResponseReceived(responseDO);
                                }
                            }
                        }
                    });

        }else if (ServiceMethods.WS_CREATEARCHITECT == serviceMethods) {

            Common.showLog("=====" + new Gson().toJson(map));
            b = Ion.with(context)
                    .load("POST", BuildConfig.BASE_URL + "addEditArchitect")
                    .addHeader("Authorization", auth);
            if (map.get("type").toString().equalsIgnoreCase("edit")) {
                b.setMultipartParameter("architectId", map.get("architectId").toString().trim());
            }
            b.setMultipartParameter("first_name", map.get("first_name").toString().trim());
            b.setMultipartParameter("last_name", map.get("last_name").toString().trim());
            b.setMultipartParameter("primary_phone", map.get("primary_phone").toString().trim());
            b.setMultipartParameter("company_name", map.get("company_name").toString().trim());
            b.setMultipartParameter("address", map.get("address").toString().trim());
            b.setMultipartParameter("city", map.get("city").toString().trim());
            b.setMultipartParameter("state", map.get("state").toString().trim());
            b.setMultipartParameter("country", map.get("country").toString().trim());
            b.setMultipartParameter("pincode", map.get("pincode").toString().trim());
            b.setMultipartParameter("email", map.get("email").toString().trim());
            b.setMultipartParameter("brand_name", map.get("brand_name").toString().trim());
            b.setMultipartParameter("established_year", map.get("established_year").toString().trim());
            b.setMultipartParameter("gst_number", map.get("gst_number").toString().trim());
            b.setMultipartParameter("secondary_phone", map.get("secondary_phone").toString().trim());

            ArrayList<String> imgs = (ArrayList<String>) map.get("file");
            for (int i = 0; i < imgs.size(); i++) {
                Common.showLog("API CALL IMAGE FILE===" + imgs.get(i));
                String fileExtension = Common.getFileExtension(imgs.get(i));
                String contentType = Common.getContentType(fileExtension);
                Common.showLog(contentType + "==========" + imgs.get(i));
                b.setMultipartFile("file", contentType, new File(imgs.get(i)));

            }

            b.asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // Handle the response here
                            try {
                                if (loaderUtils != null)
                                    loaderUtils.dismiss();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                            if (e != null) {
                                // Error occurred
                                e.printStackTrace();
                            } else {
                                try {
                                    Common.showLog("STATUS CODE : " + result.toString());

                                    JSONObject jsonObject = new JSONObject(result.toString());
                                    if (jsonObject.optString("status").equalsIgnoreCase("201")) {
                                        responseDO.setError(false);
                                    } else if (jsonObject.optString("status").equalsIgnoreCase("401") && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data").optString("logout").equalsIgnoreCase("true")) {                                HashMap<String, Object> map = new HashMap<>();
                                        map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
                                        Common.showToast(context, "Maximum device limit reached, Login again.");
                                        Common.logout(context, dialog, new Gson().toJson(map));
                                    } else {
                                        responseDO.setError(true);
                                    }
                                    responseDO.setResponse(jsonObject.toString());
                                    responseListner.onResponseReceived(responseDO);
                                } catch (Exception e2) {
                                    Log.e("Error", e2.getMessage().toString());
                                    ResponseDO responseDO = new ResponseDO();
                                    responseDO.setError(true);
                                    responseDO.setResponse("Please try again");
                                    responseListner.onResponseReceived(responseDO);
                                }
                            }
                        }
                    });

        }else  if (ServiceMethods.WS_ADDSHIPPINGCHARGE == serviceMethods) {

            Common.showLog("=====" + new Gson().toJson(map));
            b = Ion.with(context)
                    .load("POST", BuildConfig.BASE_URL + "addShippingCharge")
                    .addHeader("Authorization", auth);
            b.setMultipartParameter("shipping_id", map.get("shipping_id").toString().trim());
            b.setMultipartParameter("distance", map.get("distance").toString().trim());
            b.setMultipartParameter("loading_points", map.get("loading_points").toString().trim());
            b.setMultipartParameter("amount", map.get("amount").toString().trim());
            b.setMultipartParameter("comment", map.get("comment").toString().trim());

            ArrayList<String> imgs = (ArrayList<String>) map.get("file");
            for (int i = 0; i < imgs.size(); i++) {
                Common.showLog("API CALL IMAGE FILE===" + imgs.get(i));
                String fileExtension = Common.getFileExtension(imgs.get(i));
                String contentType = Common.getContentType(fileExtension);
                Common.showLog(contentType + "==========" + imgs.get(i));
                b.setMultipartFile("file", contentType, new File(imgs.get(i)));

            }

            b.asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // Handle the response here
                            try {
                                if (loaderUtils != null)
                                    loaderUtils.dismiss();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                            if (e != null) {
                                // Error occurred
                                e.printStackTrace();
                            } else {
                                try {
                                    Common.showLog("STATUS CODE : " + result.toString());

                                    JSONObject jsonObject = new JSONObject(result.toString());
                                    if (jsonObject.optString("status").equalsIgnoreCase("201")) {
                                        responseDO.setError(false);
                                    } else if (jsonObject.optString("status").equalsIgnoreCase("401") && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data").optString("logout").equalsIgnoreCase("true")) {                                HashMap<String, Object> map = new HashMap<>();
                                        map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
                                        Common.showToast(context, "Maximum device limit reached, Login again.");
                                        Common.logout(context, dialog, new Gson().toJson(map));
                                    } else {
                                        responseDO.setError(true);
                                    }
                                    responseDO.setResponse(jsonObject.toString());
                                    responseListner.onResponseReceived(responseDO);
                                } catch (Exception e2) {
                                    Log.e("Error", e2.getMessage().toString());
                                    ResponseDO responseDO = new ResponseDO();
                                    responseDO.setError(true);
                                    responseDO.setResponse("Please try again");
                                    responseListner.onResponseReceived(responseDO);
                                }
                            }
                        }
                    });

        }else  if (ServiceMethods.WS_UPLOADPOFILE == serviceMethods) {

            Common.showLog("=====" + new Gson().toJson(map));
            b = Ion.with(context)
                    .load("POST", BuildConfig.BASE_URL + "uploadPOFile")
                    .addHeader("Authorization", auth);
            b.setMultipartParameter("order_id", map.get("order_id").toString().trim());

            ArrayList<String> imgs = (ArrayList<String>) map.get("file");
            for (int i = 0; i < imgs.size(); i++) {
                Common.showLog("API CALL IMAGE FILE===" + imgs.get(i));
                String fileExtension = Common.getFileExtension(imgs.get(i));
                String contentType = Common.getContentType(fileExtension);
                Common.showLog(contentType + "==========" + imgs.get(i));
                b.setMultipartFile("file", contentType, new File(imgs.get(i)));

            }

            b.asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // Handle the response here
                            try {
                                if (loaderUtils != null)
                                    loaderUtils.dismiss();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                            if (e != null) {
                                // Error occurred
                                e.printStackTrace();
                            } else {
                                try {
                                    Common.showLog("STATUS CODE : " + result.toString());

                                    JSONObject jsonObject = new JSONObject(result.toString());
                                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                                        responseDO.setError(false);
                                    } else if (jsonObject.optString("status").equalsIgnoreCase("401") && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data").optString("logout").equalsIgnoreCase("true")) {                                HashMap<String, Object> map = new HashMap<>();
                                        map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
                                        Common.showToast(context, "Maximum device limit reached, Login again.");
                                        Common.logout(context, dialog, new Gson().toJson(map));
                                    } else {
                                        responseDO.setError(true);
                                    }
                                    responseDO.setResponse(jsonObject.toString());
                                    responseListner.onResponseReceived(responseDO);
                                } catch (Exception e2) {
                                    Log.e("Error", e2.getMessage().toString());
                                    ResponseDO responseDO = new ResponseDO();
                                    responseDO.setError(true);
                                    responseDO.setResponse("Please try again");
                                    responseListner.onResponseReceived(responseDO);
                                }
                            }
                        }
                    });

        }
        else if (ServiceMethods.WS_CREATEWORKLOG == serviceMethods) {

            Common.showLog("=====" + new Gson().toJson(map));
            b = Ion.with(context)
                    .load("POST", BuildConfig.BASE_URL + NKeys.CREATEWORKLOG)
                    .addHeader("Authorization", auth);
            b.setMultipartParameter("worklogStatus", map.get("worklogStatus").toString());
            b.setMultipartParameter("comment", map.get("comment").toString());
            b.setMultipartParameter("workingDate", map.get("workingDate").toString());
            if(map.get("worklogStatus").toString().equalsIgnoreCase("present")) {
                if(map.get("overTime").toString().equalsIgnoreCase("true")) {
                    b.setMultipartParameter("overTime", map.get("overTime").toString());
                    b.setMultipartParameter("hoursWorked", map.get("hoursWorked").toString());
                }
                ArrayList<String> imgs = (ArrayList<String>) map.get("file");
                for (int i = 0; i < imgs.size(); i++) {
                    Common.showLog("API CALL IMAGE FILE===" + imgs.get(i));
                    String fileExtension = Common.getFileExtension(imgs.get(i));
                    String contentType = Common.getContentType(fileExtension);
                    Common.showLog(contentType + "==========" + imgs.get(i));
                    b.setMultipartFile("file", contentType, new File(imgs.get(i)));
                }
            }
            b.asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // Handle the response here
                            try {
                                if (loaderUtils != null)
                                    loaderUtils.dismiss();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                            if (e != null) {
                                // Error occurred
                                e.printStackTrace();
                            } else {
                                try {
                                    Common.showLog("STATUS CODE : " + result.toString());

                                    JSONObject jsonObject = new JSONObject(result.toString());
                                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                                        responseDO.setError(false);
                                    } else if (jsonObject.optString("status").equalsIgnoreCase("401") && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data").optString("logout").equalsIgnoreCase("true")) {                                HashMap<String, Object> map = new HashMap<>();
                                        map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
                                        Common.showToast(context, "Maximum device limit reached, Login again.");
                                        Common.logout(context, dialog, new Gson().toJson(map));
                                    } else {
                                        responseDO.setError(true);
                                    }
                                    responseDO.setResponse(jsonObject.toString());
                                    responseListner.onResponseReceived(responseDO);
                                } catch (Exception e2) {
                                    Log.e("Error", e2.getMessage().toString());
                                    ResponseDO responseDO = new ResponseDO();
                                    responseDO.setError(true);
                                    responseDO.setResponse("Please try again");
                                    responseListner.onResponseReceived(responseDO);
                                }
                            }
                        }
                    });

        }
//        else if (ServiceMethods.WS_CREATEARCHITECT == serviceMethods) {
//            b = Ion.with(context)
//                    .load("POST", BuildConfig.BASE_URL + "addEditArchitect")
//                    .addHeader("Authorization", auth);
//            if (map.get("type").toString().equalsIgnoreCase("edit")) {
//                b.setMultipartParameter("architectId", map.get("architectId").toString().trim());
//            }
//            b.setMultipartParameter("first_name", map.get("first_name").toString().trim());
//            b.setMultipartParameter("last_name", map.get("last_name").toString().trim());
//            b.setMultipartParameter("primary_phone", map.get("primary_phone").toString().trim());
//            b.setMultipartParameter("company_name", map.get("company_name").toString().trim());
//            b.setMultipartParameter("address", map.get("address").toString().trim());
//            b.setMultipartParameter("city", map.get("city").toString().trim());
//            b.setMultipartParameter("state", map.get("state").toString().trim());
//            b.setMultipartParameter("country", map.get("country").toString().trim());
//            b.setMultipartParameter("pincode", map.get("pincode").toString().trim());
//            b.setMultipartParameter("email", map.get("email").toString().trim());
//            b.setMultipartParameter("secondary_phone", map.get("secondary_phone").toString().trim());
//
//            ArrayList<String> imgs = (ArrayList<String>) map.get("file");
//            for (int i = 0; i < imgs.size(); i++) {
//                Common.showLog("API CALL IMAGE FILE=proof==" + imgs.get(i));
//                if (!imgs.get(i).isEmpty())
//                    b.setMultipartFile("file","Application/pdf", new File(imgs.get(i)));
//            }
//
//            b.asJsonObject()
//                    .setCallback(new FutureCallback<JsonObject>() {
//                        @Override
//                        public void onCompleted(Exception e, JsonObject result) {
//                            // Handle the response here
//                            try {
//                                if (loaderUtils != null)
//                                    loaderUtils.dismiss();
//                            } catch (Exception e1) {
//                                e1.printStackTrace();
//                            }
//                            if (result == null) return;
//                            if (e != null) {
//                                // Error occurred
//                                e.printStackTrace();
//                            } else {
//                                try {
//                                    Common.showLog("STATUS CODE : " + result.toString());
//
//                                    JSONObject jsonObject = new JSONObject(result.toString());
//                                    if (jsonObject.optString("status").equalsIgnoreCase("201")) {
//                                        responseDO.setError(false);
//                                    } else {
//                                        responseDO.setError(true);
//                                    }
//                                    responseDO.setResponse(jsonObject.toString());
//                                    responseListner.onResponseReceived(responseDO);
//                                } catch (Exception e2) {
//                                    Log.e("Error", e2.getMessage().toString());
//                                    ResponseDO responseDO = new ResponseDO();
//                                    responseDO.setError(true);
//                                    responseDO.setResponse("Please try again");
//                                    responseListner.onResponseReceived(responseDO);
//                                }
//                            }
//                        }
//                    });

//        }
        else if(ServiceMethods.WS_POARRANGE == serviceMethods) {

            Common.showLog("=====" + new Gson().toJson(map));
            b = Ion.with(context)
                    .load("POST", BuildConfig.BASE_URL + NKeys.POARRANGE)
                    .addHeader("Authorization", auth);
            if(map.get("task_id") != null && !map.get("task_id").toString().equalsIgnoreCase("")) {
                b.setMultipartParameter("task_id", map.get("task_id").toString());
            } else if (map.get("po_codes") != null && map.get("order_id") != null) {
                b.setMultipartParameter("po_codes", map.get("po_codes").toString());
                b.setMultipartParameter("order_id", map.get("order_id").toString());
            } else {
                Common.showToast(context, "Parameters missing for marking PO(s) arranged.");
            }
            ArrayList<String> imgs = (ArrayList<String>) map.get("file");
            for (int i = 0; i < imgs.size(); i++) {
                Common.showLog("API CALL IMAGE FILE===" + imgs.get(i));
                String fileExtension = Common.getFileExtension(imgs.get(i));
                String contentType = Common.getContentType(fileExtension);
                Common.showLog(contentType + "==========" + imgs.get(i));
                b.setMultipartFile("file", contentType, new File(imgs.get(i)));
            }
            b.asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // Handle the response here
                            try {
                                if (loaderUtils != null)
                                    loaderUtils.dismiss();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                            if (e != null) {
                                // Error occurred
                                e.printStackTrace();
                            } else {
                                try {
                                    Common.showLog("STATUS CODE : " + result.toString());

                                    JSONObject jsonObject = new JSONObject(result.toString());
                                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                                        responseDO.setError(false);
                                    } else if (jsonObject.optString("status").equalsIgnoreCase("401") && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data").optString("logout").equalsIgnoreCase("true")) {                                HashMap<String, Object> map = new HashMap<>();
                                        map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
                                        Common.showToast(context, "Maximum device limit reached, Login again.");
                                        Common.logout(context, dialog, new Gson().toJson(map));
                                    } else {
                                        responseDO.setError(true);
                                    }
                                    responseDO.setResponse(jsonObject.toString());
                                    responseListner.onResponseReceived(responseDO);
                                } catch (Exception e2) {
                                    Log.e("Error", e2.getMessage().toString());
                                    ResponseDO responseDO = new ResponseDO();
                                    responseDO.setError(true);
                                    responseDO.setResponse("Please try again");
                                    responseListner.onResponseReceived(responseDO);
                                }
                            }
                        }
                    });

        }
        else if (ServiceMethods.WS_UPLOADCOMPLAINTMEDIA == serviceMethods) {
            b = Ion.with(context)
                    .load("POST", BuildConfig.BASE_URL + "uploadMultipleComplaintMedia")
                    .addHeader("Authorization", auth);
            b.setMultipartParameter("id", map.get("id").toString().trim());
            b.setMultipartParameter("media_type", map.get("media_type").toString().trim());


            ArrayList<String> imgs = (ArrayList<String>) map.get("file");
            for (int i = 0; i < imgs.size(); i++) {
                Common.showLog("API CALL IMAGE FILE=proof==" + imgs.get(i));
                if (!imgs.get(i).isEmpty()) {
                    String fileExtension = Common.getFileExtension(imgs.get(i));
                    String contentType = Common.getContentType(fileExtension);
                    Common.showLog(contentType);
                    b.setMultipartFile("file", contentType, new File(imgs.get(i)));
                }
            }

            b.asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // Handle the response here
                            try {
                                if (loaderUtils != null)
                                    loaderUtils.dismiss();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            if (result == null) return;
                            if (e != null) {
                                // Error occurred
                                e.printStackTrace();
                            } else {
                                try {
                                    Common.showLog("STATUS CODE : " + result.toString());

                                    JSONObject jsonObject = new JSONObject(result.toString());
                                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                                        responseDO.setError(false);
                                    } else if (jsonObject.optString("status").equalsIgnoreCase("401") && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data").optString("logout").equalsIgnoreCase("true")) {                                HashMap<String, Object> map = new HashMap<>();
                                        map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
                                        Common.showToast(context, "Maximum device limit reached, Login again.");
                                        Common.logout(context, dialog, new Gson().toJson(map));
                                    } else {
                                        responseDO.setError(true);
                                    }
                                    responseDO.setResponse(jsonObject.toString());
                                    responseListner.onResponseReceived(responseDO);
                                } catch (Exception e2) {
                                    Log.e("Error", e2.getMessage().toString());
                                    ResponseDO responseDO = new ResponseDO();
                                    responseDO.setError(true);
                                    responseDO.setResponse("Please try again");
                                    responseListner.onResponseReceived(responseDO);
                                }
                            }
                        }
                    });

        } else if (ServiceMethods.WS_UPLOADMULTIPLEORDERFILE == serviceMethods) {
            b = Ion.with(context)
                    .load("POST", BuildConfig.BASE_URL + NKeys.UPLOADMULTIPLEORDERFILE)
                    .addHeader("Authorization", auth);
            b.setMultipartParameter("id", map.get("id").toString().trim());
            b.setMultipartParameter("for_status", map.get("for_status").toString().trim());


            ArrayList<String> imgs = (ArrayList<String>) map.get("file");
            for (int i = 0; i < imgs.size(); i++) {
                Common.showLog("API CALL IMAGE FILE=proof==" + imgs.get(i));
                if (!imgs.get(i).isEmpty()) {
                    String fileExtension = Common.getFileExtension(imgs.get(i));
                    String contentType = Common.getContentType(fileExtension);
                    Common.showLog(contentType);
                    b.setMultipartFile("file", contentType, new File(imgs.get(i)));
                }
            }

            b.asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // Handle the response here
                            try {
                                if (loaderUtils != null)
                                    loaderUtils.dismiss();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            if (result == null) return;
                            if (e != null) {
                                // Error occurred
                                e.printStackTrace();
                            } else {
                                try {
                                    Common.showLog("STATUS CODE : " + result.toString());

                                    JSONObject jsonObject = new JSONObject(result.toString());
                                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                                        responseDO.setError(false);
                                    } else if (jsonObject.optString("status").equalsIgnoreCase("401") && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data").optString("logout").equalsIgnoreCase("true")) {                                HashMap<String, Object> map = new HashMap<>();
                                        map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
                                        Common.showToast(context, "Maximum device limit reached, Login again.");
                                        Common.logout(context, dialog, new Gson().toJson(map));
                                    } else {
                                        responseDO.setError(true);
                                    }
                                    responseDO.setResponse(jsonObject.toString());
                                    responseListner.onResponseReceived(responseDO);
                                } catch (Exception e2) {
                                    Log.e("Error", e2.getMessage().toString());
                                    ResponseDO responseDO = new ResponseDO();
                                    responseDO.setError(true);
                                    responseDO.setResponse("Please try again");
                                    responseListner.onResponseReceived(responseDO);
                                }
                            }
                        }
                    });

        } else {
            //GetObservable based on methods
            switch (serviceMethods) {

                case WS_LOGINWITHMOBILEPASS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.setStringBody((String) map.get(NKeys.LOGINDATA));
                    break;
                case WS_GETOTP:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.setStringBody((String) map.get(NKeys.GETOTP));
                    break;
                case WS_OTPVERIFICATION:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.setStringBody((String) map.get(NKeys.OTPVERIFICATION));
                    break;
                case WS_RESETPASSWORD:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Authorization", auth);
                    b.addHeader("Content-Type", "application/json");
                    b.setStringBody((String) map.get(NKeys.RESETPASSWORD));
                    break;
                case WS_CHANGEPASSWORD:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Authorization", auth);
                    b.addHeader("Content-Type", "application/json");
                    b.setStringBody((String) map.get(NKeys.CHANGEPASSWORD));
                    break;
                case WS_GETALLLEADS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Authorization", auth);
                    b.addHeader("Content-Type", "application/json");
                    if(map!=null)
                        b.setStringBody((String) map.get(NKeys.GETALLLEADS));
                    break;
                case WS_GETLEADTASK:
                case WS_GETLEADQUOTATION:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    break;
                case WS_GETLEADSTAGE:
                case WS_GETACTIVITYTYPE:
                case WS_GETREQUIRENMENTS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Authorization", auth);
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    break;
                case WS_GETFILTERTASK:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETFILTERTASK));
                    break;
                case WS_UPDATELEADSTAGE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.UPDATELEADSTAGEDATA));
                    break;
                case WS_GETLEADLOSTREASON:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    break;
                case WS_GETLEADHISTORYBYLEADID:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETLEADHISTORYBYLEADID));
                    break;
                case WS_CREATELEADACTIVITY:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.CREATELEADACTIVITY));
                    break;
                case WS_GETFILTERLEADS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETFILTERLEADS));
                    break;
                case WS_CREATELEADTASK:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.CREATELEADTASK));
                    break;
                case WS_LEADDETAILS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.LEADDETAILSDATA));
                    break;
                case WS_GETUSERFORSCREEN:
                case WS_GETALLOWNERS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.ALLOWNERSDATA));
                    Common.showLog("USERFILTER====" + map.get(NKeys.ALLOWNERSDATA));
                    break;
                case WS_GETMARKLEADASSTAR:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETMARKLEADASSTAR));
                    break;
                case WS_GETMARKTASKASCOMPLETED:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETMARKTASKASCOMPLETED));
                    break;
                case WS_GETRESCHEDULETASK:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETRESCHEDULETASK));
                    break;
                case WS_GETCITIES:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.setStringBody((String) map.get(NKeys.CITIESDATA));
                    break;
                case WS_GETLEADTASKBYLEADID:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.setStringBody((String) map.get(NKeys.GETLEADTASKBYLEADID));
                    break;
                case WS_GETSTATES:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    break;
                case WS_CREATELEAD:
                case WS_UPDATELEAD:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.LEADSDATA));
                    break;
                case WS_GETALLQUOTES:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    Common.showLog("=======ALL QUOTATION" + (String) map.get(NKeys.QUOTATIONDATA));
                    if (map != null) {
                        b.setStringBody((String) map.get(NKeys.QUOTATIONDATA));
                    }
                    break;
                case WS_GETQUOTESDETAILS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.QUOTATIONDATA));
                    break;
                case WS_ALLPRODUCTINVENTRY:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETALLPRODUCTINVENTRYDATA));
                    break;
                case WS_UPDATEBILLINGADDRESS:
                case WS_ADDBILLINGADDRESS:
                case WS_ADDSHIPPINGADDRESS:
                case WS_REMOVESHIPPINGADDRESS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.ADDRESSDATA));
                    break;
                case WS_FILTERGETVENDORQUOTATIONS:
                case WS_GETVENDORQUOTATIONS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETVENDORQUOTATIONS));
                    break;
                case WS_GETLEADSHIPPINGADDRESS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.ADDRESSDATA));
                    break;
                case WS_MAKEDEFAULTSHIPPINGADDRESS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.ADDRESSDATA));
                    break;
                case WS_RECHECKQUOTE:
                case WS_DOWNLOADQUOTE:
                case WS_CREATEQUOTATION:
                case WS_EDITQUOTE:
                case WS_EXPORTQUOTATIONDATA:
                case WS_EXPORTUSERDATA:
                case WS_EXPORTUSERQUOTATIONDATA:
                case WS_EXPORTLEADSDATA:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.QUOTATIONDATA));
                    break;
                case WS_UPDATEQUOTATIONPRODUCTSTATUS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.UPDATEQUOTATIONPRODUCTSTATUS));
                    break;
                case WS_UPLOADATTACHMENT:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
//                b.addHeader("Accept","application/json");
//                b.addHeader("Content-Type","application/json");
                    b.addHeader("Authorization", auth);
                    b.setMultipartParameter("id", (String) map.get("id").toString().trim());
                    ArrayList<String> imgs = (ArrayList<String>) map.get(NKeys.QUOTATIONDATA);
                    for (int i = 0; i < imgs.size(); i++) {
                        Common.showLog("API CALL IMAGE FILE===" + imgs.get(i));
                        b.setMultipartFile("file", new File(imgs.get(i)));
                    }
                    Common.showLog("=====" + new Gson().toJson(map));
                    break;
                case WS_DELETEATTACHMENT:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.QUOTATIONDATA));
                    break;
                case WS_DELETEQUOTATION:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.QUOTATIONDATA));
                    break;
                case WS_GETUSERPROFILE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETUSERPROFILE));
                    break;
                case WS_UPDATEUSERPROFILE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.UPDATEUSERPROFILE));
                    break;
                case WS_REMOVEPROFILEPIC:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.REMOVEPROFILEPIC));
                    break;
                case WS_GETALLCUSTOMERS:
                case WS_GETBRANCHNAME:
                case WS_GETVENDORCOMPANYNAME:
                case WS_GET_BANK_DETAILS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    break;
                case WS_GETROLES:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Authorization", auth);
                    break;
                case WS_CHANGEPASSWORDFORUSER:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.CHANGEPASSWORDFORUSER));
                    break;
                case WS_UPDATESTATUS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.UPDATESTATUS));
                    break;
                case WS_RESENDPASSWORD:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.RESENDPASSWORD));
                    break;
                case WS_CREATEUSER:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.CREATEUSER));
                    break;
                case WS_UPDATEUSER:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.UPDATEUSER));
                    break;
                case WS_TEMPORARYPASSWORDCHANGE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.TEMPORARYPASSWORDCHANGE));
                    break;
                case WS_GETRAZORPAY:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETRAZORPAY));
                    break;
                case WS_CREATERAZORPAY:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.CREATERAZORPAY));
                    break;
                case WS_DELETERAZORPAY:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.DELETERAZORPAY));
                    break;
                case WS_GETUSERSBYROLENAME:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETUSERSBYROLENAME));
                    break;
                case WS_GETALLCOMPLAINTS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETALLCOMPLAINTS));
                    break;
                case WS_GETMODULEBYUSER:
                    b = loadBuilder.load("GET", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETMODULEBYUSER));
                    break;
                case WS_GETALLARCHITECT:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETALLARCHITECT));
                    break;
                case WS_GETALLWORKLOGS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETALLWORKLOGS));
                    break;
                case WS_GETARCHITECTDETAILS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETARCHITECTDETAILS));
                    break;
                case WS_GETORDERBYARCHITECT:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETORDERBYARCHITECT));
                    break;
                case WS_DELETEARCHITECT:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.DELETEARCHITECT));
                    break;
                case WS_GETCOMPLAINTDETAILS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETCOMPLAINTDETAILS));
                    break;
                case WS_DELETECOMPLAINT:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.DELETECOMPLAINT));
                    break;
                case WS_COMPLAINTPROCESS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.COMPLAINTPROCESS));
                    break;
                case WS_GETCOMPLAINTFILTERVALUE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETCOMPLAINTFILTERVALUE));
                    break;
                case WS_CHECKSALESORDERNUMBER:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.CHECKSALESORDERNUMBER));
                    break;
                case WS_EDITCOMPLAINT:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.EDITCOMPLAINT));
                    break;
                case WS_GENERATEARCHITECTLINK:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GENERATEARCHITECTLINK));
                    break;
                case WS_DELETECOMPLAINTMEDIA:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.DELETECOMPLAINTMEDIA));
                    break;
                case WS_DELETEARCHITECTMEDIA:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.DELETEARCHITECTMEDIA));
                    break;
                case WS_CREATECOMPLAINTCOMMENT:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.CREATECOMPLAINTCOMMENT));
                    break;
                case WS_GETCOMPLAINTCOMMENT:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETCOMPLAINTCOMMENT));
                    break;
                case WS_GETORDERTABS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    break;
                case WS_GETALLORDERS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETALLORDERS));
                    break;
                case WS_GETORDERDETAILS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETORDERDETAILS));
                    break;
                case WS_ORDERDELETE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.ORDERDELETE));
                    break;
                case WS_ORDERCANCEL:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.ORDERCANCEL));
                    break;
                case WS_GET_ALL_TASK:
                    b = loadBuilder.load("GET", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Authorization", auth);
                    break;
                case WS_GET_ALL_SHIPPING_TAB:
                    b = loadBuilder.load("GET", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Authorization", auth);
                    break;
                case WS_ADD_TASK:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Authorization", auth);

                    b.setMultipartParameter("so_number", (String) map.get("so_number").toString().trim());
                    b.setMultipartParameter("assign_to", (String) map.get("assign_to").toString().trim());
                    b.setMultipartParameter("date", (String) map.get("date").toString().trim());
                    b.setMultipartParameter("time", (String) map.get("time").toString().trim());
                    b.setMultipartParameter("reminder_time", (String) map.get("reminder_time").toString().trim());
                    b.setMultipartParameter("comment", (String) map.get("comment").toString().trim());

                    ArrayList<String> images = (ArrayList<String>) map.get("file");
                    for (int i = 0; i < images.size(); i++) {
                        Common.showLog("API CALL IMAGE FILE=proof==" + images.get(i));
                        if (!images.get(i).isEmpty()) {
                            String fileExtension = Common.getFileExtension(images.get(i));
                            String contentType = Common.getContentType(fileExtension);
                            Common.showLog(contentType);
                            b.setMultipartFile("file", contentType, new File(images.get(i)));
                        }
                    }


                    Common.showLog("=====" + new Gson().toJson(map));
                    break;
                case WS_UPDATEDELIVERYTYPE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    Common.showLog("12345" + map.get(NKeys.UPDATEDELIVERYTYPE));
                    b.setStringBody((String) map.get(NKeys.UPDATEDELIVERYTYPE));
                    break;
                case WS_UPDATEORDERTYPE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.UPDATEORDERTYPE));
                    break;
                case WS_UPDATEDELIVERYLOCATIONTYPE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.UPDATEDELIVERYLOCATIONTYPE));
                    break;
                case WS_ORDERRESCHEDULE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.ORDERRESCHEDULE));
                    break;
                case WS_UPDATEPAYMENT:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.UPDATEPAYMENT));
                    break;
                case WS_GETORDERFILTERVALUE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETORDERFILTERVALUE));
                    break;
                case WS_GETARCHITECTNAMELIST:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    break;
                case WS_GET_TASK_DETAILS:
                case WS_COMPLETE_TASK:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.ALLOWNERSDATA));
                    break;
                case WS_APPROVE_SHIPPING_CHARGE:
                case WS_REJECT_SHIPPING_CHARGE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.REQUEST_PARAMS));
                    Common.showLog("qwerty"+map.get(NKeys.REQUEST_PARAMS));
                    break;
                case WS_ASSIGNDELIVERYAGENT:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.ASSIGNDELIVERYAGENT));
                    break;
                case WS_DELETEORDERFILE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.DELETEORDERFILE));
                    Common.showLog("data====" + map.get(NKeys.DELETEORDERFILE));
                    break;
                case WS_SENDORDERCOMMENT:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.SENDORDERCOMMENT));
                    break;
                case WS_GETORDERCOMMENT:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETORDERCOMMENT));
                    break;
                case WS_STARTLOADING:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.STARTLOADING));
                    break;
                case WS_CHANGEORDERSTATUS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.CHANGEORDERSTATUS));
                    break;
                case WS_GET_ANALYTICS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GET_ANALYTICS));
                    break;
                case WS_GET_ALL_SHIPPING_CHARGE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GET_ALL_SHIPPING_CHARGE));
                    break;
                case WS_GETSHIPPINGCHARGESTOADD:
                    b = loadBuilder.load("GET", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    break;
                case WS_GET_TRANSACTION_HISTORY:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GET_TRANSACTION_HISTORY));
                    break;
                case WS_EXPORTORDER:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.EXPORTORDER));
                    break;
                case WS_GETSINGLEUSERALLWORKLOGS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETSINGLEUSERALLWORKLOGS));
                    break;
                case WS_EXPORTWORKLOG:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.EXPORTWORKLOG));
                    break;
                case WS_GETWORKLOGDETAILS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETWORKLOGDETAILS));
                    break;
                case WS_DELETEWORKLOG:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.DELETEWORKLOG));
                    break;
                case WS_DELETEWORKLOGFILE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.DELETEWORKLOGFILE));
                    break;
                case WS_EXPORTALLWORKLOGS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.EXPORTALLWORKLOGS));
                    break;
                case WS_EXPORTCOMPLAINTS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.EXPORTCOMPLAINTS));
                    break;
                case WS_EXPORTSHIPPINGCHARGE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.EXPORTSHIPPINGCHARGE));
                case WS_NOTIFICATIONLISTINGFORORDER:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.NOTIFICATIONLISTINGFORORDER));
                    break;
                case WS_NOTIFICATIONUPDATE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.NOTIFICATIONUPDATE));
                    break;
                case WS_POSTATUSCHANGE:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.POSTATUSCHANGE));
                    break;
                case WS_UPDATEPOAOTRANSPORTATION:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.UPDATEPOAOTRANSPORTATION));
                    break;
                case WS_GETPOCODES:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETPOCODES));
                    break;
                case WS_ORDERANALYTICSDDATA:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.ORDERANALYTICSDDATA));
                    break;
                case WS_GETPRODUCTDETAILS:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.GETPRODUCTDETAILS));
                    break;
                case WS_SETELIGIBLEUSER:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.addHeader("Authorization", auth);
                    b.setStringBody((String) map.get(NKeys.SETELIGIBLEUSER));
                    break;
                case WS_LEADANALYTICSDATA:
                case WS_LEADCONVERSATIONDATA:
                case WS_SCANALYTICSDATA:
                case WS_DASHBOARANALYTICSDDATA:
                case WS_EXPORTLEADGENDATA:
                case WS_EXPORTLEADCONVDATA:
                case WS_EXPORTQUOTATIONANALYTICSDATA:
                case WS_GETNOTIFICATIONLISTINGFORTASK:
                case WS_GETNOTIFICATIONLISTINGFORRESPONSE:
                case WS_UPDATENOTIFICATION:
                case WS_NOTIFICATIONCOUNT:
                case WS_LOGOUT:
                    b = loadBuilder.load("POST", new ServiceUrls().getMETHODNAME(serviceMethods));
                    b.addHeader("Authorization", auth);
                    b.addHeader("Accept", "application/json");
                    b.addHeader("Content-Type", "application/json");
                    b.setStringBody((String) map.get(NKeys.DATA));
                    Common.showLog("DATA====" + (String) map.get(NKeys.DATA));
                    break;
            }

            Future<Response<String>> future = b.asString().withResponse();
            future.setCallback(new FutureCallback<Response<String>>() {

                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    try {
                        if (loaderUtils != null)
                            loaderUtils.dismiss();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    if (result == null) return;
                    try {
                        responseDO.setCode(result.getHeaders().code());
                        Common.showLog("STATUS CODE : " + result.getHeaders().code());
                        JSONObject jsonObject = new JSONObject(result.getResult().toString());
                        Common.showLog("STATUS CODE : ================" + responseDO.getServiceMethods() + result.getResult());
                        if (Integer.parseInt(jsonObject.optString("status")) >= 200 && Integer.parseInt(jsonObject.optString("status")) <= 299) {
                            responseDO.setError(false);
                        } else {
                            if (result.getHeaders().code() == 417 || result.getHeaders().code() == 403) {
                                responseDO.setError(true);
                                if (jsonObject.optString("message").equalsIgnoreCase("Invalid Token")) {
                                    new PreferenceUtils(context).clearAll();
                                    Common.showLog("error" + jsonObject.optString("message"));
                                    ((Activity) context).finishAffinity();
                                    context.startActivity(new Intent(context, LoginWithPasswordActivity.class));
                                    return;
                                }
                            } else if(result.getHeaders().code() == 401 && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data") != null && jsonObject.optJSONObject("data").optString("logout").equalsIgnoreCase("true")) {
                                Common.showToast(context, "Maximum device limit reached, Login again.");
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("users_device_token", MyFirebaseInstanceIDService.getDeviceRefreshToken());
                                Common.logout(context, dialog, new Gson().toJson(map));
                            }
                            responseDO.setError(true);
                            responseDO.setResponse(jsonObject.optString("message"));
                            responseListner.onResponseReceived(responseDO);
                            return;
                        }

                        responseDO.setResponse(result.getResult().toString());
                        responseListner.onResponseReceived(responseDO);
                    } catch (Exception e2) {
                        Log.e("Error", e2.getMessage().toString());
                        ResponseDO responseDO = new ResponseDO();
                        responseDO.setError(true);
                        responseDO.setResponse("Please try again");
                        responseListner.onResponseReceived(responseDO);
                    }

                }
            });
        }

    }

    private void getData(ResponseDO responseDO, String resp) {
        try {
            JSONObject jsonObject = new JSONObject(resp);
            if (jsonObject.has("data")) {
                responseDO.setResponse(jsonObject.optString("data", ""));
            } else {
                responseDO.setResponse(resp);
            }
            responseDO.setTotalPages(jsonObject.optInt("total_pages", 0));
            responseDO.setSection_type(jsonObject.optString("section_type", ""));
            responseDO.setApi_base_url(jsonObject.optString("api_base_url", ""));
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        responseDO.setResponse(resp);
    }

    public static class DProgressbar {
        Dialog progressDialog;

        public DProgressbar(Context context) {
            progressDialog = new Dialog(context);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.dialog_api_loading);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setCancelable(false);
        }

        public void show() {
            progressDialog.show();
        }

        public void dismiss() {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
