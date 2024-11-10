package com.devstringx.mytylesstockcheck.datamodal.allWorklogs;
import java.util.List;
import com.google.gson.annotations.SerializedName;
public class Data {
    @SerializedName("search")
    private Object search;

    @SerializedName("sorting")
    private String sorting;

    @SerializedName("count")
    private int count;

    @SerializedName("dateFilter")
    private DateFilter dateFilter;

    @SerializedName("tabCounts")
    private TabCounts tabCounts;

    @SerializedName("records")
    private List<RecordsItem> records;

    @SerializedName("userId")
    private Object userId;

    public void setSearch(Object search) {
        this.search = search;
    }

    public Object getSearch() {
        return search;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setDateFilter(DateFilter dateFilter) {
        this.dateFilter = dateFilter;
    }

    public DateFilter getDateFilter() {
        return dateFilter;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }

    public String getSorting() {
        return sorting;
    }

    public void setRecords(List<RecordsItem> records) {
        this.records = records;
    }

    public List<RecordsItem> getRecords() {
        return records;
    }

    public void setTabCounts(TabCounts tabCounts) {
        this.tabCounts = tabCounts;
    }

    public TabCounts getTabCounts() {
        return tabCounts;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getUserId() {
        return userId;
    }
}
