package com.devstringx.mytylesstockcheck.datamodal.allOwners;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecordsItem implements Serializable {

	@SerializedName("user_status")
	private String userStatus;

	@SerializedName("user_email")
	private String userEmail;

	@SerializedName("role")
	private int role;

	@SerializedName("date_of_birth")
	private String dateOfBirth;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("vendor_company_name")
	private String vendorCompanyName;
	@SerializedName("vendor_company_admin_id")
	private String vendor_company_admin_id;
	@SerializedName("branch")
	private String branch;

	@SerializedName("role_name")
	private String roleName;

	@SerializedName("profile_image")
	private String profileImage;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("date_of_joining")
	private String dateOfJoining;

	@SerializedName("phone_number")
	private String phoneNumber;

	@SerializedName("id")
	private int id;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("updated_date")
	private String updatedDate;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("ask_payment")
	private String askPayment;

	@SerializedName("vendor_code")
	private String vendorCode;

	@SerializedName("allowed_devices")
	private String allowedDevices;
	@SerializedName("eligible_for_admin_rights")
	private String eligibleForAdminRights = "";
	@SerializedName("eligible_for_overtime")
	private String eligibleForOvertime = "";
	@SerializedName("eligible_for_work_log")
	private String eligibleForWorklog = "";
	@SerializedName("eligible_for_cms")
	private String eligibleForCMS = "";
	@SerializedName("eligible_for_crm")
	private String eligibleForCRM = "";
	@SerializedName("eligible_for_order_listing")
	private String eligibleForOrderListing = "";

	public void setUserStatus(String userStatus){
		this.userStatus = userStatus;
	}

	public String getUserStatus(){
		return userStatus;
	}

	public void setUserEmail(String userEmail){
		this.userEmail = userEmail;
	}

	public String getUserEmail(){
		return userEmail;
	}

	public void setRole(int role){
		this.role = role;
	}

	public int getRole(){
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

	public void setVendorCompanyName(String vendorCompanyName){
		this.vendorCompanyName = vendorCompanyName;
	}

	public String getVendorCompanyName(){
		return vendorCompanyName;
	}

	public void setBranch(String branch){
		this.branch = branch;
	}

	public String getBranch(){
		return branch;
	}

	public void setRoleName(String roleName){
		this.roleName = roleName;
	}

	public String getRoleName(){
		return roleName;
	}

	public void setProfileImage(String profileImage){
		this.profileImage = profileImage;
	}

	public String getProfileImage(){
		return profileImage;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setDateOfJoining(String dateOfJoining){
		this.dateOfJoining = dateOfJoining;
	}

	public String getDateOfJoining(){
		return dateOfJoining;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public void setUpdatedDate(String updatedDate){
		this.updatedDate = updatedDate;
	}

	public String getUpdatedDate(){
		return updatedDate;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}
	public String getVendor_company_admin_id() {
		return vendor_company_admin_id;
	}

	public void setVendor_company_admin_id(String vendor_company_admin_id) {
		this.vendor_company_admin_id = vendor_company_admin_id;
	}

	public void setAskPayment(String askPayment) {
		this.askPayment = askPayment;
	}

	public String getAskPayment() {
		return askPayment;
	}

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getVendorCode() {
        return vendorCode;
    }

	public void setAllowedDevices(String allowedDevices) {
		this.allowedDevices = allowedDevices;
	}

	public String getAllowedDevices() {
		return allowedDevices;
	}

	public void setEligibleForWorklog(String eligibleForWorklog) {
		this.eligibleForWorklog = eligibleForWorklog;
	}

	public String getEligibleForWorklog() {
		return eligibleForWorklog;
	}

	public void setEligibleForAdminRights(String eligibleForAdminRights) {
		this.eligibleForAdminRights = eligibleForAdminRights;
	}

	public String getEligibleForAdminRights() {
		return eligibleForAdminRights;
	}

	public void setEligibleForOvertime(String eligibleForOvertime) {
		this.eligibleForOvertime = eligibleForOvertime;
	}

	public String getEligibleForOvertime() {
		return eligibleForOvertime;
	}

	public void setEligibleForCRM(String eligibleForCRM) {
		this.eligibleForCRM = eligibleForCRM;
	}

	public String getEligibleForCRM() {
		return eligibleForCRM;
	}

	public void setEligibleForCMS(String eligibleForCMS) {
		this.eligibleForCMS = eligibleForCMS;
	}

	public String getEligibleForCMS() {
		return eligibleForCMS;
	}

	public void setEligibleForOrderListing(String eligibleForOrderListing) {
		this.eligibleForOrderListing = eligibleForOrderListing;
	}

	public String getEligibleForOrderListing() {
		return eligibleForOrderListing;
	}
}