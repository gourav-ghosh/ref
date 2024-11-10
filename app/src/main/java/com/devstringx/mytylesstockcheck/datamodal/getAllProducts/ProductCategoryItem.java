package com.devstringx.mytylesstockcheck.datamodal.getAllProducts;

import com.google.gson.annotations.SerializedName;

public class ProductCategoryItem{

	@SerializedName("category_name")
	private String categoryName;

	@SerializedName("id")
	private int id;

	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}

	public String getCategoryName(){
		return categoryName;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}