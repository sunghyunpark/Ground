package model;

import com.google.gson.annotations.SerializedName;

public class AreaModel {
    @SerializedName("area_no")
    private int areaNo;
    private String areaName;
    @SerializedName("updated_at")
    private String updatedAt;

    public int getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(int areaNo) {
        this.areaNo = areaNo;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }


    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
