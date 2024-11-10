package com.devstringx.mytylesstockcheck.datamodal.getAllComplaintComments;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("records")
	private List<RecordsItem> records;

	@SerializedName("count")
	private int count;

	public List<RecordsItem> getRecords(){
		return records;
	}

	public int getCount(){
		return count;
	}
}