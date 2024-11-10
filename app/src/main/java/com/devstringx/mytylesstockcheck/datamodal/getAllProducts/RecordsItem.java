package com.devstringx.mytylesstockcheck.datamodal.getAllProducts;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RecordsItem{

	@SerializedName("vendor_company_name_display")
	private String vendorCompanyNameDisplay;

	@SerializedName("product_packing")
	private String productPacking;

	@SerializedName("product_weight")
	private String productWeight;

	@SerializedName("purchase_rate")
	private String purchaseRate;

	@SerializedName("product_status")
	private String productStatus;

	@SerializedName("discount")
	private String discount;

	@SerializedName("product_hsn_code")
	private String productHsnCode;

	@SerializedName("delivery_time")
	private String deliveryTime;

	@SerializedName("product_code")
	private String productCode;

	@SerializedName("inventory")
	private String inventory;

	@SerializedName("gst_rate")
	private String gstRate;

	@SerializedName("product_images")
	private List<ProductImagesItem> productImages;

	@SerializedName("product_sku")
	private String productSku;

	@SerializedName("unit_of_measurement")
	private String unitOfMeasurement;

	@SerializedName("product_category_name")
	private String productCategoryName;

	@SerializedName("final_price")
	private String finalPrice;

	@SerializedName("product_id")
	private String productId;

	@SerializedName("country_name")
	private String countryName;

	@SerializedName("id")
	private String id;

	@SerializedName("coverage")
	private String coverage;

	@SerializedName("product_material_type")
	private String productMaterialType;

	@SerializedName("product_finish")
	private String productFinish;

	@SerializedName("product_uses")
	private List<ProductUsesItem> productUses;

	@SerializedName("delivery_time_name")
	private String deliveryTimeName;

	@SerializedName("product_finish_name")
	private String productFinishName;

	@SerializedName("product_size")
	private String productSize;

	@SerializedName("brand_name")
	private String brandName;

	@SerializedName("product_quality")
	private String productQuality;

	@SerializedName("mrp")
	private String mrp;

	@SerializedName("tax")
	private String tax;

	@SerializedName("vendor_company_name")
	private String vendorCompanyName;

	@SerializedName("product_name")
	private String productName;

	@SerializedName("brand_id")
	private String brandId;

	@SerializedName("country_of_manufacture")
	private String countryOfManufacture;

	@SerializedName("quality_name")
	private String qualityName;

	@SerializedName("web_url")
	private String webUrl;

	@SerializedName("material_type_name")
	private String materialTypeName;

	@SerializedName("product_category")
	private List<ProductCategoryItem> productCategory;

	@SerializedName("product_uses_name")
	private String productUsesName;

	@SerializedName("selected")
	private boolean selected;

	public void setVendorCompanyNameDisplay(String vendorCompanyNameDisplay){
		this.vendorCompanyNameDisplay = vendorCompanyNameDisplay;
	}

	public String getVendorCompanyNameDisplay(){
		return vendorCompanyNameDisplay;
	}

	public void setProductPacking(String productPacking){
		this.productPacking = productPacking;
	}

	public String getProductPacking(){
		return productPacking;
	}

	public void setProductWeight(String productWeight){
		this.productWeight = productWeight;
	}

	public String getProductWeight(){
		return productWeight;
	}

	public void setPurchaseRate(String purchaseRate){
		this.purchaseRate = purchaseRate;
	}

	public String getPurchaseRate(){
		return purchaseRate;
	}

	public void setProductStatus(String productStatus){
		this.productStatus = productStatus;
	}

	public String getProductStatus(){
		return productStatus;
	}

	public void setDiscount(String discount){
		this.discount = discount;
	}

	public String getDiscount(){
		return discount;
	}

	public void setProductHsnCode(String productHsnCode){
		this.productHsnCode = productHsnCode;
	}

	public String getProductHsnCode(){
		return productHsnCode;
	}

	public void setDeliveryTime(String deliveryTime){
		this.deliveryTime = deliveryTime;
	}

	public String getDeliveryTime(){
		return deliveryTime;
	}

	public void setProductCode(String productCode){
		this.productCode = productCode;
	}

	public String getProductCode(){
		return productCode;
	}

	public void setInventory(String inventory){
		this.inventory = inventory;
	}

	public String getInventory(){
		return inventory;
	}

	public void setGstRate(String gstRate){
		this.gstRate = gstRate;
	}

	public String getGstRate(){
		return gstRate;
	}

	public void setProductImages(List<ProductImagesItem> productImages){
		this.productImages = productImages;
	}

	public List<ProductImagesItem> getProductImages(){
		return productImages;
	}

	public void setProductSku(String productSku){
		this.productSku = productSku;
	}

	public String getProductSku(){
		return productSku;
	}

	public void setUnitOfMeasurement(String unitOfMeasurement){
		this.unitOfMeasurement = unitOfMeasurement;
	}

	public String getUnitOfMeasurement(){
		return unitOfMeasurement;
	}

	public void setProductCategoryName(String productCategoryName){
		this.productCategoryName = productCategoryName;
	}

	public String getProductCategoryName(){
		return productCategoryName;
	}

	public void setFinalPrice(String finalPrice){
		this.finalPrice = finalPrice;
	}

	public String getFinalPrice(){
		return finalPrice;
	}

	public void setProductId(String productId){
		this.productId = productId;
	}

	public String getProductId(){
		return productId;
	}

	public void setCountryName(String countryName){
		this.countryName = countryName;
	}

	public String getCountryName(){
		return countryName;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setCoverage(String coverage){
		this.coverage = coverage;
	}

	public String getCoverage(){
		return coverage;
	}

	public void setProductMaterialType(String productMaterialType){
		this.productMaterialType = productMaterialType;
	}

	public String getProductMaterialType(){
		return productMaterialType;
	}

	public void setProductFinish(String productFinish){
		this.productFinish = productFinish;
	}

	public String getProductFinish(){
		return productFinish;
	}

	public void setProductUses(List<ProductUsesItem> productUses){
		this.productUses = productUses;
	}

	public List<ProductUsesItem> getProductUses(){
		return productUses;
	}

	public void setDeliveryTimeName(String deliveryTimeName){
		this.deliveryTimeName = deliveryTimeName;
	}

	public String getDeliveryTimeName(){
		return deliveryTimeName;
	}

	public void setProductFinishName(String productFinishName){
		this.productFinishName = productFinishName;
	}

	public String getProductFinishName(){
		return productFinishName;
	}

	public void setProductSize(String productSize){
		this.productSize = productSize;
	}

	public String getProductSize(){
		return productSize;
	}

	public void setBrandName(String brandName){
		this.brandName = brandName;
	}

	public String getBrandName(){
		return brandName;
	}

	public void setProductQuality(String productQuality){
		this.productQuality = productQuality;
	}

	public String getProductQuality(){
		return productQuality;
	}

	public void setMrp(String mrp){
		this.mrp = mrp;
	}

	public String getMrp(){
		return mrp;
	}

	public void setTax(String tax){
		this.tax = tax;
	}

	public String getTax(){
		return tax;
	}

	public void setVendorCompanyName(String vendorCompanyName){
		this.vendorCompanyName = vendorCompanyName;
	}

	public String getVendorCompanyName(){
		return vendorCompanyName;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setBrandId(String brandId){
		this.brandId = brandId;
	}

	public String getBrandId(){
		return brandId;
	}

	public void setCountryOfManufacture(String countryOfManufacture){
		this.countryOfManufacture = countryOfManufacture;
	}

	public String getCountryOfManufacture(){
		return countryOfManufacture;
	}

	public void setQualityName(String qualityName){
		this.qualityName = qualityName;
	}

	public String getQualityName(){
		return qualityName;
	}

	public void setWebUrl(String webUrl){
		this.webUrl = webUrl;
	}

	public String getWebUrl(){
		return webUrl;
	}

	public void setMaterialTypeName(String materialTypeName){
		this.materialTypeName = materialTypeName;
	}

	public String getMaterialTypeName(){
		return materialTypeName;
	}

	public void setProductCategory(List<ProductCategoryItem> productCategory){
		this.productCategory = productCategory;
	}

	public List<ProductCategoryItem> getProductCategory(){
		return productCategory;
	}

	public void setProductUsesName(String productUsesName){
		this.productUsesName = productUsesName;
	}

	public String getProductUsesName(){
		return productUsesName;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return "RecordsItem{" +
				"vendorCompanyNameDisplay='" + vendorCompanyNameDisplay + '\'' +
				", productPacking='" + productPacking + '\'' +
				", productWeight='" + productWeight + '\'' +
				", purchaseRate='" + purchaseRate + '\'' +
				", productStatus='" + productStatus + '\'' +
				", discount='" + discount + '\'' +
				", productHsnCode='" + productHsnCode + '\'' +
				", deliveryTime='" + deliveryTime + '\'' +
				", productCode='" + productCode + '\'' +
				", inventory='" + inventory + '\'' +
				", gstRate='" + gstRate + '\'' +
				", productImages=" + productImages +
				", productSku='" + productSku + '\'' +
				", unitOfMeasurement='" + unitOfMeasurement + '\'' +
				", productCategoryName='" + productCategoryName + '\'' +
				", finalPrice='" + finalPrice + '\'' +
				", productId='" + productId + '\'' +
				", countryName='" + countryName + '\'' +
				", id='" + id + '\'' +
				", coverage='" + coverage + '\'' +
				", productMaterialType='" + productMaterialType + '\'' +
				", productFinish='" + productFinish + '\'' +
				", productUses=" + productUses +
				", deliveryTimeName='" + deliveryTimeName + '\'' +
				", productFinishName='" + productFinishName + '\'' +
				", productSize='" + productSize + '\'' +
				", brandName='" + brandName + '\'' +
				", productQuality='" + productQuality + '\'' +
				", mrp='" + mrp + '\'' +
				", tax='" + tax + '\'' +
				", vendorCompanyName='" + vendorCompanyName + '\'' +
				", productName='" + productName + '\'' +
				", brandId='" + brandId + '\'' +
				", countryOfManufacture='" + countryOfManufacture + '\'' +
				", qualityName='" + qualityName + '\'' +
				", webUrl='" + webUrl + '\'' +
				", materialTypeName='" + materialTypeName + '\'' +
				", productCategory=" + productCategory +
				", productUsesName='" + productUsesName + '\'' +
				", selected=" + selected +
				'}';
	}
}