package com.areeb.supertextiles.models;

import java.util.ArrayList;

public class Bill {
    private String billNo;
    private String date;
    private String eWayBillNo;
    private String messers;
    private String messersLowerCase;
    private String address;
    private String purchaserGst;
    private String contractNo;
    private String dated;
    private String broker;
    private String description;
    private String noOfPieces;
    private String quantity;
    private String discountOnQuantity;
    private String quantityAfterDiscount;
    private String rate;
    private String amount;
    private String total;
    private String discount;
    private String discountAmount;
    private String netAmount;
    private String SGSTAmount;
    private String CGSTAmount;
    private String IGSTAmount;
    private String SGSTPercent;
    private String CGSTPercent;
    private String IGSTPercent;
    private String amountAfterTax;
    private ArrayList<String> challanNoList;

    //default constructor
    public Bill() {
        billNo = " ";
        date = " ";
        challanNoList = new ArrayList<>();
        eWayBillNo = " ";
        messers = " ";
        messersLowerCase = " ";
        address = " ";
        purchaserGst = " ";
        contractNo = " ";
        dated = " ";
        broker = " ";
        description = " ";
        noOfPieces = " ";
        quantity = " ";
        discountOnQuantity = " ";
        quantityAfterDiscount = " ";
        rate = " ";
        amount = " ";
        total = " ";
        discount = " ";
        discountAmount = " ";
        netAmount = " ";
        SGSTAmount = " ";
        CGSTAmount = " ";
        IGSTAmount = " ";
        SGSTPercent = " ";
        CGSTPercent = " ";
        IGSTPercent = " ";
        amountAfterTax = " ";
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getChallanNo() {
        return challanNoList;
    }

    public void setChallanNo(ArrayList<String> challanNoList) {
        this.challanNoList = challanNoList;
    }

    public String geteWayBillNo() {
        return eWayBillNo;
    }

    public void seteWayBillNo(String eWayBillNo) {
        this.eWayBillNo = eWayBillNo;
    }

    public String getMessers() {
        return messers;
    }

    public void setMessers(String messers) {
        this.messers = messers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPurchaserGst() {
        return purchaserGst;
    }

    public void setPurchaserGst(String purchaserGst) {
        this.purchaserGst = purchaserGst;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNoOfPieces() {
        return noOfPieces;
    }

    public void setNoOfPieces(String noOfPieces) {
        this.noOfPieces = noOfPieces;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getAmountAfterTax() {
        return amountAfterTax;
    }

    public void setAmountAfterTax(String amountAfterTax) {
        this.amountAfterTax = amountAfterTax;
    }

    public String getDiscountOnQuantity() {
        return discountOnQuantity;
    }

    public void setDiscountOnQuantity(String discountOnQuantity) {
        this.discountOnQuantity = discountOnQuantity;
    }

    public String getQuantityAfterDiscount() {
        return quantityAfterDiscount;
    }

    public void setQuantityAfterDiscount(String quantityAfterDiscount) {
        this.quantityAfterDiscount = quantityAfterDiscount;
    }

    public String getSGSTAmount() {
        return SGSTAmount;
    }

    public void setSGSTAmount(String SGSTAmount) {
        this.SGSTAmount = SGSTAmount;
    }

    public String getCGSTAmount() {
        return CGSTAmount;
    }

    public void setCGSTAmount(String CGSTAmount) {
        this.CGSTAmount = CGSTAmount;
    }

    public String getIGSTAmount() {
        return IGSTAmount;
    }

    public void setIGSTAmount(String IGSTAmount) {
        this.IGSTAmount = IGSTAmount;
    }

    public String getSGSTPercent() {
        return SGSTPercent;
    }

    public void setSGSTPercent(String SGSTPercent) {
        this.SGSTPercent = SGSTPercent;
    }

    public String getCGSTPercent() {
        return CGSTPercent;
    }

    public void setCGSTPercent(String CGSTPercent) {
        this.CGSTPercent = CGSTPercent;
    }

    public String getIGSTPercent() {
        return IGSTPercent;
    }

    public void setIGSTPercent(String IGSTPercent) {
        this.IGSTPercent = IGSTPercent;
    }

    public String getMessersLowerCase() {
        return messersLowerCase;
    }

    public void setMessersLowerCase(String messersLowerCase) {
        this.messersLowerCase = messersLowerCase;
    }
}
