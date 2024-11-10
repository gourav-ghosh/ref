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
import com.devstringx.mytylesstockcheck.databinding.FilterOwnerItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.screens.complaintFIlter.ComplainTypeFragment;

import java.util.ArrayList;
import java.util.List;

public class NameListWithCheckboxAdapter extends RecyclerView.Adapter<NameListWithCheckboxAdapter.ViewHolder> {
    private List<RecordsItem> allOwnersList=new ArrayList<>();
    private List<String> selecteditems=new ArrayList<>();
    private Context context;
    private FilterCBSelected clickListener;

    public NameListWithCheckboxAdapter(Context context, List<RecordsItem> allOwnersList, FilterCBSelected clickListener, List<String> selecteditems) {
        this.clickListener=clickListener;
        this.context=context;
        this.allOwnersList=allOwnersList;
        this.selecteditems=selecteditems;
    }


    @NonNull
    @Override
    public NameListWithCheckboxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FilterOwnerItemBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.filter_owner_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NameListWithCheckboxAdapter.ViewHolder holder, int position) {
        RecordsItem currentItem = allOwnersList.get(position);
        String name=currentItem.getCityName();
        holder.binding.filterOwnerTv.setText(name);
        if (currentItem.getId()==null){
        if(selecteditems.contains(name)) {
            holder.binding.filterOwnerIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
            currentItem.setSelected(true);
        }
        else {
            holder.binding.filterOwnerIv.setBackgroundResource(R.drawable.orange_checkbox_unselected);
            currentItem.setSelected(false);
        }}
        else {
            String id=currentItem.getId();
            if (selecteditems.contains(id)) {
                holder.binding.filterOwnerIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
                currentItem.setSelected(true);
            } else {
                holder.binding.filterOwnerIv.setBackgroundResource(R.drawable.orange_checkbox_unselected);
                currentItem.setSelected(false);
            }
        }
        holder.binding.filterOwnerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentItem.isSelected()) {
                    holder.binding.filterOwnerIv.setBackgroundResource(R.drawable.orange_checkbox_selected);
                    if (!(currentItem.getId()==null) && !currentItem.getId().isEmpty()) selecteditems.add(currentItem.getId());
                    else selecteditems.add(name);
                    currentItem.setSelected(true);
                }
                else{
                    holder.binding.filterOwnerIv.setBackgroundResource(R.drawable.orange_checkbox_unselected);
                    if (!(currentItem.getId()==null) && !currentItem.getId().isEmpty()) selecteditems.remove(currentItem.getId());
                    else selecteditems.remove(name);
                    currentItem.setSelected(false);
                }
                clickListener.onItemSelectedListner(selecteditems);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allOwnersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        FilterOwnerItemBinding binding;
        public ViewHolder(@NonNull FilterOwnerItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
    public interface FilterCBSelected {
        void onItemSelectedListner(List<String> selected_items);
    }
}
