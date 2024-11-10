package com.devstringx.mytylesstockcheck.screens.architectFilter;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentArchitectFilterSortByBinding;

public class ArchitectFilterSortByFragment extends Fragment {
    FragmentArchitectFilterSortByBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_architect_filter_sort_by, container, false);
        View view =  binding.getRoot();
        if (Common.architectSortBy.equalsIgnoreCase("orderAmountDesc")){
            binding.orderAmountDec.setImageResource(R.drawable.ic_checked_radio_button);
        } else if (Common.architectSortBy.equalsIgnoreCase("orderAmountAsc")) {
            binding.orderAmountInc.setImageResource(R.drawable.ic_checked_radio_button);
        }else if(Common.architectSortBy.equalsIgnoreCase("createdAtAsc")){
            binding.oldToNew.setImageResource(R.drawable.ic_checked_radio_button);
        }else if(Common.architectSortBy.equalsIgnoreCase("createdAtDesc")){
            binding.newToOld.setImageResource(R.drawable.ic_checked_radio_button);
        }else if(Common.architectSortBy.equalsIgnoreCase("orderCountDesc")){
            binding.numOrderDec.setImageResource(R.drawable.ic_checked_radio_button);
        }else if(Common.architectSortBy.equalsIgnoreCase("orderCountAsc")){
            binding.numOrderInc.setImageResource(R.drawable.ic_checked_radio_button);
        }
        Common.isArchitectFilterSelected(getActivity());
        binding.orderAmountInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.architectSortBy.equalsIgnoreCase("orderAmountAsc")){
                    Common.architectSortBy="orderAmountAsc";
                    binding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.numOrderInc.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.numOrderDec.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.orderAmountDec.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.orderAmountInc.setImageResource(R.drawable.ic_checked_radio_button);
                }
                Common.isArchitectFilterSelected(getActivity());
            }
        });
        binding.orderAmountDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.architectSortBy.equalsIgnoreCase("orderAmountDesc")){
                    Common.architectSortBy="orderAmountDesc";
                    binding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.numOrderInc.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.numOrderDec.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.orderAmountDec.setImageResource(R.drawable.ic_checked_radio_button);
                    binding.orderAmountInc.setImageResource(R.drawable.ic_unchecked_radio_button);
                }
                Common.isArchitectFilterSelected(getActivity());
            }
        });
        binding.newToOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.architectSortBy.equalsIgnoreCase("createdAtDesc")){
                    Common.architectSortBy="createdAtDesc";
                    binding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.newToOld.setImageResource(R.drawable.ic_checked_radio_button);
                    binding.numOrderInc.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.numOrderDec.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.orderAmountDec.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.orderAmountInc.setImageResource(R.drawable.ic_unchecked_radio_button);
                }
                Common.isArchitectFilterSelected(getActivity());
            }
        });
        binding.oldToNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.architectSortBy.equalsIgnoreCase("createdAtAsc")){
                    Common.architectSortBy="createdAtAsc";
                    binding.oldToNew.setImageResource(R.drawable.ic_checked_radio_button);
                    binding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.numOrderInc.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.numOrderDec.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.orderAmountDec.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.orderAmountInc.setImageResource(R.drawable.ic_unchecked_radio_button);
                }
                Common.isArchitectFilterSelected(getActivity());
            }
        });
        binding.numOrderInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.architectSortBy.equalsIgnoreCase("orderCountAsc")){
                    Common.architectSortBy="orderCountAsc";
                    binding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.numOrderInc.setImageResource(R.drawable.ic_checked_radio_button);
                    binding.numOrderDec.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.orderAmountDec.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.orderAmountInc.setImageResource(R.drawable.ic_unchecked_radio_button);
                }
                Common.isArchitectFilterSelected(getActivity());
            }
        });
        binding.numOrderDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.architectSortBy.equalsIgnoreCase("orderCountDesc")){
                    Common.architectSortBy="orderCountDesc";
                    binding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.numOrderInc.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.numOrderDec.setImageResource(R.drawable.ic_checked_radio_button);
                    binding.orderAmountDec.setImageResource(R.drawable.ic_unchecked_radio_button);
                    binding.orderAmountInc.setImageResource(R.drawable.ic_unchecked_radio_button);
                }
                Common.isArchitectFilterSelected(getActivity());
            }
        });
        return view;
    }
}