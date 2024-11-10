package com.devstringx.mytylesstockcheck.datamodal.dashboard.leadConversationData;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("records")
	private List<RecordsItem> records;

	public void setRecords(List<RecordsItem> records){
		this.records = records;
	}

	public List<RecordsItem> getRecords(){
		return records;
	}
}