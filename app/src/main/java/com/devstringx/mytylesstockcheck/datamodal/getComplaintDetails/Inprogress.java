package com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails;

import com.google.gson.annotations.SerializedName;

public class Inprogress{

	@SerializedName("complaint_id")
	private int complaintId;

	@SerializedName("activity_created_at")
	private String activityCreatedAt;

	@SerializedName("activity")
	private String activity;

	@SerializedName("activity_by")
	private int activityBy;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("activity_by_name")
	private String activityByName;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("message")
	private String message;

	public int getComplaintId(){
		return complaintId;
	}

	public String getActivityCreatedAt(){
		return activityCreatedAt;
	}

	public String getActivity(){
		return activity;
	}

	public int getActivityBy(){
		return activityBy;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getActivityByName(){
		return activityByName;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public String getMessage(){
		return message;
	}
}