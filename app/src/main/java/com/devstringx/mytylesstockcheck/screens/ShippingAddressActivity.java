package com.devstringx.mytylesstockcheck.screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AddressListAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.MyBroadcastReceiver;
import com.devstringx.mytylesstockcheck.databinding.ActivityShippingAddressBinding;
import com.devstringx.mytylesstockcheck.datamodal.allShippingAddress.AllShippingAddressData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.DataResponse;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Record;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.ShippingAddresses;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;
import com.nimbusds.jose.shaded.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShippingAddressActivity extends AppCompatActivity implements AddressListAdapter.AddressListClick, ResponseListner {
    ActivityShippingAddressBinding binding;
    private AddressListAdapter adapter;
    private ArrayList<ShippingAddresses> recordsItems=new ArrayList<>();
    private MyBroadcastReceiver myReceiver=new MyBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shipping_address);
        adapter = new AddressListAdapter(this, recordsItems, this);
        binding.addressRv.setHasFixedSize(true);
        binding.addressRv.setLayoutManager(new LinearLayoutManager(this));
        binding.addressRv.setAdapter(adapter);
        binding.titleTv.setText("Shipping Addresses");
        binding.addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShippingAddressActivity.this, BillingAddressActivity.class);
                intent.putExtra("type", "shipping");
                intent.putExtra("lead_id", getIntent().getIntExtra("lead_id", 0));
                intent.putExtra("site_in_charge_mobile_number", getIntent().getStringExtra("site_in_charge_mobile_number"));
                startActivityForResult(intent, 3000);
            }
        });
        getAddress(getIntent().getIntExtra("lead_id", 0));

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        Common.receivedSocketMessage(ShippingAddressActivity.this);
    }

    private void saveAddress() {
        boolean b=true;
        for (int i = 0; i < recordsItems.size(); i++) {
            if(recordsItems.get(i).isAsDefault()){
                makeDefaultAddress(getIntent().getIntExtra("lead_id",0),recordsItems.get(i).getShippingAddressId());
                b=false;
                break;
            }
        }
        if(b){
            saveShippingAddress();
        }
    }
    public void saveShippingAddress(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("lead_id", getIntent().getIntExtra("lead_id", 0));
        map.put("shipping_addresses",recordsItems);
        Common.showLog("SHIPPING======"+new Gson().toJson(map));
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put(NKeys.ADDRESSDATA, new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_ADDSHIPPINGADDRESS, dataMap, true);

    }

    private void getAddress(int Id) {
        String data = "{" +
                " \"lead_id\":"+Id +
                "}";
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.ADDRESSDATA, data);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETLEADSHIPPINGADDRESS, map, true);
    }

    private void makeDefaultAddress(int Id,int shippingAddId) {
        String data = "{" +
                " \"lead_id\":"+Id +","+
                "\"shipping_address_id\":"+shippingAddId+
                "}";
        Common.showLog("DEFAULT===="+data);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.ADDRESSDATA, data);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_MAKEDEFAULTSHIPPINGADDRESS, map, true);
    }
    private void removeAddress(int Id,int shippingAddId) {
        String data = "{" +
                " \"lead_id\":"+Id +","+
                "\"shipping_address_id\":"+shippingAddId+
                "}";
        Common.showLog("DEFAULT===="+data);
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.ADDRESSDATA, data);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_REMOVESHIPPINGADDRESS, map, true);
    }
    @Override
    public void onClickListner(int position, String type) {
        if(type.equalsIgnoreCase("edit")){
            Intent intent=new Intent(ShippingAddressActivity.this, BillingAddressActivity.class);
            intent.putExtra("type","shipping");
            intent.putExtra("data",recordsItems.get(position));
            intent.putExtra("lead_id",getIntent().getIntExtra("lead_id",0));
            startActivityForResult(intent,3000);
        }else if(type.equalsIgnoreCase("default")){
            for (int i = 0; i < recordsItems.size(); i++) {
                recordsItems.get(i).setAsDefault(false);
            }
            recordsItems.get(position).setAsDefault(true);
            adapter.refreshList(recordsItems);
            makeDefaultAddress(getIntent().getIntExtra("lead_id",0),recordsItems.get(position).getShippingAddressId());
        }else if(type.equalsIgnoreCase("remove")){
            ShippingAddresses address=recordsItems.remove(position);
            adapter.refreshList(recordsItems);
            removeAddress(getIntent().getIntExtra("lead_id",0),address.getShippingAddressId());
        }
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if(responseDO.getServiceMethods()== ServiceMethods.WS_GETLEADSHIPPINGADDRESS) {
                AllShippingAddressData shippingAddressData = new Gson().fromJson(responseDO.getResponse(), AllShippingAddressData.class);
                recordsItems = (ArrayList<ShippingAddresses>) shippingAddressData.getData().getShippingAddresses();
                for (int i = 0; i < recordsItems.size(); i++) {
                    recordsItems.get(i).setId(String.valueOf(recordsItems.get(i).getShippingAddressId()));
                    recordsItems.get(i).setCity(String.valueOf(recordsItems.get(i).getShippingCityId()));
                    recordsItems.get(i).setState(String.valueOf(recordsItems.get(i).getShippingStateId()));
                    recordsItems.get(i).setPincode(String.valueOf(recordsItems.get(i).getShippingPincode()));
                }
                adapter.refreshList(recordsItems);
                if(recordsItems.size()==0){
                    binding.noAddressRecordTv.setVisibility(View.VISIBLE);
                }else{
                    binding.noAddressRecordTv.setVisibility(View.GONE);
                }
            }else if(responseDO.getServiceMethods()== ServiceMethods.WS_MAKEDEFAULTSHIPPINGADDRESS){
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(ShippingAddressActivity.this,jsonObject.optString("message"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }else if(responseDO.getServiceMethods()== ServiceMethods.WS_REMOVESHIPPINGADDRESS){
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(responseDO.getResponse());
                    Common.showToast(ShippingAddressActivity.this,jsonObject.optString("message"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_ADDSHIPPINGADDRESS) {
                setResult(RESULT_OK);
                finish();
            }
        }else{
            Common.showToast(ShippingAddressActivity.this,responseDO.getResponse());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getAddress(getIntent().getIntExtra("lead_id",0));
    }
}