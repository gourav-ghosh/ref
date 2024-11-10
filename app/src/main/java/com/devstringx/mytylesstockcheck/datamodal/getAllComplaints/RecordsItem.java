package com.devstringx.mytylesstockcheck.datamodal.getAllComplaints;

import com.google.gson.annotations.SerializedName;

public class RecordsItem{

	@SerializedName("customer_mobile_number")
	private String customerMobileNumber;

	@SerializedName("estimate_resolve_date")
	private String estimateResolveDate;

	@SerializedName("inprogress_at")
	private String inprogressAt;

	@SerializedName("created_by_name")
	private String createdByName;

	@SerializedName("resolve_at")
	private String resolveAt;

	@SerializedName("deleted_by_name")
	private Object deletedByName;

	@SerializedName("customer_fullname")
	private String customerFullname;

	@SerializedName("issue_from")
	private String issueFrom;

	@SerializedName("updated_by_name")
	private String updatedByName;

	@SerializedName("complaint_type")
	private String complaintType;

	@SerializedName("support_executive_phone")
	private long supportExecutivePhone;

	@SerializedName("id")
	private int id;

	@SerializedName("order_sale_executive")
	private String orderSaleExecutive;

	@SerializedName("sale_order_no")
	private String saleOrderNo;

	@SerializedName("order_customer_name")
	private String orderCustomerName;

	@SerializedName("cost_to_mytyles")
	private String costToMytyles;

	@SerializedName("ticket_number")
	private String ticketNumber;

	@SerializedName("support_executive_first_name")
	private String supportExecutiveFirstName;

	@SerializedName("deleted_at")
	private Object deletedAt;

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

	@SerializedName("updated_date")
	private String updatedDate;

	public String getCustomerMobileNumber(){
		return customerMobileNumber;
	}

	public String getEstimateResolveDate(){
		return estimateResolveDate;
	}

	public String getInprogressAt(){
		return inprogressAt;
	}

	public String getCreatedByName(){
		return createdByName;
	}

	public String getResolveAt(){
		return resolveAt;
	}

	public Object getDeletedByName(){
		return deletedByName;
	}

	public String getCustomerFullname(){
		return customerFullname;
	}

	public String getIssueFrom(){
		return issueFrom;
	}

	public String getUpdatedByName(){
		return updatedByName;
	}

	public String getComplaintType(){
		return complaintType;
	}

	public long getSupportExecutivePhone(){
		return supportExecutivePhone;
	}

	public int getId(){
		return id;
	}

	public String getOrderSaleExecutive(){
		return orderSaleExecutive;
	}

	public String getSaleOrderNo(){
		return saleOrderNo;
	}

	public String getOrderCustomerName(){
		return orderCustomerName;
	}

	public String getCostToMytyles(){
		return costToMytyles;
	}

	public String getTicketNumber(){
		return ticketNumber;
	}

	public String getSupportExecutiveFirstName(){
		return supportExecutiveFirstName;
	}

	public Object getDeletedAt(){
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

	public String getUpdatedDate(){
		return updatedDate;
	}
}