package com.devstringx.mytylesstockcheck.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.HelpingAgentAssigningFormBinding;
import com.devstringx.mytylesstockcheck.datamodal.HelpingAgentData;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.POItem;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class AssignMultipleHelpingAgentsAdapter extends RecyclerView.Adapter<AssignMultipleHelpingAgentsAdapter.ViewHolder> {
    public int helping_agent_count = 0;
    public boolean isFirstLoad = true;
    private RemoveAgent removeAgent;
    private Context context;
    public List<HelpingAgentData> helpingAgentList = new ArrayList<>();
    private List<RecordsItem> agentNameList = new ArrayList<>();
    private List<POItem> poDocs = new ArrayList<>();
    private List<String> selectedPOList = new ArrayList<>();
    private List<String> allCheckedPOList = new ArrayList<>();
    private AutoCompleteTextView assignAgent;
    private OrderDetailActivity orderDetailActivity;

    private List<String> expectedDeliveryTimeList = new ArrayList<>();
    public AssignMultipleHelpingAgentsAdapter(Context context, OrderDetailActivity orderDetailActivity, RemoveAgent removeAgent, List<RecordsItem> agentNameList, List<POItem> poDocs, List<String> selectedPOList, List<String> allCheckedPOList, List<String> expectedDeliveryTimeList) {
        this.context=context;
        this.poDocs.addAll(poDocs);
        this.agentNameList.addAll(agentNameList);
        this.selectedPOList.addAll(selectedPOList);
        this.removeAgent = removeAgent;
        this.orderDetailActivity = orderDetailActivity;
        this.allCheckedPOList.addAll(allCheckedPOList);
        this.expectedDeliveryTimeList.addAll(expectedDeliveryTimeList);
    }
    @NonNull
    @Override
    public AssignMultipleHelpingAgentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HelpingAgentAssigningFormBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.helping_agent_assigning_form, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignMultipleHelpingAgentsAdapter.ViewHolder holder, int position) {
        if(!helpingAgentList.isEmpty()) {
            HelpingAgentData item = helpingAgentList.get(position);
            holder.binding.helpingAgentSrNum.setText(String.valueOf(item.getSr_no() + 1));
            if(item.getComment()!=null) {
                holder.binding.instructionET.setText(item.getComment());
            }
            if(item.getAgentId() != null) {
                for(int i=0; i<agentNameList.size(); i++) {
                    if(agentNameList.get(i).getId().equalsIgnoreCase(item.getAgentId())) {
                        holder.binding.agentAssign.setText(agentNameList.get(i).getCityName());
                        break;
                    }
                }
            }
            if(item.getExpDeliveryDate() != null) {
                holder.binding.expectedDeliveryDate.setText(item.getExpDeliveryDate());
            }
            if(item.getExpDeliveryTime() != null) {
                holder.binding.expectedDeliveryTime.setText(item.getExpDeliveryTime());
            }

            holder.binding.poOptionsRV.setHasFixedSize(true);
            holder.binding.poOptionsRV.setLayoutManager(new LinearLayoutManager(context));
            holder.binding.poOptionsRV.setAdapter(new POSelectionAdapter(context, orderDetailActivity, this, position, poDocs, item.getSelectedHelpingAgentPOList(), false, item.getSr_no(), helping_agent_count - 1));
            Common.showLog("selected POs ==== "+item.getSelectedHelpingAgentPOList().size());

            holder.binding.removeAgentIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    orderDetailActivity.updateAllCheckedPOList(helpingAgentList.get(position).getSelectedHelpingAgentPOList());
                    removeAgent.removeAgentListener(item.getSr_no());
                }
            });
            holder.binding.agentAssign.setAdapter(new CustomAutoCompleteListAdapter(context, R.layout.dropdown_item_list, R.id.title_tv, agentNameList, true));
            Common.showLog("helping agent ==== "+agentNameList.size());

            assignAgent = holder.binding.agentAssign;
            assignAgent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    removeAgent.inputHelpingAgentDataListener(item, "agent", null, holder.binding.agentAssign, position, agentNameList.get(i).getId());
                }
            });
            holder.binding.expDeliveryDateInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAgent.inputHelpingAgentDataListener(item, "expectedDate", holder.binding.expectedDeliveryDate, null, position, "");
                }
            });
            holder.binding.expectedDeliveryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b)
                        removeAgent.inputHelpingAgentDataListener(item, "expectedDate", holder.binding.expectedDeliveryDate, null, position, "");
                    else view.clearFocus();
                }
            });
            holder.binding.expectedDeliveryTime.setAdapter(new AutoCompleteAdapter((Activity) context, R.layout.dropdown_item_list, R.id.title_tv, expectedDeliveryTimeList));
            holder.binding.expectedDeliveryTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAgent.inputHelpingAgentDataListener(item, "expectedTime", null, holder.binding.expectedDeliveryTime, position, "");
                }
            });
            holder.binding.instructionET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAgent.inputHelpingAgentDataListener(item, "comment", holder.binding.instructionET, null, position, "");
                }
            });
            holder.binding.instructionET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b)
                        removeAgent.inputHelpingAgentDataListener(item, "comment", holder.binding.instructionET, null, position, "");
                    else view.clearFocus();
                }
            });
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        HelpingAgentAssigningFormBinding binding;
        public ViewHolder(@NonNull HelpingAgentAssigningFormBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }

    public void removeSelectedPOList(int position, String poId) {
        helpingAgentList.get(position).removeSelectedPOList(poId);
    }

    public void addSelectedPOList(int position, String poId) {
        helpingAgentList.get(position).addSelectedPOList(poId);
    }
    public List<String> getSelectedPOList(int position) {
        return helpingAgentList.get(position).getSelectedHelpingAgentPOList();
    }
    public void setAssignAgent(int position, String agentId) {
        helpingAgentList.get(position).setAgentId(agentId);
    }
    @Override
    public int getItemCount() {
        return helpingAgentList.size();
    }
    public interface RemoveAgent {
        public void removeAgentListener(int srNo);
        public void inputHelpingAgentDataListener(HelpingAgentData helpingAgentData, String type, AppCompatEditText appCompatEditText, AutoCompleteTextView autoCompleteTextView, int position, String agentId);
    }
}
