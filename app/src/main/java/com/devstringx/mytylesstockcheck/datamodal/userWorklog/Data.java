package com.devstringx.mytylesstockcheck.datamodal.userWorklog;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("search")
    private Object search;

    @SerializedName("sorting")
    private String sorting;

    @SerializedName("userId")
    private  int userId;

    @SerializedName("userDetails")
    private UserDetails userDetails;

    @SerializedName("dateFilter")
    private DateFilter dateFilter;

    @SerializedName("count")
    private int count;

    @SerializedName("records")
    private List<RecordsItem> records;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setRecords(List<RecordsItem> records) {
        this.records = records;
    }

    public List<RecordsItem> getRecords() {
        return records;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }

    public String getSorting() {
        return sorting;
    }

    public void setSearch(Object search) {
        this.search = search;
    }

    public Object getSearch() {
        return search;
    }

    public void setDateFilter(DateFilter dateFilter) {
        this.dateFilter = dateFilter;
    }

    public DateFilter getDateFilter() {
        return dateFilter;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }
}
