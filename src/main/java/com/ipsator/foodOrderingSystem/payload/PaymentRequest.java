package com.ipsator.foodOrderingSystem.payload;

public class PaymentRequest {

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    private String modeOfPayment;

}
