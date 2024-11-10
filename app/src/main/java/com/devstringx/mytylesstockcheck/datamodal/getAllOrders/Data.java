package com.devstringx.mytylesstockcheck.datamodal.getAllOrders;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("poCodes")
	private List<Object> poCodes;

	@SerializedName("orderType")
	private List<Object> orderType;

	@SerializedName("orderRequirement")
	private List<Object> orderRequirement;

	@SerializedName("manager")
	private List<Object> manager;

	@SerializedName("paymentMode")
	private List<Object> paymentMode;

	@SerializedName("records")
	private List<RecordsItem> records;

	@SerializedName("statusTab2")
	private String statusTab2;

	@SerializedName("deliveryType")
	private List<Object> deliveryType;

	@SerializedName("count")
	private int count;

	@SerializedName("orderStatus")
	private List<Object> orderStatus;

	@SerializedName("saleExecutive")
	private List<Object> saleExecutive;

	@SerializedName("dateFilter")
	private DateFilter dateFilter;

	@SerializedName("search")
	private Object search;

	@SerializedName("sorting")
	private String sorting;

	@SerializedName("deliveryAgent")
	private List<Object> deliveryAgent;

	@SerializedName("statusTab")
	private String statusTab;

	public List<Object> getPoCodes(){
		return poCodes;
	}

	public List<Object> getOrderType(){
		return orderType;
	}

	public List<Object> getOrderRequirement(){
		return orderRequirement;
	}

	public List<Object> getManager(){
		return manager;
	}

	public List<Object> getPaymentMode(){
		return paymentMode;
	}

	public List<RecordsItem> getRecords(){
		return records;
	}

	public String getStatusTab2(){
		return statusTab2;
	}

	public List<Object> getDeliveryType(){
		return deliveryType;
	}

	public int getCount(){
		return count;
	}

	public List<Object> getOrderStatus(){
		return orderStatus;
	}

	public List<Object> getSaleExecutive(){
		return saleExecutive;
	}

	public DateFilter getDateFilter(){
		return dateFilter;
	}

	public Object getSearch(){
		return search;
	}

	public String getSorting(){
		return sorting;
	}

	public List<Object> getDeliveryAgent(){
		return deliveryAgent;
	}

	public String getStatusTab(){
		return statusTab;
	}
}