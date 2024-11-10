package com.devstringx.mytylesstockcheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.databinding.PaymentReceiptBinding;
import com.devstringx.mytylesstockcheck.datamodal.PaymentReceiptData;

import java.util.ArrayList;
import java.util.List;

public class AddMultiplePaymentReceiptAdapter extends RecyclerView.Adapter<AddMultiplePaymentReceiptAdapter.ViewHolder> {
    public int receipt_count = 1;
    public List<PaymentReceiptData> paymentReceiptList = new ArrayList<>();
    private Context context;
    private RemoveReceipt remove_receipt;
    private List<String> payment_modes;
    List<String> payment_modes_value = new ArrayList<>();

    public AddMultiplePaymentReceiptAdapter(Context context, RemoveReceipt remove_receipt, List<String> payment_modes, List<String> payment_modes_value) {
        this.context = context;
        this.remove_receipt = remove_receipt;
        this.payment_modes = payment_modes;
        this.payment_modes_value = payment_modes_value;
        // Initialize with one receipt
        addNewReceipt();
    }

    private void addNewReceipt() {
        PaymentReceiptData data = new PaymentReceiptData();
        data.setSr_no(receipt_count++);
        paymentReceiptList.add(data);
    }

    public void updateReceiptCount(String type) {
        if (type.equalsIgnoreCase("add")) {
            addNewReceipt();
        } else if (type.equalsIgnoreCase("remove") && receipt_count > 1) {
            receipt_count--;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddMultiplePaymentReceiptAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PaymentReceiptBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.payment_receipt, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddMultiplePaymentReceiptAdapter.ViewHolder holder, int position) {
        PaymentReceiptData item = paymentReceiptList.get(position);

        // Always set the serial number
        holder.binding.receiptSrNum.setText(String.valueOf(item.getSr_no()));

        // Set values if they exist, otherwise clear the fields
        holder.binding.amountPaidTV.setText(item.getPayment_amount() != null ? item.getPayment_amount().toString() : "");
        holder.binding.refNum.setText(item.getPayment_ref() != null ? item.getPayment_ref().toString() : "");
        holder.binding.paymentMode.setText(item.getPaymentModeLabel() != null ? item.getPaymentModeLabel().toString() : "");

        // Show or hide the remove button
        holder.binding.removeReceiptIV.setVisibility(getItemCount() > 1 ? View.VISIBLE : View.GONE);

        holder.binding.removeReceiptIV.setOnClickListener(view -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                paymentReceiptList.remove(currentPosition);
                updateSerialNumbers();
                notifyItemRemoved(currentPosition);
                updateReceiptCount("remove");
                remove_receipt.removeReceiptListener(item.getSr_no());
            }
        });

        holder.binding.amountPaidTV.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                item.setPayment_amount(holder.binding.amountPaidTV.getText().toString());
            }
        });

        holder.binding.refNum.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                item.setPayment_ref(holder.binding.refNum.getText().toString());
            }
        });

        holder.binding.paymentMode.setAdapter(new AutoCompleteAdapter(context, R.layout.dropdown_item_list, R.id.title_tv, payment_modes));
        holder.binding.paymentMode.setOnItemClickListener((parent, view, position1, id) -> {
            item.setPayment_mode(payment_modes_value.get(position1));
            item.setPaymentModeLabel(holder.binding.paymentMode.getText().toString());
        });
    }

    @Override
    public int getItemCount() {
        return paymentReceiptList.size();
    }

    private void updateSerialNumbers() {
        int serialNumber = 1;
        for (PaymentReceiptData data : paymentReceiptList) {
            data.setSr_no(serialNumber++);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PaymentReceiptBinding binding;

        public ViewHolder(@NonNull PaymentReceiptBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface RemoveReceipt {
        void removeReceiptListener(int srNo);
        void inputDataListener(PaymentReceiptData receiptData, String type, AppCompatEditText amountPaidTV, AutoCompleteTextView paymentMode);
    }
}
