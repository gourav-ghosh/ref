package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

public class LinksItem{

	@SerializedName("updated_at")
	private Object updatedAt;

	@SerializedName("created_at")
	private Object createdAt;

	@SerializedName("deleted_by")
	private Object deletedBy;

	@SerializedName("id")
	private int id;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("deleted_at")
	private Object deletedAt;

	@SerializedName("URL")
	private String uRL;

	public Object getUpdatedAt(){
		return updatedAt;
	}

	public Object getCreatedAt(){
		return createdAt;
	}

	public Object getDeletedBy(){
		return deletedBy;
	}

	public int getId(){
		return id;
	}

	public int getOrderId(){
		return orderId;
	}

	public Object getDeletedAt(){
		return deletedAt;
	}

	public String getURL(){
		return uRL;
	}
}