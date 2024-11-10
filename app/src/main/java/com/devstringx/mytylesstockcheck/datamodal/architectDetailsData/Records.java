package com.devstringx.mytylesstockcheck.datamodal.architectDetailsData;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Records{

	@SerializedName("country")
	private String country;

	@SerializedName("city")
	private String city;

	@SerializedName("established_year")
	private Object establishedYear;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("deleted_by")
	private Object deletedBy;

	@SerializedName("primary_phone")
	private String primaryPhone;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("secondary_phone")
	private String secondaryPhone;

	@SerializedName("sales_executive")
	private String salesExecutive;

	@SerializedName("id")
	private int id;

	@SerializedName("state")
	private String state;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("email")
	private String email;

	@SerializedName("pincode")
	private String pincode;

	@SerializedName("address")
	private String address;

	@SerializedName("total_order_amount")
	private Object totalOrderAmount;

	@SerializedName("architect_id")
	private String architectId;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("brand_name")
	private Object brandName;

	@SerializedName("created_by")
	private int createdBy;

	@SerializedName("deleted_at")
	private Object deletedAt;

	@SerializedName("medias")
	private List<MediasItem> medias;

	@SerializedName("company_name")
	private String companyName;

	@SerializedName("updated_by")
	private Object updatedBy;

	@SerializedName("total_order_count")
	private int totalOrderCount;

	@SerializedName("gst_number")
	private Object gstNumber;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setEstablishedYear(Object establishedYear){
		this.establishedYear = establishedYear;
	}

	public Object getEstablishedYear(){
		return establishedYear;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setDeletedBy(Object deletedBy){
		this.deletedBy = deletedBy;
	}

	public Object getDeletedBy(){
		return deletedBy;
	}

	public void setPrimaryPhone(String primaryPhone){
		this.primaryPhone = primaryPhone;
	}

	public String getPrimaryPhone(){
		return primaryPhone;
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

	public void setSalesExecutive(String salesExecutive){
		this.salesExecutive = salesExecutive;
	}

	public String getSalesExecutive(){
		return salesExecutive;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setPincode(String pincode){
		this.pincode = pincode;
	}

	public String getPincode(){
		return pincode;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setTotalOrderAmount(Object totalOrderAmount){
		this.totalOrderAmount = totalOrderAmount;
	}

	public Object getTotalOrderAmount(){
		return totalOrderAmount;
	}

	public void setArchitectId(String architectId){
		this.architectId = architectId;
	}

	public String getArchitectId(){
		return architectId;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setBrandName(Object brandName){
		this.brandName = brandName;
	}

	public Object getBrandName(){
		return brandName;
	}

	public void setCreatedBy(int createdBy){
		this.createdBy = createdBy;
	}

	public int getCreatedBy(){
		return createdBy;
	}

	public void setDeletedAt(Object deletedAt){
		this.deletedAt = deletedAt;
	}

	public Object getDeletedAt(){
		return deletedAt;
	}

	public void setMedias(List<MediasItem> medias){
		this.medias = medias;
	}

	public List<MediasItem> getMedias(){
		return medias;
	}

	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}

	public String getCompanyName(){
		return companyName;
	}

	public void setUpdatedBy(Object updatedBy){
		this.updatedBy = updatedBy;
	}

	public Object getUpdatedBy(){
		return updatedBy;
	}

	public void setTotalOrderCount(int totalOrderCount){
		this.totalOrderCount = totalOrderCount;
	}

	public int getTotalOrderCount(){
		return totalOrderCount;
	}

	public void setGstNumber(Object gstNumber){
		this.gstNumber = gstNumber;
	}

	public Object getGstNumber(){
		return gstNumber;
	}
}