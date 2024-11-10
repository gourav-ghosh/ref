package com.devstringx.mytylesstockcheck.screens.transactionFilters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.ActivityTransactionHistoryFilterBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionHistoryFilter extends AppCompatActivity {
    ActivityTransactionHistoryFilterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_history_filter);
        binding.transactionDate.setChecked(true);
        if (binding.transactionDate.isChecked())
            Common.loadFragment(getSupportFragmentManager(), new TransactionHistoryDateFragment(), R.id.transaction_filter_frag_container);
        binding.filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (binding.transactionDate.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new TransactionHistoryDateFragment(), R.id.transaction_filter_frag_container);
                } else if (binding.transactionPaymentMode.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new TransactionHistoryPaymentModeFragment(), R.id.transaction_filter_frag_container);
                } else if (binding.transactionSalePerson.isChecked()) {
                    Common.loadFragment(getSupportFragmentManager(), new TransactionHistorySalePersonFragment(), R.id.transaction_filter_frag_container);
                } else {
                    Common.loadFragment(getSupportFragmentManager(), new TransactionHistoryAccountantFragment(), R.id.transaction_filter_frag_container);
                }
            }
        });

        binding.closeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.transactionDateType = "";
                Common.transactionStartDate = "";
                Common.transactionEndDate = "";
                Common.transactionPaymentMode = new ArrayList<>();
                Common.transactionPaymentModeOri = new ArrayList<>();
                Common.transactionAccountant = new ArrayList<>();
                Common.transactionSalesExe = new ArrayList<>();
                callApi();
            }
        });
        binding.applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.transactionAccountant = Common.tempTransactionAccountant;
                Common.transactionSalesExe = Common.tempTransactionSalesExe;
                callApi();
            }
        });
    }

    private void callApi() {
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
        Common.showLog("RESPONSE===+++++++" + map);
        Intent intent = new Intent();
        intent.putExtra("TranHistoryFilterDataMap", new Gson().toJson(map));
        setResult(RESULT_OK, intent);
        finish();
    }

    public void checkIsAnyFIlterSelected() {
        if (Common.transactionDateType.isEmpty() && Common.transactionAccountant.isEmpty() && Common.transactionSalesExe.isEmpty() && Common.transactionPaymentMode.isEmpty())
            binding.clearFilter.setVisibility(View.INVISIBLE);
        else binding.clearFilter.setVisibility(View.VISIBLE);
    }
}