package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

public class OrderRescheduleDatasItem{

	@SerializedName("new_delivery_time")
	private String newDeliveryTime;

	@SerializedName("old_delivery_date")
	private String oldDeliveryDate;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("old_delivery_time")
	private String oldDeliveryTime;

	@SerializedName("new_delivery_date")
	private String newDeliveryDate;

	@SerializedName("updated_by")
	private int updatedBy;

	@SerializedName("reason_for_reschedule")
	private String reasonForReschedule;

	@SerializedName("deleted_by")
	private Object deletedBy;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("deleted_at")
	private Object deletedAt;

	public String getNewDeliveryTime(){
		return newDeliveryTime;
	}

	public String getOldDeliveryDate(){
		return oldDeliveryDate;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getOldDeliveryTime(){
		return oldDeliveryTime;
	}

	public String getNewDeliveryDate(){
		return newDeliveryDate;
	}

	public int getUpdatedBy(){
		return updatedBy;
	}

	public String getReasonForReschedule(){
		return reasonForReschedule;
	}

	public Object getDeletedBy(){
		return deletedBy;
	}

	public String getCreatedAt(){
		return createdAt;
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
}