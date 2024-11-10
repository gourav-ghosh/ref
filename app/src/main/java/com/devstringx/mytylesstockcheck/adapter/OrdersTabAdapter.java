package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.OrderTabLayoutBinding;
import com.devstringx.mytylesstockcheck.datamodal.ordersTabData.CustomEntry;
import com.devstringx.mytylesstockcheck.screens.fragments.OrderListingFragment;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class OrdersTabAdapter extends RecyclerView.Adapter<OrdersTabAdapter.ViewHolder> {
    public List<CustomEntry<String, JsonElement>> statusList;
    private Context context;
    private OnTabClick onTabClick;

    public OrdersTabAdapter(Context context, List<CustomEntry<String, JsonElement>> statusList, OnTabClick onTabClick) {
        this.context = context;
        this.statusList = statusList;
        this.onTabClick = onTabClick;
        if (!statusList.isEmpty() && Common.OrderSelectedTabName.equalsIgnoreCase("")) {
            statusList.get(0).setSelected(true);
            Common.OrderSelectedTabName = statusList.get(0).getKey().toString();
            Common.showLog("1====" + Common.OrderSelectedTabName);
        }else {
            for (int i = 0; i < statusList.size(); i++) {
                if (statusList.get(i).getKey().toString().equalsIgnoreCase(Common.OrderSelectedTabName)) statusList.get(i).setSelected(true);
                else statusList.get(i).setSelected(false);
            }
        }
    }
    public void UpdateSelectedTab(RecyclerView recyclerView,int pos) {
        if (pos >= 0 && pos < statusList.size()) {
            recyclerView.smoothScrollToPosition(pos);
            onTabClick.onTabClickListener();
        }
    }
    @NonNull
    @Override
    public OrdersTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderTabLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.order_tab_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersTabAdapter.ViewHolder holder, int position) {
        CustomEntry<String, JsonElement> currentItem = statusList.get(position);
        holder.binding.tabCountTV.setText(currentItem.getValue().toString());
        holder.binding.tabNameTV.setText(currentItem.getKey().toString());
        holder.binding.tabNameTV.setSelected(currentItem.isSelected());
        holder.binding.tabNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.OutstationSelectedSubtab = "";
                if (!currentItem.isSelected()) {
                    for (CustomEntry<String, JsonElement> entry : statusList) {
                        entry.setSelected(false);
                    }
                    currentItem.setSelected(true);
                    notifyDataSetChanged();
                }
                clearFilter();
                Common.OrderSelectedTabName = holder.binding.tabNameTV.getText().toString();
                onTabClick.onTabClickListener();
                Common.showLog("2====" + Common.OrderSelectedTabName);
            }
        });
    }

    private void clearFilter() {
        Common.orderDateType="";
        Common.orderStartDate="";
        Common.orderEndDate="";
        Common.selectedOrderStatus=new ArrayList<>();
        Common.selectedDeliveryType=new ArrayList<>();
        Common.selectedOrderType=new ArrayList<>();
        Common.selectedOrderPaymentMode=new ArrayList<>();
        Common.selectedPOCodes=new ArrayList<>();
        Common.selectedOrderDisManagerName=new ArrayList<>();
        Common.selectedOrderSupExeName=new ArrayList<>();
        Common.selectedOrderDeliveryAgentName=new ArrayList<>();
        Common.orderFilterSortby="createdAtDesc";
        Common.tempOrderDateType="";
        Common.tempSelectedOrderStatus=new ArrayList<>();
        Common.tempSelectedDeliveryType=new ArrayList<>();
        Common.tempSelectedOrderType=new ArrayList<>();
        Common.tempSelectedOrderPaymentMode=new ArrayList<>();
        Common.tempSelectedPOCodes=new ArrayList<>();
        Common.tempSelectedOrderDisManagerName=new ArrayList<>();
        Common.tempSelectedOrderSupExeName=new ArrayList<>();
        Common.tempSelectedOrderDeliveryAgentName=new ArrayList<>();
        Common.tempOrderSortby="createdAtDesc";
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OrderTabLayoutBinding binding;

        public ViewHolder(@NonNull OrderTabLayoutBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }

    public interface OnTabClick {
        public void onTabClickListener();
    }
}
