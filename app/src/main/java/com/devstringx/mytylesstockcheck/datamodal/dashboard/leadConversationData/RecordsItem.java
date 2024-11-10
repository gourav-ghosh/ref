package com.devstringx.mytylesstockcheck.datamodal.dashboard.leadConversationData;

import com.google.gson.annotations.SerializedName;

public class RecordsItem{

	@SerializedName("total_leads")
	private int totalLeads;

	@SerializedName("showroom_visit")
	private int showroomVisit;

	@SerializedName("order_placed")
	private int orderPlaced;

	public void setTotalLeads(int totalLeads){
		this.totalLeads = totalLeads;
	}

	public int getTotalLeads(){
		return totalLeads;
	}

	public void setShowroomVisit(int showroomVisit){
		this.showroomVisit = showroomVisit;
	}

	public int getShowroomVisit(){
		return showroomVisit;
	}

	public void setOrderPlaced(int orderPlaced){
		this.orderPlaced = orderPlaced;
	}

	public int getOrderPlaced(){
		return orderPlaced;
	}
}