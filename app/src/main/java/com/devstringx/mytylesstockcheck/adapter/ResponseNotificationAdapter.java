package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.OtherInquiryItemBinding;
import com.devstringx.mytylesstockcheck.databinding.ResponseItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.responseNotification.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationsData.VendorQuotationsRecordsItem;
import com.devstringx.mytylesstockcheck.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ResponseNotificationAdapter extends RecyclerView.Adapter<ResponseNotificationAdapter.ViewHolder> {
    private List<RecordsItem> listItems=new ArrayList<>();
    private NotificationUpdate onItemClickListener;
    Context mContext;
    public ResponseNotificationAdapter(Context context,List<RecordsItem> recordsItems, NotificationUpdate onItemClickListener) {
        this.mContext=context;
        listItems=recordsItems;
        this.onItemClickListener = onItemClickListener;
    }
    public void refreshList(List<RecordsItem> recordsItems){
        listItems=recordsItems;
        notifyDataSetChanged();
    }

    public List<RecordsItem> getListItems() {
        return listItems;
    }

    @NonNull
    @Override
    public ResponseNotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ResponseItemBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.response_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseNotificationAdapter.ViewHolder holder, int position) {
        RecordsItem item=listItems.get(position);
        holder.binding.inquiryStatusTv.setText(Html.fromHtml("Stock <font color=#F7941D><b>'"+item.getStockCheckResponse()+"'</b> </font>"));
        holder.binding.timeAgo.setText(item.getRequestReceivedTimeNotification());
        holder.binding.tilesQtyTv.setText(item.getQuantity());
        holder.binding.tileDetail.setText(item.getProductName());
        holder.binding.uomTv.setText(item.getUnitOfMeasurement());
        holder.binding.quotTv.setText(item.getQuoteNumber());
        String url= item.getProductImages();
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.binding.tilesIv);
        if(item.getIsRead().equalsIgnoreCase("0")){
            holder.binding.rowCv.setCardBackgroundColor(mContext.getColor(R.color.light_grey));
        }else{
            holder.binding.rowCv.setCardBackgroundColor(Color.WHITE);
        }
        holder.binding.rowCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null)onItemClickListener.updateNotifationListner(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ResponseItemBinding binding;
        public ViewHolder(@NonNull ResponseItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }

    public interface NotificationUpdate{
        public void updateNotifationListner(int position);
    }
}
