package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

public class POItem{

	@SerializedName("po_code")
	private String poCode;

	@SerializedName("po_status")
	private String poStatus;

	@SerializedName("purpose")
	private Object purpose;

	@SerializedName("created_at")
	private Object createdAt;

	@SerializedName("deleted_by")
	private Object deletedBy;

	@SerializedName("specific_delivery_mode")
	private Object specificDeliveryMode;

	@SerializedName("vendor_email")
	private Object vendorEmail;

	@SerializedName("schedule_date")
	private String scheduleDate;

	@SerializedName("deleted_at")
	private Object deletedAt;

	@SerializedName("LR_No")
	private Object lRNo;

	@SerializedName("path")
	private String path;

	@SerializedName("uploaded_by")
	private int uploadedBy;

	@SerializedName("updated_at")
	private Object updatedAt;

	@SerializedName("vendor_id")
	private Object vendorId;

	public void setPath(String path) {
		this.path = path;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SerializedName("name")
	private String name;

	@SerializedName("comment")
	private Object comment;

	@SerializedName("id")
	private int id;

	@SerializedName("order_type")
	private String orderType;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("delivery_mode")
	private Object deliveryMode;

	@SerializedName("selected")
	private boolean selected;

	public String getPoCode(){
		return poCode;
	}

	public String getPoStatus(){
		return poStatus;
	}

	public Object getPurpose(){
		return purpose;
	}

	public Object getCreatedAt(){
		return createdAt;
	}

	public Object getDeletedBy(){
		return deletedBy;
	}

	public Object getSpecificDeliveryMode(){
		return specificDeliveryMode;
	}

	public Object getVendorEmail(){
		return vendorEmail;
	}

	public String getScheduleDate(){
		return scheduleDate;
	}

	public Object getDeletedAt(){
		return deletedAt;
	}

	public Object getLRNo(){
		return lRNo;
	}

	public String getPath(){
		return path;
	}

	public int getUploadedBy(){
		return uploadedBy;
	}

	public Object getUpdatedAt(){
		return updatedAt;
	}

	public Object getVendorId(){
		return vendorId;
	}

	public String getName(){
		return name;
	}

	public Object getComment(){
		return comment;
	}

	public int getId(){
		return id;
	}

	public String getOrderType(){
		return orderType;
	}

	public int getOrderId(){
		return orderId;
	}

	public Object getDeliveryMode(){
		return deliveryMode;
	}


	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}