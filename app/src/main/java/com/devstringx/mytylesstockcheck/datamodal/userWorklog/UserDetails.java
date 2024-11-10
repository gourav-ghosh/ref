package com.devstringx.mytylesstockcheck.datamodal.userWorklog;

import com.google.gson.annotations.SerializedName;

public class UserDetails {
    @SerializedName("user_id")
    private int userId;

    @SerializedName("user_first_name")
    private String userFirstName;

    @SerializedName("user_last_name")
    private String userLastName;

    @SerializedName("user_phone")
    private String userPhone;

    @SerializedName("user_profile_image")
    private String profileImage;

    @SerializedName("user_role_name")
    private String userRoleName;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
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

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    public String getUserRoleName() {
        return userRoleName;
    }
}
