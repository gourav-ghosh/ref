package com.devstringx.mytylesstockcheck.datamodal.allLeads;

import com.google.gson.annotations.SerializedName;

public class LatestActivityItem{

	@SerializedName("notes")
	private String notes;

	@SerializedName("activity_type")
	private String activityType;

	@SerializedName("activity_date")
	private String activityDate;

	public String getNotes(){
		return notes;
	}

	public String getActivityType(){
		return activityType;
	}

	public String getActivityDate(){
		return activityDate;
	}
}