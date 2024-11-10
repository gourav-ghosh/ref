package com.devstringx.mytylesstockcheck.screens.orderFilters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityOrderFilterBinding;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderFilterActivity extends AppCompatActivity implements ResponseListner {
    ActivityOrderFilterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_order_filter);
        binding.closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.orderDate.setChecked(true);
        if (binding.orderDate.isChecked()) Common.loadFragment(getSupportFragmentManager(),new OrderDateFragment(),R.id.orderfilter_frag_container);
        binding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (binding.orderDate.isChecked()){
                    Common.loadFragment(getSupportFragmentManager(),new OrderDateFragment(),R.id.orderfilter_frag_container);
                } else if (binding.orderStatus.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(),new OrderStatusFragment(),R.id.orderfilter_frag_container);
                } else if (binding.deliveryType.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(),new DeliveryTypeFragment(),R.id.orderfilter_frag_container);
                } else if (binding.orderType.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(),new OrderTypeFragment(),R.id.orderfilter_frag_container);
                }else if (binding.paymentMode.isChecked()){
                    Common.loadFragment(getSupportFragmentManager(),new PaymentModesFragment(),R.id.orderfilter_frag_container);
                } else if (binding.poCodes.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(),new POCodesFragment(),R.id.orderfilter_frag_container);
                } else if (binding.disManager.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(),new DisManagerFragment(),R.id.orderfilter_frag_container);
                } else if (binding.salesPerson.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(),new OrderSalesPersonFragment(),R.id.orderfilter_frag_container);
                } else if (binding.deliveryAgent.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(),new OrderDeliveryAgentFragment(),R.id.orderfilter_frag_container);
                }else if (binding.sortBy.isChecked()){
                    Common.loadFragment(getSupportFragmentManager(),new OrderSortByFragment(),R.id.orderfilter_frag_container);
                }
            }
        });
        binding.clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.orderDateType="";
                Common.orderStartDate="";
                Common.orderEndDate="";
                Common.selectedOrderStatus=new ArrayList<>();
                Common.selectedDeliveryType=new ArrayList<>();
                Common.selectedOrderType=new ArrayList<>();
                Common.selectedOrderPaymentMode=new ArrayList<>();
                Common.selectedPOCodes=new ArrayList<>();
                Common.selectedOrderDisManagerName=new ArrayList<>();
                Common.selectedOrderSupExeName=new ArrayList<>();
                Common.selectedOrderDeliveryAgentName=new ArrayList<>();
                Common.orderFilterSortby="createdAtDesc";
                Common.tempOrderDateType="";
                Common.tempSelectedOrderStatus=new ArrayList<>();
                Common.tempSelectedDeliveryType=new ArrayList<>();
                Common.tempSelectedOrderType=new ArrayList<>();
                Common.tempSelectedOrderPaymentMode=new ArrayList<>();
                Common.tempSelectedPOCodes=new ArrayList<>();
                Common.tempSelectedOrderDisManagerName=new ArrayList<>();
                Common.tempSelectedOrderSupExeName=new ArrayList<>();
                Common.tempSelectedOrderDeliveryAgentName=new ArrayList<>();
                Common.tempOrderSortby="createdAtDesc";

                callApi();
            }
        });
        binding.applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.orderDateType=Common.tempOrderDateType;
                Common.selectedOrderStatus=Common.tempSelectedOrderStatus;
                Common.selectedDeliveryType=Common.tempSelectedDeliveryType;
                Common.selectedOrderType=Common.tempSelectedOrderType;
                Common.selectedOrderPaymentMode=Common.tempSelectedOrderPaymentMode;
                Common.selectedPOCodes=Common.tempSelectedPOCodes;
                Common.selectedOrderDisManagerName=Common.tempSelectedOrderDisManagerName;
                Common.selectedOrderSupExeName=Common.tempSelectedOrderSupExeName;
                Common.selectedOrderDeliveryAgentName=Common.tempSelectedOrderDeliveryAgentName;
                Common.orderFilterSortby=Common.tempOrderSortby;

                if (Common.orderDateType.equalsIgnoreCase("Custom")) {
                    if (Common.orderStartDate.isEmpty()) {
                        Common.showToast(getBaseContext(), "From Date should not be empty");
                        return;
                    } else if (Common.orderEndDate.isEmpty()) {
                        Common.showToast(getBaseContext(), "To Date should not be empty");
                        return;
                    } else if (!Common.dateValidator(Common.orderStartDate,Common.orderEndDate)) {
                        Common.showToast(getBaseContext(), "from date should not be greater than to date");
                        return;
                    }
                }
                callApi();
            }
        });
    }

    private void callApi() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("search", Common.orderSearch);
        map.put("sort",Common.orderFilterSortby.toString());
        map.put("dateRange", Common.orderDateType.toString());
        map.put("fromDate", Common.orderStartDate.toString());
        map.put("toDate", Common.orderEndDate.toString());
        map.put("orderStatus", Common.selectedOrderStatus);
        map.put("paymentMode", Common.selectedOrderPaymentMode);
        map.put("poCodes", Common.selectedPOCodes);
        map.put("orderType", Common.selectedOrderType);
        map.put("deliveryType", Common.selectedDeliveryType);
        map.put("deliveryAgent", Common.selectedOrderDeliveryAgentName);
        map.put("saleExecutive", Common.selectedOrderSupExeName);
        map.put("manager", Common.selectedOrderDisManagerName);
        map.put("statusTab", Common.OrderSelectedTabName);
        map.put("statusTab2", Common.OutstationSelectedSubtab);

        Common.showLog("RESPONSE===+++++++" + map);
        Intent intent = new Intent();
        intent.putExtra("OrderFilterDataMap",new Gson().toJson(map));
        setResult(RESULT_OK,intent);
        finish();
    }

    public void checkIsAnyFIlterSelected() {
        if (Common.orderDateType.isEmpty()  && Common.selectedOrderDisManagerName.isEmpty()
                && Common.selectedOrderSupExeName.isEmpty() && Common.selectedOrderDeliveryAgentName.isEmpty()
        && Common.selectedPOCodes.isEmpty() && Common.selectedOrderType.isEmpty()
        && Common.selectedOrderPaymentMode.isEmpty() && Common.selectedDeliveryType.isEmpty()
        && Common.selectedOrderStatus.isEmpty() && Common.orderFilterSortby.equalsIgnoreCase("createdAtDesc"))
            binding.clearFilter.setVisibility(View.INVISIBLE);
        else binding.clearFilter.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {

    }
}