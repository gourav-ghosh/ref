package com.devstringx.mytylesstockcheck.datamodal.architectListingData;

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

	public void setPassDateRange(String passDateRange){
		this.passDateRange = passDateRange;
	}

	public String getPassDateRange(){
		return passDateRange;
	}

	public void setFromDate(String fromDate){
		this.fromDate = fromDate;
	}

	public String getFromDate(){
		return fromDate;
	}

	public void setToDate(String toDate){
		this.toDate = toDate;
	}

	public String getToDate(){
		return toDate;
	}

	public void setPassFromDate(Object passFromDate){
		this.passFromDate = passFromDate;
	}

	public Object getPassFromDate(){
		return passFromDate;
	}

	public void setPassToDate(Object passToDate){
		this.passToDate = passToDate;
	}

	public Object getPassToDate(){
		return passToDate;
	}
}