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
import com.devstringx.mytylesstockcheck.common.PreferenceUtils;
import com.devstringx.mytylesstockcheck.databinding.LeadListItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.allLeads.RecordsItem;

import java.util.ArrayList;
import java.util.List;

public class LeadListAdapter extends RecyclerView.Adapter<LeadListAdapter.ViewHolder> {
    private LeadListBtn clickBtn;
    private Context mContext;
    private List<RecordsItem> listItems=new ArrayList<>();
    public LeadListAdapter(Context context, LeadListBtn clickBtn, String type) {
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
    public void onBindViewHolder(@NonNull LeadListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RecordsItem item=listItems.get(position);
        holder.binding.leadName.setText(item.getFullName());

//        if(Common.userProfileRole.equalsIgnoreCase("Admin") ||
//                Common.userProfileRole.equalsIgnoreCase("Employee") && Common.userProfilEligibleForCRM != 0) {
            holder.binding.ticketsizeCv.setVisibility(View.VISIBLE);
            if (item.getTicketSize().equalsIgnoreCase("Low")) {
                holder.binding.selectLeadTicketSize.setText("Low");
                holder.binding.selectLeadTicketSize.setBackgroundColor(Color.parseColor("#A8AAB0"));
            }
            if (item.getTicketSize().equalsIgnoreCase("Medium")) {
                holder.binding.selectLeadTicketSize.setText("Medium");
                holder.binding.selectLeadTicketSize.setBackgroundColor(Color.parseColor("#F8B360"));
            }
            if (item.getTicketSize().equalsIgnoreCase("High")) {
                holder.binding.selectLeadTicketSize.setText("High");
                holder.binding.selectLeadTicketSize.setBackgroundColor(Color.parseColor("#5BA944"));
            }
            if (item.getTicketSize().equalsIgnoreCase("Unknown")) {
                holder.binding.selectLeadTicketSize.setText("Unknown");
                holder.binding.selectLeadTicketSize.setBackgroundColor(Color.parseColor("#FA564A"));
            }
//        }

        if (!item.getLatestActivity().isEmpty()) holder.binding.activityType.setText(item.getLatestActivity().get(0).getActivityType()+"\n"+item.getLatestActivity().get(0).getActivityDate());
        else holder.binding.activityType.setText("No lead activity available");
        if(!item.getLeadCreatedDate().isEmpty())
        holder.binding.dateTv.setText(Common.convertYYYYMMMdd(item.getLeadCreatedDate()));
        holder.binding.ownerName.setText(item.getLeadOwnerName());
        holder.binding.leadSourceNameTV.setText("Via "+item.getLeadSource());
        holder.binding.favorite.setSelected(item.getStar_marked());
        if (item.getStar_marked()){
            holder.binding.favorite.setImageResource(R.drawable.ic_star);
        }
        else {
            holder.binding.favorite.setImageResource(R.drawable.ic_unstar);
        }
        holder.binding.newLead.setText(item.getLeadStage());
        holder.binding.newLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBtn.onBtnClickListner(position,item.getLeadStage(),false);
            }
        });
        holder.binding.leadCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBtn.onBtnClickListner(position,"lead-detail",false);
            }
        });
        holder.binding.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!item.getStar_marked()){
                    holder.binding.favorite.setImageResource(R.drawable.ic_star);
                    item.setStar_marked(true);
                }
                else {
                    holder.binding.favorite.setImageResource(R.drawable.ic_unstar);
                    item.setStar_marked(false);
                }
                clickBtn.onBtnClickListner(position,"isStarMarked",item.getStar_marked());
            }
        });
        holder.binding.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openWhatsApp(mContext,item.getLeadPrimaryPhone(),"Hi");
            }
        });
        holder.binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.openDialerPad(mContext,item.getLeadPrimaryPhone());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LeadListItemBinding viewProductCategoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.lead_list_item, parent, false);
        return new ViewHolder(viewProductCategoryBinding);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private LeadListItemBinding binding;
        public ViewHolder(LeadListItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
    public interface LeadListBtn{
        void onBtnClickListner(int position,String type,boolean b);
    }

}
