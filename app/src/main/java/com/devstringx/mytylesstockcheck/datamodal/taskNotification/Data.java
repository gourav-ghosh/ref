package com.devstringx.mytylesstockcheck.datamodal.taskNotification;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("records")
	private List<RecordsItem> records;

	@SerializedName("count")
	private int count;

	@SerializedName("unreadCount")
	private int unreadCount;

	@SerializedName("readCount")
	private int readCount;

	public void setRecords(List<RecordsItem> records){
		this.records = records;
	}

	public List<RecordsItem> getRecords(){
		return records;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setUnreadCount(int unreadCount){
		this.unreadCount = unreadCount;
	}

	public int getUnreadCount(){
		return unreadCount;
	}

	public void setReadCount(int readCount){
		this.readCount = readCount;
	}

	public int getReadCount(){
		return readCount;
	}
}