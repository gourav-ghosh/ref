package com.devstringx.mytylesstockcheck.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.ArchitectOrdersAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityArchitectOrdersBinding;
import com.devstringx.mytylesstockcheck.datamodal.ordersByArchitect.Data;
import com.devstringx.mytylesstockcheck.datamodal.ordersByArchitect.Response;
import com.devstringx.mytylesstockcheck.datamodal.ordersByArchitect.OrdersItem;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ArchitectOrdersActivity extends AppCompatActivity implements ArchitectOrdersAdapter.OnClick{
    ActivityArchitectOrdersBinding binding;
    private ArchitectOrdersAdapter architectOrdersAdapter;
    private Data item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_architect_orders);
        getArchitectOrders(getIntent().getStringExtra("architectId"));
    }

    private void getArchitectOrders(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("architectId", id);
        map.put(NKeys.GETORDERBYARCHITECT, new Gson().toJson(map));
        new NetworkRequest(this, this::onResponseReceived).callWebServices(ServiceMethods.WS_GETORDERBYARCHITECT, map, true);

    }

    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETORDERBYARCHITECT) {
                Response response = new Gson().fromJson(responseDO.getResponse(), Response.class);
                item = response.getData();
                setArchitectOrders(item);
            }
        }
        else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(responseDO.getResponse());
                Common.showToast(this, String.valueOf(jsonObject.optString("message", "")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void setArchitectOrders(Data item) {
        if(item.getArchitectName()!=null) binding.architectNameTV.setText(item.getArchitectName());
        binding.countTV.setText(String.valueOf(item.getCount()));
        if(item.getTotalOrderAmount()!=null) {
            binding.totalOrderAmountTV.setText(item.getTotalOrderAmount()+" /-");
        }
        else {
            binding.totalOrderAmountTV.setText("0.00 /-");
        }
        if(item.getOrders().size() > 0) {
            binding.ordersRV.setVisibility(View.VISIBLE);
            binding.noOrdersTV.setVisibility(View.GONE);
            architectOrdersAdapter = new ArchitectOrdersAdapter(this, item.getOrders(),this);
            binding.ordersRV.setHasFixedSize(true);
            binding.ordersRV.setLayoutManager(new LinearLayoutManager(this));
            binding.ordersRV.setAdapter(architectOrdersAdapter);
        } else {
            binding.ordersRV.setVisibility(View.GONE);
            binding.noOrdersTV.setVisibility(View.VISIBLE);
        }

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClickListener(String id) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("source_page","architect_order");
        intent.putExtra("orderId",id);
        Common.showLog("ArchitectOrderDrtails======"+intent.getStringExtra("source_page")+"      "+intent.getStringExtra("orderId"));
        startActivityForResult(intent,100);
    }
}