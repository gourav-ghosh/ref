package com.devstringx.mytylesstockcheck.datamodal.architectListingData;

import com.google.gson.annotations.SerializedName;

public class RecordsItem{

	@SerializedName("totalOrderAmount")
	private Object totalOrderAmount;

	@SerializedName("order_count")
	private int orderCount;

	@SerializedName("company_name")
	private String companyName;

	@SerializedName("sales_executive")
	private String salesExecutive;

	@SerializedName("architect_id")
	private String architectId;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("primary_phone")
	private String primaryPhone;

	public void setTotalOrderAmount(Object totalOrderAmount){
		this.totalOrderAmount = totalOrderAmount;
	}

	public Object getTotalOrderAmount(){
		return totalOrderAmount;
	}

	public void setOrderCount(int orderCount){
		this.orderCount = orderCount;
	}

	public int getOrderCount(){
		return orderCount;
	}

	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}

	public String getCompanyName(){
		return companyName;
	}

	public void setSalesExecutive(String salesExecutive){
		this.salesExecutive = salesExecutive;
	}

	public String getSalesExecutive(){
		return salesExecutive;
	}

	public void setArchitectId(String architectId){
		this.architectId = architectId;
	}

	public String getArchitectId(){
		return architectId;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setPrimaryPhone(String primaryPhone){
		this.primaryPhone = primaryPhone;
	}

	public String getPrimaryPhone(){
		return primaryPhone;
	}
}