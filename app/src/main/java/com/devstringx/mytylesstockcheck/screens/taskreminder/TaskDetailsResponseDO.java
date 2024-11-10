package com.devstringx.mytylesstockcheck.screens.taskreminder;

import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ResponseMediasItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TaskDetailsResponseDO {

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

        @SerializedName("records")
        @Expose
        private Records records;

        public Records getRecords() {
            return records;
        }

        public void setRecords(Records records) {
            this.records = records;
        }

    }


    public class Records {

        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("sale_order_no")
        @Expose
        private String saleOrderNo;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("schedule_date")
        @Expose
        private String scheduleDate;
        @SerializedName("schedule_time")
        @Expose
        private String scheduleTime;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("reminder_timing")
        @Expose
        private String reminderTiming;
        @SerializedName("medias")
        @Expose
        private List<ResponseMediasItem> medias;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSaleOrderNo() {
            return saleOrderNo;
        }

        public void setSaleOrderNo(String saleOrderNo) {
            this.saleOrderNo = saleOrderNo;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getScheduleDate() {
            return scheduleDate;
        }

        public void setScheduleDate(String scheduleDate) {
            this.scheduleDate = scheduleDate;
        }

        public String getScheduleTime() {
            return scheduleTime;
        }

        public void setScheduleTime(String scheduleTime) {
            this.scheduleTime = scheduleTime;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getReminderTiming() {
            return reminderTiming;
        }

        public void setReminderTiming(String reminderTiming) {
            this.reminderTiming = reminderTiming;
        }

        public List<ResponseMediasItem> getMedias() {
            return medias;
        }

        public void setMedias(List<ResponseMediasItem> medias) {
            this.medias = medias;
        }

    }
}
