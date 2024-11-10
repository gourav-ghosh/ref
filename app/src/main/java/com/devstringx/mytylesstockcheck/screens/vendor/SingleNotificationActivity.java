package com.devstringx.mytylesstockcheck.screens.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivitySingleNotificationBinding;
import com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationDetails.VendorQuoteDetailRecords;
import com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationDetails.VendorQuoteDetailResponse;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SingleNotificationActivity extends AppCompatActivity implements ResponseListner {
    ActivitySingleNotificationBinding activitySingleNotificationBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySingleNotificationBinding= DataBindingUtil.setContentView(this,R.layout.activity_single_notification);
        if(getIntent().getBooleanExtra("background",false)){
            activitySingleNotificationBinding.rootLl.setBackgroundColor(getColor(R.color.darker_Blue));
        }else{
            activitySingleNotificationBinding.logo.setVisibility(View.GONE);
        }
        Common.showLog(getIntent().getStringExtra("title")+getIntent().getStringExtra("body")+getIntent().getStringExtra("id"));
        getAllInquiries(getIntent().getStringExtra("id"));

        activitySingleNotificationBinding.dialogUi.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }

    private void setData(VendorQuoteDetailRecords records) {
        activitySingleNotificationBinding.dialogUi.inquiryNumberTv.setText(records.getEnquiryNo());
        activitySingleNotificationBinding.dialogUi.timeDescriptionTv.setText(records.getProductName());
        activitySingleNotificationBinding.dialogUi.quantityTv.setText(records.getQuantity()+" "+records.getUnitOfMeasurement());

        String url = (!records.getProductAttachment().isEmpty()) ? records.getProductAttachment().get(0) : "";
       Common.showLog("NOTI IMG URL===="+url);
        Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(activitySingleNotificationBinding.dialogUi.tilesIv);

        activitySingleNotificationBinding.dialogUi.notAvailableIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activitySingleNotificationBinding.dialogUi.notAvailableResponseLl.setVisibility(View.VISIBLE);
                activitySingleNotificationBinding.dialogUi.availabilityLl.setVisibility(View.GONE);
            }
        });
        activitySingleNotificationBinding.dialogUi.availableIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", getIntent().getStringExtra("id"));
                map.put("stock_check_status", "Available");
                updateInquiryStatus(map);
            }
        });


        activitySingleNotificationBinding.dialogUi.rb1DaysIv.setSelected(true);
        activitySingleNotificationBinding.dialogUi.rb1DaysIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!activitySingleNotificationBinding.dialogUi.rb1DaysIv.isSelected()) {
                    activitySingleNotificationBinding.dialogUi.rb1DaysIv.setImageResource(R.drawable.ic_checked_radio_button);
                    activitySingleNotificationBinding.dialogUi.rb2BoxesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    activitySingleNotificationBinding.dialogUi.rb3MultipleBatchesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    activitySingleNotificationBinding.dialogUi.rb1DaysIv.setSelected(true);
                    activitySingleNotificationBinding.dialogUi.rb2BoxesIv.setSelected(false);
                    activitySingleNotificationBinding.dialogUi.rb3MultipleBatchesIv.setSelected(false);
                }

            }
        });
        activitySingleNotificationBinding.dialogUi.rb2BoxesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!activitySingleNotificationBinding.dialogUi.rb2BoxesIv.isSelected()) {
                    activitySingleNotificationBinding.dialogUi.rb1DaysIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    activitySingleNotificationBinding.dialogUi.rb2BoxesIv.setImageResource(R.drawable.ic_checked_radio_button);
                    activitySingleNotificationBinding.dialogUi.rb3MultipleBatchesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    activitySingleNotificationBinding.dialogUi.rb1DaysIv.setSelected(false);
                    activitySingleNotificationBinding.dialogUi.rb2BoxesIv.setSelected(true);
                    activitySingleNotificationBinding.dialogUi.rb3MultipleBatchesIv.setSelected(false);
                }
            }
        });
        activitySingleNotificationBinding.dialogUi.rb3MultipleBatchesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!activitySingleNotificationBinding.dialogUi.rb3MultipleBatchesIv.isSelected()) {
                    activitySingleNotificationBinding.dialogUi.rb1DaysIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    activitySingleNotificationBinding.dialogUi.rb2BoxesIv.setImageResource(R.drawable.ic_unchecked_radio_button);
                    activitySingleNotificationBinding.dialogUi.rb3MultipleBatchesIv.setImageResource(R.drawable.ic_checked_radio_button);
                    activitySingleNotificationBinding.dialogUi.rb1DaysIv.setSelected(false);
                    activitySingleNotificationBinding.dialogUi.rb2BoxesIv.setSelected(false);
                    activitySingleNotificationBinding.dialogUi.rb3MultipleBatchesIv.setSelected(true);
                }
            }
        });
        activitySingleNotificationBinding.dialogUi.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activitySingleNotificationBinding.dialogUi.notAvailableResponseLl.setVisibility(View.GONE);
                activitySingleNotificationBinding.dialogUi.availabilityLl.setVisibility(View.VISIBLE);
            }
        });
        activitySingleNotificationBinding.dialogUi.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("id", getIntent().getStringExtra("id"));
                if (activitySingleNotificationBinding.dialogUi.rb1DaysIv.isSelected()) {
                    if (!activitySingleNotificationBinding.dialogUi.daysNumEt.getText().toString().isEmpty()) {
                        if (Integer.parseInt(activitySingleNotificationBinding.dialogUi.daysNumEt.getText().toString()) > 0) {
                            Common.showLog("days" + activitySingleNotificationBinding.dialogUi.daysNumEt.getText());
                            map.put("stock_check_status", "Not Available");
                            map.put("not_available_in_days", activitySingleNotificationBinding.dialogUi.daysNumEt.getText().toString());
                            activitySingleNotificationBinding.dialogUi.notAvailableResponseLl.setVisibility(View.GONE);
                            updateInquiryStatus(map);
                        } else
                            Toast.makeText(SingleNotificationActivity.this, "entered value must be greater than 0", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(SingleNotificationActivity.this, "please enter the value of selected item", Toast.LENGTH_SHORT).show();
                } else if (activitySingleNotificationBinding.dialogUi.rb2BoxesIv.isSelected()) {
                    if (!activitySingleNotificationBinding.dialogUi.boxesNumEt.getText().toString().isEmpty()) {
                        if (Integer.parseInt(String.valueOf(activitySingleNotificationBinding.dialogUi.boxesNumEt.getText())) > 0) {
                            Common.showLog("box" + activitySingleNotificationBinding.dialogUi.boxesNumEt.getText());
                            map.put("stock_check_status", "Not Available");
                            map.put("not_available_quantity", activitySingleNotificationBinding.dialogUi.boxesNumEt.getText().toString());
                            activitySingleNotificationBinding.dialogUi.notAvailableResponseLl.setVisibility(View.GONE);
                            updateInquiryStatus(map);
                        } else
                            Toast.makeText(SingleNotificationActivity.this, "entered value must be greater than 0", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(SingleNotificationActivity.this, "please enter the value of selected item", Toast.LENGTH_SHORT).show();
                } else if (activitySingleNotificationBinding.dialogUi.rb3MultipleBatchesIv.isSelected()) {
                    Common.showLog("multipleBatches");
                    map.put("stock_check_status", "Available Multiple Batches");
                    activitySingleNotificationBinding.dialogUi.notAvailableResponseLl.setVisibility(View.GONE);
                    updateInquiryStatus(map);
                }
            }
        });
    }

    private void updateInquiryStatus(HashMap<String, Object> map) {
        Common.showLog("====="+new Gson().toJson(map));
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put(NKeys.UPDATEQUOTATIONPRODUCTSTATUS,new Gson().toJson(map));
        new NetworkRequest(this, this).callWebServices(ServiceMethods.WS_UPDATEQUOTATIONPRODUCTSTATUS, map1, true);
    }
    private void getAllInquiries(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("search", "");
        map.put("id", id);
        map.put("sort", "");
        map.put("action", "");
        map.put("quantity", "");
        map.put("enquiry_status", "");
        HashMap<String, Object> date = new HashMap<>();
        date.put("type","");
        date.put("startDate","");
        date.put("endDate","");
        map.put("dateRange",date);
        map.put("enquiry_tab", "");
        map.put(NKeys.GETVENDORQUOTATIONS,new Gson().toJson(map));
        new NetworkRequest(this,this).callWebServices(ServiceMethods.WS_GETVENDORQUOTATIONS, map, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETVENDORQUOTATIONS) {
                VendorQuoteDetailResponse response = new Gson().fromJson(responseDO.getResponse(), VendorQuoteDetailResponse.class);
                setData(response.getData().getRecords());
            }else if (responseDO.getServiceMethods() == ServiceMethods.WS_UPDATEQUOTATIONPRODUCTSTATUS) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    Toast.makeText(SingleNotificationActivity.this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                finish();
            }
        }
    }


}