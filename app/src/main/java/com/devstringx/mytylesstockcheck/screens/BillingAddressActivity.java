package com.devstringx.mytylesstockcheck.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AutoCompleteAdapter;
import com.devstringx.mytylesstockcheck.adapter.CustomAutoCompleteListAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.MyBroadcastReceiver;
import com.devstringx.mytylesstockcheck.common.StateCityPickerCallbacks;
import com.devstringx.mytylesstockcheck.common.StateCityPickerDeialog;
import com.devstringx.mytylesstockcheck.databinding.ActivityBillingAddressBinding;
import com.devstringx.mytylesstockcheck.datamodal.allLeads.BillingAddressItem;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.DataResponse;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.ShippingAddresses;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;
import com.nimbusds.jose.shaded.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BillingAddressActivity extends AppCompatActivity implements ResponseListner {
    private ActivityBillingAddressBinding binding;
    private List<RecordsItem> cityList = new ArrayList<>();
    private List<RecordsItem> stateList = new ArrayList<>();

    private BillingAddressItem record;
    private ShippingAddresses shippingAddresses;
    private MyBroadcastReceiver myReceiver=new MyBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_billing_address);

        binding.titleTv.setText("Add Billing Address");

        getStates();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent().getStringExtra("type").equalsIgnoreCase("billing")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                record = getIntent().getSerializableExtra("data",BillingAddressItem.class);
            }else{
                record = (BillingAddressItem) getIntent().getSerializableExtra("data");
            }
            if (record != null) {
                binding.titleTv.setText("Edit Billing Address");
                binding.address1ET.setText(record.address_line_1);
                binding.address2ET.setText(record.address_line_2);
                binding.zipcodeET.setText(String.valueOf(record.billing_pincode));
                binding.enterGst.setText(record.gst_number);
                binding.selectCity.setText(record.billing_city);
                binding.selectState.setText(record.billing_state);
            }
        } else {
            binding.titleTv.setText("Add Shipping Address");
            binding.landmarkTl.setVisibility(View.VISIBLE);
            binding.siteinchargemobTl.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                shippingAddresses = getIntent().getSerializableExtra("data",ShippingAddresses.class);
            }else{
                shippingAddresses = (ShippingAddresses) getIntent().getSerializableExtra("data");
            }
            if (shippingAddresses != null) {
                binding.titleTv.setText("Edit Shipping Address");
                binding.address1ET.setText(shippingAddresses.getAddressLine1());
                binding.address2ET.setText(shippingAddresses.getAddressLine2());
                binding.zipcodeET.setText(String.valueOf(shippingAddresses.getShippingPincode()));
                binding.enterGst.setText(shippingAddresses.getGstNumber());
                binding.selectCity.setText(shippingAddresses.getShippingCity());
                binding.selectState.setText(shippingAddresses.getShippingState());
                binding.landmarkEd.setText(shippingAddresses.getLandmark());
                binding.siteinchargemobEt.setText(shippingAddresses.getSiteInChargeMobileNumber());
            }
        }

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBillingAddress();
            }
        });

        binding.selectState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StateCityPickerDeialog pickerDeialog=new StateCityPickerDeialog(BillingAddressActivity.this, stateList, false, new StateCityPickerCallbacks() {
                    @Override
                    public void onStateCitySelected(RecordsItem recordsItem) {
                        for (int i = 0; i < stateList.size(); i++) {
                            stateList.get(i).setSelected(false);
                            if(recordsItem.getId().equalsIgnoreCase(stateList.get(i).getId())){
                                stateList.get(i).setSelected(true);
                            }
                        }
                        binding.selectState.setText(recordsItem.getStateName());
                        binding.selectCity.setText("");
                        getCities(recordsItem.getId());
                        Common.showLog("stateid===="+recordsItem.getId());
                    }
                });
                pickerDeialog.show();
            }
        });
        binding.selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StateCityPickerDeialog pickerDeialog=new StateCityPickerDeialog(BillingAddressActivity.this, cityList, true, new StateCityPickerCallbacks() {
                    @Override
                    public void onStateCitySelected(RecordsItem recordsItem) {
                        for (int j = 0; j < cityList.size(); j++) {
                            cityList.get(j).setSelected(false);
                            if(recordsItem.getId().equalsIgnoreCase(cityList.get(j).getId())){
                                cityList.get(j).setSelected(true);
                            }
                        }
                        binding.selectCity.setText(recordsItem.getCityName());
                        Common.showLog("cityid===="+recordsItem.getId());
                    }
                });
                pickerDeialog.show();
            }
        });


    }

    private void saveBillingAddress() {
        if (binding.address1ET.getText().toString().trim().isEmpty()) {
            Common.showToast(this, getString(R.string.addressline1_error_msg));
            return;
        } else if (binding.address2ET.getText().toString().trim().isEmpty()) {
            Common.showToast(this, getString(R.string.addressline2_error_msg));
            return;
        } else if (binding.selectCity.getText().toString().trim().isEmpty()) {
            Common.showToast(this, getString(R.string.city_error_msg));
            return;
        } else if (binding.selectState.getText().toString().trim().isEmpty()) {
            Common.showToast(this, getString(R.string.state_error_msg));
            return;
        } else if (binding.zipcodeET.getText().toString().trim().isEmpty()) {
            Common.showToast(this, getString(R.string.zipcode_error_msg));
            return;
        }else if (binding.zipcodeET.getText().toString().trim().length()<6) {
            Common.showToast(this, getString(R.string.zipcode_error_msg2));
            return;
        }

        String cityid = "", stateId = "";
        for (int j = 0; j < cityList.size(); j++) {
            if (cityList.get(j).isSelected()) {
                cityid = cityList.get(j).getId();
                break;
            }
        }

        for (int j = 0; j < stateList.size(); j++) {
            if (stateList.get(j).isSelected()) {
                stateId = stateList.get(j).getId();
                break;
            }
        }
        if (stateId == "") {
            return;
        }
        if (cityid == "") {
            return;
        }
        if (getIntent().getStringExtra("type").equalsIgnoreCase("billing")) {
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("city_id", Integer.parseInt(cityid));
            map1.put("state_id", Integer.parseInt(stateId));
            map1.put("pincode", binding.zipcodeET.getText().toString().trim());
            map1.put("gst_number", binding.enterGst.getText().toString().trim());
            map1.put("address_line_1", binding.address1ET.getText().toString().trim());
            map1.put("address_line_2", binding.address2ET.getText().toString().trim());
            map1.put("lead_id", getIntent().getIntExtra("lead_id", 0));
            map1.put("landmark", "");
            map1.put("site_in_charge_mobile_number", getIntent().getStringExtra("site_in_charge_mobile_number"));

            HashMap<String, Object> map = new HashMap<>();
            map.put(NKeys.ADDRESSDATA, new Gson().toJson(map1));
            if (record == null) {
                new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_ADDBILLINGADDRESS, map, true);
            } else {
                new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_UPDATEBILLINGADDRESS, map, true);
            }
        } else {

            HashMap<String, Object> map = new HashMap<>();
            map.put("lead_id", getIntent().getIntExtra("lead_id", 0));
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("city_id", Integer.parseInt(cityid));
            map1.put("state_id", Integer.parseInt(stateId));
            map1.put("pincode", binding.zipcodeET.getText().toString().trim());
            map1.put("gst_number", binding.enterGst.getText().toString().trim());
            map1.put("address_line_1", binding.address1ET.getText().toString().trim());
            map1.put("address_line_2", binding.address2ET.getText().toString().trim());


            if (shippingAddresses != null) {
                map1.put("id", shippingAddresses.getShippingAddressId());
            }else{
                map1.put("id", "");
            }
            map1.put("site_in_charge_mobile_number", binding.siteinchargemobEt.getText().toString());
            map1.put("landmark", binding.landmarkEd.getText().toString());
            JSONArray jsonArray=new JSONArray();
            jsonArray.add(map1);
            map.put("shipping_addresses",jsonArray);
            Common.showLog("SHIPPING======"+new Gson().toJson(map));
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put(NKeys.ADDRESSDATA, new Gson().toJson(map));
            new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_ADDSHIPPINGADDRESS, dataMap, true);
        }


    }

    private void getCities(String id) {
        String data = "{" +
                " \"state_id\":"+id +
                "}";
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.CITIESDATA, data);
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETCITIES, map, true);
    }


    private void getStates() {
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_GETSTATES, null, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {

        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETCITIES) {
                DataResponse cityDataResponse = new Gson().fromJson(responseDO.getResponse(), DataResponse.class);
                cityList = cityDataResponse.getData().getRecords();
                if (record != null) {
                    for (int i = 0; i < cityList.size(); i++) {
                        if (String.valueOf(record.getBilling_city_id()).equalsIgnoreCase(cityList.get(i).getId())) {
                            cityList.get(i).setSelected(true);
                            Common.showLog("SELECTED CITYID===" + cityList.get(i).getId());
                        }
                    }
                }
                if (shippingAddresses != null) {
                    for (int i = 0; i < cityList.size(); i++) {
                        if (String.valueOf(shippingAddresses.getShippingCityId()).equalsIgnoreCase(cityList.get(i).getId())) {
                            cityList.get(i).setSelected(true);
                            Common.showLog("SELECTED CITYID===" + cityList.get(i).getId());
                        }
                    }
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_GETSTATES) {
                DataResponse stateDataResponse = new Gson().fromJson(responseDO.getResponse(), DataResponse.class);
                stateList = stateDataResponse.getData().getRecords();
                if (record != null) {
                    for (int i = 0; i < stateList.size(); i++) {
                        if (String.valueOf(record.getBilling_state_id()).equalsIgnoreCase(stateList.get(i).getId())) {
                            stateList.get(i).setSelected(true);
                            Common.showLog("SELECTED STATEID===" + stateList.get(i).getId());
                            getCities(stateList.get(i).getId());
                        }
                    }
                }
                if (shippingAddresses != null) {
                    for (int i = 0; i < stateList.size(); i++) {
                        if (String.valueOf(shippingAddresses.getShippingStateId()).equalsIgnoreCase(stateList.get(i).getId())) {
                            stateList.get(i).setSelected(true);
                            Common.showLog("SELECTED STATEID===" + stateList.get(i).getId());
                            getCities(stateList.get(i).getId());
                        }
                    }
                }
            } else if (responseDO.getServiceMethods() == ServiceMethods.WS_ADDBILLINGADDRESS ||
                    responseDO.getServiceMethods() == ServiceMethods.WS_UPDATEBILLINGADDRESS||
                    responseDO.getServiceMethods() == ServiceMethods.WS_ADDSHIPPINGADDRESS) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}