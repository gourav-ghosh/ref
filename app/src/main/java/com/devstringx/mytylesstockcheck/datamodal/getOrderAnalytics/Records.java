package com.devstringx.mytylesstockcheck.datamodal.getOrderAnalytics;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Records{

	@SerializedName("LeadTicketSizeAnalysis")
	private LeadTicketSizeAnalysis leadTicketSizeAnalysis;

	@SerializedName("ShowroomVisit")
	private ShowroomVisit showroomVisit;

	@SerializedName("SaleAmountReport")
	private List<SaleAmountReportItem> saleAmountReport;

	@SerializedName("OrderAnalysis")
	private OrderAnalysis orderAnalysis;

	@SerializedName("SaleAnalysis")
	private SaleAnalysis saleAnalysis;

	@SerializedName("SaleAnalysisMonth")
	private SaleAnalysisMonth saleAnalysisMonth;

	public LeadTicketSizeAnalysis getLeadTicketSizeAnalysis(){
		return leadTicketSizeAnalysis;
	}

	public ShowroomVisit getShowroomVisit(){
		return showroomVisit;
	}

	public List<SaleAmountReportItem> getSaleAmountReport(){
		return saleAmountReport;
	}

	public OrderAnalysis getOrderAnalysis(){
		return orderAnalysis;
	}

	public SaleAnalysis getSaleAnalysis(){
		return saleAnalysis;
	}

	public SaleAnalysisMonth getSaleAnalysisMonth(){
		return saleAnalysisMonth;
	}
}