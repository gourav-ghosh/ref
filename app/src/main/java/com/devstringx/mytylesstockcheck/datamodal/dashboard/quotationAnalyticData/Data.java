package com.devstringx.mytylesstockcheck.datamodal.dashboard.quotationAnalyticData;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("records")
	private Records records;

	public Records getRecords(){
		return records;
	}
}