package com.devstringx.mytylesstockcheck.screens.complaintFIlter;

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
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComplainTypeFragment extends Fragment implements ResponseListner, NameListWithCheckboxAdapter.FilterCBSelected {
    FragmentComplainTypeBinding binding;
    List<RecordsItem> complaintType;
    private NameListWithCheckboxAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_complain_type, container, false);
        View view = binding.getRoot();
        getComplaintType();
        return view;
    }
    private void getComplaintType() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("filter_value","complaint_type");
        map.put(NKeys.GETCOMPLAINTFILTERVALUE, new Gson().toJson(map));
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_GETCOMPLAINTFILTERVALUE, map, true);
    }
    private void setupAdapter() {
        RecyclerView recyclerView = binding.complaintTypeRv;
        adapter = new NameListWithCheckboxAdapter(getContext(),complaintType,this::onItemSelectedListner,Common.selectedComplaintType);
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
        Common.selectedComplaintType=selected_items;
        Common.isComplaintFilterSelected(getActivity());
    }
}