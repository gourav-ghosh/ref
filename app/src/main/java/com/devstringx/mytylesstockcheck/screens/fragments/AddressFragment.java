package com.devstringx.mytylesstockcheck.screens.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentAddressBinding;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Record;
import com.devstringx.mytylesstockcheck.screens.BillingAddressActivity;
import com.devstringx.mytylesstockcheck.screens.ShippingAddressActivity;

public class AddressFragment extends Fragment {

    private FragmentAddressBinding binding;
    private Record mRecord;
    private boolean isBillingAddress,mIsEditable;
    public AddressFragment(Record record, boolean b,boolean isEditable) {
        // Required empty public constructor
        mRecord=record;
        isBillingAddress=b;
        mIsEditable=isEditable;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_address,container,false);
        View view=binding.getRoot();
        if (isBillingAddress){
            String text = "<font color=#FF000000>Billing Address</font> <font color=#FF0000>*</font>";
            binding.titleTv.setText(Html.fromHtml(text));

            if(mRecord.getBillingAddress()!=null) {
                binding.addAddressIv.setVisibility(View.GONE);
                String billingAdd = "";
                billingAdd += Common.getData(mRecord.getBillingAddress().getAddress_line_1()) + ",";
                billingAdd += Common.getData(mRecord.getBillingAddress().getAddress_line_2()) + ",\n";
                billingAdd += Common.getData(mRecord.getBillingAddress().getBilling_city()) + "-";
                billingAdd += Common.getData(String.valueOf(mRecord.getBillingAddress().getBilling_pincode())) + "\n";
                billingAdd += "PH : " + Common.getData(mRecord.getPrimaryPhone()) + "\n";
                billingAdd += "GSTIN/UIN : " + Common.getData(mRecord.getBillingAddress().getGst_number()) + "\n";
                billingAdd += "State Name : " + Common.getData(mRecord.getBillingAddress().getBilling_state());
                binding.addressTv.setText(billingAdd);
            }else{
                binding.editAddressIv.setVisibility(View.GONE);
            }
            binding.addAddressIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), BillingAddressActivity.class);
                    intent.putExtra("type","billing");
                    intent.putExtra("lead_id",mRecord.getLeadId());
                    intent.putExtra("site_in_charge_mobile_number",mRecord.getPrimaryPhone());
                    startActivityForResult(intent,3000);

                }
            });
            binding.editAddressIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), BillingAddressActivity.class);
                    intent.putExtra("type","billing");
                    intent.putExtra("data",mRecord.getBillingAddress());
                    intent.putExtra("lead_id",mRecord.getLeadId());
                    intent.putExtra("site_in_charge_mobile_number",mRecord.getPrimaryPhone());
                    startActivityForResult(intent,3000);

                }
            });
        }else{
            if(mRecord.getShippingAddresses().size()>0) {
                for (int i = 0; i < mRecord.getShippingAddresses().size(); i++) {
                    if(mRecord.getShippingAddresses().get(i).isAsDefault()){
                        String billingAdd = "";
                        billingAdd += Common.getData(mRecord.getShippingAddresses().get(i).getAddressLine1()) + ",";
                        billingAdd += Common.getData(mRecord.getShippingAddresses().get(i).getAddressLine2()) + ",\n";
                        billingAdd += Common.getData(mRecord.getShippingAddresses().get(i).getShippingCity()) + "-";
                        billingAdd += Common.getData(String.valueOf(mRecord.getShippingAddresses().get(i).getShippingPincode())) + "\n";
                        billingAdd += "PH : " + Common.getData(mRecord.getShippingAddresses().get(i).getSiteInChargeMobileNumber()) + "\n";
                        billingAdd += "GSTIN/UIN : " + Common.getData(mRecord.getShippingAddresses().get(i).getGstNumber()) + "\n";
                        billingAdd += "State Name : " + Common.getData(mRecord.getShippingAddresses().get(i).getShippingState())+ "\n";
                        billingAdd += "Landmark : " + Common.getData(mRecord.getShippingAddresses().get(i).getLandmark());
                       Common.showLog("SHIPPING ADDRESS==="+billingAdd);
                        binding.addressTv.setText(billingAdd);
                        break;
                    }
                }

            }else{
                binding.editAddressIv.setVisibility(View.GONE);
            }

            binding.addAddressIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), ShippingAddressActivity.class);
                    intent.putExtra("data",mRecord);
                    intent.putExtra("lead_id",mRecord.getLeadId());
                    intent.putExtra("site_in_charge_mobile_number",mRecord.getPrimaryPhone());
                    startActivityForResult(intent,3000);

                }
            });
            binding.editAddressIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), BillingAddressActivity.class);
                    intent.putExtra("type","shipping");
                    if(mRecord.getShippingAddresses().size()>0) {
                        for (int i = 0; i < mRecord.getShippingAddresses().size(); i++) {
                            if (mRecord.getShippingAddresses().get(i).isAsDefault()) {
                                intent.putExtra("data",mRecord.getShippingAddresses().get(i));
                            }
                        }
                    }
                    intent.putExtra("lead_id",mRecord.getLeadId());
                    startActivityForResult(intent,3000);

                }
            });
        }
        if(!mIsEditable){
            binding.editAddressIv.setVisibility(View.GONE);
            binding.addAddressIv.setVisibility(View.GONE);
        }
        return view;
    }
}