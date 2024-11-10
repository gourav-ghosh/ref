package com.devstringx.mytylesstockcheck.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.AddressListItemBinding;
import com.devstringx.mytylesstockcheck.databinding.LeadListItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.allLeads.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.ShippingAddresses;

import java.util.ArrayList;
import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {
    private AddressListClick clickBtn;

    private Context mContext;
    private List<ShippingAddresses> listItems=new ArrayList<>();
    public AddressListAdapter(Context context,List<ShippingAddresses> recordsItems, AddressListClick clickBtn) {
        this.clickBtn=clickBtn;
        this.mContext=context;
        listItems=recordsItems;
    }

    public void refreshList(List<ShippingAddresses> recordsItems){
        listItems=recordsItems;
        notifyDataSetChanged();
    }

    public List<ShippingAddresses> getListItems() {
        return listItems;
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull AddressListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ShippingAddresses mRecord=listItems.get(position);
        String billingAdd = "";
        billingAdd += Common.getData(mRecord.getAddressLine1()) + ",";
        billingAdd += Common.getData(mRecord.getAddressLine2()) + ",\n";
        billingAdd += Common.getData(mRecord.getShippingCity()) + "-";
        billingAdd += Common.getData(String.valueOf(mRecord.getShippingPincode())) + "\n";
        billingAdd += "PH : " + Common.getData(mRecord.getSiteInChargeMobileNumber()) + "\n";
        billingAdd += "GSTIN/UIN : " + Common.getData(mRecord.getGstNumber()) + "\n";
        billingAdd += "State Name : " + Common.getData(mRecord.getShippingState());
        holder.binding.addressTv.setText(billingAdd);
        if(mRecord.isAsDefault()){
            holder.binding.defaultTv.setVisibility(View.GONE);
            holder.binding.removeTv.setVisibility(View.GONE);
            holder.binding.itemLl.setBackgroundResource(R.drawable.fade_orange_bg10);
        }else{
            holder.binding.defaultTv.setVisibility(View.VISIBLE);
            holder.binding.removeTv.setVisibility(View.VISIBLE);
            holder.binding.itemLl.setBackgroundResource(R.drawable.grey_round_bg);
        }

        holder.binding.editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBtn.onClickListner(position,"edit");
            }
        });
        holder.binding.removeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBtn.onClickListner(position,"remove");
            }
        });
        holder.binding.defaultTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBtn.onClickListner(position,"default");
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AddressListItemBinding viewProductCategoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.address_list_item, parent, false);
        return new ViewHolder(viewProductCategoryBinding);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private AddressListItemBinding binding;
        public ViewHolder(AddressListItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
    public interface AddressListClick{
        void onClickListner(int position,String type);
    }

}
