package com.areeb.supertextiles;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class PreferenceManager {

    private final Context context;
    private SharedPreferences sharedPreferences;
    private final String TOTAL_METERS = "TOTAL_METERS";
    private final String NUMBER_OF_DESIGNS = "NUMBER_OF_DESIGNS";
    private final String DESIGN_1_NO = "DESIGN_1_NO";
    private final String DESIGN_2_NO = "DESIGN_2_NO";
    private final String DESIGN_3_NO = "DESIGN_3_NO";
    private final String DESIGN_4_NO = "DESIGN_4_NO";
    private final String DESIGN_1_COLOR = "DESIGN_1_COLOR";
    private final String DESIGN_2_COLOR = "DESIGN_2_COLOR";
    private final String DESIGN_3_COLOR = "DESIGN_3_COLOR";
    private final String DESIGN_4_COLOR = "DESIGN_4_COLOR";
    private final String DESIGN_1_METER_LIST = "DESIGN_1_METER_LIST";
    private final String DESIGN_2_METER_LIST = "DESIGN_2_METER_LIST";
    private final String DESIGN_3_METER_LIST = "DESIGN_3_METER_LIST";
    private final String DESIGN_4_METER_LIST = "DESIGN_4_METER_LIST";

    //default constructor
    public PreferenceManager(Context context) {
        this.context = context;
        getSharedPreference();
    }

    //initialize SharedPreference
    private void getSharedPreference() {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_preference_name), Context.MODE_PRIVATE);
    }

    //method to get TOTAL_METERS
    public String getTotalMeters() {
        return sharedPreferences.getString(TOTAL_METERS, "0");
    }

    //method to set TOTAL_METERS
    public void setTotalMeters(String totalMeters) {
        sharedPreferences.edit().putString(TOTAL_METERS, totalMeters).apply();
    }

    //set default value of TOTAL_METERS
    public void clearDesignAndMetersFromSharedPreference() {
        sharedPreferences.edit().putString(TOTAL_METERS, "0").apply();
        sharedPreferences.edit().putInt(NUMBER_OF_DESIGNS, 0).apply();
        sharedPreferences.edit().putString(DESIGN_1_NO, "0").apply();
        sharedPreferences.edit().putString(DESIGN_2_NO, "0").apply();
        sharedPreferences.edit().putString(DESIGN_3_NO, "0").apply();
        sharedPreferences.edit().putString(DESIGN_4_NO, "0").apply();
        sharedPreferences.edit().putString(DESIGN_1_COLOR, "0").apply();
        sharedPreferences.edit().putString(DESIGN_2_COLOR, "0").apply();
        sharedPreferences.edit().putString(DESIGN_3_COLOR, "0").apply();
        sharedPreferences.edit().putString(DESIGN_4_COLOR, "0").apply();
        sharedPreferences.edit().putStringSet(DESIGN_1_METER_LIST, null).apply();
        sharedPreferences.edit().putStringSet(DESIGN_2_METER_LIST, null).apply();
        sharedPreferences.edit().putStringSet(DESIGN_3_METER_LIST, null).apply();
        sharedPreferences.edit().putStringSet(DESIGN_4_METER_LIST, null).apply();
    }

    //method to get DESIGN_1_NO
    public String getDesign1No() {
        return sharedPreferences.getString(DESIGN_1_NO, "0");
    }

    //method to set DESIGN_1_NO
    public void setDesign1No(String designNo) {
        sharedPreferences.edit().putString(DESIGN_1_NO, designNo).apply();
    }

    //method to get DESIGN_2_NO
    public String getDesign2No() {
        return sharedPreferences.getString(DESIGN_2_NO, "0");
    }

    //method to set DESIGN_2_NO
    public void setDesign2No(String designNo) {
        sharedPreferences.edit().putString(DESIGN_2_NO, designNo).apply();
    }

    //method to get DESIGN_3_NO
    public String getDesign3No() {
        return sharedPreferences.getString(DESIGN_3_NO, "0");
    }

    //method to set DESIGN_3_NO
    public void setDesign3No(String designNo) {
        sharedPreferences.edit().putString(DESIGN_3_NO, designNo).apply();
    }

    //method to get DESIGN_4_NO
    public String getDesign4No() {
        return sharedPreferences.getString(DESIGN_4_NO, "0");
    }

    //method to set DESIGN_4_NO
    public void setDesign4No(String designNo) {
        sharedPreferences.edit().putString(DESIGN_4_NO, designNo).apply();
    }

    //method to get DESIGN_1_COLOR
    public String getDesign1Color() {
        return sharedPreferences.getString(DESIGN_1_COLOR, "0");
    }

    //method to set DESIGN_1_COLOR
    public void setDesign1Color(String designColor) {
        sharedPreferences.edit().putString(DESIGN_1_COLOR, designColor).apply();
    }

    //method to get DESIGN_2_COLOR
    public String getDesign2Color() {
        return sharedPreferences.getString(DESIGN_2_COLOR, "0");
    }

    //method to set DESIGN_2_COLOR
    public void setDesign2Color(String designColor) {
        sharedPreferences.edit().putString(DESIGN_2_COLOR, designColor).apply();
    }

    //method to get DESIGN_3_COLOR
    public String getDesign3Color() {
        return sharedPreferences.getString(DESIGN_3_COLOR, "0");
    }

    //method to set DESIGN_3_COLOR
    public void setDesign3Color(String designColor) {
        sharedPreferences.edit().putString(DESIGN_3_COLOR, designColor).apply();
    }

    //method to get DESIGN_4_COLOR
    public String getDesign4Color() {
        return sharedPreferences.getString(DESIGN_4_COLOR, "0");
    }

    //method to set DESIGN_4_COLOR
    public void setDesign4Color(String designColor) {
        sharedPreferences.edit().putString(DESIGN_4_COLOR, designColor).apply();
    }

    //method to get DESIGN_1_METER_LIST
    public Set<String> getDesign1MeterList() {
        return sharedPreferences.getStringSet(DESIGN_1_METER_LIST, null);
    }

    //method to set DESIGN_1_METER_LIST
    public void setDesign1MeterList(Set<String> meterSet) {
        sharedPreferences.edit().putStringSet(DESIGN_1_METER_LIST, meterSet).apply();
    }

    //method to get DESIGN_2_METER_LIST
    public Set<String> getDesign2MeterList() {
        return sharedPreferences.getStringSet(DESIGN_2_METER_LIST, null);
    }

    //method to set DESIGN_2_METER_LIST
    public void setDesign2MeterList(Set<String> meterSet) {
        sharedPreferences.edit().putStringSet(DESIGN_2_METER_LIST, meterSet).apply();
    }

    //method to get DESIGN_3_METER_LIST
    public Set<String> getDesign3MeterList() {
        return sharedPreferences.getStringSet(DESIGN_3_METER_LIST, null);
    }

    //method to set DESIGN_3_METER_LIST
    public void setDesign3MeterList(Set<String> meterSet) {
        sharedPreferences.edit().putStringSet(DESIGN_3_METER_LIST, meterSet).apply();
    }

    //method to get DESIGN_4_METER_LIST
    public Set<String> getDesign4MeterList() {
        return sharedPreferences.getStringSet(DESIGN_4_METER_LIST, null);
    }

    //method to set DESIGN_4_METER_LIST
    public void setDesign4MeterList(Set<String> meterSet) {
        sharedPreferences.edit().putStringSet(DESIGN_4_METER_LIST, meterSet).apply();
    }

    //method to set NUMBER_OF_DESIGNS
    public int getNumberOfDesigns() {
        return sharedPreferences.getInt(NUMBER_OF_DESIGNS, 0);
    }

    //method to get NUMBER_OF_DESIGNS
    public void setNumberOfDesigns(int numberOfDesigns) {
        sharedPreferences.edit().putInt(NUMBER_OF_DESIGNS, numberOfDesigns).apply();
    }
}
