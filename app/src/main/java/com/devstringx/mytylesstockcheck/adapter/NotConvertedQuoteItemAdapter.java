package com.devstringx.mytylesstockcheck.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.NotConvertedQuoteListBinding;

public class NotConvertedQuoteItemAdapter extends RecyclerView.Adapter<NotConvertedQuoteItemAdapter.ViewHolder> {
    @NonNull
    @Override
    public NotConvertedQuoteItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotConvertedQuoteListBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.not_converted_quote_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotConvertedQuoteItemAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NotConvertedQuoteListBinding binding;
        public ViewHolder(NotConvertedQuoteListBinding view) {
            super(view.getRoot());
            this.binding=view;
        }
    }
}
