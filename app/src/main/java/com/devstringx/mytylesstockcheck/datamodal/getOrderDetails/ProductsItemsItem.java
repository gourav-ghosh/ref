package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ProductsItemsItem{

	@SerializedName("quantity")
	private String quantity;

	@SerializedName("not_available_in_days")
	private String notAvailableInDays;

	@SerializedName("not_available_quantity")
	private String notAvailableQuantity;

	@SerializedName("product_image_key")
	private String productImageKey;

	@SerializedName("quotation_id")
	private String quotationId;

	@SerializedName("stock_check_status")
	private String stockCheckStatus;

	@SerializedName("can_recheck")
	private boolean canRecheck;

	@SerializedName("discount")
	private String discount;

	@SerializedName("description")
	private String description;

	@SerializedName("product_code")
	private String productCode;

	@SerializedName("hsn_code")
	private String hsnCode;

	@SerializedName("product_name")
	private String productName;

	@SerializedName("gst_rate")
	private String gstRate;

	@SerializedName("unit_of_measurement")
	private String unitOfMeasurement;

	@SerializedName("product_key")
	private List<Object> productKey;

	@SerializedName("total_amount")
	private String totalAmount;

	@SerializedName("others_options")
	private String othersOptions;

	@SerializedName("price")
	private String price;

	@SerializedName("product_id")
	private String productId;

	@SerializedName("product_attachment")
	private List<Object> productAttachment;

	@SerializedName("id")
	private String id;

	public String getQuantity(){
		return quantity;
	}

	public String getNotAvailableInDays(){
		return notAvailableInDays;
	}

	public String getNotAvailableQuantity(){
		return notAvailableQuantity;
	}

	public String getProductImageKey(){
		return productImageKey;
	}

	public String getQuotationId(){
		return quotationId;
	}

	public String getStockCheckStatus(){
		return stockCheckStatus;
	}

	public boolean isCanRecheck(){
		return canRecheck;
	}

	public String getDiscount(){
		return discount;
	}

	public String getDescription(){
		return description;
	}

	public String getProductCode(){
		return productCode;
	}

	public String getHsnCode(){
		return hsnCode;
	}

	public String getProductName(){
		return productName;
	}

	public String getGstRate(){
		return gstRate;
	}

	public String getUnitOfMeasurement(){
		return unitOfMeasurement;
	}

	public List<Object> getProductKey(){
		return productKey;
	}

	public String getTotalAmount(){
		return totalAmount;
	}

	public String getOthersOptions(){
		return othersOptions;
	}

	public String getPrice(){
		return price;
	}

	public String getProductId(){
		return productId;
	}

	public List<Object> getProductAttachment(){
		return productAttachment;
	}

	public String getId(){
		return id;
	}
}