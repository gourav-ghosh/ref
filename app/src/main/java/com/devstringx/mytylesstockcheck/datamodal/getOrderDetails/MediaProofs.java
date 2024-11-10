package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import java.util.List;

import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ComplaintMediasItem;
import com.devstringx.mytylesstockcheck.datamodal.getComplaintDetails.ResponseMediasItem;
import com.google.gson.annotations.SerializedName;

public class MediaProofs{

	@SerializedName("paymentProof")
	private List<ResponseMediasItem> paymentProof;

	@SerializedName("arrangementProof")
	private List<Object> arrangementProof;

	@SerializedName("loadingProof")
	private List<ComplaintMediasItem> loadingProof;

	@SerializedName("pickedUpProof")
	private List<Object> pickedUpProof;

	@SerializedName("instructionMedias")
	private List<Object> instructionMedias;

	@SerializedName("deliveryProof")
	private List<ResponseMediasItem> deliveryProof;

	public List<ResponseMediasItem> getPaymentProof(){
		return paymentProof;
	}

	public List<Object> getArrangementProof(){
		return arrangementProof;
	}

	public List<ComplaintMediasItem> getLoadingProof(){
		return loadingProof;
	}

	public List<Object> getPickedUpProof(){
		return pickedUpProof;
	}

	public List<Object> getInstructionMedias(){
		return instructionMedias;
	}

	public List<ResponseMediasItem> getDeliveryProof(){
		return deliveryProof;
	}
}