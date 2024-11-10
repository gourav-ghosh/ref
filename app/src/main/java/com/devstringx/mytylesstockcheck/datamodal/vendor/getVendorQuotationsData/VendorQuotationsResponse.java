package com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationsData;

import com.google.gson.annotations.SerializedName;

public class VendorQuotationsResponse {

	@SerializedName("data")
	private VendorQuotationsData vendorQuotationsData;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setData(VendorQuotationsData vendorQuotationsData){
		this.vendorQuotationsData = vendorQuotationsData;
	}

	public VendorQuotationsData getData(){
		return vendorQuotationsData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}
}