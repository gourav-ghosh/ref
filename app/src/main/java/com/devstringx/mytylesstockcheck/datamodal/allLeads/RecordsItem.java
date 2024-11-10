package com.devstringx.mytylesstockcheck.datamodal.allLeads;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RecordsItem{

	@SerializedName("lead_email")
	private String leadEmail;

	@SerializedName("requirements")
	private List<RequirementsItem> requirements;

	@SerializedName("latest_activity")
	private List<LatestActivityItem> latestActivity;

	@SerializedName("comments")
	private List<Object> comments;

	@SerializedName("lead_notes")
	private String leadNotes;

	@SerializedName("lead_source")
	private String leadSource;

	@SerializedName("activity_date")
	private String activityDate;

	@SerializedName("billing_address")
	private BillingAddressItem billingAddress;

	@SerializedName("lead_r_id")
	private String leadRId;

	public void setStar_marked(boolean starMarked) {
		isStarMarked = starMarked;
	}

	@SerializedName("is_star_marked")
	private boolean isStarMarked;

	@SerializedName("lead_owner_id")
	private int leadOwnerId;

	@SerializedName("lead_created_date")
	private String leadCreatedDate;

	@SerializedName("full_name")
	private String fullName;

	@SerializedName("lead_owner_name")
	private String leadOwnerName;

	@SerializedName("activity_notes")
	private String activityNotes;

	@SerializedName("lead_secondary_phone")
	private String leadSecondaryPhone;

	@SerializedName("activity_type")
	private String activityType;

	@SerializedName("lead_stage")
	private String leadStage;

	@SerializedName("lead_primary_phone")
	private String leadPrimaryPhone;

	@SerializedName("lead_type")
	private String leadType;

	@SerializedName("lead_id")
	private int leadId;

	@SerializedName("ticket_size")
	private String ticketSize;

	public String getTicketSize() {
		return ticketSize;
	}

	public void setTicketSize(String ticketSize) {
		this.ticketSize = ticketSize;
	}

	public String getLeadEmail(){
		return leadEmail;
	}

	public List<RequirementsItem> getRequirements(){
		return requirements;
	}

	public List<LatestActivityItem> getLatestActivity(){
		return latestActivity;
	}

	public List<Object> getComments(){
		return comments;
	}

	public String getLeadNotes(){
		return leadNotes;
	}

	public String getLeadSource(){
		return leadSource;
	}

	public String getActivityDate(){
		return activityDate;
	}

	public BillingAddressItem getBillingAddress(){
		return billingAddress;
	}

	public String getLeadRId(){
		return leadRId;
	}

	public boolean getStar_marked(){
		return isStarMarked;
	}

	public int getLeadOwnerId(){
		return leadOwnerId;
	}

	public String getLeadCreatedDate(){
		return leadCreatedDate;
	}

	public String getFullName(){
		return fullName;
	}

	public String getLeadOwnerName(){
		return leadOwnerName;
	}

	public String getActivityNotes(){
		return activityNotes;
	}

	public String getLeadSecondaryPhone(){
		return leadSecondaryPhone;
	}

	public String getActivityType(){
		return activityType;
	}

	public String getLeadStage(){
		return leadStage;
	}

	public String  getLeadPrimaryPhone(){
		return leadPrimaryPhone;
	}

	public String getLeadType(){
		return leadType;
	}

	public int getLeadId(){
		return leadId;
	}
}