package com.areeb.supertextiles;

import androidx.annotation.NonNull;

import com.areeb.supertextiles.models.Challan;
import com.areeb.supertextiles.models.Design;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDatabaseHelper {

    //database path strings
    public static final String CUSTOMERS = "customers";
    public static final String DELIVERY_ADDRESS = "delivery_address";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String GST_NO = "gstno";
    public static final String address = "address";
    public static final String CHALLAN_NO = "challan_no";
    public static final String CHALLAN_LIST = "challan_list";
    public static final String DESIGN_DATA = "design_data";
    public static final String DESIGN_1 = "design_1";
    public static final String DESIGN_2 = "design_2";
    public static final String DESIGN_3 = "design_3";
    public static final String DESIGN_4 = "design_4";
    public static final String DESIGN_COLOR = "design_color";
    public static final String DESIGN_NO = "design_no";
    public static final String METER_LIST = "meterList";


    //get DatabaseReference of all customers
    public static DatabaseReference getAllCustomersReference() {
        return FirebaseDatabase.getInstance().getReference().child(CUSTOMERS);
    }

    //get DatabaseReference of any particular customer
    public static DatabaseReference getCustomerDatabaseReferenceByID(String id) {
        return FirebaseDatabase.getInstance().getReference().child(CUSTOMERS).child(id);
    }

    //get DatabaseReference of deliveryAddress
    public static DatabaseReference getDeliveryAddressDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference().child(DELIVERY_ADDRESS);
    }

    //get DatabaseReference of deliveryAddress by ID
    public static DatabaseReference getDeliveryAddressDatabaseReferenceByID(String id) {
        return FirebaseDatabase.getInstance().getReference().child(DELIVERY_ADDRESS).child(id);
    }

    //get challan_no from Firebase Database
    public static DatabaseReference getChallanNoDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference().child(CHALLAN_NO);
    }

    //set challan_no
    public static void setChallanNoInDatabase(long challanNumber) {
        FirebaseDatabase.getInstance().getReference().child(CHALLAN_NO).setValue(challanNumber);
    }

    //get CHALLAN_LIST DatabaseReference
    public static DatabaseReference getChallanListDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference().child(CHALLAN_LIST);
    }

    //set Challan data in CHALLAN_LIST
    public static void setChallanDataInChallanList(String challanNo, Challan challan) {
        FirebaseDatabase.getInstance().getReference().child(CHALLAN_LIST).child(challanNo).setValue(challan);
    }

    //get DESIGN_DATA DatabaseReference
    public static DatabaseReference getDesignDataDatabaseReference(String challanNo) {
        return FirebaseDatabase.getInstance().getReference().child(DESIGN_DATA).child(challanNo);
    }

    //set designObject in DESIGN_DATA
    public static void setDesignDataInDatabase(String challanNo, String designNo, Design designObject) {
        FirebaseDatabase.getInstance().getReference().child(DESIGN_DATA).child(challanNo).child(designNo).setValue(designObject);
    }
}
