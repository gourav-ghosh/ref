package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Documents{

	@SerializedName("SO")
	private List<Object> sO;

	@SerializedName("PO")
	private List<POItem> pO;

	public List<Object> getSO(){
		return sO;
	}

	public List<POItem> getPO(){
		return pO;
	}
}