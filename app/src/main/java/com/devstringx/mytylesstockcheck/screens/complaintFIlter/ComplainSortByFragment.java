package com.devstringx.mytylesstockcheck.screens.complaintFIlter;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentComplainSortByBinding;

public class ComplainSortByFragment extends Fragment {
    FragmentComplainSortByBinding complainSortByBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        complainSortByBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_complain_sort_by, container, false);
        View view=complainSortByBinding.getRoot();
        if (Common.complaintSortBy.equalsIgnoreCase("ticketNumberDesc")){
            complainSortByBinding.dec.setImageResource(R.drawable.ic_checked_radio_button);
        } else if (Common.complaintSortBy.equalsIgnoreCase("ticketNumberAsc")) {
            complainSortByBinding.inc.setImageResource(R.drawable.ic_checked_radio_button);
        }else if(Common.complaintSortBy.equalsIgnoreCase("createdAtAsc")){
            complainSortByBinding.oldToNew.setImageResource(R.drawable.ic_checked_radio_button);
        }else if(Common.complaintSortBy.equalsIgnoreCase("createdAtDesc")){
            complainSortByBinding.newToOld.setImageResource(R.drawable.ic_checked_radio_button);
        }
        Common.isComplaintFilterSelected(getActivity());
        complainSortByBinding.dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.complaintSortBy.equalsIgnoreCase("ticketNumberDesc")){
                    Common.complaintSortBy="ticketNumberDesc";
                    complainSortByBinding.dec.setImageResource(R.drawable.ic_checked_radio_button);
                    complainSortByBinding.inc.setImageResource(R.drawable.ic_unchecked_radio_button);
                    complainSortByBinding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                    complainSortByBinding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                }
                Common.isComplaintFilterSelected(getActivity());
            }
        });
        complainSortByBinding.inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.complaintSortBy.equalsIgnoreCase("ticketNumberAsc")){
                    Common.complaintSortBy="ticketNumberAsc";
                    complainSortByBinding.dec.setImageResource(R.drawable.ic_unchecked_radio_button);
                    complainSortByBinding.inc.setImageResource(R.drawable.ic_checked_radio_button);
                    complainSortByBinding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                    complainSortByBinding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                }
                Common.isComplaintFilterSelected(getActivity());
            }
        });
        complainSortByBinding.newToOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.complaintSortBy.equalsIgnoreCase("createdAtDesc")){
                    Common.complaintSortBy="createdAtDesc";
                    complainSortByBinding.dec.setImageResource(R.drawable.ic_unchecked_radio_button);
                    complainSortByBinding.inc.setImageResource(R.drawable.ic_unchecked_radio_button);
                    complainSortByBinding.oldToNew.setImageResource(R.drawable.ic_unchecked_radio_button);
                    complainSortByBinding.newToOld.setImageResource(R.drawable.ic_checked_radio_button);
                }
                Common.isComplaintFilterSelected(getActivity());
            }
        });
        complainSortByBinding.oldToNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.complaintSortBy.equalsIgnoreCase("createdAtAsc")){
                    Common.complaintSortBy="createdAtAsc";
                    complainSortByBinding.dec.setImageResource(R.drawable.ic_unchecked_radio_button);
                    complainSortByBinding.inc.setImageResource(R.drawable.ic_unchecked_radio_button);
                    complainSortByBinding.oldToNew.setImageResource(R.drawable.ic_checked_radio_button);
                    complainSortByBinding.newToOld.setImageResource(R.drawable.ic_unchecked_radio_button);
                }
                Common.isComplaintFilterSelected(getActivity());
            }
        });
        return view;
    }
}