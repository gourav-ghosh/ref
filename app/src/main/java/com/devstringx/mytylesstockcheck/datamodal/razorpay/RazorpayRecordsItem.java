package com.devstringx.mytylesstockcheck.datamodal.razorpay;

import com.google.gson.annotations.SerializedName;

public class RazorpayRecordsItem {

	@SerializedName("generated_link")
	private String generatedLink;

	@SerializedName("payment_id")
	private String paymentId;

	@SerializedName("order_amount")
	private String orderAmount;

	@SerializedName("created_by_last_name")
	private String createdByLastName;

	@SerializedName("payment")
	private String payment;

	@SerializedName("id")
	private String id;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("updated_date")
	private String updatedDate;

	@SerializedName("created_by_first_name")
	private String createdByFirstName;

	public String getGeneratedLink(){
		return generatedLink;
	}

	public String getPaymentId(){
		return paymentId;
	}

	public String getOrderAmount(){
		return orderAmount;
	}

	public String getCreatedByLastName(){
		return createdByLastName;
	}

	public String getPayment(){
		return payment;
	}

	public String getId(){
		return id;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public String getUpdatedDate(){
		return updatedDate;
	}

	public String getCreatedByFirstName(){
		return createdByFirstName;
	}
}