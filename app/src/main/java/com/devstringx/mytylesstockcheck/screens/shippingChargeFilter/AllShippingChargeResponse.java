package com.devstringx.mytylesstockcheck.screens.shippingChargeFilter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class AllShippingChargeResponse {

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

        @SerializedName("search")
        @Expose
        private String search;
        @SerializedName("sorting")
        @Expose
        private String sorting;
        @SerializedName("shippingChargeStatus")
        @Expose
        private List<String> shippingChargeStatus;
        @SerializedName("deliveryAgent")
        @Expose
        private List<String> deliveryAgent;
        @SerializedName("statusTab")
        @Expose
        private String statusTab;
        @SerializedName("dateFilter")
        @Expose
        private DateFilter dateFilter;
        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("records")
        @Expose
        private List<Record> records;
        private boolean selected;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public String getSearch() {
            return search;
        }

        public void setSearch(String search) {
            this.search = search;
        }

        public String getSorting() {
            return sorting;
        }

        public void setSorting(String sorting) {
            this.sorting = sorting;
        }

        public List<String> getShippingChargeStatus() {
            return shippingChargeStatus;
        }

        public void setShippingChargeStatus(List<String> shippingChargeStatus) {
            this.shippingChargeStatus = shippingChargeStatus;
        }

        public List<String> getDeliveryAgent() {
            return deliveryAgent;
        }

        public void setDeliveryAgent(List<String> deliveryAgent) {
            this.deliveryAgent = deliveryAgent;
        }

        public String getStatusTab() {
            return statusTab;
        }

        public void setStatusTab(String statusTab) {
            this.statusTab = statusTab;
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

        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("distance")
        @Expose
        private String distance;
        @SerializedName("loading_points")
        @Expose
        private String loadingPoints;
        @SerializedName("order_id")
        @Expose
        private int orderId;
        @SerializedName("sale_order_no")
        @Expose
        private String saleOrderNo;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("request_raised_time")
        @Expose
        private String requestRaisedTime;
        @SerializedName("approved_time")
        @Expose
        private String approvedTime;
        @SerializedName("rejected_time")
        @Expose
        private String rejectedTime;
        @SerializedName("re_request_time")
        @Expose
        private String reRequestTime;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("sales_executive")
        @Expose
        private String salesExecutive;
        @SerializedName("deliveryAgent")
        @Expose
        private String deliveryAgent;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        private boolean selected=false;
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getLoadingPoints() {
            return loadingPoints;
        }

        public void setLoadingPoints(String loadingPoints) {
            this.loadingPoints = loadingPoints;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getSaleOrderNo() {
            return saleOrderNo;
        }

        public void setSaleOrderNo(String saleOrderNo) {
            this.saleOrderNo = saleOrderNo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getRequestRaisedTime() {
            return requestRaisedTime;
        }

        public void setRequestRaisedTime(String requestRaisedTime) {
            this.requestRaisedTime = requestRaisedTime;
        }

        public String getApprovedTime() {
            return approvedTime;
        }

        public void setApprovedTime(String approvedTime) {
            this.approvedTime = approvedTime;
        }

        public String getRejectedTime() {
            return rejectedTime;
        }

        public void setRejectedTime(String rejectedTime) {
            this.rejectedTime = rejectedTime;
        }

        public String getReRequestTime() {
            return reRequestTime;
        }

        public void setReRequestTime(String reRequestTime) {
            this.reRequestTime = reRequestTime;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getSalesExecutive() {
            return salesExecutive;
        }

        public void setSalesExecutive(String salesExecutive) {
            this.salesExecutive = salesExecutive;
        }

        public String getDeliveryAgent() {
            return deliveryAgent;
        }

        public void setDeliveryAgent(String deliveryAgent) {
            this.deliveryAgent = deliveryAgent;
        }

    }
}
