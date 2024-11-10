package com.devstringx.mytylesstockcheck.screens.analytics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataAnalyticsResponse {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("sorting")
        @Expose
        private String sorting;
        @SerializedName("role")
        @Expose
        private String role;
        @SerializedName("dateFilter")
        @Expose
        private DateFilter dateFilter;
        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("records")
        @Expose
        private List<Record> records;

        public String getSorting() {
            return sorting;
        }

        public void setSorting(String sorting) {
            this.sorting = sorting;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public DateFilter getDateFilter() {
            return dateFilter;
        }

        public void setDateFilter(DateFilter dateFilter) {
            this.dateFilter = dateFilter;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<Record> getRecords() {
            return records;
        }

        public void setRecords(List<Record> records) {
            this.records = records;
        }

    }

    public class DateFilter {

        @SerializedName("from_date")
        @Expose
        private String fromDate;
        @SerializedName("to_date")
        @Expose
        private String toDate;
        @SerializedName("pass_from_date")
        @Expose
        private String passFromDate;
        @SerializedName("pass_to_date")
        @Expose
        private String passToDate;
        @SerializedName("pass_date_range")
        @Expose
        private String passDateRange;

        public String getFromDate() {
            return fromDate;
        }

        public void setFromDate(String fromDate) {
            this.fromDate = fromDate;
        }

        public String getToDate() {
            return toDate;
        }

        public void setToDate(String toDate) {
            this.toDate = toDate;
        }

        public String getPassFromDate() {
            return passFromDate;
        }

        public void setPassFromDate(String passFromDate) {
            this.passFromDate = passFromDate;
        }

        public String getPassToDate() {
            return passToDate;
        }

        public void setPassToDate(String passToDate) {
            this.passToDate = passToDate;
        }

        public String getPassDateRange() {
            return passDateRange;
        }

        public void setPassDateRange(String passDateRange) {
            this.passDateRange = passDateRange;
        }

    }


    public class Record {

        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("totalOrderAmount")
        @Expose
        private String totalOrderAmount;
        @SerializedName("averageAmount")
        @Expose
        private String averageAmount;
        @SerializedName("order_count")
        @Expose
        private int orderCount;
        @SerializedName("completion_rate")
        @Expose
        private String completionRate;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getTotalOrderAmount() {
            return totalOrderAmount;
        }

        public void setTotalOrderAmount(String totalOrderAmount) {
            this.totalOrderAmount = totalOrderAmount;
        }

        public String getAverageAmount() {
            return averageAmount;
        }

        public void setAverageAmount(String averageAmount) {
            this.averageAmount = averageAmount;
        }

        public int getOrderCount() {
            return orderCount;
        }

        public void setOrderCount(int orderCount) {
            this.orderCount = orderCount;
        }

        public String getCompletionRate() {
            return completionRate;
        }

        public void setCompletionRate(String completionRate) {
            this.completionRate = completionRate;
        }

    }
}
