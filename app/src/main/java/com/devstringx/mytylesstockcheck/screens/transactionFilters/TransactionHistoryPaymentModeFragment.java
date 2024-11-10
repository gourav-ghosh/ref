package com.devstringx.mytylesstockcheck.screens.transactionFilters;

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
import com.devstringx.mytylesstockcheck.databinding.FragmentTransactionHistoryPaymentModeBinding;
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

public class TransactionHistoryPaymentModeFragment extends Fragment implements ResponseListner, NameListWithCheckboxAdapter.FilterCBSelected {
    private FragmentTransactionHistoryPaymentModeBinding binding;
    private List<RecordsItem> allOwnersList = new ArrayList<>();
    NameListWithCheckboxAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction_history_payment_mode, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        addStaticData();
    }

    private void addStaticData() {
        allOwnersList = new ArrayList<>();
        //[cash, bank_transfer, upi, cheque, dd, credit, card_swipe, razorpay, credit_notes, others]
        String[] recordsArray = {"Cash", "Bank Transfer", "UPI", "Cheque", "DD", "Credit", "Card Swipe", "Razorpay", "Credit Notes", "Others"};
        for (int i = 0; i < recordsArray.length; i++) {
            RecordsItem recordsItem = new RecordsItem();
            recordsItem.setCityName(recordsArray[i]);
            allOwnersList.add(recordsItem);
        }
        setupAdapter();
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
        RecyclerView recyclerView = binding.transactionPaymentRv;
        adapter = new NameListWithCheckboxAdapter(getContext(), allOwnersList, this::onItemSelectedListner, Common.transactionPaymentModeOri);
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
        Common.transactionPaymentModeOri = selected_items;
        List<String> sel_payment_modes = new ArrayList<>();
        if (selected_items != null) {
            for (String mode : selected_items) {
                String paymode = mode.toLowerCase().trim();
                paymode = paymode.replace(" ", "_");
                sel_payment_modes.add(paymode);
            }
        }
        Common.transactionPaymentMode = sel_payment_modes;
        Common.isTransactionFilterSelected(getActivity());
    }
}