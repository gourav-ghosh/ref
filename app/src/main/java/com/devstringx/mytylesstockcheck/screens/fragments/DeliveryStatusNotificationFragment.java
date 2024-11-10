package com.devstringx.mytylesstockcheck.screens.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.DeliveryStatusNotificationListingAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentDeliveryStatusNotificationBinding;
import com.devstringx.mytylesstockcheck.datamodal.ordersNotification.OrderNotificationResponse;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeliveryStatusNotificationFragment extends Fragment implements ResponseListner , DeliveryStatusNotificationListingAdapter.OnClick{
    FragmentDeliveryStatusNotificationBinding binding;
    DeliveryStatusNotificationListingAdapter adapter;
    String user_id;
    String id;
    String notificationType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_delivery_status_notification, container, false);
        View view = binding.getRoot();
        binding.refreshLl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callOrderNotificationOrder();
            }
        });
        callOrderNotificationOrder();
        setupAdapter();
        return view;
    }

    private void callOrderNotificationOrder() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("user_id", new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.USERID,"").toString());
        map.put(NKeys.NOTIFICATIONLISTINGFORORDER, new Gson().toJson(map));
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_NOTIFICATIONLISTINGFORORDER, map, false);
    }

    private void setupAdapter() {
        adapter = new DeliveryStatusNotificationListingAdapter(getContext(),this);
        binding.deliveryStatusRV.setHasFixedSize(true);
        binding.deliveryStatusRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.deliveryStatusRV.setAdapter(adapter);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if(binding.refreshLl.isRefreshing())binding.refreshLl.setRefreshing(false);
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_NOTIFICATIONLISTINGFORORDER) {
                OrderNotificationResponse orderNotificationResponse = new Gson().fromJson(responseDO.getResponse(), OrderNotificationResponse.class);
                adapter.refreshList(orderNotificationResponse.getData().getRecords());
            }else if(responseDO.getServiceMethods() == ServiceMethods.WS_NOTIFICATIONUPDATE) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("orderId",id);
                intent.putExtra("type",notificationType);
                startActivityForResult(intent,800);
                callOrderNotificationOrder();
            }
        }
    }

    @Override
    public void onCLickListener(String user_id, String id, String notificationType) {
        callNotificationReadApi(user_id);
        this.id=id;
        this.user_id=user_id;
        this.notificationType=notificationType;
    }

    private void callNotificationReadApi(String id) {
        HashMap<String, Object> map = new HashMap<>();
        List<String> idArray = new ArrayList<>();
        idArray.add(id);
        map.put("id", idArray);
        map.put("action", "unread");
        map.put("notification_type", "notification order");
        map.put(NKeys.NOTIFICATIONUPDATE, new Gson().toJson(map));
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_NOTIFICATIONUPDATE, map, true);
    }
}