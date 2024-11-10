package com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationsData;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class VendorQuotationsData {

	@SerializedName("records")
	private List<VendorQuotationsRecordsItem> records;

	@SerializedName("count")
	private int count;

	public void setRecords(List<VendorQuotationsRecordsItem> records){
		this.records = records;
	}

	public List<VendorQuotationsRecordsItem> getRecords(){
		return records;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}
}