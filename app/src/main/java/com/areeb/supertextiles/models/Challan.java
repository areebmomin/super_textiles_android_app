package com.areeb.supertextiles.models;

public class Challan {
    private String challan_no;
    private String date;
    private String purchaser;
    private String lot_no;
    private String lr_no;
    private String delivery_at;
    private String purchaser_gst;
    private String quality;
    private String total_pieces;
    private String total_meters;
    private String fold;
    private String no_of_designs;

    public Challan() {

    }

    public String getChallan_no() {
        return challan_no;
    }

    public void setChallan_no(String challan_no) {
        this.challan_no = challan_no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(String purchaser) {
        this.purchaser = purchaser;
    }

    public String getLot_no() {
        return lot_no;
    }

    public void setLot_no(String lot_no) {
        if (lot_no.isEmpty()) {
            this.lot_no = " ";
        } else {
            this.lot_no = lot_no;
        }
    }

    public String getLr_no() {
        return lr_no;
    }

    public void setLr_no(String lr_no) {
        if (lr_no.isEmpty()) {
            this.lr_no = " ";
        } else {
            this.lr_no = lr_no;
        }
    }

    public String getDelivery_at() {
        return delivery_at;
    }

    public void setDelivery_at(String delivery_at) {
        this.delivery_at = delivery_at;
    }

    public String getPurchaser_gst() {
        return purchaser_gst;
    }

    public void setPurchaser_gst(String purchaser_gst) {
        this.purchaser_gst = purchaser_gst;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getTotal_pieces() {
        return total_pieces;
    }

    public void setTotal_pieces(String total_pieces) {
        this.total_pieces = total_pieces;
    }

    public String getTotal_meters() {
        return total_meters;
    }

    public void setTotal_meters(String total_meters) {
        this.total_meters = total_meters;
    }

    public String getFold() {
        return fold;
    }

    public void setFold(String fold) {
        this.fold = fold;
    }

    public String getNo_of_designs() {
        return no_of_designs;
    }

    public void setNo_of_designs(String no_of_designs) {
        this.no_of_designs = no_of_designs;
    }

}
