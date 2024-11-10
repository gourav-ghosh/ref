package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

public class CommentsItem{

	@SerializedName("role_name")
	private String roleName;

	@SerializedName("commented_by")
	private String commentedBy;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("comment")
	private String comment;

	@SerializedName("commented_for")
	private Object commentedFor;

	public String getRoleName(){
		return roleName;
	}

	public String getCommentedBy(){
		return commentedBy;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getComment(){
		return comment;
	}

	public Object getCommentedFor(){
		return commentedFor;
	}
}