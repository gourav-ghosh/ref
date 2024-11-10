package com.devstringx.mytylesstockcheck.datamodal.ordersNotification;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("records")
	private List<OrderNotificationRecordsItem> records;

	@SerializedName("count")
	private int count;

	@SerializedName("unreadCount")
	private int unreadCount;

	@SerializedName("readCount")
	private int readCount;

	public List<OrderNotificationRecordsItem> getRecords(){
		return records;
	}

	public int getCount(){
		return count;
	}

	public int getUnreadCount(){
		return unreadCount;
	}

	public int getReadCount(){
		return readCount;
	}
}