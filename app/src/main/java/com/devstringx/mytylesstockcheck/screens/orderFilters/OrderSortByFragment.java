package com.devstringx.mytylesstockcheck.screens.orderFilters;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentOrderSortByBinding;
import com.devstringx.mytylesstockcheck.datamodal.Data;

public class OrderSortByFragment extends Fragment {
    FragmentOrderSortByBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_order_sort_by, container, false);
        View view=binding.getRoot();
        if (Common.orderFilterSortby.equalsIgnoreCase("createdAtDesc")) binding.newRb.setImageResource(R.drawable.ic_checked_radio_button);
        else if (Common.orderFilterSortby.equalsIgnoreCase("createdAtAsc")) binding.oldRb.setImageResource(R.drawable.ic_checked_radio_button);
        else if (Common.orderFilterSortby.equalsIgnoreCase("orderAmountAsc")) binding.lowRb.setImageResource(R.drawable.ic_checked_radio_button);
        else if (Common.orderFilterSortby.equalsIgnoreCase("orderAmountDesc")) binding.highRb.setImageResource(R.drawable.ic_checked_radio_button);
        else if (Common.orderFilterSortby.equalsIgnoreCase("saleOrderNumberAsc")) binding.inc.setImageResource(R.drawable.ic_checked_radio_button);
        else if (Common.orderFilterSortby.equalsIgnoreCase("saleOrderNumberDesc")) binding.dec.setImageResource(R.drawable.ic_checked_radio_button);
        Common.isOrderListingFilterSelected(getActivity());
        binding.oldRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.tempOrderSortby="createdAtAsc";
                binding.oldRb.setImageResource(R.drawable.ic_checked_radio_button);
                binding.newRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.lowRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.highRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.inc.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.dec.setImageResource(R.drawable.ic_unchecked_radio_button);
                Common.isOrderListingFilterSelected(getActivity());
            }
        });
        binding.newRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.tempOrderSortby="createdAtDesc";
                binding.oldRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.newRb.setImageResource(R.drawable.ic_checked_radio_button);
                binding.lowRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.highRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.inc.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.dec.setImageResource(R.drawable.ic_unchecked_radio_button);
                Common.isOrderListingFilterSelected(getActivity());

            }
        });
        binding.lowRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.tempOrderSortby="orderAmountAsc";
                binding.oldRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.newRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.lowRb.setImageResource(R.drawable.ic_checked_radio_button);
                binding.highRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.inc.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.dec.setImageResource(R.drawable.ic_unchecked_radio_button);
                Common.isOrderListingFilterSelected(getActivity());
            }
        });
        binding.highRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.tempOrderSortby="orderAmountDesc";
                binding.oldRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.newRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.lowRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.highRb.setImageResource(R.drawable.ic_checked_radio_button);
                binding.inc.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.dec.setImageResource(R.drawable.ic_unchecked_radio_button);
                Common.isOrderListingFilterSelected(getActivity());
            }
        });
        binding.inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.tempOrderSortby="saleOrderNumberAsc";
                binding.oldRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.newRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.lowRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.highRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.inc.setImageResource(R.drawable.ic_checked_radio_button);
                binding.dec.setImageResource(R.drawable.ic_unchecked_radio_button);
                Common.isOrderListingFilterSelected(getActivity());
            }
        });
        binding.dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.tempOrderSortby="saleOrderNumberDesc";
                binding.oldRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.newRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.lowRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.highRb.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.inc.setImageResource(R.drawable.ic_unchecked_radio_button);
                binding.dec.setImageResource(R.drawable.ic_checked_radio_button);
                Common.isOrderListingFilterSelected(getActivity());
            }
        });
        return view;
    }
}