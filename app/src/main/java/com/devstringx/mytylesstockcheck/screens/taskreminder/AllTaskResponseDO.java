package com.devstringx.mytylesstockcheck.screens.taskreminder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllTaskResponseDO {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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
        private List<TaskReminderItem> records;

        public List<TaskReminderItem> getRecords() {
            return records;
        }

        public void setRecords(List<TaskReminderItem> records) {
            this.records = records;
        }

    }
}
