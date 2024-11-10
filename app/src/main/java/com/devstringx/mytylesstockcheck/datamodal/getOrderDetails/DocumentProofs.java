package com.devstringx.mytylesstockcheck.datamodal.getOrderDetails;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DocumentProofs{

	@SerializedName("paymentProof")
	private List<Object> paymentProof;

	@SerializedName("arrangementProof")
	private List<Object> arrangementProof;

	@SerializedName("loadingProof")
	private List<Object> loadingProof;

	@SerializedName("pickedUpProof")
	private List<Object> pickedUpProof;

	@SerializedName("instructionMedias")
	private List<Object> instructionMedias;

	@SerializedName("deliveryProof")
	private List<Object> deliveryProof;

	public List<Object> getPaymentProof(){
		return paymentProof;
	}

	public List<Object> getArrangementProof(){
		return arrangementProof;
	}

	public List<Object> getLoadingProof(){
		return loadingProof;
	}

	public List<Object> getPickedUpProof(){
		return pickedUpProof;
	}

	public List<Object> getInstructionMedias(){
		return instructionMedias;
	}

	public List<Object> getDeliveryProof(){
		return deliveryProof;
	}
}