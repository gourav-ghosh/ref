package com.devstringx.mytylesstockcheck.datamodal.getOrderAnalytics;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ShowroomVisit{

	@SerializedName("values")
	private List<Integer> values;

	@SerializedName("categories")
	private List<String> categories;

	public List<Integer> getValues(){
		return values;
	}

	public List<String> getCategories(){
		return categories;
	}
}