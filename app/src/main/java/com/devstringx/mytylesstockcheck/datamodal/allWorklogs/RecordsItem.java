package com.devstringx.mytylesstockcheck.datamodal.allWorklogs;

import com.google.gson.annotations.SerializedName;

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

    @SerializedName("user_first_name")
    private String firstName;

    @SerializedName("user_last_name")
    private String lastName;

    @SerializedName("user_role_name")
    private String roleName;

    @SerializedName("user_phone")
    private String phone;

    @SerializedName("user_profile_image")
    private String profileImage;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setHoursWorked(String hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public String getHoursWorked() {
        return hoursWorked;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setWorkingDate(String workingDate) {
        this.workingDate = workingDate;
    }

    public String getWorkingDate() {
        return workingDate;
    }
}
