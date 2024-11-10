package com.devstringx.mytylesstockcheck.datamodal.leadDetailsData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShippingAddresses implements Serializable {

	@SerializedName("as_default")
	private boolean asDefault;
	@SerializedName("id")
	private String id="";
	@SerializedName("shipping_address_id")
	private int shippingAddressId;

	@SerializedName("shipping_state")
	private String shippingState;

	@SerializedName("shipping_pincode")
	private String shippingPincode;

	@SerializedName("shipping_state_id")
	private int shippingStateId;

	@SerializedName("shipping_city")
	private String shippingCity;

	@SerializedName("address_line_1")
	private String addressLine1;

	@SerializedName("address_line_2")
	private String addressLine2;

	@SerializedName("gst_number")
	private String gstNumber;

	@SerializedName("site_in_charge_mobile_number")
	private String siteInChargeMobileNumber;

	@SerializedName("landmark")
	private String landmark;

	@SerializedName("shipping_city_id")
	private int shippingCityId;

	@SerializedName("state_id")
	private String state;

	@SerializedName("city_id")
	private String city;
	@SerializedName("pincode")
	private String pincode;

	public void setAsDefault(boolean asDefault){
		this.asDefault = asDefault;
	}

	public boolean isAsDefault(){
		return asDefault;
	}

	public void setShippingAddressId(int shippingAddressId){
		this.shippingAddressId = shippingAddressId;
	}

	public int getShippingAddressId(){
		return shippingAddressId;
	}

	public void setShippingState(String shippingState){
		this.shippingState = shippingState;
	}

	public String getShippingState(){
		return shippingState;
	}

	public void setShippingPincode(String shippingPincode){
		this.shippingPincode = shippingPincode;
	}

	public String getShippingPincode(){
		return shippingPincode;
	}

	public void setShippingStateId(int shippingStateId){
		this.shippingStateId = shippingStateId;
	}

	public int getShippingStateId(){
		return shippingStateId;
	}

	public void setShippingCity(String shippingCity){
		this.shippingCity = shippingCity;
	}

	public String getShippingCity(){
		return shippingCity;
	}

	public void setAddressLine1(String addressLine1){
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine1(){
		return addressLine1;
	}

	public void setAddressLine2(String addressLine2){
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine2(){
		return addressLine2;
	}

	public void setGstNumber(String gstNumber){
		this.gstNumber = gstNumber;
	}

	public String getGstNumber(){
		return gstNumber;
	}

	public void setSiteInChargeMobileNumber(String siteInChargeMobileNumber){
		this.siteInChargeMobileNumber = siteInChargeMobileNumber;
	}

	public String getSiteInChargeMobileNumber(){
		return siteInChargeMobileNumber;
	}

	public void setLandmark(String landmark){
		this.landmark = landmark;
	}

	public String getLandmark(){
		return landmark;
	}

	public void setShippingCityId(int shippingCityId){
		this.shippingCityId = shippingCityId;
	}

	public int getShippingCityId(){
		return shippingCityId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
}