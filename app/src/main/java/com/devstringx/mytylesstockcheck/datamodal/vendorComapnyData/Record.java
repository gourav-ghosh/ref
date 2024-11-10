package com.devstringx.mytylesstockcheck.datamodal.vendorComapnyData;

import com.google.gson.annotations.SerializedName;

public class Record {
    @SerializedName("id")
    private int id;

    @SerializedName("vendor_company_name")
    private String vendorCompanyName;

    @SerializedName("vendor_code")
    private String vendorCode;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCompanyName(String vendorCompanyName) {
        this.vendorCompanyName = vendorCompanyName;
    }

    public String getVendorCompanyName() {
        return vendorCompanyName;
    }
}
