
package com.devstringx.mytylesstockcheck.screens.razorpayFilter;

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
import com.devstringx.mytylesstockcheck.databinding.FragmentRazorpayFilterSalePersonBinding;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RazorpayFilterSalePersonFragment extends Fragment implements ResponseListner , NameListWithCheckboxAdapter.FilterCBSelected {
    FragmentRazorpayFilterSalePersonBinding filterSalePersonBinding;
    private List<RecordsItem> allOwnersList = new ArrayList<>();
    NameListWithCheckboxAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        filterSalePersonBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_razorpay_filter_sale_person, container, false);
        View view = filterSalePersonBinding.getRoot();
        getUsersByRoleName();
        return view;
    }
    private void getUsersByRoleName() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("role_name", "Sales Executive");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GETUSERSBYROLENAME, jsonObject.toString());
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_GETUSERSBYROLENAME, map, true);
    }
    private void setupAdapter() {
        RecyclerView recyclerView = filterSalePersonBinding.salesNameRv;
        adapter = new NameListWithCheckboxAdapter(getContext(),allOwnersList,this::onItemSelectedListner,Common.selectedSalesExeName);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERSBYROLENAME) {
                AllOwnersData allOwnersData = new Gson().fromJson(responseDO.getResponse(), AllOwnersData.class);
                allOwnersList = new ArrayList<>();
                for (int i = 0; i < allOwnersData.getData().getRecords().size(); i++) {
                    RecordsItem recordsItem = new RecordsItem();
                    recordsItem.setCityName(allOwnersData.getData().getRecords().get(i).getFirstName() + " " + allOwnersData.getData().getRecords().get(i).getLastName());
                    recordsItem.setId(String.valueOf(allOwnersData.getData().getRecords().get(i).getId()));
                    allOwnersList.add(recordsItem);
                }
                setupAdapter();
            }
        }
    }

    @Override
    public void onItemSelectedListner(List<String> selected_items) {
        Common.selectedSalesExeName=selected_items;
        Common.isRazorPayFilterSelected(getActivity());
    }
}