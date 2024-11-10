package com.devstringx.mytylesstockcheck.datamodal.dashboard.quotationAnalyticData;

import com.google.gson.annotations.SerializedName;

public class Records{

	@SerializedName("quotationAnalytics")
	private QuotationAnalytics quotationAnalytics;

	public QuotationAnalytics getQuotationAnalytics(){
		return quotationAnalytics;
	}
}