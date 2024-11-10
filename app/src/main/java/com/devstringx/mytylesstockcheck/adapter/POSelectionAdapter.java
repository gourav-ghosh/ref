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
import com.devstringx.mytylesstockcheck.databinding.AssignPoItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.POItem;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;

import java.util.List;

public class POSelectionAdapter extends RecyclerView.Adapter<POSelectionAdapter.ViewHolder> {
    private List<POItem> allPOList;
    private List<String> selectedPOList;
    private Context context;
    public int helpingAgentCount = 0;
    private boolean isMainAgent;
    private int currentHelpingAgent;
    private int lastHelpingAgent;
    private OrderDetailActivity orderDetailActivity;
    private int helpingAgentPosition;
    private AssignMultipleHelpingAgentsAdapter helpingAgentsAdapter;
    public POSelectionAdapter(Context context, OrderDetailActivity orderDetailActivity, AssignMultipleHelpingAgentsAdapter helpingAgentsAdapter, int helpingAgentPosition, List<POItem> allPOList, List<String> selectedPOList, boolean isMainAgent, int currentHelpingAgent, int lastHelpingAgent) {
        this.allPOList = allPOList;
        this.helpingAgentsAdapter = helpingAgentsAdapter;
        this.orderDetailActivity = orderDetailActivity;
        this.context = context;
        this.selectedPOList = selectedPOList;
        this.isMainAgent = isMainAgent;
        this.currentHelpingAgent = currentHelpingAgent;
        this.lastHelpingAgent = lastHelpingAgent;
        this.helpingAgentPosition = helpingAgentPosition;
    }

    @NonNull
    @Override
    public POSelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AssignPoItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.assign_po_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull POSelectionAdapter.ViewHolder holder, int position) {
        POItem currentItem = allPOList.get(position);
        if(!isMainAgent) {
            if(currentHelpingAgent == lastHelpingAgent) {
                if(selectedPOList.contains(String.valueOf(currentItem.getId()))) {
                    showPOOptions(holder, currentItem, true);
                }
                else if(!(orderDetailActivity.getAllCheckedPOList().contains(String.valueOf(currentItem.getId())))) {
                    showPOOptions(holder, currentItem, false);
                }
                else {
                    holder.binding.poOptions.setVisibility(View.GONE);
                }
            }
            else {
                if(selectedPOList.contains(String.valueOf(currentItem.getId()))) {
                    showPOOptions(holder, currentItem, false);
                }
                else {
                    holder.binding.poOptions.setVisibility(View.GONE);
                }
            }
        }
        else {
            showPOOptions(holder, currentItem, false);
        }
    }

    public void showPOOptions(@NonNull POSelectionAdapter.ViewHolder holder, POItem currentItem, boolean isAlreadySelected) {
        holder.binding.poOptions.setVisibility(View.VISIBLE);
        String name=currentItem.getName();
        if(!isMainAgent) selectedPOList=helpingAgentsAdapter.getSelectedPOList(helpingAgentPosition);
        holder.binding.poNameTV.setText(name);
        currentItem.setSelected(true);
        orderDetailActivity.updateAllCheckedPOList(String.valueOf(currentItem.getId()), "add");
        holder.binding.poOptionIV.setImageResource(R.drawable.orange_checkbox_selected);
        if(isMainAgent)
            selectedPOList.add(String.valueOf(currentItem.getId()));
        else if(!isAlreadySelected)
            helpingAgentsAdapter.addSelectedPOList(helpingAgentPosition, String.valueOf(currentItem.getId()));

        orderDetailActivity.checkPoRemaining();
        holder.binding.poOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(isMainAgent && helpingAgentCount != 0)) {
                    if(currentItem.isSelected()) {
                        holder.binding.poOptionIV.setImageResource(R.drawable.orange_checkbox_unselected);
                        if(isMainAgent)
                            selectedPOList.remove(String.valueOf((currentItem.getId())));
                        else
                            helpingAgentsAdapter.removeSelectedPOList(helpingAgentPosition, String.valueOf(currentItem.getId()));

                        orderDetailActivity.updateAllCheckedPOList(String.valueOf(currentItem.getId()), "remove");
                        currentItem.setSelected(false);
                    }
                    else {
                        holder.binding.poOptionIV.setImageResource(R.drawable.orange_checkbox_selected);
                        if(isMainAgent)
                            selectedPOList.add(String.valueOf(currentItem.getId()));
                        else
                            helpingAgentsAdapter.addSelectedPOList(helpingAgentPosition, String.valueOf(currentItem.getId()));
                        orderDetailActivity.updateAllCheckedPOList(String.valueOf(currentItem.getId()), "add");
                        currentItem.setSelected(true);
                    }
                    orderDetailActivity.checkPoRemaining();
                }
                else {
                    Common.showToast(context, "Cannot select POs for main delivery agent once you start assigning helping agents");
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return allPOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AssignPoItemBinding binding;

        public ViewHolder(@NonNull AssignPoItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }

    public List<String> getSelectedPOList() {
        return selectedPOList;
    }

}
