package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

public class Architect{

	@SerializedName("country")
	private String country;

	@SerializedName("pincode")
	private Object pincode;

	@SerializedName("address")
	private Object address;

	@SerializedName("city")
	private String city;

	@SerializedName("established_year")
	private Object establishedYear;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("brand_name")
	private Object brandName;

	@SerializedName("primary_phone")
	private String primaryPhone;

	@SerializedName("secondary_phone")
	private String secondaryPhone;

	@SerializedName("company_name")
	private String companyName;

	@SerializedName("id")
	private int id;

	@SerializedName("state")
	private String state;

	@SerializedName("gst_number")
	private Object gstNumber;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("email")
	private String email;

	public String getCountry(){
		return country;
	}

	public Object getPincode(){
		return pincode;
	}

	public Object getAddress(){
		return address;
	}

	public String getCity(){
		return city;
	}

	public Object getEstablishedYear(){
		return establishedYear;
	}

	public String getLastName(){
		return lastName;
	}

	public Object getBrandName(){
		return brandName;
	}

	public String getPrimaryPhone(){
		return primaryPhone;
	}

	public String getSecondaryPhone(){
		return secondaryPhone;
	}

	public String getCompanyName(){
		return companyName;
	}

	public int getId(){
		return id;
	}

	public String getState(){
		return state;
	}

	public Object getGstNumber(){
		return gstNumber;
	}

	public String getFirstName(){
		return firstName;
	}

	public String getEmail(){
		return email;
	}
}