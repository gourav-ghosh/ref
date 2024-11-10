package com.devstringx.mytylesstockcheck.datamodal.getAllComplaints;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("issueForm")
	private List<Object> issueForm;

	@SerializedName("search")
	private Object search;

	@SerializedName("supportExecutive")
	private List<Object> supportExecutive;

	@SerializedName("manager")
	private List<Object> manager;

	@SerializedName("statusCounts")
	private StatusCounts statusCounts;

	@SerializedName("records")
	private List<RecordsItem> records;

	@SerializedName("sorting")
	private String sorting;

	@SerializedName("complaintType")
	private List<Object> complaintType;

	@SerializedName("statusTab")
	private String statusTab;

	@SerializedName("count")
	private int count;

	@SerializedName("saleExecutive")
	private List<Object> saleExecutive;

	@SerializedName("dateFilter")
	private DateFilter dateFilter;

	public List<Object> getIssueForm(){
		return issueForm;
	}

	public Object getSearch(){
		return search;
	}

	public List<Object> getSupportExecutive(){
		return supportExecutive;
	}

	public List<Object> getManager(){
		return manager;
	}

	public StatusCounts getStatusCounts(){
		return statusCounts;
	}

	public List<RecordsItem> getRecords(){
		return records;
	}

	public String getSorting(){
		return sorting;
	}

	public List<Object> getComplaintType(){
		return complaintType;
	}

	public String getStatusTab(){
		return statusTab;
	}

	public int getCount(){
		return count;
	}

	public List<Object> getSaleExecutive(){
		return saleExecutive;
	}

	public DateFilter getDateFilter(){
		return dateFilter;
	}
}