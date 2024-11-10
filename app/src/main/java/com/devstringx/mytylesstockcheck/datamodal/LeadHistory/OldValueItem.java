package com.devstringx.mytylesstockcheck.datamodal.LeadHistory;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OldValueItem implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("requirement")
	private String requirement;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setRequirement(String requirement){
		this.requirement = requirement;
	}

	public String getRequirement(){
		return requirement;
	}
}