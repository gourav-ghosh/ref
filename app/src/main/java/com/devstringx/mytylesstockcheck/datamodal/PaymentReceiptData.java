package com.devstringx.mytylesstockcheck.datamodal;

import java.util.List;

public class PaymentReceiptData {
    private int sr_no;
    private String paymentAmount;
    private String payment_ref;
    private String paymentMode;

    public String getPaymentModeLabel() {
        return paymentModeLabel;
    }

    public void setPaymentModeLabel(String paymentModeLabel) {
        this.paymentModeLabel = paymentModeLabel;
    }

    private String paymentModeLabel;

    

    public String getPayment_amount() {
        return paymentAmount;
    }

    public void setPayment_amount(String payment_amount) {
        this.paymentAmount = payment_amount;
    }

    public String getPayment_ref() {
        return payment_ref;
    }

    public void setPayment_ref(String payment_ref) {
        this.payment_ref = payment_ref;
    }


    public String getPayment_mode() {
        return paymentMode;
    }

    public void setPayment_mode(String payment_mode) {
        this.paymentMode = payment_mode;
    }
    public int getSr_no() {
        return sr_no;
    }

    public void setSr_no(int sr_no) {
        this.sr_no = sr_no;
    }

}
