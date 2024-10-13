package com.book.BookProject.order;

public class PaymentInfo {
    private String impUid;
    private String merchantUid;
    private int amount;

    public PaymentInfo(String impUid, String merchantUid, int amount) {
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.amount = amount;
    }

    public String getImpUid() {
        return impUid;
    }

    public String getMerchantUid() {
        return merchantUid;
    }

    public int getAmount() {
        return amount;
    }
}