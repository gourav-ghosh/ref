package com.devstringx.mytylesstockcheck.datamodal.ordersByArchitect;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("count")
    private String count;

    @SerializedName("totalOrderAmount")
    private String totalOrderAmount;

    @SerializedName("architectName")
    private String architectName;

    @SerializedName("orders")
    private List<OrdersItem> orders;

    public void setCount(String count) {
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    public void setTotalOrderAmount(String totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public String getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setArchitectName(String architectName) {
        this.architectName = architectName;
    }

    public String getArchitectName() {
        return architectName;
    }

    public void setOrders(List<OrdersItem> orders){
        this.orders = orders;
    }

    public List<OrdersItem> getOrders(){
        return orders;
    }

}
