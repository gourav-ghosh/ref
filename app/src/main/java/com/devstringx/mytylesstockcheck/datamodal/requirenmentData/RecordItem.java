package com.devstringx.mytylesstockcheck.datamodal.requirenmentData;

import com.google.gson.annotations.SerializedName;

public class RecordItem{

	@SerializedName("id")
	private int id;

	@SerializedName("requirement")
	private String requirement;

	@SerializedName("selected")
	private boolean selected;

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

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}