package com.devstringx.mytylesstockcheck.screens.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.FragmentComplaintStatusTabBinding;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.Data;
import com.devstringx.mytylesstockcheck.screens.ComplaintDetailsActivity;

public class ComplaintStatusTabFragment extends Fragment {
    FragmentComplaintStatusTabBinding binding;
    private Data complaintDetailResponse;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_complaint_status_tab, container, false);
        View view = binding.getRoot();
        complaintDetailResponse = ((ComplaintDetailsActivity) getActivity()).complaintDetailResponse;
        setData(complaintDetailResponse);
        return view;
    }

    private void setData(Data data) {
        if (data.getComplaintActivities().getCreated()!=null){
            binding.createdText.setText(data.getComplaintActivities().getCreated().getActivity());
            binding.createdbyNameTV.setText(data.getComplaintActivities().getCreated().getActivityByName());
            binding.createdDateTimeTV.setText(data.getComplaintActivities().getCreated().getActivityCreatedAt().toString());
            if (data.getComplaintActivities().getCustomerDetailsUpdate()!=null){
                binding.customerDetailUpdatedRl.setVisibility(View.VISIBLE);
                binding.customerDetailUpdated.setText(data.getComplaintActivities().getCustomerDetailsUpdate().getActivity());
                binding.processedbyNameTV.setText(data.getComplaintActivities().getCustomerDetailsUpdate().getActivityByName());
                binding.processDateTimeTV.setText(data.getComplaintActivities().getCustomerDetailsUpdate().getActivityCreatedAt());
            } else {
                binding.customerDetailUpdatedRl.setVisibility(View.GONE);
            }
            if (data.getComplaintActivities().getResulationDateChange()!=null){
                binding.dateChangedRl.setVisibility(View.VISIBLE);
                binding.resolutionDateChangedText.setText(data.getComplaintActivities().getResulationDateChange().getActivity().toString());
                binding.resProcessedbyNameTV.setText(data.getComplaintActivities().getResulationDateChange().getActivityByName());
                binding.resDateTimeTV.setText(data.getComplaintActivities().getResulationDateChange().getActivityCreatedAt());
            } else {
                binding.dateChangedRl.setVisibility(View.GONE);
            }
            if (data.getComplaintActivities().getInprogress()!=null){
                binding.inprogressIV.setBackgroundResource(R.drawable.light_green_round4bg);
                binding.inprogressIV.setColorFilter(ContextCompat.getColor(getContext(), R.color.green), PorterDuff.Mode.SRC_IN);
                binding.inprogressProcessbyText.setText(data.getComplaintActivities().getInprogress().getActivityByName());
                binding.inprogressText.setText(data.getComplaintActivities().getInprogress().getActivity());
                binding.inprogressDateTimeTV.setText(data.getComplaintActivities().getInprogress().getActivityCreatedAt());
            } else {
                binding.inprogressIV.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray), PorterDuff.Mode.SRC_IN);
                binding.inprogressIV.setBackgroundResource(R.drawable.grayish_round_4bg);
                binding.inprogressProcessedbyNameTV.setText("N/A");
                binding.inprogressText.setText("Not Inprogress");
            }
            if (data.getComplaintActivities().getResolved()!=null){
                binding.resolvedIV.setBackgroundResource(R.drawable.light_green_round4bg);
                binding.resolvedIV.setColorFilter(ContextCompat.getColor(getContext(), R.color.green), PorterDuff.Mode.SRC_IN);
                binding.resolvedText.setText(data.getComplaintActivities().getResolved().getActivity());
                binding.resolvedProcessbyText.setText(data.getComplaintActivities().getResolved().getActivityByName());
                binding.resDateTimeTV.setText(data.getComplaintActivities().getResolved().getActivityCreatedAt());
            } else {
                binding.resolvedIV.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray), PorterDuff.Mode.SRC_IN);
                binding.resolvedIV.setBackgroundResource(R.drawable.grayish_round_4bg);
                binding.resolvedText.setText("Not Resolved");
                binding.resolvedProcessedbyNameTV.setText("N/A");
                binding.resDateTimeTV.setText("N/A");
            }
        }else {
            binding.createdText.setText("Not Created");
            binding.createdbyNameTV.setText("N/A");
            binding.createdDateTimeTV.setText("N/A");
        }
    }
}