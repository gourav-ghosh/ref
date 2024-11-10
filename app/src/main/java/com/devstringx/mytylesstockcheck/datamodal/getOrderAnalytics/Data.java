package com.devstringx.mytylesstockcheck.datamodal.getOrderAnalytics;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("records")
	private Records records;

	@SerializedName("saleExecutive")
	private List<Object> saleExecutive;

	@SerializedName("dateFilter")
	private DateFilter dateFilter;

	public Records getRecords(){
		return records;
	}

	public List<Object> getSaleExecutive(){
		return saleExecutive;
	}

	public DateFilter getDateFilter(){
		return dateFilter;
	}
}