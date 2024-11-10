package com.devstringx.mytylesstockcheck.screens.razorpayFilter;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentRazorpayFilterStatusBinding;

public class RazorpayFilterStatusFragment extends Fragment {
    FragmentRazorpayFilterStatusBinding filterStatusBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        filterStatusBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_razorpay_filter_status, container, false);
        View view = filterStatusBinding.getRoot();
        if (Common.razorpayStatus.contains("True")) {
            filterStatusBinding.paidIv.setImageResource(R.drawable.orange_checkbox_selected);
        }
        if (Common.razorpayStatus.contains("False")) {
            filterStatusBinding.pendingIv.setImageResource(R.drawable.orange_checkbox_selected);
        }
        Common.isRazorPayFilterSelected(getActivity());
        filterStatusBinding.paidIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.razorpayStatus.contains("True")) {
                    filterStatusBinding.paidIv.setImageResource(R.drawable.orange_checkbox_selected);
                    Common.razorpayStatus.add("True");
                } else {
                    filterStatusBinding.paidIv.setImageResource(R.drawable.orange_checkbox_unselected);
                    Common.razorpayStatus.remove("True");
                }
                Common.isRazorPayFilterSelected(getActivity());
            }
        });
        filterStatusBinding.pendingIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.razorpayStatus.contains("False")) {
                    filterStatusBinding.pendingIv.setImageResource(R.drawable.orange_checkbox_selected);
                    Common.razorpayStatus.add("False");
                } else {
                    filterStatusBinding.pendingIv.setImageResource(R.drawable.orange_checkbox_unselected);
                    Common.razorpayStatus.remove("False");
                }
                Common.isRazorPayFilterSelected(getActivity());
            }
        });
        return view;
    }
}