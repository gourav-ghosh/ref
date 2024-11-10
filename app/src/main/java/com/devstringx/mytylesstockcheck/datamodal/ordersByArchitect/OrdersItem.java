package com.devstringx.mytylesstockcheck.datamodal.ordersByArchitect;

import com.google.gson.annotations.SerializedName;

public class OrdersItem {

    @SerializedName("order_id")
    private String orderId;

    @SerializedName("sale_order_no")
    private String orderNo;

    @SerializedName("status")
    private String orderStatus;

    @SerializedName("created_at")
    private String orderDate;

    @SerializedName("quote_order_amount")
    private String orderAmount;

    public void setOrderId(String id) {
        this.orderId = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

}
