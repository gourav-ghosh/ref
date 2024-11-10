package com.devstringx.mytylesstockcheck.datamodal.dashboard.leadAnalyticData;

import com.google.gson.annotations.SerializedName;

public class RecordsItem{

	@SerializedName("month_name")
	private String monthName;

	@SerializedName("Web Signups")
	private int webSignups;

	@SerializedName("Google Business")
	private int googleBusiness;

	@SerializedName("Interior/Architect Ref")
	private int interiorArchitectRef;

	@SerializedName("Website")
	private int website;

	@SerializedName("Offline")
	private int offline;

	@SerializedName("Instagram")
	private int instagram;

	@SerializedName("Zopim Chat")
	private int zopimChat;

	@SerializedName("Friend Referral")
	private int friendReferral;

	public void setMonthName(String monthName){
		this.monthName = monthName;
	}

	public String getMonthName(){
		return monthName;
	}

	public void setWebSignups(int webSignups){
		this.webSignups = webSignups;
	}

	public int getWebSignups(){
		return webSignups;
	}

	public void setGoogleBusiness(int googleBusiness){
		this.googleBusiness = googleBusiness;
	}

	public int getGoogleBusiness(){
		return googleBusiness;
	}

	public void setInteriorArchitectRef(int interiorArchitectRef){
		this.interiorArchitectRef = interiorArchitectRef;
	}

	public int getInteriorArchitectRef(){
		return interiorArchitectRef;
	}

	public void setWebsite(int website){
		this.website = website;
	}

	public int getWebsite(){
		return website;
	}

	public void setOffline(int offline){
		this.offline = offline;
	}

	public int getOffline(){
		return offline;
	}

	public void setInstagram(int instagram){
		this.instagram = instagram;
	}

	public int getInstagram(){
		return instagram;
	}

	public void setZopimChat(int zopimChat){
		this.zopimChat = zopimChat;
	}

	public int getZopimChat(){
		return zopimChat;
	}

	public void setFriendReferral(int friendReferral){
		this.friendReferral = friendReferral;
	}

	public int getFriendReferral(){
		return friendReferral;
	}
}