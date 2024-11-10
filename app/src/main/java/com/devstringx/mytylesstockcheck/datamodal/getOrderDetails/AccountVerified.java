package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AccountVerified {

    @SerializedName("activity")
    String activity;

    @SerializedName("message")
    String message;

    @SerializedName("customer_message")
    String customerMessage;

    @SerializedName("comment")
    String comment;

    @SerializedName("wt_created_at")
    Date wtCreatedAt;

    @SerializedName("created_at")
    String createdAt;

    @SerializedName("updated_at")
    String updatedAt;

    @SerializedName("commented_by")
    String commentedBy;

    @SerializedName("activity_by")
    String activityBy;


    public void setActivity(String activity) {
        this.activity = activity;
    }
    public String getActivity() {
        return activity;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }
    public String getCustomerMessage() {
        return customerMessage;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getComment() {
        return comment;
    }

    public void setWtCreatedAt(Date wtCreatedAt) {
        this.wtCreatedAt = wtCreatedAt;
    }
    public Date getWtCreatedAt() {
        return wtCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }
    public String getCommentedBy() {
        return commentedBy;
    }

    public void setActivityBy(String activityBy) {
        this.activityBy = activityBy;
    }
    public String getActivityBy() {
        return activityBy;
    }

}
