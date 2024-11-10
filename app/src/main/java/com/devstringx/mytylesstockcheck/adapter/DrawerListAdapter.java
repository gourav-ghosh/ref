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
import com.devstringx.mytylesstockcheck.databinding.DrawerListItemBinding;
import com.devstringx.mytylesstockcheck.databinding.LeadListItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.drawerModal.DrawerData;

import java.util.ArrayList;
import java.util.List;

public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.ViewHolder> {
    private DrawerListClick clickBtn;

    private Context mContext;
    private List<DrawerData> listItems=new ArrayList<>();
    public DrawerListAdapter(Context context, DrawerListClick clickBtn) {
        this.clickBtn=clickBtn;
        this.mContext=context;
    }

    public void refreshList(List<DrawerData> recordsItems){
        listItems=recordsItems;
        notifyDataSetChanged();
    }


    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull DrawerListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.binding.textView.setText(listItems.get(position).getTitle());
            holder.binding.multiselectionIv.setImageResource(listItems.get(position).getIcon());
            if (listItems.get(position).isSelected()){
                holder.binding.drawerRl.setBackgroundResource(R.drawable.drawer_item_selection_bg);
            }else{
                holder.binding.drawerRl.setBackgroundColor(Color.WHITE);
            }
            holder.binding.drawerRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickBtn!=null)clickBtn.onBtnClickListner(position,listItems.get(position).getTitle());
                }
            });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DrawerListItemBinding drawerListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.drawer_list_item, parent, false);
        return new ViewHolder(drawerListItemBinding);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private DrawerListItemBinding binding;
        public ViewHolder(DrawerListItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
    public interface DrawerListClick{
        void onBtnClickListner(int position,String type);
    }

}
