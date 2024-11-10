package com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationDetails;

import com.google.gson.annotations.SerializedName;

public class VendorQuoteDetailData {

	@SerializedName("records")
	private VendorQuoteDetailRecords vendorQuoteDetailRecords;

	public void setRecords(VendorQuoteDetailRecords vendorQuoteDetailRecords){
		this.vendorQuoteDetailRecords = vendorQuoteDetailRecords;
	}

	public VendorQuoteDetailRecords getRecords(){
		return vendorQuoteDetailRecords;
	}
}