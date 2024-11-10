package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

public class DeliveryModesItem{

	@SerializedName("comments")
	private String comments;

	@SerializedName("Driver_Contact")
	private String driverContact;

	@SerializedName("Vehicle_Number")
	private String vehicleNumber;

	@SerializedName("Booking_Date")
	private String bookingDate;

	@SerializedName("delivery_mode")
	private String deliveryMode;

	@SerializedName("Driver_Name")
	private String driverName;

	@SerializedName("LR_No")
	private Object lRNo;

	public String getComments(){
		return comments;
	}

	public String getDriverContact(){
		return driverContact;
	}

	public String getVehicleNumber(){
		return vehicleNumber;
	}

	public String getBookingDate(){
		return bookingDate;
	}

	public String getDeliveryMode(){
		return deliveryMode;
	}

	public String getDriverName(){
		return driverName;
	}

	public Object getLRNo(){
		return lRNo;
	}
}