package com.devstringx.mytylesstockcheck.datamodal.dashboard.scAnalyticData;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Records{

	@SerializedName("result")
	private List<ResultItem> result;

	@SerializedName("percentageChange")
	private PercentageChange percentageChange;

	public void setResult(List<ResultItem> result){
		this.result = result;
	}

	public List<ResultItem> getResult(){
		return result;
	}

	public void setPercentageChange(PercentageChange percentageChange){
		this.percentageChange = percentageChange;
	}

	public PercentageChange getPercentageChange(){
		return percentageChange;
	}
}