package com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationDetails;

import com.google.gson.annotations.SerializedName;

public class VendorQuoteDetailResponse {

	@SerializedName("data")
	private VendorQuoteDetailData vendorQuoteDetailData;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setData(VendorQuoteDetailData vendorQuoteDetailData){
		this.vendorQuoteDetailData = vendorQuoteDetailData;
	}

	public VendorQuoteDetailData getData(){
		return vendorQuoteDetailData;
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