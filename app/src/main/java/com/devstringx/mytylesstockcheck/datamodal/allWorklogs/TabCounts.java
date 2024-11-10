package com.devstringx.mytylesstockcheck.datamodal.allWorklogs;

import com.google.gson.annotations.SerializedName;

public class TabCounts {
    @SerializedName("totalEmployee")
    private int totalEmployee;

    @SerializedName("presentEmployee")
    private int presentEmployee;

    @SerializedName("absentEmployee")
    private int absentEmployee;


    @SerializedName("notUpdatedEmployee")
    private int notUpdatedEmployee;

    public void setAbsentEmployee(int absentEmployee) {
        this.absentEmployee = absentEmployee;
    }

    public int getAbsentEmployee() {
        return absentEmployee;
    }

    public void setNotUpdatedEmployee(int notUpdatedEmployee) {
        this.notUpdatedEmployee = notUpdatedEmployee;
    }

    public int getNotUpdatedEmployee() {
        return notUpdatedEmployee;
    }

    public void setPresentEmployee(int presentEmployee) {
        this.presentEmployee = presentEmployee;
    }

    public int getPresentEmployee() {
        return presentEmployee;
    }

    public void setTotalEmployee(int totalEmployee) {
        this.totalEmployee = totalEmployee;
    }

    public int getTotalEmployee() {
        return totalEmployee;
    }
}
