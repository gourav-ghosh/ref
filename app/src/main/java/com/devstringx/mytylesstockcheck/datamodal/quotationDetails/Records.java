package com.devstringx.mytylesstockcheck.datamodal.quotationDetails;

import com.devstringx.mytylesstockcheck.datamodal.products.ProductItem;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Records implements Serializable {

	@SerializedName("sgst")
	private String sgst;

	@SerializedName("is_transportation")
	private boolean isTransportation;

	@SerializedName("total_discount")
	private String totalDiscount;

	@SerializedName("shipping_city_name")
	private String shippingCityName;

	@SerializedName("sales_executive_user")
	private String salesExecutiveUser;

	@SerializedName("quote_number")
	private String quoteNumber;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("is_unloading_charges")
	private boolean isUnloadingCharges;

	@SerializedName("is_discount_charges")
	private boolean isDiscountCharges;

	@SerializedName("igst")
	private String igst;

	@SerializedName("primary_phone")
	private String primaryPhone;

	@SerializedName("shipping_gst_number")
	private String shippingGstNumber;

	@SerializedName("shipping_address_phone")
	private String shippingAddressPhone;

	@SerializedName("po_number")
	private String poNumber;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("billing_state_name")
	private String billingStateName;

	@SerializedName("secondary_phone")
	private String secondaryPhone;

	@SerializedName("unloading_charges")
	private String unloadingCharges;

	@SerializedName("shipping_address_pincode")
	private String shippingAddressPincode;

	@SerializedName("quotation_type")
	private String quotationType;

	@SerializedName("terms_conditions")
	private String termsConditions;

	@SerializedName("bank_details")
	private String bankDetails;

	@SerializedName("billing_address_line_2")
	private String billingAddressLine2;

	@SerializedName("sales_executive_name")
	private String salesExecutiveName;

	@SerializedName("stock_check_type")
	private String stockCheckType;

	@SerializedName("any_other_reference")
	private String anyOtherReference;

	@SerializedName("id")
	private int id;

	@SerializedName("billing_address_line_1")
	private String billingAddressLine1;

	@SerializedName("billing_address_pincode")
	private String billingAddressPincode;

	@SerializedName("total_taxable")
	private String totalTaxable;

	@SerializedName("shipping_address_line_1")
	private String shippingAddressLine1;

	@SerializedName("shipping_address_line_2")
	private String shippingAddressLine2;

	@SerializedName("quotation_status")
	private String quotationStatus;

	@SerializedName("cgst")
	private String cgst;

	@SerializedName("billing_gst_number")
	private String billingGstNumber;

	@SerializedName("full_name")
	private String fullName;

	@SerializedName("billing_address_phone")
	private String billingAddressPhone;

	@SerializedName("transportation_charges")
	private String transportationCharges;

	@SerializedName("final_total")
	private String finalTotal;

	@SerializedName("final_total_words")
	private String finalTotalWords;

	@SerializedName("shipping_state_name")
	private String shippingStateName;

	@SerializedName("billing_city_name")
	private String billingCityName;

	@SerializedName("quote_date")
	private String quoteDate;

	@SerializedName("customer_id")
	private int customerId;

	@SerializedName("products_items")
	private List<ProductItem> productsItems;

	public void setSgst(String sgst){
		this.sgst = sgst;
	}

	public String getSgst(){
		return sgst;
	}

	public void setTotalDiscount(String totalDiscount){
		this.totalDiscount = totalDiscount;
	}

	public String getTotalDiscount(){
		return totalDiscount;
	}

	public void setShippingCityName(String shippingCityName){
		this.shippingCityName = shippingCityName;
	}

	public String getShippingCityName(){
		return shippingCityName;
	}

	public void setSalesExecutiveUser(String salesExecutiveUser){
		this.salesExecutiveUser = salesExecutiveUser;
	}

	public String getSalesExecutiveUser(){
		return salesExecutiveUser;
	}

	public void setQuoteNumber(String quoteNumber){
		this.quoteNumber = quoteNumber;
	}

	public String getQuoteNumber(){
		return quoteNumber;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setIgst(String igst){
		this.igst = igst;
	}

	public String getIgst(){
		return igst;
	}

	public void setPrimaryPhone(String primaryPhone){
		this.primaryPhone = primaryPhone;
	}

	public String getPrimaryPhone(){
		return primaryPhone;
	}

	public void setShippingGstNumber(String shippingGstNumber){
		this.shippingGstNumber = shippingGstNumber;
	}

	public String getShippingGstNumber(){
		return shippingGstNumber;
	}

	public void setShippingAddressPhone(String shippingAddressPhone){
		this.shippingAddressPhone = shippingAddressPhone;
	}

	public String getShippingAddressPhone(){
		return shippingAddressPhone;
	}

	public void setPoNumber(String poNumber){
		this.poNumber = poNumber;
	}

	public String getPoNumber(){
		return poNumber;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setBillingStateName(String billingStateName){
		this.billingStateName = billingStateName;
	}

	public String getBillingStateName(){
		return billingStateName;
	}

	public void setSecondaryPhone(String secondaryPhone){
		this.secondaryPhone = secondaryPhone;
	}

	public String getSecondaryPhone(){
		return secondaryPhone;
	}

	public void setUnloadingCharges(String unloadingCharges){
		this.unloadingCharges = unloadingCharges;
	}

	public String getUnloadingCharges(){
		return unloadingCharges;
	}

	public void setShippingAddressPincode(String shippingAddressPincode){
		this.shippingAddressPincode = shippingAddressPincode;
	}

	public String getShippingAddressPincode(){
		return shippingAddressPincode;
	}

	public void setQuotationType(String quotationType){
		this.quotationType = quotationType;
	}

	public String getQuotationType(){
		return quotationType;
	}

	public void setTermsConditions(String termsConditions){
		this.termsConditions = termsConditions;
	}

	public String getTermsConditions(){
		return termsConditions;
	}

	public void setBillingAddressLine2(String billingAddressLine2){
		this.billingAddressLine2 = billingAddressLine2;
	}

	public String getBillingAddressLine2(){
		return billingAddressLine2;
	}

	public void setSalesExecutiveName(String salesExecutiveName){
		this.salesExecutiveName = salesExecutiveName;
	}

	public String getSalesExecutiveName(){
		return salesExecutiveName;
	}

	public void setStockCheckType(String stockCheckType){
		this.stockCheckType = stockCheckType;
	}

	public String getStockCheckType(){
		return stockCheckType;
	}

	public void setAnyOtherReference(String anyOtherReference){
		this.anyOtherReference = anyOtherReference;
	}

	public String getAnyOtherReference(){
		return anyOtherReference;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setBillingAddressLine1(String billingAddressLine1){
		this.billingAddressLine1 = billingAddressLine1;
	}

	public String getBillingAddressLine1(){
		return billingAddressLine1;
	}

	public void setBillingAddressPincode(String billingAddressPincode){
		this.billingAddressPincode = billingAddressPincode;
	}

	public String getBillingAddressPincode(){
		return billingAddressPincode;
	}

	public void setTotalTaxable(String totalTaxable){
		this.totalTaxable = totalTaxable;
	}

	public String getTotalTaxable(){
		return totalTaxable;
	}

	public void setShippingAddressLine1(String shippingAddressLine1){
		this.shippingAddressLine1 = shippingAddressLine1;
	}

	public String getShippingAddressLine1(){
		return shippingAddressLine1;
	}

	public void setShippingAddressLine2(String shippingAddressLine2){
		this.shippingAddressLine2 = shippingAddressLine2;
	}

	public String getShippingAddressLine2(){
		return shippingAddressLine2;
	}

	public void setQuotationStatus(String quotationStatus){
		this.quotationStatus = quotationStatus;
	}

	public String getQuotationStatus(){
		return quotationStatus;
	}

	public void setCgst(String cgst){
		this.cgst = cgst;
	}

	public String getCgst(){
		return cgst;
	}

	public void setBillingGstNumber(String billingGstNumber){
		this.billingGstNumber = billingGstNumber;
	}

	public String getBillingGstNumber(){
		return billingGstNumber;
	}

	public void setFullName(String fullName){
		this.fullName = fullName;
	}

	public String getFullName(){
		return fullName;
	}

	public void setBillingAddressPhone(String billingAddressPhone){
		this.billingAddressPhone = billingAddressPhone;
	}

	public String getBillingAddressPhone(){
		return billingAddressPhone;
	}

	public void setTransportationCharges(String transportationCharges){
		this.transportationCharges = transportationCharges;
	}

	public String getTransportationCharges(){
		return transportationCharges;
	}

	public void setFinalTotal(String finalTotal){
		this.finalTotal = finalTotal;
	}

	public String getFinalTotal(){
		return finalTotal;
	}

	public void setFinalTotalWords(String finalTotalWords){
		this.finalTotalWords = finalTotalWords;
	}

	public String getFinalTotalWords(){
		return finalTotalWords;
	}

	public void setShippingStateName(String shippingStateName){
		this.shippingStateName = shippingStateName;
	}

	public String getShippingStateName(){
		return shippingStateName;
	}

	public void setBillingCityName(String billingCityName){
		this.billingCityName = billingCityName;
	}

	public String getBillingCityName(){
		return billingCityName;
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

	public List<ProductItem> getProductsItems() {
		return productsItems;
	}

	public void setProductsItems(List<ProductItem> productsItems) {
		this.productsItems = productsItems;
	}

	public boolean isTransportation() {
		return isTransportation;
	}

	public void setTransportation(boolean transportation) {
		isTransportation = transportation;
	}

	public boolean isUnloadingCharges() {
		return isUnloadingCharges;
	}

	public void setUnloadingCharges(boolean unloadingCharges) {
		isUnloadingCharges = unloadingCharges;
	}

	public boolean isDiscountCharges() {
		return isDiscountCharges;
	}

	public void setDiscountCharges(boolean discountCharges) {
		isDiscountCharges = discountCharges;
	}

	public String getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(String bankDetails) {
		this.bankDetails = bankDetails;
	}
}