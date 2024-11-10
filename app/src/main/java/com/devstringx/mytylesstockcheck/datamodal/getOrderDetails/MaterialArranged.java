package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

public class MaterialArranged{

	@SerializedName("commented_by")
	private Object commentedBy;

	@SerializedName("customer_message")
	private Object customerMessage;

	@SerializedName("activity")
	private String activity;

	@SerializedName("wt_created_at")
	private String wtCreatedAt;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("activity_by")
	private String activityBy;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("comment")
	private Object comment;

	@SerializedName("message")
	private String message;

	public Object getCommentedBy(){
		return commentedBy;
	}

	public Object getCustomerMessage(){
		return customerMessage;
	}

	public String getActivity(){
		return activity;
	}

	public String getWtCreatedAt(){
		return wtCreatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getActivityBy(){
		return activityBy;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public Object getComment(){
		return comment;
	}

	public String getMessage(){
		return message;
	}
}