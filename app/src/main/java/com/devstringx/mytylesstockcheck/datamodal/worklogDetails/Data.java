package com.devstringx.mytylesstockcheck.datamodal.worklogDetails;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("records")
    private List<Records> records;

    public void setRecords(List<Records> records) {
        this.records = records;
    }

    public List<Records> getRecords() {
        return records;
    }
}
