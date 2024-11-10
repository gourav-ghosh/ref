package com.devstringx.mytylesstockcheck.screens.analytics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.NameListWithCheckboxAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentComplainTypeBinding;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoleFragment extends Fragment implements ResponseListner, NameListWithCheckboxAdapter.FilterCBSelected {
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
        String[] recordsArray = {"Sale Executive", "Delivery Agent"};
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
        adapter = new NameListWithCheckboxAdapter(getContext(), complaintType, this::onItemSelectedListner, Common.selectedRole_Original);
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
        Common.selectedRole_Original = selected_items;
        String role = CollectionUtils.isEmpty(selected_items) ? "" : selected_items.get(0);
        if ("Sale Executive".equalsIgnoreCase(role))
            Common.selectedRole = "sales_person";
        if ("Delivery Agent".equalsIgnoreCase(role))
            Common.selectedRole = "delivery_agent";
        Common.isAnalyticFilterSelected(getActivity());
    }
}