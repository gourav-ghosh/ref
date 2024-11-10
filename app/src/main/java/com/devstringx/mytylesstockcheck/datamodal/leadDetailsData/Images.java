package com.devstringx.mytylesstockcheck.datamodal.leadDetailsData;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Images implements Serializable {

	@SerializedName("feature_image")
	private boolean featureImage;

	@SerializedName("image_key")
	private List<String> imageKey;

	@SerializedName("lead_attachment")
	private List<String> leadAttachment;

	@SerializedName("id")
	private int id=0;

	@SerializedName("lead_id")
	private int leadId;

	public void setFeatureImage(boolean featureImage){
		this.featureImage = featureImage;
	}

	public boolean isFeatureImage(){
		return featureImage;
	}

	public void setImageKey(List<String> imageKey){
		this.imageKey = imageKey;
	}

	public List<String> getImageKey(){
		return imageKey;
	}

	public void setLeadAttachment(List<String> leadAttachment){
		this.leadAttachment = leadAttachment;
	}

	public List<String> getLeadAttachment(){
		return leadAttachment;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setLeadId(int leadId){
		this.leadId = leadId;
	}

	public int getLeadId(){
		return leadId;
	}
}