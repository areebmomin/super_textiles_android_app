package com.areeb.supertextiles.models;

public class Report {

    private String billNo;
    private String challanNo;
    private String date;
    private String lotNo;
    private String transport;
    private String party;
    private String partyLowerCase;
    private String deliveryAddress;
    private String pieces;
    private String meters;
    private String designNo;
    private String amount;
    private String rate;
    private String gstPercent;
    private String gstAmount;
    private String totalGSTAmount;
    private String commission;
    private String netAmount;
    private String finalAmount;
    private String receivedAmount;
    private String chequeNo;
    private String chequeDate;
    private String discountPercent;

    //default constructor
    public Report() {
        billNo = " ";
        challanNo = " ";
        date = " ";
        lotNo = " ";
        transport = " ";
        party = " ";
        deliveryAddress = " ";
        pieces = " ";
        meters = " ";
        designNo = " ";
        amount = " ";
        rate = " ";
        gstPercent = " ";
        gstAmount = " ";
        totalGSTAmount = " ";
        commission = " ";
        netAmount = " ";
        finalAmount = " ";
        receivedAmount = " ";
        chequeNo = " ";
        chequeDate = " ";
        discountPercent = " ";
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPieces() {
        return pieces;
    }

    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    public String getMeters() {
        return meters;
    }

    public void setMeters(String meters) {
        this.meters = meters;
    }

    public String getDesignNo() {
        return designNo;
    }

    public void setDesignNo(String designNo) {
        this.designNo = designNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getGstPercent() {
        return gstPercent;
    }

    public void setGstPercent(String gstPercent) {
        this.gstPercent = gstPercent;
    }

    public String getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(String gstAmount) {
        this.gstAmount = gstAmount;
    }

    public String getTotalGSTAmount() {
        return totalGSTAmount;
    }

    public void setTotalGSTAmount(String totalGSTAmount) {
        this.totalGSTAmount = totalGSTAmount;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getPartyLowerCase() {
        return partyLowerCase;
    }

    public void setPartyLowerCase(String partyLowerCase) {
        this.partyLowerCase = partyLowerCase;
    }
}
