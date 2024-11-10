package com.devstringx.mytylesstockcheck.adapter;

import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.AllTaskRvListBinding;
import com.devstringx.mytylesstockcheck.databinding.LeadHistoryItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.LeadHistory.NewValueItem;
import com.devstringx.mytylesstockcheck.datamodal.LeadHistory.OldValueItem;
import com.devstringx.mytylesstockcheck.datamodal.LeadHistory.RecordItem;
import com.devstringx.mytylesstockcheck.datamodal.allLeadTask.RecordsItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeadHistoryAdapter extends RecyclerView.Adapter<LeadHistoryAdapter.ViewHolder> {
    private List<RecordItem> listItems = new ArrayList<>();
//    private List<NewValueItem> newValueItemList = new ArrayList<>();
//    private List<OldValueItem> oldValueItemList = new ArrayList<>();

    public void refreshList(List<RecordItem> recordsItems) {
        listItems = recordsItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LeadHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LeadHistoryItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.lead_history_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadHistoryAdapter.ViewHolder holder, int position) {
        RecordItem item = listItems.get(position);
        holder.binding.changeDateTime.setText(Common.historyDateConvert(item.getChangeDate()) + " " + item.getChangeTime());
        holder.binding.changesBy.setText("By:" + item.getUpdatedByFirstName() + "(" + item.getUpdatedByRole() + ")");
        holder.binding.commentTv.setVisibility(View.GONE);
        if (item.getNewValue().toString().equalsIgnoreCase("Order Placed"))
            holder.binding.header.setBackgroundColor(Color.parseColor("#3B9321"));

        if (item.getNewValue().toString().equalsIgnoreCase("Lost"))
            holder.binding.header.setBackgroundColor(Color.parseColor("#F72C1D"));

        if (item.getType().equalsIgnoreCase("Task")) {
            holder.binding.detail.setText(item.getTask());
//            if (!item.getTask().equalsIgnoreCase("Task Completed") ){
            if (item.getComment() != null)
                holder.binding.moreDetail.setText(Html.fromHtml("<b>Follow up on " + Common.historyDateConvert(item.getFollowUpDate()) + "," + item.getFollowUpTime() + "</b>: " + item.getComment()));
            else
                holder.binding.moreDetail.setText(Html.fromHtml("<b>Follow up on " + Common.historyDateConvert(item.getFollowUpDate()) + "," + item.getFollowUpTime() + "</b>: "));

//            }
        } else if (item.getType().equalsIgnoreCase("Activity")) {
            holder.binding.detail.setText("Activity added");
            if (item.getComment() != null)
                holder.binding.moreDetail.setText(Html.fromHtml("<b>" + item.getNewValue() + ": </b>" + item.getComment()));
            else
                holder.binding.moreDetail.setText(Html.fromHtml("<b>" + item.getNewValue() + " </b>"));
        }else if (item.getType().equalsIgnoreCase("attachment")) {
            holder.binding.detail.setText(item.getFieldName() + " Added");
            holder.binding.moreDetail.setText(Html.fromHtml("<b>" + item.getNewValue() + "</b>"));
        }else if (item.getType().equalsIgnoreCase("leadstage")) {
            holder.binding.detail.setText(item.getFieldName() + " Updated");
            holder.binding.moreDetail.setText(Html.fromHtml(item.getFieldName() + " updated from <b>" + item.getOldValue() + "</b> to <b>" + item.getNewValue() + "</b>"));
            if(!item.getComment().isEmpty()) {
                holder.binding.commentTv.setVisibility(View.VISIBLE);
                holder.binding.commentTv.setText("Comment : " + item.getComment());
            }
        } else {
            holder.binding.detail.setText("Lead " + item.getFieldName() + " Updated");

            if (item.getFieldName().equalsIgnoreCase("State")) {
                if (item.getOldValue() == null)
                    holder.binding.moreDetail.setText(Html.fromHtml("Lead " + item.getFieldName() + " updated" + " to <b>" + item.getNewState() + "</b>"));
                else if (item.getNewValue() == null) {
                    holder.binding.moreDetail.setText(Html.fromHtml("Lead " + item.getFieldName() + " <b>is removed</b>"));
                } else
                    holder.binding.moreDetail.setText(Html.fromHtml("Lead " + item.getFieldName() + " updated from <b>" + item.getOldState() + "</b> to <b>" + item.getNewState() + "</b>"));
            } else if (item.getFieldName().equalsIgnoreCase("City")) {
                if (item.getOldValue() == null)
                    holder.binding.moreDetail.setText(Html.fromHtml("Lead " + item.getFieldName() + " updated" + " to <b>" + item.getNewCity() + "</b>"));
                else if (item.getNewValue() == null) {
                    holder.binding.moreDetail.setText(Html.fromHtml("Lead " + item.getFieldName() + " <b>is removed</b>"));
                } else
                    holder.binding.moreDetail.setText(Html.fromHtml("Lead " + item.getFieldName() + " updated from <b>" + item.getOldCity() + "</b> to <b>" + item.getNewCity() + "</b>"));
            } else if (item.getFieldName().equalsIgnoreCase("Requirements")) {
                String oldRequirements = "", newRequirements = "";
                Gson gson = new Gson();
                Type oldValueType = new TypeToken<List<OldValueItem>>(){}.getType();
                Type newValueType = new TypeToken<List<NewValueItem>>(){}.getType();
                List<OldValueItem> oldRequirementsList = gson.fromJson(gson.toJsonTree(item.getOldValue()), oldValueType);
                List<NewValueItem> newRequirementsList = gson.fromJson(gson.toJsonTree(item.getNewValue()), newValueType);
                if (item.getOldValue()!=null) oldRequirements = appendOldRequirements(oldRequirementsList);
                if (item.getNewValue()!=null) newRequirements = appendNewRequirements(newRequirementsList);
                if (item.getOldValue() == null) {
                    holder.binding.moreDetail.setText(Html.fromHtml("Lead " + item.getFieldName() + " updated to <b>" + newRequirements + "</b>"));
                } else if (item.getNewValue() == null) {
                    holder.binding.moreDetail.setText(Html.fromHtml("Lead " + item.getFieldName() + " <b>is removed</b>"));
                } else {
                    holder.binding.moreDetail.setText(Html.fromHtml("Lead " + item.getFieldName() + " updated from <b>" + oldRequirements + "</b> to <b>" + newRequirements + "</b>"));
                }
            } else if (item.getFieldName().equalsIgnoreCase("Lead Owner")) {
                holder.binding.detail.setText(item.getFieldName() + " Updated");
                holder.binding.moreDetail.setText(Html.fromHtml(item.getFieldName() + " updated from <b>" + item.getOldOwnerFirstName() + " " + item.getOldOwnerLastName() + "</b> to <b>" + item.getNewOwnerFirstName() + " " + item.getNewOwnerLastName() + "</b>"));
            }else {
                if (item.getOldValue() == null)
                    holder.binding.moreDetail.setText(Html.fromHtml("Lead " + item.getFieldName() + " updated" + " to <b>" + item.getNewValue() + "</b>"));
                else if (item.getNewValue() == null) {
                    holder.binding.moreDetail.setText(Html.fromHtml("Lead " + item.getFieldName() + " <b>is removed</b>"));
                } else
                    holder.binding.moreDetail.setText(Html.fromHtml("Lead " + item.getFieldName() + " updated from <b>" + item.getOldValue() + "</b> to <b>" + item.getNewValue() + "</b>"));
            }
        }
    }

    private String appendNewRequirements(List<NewValueItem> newValueItemList) {
        StringBuilder requirementsBuilder = new StringBuilder();
        for (NewValueItem newValueItem : newValueItemList) {
            requirementsBuilder.append(newValueItem.getRequirement()).append(", ");
        }
        // Remove the trailing comma and space
        if (requirementsBuilder.length() > 0) {
            requirementsBuilder.setLength(requirementsBuilder.length() - 2);
        }
        return requirementsBuilder.toString();
    }

    // Function to append requirements from OldValueItem list
    private String appendOldRequirements(List<OldValueItem> oldValueItemList) {
        StringBuilder requirementsBuilder = new StringBuilder();
        for (OldValueItem oldValueItem : oldValueItemList) {
            requirementsBuilder.append(oldValueItem.getRequirement()).append(", ");
        }
        // Remove the trailing comma and space
        if (requirementsBuilder.length() > 0) {
            requirementsBuilder.setLength(requirementsBuilder.length() - 2);
        }
        return requirementsBuilder.toString();
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LeadHistoryItemBinding binding;

        public ViewHolder(@NonNull LeadHistoryItemBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
}
