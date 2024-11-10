package com.devstringx.mytylesstockcheck.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.SelectedProductItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.ProductsItemsItem;
import com.devstringx.mytylesstockcheck.datamodal.products.ProductItem;
import com.devstringx.mytylesstockcheck.screens.AddQuoteActivity;
import com.devstringx.mytylesstockcheck.screens.QuoteDetailActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class DetailProductListAdapter extends RecyclerView.Adapter<DetailProductListAdapter.ViewHolder> {

    List<ProductItem> listData=new ArrayList<>();
    Context mContext;
    onRecheckClick mOnReCheckClickListner;
    public DetailProductListAdapter(Context mContext, List<ProductItem> listData, onRecheckClick onReCheckClickListner) {
        this.listData = listData;
        this.mContext = mContext;
        mOnReCheckClickListner=onReCheckClickListner;
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
    public DetailProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SelectedProductItemBinding taskListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.selected_product_item, parent, false);
        return new ViewHolder(taskListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailProductListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Common.showLog("===getQuantity=="+position+"======="+listData.get(position).getProductName());
        holder.binding.qtyTv.setText(String.valueOf(listData.get(position).getQuantity()));
        holder.binding.tileDetail.setText(listData.get(position).getProductName());
        holder.binding.location.setText(listData.get(position).getDescription());
        holder.binding.totalAmountTv.setText(Common.getCurrencyAmount(String.valueOf(mOnReCheckClickListner.getProductTaxableAmount(listData.get(position)))));
        holder.binding.rateTv.setText(Common.getCurrencyAmountWithoutDecimal(String.valueOf(listData.get(position).getPrice())));
        holder.binding.uomTv.setText(listData.get(position).getUnitOfMeasurement());
        holder.binding.discountTv.setText(listData.get(position).getDiscount()+"%");
        holder.binding.gstTv.setText(listData.get(position).getGstRate()+"%");
        if(listData.get(position).getProductAttachment().size()>0){
            listData.get(position).setImageUrl(listData.get(position).getProductAttachment().get(0));
        }
        holder.binding.tileDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleTooltip.Builder(mContext)
                        .anchorView(holder.binding.tileDetail)
                        .text(listData.get(position).getProductName())
                        .gravity(Gravity.TOP)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show();
            }
        });
        Glide.with(mContext)
                .load(listData.get(position).getImageUrl())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.binding.tileIv);
        if(listData.get(position).isCanRecheck()){
            holder.binding.recheckLl.setVisibility(View.VISIBLE);
        }else{
            holder.binding.recheckLl.setVisibility(View.GONE);
        }
        holder.binding.availabilityTv.setText(listData.get(position).getStockCheckStatus());
        if(listData.get(position).getStockCheckStatus().equalsIgnoreCase("Available")){
            holder.binding.availabilityTv.setTextColor(mContext.getColor(R.color.green));
            holder.binding.availabilityTv.setBackgroundResource(R.drawable.availability_label);
        }else{
            holder.binding.availabilityTv.setTextColor(mContext.getColor(R.color.red));
            holder.binding.availabilityTv.setBackgroundResource(R.drawable.partially_available_red_label);
        }
        holder.binding.recheckLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnReCheckClickListner!=null){
                    mOnReCheckClickListner.onReCheckClick(position);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SelectedProductItemBinding binding;
        public ViewHolder(@NonNull SelectedProductItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
    public interface onRecheckClick{
        public void onReCheckClick(int position);
        BigDecimal getProductTaxableAmount(ProductItem productItem);
    }
}
