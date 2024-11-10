package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

public class PaymentInfoItem{

	@SerializedName("payment_mode")
	private String paymentMode;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("payment_status")
	private String paymentStatus;

	@SerializedName("partial_amount")
	private String partialAmount;

	@SerializedName("order_amount")
	private String orderAmount;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("Ref_no")
	private String RefNo;

	@SerializedName("id")
	private String id;

	public String getPaymentMode(){
		return paymentMode;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getPaymentStatus(){
		return paymentStatus;
	}

	public String getPartialAmount(){
		return partialAmount;
	}

	public String getOrderAmount(){
		return orderAmount;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getId(){
		return id;
	}

	public void setRefNo(String refNo) {
		RefNo = refNo;
	}

	public String getRefNo() {
		return RefNo;
	}
}