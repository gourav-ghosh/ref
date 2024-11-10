package com.devstringx.mytylesstockcheck.datamodal.leadDetailsData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CommentsItem implements Serializable {

	@SerializedName("comment")
	private String comment;

	@SerializedName("id")
	private int id;

	@SerializedName("comment_created_at")
	private String commentCreatedAt;

	public void setComment(String comment){
		this.comment = comment;
	}

	public String getComment(){
		return comment;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setCommentCreatedAt(String commentCreatedAt){
		this.commentCreatedAt = commentCreatedAt;
	}

	public String getCommentCreatedAt(){
		return commentCreatedAt;
	}
}