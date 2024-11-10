package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("amount_paid")
	private String amountPaid;

	@SerializedName("due_amount")
	private String dueAmount;

	@SerializedName("total_amount")
	private String totalAmount;

	@SerializedName("records")
	private Records records;

	public String getAmountPaid(){
		return amountPaid;
	}

	public String getDueAmount(){
		return dueAmount;
	}

	public String getTotalAmount(){
		return totalAmount;
	}

	public Records getRecords(){
		return records;
	}
}