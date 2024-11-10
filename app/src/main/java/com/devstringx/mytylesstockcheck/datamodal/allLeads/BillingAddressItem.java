package com.devstringx.mytylesstockcheck.datamodal.allLeads;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BillingAddressItem implements Serializable {
    @SerializedName("billing_address_id")
    public String billing_address_id;
    @SerializedName("pincode")
    public String billing_pincode;
    @SerializedName("site_in_charge_mobile_number")
    public String site_in_charge_mobile_number;
    @SerializedName("address_line_1")
    public String address_line_1;
    @SerializedName("address_line_2")
    public String address_line_2;
    @SerializedName("landmark")
    public String landmark;
    @SerializedName("state")
    public String billing_state_id;
    @SerializedName("state_name")
    public String billing_state;
    @SerializedName("city")
    public String billing_city_id;
    @SerializedName("city_name")
    public String billing_city;
    @SerializedName("gst_number")
    public String gst_number;

    public String getGst_number() {
        return gst_number;
    }

    public void setGst_number(String gst_number) {
        this.gst_number = gst_number;
    }

    public String getSite_in_charge_mobile_number() {
        return site_in_charge_mobile_number;
    }

    public void setSite_in_charge_mobile_number(String site_in_charge_mobile_number) {
        this.site_in_charge_mobile_number = site_in_charge_mobile_number;
    }

    public String getBilling_address_id() {
        return billing_address_id;
    }

    public void setBilling_address_id(String billing_address_id) {
        this.billing_address_id = billing_address_id;
    }

    public String getBilling_pincode() {
        return billing_pincode;
    }

    public void setBilling_pincode(String billing_pincode) {
        this.billing_pincode = billing_pincode;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getBilling_state_id() {
        return billing_state_id;
    }

    public void setBilling_state_id(String billing_state_id) {
        this.billing_state_id = billing_state_id;
    }

    public String getBilling_state() {
        return billing_state;
    }

    public void setBilling_state(String billing_state) {
        this.billing_state = billing_state;
    }

    public String getBilling_city_id() {
        return billing_city_id;
    }

    public void setBilling_city_id(String billing_city_id) {
        this.billing_city_id = billing_city_id;
    }

    public String getBilling_city() {
        return billing_city;
    }

    public void setBilling_city(String billing_city) {
        this.billing_city = billing_city;
    }
}