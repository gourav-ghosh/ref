package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import com.google.gson.annotations.SerializedName;

public class ActivityLogs{

	@SerializedName("poReceived")
	private Object poReceived;

	@SerializedName("dispatched")
	private Dispatched dispatched;

	@SerializedName("orderProcessed")
	private OrderProcessed orderProcessed;

	@SerializedName("readyForPickup")
	private ReadyForPickup readyForPickup;

	@SerializedName("delivered")
	private Delivered delivered;

	@SerializedName("loading")
	private Loading loading;

	@SerializedName("accountVerified")
	private AccountVerified accountVerified;

	@SerializedName("materialArranged")
	private MaterialArranged materialArranged;

	@SerializedName("pickedUp")
	private PickedUp pickedUp;

	public Object getPoReceived(){
		return poReceived;
	}

	public Dispatched getDispatched(){
		return dispatched;
	}

	public OrderProcessed getOrderProcessed(){
		return orderProcessed;
	}

	public ReadyForPickup getReadyForPickup(){
		return readyForPickup;
	}

	public Delivered getDelivered(){
		return delivered;
	}

	public Loading getLoading(){
		return loading;
	}

	public AccountVerified getAccountVerified(){
		return accountVerified;
	}

	public MaterialArranged getMaterialArranged(){
		return materialArranged;
	}

	public PickedUp getPickedUp(){
		return pickedUp;
	}
}