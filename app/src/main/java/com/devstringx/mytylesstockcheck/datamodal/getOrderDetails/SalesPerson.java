package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

public class SalesPerson{

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("phone_number")
	private String phoneNumber;

	@SerializedName("id")
	private String id;

	@SerializedName("first_name")
	private String firstName;

	public String getLastName(){
		return lastName;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public String getId(){
		return id;
	}

	public String getFirstName(){
		return firstName;
	}
}