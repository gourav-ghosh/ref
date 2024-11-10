package com.devstringx.mytylesstockcheck.datamodal.getOrderAnalytics;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SaleAnalysisMonth{

	@SerializedName("values")
	private List<Object> values;

	@SerializedName("categories")
	private List<String> categories;

	public List<Object> getValues(){
		return values;
	}

	public List<String> getCategories(){
		return categories;
	}
}