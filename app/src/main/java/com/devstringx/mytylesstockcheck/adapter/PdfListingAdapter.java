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
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.PdfAttachItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.MainAgent;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.OrderTasks;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.POItem;
import com.devstringx.mytylesstockcheck.screens.fragments.PurchaseDetailsSubTabFragment;

import java.util.ArrayList;
import java.util.List;

public class PdfListingAdapter extends RecyclerView.Adapter<PdfListingAdapter.ViewHolder> {
    private Context context;
    private OnClick onClick;
    private List<POItem> po = new ArrayList<>();
    private List<MainAgent> helpingPO = new ArrayList<>();
    private String type;
    private OrderTasks orderTasks;
    private String userRoleId = "";

    public PdfListingAdapter(Context context, List<POItem> po, OnClick onClick) {
        this.context = context;
        this.po = po;
        this.onClick = onClick;
    }

    public PdfListingAdapter(Context context, String type, OrderTasks orderTasks, OnClick onClick) {
        this.context = context;
        this.onClick = onClick;
        this.orderTasks = orderTasks;
        this.type = type;
        this.userRoleId = userRoleId = new PreferenceUtils(context).getStringFromPreference(PreferenceUtils.ROLEID,"");
    }
    public PdfListingAdapter(Context context, String type, List<MainAgent> po, OnClick onClick) {
        this.context = context;
        this.onClick = onClick;
        this.helpingPO = po;
        this.type = type;
        this.userRoleId = userRoleId = new PreferenceUtils(context).getStringFromPreference(PreferenceUtils.ROLEID,"");
    }

    @NonNull
    @Override
    public PdfListingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PdfAttachItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.pdf_attach_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfListingAdapter.ViewHolder holder, int position) {
        if (type == null) {
            POItem item = po.get(position);
            holder.binding.pdfNameTV.setText(item.getName());
            holder.binding.statusIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onStatusClickListener(item.getId(), item.getName());
                }
            });
            if(item.getPoStatus() == null)
                holder.binding.statusIV.setVisibility(View.VISIBLE);
            else holder.binding.statusIV.setVisibility(View.GONE);
            holder.binding.downloadIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onDownloadListener(item.getPath().toString());
                }
            });
            holder.binding.viewPdfIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onClickListener(item.getPath().toString());
                }
            });
        }else {
            MainAgent item;
            if(helpingPO.size()>0) {
                item = helpingPO.get(position);
            }
            else {
                item = orderTasks.getMainAgent().get(position);
            }
            holder.binding.pdfNameTV.setText(item.getPoName());
//            TODO have to handle the color for delivery agent and helping delivery agent
            if(!item.getTaskStatus().isEmpty() && item.getTaskStatus().toString().equalsIgnoreCase("arranged"))
                holder.binding.poStatus.setBackgroundResource(R.color.green);
            else holder.binding.poStatus.setBackgroundResource(R.color.red);
            holder.binding.downloadIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onDownloadListener(item.getDocument().toString());
                }
            });
            if(item.getTaskStatus().equalsIgnoreCase("pending")) {
                holder.binding.markPOArrangeIV.setVisibility(View.VISIBLE);
            } else holder.binding.markPOArrangeIV.setVisibility(View.GONE);
            if(userRoleId.equalsIgnoreCase("1") || userRoleId.equalsIgnoreCase("8")) {
                holder.binding.markPOArrangeIV.setVisibility(View.VISIBLE);
            } else holder.binding.markPOArrangeIV.setVisibility(View.GONE);
            holder.binding.markPOArrangeIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onMarkArrangeClickListener(Integer.parseInt(item.getId()), item.getPoName());
                }
            });
            holder.binding.viewPdfIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onClickListener(item.getDocument().toString());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (type == null) return po.size();
        else {
            if(helpingPO.size() == 0) return orderTasks.getMainAgent().size();
            else return helpingPO.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PdfAttachItemBinding binding;

        public ViewHolder(@NonNull PdfAttachItemBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }

    public interface OnClick {
        public void onClickListener(String path);
        public void onDownloadListener(String path);

        void onStatusClickListener(int id, String name);
        void onMarkArrangeClickListener(int id, String name);
    }
}
