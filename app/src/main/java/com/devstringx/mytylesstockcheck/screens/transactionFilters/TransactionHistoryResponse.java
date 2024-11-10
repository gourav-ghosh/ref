package com.devstringx.mytylesstockcheck.screens.transactionFilters;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionHistoryResponse {

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
        @SerializedName("paymentMode")
        @Expose
        private List<String> paymentMode;
        @SerializedName("accountManager")
        @Expose
        private List<String> accountManager;
        @SerializedName("salesExecutive")
        @Expose
        private List<String> salesExecutive;
        @SerializedName("dateFilter")
        @Expose
        private DateFilter dateFilter;
        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("records")
        @Expose
        private List<Record> records;

        public String getSearch() {
            return search;
        }

        public void setSearch(String search) {
            this.search = search;
        }

        public List<String> getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(List<String> paymentMode) {
            this.paymentMode = paymentMode;
        }

        public List<String> getAccountManager() {
            return accountManager;
        }

        public void setAccountManager(List<String> accountManager) {
            this.accountManager = accountManager;
        }

        public List<String> getSalesExecutive() {
            return salesExecutive;
        }

        public void setSalesExecutive(List<String> salesExecutive) {
            this.salesExecutive = salesExecutive;
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

        @SerializedName("sale_order_no")
        @Expose
        private String saleOrderNo;
        @SerializedName("sales_executive")
        @Expose
        private String salesExecutive;
        @SerializedName("account_manager")
        @Expose
        private String accountManager;
        @SerializedName("client_name")
        @Expose
        private String clientName;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("payment_mode")
        @Expose
        private String paymentMode;
        @SerializedName("Ref_no")
        @Expose
        private String refNo;
        @SerializedName("partial_amount")
        @Expose
        private String partialAmount;
        @SerializedName("customer_name")
        @Expose
        private String customerName;

        public String getSaleOrderNo() {
            return saleOrderNo;
        }

        public void setSaleOrderNo(String saleOrderNo) {
            this.saleOrderNo = saleOrderNo;
        }

        public String getSalesExecutive() {
            return salesExecutive;
        }

        public void setSalesExecutive(String salesExecutive) {
            this.salesExecutive = salesExecutive;
        }

        public String getAccountManager() {
            return accountManager;
        }

        public void setAccountManager(String accountManager) {
            this.accountManager = accountManager;
        }

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public String getRefNo() {
            return refNo;
        }

        public void setRefNo(String refNo) {
            this.refNo = refNo;
        }

        public String getPartialAmount() {
            return partialAmount;
        }

        public void setPartialAmount(String partialAmount) {
            this.partialAmount = partialAmount;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

    }
}
