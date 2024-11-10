package com.devstringx.mytylesstockcheck.datamodal.getOrderComment;

import com.google.gson.annotations.SerializedName;

public class OrderRecordsItem {

	@SerializedName("commented_by_name")
	private String commentedByName;

	@SerializedName("commented_by_last_name")
	private String commentedByLastName;

	@SerializedName("profile_image")
	private Object profileImage;

	@SerializedName("commenter_role")
	private String commenterRole;

	@SerializedName("comment")
	private String comment;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("commented_by_phone_number")
	private long commentedByPhoneNumber;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("commented_by_id")
	private int commentedById;

	public String getCommentedByName(){
		return commentedByName;
	}

	public String getCommentedByLastName(){
		return commentedByLastName;
	}

	public Object getProfileImage(){
		return profileImage;
	}

	public String getCommenterRole(){
		return commenterRole;
	}

	public String getComment(){
		return comment;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public long getCommentedByPhoneNumber(){
		return commentedByPhoneNumber;
	}

	public int getOrderId(){
		return orderId;
	}

	public int getCommentedById(){
		return commentedById;
	}
}