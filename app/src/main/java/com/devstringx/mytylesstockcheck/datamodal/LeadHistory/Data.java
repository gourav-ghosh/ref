package com.devstringx.mytylesstockcheck.datamodal.LeadHistory;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("record")
	private List<RecordItem> record;

	public void setRecord(List<RecordItem> record){
		this.record = record;
	}

	public List<RecordItem> getRecord(){
		return record;
	}
}