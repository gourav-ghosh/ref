package com.devstringx.mytylesstockcheck.datamodal.deliveryModes;

import com.devstringx.mytylesstockcheck.datamodal.deliveryModes.RecordsItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("records")
    private List<RecordsItem> records;

    @SerializedName("count")
    private int count;

    public void setRecords(List<RecordsItem> records){
        this.records = records;
    }

    public List<RecordsItem> getRecords(){
        return records;
    }

    public void setCount(int count){
        this.count = count;
    }

    public int getCount(){
        return count;
    }
}
