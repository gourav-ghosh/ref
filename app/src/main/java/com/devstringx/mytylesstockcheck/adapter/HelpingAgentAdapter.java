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
import com.devstringx.mytylesstockcheck.databinding.HelpingAgentLayoutBinding;
import com.devstringx.mytylesstockcheck.databinding.PdfAttachItemBinding;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.HelpingAgentItemItem;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.MainAgent;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.OrderTasks;
import com.devstringx.mytylesstockcheck.datamodal.getOrderDetails.POItem;

import java.util.ArrayList;
import java.util.List;

public class HelpingAgentAdapter extends RecyclerView.Adapter<HelpingAgentAdapter.ViewHolder> {
    private Context context;
    private OrderTasks orderTasks;
    private FileData fileData;

    public HelpingAgentAdapter(Context context, OrderTasks orderTasks, FileData fileData) {
        this.context = context;
        this.orderTasks = orderTasks;
        this.fileData = fileData;
    }

    @NonNull
    @Override
    public HelpingAgentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HelpingAgentLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.helping_agent_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpingAgentAdapter.ViewHolder holder, int position) {
        HelpingAgentItemItem item1 = orderTasks.getHelpingAgent().get(position).get(position);
        holder.binding.saleExe.setText(position+1 + "). Helping Agent Name");
        holder.binding.saleExeTV.setText(item1.getAssignedTo().toString());
        holder.binding.instructionTV.setText(item1.getComment().toString());
        holder.binding.saleCallIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openDialerPad(context,item1.getAssignedPhone().toString());
            }
        });
        holder.binding.saleWhatsappIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openWhatsApp(context, item1.getAssignedPhone().toString());
            }
        });
        List<MainAgent> files = new ArrayList<>();
        for (int i = 0; i <orderTasks.getHelpingAgent().get(position).size(); i++) {
            HelpingAgentItemItem item = orderTasks.getHelpingAgent().get(position).get(i);
//            POItem po = new POItem();
//            po.setName(item.getPoName());
//            po.setPath(item.getDocument().toString());
//            files.add(po);
            MainAgent po = new MainAgent();
            po.setPoName(item.getPoName());
            po.setDocument(item.getDocument().toString());
            po.setId(item.getId());
            po.setTaskStatus(item.getTaskStatus());
            files.add(po);
        }
        Common.showLog("files size+==+=="+files.size());
        fileData.sendFileData(files,holder.binding.pdfRV);
    }


    @Override
    public int getItemCount() {
        if (orderTasks.getHelpingAgent().isEmpty()) return 0;
        else return orderTasks.getHelpingAgent().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        HelpingAgentLayoutBinding binding;

        public ViewHolder(@NonNull HelpingAgentLayoutBinding view) {
            super(view.getRoot());
            this.binding = view;
        }
    }
    public interface FileData{
        public void sendFileData(List<MainAgent> files, RecyclerView rv);
    }
}
