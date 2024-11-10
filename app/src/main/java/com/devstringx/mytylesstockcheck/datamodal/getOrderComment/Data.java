package com.devstringx.mytylesstockcheck.datamodal.getOrderComment;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("records")
	private List<OrderRecordsItem> records;

	@SerializedName("count")
	private int count;

	public List<OrderRecordsItem> getRecords(){
		return records;
	}

	public int getCount(){
		return count;
	}
}