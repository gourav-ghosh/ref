package com.devstringx.mytylesstockcheck.datamodal.deliveryModes;

import com.google.gson.annotations.SerializedName;

public class RecordsItem {
    @SerializedName("value")
    private String value;

    @SerializedName("label")
    private String label;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
