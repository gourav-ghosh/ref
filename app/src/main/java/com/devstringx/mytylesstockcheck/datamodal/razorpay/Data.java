package com.devstringx.mytylesstockcheck.datamodal.razorpay;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("date")
	private Object date;

	@SerializedName("paid_count")
	private String paidCount;

	@SerializedName("records")
	private List<RazorpayRecordsItem> records;

	@SerializedName("count")
	private String count;

	@SerializedName("saleExecutive")
	private List<Object> saleExecutive;

	@SerializedName("dateFilter")
	private DateFilter dateFilter;

	@SerializedName("pending_amount")
	private String pendingAmount;

	@SerializedName("pending_count")
	private String pendingCount;

	@SerializedName("search")
	private Object search;

	@SerializedName("total_amount")
	private String totalAmount;

	@SerializedName("sorting")
	private String sorting;

	@SerializedName("paid_amount")
	private String paidAmount;

	@SerializedName("paymentStatus")
	private List<Object> status;

	public Object getDate(){
		return date;
	}

	public String getPaidCount(){
		return paidCount;
	}

	public List<RazorpayRecordsItem> getRecords(){
		return records;
	}

	public String getCount(){
		return count;
	}

	public List<Object> getSaleExecutive(){
		return saleExecutive;
	}

	public DateFilter getDateFilter(){
		return dateFilter;
	}

	public String getPendingAmount(){
		return pendingAmount;
	}

	public String getPendingCount(){
		return pendingCount;
	}

	public Object getSearch(){
		return search;
	}

	public String getTotalAmount(){
		return totalAmount;
	}

	public String getSorting(){
		return sorting;
	}

	public String getPaidAmount(){
		return paidAmount;
	}

	public List<Object> getStatus(){
		return status;
	}
}