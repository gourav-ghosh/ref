package com.devstringx.mytylesstockcheck.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.UserListItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.allOwners.RecordsItem;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private UserListBtn clickBtn;

    private Context mContext;
    private List<RecordsItem> listItems=new ArrayList<>();
    public UserListAdapter(Context context, UserListBtn clickBtn) {
        this.clickBtn=clickBtn;
        this.mContext=context;
    }

    public void refreshList(List<RecordsItem> recordsItems){
        listItems=recordsItems;
        notifyDataSetChanged();
    }

    public List<RecordsItem> getListItems() {
        return listItems;
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RecordsItem item=listItems.get(position);
        holder.binding.tileNameTv.setText(Common.getProperCase(item.getFirstName())+" "+Common.getProperCase(item.getLastName()));
        holder.binding.tileNumberTv.setText(item.getPhoneNumber());
        holder.binding.tileDateTv.setText(item.getCreatedDate());
        holder.binding.statusTv.setText(Common.getProperCase(item.getUserStatus()));
        if(item.getUserStatus().equalsIgnoreCase("added")){
            holder.binding.statusTv.setTextColor(mContext.getColor(R.color.gray));
            holder.binding.statusIv.setImageResource(R.drawable.grey_dot);
        }else if(item.getUserStatus().equalsIgnoreCase("active")){
            holder.binding.statusTv.setTextColor(mContext.getColor(R.color.green));
            holder.binding.statusIv.setImageResource(R.drawable.green_dot);
        }else{
            holder.binding.statusTv.setTextColor(mContext.getColor(R.color.red));
            holder.binding.statusIv.setImageResource(R.drawable.red_dot);
        }
        //F5FAFF
        if(position%2==0){
            holder.binding.userItemLl.setBackgroundColor(Color.WHITE);
        }else{
            holder.binding.userItemLl.setBackgroundColor(Color.parseColor("#F5FAFF"));
        }
        holder.binding.menuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBtn.onBtnClickListner(item,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserListItemBinding viewProductCategoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.user_list_item, parent, false);
        return new ViewHolder(viewProductCategoryBinding);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private UserListItemBinding binding;
        public ViewHolder(UserListItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
    public interface UserListBtn{
        void onBtnClickListner(int position,String type,boolean b);

        void onBtnClickListner(RecordsItem item, int position);
    }

}
