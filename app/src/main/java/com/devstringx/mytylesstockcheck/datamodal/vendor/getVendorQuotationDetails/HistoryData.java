package com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationDetails;

import com.google.gson.annotations.SerializedName;

public class HistoryData{

	@SerializedName("show_response")
	private String showResponse;

	@SerializedName("date_time")
	private String dateTime;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("user_name")
	private String userName;

	@SerializedName("field_value")
	private String fieldValue;

	@SerializedName("old_value")
	private String oldValue;

	@SerializedName("id")
	private int id;

	@SerializedName("quotation_product_id")
	private int quotationProductId;

	@SerializedName("new_value")
	private String newValue;

	public void setShowResponse(String showResponse){
		this.showResponse = showResponse;
	}

	public String getShowResponse(){
		return showResponse;
	}

	public void setDateTime(String dateTime){
		this.dateTime = dateTime;
	}

	public String getDateTime(){
		return dateTime;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setFieldValue(String fieldValue){
		this.fieldValue = fieldValue;
	}

	public String getFieldValue(){
		return fieldValue;
	}

	public void setOldValue(String oldValue){
		this.oldValue = oldValue;
	}

	public String getOldValue(){
		return oldValue;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setQuotationProductId(int quotationProductId){
		this.quotationProductId = quotationProductId;
	}

	public int getQuotationProductId(){
		return quotationProductId;
	}

	public void setNewValue(String newValue){
		this.newValue = newValue;
	}

	public String getNewValue(){
		return newValue;
	}
}