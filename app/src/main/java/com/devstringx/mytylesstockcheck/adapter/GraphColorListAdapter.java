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
import com.devstringx.mytylesstockcheck.databinding.GraphStatusItemBinding;

import java.util.ArrayList;
import java.util.List;

public class GraphColorListAdapter extends RecyclerView.Adapter<GraphColorListAdapter.ViewHolder> {

    private List<String> mStatus=new ArrayList<>();
    private List<String> mColors=new ArrayList<>();
    private List<String> mStatusTotalCount=new ArrayList<>();
    private Context mContext;
    public GraphColorListAdapter(Context context,ArrayList<String> status, ArrayList<String> colors, ArrayList<String> statusTotalCount) {
        mStatus=status;
        mColors=colors;
        mStatusTotalCount=statusTotalCount;
        mContext=context;
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull GraphColorListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(mStatus.size()==3) {
            holder.binding.statusTv.setText(mStatus.get(position) + " " + mStatusTotalCount.get(position));
        }else{
            holder.binding.statusTv.setText(mStatus.get(position));
        }
        holder.binding.statusCv.setCardBackgroundColor(Color.parseColor(mColors.get(position)));
        holder.binding.statusLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.showToast(mContext,mStatus.get(position)+" "+mStatusTotalCount.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStatus.size();
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GraphStatusItemBinding viewProductCategoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.graph_status_item, parent, false);
        return new ViewHolder(viewProductCategoryBinding);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private GraphStatusItemBinding binding;
        public ViewHolder(GraphStatusItemBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
}
