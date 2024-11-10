package com.devstringx.mytylesstockcheck.datamodal.products;

import com.devstringx.mytylesstockcheck.common.Common;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

public class ProductItem implements Serializable {
	@SerializedName("id")
	private String id="";

	@SerializedName("quotation_id")
	private String quotation_id="";

	@SerializedName("gst_rate")
	private String gstRate;

	@SerializedName("unit_of_measurement")
	private String unitOfMeasurement;

	@SerializedName("quantity")
	private BigInteger quantity;

	@SerializedName("total_amount")
	private String totalAmount;

	@SerializedName("price")
	private String price;

	@SerializedName("product_id")
	private String productId;

	@SerializedName("description")
	private String description;

	@SerializedName("discount")
	private String discount;

	@SerializedName("hsn_code")
	private String hsnCode;

	@SerializedName("image_url")
	private String imageUrl;
	@SerializedName("product_name")
	private String productName;
	@SerializedName("can_recheck")
	private boolean canRecheck;

	@SerializedName("product_attachment")
	private List<String> productAttachment;

	@SerializedName("stock_check_status")
	private String stockCheckStatus="";

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

	public void setQuantity(BigInteger quantity){
		this.quantity = quantity;
	}

	public BigInteger getQuantity(){
		return quantity;
	}

	public void setTotalAmount(String totalAmount){
		this.totalAmount = totalAmount;
	}

	public String getTotalAmount(){
		return totalAmount;
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

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setDiscount(String discount){
		this.discount = discount;
	}

	public String getDiscount(){
		return discount;
	}

	public void setHsnCode(String hsnCode){
		this.hsnCode = hsnCode;
	}

	public String getHsnCode(){
		return hsnCode;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public boolean isCanRecheck() {
		return canRecheck;
	}

	public void setCanRecheck(boolean canRecheck) {
		this.canRecheck = canRecheck;
	}

	public List<String> getProductAttachment() {
		return productAttachment;
	}

	public void setProductAttachment(List<String> productAttachment) {
		this.productAttachment = productAttachment;
		Common.showLog("PRODUCTIMAGE==="+new Gson().toJson(this.productAttachment));
		if(this.productAttachment.size()>0)
			imageUrl=this.productAttachment.get(0);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStockCheckStatus() {
		return stockCheckStatus;
	}

	public void setStockCheckStatus(String stockCheckStatus) {
		this.stockCheckStatus = stockCheckStatus;
	}

	public String getQuotation_id() {
		return quotation_id;
	}

	public void setQuotation_id(String quotation_id) {
		this.quotation_id = quotation_id;
	}
}