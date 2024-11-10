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
import com.devstringx.mytylesstockcheck.databinding.FragmentPaymentModesBinding;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentModesFragment extends Fragment implements NameListWithCheckboxAdapter.FilterCBSelected , ResponseListner {
    FragmentPaymentModesBinding binding;
    private List<RecordsItem> allOwnersList = new ArrayList<>();
    NameListWithCheckboxAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_modes, container, false);
        View view = binding.getRoot();
        getPaymentModes();
        return view;
    }
    private void setupAdapter() {
        RecyclerView recyclerView = binding.orderFilterPaymentModesRv;
        adapter = new NameListWithCheckboxAdapter(getContext(),allOwnersList,this::onItemSelectedListner,new ArrayList<>(Common.selectedOrderPaymentMode));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void getPaymentModes() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("filter_value", "paymentMode");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETORDERFILTERVALUE, jsonObject.toString());
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_GETORDERFILTERVALUE, map, true);
    }

    @Override
    public void onItemSelectedListner(List<String> selected_items) {
        Common.tempSelectedOrderPaymentMode=selected_items;
        Common.isOrderListingFilterSelected(getActivity());
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
                    recordsItem.setCityName(records.get(i).getAsJsonObject().get("label").getAsString());
                    recordsItem.setId(records.get(i).getAsJsonObject().get("value").getAsString());
                    allOwnersList.add(recordsItem);
                }
                setupAdapter();
            }
        }
    }
}