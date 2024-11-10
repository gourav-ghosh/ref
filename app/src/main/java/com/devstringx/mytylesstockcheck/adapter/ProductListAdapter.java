package com.devstringx.mytylesstockcheck.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.customSwipeList.ViewBinderHelper;
import com.devstringx.mytylesstockcheck.databinding.ProductListItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.getAllProducts.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.products.ProductItem;
import com.devstringx.mytylesstockcheck.screens.AddProductActivity;
import com.devstringx.mytylesstockcheck.screens.AddQuoteActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    List<ProductItem> listData=new ArrayList<>();
    Context mContext;
    onEditClick mOnEditClickListner;
    public ProductListAdapter(Context mContext, List<ProductItem> listData,onEditClick onEditClickListner) {
        this.listData = listData;
        this.mContext = mContext;
        mOnEditClickListner=onEditClickListner;
    }

    public void refreshList(List<ProductItem> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    public List<ProductItem> getListData() {
        return listData;
    }


    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductListItemBinding taskListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.product_list_item, parent, false);
        return new ViewHolder(taskListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Common.showLog("===getQuantity=="+position+"======="+listData.get(position).getQuantity());
        holder.binding.qtyTv.setText(String.valueOf(listData.get(position).getQuantity())+" "+listData.get(position).getUnitOfMeasurement());
        holder.binding.titleTv.setText(listData.get(position).getProductName());
        holder.binding.descTv.setText(listData.get(position).getDescription());
        holder.binding.totalTv.setText(Common.getCurrencyAmount(String.valueOf(((AddQuoteActivity)mContext).getProductTaxableAmount(listData.get(position)))));
        holder.binding.rateTv.setText(Common.getCurrencyAmount(String.valueOf(listData.get(position).getPrice())));
        holder.binding.discTv.setText(listData.get(position).getDiscount()+"%");
        holder.binding.gstTv.setText(listData.get(position).getGstRate()+"%");
        Glide.with(mContext)
                .load(listData.get(position).getImageUrl())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.binding.prodImg);
        holder.binding.editProductIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnEditClickListner!=null){
                    mOnEditClickListner.onEditClick(position);
                }
            }
        });
        holder.binding.deleteProductIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                if(mContext instanceof AddQuoteActivity) {
                    ((AddQuoteActivity) mContext).updateTaxValue();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ProductListItemBinding binding;
        public ViewHolder(@NonNull ProductListItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
    public interface onEditClick{
        public void onEditClick(int position);
    }
}
