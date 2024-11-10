package com.devstringx.mytylesstockcheck.datamodal.leadstage;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("record")
	private List<String> record;

	public void setRecord(List<String> record){
		this.record = record;
	}

	public List<String> getRecord(){
		return record;
	}
}