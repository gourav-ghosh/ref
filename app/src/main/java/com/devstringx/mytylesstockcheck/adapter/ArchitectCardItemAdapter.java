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
import com.devstringx.mytylesstockcheck.databinding.ArchitectCardItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.architectListingData.RecordsItem;

import java.util.ArrayList;
import java.util.List;

public class ArchitectCardItemAdapter extends RecyclerView.Adapter<ArchitectCardItemAdapter.ViewHolder> {
    private OnArchitectItemClickListener clickListener;
    private Context context;
    private List<RecordsItem> records = new ArrayList<>();

    public ArchitectCardItemAdapter(Context context,  OnArchitectItemClickListener clickListener) {
        this.context=context;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ArchitectCardItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ArchitectCardItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.architect_card_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchitectCardItemAdapter.ViewHolder holder, int position) {
        RecordsItem item = records.get(position);
        holder.binding.nameTV.setText(item.getFirstName()+" "+item.getLastName());
        holder.binding.enterpriseNameTV.setText(item.getCompanyName());
        holder.binding.sinceTV.setText("since "+item.getCreatedAt());
        holder.binding.orderCountTV.setText(String.valueOf(item.getOrderCount()));
        holder.binding.callBtnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClickRedirection(null,item.getPrimaryPhone(),"call");
            }
        });
        holder.binding.whatsappBtnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClickRedirection(null,item.getPrimaryPhone(),"whatsapp");
            }
        });
        holder.binding.itemRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClickRedirection(String.valueOf(item.getId()),"","");
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void refreshList(List<RecordsItem> records) {
        this.records=records;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ArchitectCardItemBinding binding;
        public ViewHolder(@NonNull ArchitectCardItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
    public interface OnArchitectItemClickListener {
        void onClickRedirection(String id , String mobileNum , String type);
    }
}
