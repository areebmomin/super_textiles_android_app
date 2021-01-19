package com.areeb.supertextiles.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.areeb.supertextiles.R;
import com.areeb.supertextiles.models.Design;
import com.google.gson.Gson;

import static com.areeb.supertextiles.activities.MainActivity.CHALLAN_FRAGMENT;

public class PreferenceManager {

    private final Context context;
    private SharedPreferences sharedPreferences;
    private final String TOTAL_METERS = "TOTAL_METERS";
    private final String NUMBER_OF_DESIGNS = "NUMBER_OF_DESIGNS";
    private final String DESIGN_1_OBJECT = "DESIGN_1_OBJECT";
    private final String DESIGN_2_OBJECT = "DESIGN_2_OBJECT";
    private final String DESIGN_3_OBJECT = "DESIGN_3_OBJECT";
    private final String DESIGN_4_OBJECT = "DESIGN_4_OBJECT";
    private final String LAST_VISITED_FRAGMENT = "LAST_VISITED_FRAGMENT";

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
        sharedPreferences.edit().putString(DESIGN_1_OBJECT, null).apply();
        sharedPreferences.edit().putString(DESIGN_2_OBJECT, null).apply();
        sharedPreferences.edit().putString(DESIGN_3_OBJECT, null).apply();
        sharedPreferences.edit().putString(DESIGN_4_OBJECT, null).apply();
    }

    //method to set NUMBER_OF_DESIGNS
    public int getNumberOfDesigns() {
        return sharedPreferences.getInt(NUMBER_OF_DESIGNS, 0);
    }

    //method to get NUMBER_OF_DESIGNS
    public void setNumberOfDesigns(int numberOfDesigns) {
        sharedPreferences.edit().putInt(NUMBER_OF_DESIGNS, numberOfDesigns).apply();
    }

    //method to set Design object for design number 1
    public void setDesign1Object(Design design1Object) {
        Gson gson = new Gson();
        String design1Json = gson.toJson(design1Object);
        sharedPreferences.edit().putString(DESIGN_1_OBJECT, design1Json).apply();
    }

    //method to set Design object for design number 2
    public void setDesign2Object(Design design2Object) {
        Gson gson = new Gson();
        String design2Json = gson.toJson(design2Object);
        sharedPreferences.edit().putString(DESIGN_2_OBJECT, design2Json).apply();
    }

    //method to set Design object for design number 3
    public void setDesign3Object(Design design3Object) {
        Gson gson = new Gson();
        String design3Json = gson.toJson(design3Object);
        sharedPreferences.edit().putString(DESIGN_3_OBJECT, design3Json).apply();
    }

    //method to set Design object for design number 4
    public void setDesign4Object(Design design4Object) {
        Gson gson = new Gson();
        String design4Json = gson.toJson(design4Object);
        sharedPreferences.edit().putString(DESIGN_4_OBJECT, design4Json).apply();
    }

    //method to get Design object for design number 1
    public Design getDesign1Object(){
        String design1Json = sharedPreferences.getString(DESIGN_1_OBJECT, "");
        Gson gson = new Gson();
        return gson.fromJson(design1Json, Design.class);
    }

    //method to get Design object for design number 2
    public Design getDesign2Object(){
        String design2Json = sharedPreferences.getString(DESIGN_2_OBJECT, "");
        Gson gson = new Gson();
        return gson.fromJson(design2Json, Design.class);
    }

    //method to get Design object for design number 3
    public Design getDesign3Object(){
        String design3Json = sharedPreferences.getString(DESIGN_3_OBJECT, "");
        Gson gson = new Gson();
        return gson.fromJson(design3Json, Design.class);
    }

    //method to get Design object for design number 4
    public Design getDesign4Object(){
        String design4Json = sharedPreferences.getString(DESIGN_4_OBJECT, "");
        Gson gson = new Gson();
        return gson.fromJson(design4Json, Design.class);
    }

    //method to set last visited fragment
    public void setLastVisitedFragment(String fragment) {
        sharedPreferences.edit().putString(LAST_VISITED_FRAGMENT, fragment).apply();
    }

    //method to get last visited fragment
    public String getLastVisitedFragment() {
        return sharedPreferences.getString(LAST_VISITED_FRAGMENT, CHALLAN_FRAGMENT);
    }
}
