package com.devstringx.mytylesstockcheck.datamodal.getAllProducts;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ProductImagesItem{

	@SerializedName("feature_image")
	private boolean featureImage;

	@SerializedName("product_key")
	private List<String> productKey;

	@SerializedName("product_image_key")
	private String productImageKey;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("product_attachment")
	private List<String> productAttachment;

	@SerializedName("id")
	private int id;

	public void setFeatureImage(boolean featureImage){
		this.featureImage = featureImage;
	}

	public boolean isFeatureImage(){
		return featureImage;
	}

	public void setProductKey(List<String> productKey){
		this.productKey = productKey;
	}

	public List<String> getProductKey(){
		return productKey;
	}

	public void setProductImageKey(String productImageKey){
		this.productImageKey = productImageKey;
	}

	public String getProductImageKey(){
		return productImageKey;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setProductAttachment(List<String> productAttachment){
		this.productAttachment = productAttachment;
	}

	public List<String> getProductAttachment(){
		return productAttachment;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}