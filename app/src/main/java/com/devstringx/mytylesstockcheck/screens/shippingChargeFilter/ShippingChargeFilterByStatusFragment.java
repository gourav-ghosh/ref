package com.devstringx.mytylesstockcheck.screens.shippingChargeFilter;

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
import com.devstringx.mytylesstockcheck.databinding.FragmentComplainTypeBinding;
import com.devstringx.mytylesstockcheck.databinding.FragmentShippingChargeFilterByStatusBinding;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ShippingChargeFilterByStatusFragment extends Fragment implements ResponseListner, NameListWithCheckboxAdapter.FilterCBSelected {
    FragmentComplainTypeBinding binding;
    List<RecordsItem> complaintType;
    private NameListWithCheckboxAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_complain_type, container, false);
        View view = binding.getRoot();
        addStaticData();
        return view;
    }


    private void addStaticData() {
        complaintType = new ArrayList<>();
        // [Pending, Approved, Paid, Rejected, All]
        String[] recordsArray = {"Pending","Approved","Paid","Rejected"};
        for (int i = 0; i < recordsArray.length; i++) {
            RecordsItem recordsItem = new RecordsItem();
            recordsItem.setCityName(recordsArray[i]);
            complaintType.add(recordsItem);
        }
        setupAdapter();
    }

    private void setupAdapter() {
        RecyclerView recyclerView = binding.complaintTypeRv;
        List<String> lsSelectedRoles = new ArrayList<>();
        lsSelectedRoles.add(Common.selectedRole);
        adapter = new NameListWithCheckboxAdapter(getContext(), complaintType, this::onItemSelectedListner, Common.selectedShippingStatustName);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETCOMPLAINTFILTERVALUE) {
                String jsonResponse = responseDO.getResponse().toString();
                JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);
                JsonObject dataObject = jsonObject.getAsJsonObject("data");
                JsonArray recordsArray = dataObject.getAsJsonArray("records");
                complaintType = new ArrayList<>();
                for (int i = 0; i < recordsArray.size(); i++) {
                    RecordsItem recordsItem = new RecordsItem();
                    recordsItem.setCityName(recordsArray.get(i).getAsString());
                    complaintType.add(recordsItem);
                }
                setupAdapter();
            }
        }
    }

    @Override
    public void onItemSelectedListner(List<String> selected_items) {
        Common.selectedShippingStatustName = selected_items;
        Common.isShippingChargeFilterSelected(getActivity());
    }
}