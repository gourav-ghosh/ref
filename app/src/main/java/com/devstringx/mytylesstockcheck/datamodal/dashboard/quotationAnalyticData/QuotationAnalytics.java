package com.devstringx.mytylesstockcheck.datamodal.dashboard.quotationAnalyticData;

import com.google.gson.annotations.SerializedName;

public class QuotationAnalytics{

	@SerializedName("total")
	private String total;

	@SerializedName("converted_quotes")
	private String convertedQuotes;

	@SerializedName("total_not_converted")
	private String totalNotConverted;

	@SerializedName("open_quotes")
	private String openQuotes;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("total_converted")
	private String totalConverted;

	public String getTotal(){
		return total;
	}

	public String getConvertedQuotes(){
		return convertedQuotes;
	}

	public String getTotalNotConverted(){
		return totalNotConverted;
	}

	public String getOpenQuotes(){
		return openQuotes;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getTotalConverted(){
		return totalConverted;
	}
}