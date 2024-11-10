package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.PoCodesCheckboxItemBinding;

import java.util.HashSet;
import java.util.List;

public class PoCodesAdapter extends RecyclerView.Adapter<PoCodesAdapter.ViewHolder> {
    private Context context;
    private List<String> poCodes;
    private HashSet<String> selectedPoCodes;
    private OnClick onClick;
    private int orange;
    private int gray;
    public PoCodesAdapter(Context context, List<String> po_codes, HashSet<String> selectedPoCodes, OnClick onClick) {
        this.context = context;
        this.poCodes = po_codes;
        this.selectedPoCodes = selectedPoCodes;
        this.onClick = onClick;
        orange = ContextCompat.getColor(context, R.color.orange);
        gray = ContextCompat.getColor(context, R.color.gray);
    }

    @NonNull
    @Override
    public PoCodesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PoCodesCheckboxItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.po_codes_checkbox_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PoCodesAdapter.ViewHolder holder, int position) {
        String poCode = poCodes.get(position);
        holder.binding.poCodesTV.setText(poCode);
        if(selectedPoCodes.contains(poCode)) {
            holder.binding.poCodeOptionIV.setImageResource(R.drawable.orange_checkbox_selected);
            holder.binding.poCodeBg.setCardBackgroundColor(orange);
        } else {
            holder.binding.poCodeOptionIV.setImageResource(R.drawable.orange_checkbox_unselected);
            holder.binding.poCodeBg.setCardBackgroundColor(gray);
        }
        holder.binding.poOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selectedPoCodes.contains(poCode)) {
//                    selectedPoCodes.add(poCode);
                    holder.binding.poCodeOptionIV.setImageResource(R.drawable.orange_checkbox_selected);
                    holder.binding.poCodeBg.setCardBackgroundColor(orange);
                } else {
//                    selectedPoCodes.remove(poCode);
                    holder.binding.poCodeOptionIV.setImageResource(R.drawable.orange_checkbox_unselected);
                    holder.binding.poCodeBg.setCardBackgroundColor(gray);
                }
                onClick.onPoCodeClickListener(poCode);
            }
        });
    }
    @Override
    public int getItemCount() {
        return poCodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PoCodesCheckboxItemBinding binding;
        public ViewHolder(@NonNull PoCodesCheckboxItemBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
    public interface OnClick {
        void onPoCodeClickListener(String poCode);
    }
}
