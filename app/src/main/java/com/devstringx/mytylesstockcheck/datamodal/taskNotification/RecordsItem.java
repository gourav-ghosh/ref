package com.devstringx.mytylesstockcheck.datamodal.taskNotification;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecordsItem implements Serializable {

	@SerializedName("request_received_time_notification")
	private String requestReceivedTimeNotification;

	@SerializedName("secondary_phone")
	private String secondaryPhone;

	@SerializedName("request_received_time")
	private String requestReceivedTime;

	@SerializedName("subject")
	private String subject;

	@SerializedName("notification_status")
	private String notificationStatus;

	@SerializedName("task_id")
	private int taskId;

	@SerializedName("id")
	private int id;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("task_details")
	private String taskDetails;

	@SerializedName("primary_phone")
	private String primaryPhone;
	@SerializedName("is_read")
	private String isRead;
	@SerializedName("due_date")
	private String dueDate;
	@SerializedName("lead_id")
	private int leadId;

	public void setRequestReceivedTimeNotification(String requestReceivedTimeNotification){
		this.requestReceivedTimeNotification = requestReceivedTimeNotification;
	}

	public String getRequestReceivedTimeNotification(){
		return requestReceivedTimeNotification;
	}

	public void setSecondaryPhone(String secondaryPhone){
		this.secondaryPhone = secondaryPhone;
	}

	public String getSecondaryPhone(){
		return secondaryPhone;
	}

	public void setRequestReceivedTime(String requestReceivedTime){
		this.requestReceivedTime = requestReceivedTime;
	}

	public String getRequestReceivedTime(){
		return requestReceivedTime;
	}

	public void setSubject(String subject){
		this.subject = subject;
	}

	public String getSubject(){
		return subject;
	}

	public void setNotificationStatus(String notificationStatus){
		this.notificationStatus = notificationStatus;
	}

	public String getNotificationStatus(){
		return notificationStatus;
	}

	public void setTaskId(int taskId){
		this.taskId = taskId;
	}

	public int getTaskId(){
		return taskId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public void setTaskDetails(String taskDetails){
		this.taskDetails = taskDetails;
	}

	public String getTaskDetails(){
		return taskDetails;
	}

	public void setPrimaryPhone(String primaryPhone){
		this.primaryPhone = primaryPhone;
	}

	public String getPrimaryPhone(){
		return primaryPhone;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public int getLeadId() {
		return leadId;
	}

	public void setLeadId(int leadId) {
		this.leadId = leadId;
	}
}