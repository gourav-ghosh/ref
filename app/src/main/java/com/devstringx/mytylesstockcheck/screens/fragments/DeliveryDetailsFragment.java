package com.devstringx.mytylesstockcheck.screens.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.DashboardViewPager2Adapter;
import com.devstringx.mytylesstockcheck.databinding.FragmentDeliveryDetailsBinding;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.Records;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;

public class DeliveryDetailsFragment extends Fragment {
    FragmentDeliveryDetailsBinding binding;
    DashboardViewPager2Adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_delivery_details, container, false);
        View view=binding.getRoot();
        binding.deliveryDetailsRb.setChecked(true);
        adapter = new DashboardViewPager2Adapter(getParentFragmentManager(),getLifecycle());
        adapter.addFrag(new DeliveryDetailsSubTabFragment(),"subTab_deliveryDetail");
        adapter.addFrag(new PurchaseDetailsSubTabFragment(),"subTab_purchaseDetail");
        binding.rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (binding.deliveryDetailsRb.isChecked()){
                    binding.viewPager.setCurrentItem(adapter.getFragPosition("subTab_deliveryDetail"));
                }else {
                    binding.viewPager.setCurrentItem(adapter.getFragPosition("subTab_purchaseDetail"));
                }
            }
        });
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setUserInputEnabled(false);
        return view;
    }
}