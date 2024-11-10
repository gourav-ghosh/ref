package com.devstringx.mytylesstockcheck.datamodal.architectListingData;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("salesExecutive")
	private List<Object> salesExecutive;

	@SerializedName("search")
	private Object search;

	@SerializedName("records")
	private List<RecordsItem> records;

	@SerializedName("sorting")
	private String sorting;

	@SerializedName("count")
	private int count;

	@SerializedName("dateFilter")
	private DateFilter dateFilter;
	@SerializedName("link")
	private String link;

	public String getLink() {
		return link;
	}

	public void setSalesExecutive(List<Object> salesExecutive){
		this.salesExecutive = salesExecutive;
	}

	public List<Object> getSalesExecutive(){
		return salesExecutive;
	}

	public void setSearch(Object search){
		this.search = search;
	}

	public Object getSearch(){
		return search;
	}

	public void setRecords(List<RecordsItem> records){
		this.records = records;
	}

	public List<RecordsItem> getRecords(){
		return records;
	}

	public void setSorting(String sorting){
		this.sorting = sorting;
	}

	public String getSorting(){
		return sorting;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setDateFilter(DateFilter dateFilter){
		this.dateFilter = dateFilter;
	}

	public DateFilter getDateFilter(){
		return dateFilter;
	}
}