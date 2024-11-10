package com.devstringx.mytylesstockcheck.webservices;



import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

public class ResponseHandler {
    JSONObject jsonObj;

    //{"status":200,"msg":"Reply Comment Added Successfully"}
    public Object parseData(ServiceMethods serviceMethods, String response) {
        Object data = null;
        Gson gson = new Gson();
        try {
            jsonObj = new JSONObject(response);
            JSONObject statusObj = jsonObj.getJSONObject("status");
            if (statusObj.optInt("code") == 1) {
                switch (serviceMethods) {


                }
            } else {
                //{"status":{"code":0,"msg":"Requirement not fullfill!"},"data":"forcenumber CRPF  already exists., "}
                switch (serviceMethods) {

                }

            }
        } catch (JsonSyntaxException e) {

        } catch (Exception e) {
            e.printStackTrace();
            data = null;
        }
        return data;
    }


}
