package com.devstringx.mytylesstockcheck.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.OtherInquiryItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationsData.VendorQuotationsRecordsItem;
import com.devstringx.mytylesstockcheck.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class OtherInquiryAdapter extends RecyclerView.Adapter<OtherInquiryAdapter.ViewHolder> {
    private List<VendorQuotationsRecordsItem> listItems=new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    Context mContext;
    public OtherInquiryAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.mContext=context;
        this.onItemClickListener = onItemClickListener;
    }
    public void refreshList(List<VendorQuotationsRecordsItem> recordsItems){
        listItems=recordsItems;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public OtherInquiryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OtherInquiryItemBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.other_inquiry_item, parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull OtherInquiryAdapter.ViewHolder holder, int position) {
        VendorQuotationsRecordsItem item=listItems.get(position);
        holder.binding.inquiryNumberTv.setText(item.getInquiryNo());
        holder.binding.dateTimeTv.setText(item.getRequestReceivedTime());
        holder.binding.tilesQtyTv.setText(item.getQuantity());
        holder.binding.tileDetail.setText(item.getProductName());
        String code = "<font color=#66728A> Code : " + "</font> <font color=#FF000000> " + item.getProductCode() + "</font>";
        holder.binding.tileCode.setText(Html.fromHtml(code));
        if(item.getApprovedByName().isEmpty())
            holder.binding.respondedBy.setText("-----");
        else
        holder.binding.respondedBy.setText(item.getApprovedByName());
        if(item.getRespondedIn().isEmpty())
            holder.binding.respondedIn.setText("-----");
        else
        holder.binding.respondedIn.setText(item.getRespondedIn());
        holder.binding.tileUom.setText("Qty ("+item.getUnitOfMeasurement()+")");
        String url=(!item.getProductAttachment().isEmpty())? item.getProductAttachment().get(0):"";
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.binding.tilesIv);
        holder.binding.tileDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleTooltip.Builder(mContext)
                        .anchorView(holder.binding.tileDetail)
                        .text(item.getProductName())
                        .gravity(Gravity.TOP)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show();
            }
        });
        if (item.getStockCheckStatus().equalsIgnoreCase("Available")){
            holder.binding.dotView.setBackgroundResource(R.drawable.green_dot);
            holder.binding.inquiryStatusTv.setText(item.getStockCheckStatus());
        }else {
            holder.binding.dotView.setBackgroundResource(R.drawable.red_dot);
            holder.binding.inquiryStatusTv.setText(item.getStockCheckStatus());
        }
        holder.binding.respondedInquiriesCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position,item.getId());
            }
        });
        holder.binding.copy.setClickable(true);
        holder.binding.copy.setFocusableInTouchMode(true);
        holder.binding.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "Product Name : " + item.getProductName() + " & Quantity : " + item.getQuantity()  + " " +item.getUnitOfMeasurement();
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Name & Quantity", text);
                if (clipboard == null || clip == null) return;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mContext,"Copied Product Name & Quantity",Toast.LENGTH_SHORT).show();
            }
        });
        if(Common.getPermission(mContext,"DB","") && Common.getPermission(mContext,"SCE","") &&
                Common.getPermission(mContext,"MU","") ||
                Common.getPermission(mContext,"DB","") && Common.getPermission(mContext,"SCE","")) {
            holder.binding.copy.setVisibility(View.INVISIBLE);
            holder.binding.tileCode.setVisibility(View.GONE);
        }
        if(Common.getPermission(mContext,"DB","") &&
                Common.getPermission(mContext,"ML","") &&
                Common.getPermission(mContext,"MU","") &&
                Common.getPermission(mContext,"MQ","") &&
                Common.getPermission(mContext,"SCE","") &&
                Common.getPermission(mContext,"CMS","") &&
                Common.getPermission(mContext,"DWL","")) {
            holder.binding.copy.setVisibility(View.VISIBLE);
            holder.binding.tileCode.setVisibility(View.VISIBLE);
        }
        if(Common.getPermission(mContext,"DB","") &&
                Common.getPermission(mContext,"SCE","") &&
                Common.getPermission(mContext,"MU","") &&
                Common.getPermission(mContext,"MQ","") &&
                Common.getPermission(mContext,"RZP","")) {
            holder.binding.copy.setVisibility(View.INVISIBLE);
            holder.binding.tileCode.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        OtherInquiryItemBinding binding;
        public ViewHolder(@NonNull OtherInquiryItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
}
