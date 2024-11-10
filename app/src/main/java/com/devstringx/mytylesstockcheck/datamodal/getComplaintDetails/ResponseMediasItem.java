package com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails;

import com.google.gson.annotations.SerializedName;

public class ResponseMediasItem{

	@SerializedName("min_path")
	private String minPath;

	@SerializedName("path")
	private String path;

	@SerializedName("complaint_id")
	private String complaintId;

	@SerializedName("thumbnail_path")
	private String thumbnailPath;

	@SerializedName("media_type")
	private String mediaType;

	@SerializedName("response_id")
	private String responseId;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("deleted_by")
	private String deletedBy;

	@SerializedName("id")
	private String id;

	@SerializedName("deleted_at")
	private String deletedAt;
	private boolean selected;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getMinPath(){
		return minPath;
	}

	public String getPath(){
		return path;
	}

	public String getComplaintId(){
		return complaintId;
	}

	public String getThumbnailPath(){
		return thumbnailPath;
	}

	public String getMediaType(){
		return mediaType;
	}

	public String getResponseId(){
		return responseId;
	}

	public String getName(){
		return name;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getDeletedBy(){
		return deletedBy;
	}

	public String getId(){
		return id;
	}

	public String getDeletedAt(){
		return deletedAt;
	}
}