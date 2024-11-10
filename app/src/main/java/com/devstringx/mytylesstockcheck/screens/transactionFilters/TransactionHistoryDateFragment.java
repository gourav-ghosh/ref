package com.devstringx.mytylesstockcheck.screens.transactionFilters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentTransactionHistoryDateBinding;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;

public class TransactionHistoryDateFragment extends Fragment {
    FragmentTransactionHistoryDateBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_transaction_history_date, container, false);
        View view=binding.getRoot();
        if (Common.transactionDateType!=null) {
            if (Common.transactionDateType!=null) {
                if (Common.transactionDateType.equalsIgnoreCase("all")) binding.spinnerView.selectItemByIndex(0);
                else if (Common.transactionDateType.equalsIgnoreCase("today")) binding.spinnerView.selectItemByIndex(1);
                else if (Common.transactionDateType.equalsIgnoreCase("yesterday")) binding.spinnerView.selectItemByIndex(2);
                else if (Common.transactionDateType.equalsIgnoreCase("this_week")) binding.spinnerView.selectItemByIndex(3);
                else if (Common.transactionDateType.equalsIgnoreCase("this_month")) binding.spinnerView.selectItemByIndex(4);
                else if (Common.transactionDateType.equalsIgnoreCase("last_month")) binding.spinnerView.selectItemByIndex(5);
                else if (Common.transactionDateType.equalsIgnoreCase("this_year")) binding.spinnerView.selectItemByIndex(6);
                else if (Common.transactionDateType.equalsIgnoreCase("custom")) binding.spinnerView.selectItemByIndex(7);
            }
        }
        if (Common.transactionStartDate!=null) binding.fromDate.setText(Common.transactionStartDate);
        if (Common.transactionEndDate!=null) binding.toDate.setText(Common.transactionEndDate);
        if (binding.spinnerView.getText().equals("Custom")){
            binding.customDate.setVisibility(View.VISIBLE);
        }else binding.customDate.setVisibility(View.GONE);
        Common.isTransactionFilterSelected(getActivity());
        binding.spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                i=binding.spinnerView.getSelectedIndex();
                if (i==7){
                    binding.customDate.setVisibility(View.VISIBLE);
                }else binding.customDate.setVisibility(View.GONE);
                if (i==0) Common.transactionDateType="all";
                else if (i==1) Common.transactionDateType="today";
                else if (i==2) Common.transactionDateType="yesterday";
                else if (i==3) Common.transactionDateType="this_week";
                else if (i==4) Common.transactionDateType="this_month";
                else if (i==5) Common.transactionDateType="last_month";
                else if (i==6) Common.transactionDateType="this_year";
                else if (i==7) Common.transactionDateType="custom";
                Common.isTransactionFilterSelected(getActivity());
            }
        });
        binding.spinnerView.setSpinnerOutsideTouchListener(new OnSpinnerOutsideTouchListener() {
            @Override
            public void onSpinnerOutsideTouch(@NonNull View view, @NonNull MotionEvent motionEvent) {
                binding.spinnerView.dismiss();
            }
        });
        binding.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),binding.fromDate, "transaction_start_date");
            }
        });
        binding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),binding.toDate, "transaction_end_date");
            }
        });
        return view;
    }
}