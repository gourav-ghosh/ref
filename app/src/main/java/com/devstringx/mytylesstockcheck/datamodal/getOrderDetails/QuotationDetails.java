package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import java.util.List;

import com.devstringx.mytylesstockcheck.datamodal.products.ProductItem;
import com.google.gson.annotations.SerializedName;

public class QuotationDetails{

	@SerializedName("sgst")
	private String sgst;

	@SerializedName("indicator")
	private String indicator;

	@SerializedName("is_transportation")
	private boolean isTransportation;

	@SerializedName("quote_number")
	private String quoteNumber;

	@SerializedName("is_unloading_charges")
	private boolean isUnloadingCharges;

	@SerializedName("primary_phone")
	private String primaryPhone;

	@SerializedName("shipping_gst_number")
	private String shippingGstNumber;

	@SerializedName("shipping_address_phone")
	private String shippingAddressPhone;

	@SerializedName("order_status")
	private String orderStatus;

	@SerializedName("billing_state_name")
	private String billingStateName;

	@SerializedName("unloading_charges")
	private String unloadingCharges;

	@SerializedName("shipping_address_pincode")
	private String shippingAddressPincode;

	@SerializedName("quotation_type")
	private String quotationType;

	@SerializedName("terms_conditions")
	private String termsConditions;

	@SerializedName("billing_address_line_2")
	private String billingAddressLine2;

	@SerializedName("transportation_charges_val")
	private String transportationChargesVal;

	@SerializedName("sales_executive_name")
	private String salesExecutiveName;

	@SerializedName("any_other_reference")
	private String anyOtherReference;

	@SerializedName("id")
	private String id;

	@SerializedName("billing_address_line_1")
	private String billingAddressLine1;

	@SerializedName("billing_address_pincode")
	private String billingAddressPincode;

	@SerializedName("shipping_address_line_1")
	private String shippingAddressLine1;

	@SerializedName("shipping_address_line_2")
	private String shippingAddressLine2;

	@SerializedName("full_name")
	private String fullName;

	@SerializedName("payment_receipt_url")
	private String paymentReceiptUrl;

	@SerializedName("transportation_charges")
	private String transportationCharges;

	@SerializedName("unloading_charges_val")
	private String unloadingChargesVal;

	@SerializedName("is_discount_charges")
	private boolean isDiscountCharges;

	@SerializedName("total_discount")
	private String totalDiscount;

	@SerializedName("shipping_city_name")
	private String shippingCityName;

	@SerializedName("sales_executive_user")
	private String salesExecutiveUser;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("igst")
	private String igst;

	@SerializedName("po_number")
	private String poNumber;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("secondary_phone")
	private String secondaryPhone;

	@SerializedName("stock_check_type")
	private String stockCheckType;

	@SerializedName("payment_verified")
	private String paymentVerified;

	@SerializedName("email")
	private String email;

	@SerializedName("total_taxable")
	private String totalTaxable;

	@SerializedName("quotation_status")
	private String quotationStatus;

	@SerializedName("cgst")
	private String cgst;

	@SerializedName("products_items")
	private List<ProductItem> productsItems;

	@SerializedName("billing_gst_number")
	private String billingGstNumber;

	@SerializedName("billing_address_phone")
	private String billingAddressPhone;

	@SerializedName("final_total")
	private String finalTotal;

	@SerializedName("final_total_words")
	private String finalTotalWords;

	@SerializedName("shipping_state_name")
	private String shippingStateName;

	@SerializedName("billing_city_name")
	private String billingCityName;

	@SerializedName("comment")
	private String comment;

	@SerializedName("quote_date")
	private String quoteDate;

	@SerializedName("customer_id")
	private String customerId;

	public String getSgst(){
		return sgst;
	}

	public String getIndicator(){
		return indicator;
	}

	public boolean isIsTransportation(){
		return isTransportation;
	}

	public String getQuoteNumber(){
		return quoteNumber;
	}

	public boolean isIsUnloadingCharges(){
		return isUnloadingCharges;
	}

	public String getPrimaryPhone(){
		return primaryPhone;
	}

	public String getShippingGstNumber(){
		return shippingGstNumber;
	}

	public String getShippingAddressPhone(){
		return shippingAddressPhone;
	}

	public String getOrderStatus(){
		return orderStatus;
	}

	public String getBillingStateName(){
		return billingStateName;
	}

	public String getUnloadingCharges(){
		return unloadingCharges;
	}

	public String getShippingAddressPincode(){
		return shippingAddressPincode;
	}

	public String getQuotationType(){
		return quotationType;
	}

	public String getTermsConditions(){
		return termsConditions;
	}

	public String getBillingAddressLine2(){
		return billingAddressLine2;
	}

	public String getTransportationChargesVal(){
		return transportationChargesVal;
	}

	public String getSalesExecutiveName(){
		return salesExecutiveName;
	}

	public String getAnyOtherReference(){
		return anyOtherReference;
	}

	public String getId(){
		return id;
	}

	public String getBillingAddressLine1(){
		return billingAddressLine1;
	}

	public String getBillingAddressPincode(){
		return billingAddressPincode;
	}

	public String getShippingAddressLine1(){
		return shippingAddressLine1;
	}

	public String getShippingAddressLine2(){
		return shippingAddressLine2;
	}

	public String getFullName(){
		return fullName;
	}

	public String getPaymentReceiptUrl(){
		return paymentReceiptUrl;
	}

	public String getTransportationCharges(){
		return transportationCharges;
	}

	public String getUnloadingChargesVal(){
		return unloadingChargesVal;
	}

	public boolean isIsDiscountCharges(){
		return isDiscountCharges;
	}

	public String getTotalDiscount(){
		return totalDiscount;
	}

	public String getShippingCityName(){
		return shippingCityName;
	}

	public String getSalesExecutiveUser(){
		return salesExecutiveUser;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getIgst(){
		return igst;
	}

	public String getPoNumber(){
		return poNumber;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getSecondaryPhone(){
		return secondaryPhone;
	}

	public String getStockCheckType(){
		return stockCheckType;
	}

	public String getPaymentVerified(){
		return paymentVerified;
	}

	public String getEmail(){
		return email;
	}

	public String getTotalTaxable(){
		return totalTaxable;
	}

	public String getQuotationStatus(){
		return quotationStatus;
	}

	public String getCgst(){
		return cgst;
	}

	public List<ProductItem> getProductsItems(){
		return productsItems;
	}

	public String getBillingGstNumber(){
		return billingGstNumber;
	}

	public String getBillingAddressPhone(){
		return billingAddressPhone;
	}

	public String getFinalTotal(){
		return finalTotal;
	}

	public String getFinalTotalWords(){
		return finalTotalWords;
	}

	public String getShippingStateName(){
		return shippingStateName;
	}

	public String getBillingCityName(){
		return billingCityName;
	}

	public String getComment(){
		return comment;
	}

	public String getQuoteDate(){
		return quoteDate;
	}

	public String getCustomerId(){
		return customerId;
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

}