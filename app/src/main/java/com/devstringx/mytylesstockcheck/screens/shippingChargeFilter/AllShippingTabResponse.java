package com.devstringx.mytylesstockcheck.screens.shippingChargeFilter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllShippingTabResponse {

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

        @SerializedName("statusTab")
        @Expose
        private StatusTab statusTab;

        public StatusTab getStatusTab() {
            return statusTab;
        }

        public void setStatusTab(StatusTab statusTab) {
            this.statusTab = statusTab;
        }
    }

    public class StatusTab {

        @SerializedName("Requested")
        @Expose
        private String requested;
        @SerializedName("Approved")
        @Expose
        private String approved;
        @SerializedName("Paid")
        @Expose
        private String paid;
        @SerializedName("Rejected")
        @Expose
        private String rejected;
        @SerializedName("All")
        @Expose
        private String all;

        @SerializedName("Pending")
        @Expose
        private String pending;

        public String getPending() {
            return pending;
        }

        public void setPending(String pending) {
            this.pending = pending;
        }

        public String getRequested() {
            return requested;
        }

        public void setRequested(String requested) {
            this.requested = requested;
        }

        public String getApproved() {
            return approved;
        }

        public void setApproved(String approved) {
            this.approved = approved;
        }

        public String getPaid() {
            return paid;
        }

        public void setPaid(String paid) {
            this.paid = paid;
        }

        public String getRejected() {
            return rejected;
        }

        public void setRejected(String rejected) {
            this.rejected = rejected;
        }

        public String getAll() {
            return all;
        }

        public void setAll(String all) {
            this.all = all;
        }
    }


}
