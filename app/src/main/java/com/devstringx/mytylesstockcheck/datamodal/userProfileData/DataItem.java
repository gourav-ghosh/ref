package com.devstringx.mytylesstockcheck.datamodal.userProfileData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataItem implements Serializable {

	@SerializedName("user_email")
	private String userEmail;

	@SerializedName("profile_image")
	private Object profileImage;

	@SerializedName("role")
	private String role;

	@SerializedName("date_of_birth")
	private String dateOfBirth;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("date_of_joining")
	private String dateOfJoining;

	@SerializedName("phone_number")
	private long phoneNumber;

	@SerializedName("vendor_company_name")
	private Object vendorCompanyName;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("eligible_for_admin_rights")
	private int eligibleForAdminRights;

	@SerializedName("ask_payment")
	private int askPayment;

	@SerializedName("eligible_for_overtime")
	private int eligibleForOvertime;

	@SerializedName("eligible_for_work_log")
	private int eligibleForWorklog;

	@SerializedName("eligible_for_cms")
	private int eligibleForCMS;

	@SerializedName("eligible_for_crm")
	private int eligibleForCRM;

	@SerializedName("vendor_code")
	private String vendorCode;
	@SerializedName("eligible_for_order_listing")
	private int eligibleForOrderListing;

	public void setUserEmail(String userEmail){
		this.userEmail = userEmail;
	}

	public String getUserEmail(){
		return userEmail;
	}

	public void setProfileImage(Object profileImage){
		this.profileImage = profileImage;
	}

	public Object getProfileImage(){
		return profileImage;
	}

	public void setRole(String role){
		this.role = role;
	}

	public String getRole(){
		return role;
	}

	public void setDateOfBirth(String dateOfBirth){
		this.dateOfBirth = dateOfBirth;
	}

	public String getDateOfBirth(){
		return dateOfBirth;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setDateOfJoining(String dateOfJoining){
		this.dateOfJoining = dateOfJoining;
	}

	public String getDateOfJoining(){
		return dateOfJoining;
	}

	public void setPhoneNumber(long phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public long getPhoneNumber(){
		return phoneNumber;
	}

	public void setVendorCompanyName(Object vendorCompanyName){
		this.vendorCompanyName = vendorCompanyName;
	}

	public Object getVendorCompanyName(){
		return vendorCompanyName;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setAskPayment(int askPayment) {
		this.askPayment = askPayment;
	}

	public int getAskPayment() {
		return askPayment;
	}

	public void setEligibleForAdminRights(int eligibleForAdminRights) {
		this.eligibleForAdminRights = eligibleForAdminRights;
	}

	public int getEligibleForAdminRights() {
		return eligibleForAdminRights;
	}

	public void setEligibleForCMS(int eligibleForCMS) {
		this.eligibleForCMS = eligibleForCMS;
	}

	public int getEligibleForCMS() {
		return eligibleForCMS;
	}

	public void setEligibleForCRM(int eligibleForCRM) {
		this.eligibleForCRM = eligibleForCRM;
	}

	public int getEligibleForCRM() {
		return eligibleForCRM;
	}

	public void setEligibleForOvertime(int eligibleForOvertime) {
		this.eligibleForOvertime = eligibleForOvertime;
	}

	public int getEligibleForOvertime() {
		return eligibleForOvertime;
	}

	public void setEligibleForWorklog(int eligibleForWorklog) {
		this.eligibleForWorklog = eligibleForWorklog;
	}

	public int getEligibleForWorklog() {
		return eligibleForWorklog;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setEligibleForOrderListing(int eligibleForOrderListing) {
		this.eligibleForOrderListing = eligibleForOrderListing;
	}

	public int getEligibleForOrderListing() {
		return eligibleForOrderListing;
	}
}