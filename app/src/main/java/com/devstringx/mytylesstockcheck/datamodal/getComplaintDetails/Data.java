package com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("customer_mobile_number")
	private String customerMobileNumber;

	@SerializedName("responseMedias")
	private List<ResponseMediasItem> responseMedias;

	@SerializedName("estimate_resolve_date")
	private String estimateResolveDate;

	@SerializedName("agent_id")
	private String agentId;

	@SerializedName("complaintMedias")
	private List<ComplaintMediasItem> complaintMedias;

	@SerializedName("response_id")
	private int responseId;

	@SerializedName("resolve_at")
	private String resolveAt;

	@SerializedName("customer_fullname")
	private String customerFullname;

	@SerializedName("wf_estimate_resolve_date")
	private String wfEstimateResolveDate;

	@SerializedName("issue_from")
	private String issueFrom;

	@SerializedName("complaint_type")
	private String complaintType;

	@SerializedName("solution")
	private String solution;

	@SerializedName("comment_on_date_change")
	private String commentOnDateChange;

	@SerializedName("id")
	private int id;

	@SerializedName("sale_order_no")
	private String saleOrderNo;

	@SerializedName("agent_last_name")
	private String agentLastName;

	@SerializedName("created_time")
	private String createdTime;

	@SerializedName("agent_phone_number")
	private String agentPhoneNumber;

	@SerializedName("support_executive_phone_number")
	private String supportExecutivePhoneNumber;

	@SerializedName("vendor_name")
	private String vendorName;

	@SerializedName("agent_first_name")
	private String agentFirstName;

	@SerializedName("cost_to_mytyles")
	private String costToMytyles;

	@SerializedName("resolution_message")
	private String resolutionMessage;

	@SerializedName("ticket_number")
	private String ticketNumber;

	@SerializedName("support_executive_first_name")
	private String supportExecutiveFirstName;

	@SerializedName("deleted_at")
	private String deletedAt;

	@SerializedName("support_executive_last_name")
	private String supportExecutiveLastName;

	@SerializedName("complaint_status")
	private String complaintStatus;

	@SerializedName("comment")
	private String comment;

	@SerializedName("other_complaint_type")
	private String otherComplaintType;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("complaintActivities")
	private ComplaintActivities complaintActivities;

	@SerializedName("other_solution")
	private String otherSolution;

	public String getCustomerMobileNumber(){
		return customerMobileNumber;
	}

	public List<ResponseMediasItem> getResponseMedias(){
		return responseMedias;
	}

	public String getEstimateResolveDate(){
		return estimateResolveDate;
	}

	public String getAgentId(){
		return agentId;
	}

	public List<ComplaintMediasItem> getComplaintMedias(){
		return complaintMedias;
	}

	public int getResponseId(){
		return responseId;
	}

	public String getResolveAt(){
		return resolveAt;
	}

	public String getCustomerFullname(){
		return customerFullname;
	}

	public String getWfEstimateResolveDate(){
		return wfEstimateResolveDate;
	}

	public String getIssueFrom(){
		return issueFrom;
	}

	public String getComplaintType(){
		return complaintType;
	}

	public String getSolution(){
		return solution;
	}

	public String getCommentOnDateChange(){
		return commentOnDateChange;
	}

	public int getId(){
		return id;
	}

	public String getSaleOrderNo(){
		return saleOrderNo;
	}

	public String getAgentLastName(){
		return agentLastName;
	}

	public String getCreatedTime(){
		return createdTime;
	}

	public String getAgentPhoneNumber(){
		return agentPhoneNumber;
	}

	public String getSupportExecutivePhoneNumber(){
		return supportExecutivePhoneNumber;
	}

	public String getVendorName(){
		return vendorName;
	}

	public String getAgentFirstName(){
		return agentFirstName;
	}

	public String getCostToMytyles(){
		return costToMytyles;
	}

	public String getResolutionMessage(){
		return resolutionMessage;
	}

	public String getTicketNumber(){
		return ticketNumber;
	}

	public String getSupportExecutiveFirstName(){
		return supportExecutiveFirstName;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	public String getSupportExecutiveLastName(){
		return supportExecutiveLastName;
	}

	public String getComplaintStatus(){
		return complaintStatus;
	}

	public String getComment(){
		return comment;
	}

	public String getOtherComplaintType(){
		return otherComplaintType;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public ComplaintActivities getComplaintActivities(){
		return complaintActivities;
	}

	public String getOtherSolution(){
		return otherSolution;
	}
}