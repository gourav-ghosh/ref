package com.devstringx.mytylesstockcheck.screens.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentPickUpOrHomeDeliveryBinding;
import com.devstringx.mytylesstockcheck.datamodal.quotationDetails.Records;
import com.devstringx.mytylesstockcheck.screens.ProcessForDispatchActivity;

import java.util.Timer;
import java.util.TimerTask;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class PickUpOrHomeDeliveryFragment extends Fragment {
    FragmentPickUpOrHomeDeliveryBinding pickUpOrHomeDeliveryBinding;
    Records mRecord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pickUpOrHomeDeliveryBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_pick_up_or_home_delivery, container, false);
        View view=pickUpOrHomeDeliveryBinding.getRoot();

        mRecord = (Records) getArguments().getSerializable("data");

        pickUpOrHomeDeliveryBinding.homeDeliveryIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mRecord,"home");
            }
        });

        pickUpOrHomeDeliveryBinding.pickUpIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mRecord,"pick-up");
            }
        });

        return view;
    }

    private void startActivity(Records records,String delivery) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                removefragment();
            }
        }, 2000);

        Intent intent = new Intent(getActivity(), ProcessForDispatchActivity.class);
        intent.putExtra("deliveryType",delivery);
        intent.putExtra("data", records);
        startActivity(intent);
    }

    private void removefragment() {

        if (getActivity() != null) {
            try {
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            } catch (Exception e) {
                Common.showLog(e.getMessage());
                e.printStackTrace();
            }
        }
    }


}