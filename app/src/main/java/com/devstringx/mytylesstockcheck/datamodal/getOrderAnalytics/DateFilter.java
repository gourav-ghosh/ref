package com.devstringx.mytylesstockcheck.datamodal.getOrderAnalytics;

import com.google.gson.annotations.SerializedName;

public class DateFilter{

	@SerializedName("pass_date_range")
	private String passDateRange;

	@SerializedName("from_date")
	private String fromDate;

	@SerializedName("to_date")
	private String toDate;

	@SerializedName("pass_from_date")
	private Object passFromDate;

	@SerializedName("pass_to_date")
	private Object passToDate;

	public String getPassDateRange(){
		return passDateRange;
	}

	public String getFromDate(){
		return fromDate;
	}

	public String getToDate(){
		return toDate;
	}

	public Object getPassFromDate(){
		return passFromDate;
	}

	public Object getPassToDate(){
		return passToDate;
	}
}