package com.devstringx.mytylesstockcheck.datamodal.getAllComplaintComments;

import com.google.gson.annotations.SerializedName;

public class RecordsItem{

	@SerializedName("complaint_id")
	private int complaintId;

	@SerializedName("profile_image")
	private Object profileImage;

	@SerializedName("created_by_last_name")
	private String createdByLastName;

	@SerializedName("comment")
	private String comment;

	@SerializedName("created_by_id")
	private int createdById;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("created_by_name")
	private String createdByName;

	@SerializedName("created_by_phone_number")
	private long createdByPhoneNumber;

	public int getComplaintId(){
		return complaintId;
	}

	public Object getProfileImage(){
		return profileImage;
	}

	public String getCreatedByLastName(){
		return createdByLastName;
	}

	public String getComment(){
		return comment;
	}

	public int getCreatedById(){
		return createdById;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public String getCreatedByName(){
		return createdByName;
	}

	public long getCreatedByPhoneNumber(){
		return createdByPhoneNumber;
	}
}