package com.devstringx.mytylesstockcheck.datamodal.vendor.getVendorQuotationDetails;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class VendorQuoteDetailRecords {

	@SerializedName("enquiry_date_time")
	private String enquiryDateTime;

	@SerializedName("not_available_quantity")
	private String notAvailableQuantity;

	@SerializedName("product_image_key")
	private String productImageKey;

	@SerializedName("quotation_id")
	private String quotationId;

	@SerializedName("request_received_time")
	private String requestReceivedTime;

	@SerializedName("stock_check_status")
	private String stockCheckStatus;

	@SerializedName("discount")
	private String discount;

	@SerializedName("description")
	private String description;

	@SerializedName("product_code")
	private String productCode;

	@SerializedName("approved_by_name")
	private String approvedByName;

	@SerializedName("gst_rate")
	private String gstRate;

	@SerializedName("unit_of_measurement")
	private String unitOfMeasurement;

	@SerializedName("product_key")
	private List<String> productKey;

	@SerializedName("price")
	private String price;

	@SerializedName("product_id")
	private String productId;

	@SerializedName("enquiry_no")
	private String enquiryNo;

	@SerializedName("stock_check_type")
	private String stockCheckType;

	@SerializedName("id")
	private String id;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("is_ordered")
	private String isOrdered;

	@SerializedName("quantity")
	private String quantity;

	@SerializedName("not_available_in_days")
	private String notAvailableInDays;

	@SerializedName("responded_in")
	private String respondedIn;

	@SerializedName("history")
	private List<HistoryData> history;

	@SerializedName("hsn_code")
	private String hsnCode;

	@SerializedName("product_name")
	private String productName;

	@SerializedName("enquiry_status")
	private String enquiryStatus;

	@SerializedName("approved_by")
	private String approvedBy;

	@SerializedName("total_amount")
	private String totalAmount;

	@SerializedName("others_options")
	private String othersOptions;

	@SerializedName("product_attachment")
	private List<String> productAttachment;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("responded_check")
	private String respondedCheck;

	public void setEnquiryDateTime(String enquiryDateTime){
		this.enquiryDateTime = enquiryDateTime;
	}

	public String getEnquiryDateTime(){
		return enquiryDateTime;
	}

	public void setNotAvailableQuantity(String notAvailableQuantity){
		this.notAvailableQuantity = notAvailableQuantity;
	}

	public String getNotAvailableQuantity(){
		return notAvailableQuantity;
	}

	public void setProductImageKey(String productImageKey){
		this.productImageKey = productImageKey;
	}

	public String getProductImageKey(){
		return productImageKey;
	}

	public void setQuotationId(String quotationId){
		this.quotationId = quotationId;
	}

	public String getQuotationId(){
		return quotationId;
	}

	public void setRequestReceivedTime(String requestReceivedTime){
		this.requestReceivedTime = requestReceivedTime;
	}

	public String getRequestReceivedTime(){
		return requestReceivedTime;
	}

	public void setStockCheckStatus(String stockCheckStatus){
		this.stockCheckStatus = stockCheckStatus;
	}

	public String getStockCheckStatus(){
		return stockCheckStatus;
	}

	public void setDiscount(String discount){
		this.discount = discount;
	}

	public String getDiscount(){
		return discount;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setProductCode(String productCode){
		this.productCode = productCode;
	}

	public String getProductCode(){
		return productCode;
	}

	public void setApprovedByName(String approvedByName){
		this.approvedByName = approvedByName;
	}

	public String getApprovedByName(){
		return approvedByName;
	}

	public void setGstRate(String gstRate){
		this.gstRate = gstRate;
	}

	public String getGstRate(){
		return gstRate;
	}

	public void setUnitOfMeasurement(String unitOfMeasurement){
		this.unitOfMeasurement = unitOfMeasurement;
	}

	public String getUnitOfMeasurement(){
		return unitOfMeasurement;
	}

	public void setProductKey(List<String> productKey){
		this.productKey = productKey;
	}

	public List<String> getProductKey(){
		return productKey;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setProductId(String productId){
		this.productId = productId;
	}

	public String getProductId(){
		return productId;
	}

	public void setEnquiryNo(String enquiryNo){
		this.enquiryNo = enquiryNo;
	}

	public String getEnquiryNo(){
		return enquiryNo;
	}

	public void setStockCheckType(String stockCheckType){
		this.stockCheckType = stockCheckType;
	}

	public String getStockCheckType(){
		return stockCheckType;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setIsOrdered(String isOrdered){
		this.isOrdered = isOrdered;
	}

	public String getIsOrdered(){
		return isOrdered;
	}

	public void setQuantity(String quantity){
		this.quantity = quantity;
	}

	public String getQuantity(){
		return quantity;
	}

	public void setNotAvailableInDays(String notAvailableInDays){
		this.notAvailableInDays = notAvailableInDays;
	}

	public String getNotAvailableInDays(){
		return notAvailableInDays;
	}

	public void setRespondedIn(String respondedIn){
		this.respondedIn = respondedIn;
	}

	public String getRespondedIn(){
		return respondedIn;
	}

	public void setHistory(List<HistoryData> history){
		this.history = history;
	}

	public List<HistoryData> getHistory(){
		return history;
	}

	public void setHsnCode(String hsnCode){
		this.hsnCode = hsnCode;
	}

	public String getHsnCode(){
		return hsnCode;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setEnquiryStatus(String enquiryStatus){
		this.enquiryStatus = enquiryStatus;
	}

	public String getEnquiryStatus(){
		return enquiryStatus;
	}

	public void setApprovedBy(String approvedBy){
		this.approvedBy = approvedBy;
	}

	public String getApprovedBy(){
		return approvedBy;
	}

	public void setTotalAmount(String totalAmount){
		this.totalAmount = totalAmount;
	}

	public String getTotalAmount(){
		return totalAmount;
	}

	public void setOthersOptions(String othersOptions){
		this.othersOptions = othersOptions;
	}

	public String getOthersOptions(){
		return othersOptions;
	}

	public void setProductAttachment(List<String> productAttachment){
		this.productAttachment = productAttachment;
	}

	public List<String> getProductAttachment(){
		return productAttachment;
	}

	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public void setRespondedCheck(String respondedCheck){
		this.respondedCheck = respondedCheck;
	}

	public String getRespondedCheck(){
		return respondedCheck;
	}
}