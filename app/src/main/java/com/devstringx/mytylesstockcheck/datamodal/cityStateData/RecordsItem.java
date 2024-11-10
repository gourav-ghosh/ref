package com.devstringx.mytylesstockcheck.datamodal.cityStateData;

import com.google.gson.annotations.SerializedName;

public class RecordsItem{

	@SerializedName("city_name")
	private String cityName;

	@SerializedName("state_name")
	private String stateName;

	@SerializedName("id")
	private String id;

	@SerializedName("state_id")
	private String stateId;

	@SerializedName("selected")
	private boolean selected;

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public void setCityName(String cityName){
		this.cityName = cityName;
	}

	public String getCityName(){
		return cityName;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setStateId(String stateId){
		this.stateId = stateId;
	}

	public String getStateId(){
		return stateId;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}