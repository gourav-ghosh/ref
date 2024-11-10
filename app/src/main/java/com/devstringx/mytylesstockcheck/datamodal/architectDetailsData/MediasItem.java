package com.devstringx.mytylesstockcheck.datamodal.architectDetailsData;

import com.google.gson.annotations.SerializedName;

public class MediasItem{

	@SerializedName("min_path")
	private String minPath;

	@SerializedName("path")
	private String path;

	@SerializedName("thumbnail_path")
	private String thumbnailPath;

	@SerializedName("updated_at")
	private Object updatedAt;

	@SerializedName("media_type")
	private String mediaType;

	@SerializedName("name")
	private String name;

	@SerializedName("architect_id")
	private int architectId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("deleted_at")
	private Object deletedAt;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	private boolean selected;

	public void setMinPath(String minPath){
		this.minPath = minPath;
	}

	public String getMinPath(){
		return minPath;
	}

	public void setPath(String path){
		this.path = path;
	}

	public String getPath(){
		return path;
	}

	public void setThumbnailPath(String thumbnailPath){
		this.thumbnailPath = thumbnailPath;
	}

	public String getThumbnailPath(){
		return thumbnailPath;
	}

	public void setUpdatedAt(Object updatedAt){
		this.updatedAt = updatedAt;
	}

	public Object getUpdatedAt(){
		return updatedAt;
	}

	public void setMediaType(String mediaType){
		this.mediaType = mediaType;
	}

	public String getMediaType(){
		return mediaType;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setArchitectId(int architectId){
		this.architectId = architectId;
	}

	public int getArchitectId(){
		return architectId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setDeletedAt(Object deletedAt){
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt(){
		return deletedAt;
	}
}