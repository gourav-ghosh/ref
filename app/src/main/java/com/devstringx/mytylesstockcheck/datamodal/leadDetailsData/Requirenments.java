package com.devstringx.mytylesstockcheck.datamodal.leadDetailsData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Requirenments implements Serializable {

	@SerializedName("master_requirement_id")
	private int masterRequirementId;

	@SerializedName("requirement")
	private String requirement;

	public void setMasterRequirementId(int masterRequirementId){
		this.masterRequirementId = masterRequirementId;
	}

	public int getMasterRequirementId(){
		return masterRequirementId;
	}

	public void setRequirement(String requirement){
		this.requirement = requirement;
	}

	public String getRequirement(){
		return requirement;
	}
}