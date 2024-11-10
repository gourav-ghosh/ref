package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

public class PaymentProofItem{

	@SerializedName("min_path")
	private String minPath;

	@SerializedName("commented_by")
	private String commentedBy;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("deleted_by")
	private String deletedBy;

	@SerializedName("created_by")
	private String createdBy;

	@SerializedName("deleted_at")
	private String deletedAt;

	@SerializedName("path")
	private String path;

	@SerializedName("thumbnail_path")
	private String thumbnailPath;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("media_type")
	private String mediaType;

	@SerializedName("thumbnail_name")
	private String thumbnailName;

	@SerializedName("name")
	private String name;

	@SerializedName("for_status")
	private String forStatus;

	@SerializedName("comment")
	private String comment;

	@SerializedName("id")
	private String id;

	@SerializedName("order_id")
	private String orderId;

	public String getMinPath(){
		return minPath;
	}

	public String getCommentedBy(){
		return commentedBy;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getDeletedBy(){
		return deletedBy;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	public String getPath(){
		return path;
	}

	public String getThumbnailPath(){
		return thumbnailPath;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getMediaType(){
		return mediaType;
	}

	public String getThumbnailName(){
		return thumbnailName;
	}

	public String getName(){
		return name;
	}

	public String getForStatus(){
		return forStatus;
	}

	public String getComment(){
		return comment;
	}

	public String getId(){
		return id;
	}

	public String getOrderId(){
		return orderId;
	}
}