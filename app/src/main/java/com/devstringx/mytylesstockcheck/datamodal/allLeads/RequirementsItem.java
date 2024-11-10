package com.devstringx.mytylesstockcheck.datamodal.allLeads;

import com.google.gson.annotations.SerializedName;

public class RequirementsItem{

	@SerializedName("master_requirement_id")
	private int masterRequirementId;

	@SerializedName("requirement")
	private String requirement;

	public int getMasterRequirementId(){
		return masterRequirementId;
	}

	public String getRequirement(){
		return requirement;
	}
}