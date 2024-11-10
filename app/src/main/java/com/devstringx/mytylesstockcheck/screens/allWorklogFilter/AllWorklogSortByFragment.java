package com.devstringx.mytylesstockcheck.screens.allWorklogFilter;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentAllWorklogSortByBinding;

public class AllWorklogSortByFragment extends Fragment {
    FragmentAllWorklogSortByBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_all_worklog_sort_by, container, false);
        View view=binding.getRoot();
        if(Common.allWorklogSortBy.equalsIgnoreCase("createdAtDesc")) {
            binding.newToOld.setImageResource(R.drawable.ic_checked_radio_button);
        }
        else if(Common.allWorklogSortBy.equalsIgnoreCase("createdAtAsc")) {
            binding.oldToNew.setImageResource(R.drawable.ic_checked_radio_button);
        }
        Common.isAllWorklogFilterSelected(getActivity());
        binding.oldToNewLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Common.allWorklogSortBy.equalsIgnoreCase("createdAtAsc")) {
                    Common.allWorklogSortBy="createdAtAsc";
                    binding.oldToNew.setImageResource(R.drawable.ic_checked_radio_button);
                    binding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                }
                Common.isAllWorklogFilterSelected(getActivity());
            }
        });
        binding.newToOldLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Common.allWorklogSortBy.equalsIgnoreCase("createdAtDesc")) {
                    Common.allWorklogSortBy="createdAtDesc";
                    binding.newToOld.setImageResource(R.drawable.ic_checked_radio_button);
                    binding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                }
                Common.isAllWorklogFilterSelected(getActivity());
            }
        });
        return view;
    }
}