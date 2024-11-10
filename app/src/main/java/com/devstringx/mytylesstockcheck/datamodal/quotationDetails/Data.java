package com.devstringx.mytylesstockcheck.datamodal.quotationDetails;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("records")
	private Records records;

	public void setRecords(Records records){
		this.records = records;
	}

	public Records getRecords(){
		return records;
	}
}