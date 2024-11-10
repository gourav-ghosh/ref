package com.devstringx.mytylesstockcheck.datamodal.leadDetailsData;

import java.io.Serializable;
import java.util.List;

import com.devstringx.mytylesstockcheck.datamodal.allLeads.Activity;
import com.devstringx.mytylesstockcheck.datamodal.allLeads.BillingAddressItem;
import com.google.gson.annotations.SerializedName;

public class Record implements Serializable {

	@SerializedName("notes")
	private String notes;

	@SerializedName("lead_source")
	private String leadSource;
	@SerializedName("shipping_addresses")
	private List<ShippingAddresses> shippingAddresses;

	@SerializedName("lead_r_id")
	private String leadRId;

	@SerializedName("primary_phone")
	private String primaryPhone;

	@SerializedName("lead_created_date")
	private String leadCreatedDate;

	@SerializedName("lead_owner_id")
	private int leadOwnerId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("secondary_phone")
	private String secondaryPhone;


	@SerializedName("lead_id")
	private int leadId;

	@SerializedName("email")
	private String email;

	@SerializedName("state_id")
	private int stateId;

	@SerializedName("owner")
	private int owner;


	@SerializedName("requirements")
	private List<Requirenments> requirements;

	@SerializedName("comments")
	private List<CommentsItem> comments;


	@SerializedName("full_name")
	private String fullName;

	@SerializedName("lead_stage")
	private String leadStage;


	@SerializedName("lead_type")
	private String leadType;

	@SerializedName("lead_owner")
	private String leadOwner;
	@SerializedName("ticket_size")
	private String ticketSize;

	public String getTicketSize() {
		return ticketSize;
	}

	public void setTicketSize(String ticketSize) {
		this.ticketSize = ticketSize;
	}

	@SerializedName("lead_owner_name")
	private String leadOwnerName;

	@SerializedName("billing_address")
	private BillingAddressItem billing_address;
	@SerializedName("images")
	private List<Images> images;

	public BillingAddressItem getBillingAddress() {
		return billing_address;
	}

	public void setBilling_address(BillingAddressItem billing_address) {
		this.billing_address = billing_address;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@SerializedName("activity")
	private Activity activity;

	public String getLeadOwnerName() {
		return leadOwnerName;
	}

	public void setLeadOwnerName(String leadOwnerName) {
		this.leadOwnerName = leadOwnerName;
	}

	public void setNotes(String notes){
		this.notes = notes;
	}

	public String getNotes(){
		return notes;
	}

	public void setLeadSource(String leadSource){
		this.leadSource = leadSource;
	}

	public String getLeadSource(){
		return leadSource;
	}

	public void setShippingAddresses(List<ShippingAddresses> shippingAddresses){
		this.shippingAddresses = shippingAddresses;
	}

	public List<ShippingAddresses> getShippingAddresses(){
		return shippingAddresses;
	}

	public void setLeadRId(String leadRId){
		this.leadRId = leadRId;
	}

	public String getLeadRId(){
		return leadRId;
	}

	public void setPrimaryPhone(String primaryPhone){
		this.primaryPhone = primaryPhone;
	}

	public String getPrimaryPhone(){
		return primaryPhone;
	}

	public void setLeadCreatedDate(String leadCreatedDate){
		this.leadCreatedDate = leadCreatedDate;
	}

	public String getLeadCreatedDate(){
		return leadCreatedDate;
	}

	public void setLeadOwnerId(int leadOwnerId){
		this.leadOwnerId = leadOwnerId;
	}

	public int getLeadOwnerId(){
		return leadOwnerId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setSecondaryPhone(String secondaryPhone){
		this.secondaryPhone = secondaryPhone;
	}

	public String getSecondaryPhone(){
		return secondaryPhone;
	}

	public void setLeadId(int leadId){
		this.leadId = leadId;
	}

	public int getLeadId(){
		return leadId;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setStateId(int stateId){
		this.stateId = stateId;
	}

	public int getStateId(){
		return stateId;
	}

	public void setOwner(int owner){
		this.owner = owner;
	}

	public int getOwner(){
		return owner;
	}

	public void setRequirements(List<Requirenments> requirements){
		this.requirements = requirements;
	}

	public List<Requirenments> getRequirements(){
		return requirements;
	}

	public void setComments(List<CommentsItem> comments){
		this.comments = comments;
	}

	public List<CommentsItem> getComments(){
		return comments;
	}
	public void setFullName(String fullName){
		this.fullName = fullName;
	}

	public String getFullName(){
		return fullName;
	}

	public void setLeadStage(String leadStage){
		this.leadStage = leadStage;
	}

	public String getLeadStage(){
		return leadStage;
	}

	public void setLeadType(String leadType){
		this.leadType = leadType;
	}

	public String getLeadType(){
		return leadType;
	}

	public void setLeadOwner(String leadOwner){
		this.leadOwner = leadOwner;
	}

	public String getLeadOwner(){
		return leadOwner;
	}

	public List<Images> getImages() {
		return images;
	}

	public void setImages(List<Images> images) {
		this.images = images;
	}
}