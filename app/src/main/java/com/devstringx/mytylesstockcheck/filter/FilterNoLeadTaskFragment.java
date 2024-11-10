package com.devstringx.mytylesstockcheck.filter;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentFilterNoLeadTaskBinding;

public class FilterNoLeadTaskFragment extends Fragment {
    FragmentFilterNoLeadTaskBinding noLeadTaskBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        noLeadTaskBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_filter_no_lead_task,container,false);
        View view=noLeadTaskBinding.getRoot();
        if (Common.filterNoLeadTask) noLeadTaskBinding.starredIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
        else noLeadTaskBinding.starredIv.setBackgroundResource(R.drawable.orange_checkbox_unselected);
        noLeadTaskBinding.starredIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!noLeadTaskBinding.starredIv.isSelected()){
                    noLeadTaskBinding.starredIv.setSelected(true);
                    Common.filterNoLeadTaskTemp=true;
                    noLeadTaskBinding.starredIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
                }
                else {
                    noLeadTaskBinding.starredIv.setSelected(false);
                    Common.filterNoLeadTaskTemp=false;
                    noLeadTaskBinding.starredIv.setBackgroundResource(R.drawable.orange_checkbox_unselected);
                }
//                Common.isFilterSelected(getActivity());
            }
        });
        return view;
    }
}