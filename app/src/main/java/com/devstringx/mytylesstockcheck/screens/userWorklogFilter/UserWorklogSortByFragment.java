package com.devstringx.mytylesstockcheck.screens.userWorklogFilter;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentUserWorklogSortByBinding;

public class UserWorklogSortByFragment extends Fragment {
    FragmentUserWorklogSortByBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_worklog_sort_by, container, false);
        View view=binding.getRoot();
        if(Common.userWorklogSortBy.equalsIgnoreCase("createdAtDesc"))
            binding.newToOld.setImageResource(R.drawable.ic_checked_radio_button);
        else if (Common.userWorklogSortBy.equalsIgnoreCase("createdAtAsc")) {
            binding.oldToNew.setImageResource(R.drawable.ic_checked_radio_button);
        }
        Common.isUserWorklogFilterSelected(getActivity());
        binding.oldToNewLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Common.userWorklogSortBy.equalsIgnoreCase("createdAtAsc")) {
                    Common.userWorklogSortBy = "createdAtAsc";
                    binding.oldToNew.setImageResource(R.drawable.ic_checked_radio_button);
                    binding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                }
                Common.isUserWorklogFilterSelected(getActivity());
            }
        });
        binding.newToOldLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Common.userWorklogSortBy.equalsIgnoreCase("createdAtDesc")) {
                    Common.userWorklogSortBy = "createdAtDesc";
                    binding.newToOld.setImageResource(R.drawable.ic_checked_radio_button);
                    binding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                }
                Common.isUserWorklogFilterSelected(getActivity());
            }
        });
        return view;
    }
}