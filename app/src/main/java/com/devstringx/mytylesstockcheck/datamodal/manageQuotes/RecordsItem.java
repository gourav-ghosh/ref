package com.devstringx.mytylesstockcheck.datamodal.manageQuotes;

import com.google.gson.annotations.SerializedName;

public class RecordsItem{

	@SerializedName("full_name")
	private String fullName;

	@SerializedName("quote_number")
	private String quoteNumber;

	@SerializedName("final_total")
	private String finalTotal="";

	@SerializedName("sales_executive_name")
	private String salesExecutiveName;

	@SerializedName("quotation_status")
	private String quotationStatus;

	@SerializedName("id")
	private int id;

	@SerializedName("quote_date")
	private String quoteDate;

	@SerializedName("customer_id")
	private int customerId;
	@SerializedName("total_quotation_products")
	private int totalQuotationProducts;
	@SerializedName("total_response")
	private int totalResponse;

	public void setFullName(String fullName){
		this.fullName = fullName;
	}

	public String getFullName(){
		return fullName;
	}

	public void setQuoteNumber(String quoteNumber){
		this.quoteNumber = quoteNumber;
	}

	public String getQuoteNumber(){
		return quoteNumber;
	}

	public void setFinalTotal(String finalTotal){
		this.finalTotal = finalTotal;
	}

	public String getFinalTotal(){
		return finalTotal;
	}

	public void setSalesExecutiveName(String salesExecutiveName){
		this.salesExecutiveName = salesExecutiveName;
	}

	public String getSalesExecutiveName(){
		return salesExecutiveName;
	}

	public void setQuotationStatus(String quotationStatus){
		this.quotationStatus = quotationStatus;
	}

	public String getQuotationStatus(){
		return quotationStatus;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setQuoteDate(String quoteDate){
		this.quoteDate = quoteDate;
	}

	public String getQuoteDate(){
		return quoteDate;
	}

	public void setCustomerId(int customerId){
		this.customerId = customerId;
	}

	public int getCustomerId(){
		return customerId;
	}

	public int getTotalQuotationProducts() {
		return totalQuotationProducts;
	}

	public void setTotalQuotationProducts(int totalQuotationProducts) {
		this.totalQuotationProducts = totalQuotationProducts;
	}

	public int getTotalResponse() {
		return totalResponse;
	}

	public void setTotalResponse(int totalResponse) {
		this.totalResponse = totalResponse;
	}
}