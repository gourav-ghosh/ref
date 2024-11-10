package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.ShippingAddresses;

import java.util.ArrayList;
import java.util.List;

public class ShippingAddAutoCompleteListAdapter extends ArrayAdapter<String> {

    private ArrayList<ShippingAddresses> fullList;
    private String phoneno="",gstno="",staten="";
    public ShippingAddAutoCompleteListAdapter(Context context, int resource, int textViewResourceId, List<ShippingAddresses> objects) {
        super(context, resource, textViewResourceId);
        fullList = (ArrayList<ShippingAddresses>) objects;
    }
    public void refreshList(List<ShippingAddresses> objects){
        fullList = (ArrayList<ShippingAddresses>) objects;
        notifyDataSetChanged();
    }

    public ArrayList<ShippingAddresses> getFullList() {
        return fullList;
    }

    @Override
    public int getCount() {
        return fullList.size();
    }

    @Override
    public String getItem(int position) {
        String billingAdd="";
        billingAdd+= Common.getData(fullList.get(position).getAddressLine1())+",";
        billingAdd+=Common.getData(fullList.get(position).getAddressLine2())+",\n";
        billingAdd+=Common.getData(fullList.get(position).getShippingCity())+"-";
        billingAdd+=Common.getData(fullList.get(position).getShippingPincode())+"\n";
        billingAdd+="PH : "+Common.getData(phoneno)+"\n";
        billingAdd+="GSTIN/UIN : "+Common.getData(gstno)+"\n";
        billingAdd+="State Name : "+Common.getData(staten);
        return billingAdd;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public void setGstno(String gstno) {
        this.gstno = gstno;
    }

    public void setStaten(String staten) {
        this.staten = staten;
    }
}