package com.devstringx.mytylesstockcheck.screens.shippingChargeFilter;

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
import com.devstringx.mytylesstockcheck.databinding.FragmentShippingChargeFilterByDateBinding;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener;

public class ShippingChargeFilterByDateFragment extends Fragment {
    FragmentShippingChargeFilterByDateBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shipping_charge_filter_by_date, container, false);
        View view = binding.getRoot();
        if (Common.shippingDateType != null && !Common.shippingDateType.isEmpty()) {
            if (Common.shippingDateType.equalsIgnoreCase("all"))
                binding.spinnerView.selectItemByIndex(0);
            else if (Common.shippingDateType.equalsIgnoreCase("today"))
                binding.spinnerView.selectItemByIndex(1);
            else if (Common.shippingDateType.equalsIgnoreCase("yesterday"))
                binding.spinnerView.selectItemByIndex(2);
            else if (Common.shippingDateType.equalsIgnoreCase("this_week"))
                binding.spinnerView.selectItemByIndex(3);
            else if (Common.shippingDateType.equalsIgnoreCase("this_month"))
                binding.spinnerView.selectItemByIndex(4);
            else if (Common.shippingDateType.equalsIgnoreCase("last_month"))
                binding.spinnerView.selectItemByIndex(5);
            else if (Common.shippingDateType.equalsIgnoreCase("this_year"))
                binding.spinnerView.selectItemByIndex(6);
            else if (Common.shippingDateType.equalsIgnoreCase("custom"))
                binding.spinnerView.selectItemByIndex(7);
        }else binding.spinnerView.selectItemByIndex(-1);
        if (Common.shippingStartDate != null) binding.fromDate.setText(Common.shippingStartDate);
        if (Common.shippingEndDate != null) binding.toDate.setText(Common.shippingEndDate);
        if (binding.spinnerView.getText().equals("Custom")) {
            binding.customDate.setVisibility(View.VISIBLE);
        } else binding.customDate.setVisibility(View.GONE);
        Common.isShippingChargeFilterSelected(getActivity());
        binding.spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<Object>() {
            @Override
            public void onItemSelected(int i, @Nullable Object o, int i1, Object t1) {
                i = binding.spinnerView.getSelectedIndex();
                if (i == 7) {
                    binding.customDate.setVisibility(View.VISIBLE);
                } else binding.customDate.setVisibility(View.GONE);
                if (i == 0) Common.shippingDateType = "all";
                else if (i == 1) Common.shippingDateType = "today";
                else if (i == 2) Common.shippingDateType = "yesterday";
                else if (i == 3) Common.shippingDateType = "this_week";
                else if (i == 4) Common.shippingDateType = "this_month";
                else if (i == 5) Common.shippingDateType = "last_month";
                else if (i == 6) Common.shippingDateType = "this_year";
                else if (i == 7) Common.shippingDateType = "custom";
                Common.isShippingChargeFilterSelected(getActivity());
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
                Common.openCalenderForUpcomingDates(getActivity(), binding.fromDate, "shipping_start_date");
            }
        });
        binding.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(), binding.toDate, "shipping_end_date");
            }
        });
        return view;
    }
}