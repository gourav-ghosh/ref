package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

public class HelpingAgentItemItem{

	@SerializedName("po_code")
	private String poCode;

	@SerializedName("task_status")
	private String taskStatus;

	@SerializedName("po_status")
	private String poStatus;

	@SerializedName("task_completed_date")
	private String taskCompletedDate;

	@SerializedName("document")
	private String document;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("assignedPhone")
	private String assignedPhone;

	@SerializedName("specific_delivery_mode")
	private String specificDeliveryMode;

	@SerializedName("document_created_at")
	private String documentCreatedAt;

	@SerializedName("delivery_time")
	private String deliveryTime;

	@SerializedName("schedule_date")
	private String scheduleDate;

	@SerializedName("assignedId")
	private String assignedId;

	@SerializedName("LR_No")
	private String lRNo;

	@SerializedName("delivery_date")
	private String deliveryDate;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("po_name")
	private String poName;

	@SerializedName("comment")
	private String comment;

	@SerializedName("id")
	private String id;

	@SerializedName("assigned_by")
	private String assignedBy;

	@SerializedName("delivery_mode")
	private String deliveryMode;

	@SerializedName("assigned_to")
	private String assignedTo;

	public String getPoCode(){
		return poCode;
	}

	public String getTaskStatus(){
		return taskStatus;
	}

	public String getPoStatus(){
		return poStatus;
	}

	public String getTaskCompletedDate(){
		return taskCompletedDate;
	}

	public String getDocument(){
		return document;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getAssignedPhone(){
		return assignedPhone;
	}

	public String getSpecificDeliveryMode(){
		return specificDeliveryMode;
	}

	public String getDocumentCreatedAt(){
		return documentCreatedAt;
	}

	public String getDeliveryTime(){
		return deliveryTime;
	}

	public String getScheduleDate(){
		return scheduleDate;
	}

	public String getAssignedId(){
		return assignedId;
	}

	public String getLRNo(){
		return lRNo;
	}

	public String getDeliveryDate(){
		return deliveryDate;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getPoName(){
		return poName;
	}

	public String getComment(){
		return comment;
	}

	public String getId(){
		return id;
	}

	public String getAssignedBy(){
		return assignedBy;
	}

	public String getDeliveryMode(){
		return deliveryMode;
	}

	public String getAssignedTo(){
		return assignedTo;
	}
}