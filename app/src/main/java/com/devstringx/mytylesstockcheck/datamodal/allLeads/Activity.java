package com.devstringx.mytylesstockcheck.datamodal.allLeads;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Activity implements Serializable {

	@SerializedName("activity_created_at")
	private String activityCreatedAt;

	@SerializedName("notes")
	private String notes;

	@SerializedName("activity_type")
	private String activityType;

	@SerializedName("activity_id")
	private int activityId;

	public void setActivityCreatedAt(String activityCreatedAt){
		this.activityCreatedAt = activityCreatedAt;
	}

	public String getActivityCreatedAt(){
		return activityCreatedAt;
	}

	public void setNotes(String notes){
		this.notes = notes;
	}

	public String getNotes(){
		return notes;
	}

	public void setActivityType(String activityType){
		this.activityType = activityType;
	}

	public String getActivityType(){
		return activityType;
	}

	public void setActivityId(int activityId){
		this.activityId = activityId;
	}

	public int getActivityId(){
		return activityId;
	}
}