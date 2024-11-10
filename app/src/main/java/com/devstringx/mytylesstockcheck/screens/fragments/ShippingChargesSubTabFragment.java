package com.devstringx.mytylesstockcheck.screens.fragments;

import static android.content.Intent.getIntent;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentShippingChargesSubTabBinding;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.Records;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;

public class ShippingChargesSubTabFragment extends Fragment {
    public Records records;
    FragmentShippingChargesSubTabBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shipping_charges_sub_tab, container, false);
        records = ((OrderDetailActivity) getActivity()).records;
        if (new PreferenceUtils(getActivity()).getStringFromPreference(PreferenceUtils.ROLEID, "").equalsIgnoreCase("10")) {
            binding.schResponseBtnLL.setVisibility(View.VISIBLE);
        }
        if (records.getShippingChargeDetails() !=null && !records.getShippingChargeDetails().isEmpty()) {
            binding.shippingChargesLL.setVisibility(View.VISIBLE);
            binding.noShippingChargesTV.setVisibility(View.GONE);
            binding.dateUnderSch.setText(records.getShippingChargeDetails().get(0).getCreatedAt());
            binding.dateUnderResponse.setText(records.getShippingChargeDetails().get(0).getUpdatedAt());
            if (records.getShippingChargeDetails().get(0).getStatus().equalsIgnoreCase("request_sent")) {
                binding.responseTagTV.setText("Request Sent");
                binding.responseTagTV.setTextColor(getActivity().getResources().getColor(R.color.gray));
            } else if (records.getShippingChargeDetails().get(0).getStatus().equalsIgnoreCase("rejected")) {
                binding.responseTagTV.setText("Rejected");
                binding.responseTagTV.setTextColor(getActivity().getResources().getColor(R.color.red));
            } else if (records.getShippingChargeDetails().get(0).getStatus().equalsIgnoreCase("approved")) {
                binding.responseTagTV.setText("Approved");
                binding.responseTagTV.setTextColor(getActivity().getResources().getColor(R.color.orange));
            }
            binding.distanceTV.setText(records.getShippingChargeDetails().get(0).getDistance());
            binding.quantityTV.setText(records.getShippingChargeDetails().get(0).getLoadingPoints());
            binding.totalAmountTV.setText("₹ " + records.getShippingChargeDetails().get(0).getAmount());
        } else {
            binding.shippingChargesLL.setVisibility(View.GONE);
            binding.noShippingChargesTV.setVisibility(View.VISIBLE);
        }

        View view = binding.getRoot();
        return view;
    }
}