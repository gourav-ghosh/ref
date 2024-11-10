package com.devstringx.mytylesstockcheck.datamodal.worklogDetails;

import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ComplaintMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ResponseMediasItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Records {
    @SerializedName("id")
    private int id;

    @SerializedName("status")
    private String status;

    @SerializedName("hours_worked")
    private float hoursWorked;

    @SerializedName("comment")
    private String comment;

    @SerializedName("working_date")
    private String workingDate;

    @SerializedName("user_first_name")
    private String userFirstName;

    @SerializedName("user_last_name")
    private String userLastName;

    @SerializedName("user_phone")
    private String userPhone;

    @SerializedName("user_role_name")
    private String userRoleName;

    @SerializedName("created_date")
    private String createdDate;

    @SerializedName("created_time")
    private String createdTime;

    @SerializedName("updated_date")
    private String updatedDate;

    @SerializedName("user_profile_image")
    private String userProfileImage;

    @SerializedName("worklogMedias")
    private List<ResponseMediasItem> mediaItems;

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

    public void setHoursWorked(float hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public float getHoursWorked() {
        return hoursWorked;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setWorkingDate(String workingDate) {
        this.workingDate = workingDate;
    }

    public String getWorkingDate() {
        return workingDate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setMediaItems(List<ResponseMediasItem> mediaItems) {
        this.mediaItems = mediaItems;
    }

    public List<ResponseMediasItem> getMediaItems() {
        return mediaItems;
    }
}
