package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Records{

	@SerializedName("dispatch_manager")
	private DispatchManager dispatchManager;

	@SerializedName("customer_phone_number")
	private String customerPhoneNumber;

	@SerializedName("delivery_address")
	private String deliveryAddress;

	@SerializedName("sales_person")
	private SalesPerson salesPerson;

	@SerializedName("documents")
	private Documents documents;

	@SerializedName("deleted_by")
	private Object deletedBy;

	@SerializedName("delivery_time")
	private String deliveryTime;

	@SerializedName("pending_material")
	private Object pendingMaterial;

	@SerializedName("media_proofs")
	private MediaProofs mediaProofs;

	@SerializedName("delivery_type")
	private String deliveryType;

	@SerializedName("delivery_location_type")
	private String deliveryLocationType;

	@SerializedName("edited_at")
	private Object editedAt;

	@SerializedName("links")
	private List<LinksItem> links;

	@SerializedName("id")
	private String id;

	@SerializedName("payment_info")
	private List<PaymentInfoItem> paymentInfo;

	@SerializedName("order_type")
	private String orderType;

	@SerializedName("quote_order_amount")
	private String quoteOrderAmount;

	@SerializedName("wf_delivery_date")
	private String wfDeliveryDate;

	@SerializedName("customer_address")
	private String customerAddress;

	@SerializedName("payment_verification")
	private String paymentVerification;

	@SerializedName("created_by")
	private String createdBy;

	@SerializedName("deleted_at")
	private Object deletedAt;

	@SerializedName("delivery_date")
	private String deliveryDate;

	@SerializedName("encrypted_sale_order_no")
	private String encryptedSaleOrderNo;

	@SerializedName("customer_email")
	private Object customerEmail;

	@SerializedName("updated_by")
	private String updatedBy;

	@SerializedName("quotation_url")
	private Object quotationUrl;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("status")
	private String status;

	@SerializedName("quotation_id")
	private String quotationId;

	@SerializedName("architect")
	private Architect architect;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("delivery_agent")
	private DeliveryAgent deliveryAgent;

	@SerializedName("is_deleted")
	private String isDeleted;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("quotationDetails")
	private QuotationDetails quotationDetails;

	@SerializedName("shipping_charge_details")
	private List<ShippingChargesDetails> shippingChargeDetails;

	@SerializedName("document_proofs")
	private DocumentProofs documentProofs;

	@SerializedName("sale_order_no")
	private String saleOrderNo;

	@SerializedName("lead_id")
	private String leadId;

	@SerializedName("direct_ready_for_pickup")
	private String directReadyForPickup;

	@SerializedName("comments")
	private List<CommentsItem> comments;

	@SerializedName("activity_logs")
	private ActivityLogs activityLogs;

	@SerializedName("delivery_instructions")
	private Object deliveryInstructions;

	@SerializedName("wf_created_at")
	private String wfCreatedAt;

	@SerializedName("delivery_modes")
	private List<DeliveryModesItem> deliveryModes;

	@SerializedName("wf_delivery_time")
	private String wfDeliveryTime;

	@SerializedName("customer_name")
	private String customerName;

	@SerializedName("start_financial_year")
	private String startFinancialYear;

	@SerializedName("short_url")
	private String shortUrl;

	@SerializedName("order_reschedule_datas")
	private List<OrderRescheduleDatasItem> orderRescheduleDatas;

	@SerializedName("order_tasks")
	private OrderTasks orderTasks;

	public DispatchManager getDispatchManager(){
		return dispatchManager;
	}

	public String getCustomerPhoneNumber(){
		return customerPhoneNumber;
	}

	public String getDeliveryAddress(){
		return deliveryAddress;
	}

	public SalesPerson getSalesPerson(){
		return salesPerson;
	}

	public Documents getDocuments(){
		return documents;
	}

	public Object getDeletedBy(){
		return deletedBy;
	}

	public String getDeliveryTime(){
		return deliveryTime;
	}

	public Object getPendingMaterial(){
		return pendingMaterial;
	}

	public MediaProofs getMediaProofs(){
		return mediaProofs;
	}

	public String getDeliveryType(){
		return deliveryType;
	}

	public String getDeliveryLocationType(){
		return deliveryLocationType;
	}

	public Object getEditedAt(){
		return editedAt;
	}

	public List<LinksItem> getLinks(){
		return links;
	}

	public String getId(){
		return id;
	}

	public List<PaymentInfoItem> getPaymentInfo(){
		return paymentInfo;
	}

	public String getOrderType(){
		return orderType;
	}

	public String getQuoteOrderAmount(){
		return quoteOrderAmount;
	}

	public String getWfDeliveryDate(){
		return wfDeliveryDate;
	}

	public String getCustomerAddress(){
		return customerAddress;
	}

	public String getPaymentVerification(){
		return paymentVerification;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public Object getDeletedAt(){
		return deletedAt;
	}

	public String getDeliveryDate(){
		return deliveryDate;
	}

	public String getEncryptedSaleOrderNo(){
		return encryptedSaleOrderNo;
	}

	public Object getCustomerEmail(){
		return customerEmail;
	}

	public String getUpdatedBy(){
		return updatedBy;
	}

	public Object getQuotationUrl(){
		return quotationUrl;
	}

	public String getOrderId(){
		return orderId;
	}

	public String getStatus(){
		return status;
	}

	public String getQuotationId(){
		return quotationId;
	}

	public Architect getArchitect(){
		return architect;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public DeliveryAgent getDeliveryAgent(){
		return deliveryAgent;
	}

	public String getIsDeleted(){
		return isDeleted;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public QuotationDetails getQuotationDetails(){
		return quotationDetails;
	}

	public List<ShippingChargesDetails> getShippingChargeDetails(){
		return shippingChargeDetails;
	}

	public DocumentProofs getDocumentProofs(){
		return documentProofs;
	}

	public String getSaleOrderNo(){
		return saleOrderNo;
	}

	public String getLeadId(){
		return leadId;
	}

	public String getDirectReadyForPickup(){
		return directReadyForPickup;
	}

	public List<CommentsItem> getComments(){
		return comments;
	}

	public ActivityLogs getActivityLogs(){
		return activityLogs;
	}

	public Object getDeliveryInstructions(){
		return deliveryInstructions;
	}

	public String getWfCreatedAt(){
		return wfCreatedAt;
	}

	public List<DeliveryModesItem> getDeliveryModes(){
		return deliveryModes;
	}

	public String getWfDeliveryTime(){
		return wfDeliveryTime;
	}

	public String getCustomerName(){
		return customerName;
	}

	public String getStartFinancialYear(){
		return startFinancialYear;
	}

	public List<OrderRescheduleDatasItem> getOrderRescheduleDatas(){
		return orderRescheduleDatas;
	}

	public OrderTasks getOrderTasks(){
		return orderTasks;
	}

	public String getShortUrl() {
		return shortUrl;
	}
}