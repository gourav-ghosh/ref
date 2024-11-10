package com.devstringx.mytylesstockcheck.filter;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.FilterItemAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentFilterOwnerBinding;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.AllOwnersData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Record;
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

public class FilterOwnerFragment extends Fragment implements ResponseListner , FilterItemAdapter.FilterItemSelected{
    FragmentFilterOwnerBinding filterOwnerBinding;
    FilterItemAdapter adapter;
    private List<RecordsItem> allOwnersList = new ArrayList<>();
    private Record record = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        filterOwnerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter_owner, container, false);
        View view = filterOwnerBinding.getRoot();
        getAllOwners();
        Common.showLog("ownerList===="+Common.filterOwnerList);
        Common.showLog(record+"");
        return view;
    }
    private void getAllOwners() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("role", "");
            jsonObject.put("user_status", "active");
            jsonObject.put("screen", "Lead");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.ALLOWNERSDATA, jsonObject.toString());
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_GETUSERFORSCREEN, map, true);
    }

    private void setupAdapter() {
        RecyclerView recyclerView = filterOwnerBinding.filterItemRv;
        adapter = new FilterItemAdapter(getContext(), allOwnersList,"ownerList", this::onItemSelectedListner,new ArrayList<>(Common.filterOwnerList));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GETUSERFORSCREEN) {
                AllOwnersData allOwnersData = new Gson().fromJson(responseDO.getResponse(), AllOwnersData.class);
                allOwnersList = new ArrayList<>();
                for (int i = 0; i < allOwnersData.getData().getRecords().size(); i++) {
                    RecordsItem recordsItem = new RecordsItem();
                    recordsItem.setCityName(allOwnersData.getData().getRecords().get(i).getFirstName() + " " + allOwnersData.getData().getRecords().get(i).getLastName());
                    recordsItem.setId(String.valueOf(allOwnersData.getData().getRecords().get(i).getId()));
                    if (record != null) {
                        if (record.getLeadOwnerId() == allOwnersData.getData().getRecords().get(i).getId()) {
                            recordsItem.setSelected(true);
                            Common.showLog("SELECTED ASSIGNID===" + allOwnersData.getData().getRecords().get(i).getId());
                        }
                    }
                    allOwnersList.add(recordsItem);

                }
                setupAdapter();

            }
        }
    }

    @Override
    public void onItemSelectedListner(List<String> selected_items) {
        Common.showLog(selected_items+"");
        Common.filterOwnerListTemp=selected_items;
        Common.isFilterSelected(getActivity());
        Common.showLog("ownerList===="+Common.filterOwnerListTemp);

        }
}