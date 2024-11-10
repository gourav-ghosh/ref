package com.devstringx.mytylesstockcheck.datamodal.getOrderComment;

import com.google.gson.annotations.SerializedName;

public class OrderCommentResponse{

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public Data getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public int getStatus(){
		return status;
	}
}