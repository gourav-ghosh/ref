package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.PoAoItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.getAllOrders.RecordsItem;

import java.util.List;

public class PoAoAdapter extends RecyclerView.Adapter<PoAoAdapter.ViewHolder> {
    private Context context;
    private OnClick onClick;
    private List<RecordsItem> records;

    public PoAoAdapter(Context context, OnClick onClick) {
        this.context=context;
        this.onClick=onClick;
    }

    public void refreshAdapter(Context context, List<RecordsItem> records) {
        this.context = context;
        this.records = records;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PoAoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PoAoItemBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.po_ao_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PoAoAdapter.ViewHolder holder, int position) {
        RecordsItem recordsItem = records.get(position);
        holder.binding.pdfNameTV.setText(recordsItem.getName());
        if (recordsItem.getDocumentCreatedAt()!=null) holder.binding.creationTv.setText("Creation : "+recordsItem.getDocumentCreatedAt());
        else holder.binding.creationTv.setText("Creation : N/A");
        holder.binding.ERDTV.setText("EDA : "+recordsItem.getScheduleDate());
        if (recordsItem.getDeliveryMode()!=null && recordsItem.getLRNo()!=null){
            holder.binding.transportModeTV.setVisibility(View.VISIBLE);
            holder.binding.transportModeTV.setText("Mode : "+recordsItem.getDeliveryMode().replace('_', ' '));
            holder.binding.vehicleNumTV.setVisibility(View.VISIBLE);
            holder.binding.vehicleNumTV.setText("LR NO : "+recordsItem.getLRNo());
            holder.binding.updateTeansportationTV.setVisibility(View.GONE);
        }else {
            holder.binding.transportModeTV.setVisibility(View.GONE);
            holder.binding.vehicleNumTV.setVisibility(View.GONE);
            holder.binding.updateTeansportationTV.setVisibility(View.VISIBLE);
        }
        holder.binding.updateTeansportationTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClickListener(String.valueOf(recordsItem.getId()));
            }
        });
        holder.binding.viewPdfIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onViewClickListener(recordsItem.getPath());
            }
        });
        holder.binding.markPOArrangeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onMarkArrangeClickListener(recordsItem.getId(), recordsItem.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (records.isEmpty())return 0;
        else return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PoAoItemBinding binding;
        public ViewHolder(@NonNull PoAoItemBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
    public interface OnClick{
        public void onClickListener(String id);
        void onMarkArrangeClickListener(int id, String name);
        void onViewClickListener(String path);
    }
}
