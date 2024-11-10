package com.devstringx.mytylesstockcheck.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.customSwipeList.ViewBinderHelper;
import com.devstringx.mytylesstockcheck.databinding.AllTaskRvListBinding;
import com.devstringx.mytylesstockcheck.databinding.QuoteListItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.manageQuotes.RecordsItem;
import com.devstringx.mytylesstockcheck.screens.QuoteDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class QuoteListAdapter extends RecyclerView.Adapter<QuoteListAdapter.ViewHolder> {

    List<RecordsItem> listData=new ArrayList<>();
    Context mContext;
    InfoClick mInfoClick;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    public QuoteListAdapter(Context mContext,List<RecordsItem> listData,InfoClick infoClick) {
        this.listData = listData;
        this.mContext = mContext;
        binderHelper.setOpenOnlyOne(true);
        mInfoClick=infoClick;
    }

    public void refreshList(List<RecordsItem> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    public List<RecordsItem> getListData() {
        return listData;
    }

    @NonNull
    @Override
    public QuoteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuoteListItemBinding taskListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.quote_list_item, parent, false);
        return new ViewHolder(taskListBinding);
    }

    public void updateList(int position){
        listData.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RecordsItem item=listData.get(position);
        binderHelper.bind(holder.binding.swipeLayout, String.valueOf(item.getId()));
        holder.binding.leadName.setText(item.getFullName());
        if(!item.getQuoteDate().isEmpty())
            holder.binding.dateTv.setText(item.getQuoteDate());
        holder.binding.quotenoTv.setText(item.getQuoteNumber());
        holder.binding.statusTv.setText(item.getQuotationStatus());
        if(item.getQuotationStatus().equalsIgnoreCase("open")){
            holder.binding.statusTv.setTextColor(mContext.getColor(R.color.openstatus_color));
            holder.binding.statusTv.setBackgroundResource(R.drawable.open_status_bg);
        }else if(item.getQuotationStatus().equalsIgnoreCase("Draft")){
            holder.binding.statusTv.setTextColor(mContext.getColor(R.color.draftstatus_color));
            holder.binding.statusTv.setBackgroundResource(R.drawable.cancel_status_bg);
        }else if(item.getQuotationStatus().equalsIgnoreCase("Converted")){
            holder.binding.statusTv.setTextColor(mContext.getColor(R.color.convertedstatus_color));
            holder.binding.statusTv.setBackgroundResource(R.drawable.completed_status_bg);
        }
        holder.binding.ammountTv.setText(Common.getCurrencyAmount(item.getFinalTotal()));
        holder.binding.deleteLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInfoClick!=null){
                    mInfoClick.infoClickListner(position,0,"delete");
                }
            }
        });
        holder.binding.frontLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, QuoteDetailActivity.class);
                intent.putExtra("id",listData.get(position).getId());
                intent.putExtra("from", "list");
                mContext.startActivity(intent);
            }
        });
        holder.binding.progressbar.setMax(listData.get(position).getTotalQuotationProducts());
        holder.binding.progressbar.setProgress(listData.get(position).getTotalResponse());
        Common.showLog("TOTAL======getTotalQuotationProducts====="+listData.get(position).getTotalQuotationProducts());
        try {
            int percentage = (listData.get(position).getTotalResponse() * 100) / listData.get(position).getTotalQuotationProducts();
            if(percentage==100){
                holder.binding.progressStatusTv.setText(percentage + "% completed");
            }else
                holder.binding.progressStatusTv.setText(percentage + "% to complete");
        }catch (Exception e){
            e.printStackTrace();
            holder.binding.progressStatusTv.setText( "0% to complete");
        }
        holder.binding.infoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInfoClick!=null){
                    mInfoClick.infoClickListner(listData.get(position).getTotalResponse(),listData.get(position).getTotalQuotationProducts(),"info");
                }
            }
        });
    }
    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }
    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        QuoteListItemBinding binding;
        public ViewHolder(@NonNull QuoteListItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }

    public interface InfoClick{
        void infoClickListner(int totalResponse, int totalQuotationProducts,String type);
    }
}
