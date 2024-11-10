package com.devstringx.mytylesstockcheck.datamodal.userWorklog;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecordsItem {
    @SerializedName("id")
    private int id;

    @SerializedName("status")
    private String status;

    @SerializedName("hours_worked")
    private String hoursWorked;

    @SerializedName("comment")
    private String comment;

    @SerializedName("working_date")
    private String workingDate;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("created_date")
    private String createdDate;

    @SerializedName("created_time")
    private String createdTime;

    @SerializedName("medias")
    private List<MediaItems> medias;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setWorkingDate(String workingDate) {
        this.workingDate = workingDate;
    }

    public String getWorkingDate() {
        return workingDate;
    }

    public void setHoursWorked(String hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public String getHoursWorked() {
        return hoursWorked;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setMedias(List<MediaItems> medias) {
        this.medias = medias;
    }

    public List<MediaItems> getMedias() {
        return medias;
    }
}
