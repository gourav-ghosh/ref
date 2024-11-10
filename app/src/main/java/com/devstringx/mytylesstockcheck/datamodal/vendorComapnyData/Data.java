package com.devstringx.mytylesstockcheck.datamodal.vendorComapnyData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("count")
    private int count;

    @SerializedName("records")
    public List<Record> record;

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setRecord(List<Record> record) {
        this.record = record;
    }

    public List<Record> getRecord() {
        return record;
    }
}
