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
import com.devstringx.mytylesstockcheck.databinding.FilterStageItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;

import java.util.ArrayList;
import java.util.List;

public class FilterItemAdapter extends RecyclerView.Adapter<FilterItemAdapter.ViewHolder>{
    private List<String> leadStageList = new ArrayList<>();
    private List<String> selecteditems=new ArrayList<>();
    private ArrayList<RecordsItem> fullList;
    FilterItemSelected filterItemSelected;
    private Context context;
    private String ownerList=null;
    public FilterItemAdapter(Context context, List<String> leadStageList, FilterItemSelected filterItemSelected,List<String> selecteditemsList) {
        this.context=context;
        this.leadStageList=leadStageList;
        this.filterItemSelected=filterItemSelected;
        if (selecteditemsList!=null) this.selecteditems=selecteditemsList;
    }
    public FilterItemAdapter(Context context, List<RecordsItem> allOwnersList, String ownerList, FilterItemSelected filterItemSelected,List<String> selecteditemsList) {
        this.context=context;
        this.fullList=(ArrayList<RecordsItem>)allOwnersList;
        this.ownerList=ownerList;
        this.filterItemSelected =filterItemSelected;
        if (selecteditemsList!=null) this.selecteditems=selecteditemsList;
    }
    @NonNull
    @Override
    public FilterItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FilterStageItemBinding filterStageItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.filter_stage_item, parent, false);
        return new ViewHolder(filterStageItemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull FilterItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (ownerList!=null) holder.filterItemBinding.filterStageIv.setSelected(selecteditems.contains(fullList.get(position).getId()));
        else holder.filterItemBinding.filterStageIv.setSelected(selecteditems.contains(leadStageList.get(position)));
        if(holder.filterItemBinding.filterStageIv.isSelected()) holder.filterItemBinding.filterStageIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
        else holder.filterItemBinding.filterStageIv.setBackgroundResource(R.drawable.orange_checkbox_unselected);
        holder.filterItemBinding.filterStageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.filterItemBinding.filterStageIv.isSelected()){
                    holder.filterItemBinding.filterStageIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
                    holder.filterItemBinding.filterStageIv.setSelected(true);
                    if (ownerList!=null){
                        selecteditems.add(fullList.get(position).getId());
                    } else selecteditems.add(holder.filterItemBinding.listTv.getText().toString());
                }else {
                    holder.filterItemBinding.filterStageIv.setSelected(false);
                    holder.filterItemBinding.filterStageIv.setBackgroundResource(R.drawable.orange_checkbox_unselected);
                    if (ownerList!=null){
                        selecteditems.remove(fullList.get(position).getId());
                    } else selecteditems.remove(holder.filterItemBinding.listTv.getText().toString());
                }
                filterItemSelected.onItemSelectedListner(selecteditems);

            }
        });
        if (ownerList!=null) {
            holder.filterItemBinding.listTv.setText(fullList.get(position).getCityName());
        }
        else{
            holder.filterItemBinding.listTv.setText((String) leadStageList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (ownerList!=null)
            return fullList.size();
        else return leadStageList.size();
    }

    public List<String> getSelecteditemsList() {
        return selecteditems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        FilterStageItemBinding filterItemBinding;
        public ViewHolder(@NonNull FilterStageItemBinding view) {
            super(view.getRoot());
            this.filterItemBinding =view;
        }
    }
    public interface FilterItemSelected {
        void onItemSelectedListner(List<String> selected_items);
    }

}
