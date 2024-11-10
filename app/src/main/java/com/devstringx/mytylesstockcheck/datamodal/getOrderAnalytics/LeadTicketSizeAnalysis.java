package com.devstringx.mytylesstockcheck.datamodal.getOrderAnalytics;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LeadTicketSizeAnalysis{

	@SerializedName("number")
	private List<Integer> number;

	@SerializedName("percentage")
	private List<Integer> percentage;

	@SerializedName("categories")
	private List<String> categories;

	public List<Integer> getNumber(){
		return number;
	}

	public List<Integer> getPercentage(){
		return percentage;
	}

	public List<String> getCategories(){
		return categories;
	}
}