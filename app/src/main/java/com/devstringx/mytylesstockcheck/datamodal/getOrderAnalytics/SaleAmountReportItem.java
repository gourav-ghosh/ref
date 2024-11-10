package com.devstringx.mytylesstockcheck.datamodal.getOrderAnalytics;

import com.google.gson.annotations.SerializedName;

public class SaleAmountReportItem{

	@SerializedName("amount")
	private String amount;

	@SerializedName("count")
	private int count;

	public String getAmount(){
		return amount;
	}

	public int getCount(){
		return count;
	}
}