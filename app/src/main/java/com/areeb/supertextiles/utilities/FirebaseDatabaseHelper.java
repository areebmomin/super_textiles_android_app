package com.areeb.supertextiles.utilities;

import androidx.annotation.NonNull;

import com.areeb.supertextiles.models.Bill;
import com.areeb.supertextiles.models.Challan;
import com.areeb.supertextiles.models.Design;
import com.areeb.supertextiles.models.Report;
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
    public static final String ADDRESS = "address";
    public static final String CHALLAN_NO = "challan_no";
    public static final String CHALLAN_LIST = "challan_list";
    public static final String QUALITY_LIST = "quality_list";
    public static final String DESIGN_DATA = "design_data";
    public static final String DESIGN_1 = "design_1";
    public static final String DESIGN_2 = "design_2";
    public static final String DESIGN_3 = "design_3";
    public static final String DESIGN_4 = "design_4";
    public static final String DESIGN_COLOR = "design_color";
    public static final String DESIGN_NO = "design_no";
    public static final String METER_LIST = "meterList";
    public static final String BILL_NO = "bill_no";
    public static final String BILL_LIST = "bill_list";
    public static final String REPORT_LIST = "report_list";
    public static final String RECEIVED_AMOUNT = "receivedAmount";
    public static final String CHEQUE_NO = "chequeNo";
    public static final String CHEQUE_DATE = "chequeDate";
    public static final String TRANSPORT = "transport";
    public static final String PURCHASER_LOWER_CASE = "purchaser_lower_case";
    public static final String MESSERS_LOWER_CASE = "messersLowerCase";
    public static final String PARTY_LOWER_CASE = "partyLowerCase";
    public static final String NAME_LOWER_CASE = "nameLowerCase";

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

    //set challan_no in Firebase Database
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

    //get BILL_NO from Firebase Database
    public static DatabaseReference getBillNoDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference().child(BILL_NO);
    }

    //set BILL_NO in Firebase Database
    public static void setBillNoInDatabase(long billNo) {
        FirebaseDatabase.getInstance().getReference().child(BILL_NO).setValue(billNo);
    }

    //set Bill data in BILL_LIST
    public static void setBillDataInBillList(String challanNo, Bill billObject) {
        FirebaseDatabase.getInstance().getReference().child(BILL_LIST).child(challanNo).setValue(billObject);
    }

    //get bill list database reference
    public static DatabaseReference getBillListDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference().child(BILL_LIST);
    }

    //set Report data in REPORT_LIST
    public static void setReportDataInReportList(String billNo, Report reportObject) {
        FirebaseDatabase.getInstance().getReference().child(REPORT_LIST).child(billNo).setValue(reportObject);
    }

    //get report list database reference
    public static DatabaseReference getReportListDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference().child(REPORT_LIST);
    }

    //get quality list database reference
    public static DatabaseReference getQualityListDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference().child(QUALITY_LIST);
    }
}
