package com.devstringx.mytylesstockcheck.screens.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devstringx.mytylesstockcheck.R;
import com.devstringx.mytylesstockcheck.adapter.AutoCompleteAdapter;
import com.devstringx.mytylesstockcheck.common.Common;
import com.devstringx.mytylesstockcheck.databinding.FragmentComplaintDetailsTabBinding;
import com.devstringx.mytylesstockcheck.datamodal.cityStateData.RecordsItem;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ComplaintDetailsResponse;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.Data;
import com.devstringx.mytylesstockcheck.screens.ComplaintDetailsActivity;
import com.devstringx.mytylesstockcheck.screens.OrderDetailActivity;
import com.devstringx.mytylesstockcheck.webservices.NKeys;
import com.devstringx.mytylesstockcheck.webservices.NetworkRequest;
import com.devstringx.mytylesstockcheck.webservices.ResponseDO;
import com.devstringx.mytylesstockcheck.webservices.ResponseListner;
import com.devstringx.mytylesstockcheck.webservices.ServiceMethods;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComplaintDetailsTabFragment extends Fragment implements ResponseListner {
    FragmentComplaintDetailsTabBinding binding;
    private Data complaintDetailResponse;
    private String agent_id = "";
    private String comment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_complaint_details_tab, container, false);
        View view = binding.getRoot();
        if(!Common.getPermission(getContext(),"CMS","ECMS")) {
            binding.editCustomerIv.setVisibility(View.GONE);
            binding.editAgentIv.setVisibility(View.GONE);
            binding.editCostIv.setVisibility(View.GONE);
            binding.editErdIv.setVisibility(View.GONE);
            binding.editVendorIv.setVisibility(View.GONE);
        }
        binding.orderNumTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("source_page","CMS");
                intent.putExtra("orderId",getActivity().getIntent().getStringExtra("id").toString());
                Common.showLog("ComplaintDetails======"+intent.getStringExtra("source_page")+"      "+intent.getStringExtra("orderId"));
                startActivityForResult(intent,100);
            }
        });
        Common.showLog("id===="+getActivity().getIntent().getStringExtra("id").toString());
        complaintDetailResponse = ((ComplaintDetailsActivity) getActivity()).complaintDetailResponse;
        setData(complaintDetailResponse);
        return view;
    }

    private void setData(Data data) {
        binding.customerNameTV.setText(data.getCustomerFullname());
        if (data.getCommentOnDateChange()!=null) comment = data.getCommentOnDateChange();
        binding.callCustomerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openDialerPad(getContext(),data.getCustomerMobileNumber());
            }
        });
        if (data.getComplaintStatus().toString().equalsIgnoreCase("Resolved")){
            binding.editAgentIv.setVisibility(View.GONE);
            binding.editErdIv.setVisibility(View.GONE);
        }
        if (data.getIssueFrom()!=null) {
            binding.issueFromTV.setText(data.getIssueFrom().toString());
            if (data.getIssueFrom().equalsIgnoreCase("Vendor")){
                binding.vendorLL.setVisibility(View.VISIBLE);
                binding.agentLL.setVisibility(View.GONE);
                binding.vendorNameTV.setText(data.getVendorName().toString());
            }else if (data.getIssueFrom().equalsIgnoreCase("Mytyles")){
                if (data.getAgentId()!=null){
                    binding.agentLL.setVisibility(View.VISIBLE);
                    binding.vendorLL.setVisibility(View.GONE);
                    binding.agentNameTV.setText(data.getAgentFirstName().toString());
                    binding.callAgentIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Common.openDialerPad(getContext(),data.getAgentPhoneNumber().toString());
                        }
                    });
                }
            }
        }else{
            binding.issueFromTV.setText("N/A");
            binding.vendorLL.setVisibility(View.GONE);
            binding.agentLL.setVisibility(View.GONE);
        }
        if (data.getSolution()!=null) binding.replacementByTV.setText(data.getSolution().toString());
        else binding.replacementByTV.setText("N/A");
        binding.dateTimeTV.setText(data.getCreatedDate()+" "+data.getCreatedTime());
        binding.erdTV.setText(data.getEstimateResolveDate());
        binding.complaintTypeTV.setText(data.getComplaintType());
        if (data.getCostToMytyles()!=null) binding.costTV.setText(data.getCostToMytyles().toString());
        else binding.costLL.setVisibility(View.GONE);

        binding.supportExeNameTV.setText(data.getSupportExecutiveFirstName().toString());
        binding.callSupportIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openDialerPad(getContext(),data.getSupportExecutivePhoneNumber().toString());
            }
        });
        binding.initialMsgTV.setText(data.getComment().toString());
        if (data.getResolutionMessage()!=null){
            binding.resolutionLL.setVisibility(View.VISIBLE);
            binding.resolutionMsgTV.setText(data.getResolutionMessage().toString());
        }
        binding.editErdIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateComplaintDetailsDialog("Update Exp. Resolution Date","resolutionDate");
            }
        });
        binding.editCustomerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateComplaintDetailsDialog("Update Customer Details","customer");
            }
        });
        binding.editCostIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateComplaintDetailsDialog("Update Cost to Mytyles","cost");
            }
        });
        binding.editAgentIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateComplaintDetailsDialog("Update Delivery Agent","deliveryAgent");
            }
        });
        binding.viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateComplaintDetailsDialog("Reason On Date Change","comment");
            }
        });

    }

    private void UpdateComplaintDetailsDialog(String title, String type) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.update_complaint_details);
        dialog.setCancelable(false);
        TextView dialogTitle = dialog.findViewById(R.id.dialog_title_tv);
        ImageView close = dialog.findViewById(R.id.close_dialog);
        LinearLayout customerLL = dialog.findViewById(R.id.updateCustomerDetailsLL);
        LinearLayout commentLL = dialog.findViewById(R.id.show_comment_LL);
        LinearLayout costLL = dialog.findViewById(R.id.cost_to_mytylesLL);
        LinearLayout resolutionDateLL = dialog.findViewById(R.id.change_resolution_date_LL);
        LinearLayout deliveryAgentLL = dialog.findViewById(R.id.delivery_agentLL);
        AutoCompleteTextView delivery_agent = dialog.findViewById(R.id.delivery_agent_name);
        TextView updateBtn = dialog.findViewById(R.id.updateBtn);
        AppCompatEditText updateCost = dialog.findViewById(R.id.cost_to_mytylesET);
        TextView show_comment = dialog.findViewById(R.id.commentTV);
        AppCompatEditText erd_date = dialog.findViewById(R.id.expected_delivery_date);
        AppCompatEditText reason = dialog.findViewById(R.id.reasonET);
        AppCompatEditText customer_name = dialog.findViewById(R.id.customerNameET);
        AppCompatEditText customer_mobile = dialog.findViewById(R.id.customer_mobilenoET);
        List<String> agentNameList = ((ComplaintDetailsActivity)getActivity()).agentNameList;
        List<RecordsItem> allOwnersList = ((ComplaintDetailsActivity)getActivity()).allOwnersList;
        dialogTitle.setText(title);
        if (type.equalsIgnoreCase("customer")){
            customerLL.setVisibility(View.VISIBLE);
        }
        else if (type.equalsIgnoreCase("comment")) {
            commentLL.setVisibility(View.VISIBLE);
            updateBtn.setVisibility(View.GONE);
            if (comment!=null) show_comment.setText(comment);
            else show_comment.setText("N/A");
        }
        else if (type.equalsIgnoreCase("resolutionDate")) {
            resolutionDateLL.setVisibility(View.VISIBLE);
        } else if (type.equalsIgnoreCase("deliveryAgent")) {
            deliveryAgentLL.setVisibility(View.VISIBLE);
        } else if (type.equalsIgnoreCase("cost")) {
            costLL.setVisibility(View.VISIBLE);
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        erd_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openCalenderForUpcomingDates(getActivity(),erd_date);
            }
        });
        delivery_agent.setAdapter(new AutoCompleteAdapter(getContext(), R.layout.dropdown_item_list, R.id.title_tv, agentNameList));

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("complaint_id",getActivity().getIntent().getStringExtra("id"));
                if (type.equalsIgnoreCase("customer")){
                    if(customer_name.getText().toString().isEmpty()) {
                        Common.showToast(getContext(),"customer name must be filled.");
                        return;
                    } else if(customer_mobile.getText().toString().isEmpty()) {
                        Common.showToast(getContext(),"customer mobile number must be filled.");
                        return;
                    } else if (!(customer_mobile.getText().toString().trim().length()==10)) {
                        Common.showToast(getContext(),"customer mobile number must be 10 digits.");
                        return;
                    }
                    map.put("customer_fullname",customer_name.getText().toString());
                    map.put("customer_mobile_number",customer_mobile.getText().toString());

                } else if (type.equalsIgnoreCase("resolutionDate")) {
                    if(erd_date.getText().toString().isEmpty()) {
                        Common.showToast(getContext(),"Please select a Est. resolution date.");
                        return;
                    } else if(reason.getText().toString().isEmpty()) {
                        Common.showToast(getContext(),"please provide a reason.");
                        return;
                    }
                    map.put("estimate_resolve_date",erd_date.getText().toString());
                    map.put("comment_on_date_change",reason.getText().toString());
                } else if (type.equalsIgnoreCase("deliveryAgent")) {
                    if (delivery_agent.getText().toString().trim().isEmpty()) {
                        Common.showToast(getContext(), "Please select an agent");
                        return;
                    }
                    for (int i = 0; i < allOwnersList.size(); i++) {
                        if (delivery_agent.getText().toString().equalsIgnoreCase(allOwnersList.get(i).getCityName().toString())) {
                            agent_id = allOwnersList.get(i).getId();
                        }
                    }
                    map.put("agent_id",agent_id);
                } else if (type.equalsIgnoreCase("cost")) {
                    if (updateCost.getText().toString().isEmpty()){
                        Common.showToast(getContext(),"Please enter the cost to mytyles.");
                        return;
                    }
                    map.put("cost_to_mytyles",updateCost.getText().toString());
                }
                dialog.dismiss();
                editComplaint(new Gson().toJson(map));
            }
        });
        dialog.show();
    }

    private void editComplaint(String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(NKeys.EDITCOMPLAINT, data);
        new NetworkRequest(getContext(), this).callWebServices(ServiceMethods.WS_EDITCOMPLAINT, map, true);
    }

    @Override
    public void onResponseReceived(ResponseDO responseDO) {
        Common.showLog("RESPONSE===" + responseDO.getResponse());
        if (!responseDO.isError()) {
            if (responseDO.getServiceMethods() == ServiceMethods.WS_EDITCOMPLAINT) {
                try {
                    JSONObject jsonObject = new JSONObject(responseDO.getResponse());
                    if (jsonObject.optString("status").equalsIgnoreCase("200")) {
                        Common.showToast(getContext(), jsonObject.optString("message"));
                        ((ComplaintDetailsActivity) getActivity()).getComplaintDetails();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}