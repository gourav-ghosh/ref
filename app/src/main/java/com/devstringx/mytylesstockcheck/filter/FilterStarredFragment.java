package com.devstringx.mytylesstockcheck.filter;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentFilterStarredBinding;

public class FilterStarredFragment extends Fragment {
    FragmentFilterStarredBinding filterStarredBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        filterStarredBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_filter_starred,container,false);
        View view=filterStarredBinding.getRoot();
        if (Common.filterStarMarked) filterStarredBinding.starredIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
        else filterStarredBinding.starredIv.setBackgroundResource(R.drawable.orange_checkbox_unselected);
        filterStarredBinding.starredIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!filterStarredBinding.starredIv.isSelected()){
                    filterStarredBinding.starredIv.setSelected(true);
                    Common.filterStarMarkedTemp=true;
                    filterStarredBinding.starredIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
                }
                else {
                    filterStarredBinding.starredIv.setSelected(false);
                    Common.filterStarMarkedTemp=false;
                    filterStarredBinding.starredIv.setBackgroundResource(R.drawable.orange_checkbox_unselected);
                }
//                Common.isFilterSelected(getActivity());
            }
        });
        return view;
    }
}