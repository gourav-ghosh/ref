package com.devstringx.mytylesstockcheck.datamodal.shippingChargeData;

import com.google.gson.annotations.SerializedName;

public class ShippingChargeRecords {
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @SerializedName("city_name")
    private String cityName;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected=false;
}
