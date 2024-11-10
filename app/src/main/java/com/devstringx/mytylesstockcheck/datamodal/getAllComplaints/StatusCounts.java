package com.devstringx.mytylesstockcheck.datamodal.getAllComplaints;

import com.google.gson.annotations.SerializedName;

public class StatusCounts{

	@SerializedName("All")
	private int all;

	@SerializedName("Delayed")
	private int delayed;

	@SerializedName("Resolved")
	private int resolved;

	@SerializedName("Cancelled")
	private int cancelled;

	@SerializedName("Created")
	private int created;

	@SerializedName("Inprogress")
	private int inprogress;

	public int getAll(){
		return all;
	}

	public int getDelayed(){
		return delayed;
	}

	public int getResolved(){
		return resolved;
	}

	public int getCancelled(){
		return cancelled;
	}

	public int getCreated(){
		return created;
	}

	public int getInprogress(){
		return inprogress;
	}
}