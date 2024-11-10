package com.devstringx.mytylesstockcheck.datamodal.leadDetailsData;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("record")
	private Record record;

	public void setRecord(Record record){
		this.record = record;
	}

	public Record getRecord(){
		return record;
	}
}