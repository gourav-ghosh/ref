package com.devstringx.mytylesstockcheck.datamodal.getAllOrders;

import com.google.gson.annotations.SerializedName;

public class RecordsItem{

	@SerializedName("delivery_address")
	private String deliveryAddress;

	@SerializedName("payment_status")
	private String paymentStatus;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("payment_verification")
	private String paymentVerification;

	@SerializedName("delivery_time")
	private String deliveryTime;

	@SerializedName("delivery_date")
	private String deliveryDate;

	@SerializedName("activityDate")
	private Object activityDate;

	@SerializedName("delivery_type")
	private String deliveryType;

	@SerializedName("order_amount")
	private String orderAmount;

	@SerializedName("is_delayed")
	private String isDelayed;

	@SerializedName("sales_executive_name")
	private String salesExecutiveName;

	@SerializedName("delivery_location_type")
	private String deliveryLocationType;

	@SerializedName("id")
	private int id;

	@SerializedName("customer_name")
	private String customerName;

	@SerializedName("sale_order_no")
	private String saleOrderNo;

	@SerializedName("delivery_agent_name")
	private Object deliveryAgentName;

	@SerializedName("status")
	private String status;
	@SerializedName("purpose")
	private String purpose;

	@SerializedName("deleted_by")
	private String deletedBy;

	@SerializedName("task_id")
	private String taskId;

	@SerializedName("vendor_email")
	private String vendorEmail;

	@SerializedName("path")
	private String path;

	@SerializedName("uploaded_by")
	private int uploadedBy;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("order_type")
	private String orderType;

	@SerializedName("po_code")
	private String poCode;

	@SerializedName("po_status")
	private String poStatus;

	@SerializedName("task_status")
	private String taskStatus;

	@SerializedName("wf_schedule_date")
	private String wfScheduleDate;

	@SerializedName("specific_delivery_mode")
	private String specificDeliveryMode;

	@SerializedName("document_created_at")
	private String documentCreatedAt;

	@SerializedName("schedule_date")
	private String scheduleDate;

	@SerializedName("deleted_at")
	private String deletedAt;

	@SerializedName("LR_No")
	private String lRNo;

	@SerializedName("vendor_id")
	private String vendorId;

	@SerializedName("name")
	private String name;

	@SerializedName("comment")
	private String comment;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("delivery_mode")
	private String deliveryMode;

	public String getPurpose(){
		return purpose;
	}

	public String getDeletedBy(){
		return deletedBy;
	}

	public String getTaskId(){
		return taskId;
	}

	public String getVendorEmail(){
		return vendorEmail;
	}

	public String getPath(){
		return path;
	}

	public int getUploadedBy(){
		return uploadedBy;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getOrderType(){
		return orderType;
	}

	public String getPoCode(){
		return poCode;
	}

	public String getPoStatus(){
		return poStatus;
	}

	public String getTaskStatus(){
		return taskStatus;
	}

	public String getWfScheduleDate(){
		return wfScheduleDate;
	}

	public String getSpecificDeliveryMode(){
		return specificDeliveryMode;
	}

	public String getDocumentCreatedAt(){
		return documentCreatedAt;
	}

	public String getScheduleDate(){
		return scheduleDate;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	public String getLRNo(){
		return lRNo;
	}

	public String getVendorId(){
		return vendorId;
	}

	public String getName(){
		return name;
	}

	public String getComment(){
		return comment;
	}

	public int getOrderId(){
		return orderId;
	}

	public String getDeliveryMode(){
		return deliveryMode;
	}

	public String getDeliveryAddress(){
		return deliveryAddress;
	}

	public String getPaymentStatus(){
		return paymentStatus;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getPaymentVerification(){
		return paymentVerification;
	}

	public String getDeliveryTime(){
		return deliveryTime;
	}

	public String getDeliveryDate(){
		return deliveryDate;
	}

	public Object getActivityDate(){
		return activityDate;
	}

	public String getDeliveryType(){
		return deliveryType;
	}

	public String getOrderAmount(){
		return orderAmount;
	}

	public String getIsDelayed(){
		return isDelayed;
	}

	public String getSalesExecutiveName(){
		return salesExecutiveName;
	}

	public String getDeliveryLocationType(){
		return deliveryLocationType;
	}

	public int getId(){
		return id;
	}

	public String getCustomerName(){
		return customerName;
	}

	public String getSaleOrderNo(){
		return saleOrderNo;
	}

	public Object getDeliveryAgentName(){
		return deliveryAgentName;
	}

	public String getStatus(){
		return status;
	}
}