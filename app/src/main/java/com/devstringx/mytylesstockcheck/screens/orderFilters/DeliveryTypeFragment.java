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
import com.devstringx.mytylesstockcheck.databinding.FragmentDeliveryTypeBinding;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeliveryTypeFragment extends Fragment implements ResponseListner , NameListWithCheckboxAdapter.FilterCBSelected{
    FragmentDeliveryTypeBinding binding;
    private List<RecordsItem> allOwnersList = new ArrayList<>();
    NameListWithCheckboxAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_delivery_type, container, false);
        View view=binding.getRoot();
        getDeliveryType();
        return view;
    }

    private void getDeliveryType() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("filter_value", "deliveryType");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETORDERFILTERVALUE, jsonObject.toString());
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_GETORDERFILTERVALUE, map, true);
    }
    private void setupAdapter() {
        RecyclerView recyclerView = binding.orderFilterDeliveryTypeRv;
        adapter = new NameListWithCheckboxAdapter(getContext(),allOwnersList,this::onItemSelectedListner,new ArrayList<>(Common.selectedDeliveryType));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETORDERFILTERVALUE) {
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(responseDO.getResponse()).getAsJsonObject();
                JsonObject data = jsonObject.getAsJsonObject("data");
                JsonArray records = data.getAsJsonArray("records");
                allOwnersList = new ArrayList<>();

                for (int i = 0; i < records.size(); i++) {
                    RecordsItem recordsItem = new RecordsItem();
                    recordsItem.setCityName(records.get(i).getAsJsonObject().get("value").getAsString());
                    allOwnersList.add(recordsItem);
                }
                setupAdapter();
            }
        }
    }

    @Override
    public void onItemSelectedListner(List<String> selected_items) {
        Common.tempSelectedDeliveryType=selected_items;
        Common.isOrderListingFilterSelected(getActivity());
    }
}