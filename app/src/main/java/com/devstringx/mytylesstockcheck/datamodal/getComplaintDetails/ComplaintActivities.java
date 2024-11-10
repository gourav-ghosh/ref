package com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails;

import com.google.gson.annotations.SerializedName;

public class ComplaintActivities{

	@SerializedName("created")
	private Created created;

	@SerializedName("resulationDateChange")
	private ResulationDateChange resulationDateChange;

	@SerializedName("customerDetailsUpdate")
	private CustomerDetailsUpdate customerDetailsUpdate;

	@SerializedName("inprogress")
	private Inprogress inprogress;

	@SerializedName("cancelled")
	private Cancelled cancelled;

	@SerializedName("resolved")
	private Resolved resolved;

	public Created getCreated(){
		return created;
	}

	public ResulationDateChange getResulationDateChange(){
		return resulationDateChange;
	}

	public CustomerDetailsUpdate getCustomerDetailsUpdate(){
		return customerDetailsUpdate;
	}

	public Inprogress getInprogress(){
		return inprogress;
	}

	public Cancelled getCancelled(){
		return cancelled;
	}

	public Resolved getResolved(){
		return resolved;
	}
}