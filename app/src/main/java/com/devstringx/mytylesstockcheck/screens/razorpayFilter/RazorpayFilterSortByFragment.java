package com.devstringx.mytylesstockcheck.screens.razorpayFilter;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentRazorpayFilterSortByBinding;

public class RazorpayFilterSortByFragment extends Fragment {
    FragmentRazorpayFilterSortByBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_razorpay_filter_sort_by, container, false);
        View view = binding.getRoot();
        if (Common.sortByRazorpay.equalsIgnoreCase("amountDesc")) binding.highToLow.setImageResource(R.drawable.ic_checked_radio_button);
        else if (Common.sortByRazorpay.equalsIgnoreCase("amountAsc")) binding.lowToHigh.setImageResource(R.drawable.ic_checked_radio_button);
        else if (Common.sortByRazorpay.equalsIgnoreCase("createdAtAsc")) binding.oldToNew.setImageResource(R.drawable.ic_checked_radio_button);
        else binding.newToOld.setImageResource(R.drawable.ic_checked_radio_button);
        Common.isRazorPayFilterSelected(getActivity());
        binding.highToLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.sortByRazorpay.equalsIgnoreCase("amountDesc")){
                    binding.highToLow.setImageResource(R.drawable.ic_checked_radio_button);
                    binding.lowToHigh.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                    Common.sortByRazorpay="amountDesc";
                }
                Common.isRazorPayFilterSelected(getActivity());
            }
        });
        binding.lowToHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.sortByRazorpay.equalsIgnoreCase("amountAsc")){
                    binding.highToLow.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.lowToHigh.setImageResource(R.drawable.ic_checked_radio_button);
                    binding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                    Common.sortByRazorpay="amountAsc";
                }
                Common.isRazorPayFilterSelected(getActivity());
            }
        });
        binding.oldToNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.sortByRazorpay.equalsIgnoreCase("createdAtAsc")){
                    binding.highToLow.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.lowToHigh.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.oldToNew.setImageResource(R.drawable.ic_checked_radio_button);
                    binding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                    Common.sortByRazorpay="createdAtAsc";
                }
                Common.isRazorPayFilterSelected(getActivity());
            }
        });
        binding.newToOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.sortByRazorpay.equalsIgnoreCase("createdAtDesc")){
                    binding.highToLow.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.lowToHigh.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.newToOld.setImageResource(R.drawable.ic_checked_radio_button);
                    Common.sortByRazorpay="createdAtDesc";
                }
                Common.isRazorPayFilterSelected(getActivity());
            }
        });
        return view;
    }
}