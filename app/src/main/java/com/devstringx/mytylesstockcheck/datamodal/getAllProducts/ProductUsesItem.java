package com.devstringx.mytylesstockcheck.datamodal.getAllProducts;

import com.google.gson.annotations.SerializedName;

public class ProductUsesItem{

	@SerializedName("use_type_name")
	private String useTypeName;

	@SerializedName("id")
	private int id;

	public void setUseTypeName(String useTypeName){
		this.useTypeName = useTypeName;
	}

	public String getUseTypeName(){
		return useTypeName;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}