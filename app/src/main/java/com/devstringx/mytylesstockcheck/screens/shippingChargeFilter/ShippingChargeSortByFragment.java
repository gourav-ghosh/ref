package com.devstringx.mytylesstockcheck.screens.shippingChargeFilter;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentShippingChargeSortByBinding;
import com.devstringx.mytylesstockcheck.datamodal.Data;

public class ShippingChargeSortByFragment extends Fragment {
    FragmentShippingChargeSortByBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_shipping_charge_sort_by, container, false);
        View view = binding.getRoot();
        if (Common.shippingFilterSortBy.equalsIgnoreCase("createdAtDesc")) binding.newToOld.setImageResource(R.drawable.ic_checked_radio_button);
        else if (Common.shippingFilterSortBy.equalsIgnoreCase("createdAtAsc")) binding.oldToNew.setImageResource(R.drawable.ic_checked_radio_button);
        else if (Common.shippingFilterSortBy.equalsIgnoreCase("amountAsc")) binding.lowRb.setImageResource(R.drawable.ic_checked_radio_button);
        else if (Common.shippingFilterSortBy.equalsIgnoreCase("amountDesc")) binding.highRb.setImageResource(R.drawable.ic_checked_radio_button);
        Common.isShippingChargeFilterSelected(getActivity());
        //createdAtDesc, createdAtAsc, amountAsc, amountDesc,
        binding.oldToNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.shippingFilterSortBy="createdAtAsc";
                binding.oldToNew.setImageResource(R.drawable.ic_checked_radio_button);
                binding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.lowRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.highRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                Common.isShippingChargeFilterSelected(getActivity());
            }
        });
        binding.newToOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.shippingFilterSortBy="createdAtDesc";
                binding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.newToOld.setImageResource(R.drawable.ic_checked_radio_button);
                binding.lowRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.highRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                Common.isShippingChargeFilterSelected(getActivity());
            }
        });
        binding.lowRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.shippingFilterSortBy="amountAsc";
                binding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.lowRb.setImageResource(R.drawable.ic_checked_radio_button);
                binding.highRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                Common.isShippingChargeFilterSelected(getActivity());
            }
        });
        binding.highRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.shippingFilterSortBy="amountDesc";
                binding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.lowRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.highRb.setImageResource(R.drawable.ic_checked_radio_button);
                Common.isShippingChargeFilterSelected(getActivity());
            }
        });
        return view;
    }
}