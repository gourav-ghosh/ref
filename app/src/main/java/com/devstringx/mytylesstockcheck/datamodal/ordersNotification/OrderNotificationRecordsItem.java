package com.devstringx.mytylesstockcheck.datamodal.ordersNotification;

import com.google.gson.annotations.SerializedName;

public class OrderNotificationRecordsItem {

	@SerializedName("user_device_token")
	private String userDeviceToken;

	@SerializedName("notification_type")
	private String notificationType;

	@SerializedName("request_received_time")
	private String requestReceivedTime;

	@SerializedName("notification_status")
	private String notificationStatus;

	@SerializedName("title")
	private String title;

	@SerializedName("body")
	private String body;

	@SerializedName("notification_send_count")
	private String notificationSendCount;

	@SerializedName("is_read")
	private String isRead;

	@SerializedName("request_received_time_notification")
	private String requestReceivedTimeNotification;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("id")
	private String id;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("updated_date")
	private String updatedDate;

	@SerializedName("order_id")
	private String orderId;

	public String getUserDeviceToken(){
		return userDeviceToken;
	}

	public String getNotificationType(){
		return notificationType;
	}

	public String getRequestReceivedTime(){
		return requestReceivedTime;
	}

	public String getNotificationStatus(){
		return notificationStatus;
	}

	public String getTitle(){
		return title;
	}

	public String getBody(){
		return body;
	}

	public String getNotificationSendCount(){
		return notificationSendCount;
	}

	public String getIsRead(){
		return isRead;
	}

	public String getRequestReceivedTimeNotification(){
		return requestReceivedTimeNotification;
	}

	public String getUserId(){
		return userId;
	}

	public String getId(){
		return id;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public String getUpdatedDate(){
		return updatedDate;
	}

	public String getOrderId(){
		return orderId;
	}
}
