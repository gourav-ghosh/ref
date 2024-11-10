package com.devstringx.mytylesstockcheck.datamodal.responseNotification;

import com.google.gson.annotations.SerializedName;

public class RecordsItem{

	@SerializedName("product_multiple_images")
	private String productImages;

	@SerializedName("is_read")
	private String isRead;

	@SerializedName("unit_of_measurement")
	private String unitOfMeasurement;

	@SerializedName("stock_check_response")
	private String stockCheckResponse;

	@SerializedName("quantity")
	private String quantity;

	@SerializedName("request_received_time_notification")
	private String requestReceivedTimeNotification;

	@SerializedName("quote_number")
	private String quoteNumber;

	@SerializedName("request_received_time")
	private String requestReceivedTime;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("id")
	private int id;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("product_name")
	private String productName;

	@SerializedName("quotation_id")
	private String quotation_id;

	public void setProductImages(String productImages){
		this.productImages = productImages;
	}

	public String getProductImages(){
		return productImages;
	}

	public void setIsRead(String isRead){
		this.isRead = isRead;
	}

	public String getIsRead(){
		return isRead;
	}

	public void setUnitOfMeasurement(String unitOfMeasurement){
		this.unitOfMeasurement = unitOfMeasurement;
	}

	public String getUnitOfMeasurement(){
		return unitOfMeasurement;
	}

	public void setStockCheckResponse(String stockCheckResponse){
		this.stockCheckResponse = stockCheckResponse;
	}

	public String getStockCheckResponse(){
		return stockCheckResponse;
	}

	public void setQuantity(String quantity){
		this.quantity = quantity;
	}

	public String getQuantity(){
		return quantity;
	}

	public void setRequestReceivedTimeNotification(String requestReceivedTimeNotification){
		this.requestReceivedTimeNotification = requestReceivedTimeNotification;
	}

	public String getRequestReceivedTimeNotification(){
		return requestReceivedTimeNotification;
	}

	public void setQuoteNumber(String quoteNumber){
		this.quoteNumber = quoteNumber;
	}

	public String getQuoteNumber(){
		return quoteNumber;
	}

	public void setRequestReceivedTime(String requestReceivedTime){
		this.requestReceivedTime = requestReceivedTime;
	}

	public String getRequestReceivedTime(){
		return requestReceivedTime;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
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

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public String getQuotation_id() {
		return quotation_id;
	}

	public void setQuotation_id(String quotation_id) {
		this.quotation_id = quotation_id;
	}
}