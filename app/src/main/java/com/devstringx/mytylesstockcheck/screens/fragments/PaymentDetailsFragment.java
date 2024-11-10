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
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.FragmentPaymentDetailsBinding;
import com.devstringx.mytylesstockcheck.datamodal.Data;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;

public class PaymentDetailsFragment extends Fragment {
    FragmentPaymentDetailsBinding binding;
    DashboardViewPager2Adapter adapter;
    private String userRoleId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_details, container, false);
        View view = binding.getRoot();
        userRoleId = new PreferenceUtils(getContext()).getStringFromPreference(PreferenceUtils.ROLEID,"");
//        adapter = new DashboardViewPager2Adapter(getParentFragmentManager(), getLifecycle());
//        adapter.addFrag(new PaymentDetailSubTabFragment(), "subTab_paymentDetail");
//        adapter.addFrag(new ShippingChargesSubTabFragment(), "subTab_ShippingCharge");
        if (getActivity().getIntent().getStringExtra("source_page") != null) {
            if (getActivity().getIntent().getStringExtra("source_page").equalsIgnoreCase("shipping_charges")) {
                binding.shippingRb.setChecked(true);
                if (binding.shippingRb.isChecked())
                    Common.loadFragment(getParentFragmentManager(), new ShippingChargesSubTabFragment(), R.id.view_pager);
            }else{
                binding.paymentDetailsRb.setChecked(true);
                if (binding.paymentDetailsRb.isChecked())
                    Common.loadFragment(getParentFragmentManager(), new PaymentDetailSubTabFragment(), R.id.view_pager);
            }
        }else {
            if(userRoleId.equalsIgnoreCase("8") || userRoleId.equalsIgnoreCase("9")) {
                binding.paymentDetailsRb.setVisibility(View.GONE);
                binding.shippingRb.setChecked(true);
                if (binding.shippingRb.isChecked())
                    Common.loadFragment(getParentFragmentManager(), new ShippingChargesSubTabFragment(), R.id.view_pager);
            }
            else {
                binding.paymentDetailsRb.setChecked(true);
                if (binding.paymentDetailsRb.isChecked())
                    Common.loadFragment(getParentFragmentManager(), new PaymentDetailSubTabFragment(), R.id.view_pager);
            }
        }
        binding.rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (binding.paymentDetailsRb.isChecked())
                    Common.loadFragment(getParentFragmentManager(),new PaymentDetailSubTabFragment(),R.id.view_pager);
//                    binding.viewPager.setCurrentItem(adapter.getFragPosition("subTab_paymentDetail"));
                else
                    Common.loadFragment(getParentFragmentManager(),new ShippingChargesSubTabFragment(),R.id.view_pager);
//                    binding.viewPager.setCurrentItem(adapter.getFragPosition("subTab_ShippingCharge"));
            }
        });
//        binding.viewPager.setAdapter(adapter);
//        binding.viewPager.setUserInputEnabled(false);
        return view;
    }
}