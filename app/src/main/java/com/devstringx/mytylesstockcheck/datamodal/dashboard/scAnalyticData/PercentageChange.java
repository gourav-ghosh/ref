package com.devstringx.mytylesstockcheck.datamodal.dashboard.scAnalyticData;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class PercentageChange{

	@SerializedName("total_enquiries")
	private String totalEnquiries;

	@SerializedName("not_available_enquiries")
	private String notAvailableEnquiries;

	@SerializedName("available_enquiries")
	private String availableEnquiries;

	@SerializedName("not_available_enquiries_and_order_placed")
	private String notAvailableEnquiriesAndOrderPlaced;

	@SerializedName("enquiries_handled_stock_check")
	private String enquiriesHandledStockCheck="0";

	public void setTotalEnquiries(String totalEnquiries){
		this.totalEnquiries = totalEnquiries;
	}

	public String getTotalEnquiries(){
		return totalEnquiries;
	}

	public void setNotAvailableEnquiries(String notAvailableEnquiries){
		this.notAvailableEnquiries = notAvailableEnquiries;
	}

	public String getNotAvailableEnquiries(){
		return notAvailableEnquiries;
	}

	public void setAvailableEnquiries(String availableEnquiries){
		this.availableEnquiries = availableEnquiries;
	}

	public String getAvailableEnquiries(){
		return availableEnquiries;
	}

	public void setNotAvailableEnquiriesAndOrderPlaced(String notAvailableEnquiriesAndOrderPlaced){
		this.notAvailableEnquiriesAndOrderPlaced = notAvailableEnquiriesAndOrderPlaced;
	}

	public String getNotAvailableEnquiriesAndOrderPlaced(){
		return notAvailableEnquiriesAndOrderPlaced;
	}

	public String getEnquiriesHandledStockCheck() {
		return enquiriesHandledStockCheck;
	}

	public void setEnquiriesHandledStockCheck(String enquiriesHandledStockCheck) {
		this.enquiriesHandledStockCheck = enquiriesHandledStockCheck;
	}
}