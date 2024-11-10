package com.devstringx.mytylesstockcheck.datamodal.branchNames;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("branch_name")
	private String branchName;

	@SerializedName("id")
	private int id;

	public void setBranchName(String branchName){
		this.branchName = branchName;
	}

	public String getBranchName(){
		return branchName;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}