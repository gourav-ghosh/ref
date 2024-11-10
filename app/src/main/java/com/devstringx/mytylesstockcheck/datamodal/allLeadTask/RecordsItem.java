package com.devstringx.mytylesstockcheck.datamodal.allLeadTask;

import com.google.gson.annotations.SerializedName;

public class RecordsItem{
	@SerializedName("follow_up_date")
	private String followUpDate;
	@SerializedName("role_name")
	private String roleName;
	@SerializedName("updated_at")
	private String updatedAt;
	@SerializedName("subject")
	private String subject;
	@SerializedName("user_name")
	private String userName;
	@SerializedName("follow_up_time")
	private String followUpTime;
	@SerializedName("created_at")
	private String createdAt;
	@SerializedName("id")
	private int id;
	@SerializedName("created_by")
	private int createdBy;
	@SerializedName("lead_id")
	private int leadId;
	@SerializedName("task_details")
	private String taskDetails;
	@SerializedName("status")
	private String status;
	@SerializedName("full_name")
	private String fullName="";

	public String getFollowUpDate(){
		return followUpDate;
	}

	public String getRoleName(){
		return roleName;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getSubject(){
		return subject;
	}

	public String getUserName(){
		return userName;
	}

	public String getFollowUpTime(){
		return followUpTime;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public int getCreatedBy(){
		return createdBy;
	}

	public int getLeadId(){
		return leadId;
	}

	public String getTaskDetails(){
		return taskDetails;
	}

	public String getStatus(){
		return status;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
