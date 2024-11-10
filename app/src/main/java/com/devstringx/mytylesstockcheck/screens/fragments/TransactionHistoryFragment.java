package com.devstringx.mytylesstockcheck.screens.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.TransactionHistoryListingAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentTransactionHistoryBinding;
import com.devstringx.mytylesstockcheck.datamodal.getAllComplaints.AllComplaintResponse;
import com.devstringx.mytylesstockcheck.screens.transactionFilters.TransactionHistoryFilter;
import com.devstringx.mytylesstockcheck.screens.transactionFilters.TransactionHistoryResponse;
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

public class TransactionHistoryFragment extends Fragment implements ResponseListner {
    FragmentTransactionHistoryBinding binding;
    TransactionHistoryListingAdapter adapter;
    private List<TransactionHistoryResponse.Record> recordsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction_history, container, false);
        View view = binding.getRoot();
        setTransactionsAdapter();

        binding.transactionFilterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), TransactionHistoryFilter.class), 002);
            }
        });
        binding.searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchHistory(binding.searchET.getText().toString(), false);
            }
        });
        return view;
    }

    private void searchHistory(String search, boolean isShowLoader) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("search", search);
        callAPI(new Gson().toJson(map), false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getTransactionHistory();
    }

    private void setTransactionsAdapter() {
        RecyclerView recyclerView = binding.transactionHistoryRV;
        adapter = new TransactionHistoryListingAdapter(getContext(), recordsList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void getTransactionHistory() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("search", "");
        map.put("pageNumber", "");
        map.put("pageSize", "");
        map.put("dateRange", Common.transactionDateType);
        map.put("fromDate", Common.transactionStartDate);
        map.put("toDate", Common.transactionEndDate);
        map.put("paymentMode", Common.transactionPaymentMode);
        map.put("accountManager", Common.transactionAccountant);
        map.put("salesExecutive", Common.transactionSalesExe);
        callAPI(new Gson().toJson(map), true);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            String receivedHashMap = data.getStringExtra("TranHistoryFilterDataMap");
            callAPI(receivedHashMap, true);
        }
    }

    private void callAPI(String receivedHashMap, boolean isLoader) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.GET_TRANSACTION_HISTORY, receivedHashMap);
        new NetworkRequest(getActivity(), this).callWebServices(ServiceMethods.WS_GET_TRANSACTION_HISTORY, map, isLoader);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_GET_TRANSACTION_HISTORY) {
                TransactionHistoryResponse response = new Gson().fromJson(responseDO.getResponse(), TransactionHistoryResponse.class);
                if (response.getData() != null) {
                    recordsList = response.getData().getRecords();
                    adapter.refreshList(recordsList);
                }
                //records check
            }
        }
    }

}