package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderTasks{

	@SerializedName("mainAgent")
	private List<MainAgent> mainAgent;

	@SerializedName("helpingAgent")
	private List<List<HelpingAgentItemItem>> helpingAgent;

	public List<MainAgent> getMainAgent(){
		return mainAgent;
	}

	public List<List<HelpingAgentItemItem>> getHelpingAgent(){
		return helpingAgent;
	}
}