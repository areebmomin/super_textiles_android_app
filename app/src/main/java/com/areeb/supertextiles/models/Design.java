package com.areeb.supertextiles.models;

import java.util.ArrayList;

public class Design {

    private String design_no;
    private String design_color;
    private ArrayList<String> meterList;

    //default constructor
    public Design() {

    }

    public String getDesign_no() {
        return design_no;
    }

    public void setDesign_no(String design_no) {
        this.design_no = design_no;
    }

    public String getDesign_color() {
        return design_color;
    }

    public void setDesign_color(String design_color) {
        this.design_color = design_color;
    }

    public ArrayList<String> getMeterList() {
        return meterList;
    }

    public void setMeterList(ArrayList<String> meterList) {
        this.meterList = meterList;
    }
}
