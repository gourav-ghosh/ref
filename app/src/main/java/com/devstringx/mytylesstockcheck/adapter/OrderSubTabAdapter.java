package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.OrderTabLayoutBinding;
import com.devstringx.mytylesstockcheck.databinding.OutstationSubtabsLayoutBinding;
import com.devstringx.mytylesstockcheck.datamodal.ordersTabData.CustomEntry;
import com.devstringx.mytylesstockcheck.screens.fragments.OrderListingFragment;
import com.google.gson.JsonElement;

import java.util.List;

public class OrderSubTabAdapter extends RecyclerView.Adapter<OrderSubTabAdapter.ViewHolder> {
    private List<CustomEntry<String, JsonElement>> statusList;
    private Context context;
    private OnSubTabClick onTabClick;
    private OrderListingFragment orderListingFragment;

    public OrderSubTabAdapter(Context context, List<CustomEntry<String, JsonElement>> statusList, OnSubTabClick onTabClick) {
        this.context = context;
        this.statusList = statusList;
        this.onTabClick = onTabClick;
        if (!statusList.isEmpty()) {
            statusList.get(0).setSelected(true);
            Common.OutstationSelectedSubtab = "Spoton";
            Common.showLog("1====" + Common.OutstationSelectedSubtab);
        }
    }

    @NonNull
    @Override
    public OrderSubTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OutstationSubtabsLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.outstation_subtabs_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSubTabAdapter.ViewHolder holder, int position) {
        CustomEntry<String, JsonElement> currentItem = statusList.get(position);
        holder.binding.tabCountTV.setText(currentItem.getValue().toString());
        if (currentItem.getKey().toString().equalsIgnoreCase("Outstation_Spoton"))
            holder.binding.tabNameTV.setText("Spoton");
        else if (currentItem.getKey().toString().equalsIgnoreCase("Outstation_Jabbar"))
            holder.binding.tabNameTV.setText("Jabbar");
        else holder.binding.tabNameTV.setText("Others");
//        holder.binding.tabNameTV.setText(currentItem.getKey().toString());
        holder.binding.tabNameTV.setSelected(currentItem.isSelected());
        holder.binding.tabNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentItem.isSelected()) {
                    for (CustomEntry<String, JsonElement> entry : statusList) {
                        entry.setSelected(false);
                    }
                    currentItem.setSelected(true);
                    notifyDataSetChanged();
                }
                Common.OutstationSelectedSubtab = holder.binding.tabNameTV.getText().toString();
                onTabClick.onSubTabClickListener();
                Common.showLog("2====" + Common.OutstationSelectedSubtab);
            }
        });
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OutstationSubtabsLayoutBinding binding;

        public ViewHolder(@NonNull OutstationSubtabsLayoutBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }

    public interface OnSubTabClick {
        public void onSubTabClickListener();
    }
}
