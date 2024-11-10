package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

public class ShippingChargesDetails{

	@SerializedName("tds")
	private String tds;

	@SerializedName("loading_points")
	private String loadingPoints;

	@SerializedName("distance")
	private String distance;

	@SerializedName("media_ids")
	private String mediaIds;

	@SerializedName("agent_id")
	private String agentId;

	@SerializedName("agent_comment")
	private String agentComment;

	@SerializedName("deleted_by")
	private String deletedBy;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("re_request_time")
	private String reRequestTime;

	@SerializedName("amount_payable")
	private String amountPayable;

	@SerializedName("payer")
	private String payer;

	@SerializedName("rejected_by")
	private String rejectedBy;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("approved_time")
	private String approvedTime;

	@SerializedName("id")
	private String id;

	@SerializedName("amount")
	private String amount;

	@SerializedName("payment_mode")
	private String paymentMode;

	@SerializedName("accounts_comment")
	private String accountsComment;

	@SerializedName("request_raised_time")
	private String requestRaisedTime;

	@SerializedName("paid_time")
	private String paidTime;

	@SerializedName("rejected_time")
	private String rejectedTime;

	@SerializedName("deleted_at")
	private String deletedAt;

	@SerializedName("reason_for_rejection")
	private String reasonForRejection;

	@SerializedName("wf_amount")
	private String wfAmount;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("status")
	private String status;

	public String getTds(){
		return tds;
	}

	public String getLoadingPoints() {
		return loadingPoints;
	}

	public String getDistance(){
		return distance;
	}

	public String getMediaIds(){
		return mediaIds;
	}

	public String getAgentId(){
		return agentId;
	}

	public String getAgentComment(){
		return agentComment;
	}

	public String getDeletedBy(){
		return deletedBy;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getReRequestTime(){
		return reRequestTime;
	}

	public String getAmountPayable(){
		return amountPayable;
	}

	public String getPayer(){
		return payer;
	}

	public String getRejectedBy(){
		return rejectedBy;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getApprovedTime(){
		return approvedTime;
	}

	public String getId(){
		return id;
	}

	public String getAmount(){
		return amount;
	}

	public String getPaymentMode(){
		return paymentMode;
	}

	public String getAccountsComment(){
		return accountsComment;
	}

	public String getRequestRaisedTime(){
		return requestRaisedTime;
	}

	public String getPaidTime(){
		return paidTime;
	}

	public String getRejectedTime(){
		return rejectedTime;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	public String getReasonForRejection(){
		return reasonForRejection;
	}

	public String getWfAmount(){
		return wfAmount;
	}

	public String getOrderId(){
		return orderId;
	}

	public String getStatus(){
		return status;
	}
}