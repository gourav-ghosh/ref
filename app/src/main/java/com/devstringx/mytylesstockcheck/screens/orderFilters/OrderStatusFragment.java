package com.devstringx.mytylesstockcheck.screens.orderFilters;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.NameListWithCheckboxAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentOrderStatusBinding;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderStatusFragment extends Fragment implements ResponseListner , NameListWithCheckboxAdapter.FilterCBSelected{
    FragmentOrderStatusBinding binding;
    private List<RecordsItem> allOwnersList = new ArrayList<>();
    NameListWithCheckboxAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_status, container, false);
        View view = binding.getRoot();
        getAllOrderStatus();
        return view;
    }
    private void setupAdapter() {
        RecyclerView recyclerView = binding.orderFilterStatusRv;
        adapter = new NameListWithCheckboxAdapter(getContext(),allOwnersList,this::onItemSelectedListner, new ArrayList<>(Common.selectedOrderStatus));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void getAllOrderStatus() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("filter_value", "orderStatus");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETORDERFILTERVALUE, jsonObject.toString());
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_GETORDERFILTERVALUE, map, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETORDERFILTERVALUE) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    JSONArray recordsArray = jsonObject.getJSONObject("data").getJSONArray("records");
                    allOwnersList = new ArrayList<>();
                    for (int i = 0; i < recordsArray.length(); i++) {
                        RecordsItem recordsItem = new RecordsItem();
                        recordsItem.setCityName(recordsArray.getString(i).toString());
                        allOwnersList.add(recordsItem);
                    }
                    setupAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onItemSelectedListner(List<String> selected_items) {
        Common.tempSelectedOrderStatus=selected_items;
        Common.isOrderListingFilterSelected(getActivity());
    }
}