package com.devstringx.mytylesstockcheck.datamodal.allShippingAddress;

import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.Record;
import com.devstringx.mytylesstockcheck.datamodal.leadDetailsData.ShippingAddresses;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

	@SerializedName("record")
	private List<ShippingAddresses> shippingAddresses;

	public List<ShippingAddresses> getShippingAddresses() {
		return shippingAddresses;
	}

	public void setShippingAddresses(List<ShippingAddresses> shippingAddresses) {
		this.shippingAddresses = shippingAddresses;
	}
}