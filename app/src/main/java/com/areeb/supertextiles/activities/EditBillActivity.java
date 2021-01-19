package com.areeb.supertextiles.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.areeb.supertextiles.R;
import com.areeb.supertextiles.models.Bill;
import com.areeb.supertextiles.models.Challan;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.areeb.supertextiles.activities.AddBillActivity.calculateDiscount;
import static com.areeb.supertextiles.activities.AddBillActivity.calculateIGST;
import static com.areeb.supertextiles.activities.AddBillActivity.calculateSGSTAndCGST;
import static com.areeb.supertextiles.activities.AddBillActivity.createNewReportDataInDatabase;
import static com.areeb.supertextiles.activities.AddChallanActivity.showErrorInTextField;
import static com.areeb.supertextiles.activities.ViewBillActivity.BILL_OBJECT;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.CHALLAN_NO;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getBillListDatabaseReference;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getChallanListDatabaseReference;

public class EditBillActivity extends AppCompatActivity {

    RadioButton SGST25RadioButton, SGST6RadioButton, CGST25RadioButton, CGST6RadioButton, IGST5RadioButton, IGST12RadioButton;
    RadioGroup SGSTRadioGroupEditBill;
    TextInputLayout billNoEditBillTextField, challanNoEditBillTextField, dateEditBillTextField, messersEditBillTextField,
            addressEditBillTextField, purchaserGSTEditBillTextField, contractNoEditBillTextField, datedEditBillTextField,
            brokerEditBillTextField, eWayBillNoEditBillTextField, descriptionEditBillTextField, noOfPiecesEditBillTextField,
            quantityEditBillTextField, quantityDiscountEditBillTextField, quantityAfterDiscountEditBillTextField,
            rateEditBillTextField, amountEditBillTextField, totalEditBillTextField, discountEditBillTextField,
            discountAmountEditBillTextField, netAmountEditBillTextField, SGSTAmountEditBillTextField,
            CGSTAmountEditBillTextField, IGSTAmountEditBillTextField, amountAfterTaxEditBillTextField;
    String messers, address, purchaserGST, description, deliveryAddress, lotNo;
    AutoCompleteTextView challanNoAutoCompleteTextView;
    ArrayList<String> challanNoList = new ArrayList<>();
    DatabaseReference challanListDatabaseReference;
    TableLayout editBillTableLayout;
    HashMap<String, Challan> challanHashMap;
    ArrayList<String> challanNoSelectedList;
    ArrayAdapter<String> challanNoAdapter;
    ConstraintLayout editBillParent;
    Calendar calendar;
    Bill bill;
    Gson gson;

    //DatePicker dialog listener
    DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
        //set date
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //update date EditText
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Objects.requireNonNull(dateEditBillTextField.getEditText()).setText(simpleDateFormat.format(calendar.getTime()));
    };

    //DatePicker dialog listener for datedTextField
    DatePickerDialog.OnDateSetListener datedSetListener = (view, year, month, dayOfMonth) -> {
        //set date
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //update date EditText
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Objects.requireNonNull(datedEditBillTextField.getEditText()).setText(simpleDateFormat.format(calendar.getTime()));
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Bill");
        setContentView(R.layout.activity_edit_bill);

        //initializing xml views
        SGST25RadioButton = findViewById(R.id.SGST25RadioButton);
        SGST6RadioButton = findViewById(R.id.SGST6RadioButton);
        CGST25RadioButton = findViewById(R.id.CGST25RadioButton);
        CGST6RadioButton = findViewById(R.id.CGST6RadioButton);
        IGST5RadioButton = findViewById(R.id.IGST5RadioButton);
        IGST12RadioButton = findViewById(R.id.IGST12RadioButton);
        billNoEditBillTextField = findViewById(R.id.billNoEditBillTextField);
        challanNoEditBillTextField = findViewById(R.id.challanNoEditBillTextField);
        dateEditBillTextField = findViewById(R.id.dateEditBillTextField);
        messersEditBillTextField = findViewById(R.id.messersEditBillTextField);
        addressEditBillTextField = findViewById(R.id.addressEditBillTextField);
        purchaserGSTEditBillTextField = findViewById(R.id.purchaserGSTEditBillTextField);
        contractNoEditBillTextField = findViewById(R.id.contractNoEditBillTextField);
        datedEditBillTextField = findViewById(R.id.datedEditBillTextField);
        brokerEditBillTextField = findViewById(R.id.brokerEditBillTextField);
        eWayBillNoEditBillTextField = findViewById(R.id.eWayBillNoEditBillTextField);
        descriptionEditBillTextField = findViewById(R.id.descriptionEditBillTextField);
        noOfPiecesEditBillTextField = findViewById(R.id.noOfPiecesEditBillTextField);
        quantityEditBillTextField = findViewById(R.id.quantityEditBillTextField);
        quantityDiscountEditBillTextField = findViewById(R.id.quantityDiscountEditBillTextField);
        quantityAfterDiscountEditBillTextField = findViewById(R.id.quantityAfterDiscountEditBillTextField);
        rateEditBillTextField = findViewById(R.id.rateEditBillTextField);
        amountEditBillTextField = findViewById(R.id.amountEditBillTextField);
        totalEditBillTextField = findViewById(R.id.totalEditBillTextField);
        discountEditBillTextField = findViewById(R.id.discountEditBillTextField);
        discountAmountEditBillTextField = findViewById(R.id.discountAmountEditBillTextField);
        netAmountEditBillTextField = findViewById(R.id.netAmountEditBillTextField);
        SGSTAmountEditBillTextField = findViewById(R.id.SGSTAmountEditBillTextField);
        CGSTAmountEditBillTextField = findViewById(R.id.CGSTAmountEditBillTextField);
        IGSTAmountEditBillTextField = findViewById(R.id.IGSTAmountEditBillTextField);
        amountAfterTaxEditBillTextField = findViewById(R.id.amountAfterTaxEditBillTextField);
        SGSTRadioGroupEditBill = findViewById(R.id.SGSTRadioGroupEditBill);
        challanNoAutoCompleteTextView = (AutoCompleteTextView) challanNoEditBillTextField.getEditText();
        editBillTableLayout = findViewById(R.id.editBillTableLayout);
        editBillParent = findViewById(R.id.editBillParent);

        //initialize challanHashMap
        challanHashMap = new HashMap<>();

        //initialize challanNoList
        challanNoSelectedList = new ArrayList<>();

        //inflate challanNoTextField drop down
        challanNoAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_list_item, challanNoList);

        //initialize strings
        messers = " ";
        address = " ";
        purchaserGST = " ";
        description = " ";
        deliveryAddress = " ";
        lotNo = " ";

        //initialize Bill object
        bill = new Bill();

        //get data from intent and set in EditText's
        if (getIntent() != null) {

            //initialize Gson
            gson = new Gson();

            //get Bill object from intent
            bill = gson.fromJson(getIntent().getStringExtra(BILL_OBJECT), Bill.class);

            //set data to EditText if bill object not null
            if (bill != null) {
                Objects.requireNonNull(billNoEditBillTextField.getEditText()).setText(bill.getBillNo());
                Objects.requireNonNull(dateEditBillTextField.getEditText()).setText(bill.getDate());
                Objects.requireNonNull(messersEditBillTextField.getEditText()).setText(bill.getMessers());
                Objects.requireNonNull(addressEditBillTextField.getEditText()).setText(bill.getAddress());
                Objects.requireNonNull(purchaserGSTEditBillTextField.getEditText()).setText(bill.getPurchaserGst());
                Objects.requireNonNull(contractNoEditBillTextField.getEditText()).setText(bill.getContractNo().trim());
                Objects.requireNonNull(datedEditBillTextField.getEditText()).setText(bill.getDated().trim());
                Objects.requireNonNull(brokerEditBillTextField.getEditText()).setText(bill.getBroker().trim());
                Objects.requireNonNull(eWayBillNoEditBillTextField.getEditText()).setText(bill.geteWayBillNo().trim());
                Objects.requireNonNull(descriptionEditBillTextField.getEditText()).setText(bill.getDescription());
                Objects.requireNonNull(noOfPiecesEditBillTextField.getEditText()).setText(bill.getNoOfPieces());
                Objects.requireNonNull(quantityEditBillTextField.getEditText()).setText(bill.getQuantity());
                Objects.requireNonNull(quantityDiscountEditBillTextField.getEditText()).setText(bill.getDiscountOnQuantity().trim());
                Objects.requireNonNull(quantityAfterDiscountEditBillTextField.getEditText()).setText(bill.getQuantityAfterDiscount().trim());
                Objects.requireNonNull(rateEditBillTextField.getEditText()).setText(bill.getRate().trim());
                Objects.requireNonNull(amountEditBillTextField.getEditText()).setText(bill.getAmount().trim());
                Objects.requireNonNull(totalEditBillTextField.getEditText()).setText(bill.getTotal().trim());
                Objects.requireNonNull(discountEditBillTextField.getEditText()).setText(bill.getDiscount().trim());
                Objects.requireNonNull(discountAmountEditBillTextField.getEditText()).setText(bill.getDiscountAmount().trim());
                Objects.requireNonNull(netAmountEditBillTextField.getEditText()).setText(bill.getNetAmount().trim());
                Objects.requireNonNull(SGSTAmountEditBillTextField.getEditText()).setText(bill.getSGSTAmount().trim());
                Objects.requireNonNull(CGSTAmountEditBillTextField.getEditText()).setText(bill.getCGSTAmount().trim());
                Objects.requireNonNull(IGSTAmountEditBillTextField.getEditText()).setText(bill.getIGSTAmount().trim());
                Objects.requireNonNull(amountAfterTaxEditBillTextField.getEditText()).setText(bill.getAmountAfterTax().trim());

                //set all challan no in table layout
                for (String selectedChallanNo : bill.getChallanNo()) {
                    //add challan number to challanNoSelectedList
                    challanNoSelectedList.add(selectedChallanNo);

                    //add challan number to table
                    addChallanNoToTable(editBillTableLayout, selectedChallanNo, challanNoSelectedList, challanNoAdapter);
                }

                //select correct GST percent according
                if (bill.getIGSTPercent().equals("12")) {
                    IGST12RadioButton.performClick();
                }
            }

            //get all challan data from database
            challanListDatabaseReference = getChallanListDatabaseReference();
            challanListDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //put all challan data in challanArrayList
                    for (DataSnapshot challanObject : snapshot.getChildren()) {
                        //cast database snapshot to Challan object
                        Challan challan = challanObject.getValue(Challan.class);

                        //put challan object on HashMap identified by challan number
                        if (challan != null && challan.getChallan_no() != null)
                            challanHashMap.put(challan.getChallan_no(), challan);

                        //put challanNo in challanNoList
                        challanNoList.add(String.valueOf(challanObject.child(CHALLAN_NO).getValue()));
                    }

                    if (challanNoAutoCompleteTextView != null)
                        challanNoAutoCompleteTextView.setAdapter(challanNoAdapter);

                    //remove selected challan number from challanNoAdapter
                    if (bill != null) {
                        for (String selectedChallanNo : bill.getChallanNo()) {
                            challanNoAdapter.remove(selectedChallanNo);
                            challanNoAdapter.notifyDataSetChanged();
                        }
                    }

                    //set all the default values for bill
                    if (bill != null) {
                        Challan selectedChallan = challanHashMap.get(bill.getChallanNo().get(0));
                        if (selectedChallan != null) {
                            messers = selectedChallan.getPurchaser();
                            address = selectedChallan.getPurchaser_address();
                            purchaserGST = selectedChallan.getPurchaser_gst();
                            description = selectedChallan.getQuality();
                            deliveryAddress = selectedChallan.getDelivery_at();
                            lotNo = selectedChallan.getLot_no();
                        }
                    }

                    challanNoAdapter.setNotifyOnChange(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    error.toException().printStackTrace();
                }
            });
        }

        //set filters for discount percent EditText
        Objects.requireNonNull(discountEditBillTextField.getEditText()).setFilters(new InputFilter[]{new InputFilterMinMax(0, 100)});
    }

    @Override
    protected void onStart() {
        super.onStart();

        //challanNoAutoCompleteTextView onItemClickListener
        challanNoAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            TextView textView =  (TextView) view;
            String selectedChallanNo = String.valueOf(textView.getText());

            //get challan object of selected challan number
            Challan challan = challanHashMap.get(selectedChallanNo);

            //get row count in challan no table
            int rowCount = editBillTableLayout.getChildCount();

            //if statement will be executed when table only contains table heading
            if (rowCount == 1) {
                //set data related to challan in text fields
                if (challan != null) {

                    //add challan number to challanNoSelectedList
                    challanNoSelectedList.add(selectedChallanNo);

                    //remove selected challan number from challanNoAdapter
                    challanNoAdapter.remove(selectedChallanNo);
                    challanNoAdapter.notifyDataSetChanged();

                    //add challan number to table
                    addChallanNoToTable(editBillTableLayout, selectedChallanNo, challanNoSelectedList, challanNoAdapter);

                    //add messers data in text field
                    Objects.requireNonNull(messersEditBillTextField.getEditText()).setText(challan.getPurchaser());

                    //add address in text field
                    Objects.requireNonNull(addressEditBillTextField.getEditText()).setText(challan.getPurchaser_address());

                    //add purchaser GST in text field
                    Objects.requireNonNull(purchaserGSTEditBillTextField.getEditText()).setText(challan.getPurchaser_gst());

                    //add description data in text field
                    Objects.requireNonNull(descriptionEditBillTextField.getEditText()).setText(challan.getQuality());

                    //set no of pieces and quantity
                    setChallanDataInTextFields(challan);

                    //set all the default values for bill
                    messers = challan.getPurchaser();
                    address = challan.getPurchaser_address();
                    purchaserGST = challan.getPurchaser_gst();
                    description = challan.getQuality();
                    deliveryAddress = challan.getDelivery_at();
                    lotNo = challan.getLot_no();
                }
            }
            //else statement will be executed when table contains more than 2 rows
            else if (rowCount > 1) {
                if (challan != null) {
                    if (messers.equals(challan.getPurchaser()) && address.equals(challan.getPurchaser_address()) &&
                            purchaserGST.equals(challan.getPurchaser_gst()) && description.equals(challan.getQuality())) {

                        //add challan number to challanNoSelectedList
                        challanNoSelectedList.add(selectedChallanNo);

                        //remove selected challan number from challanNoAdapter
                        challanNoAdapter.remove(selectedChallanNo);
                        challanNoAdapter.notifyDataSetChanged();

                        //add challan number to table
                        addChallanNoToTable(editBillTableLayout, selectedChallanNo, challanNoSelectedList, challanNoAdapter);

                        //set no of pieces and quantity
                        setChallanDataInTextFields(challan);
                    }
                    else {
                        Toast.makeText(this, "Choose challan of same type", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //quantity edit text onTextChange listener
        Objects.requireNonNull(quantityEditBillTextField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //remove value in discount quantity text field
                Objects.requireNonNull(quantityDiscountEditBillTextField.getEditText()).getText().clear();

                //add quantity in quantity after discount text field
                Objects.requireNonNull(quantityAfterDiscountEditBillTextField.getEditText()).setText(s.toString());

                //set filter on discountOnQuantity
                String totalQuantityString = s.toString();
                if (!totalQuantityString.isEmpty()) {
                    double totalQuantityDouble = Double.parseDouble(totalQuantityString);

                    Objects.requireNonNull(quantityDiscountEditBillTextField.getEditText()).setFilters(new InputFilter[]{new InputFilterMinMax(0, totalQuantityDouble)});
                }
            }
        });

        //discountOnQuantity EditText onTextChange listener
        Objects.requireNonNull(quantityDiscountEditBillTextField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               //initialize variables
                double quantity, discount;
                String quantityAfterDiscount;

                //get quantity from quantity editText
                String quantityString = quantityEditBillTextField.getEditText().getText().toString();

                //calculate quantityAfterDiscount if quantity is not null
                if (!quantityString.isEmpty() && !s.toString().isEmpty()) {
                    quantity = Double.parseDouble(quantityString);
                    discount = Double.parseDouble(s.toString());

                    //calculate quantity after discount value
                    quantityAfterDiscount = new DecimalFormat("##.#").format(quantity - discount);

                    //set remaining quantity to quantityAfterDiscount EditText
                    Objects.requireNonNull(quantityAfterDiscountEditBillTextField.getEditText()).setText(quantityAfterDiscount);
                }
                //set quantity data to quantityAfterDiscount EditText if anything is null
                else if (!quantityString.isEmpty()) {
                    Objects.requireNonNull(quantityAfterDiscountEditBillTextField.getEditText()).setText(quantityString);
                }
            }
        });

        //quantityAfterDiscount EditText onTextChange listener
        Objects.requireNonNull(quantityAfterDiscountEditBillTextField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String rateString = Objects.requireNonNull(rateEditBillTextField.getEditText()).getText().toString();

                if (!rateString.isEmpty()) {
                    //calculate amount, total, netAmount
                    calculateAmount(rateString);
                }
            }
        });

        //rate EditText onTextChange listener
        Objects.requireNonNull(rateEditBillTextField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //calculate amount if rate is not empty
                if (!s.toString().isEmpty()) {
                    //calculate amount, total, netAmount
                    calculateAmount(s.toString());
                }
                //clear amount and total textFields if rate is empty
                else {
                    Objects.requireNonNull(amountEditBillTextField.getEditText()).getText().clear();
                    Objects.requireNonNull(totalEditBillTextField.getEditText()).getText().clear();
                    Objects.requireNonNull(netAmountEditBillTextField.getEditText()).getText().clear();
                    Objects.requireNonNull(SGSTAmountEditBillTextField.getEditText()).getText().clear();
                    Objects.requireNonNull(CGSTAmountEditBillTextField.getEditText()).getText().clear();
                    Objects.requireNonNull(IGSTAmountEditBillTextField.getEditText()).getText().clear();
                    Objects.requireNonNull(amountAfterTaxEditBillTextField.getEditText()).getText().clear();
                    Objects.requireNonNull(discountAmountEditBillTextField.getEditText()).getText().clear();
                }
            }
        });

        //discountEditBillTextField onTextChange listener
        Objects.requireNonNull(discountEditBillTextField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String amountString = Objects.requireNonNull(amountEditBillTextField.getEditText()).getText().toString();

                //check if percent is not empty
                if (!s.toString().isEmpty()) {
                    if (!amountString.isEmpty()) {

                        //calculate discount
                        calculateDiscount(amountString, s.toString(), discountAmountEditBillTextField, netAmountEditBillTextField);

                        //perform click on radio button to calculate GST amount
                        SGST25RadioButton.performClick();
                    }
                    //else statement will be executed if user enters discount percent before rate
                    else {
                        //goto rateEditText
                        showErrorInTextField(rateEditBillTextField, "Rate is required");

                        //clear all the text in discountEditText
                        discountEditBillTextField.getEditText().getText().clear();
                    }
                }

                //else statement will be executed if discountEditText is empty
                else {
                    //set amountEditText value to netAmountEditText and clear discountAmountEditText
                    Objects.requireNonNull(netAmountEditBillTextField.getEditText()).setText(amountString);
                    Objects.requireNonNull(discountAmountEditBillTextField.getEditText()).getText().clear();

                    //perform click on radio button to calculate GST amount
                    SGST25RadioButton.performClick();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_bill_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String title = item.getTitle().toString();

        if (title.equals(getString(R.string.done))){

            //check for empty field in edit bill form
            boolean isAnyFieldEmpty = checkForEmptyField();

            if (!isAnyFieldEmpty) {

                //get data from EditText's
                Bill bill = new Bill();
                bill.setBillNo(Objects.requireNonNull(billNoEditBillTextField.getEditText()).getText().toString());
                bill.setChallanNo(challanNoSelectedList);
                bill.setDate(Objects.requireNonNull(dateEditBillTextField.getEditText()).getText().toString());
                bill.setMessers(Objects.requireNonNull(messersEditBillTextField.getEditText()).getText().toString());
                bill.setMessersLowerCase(Objects.requireNonNull(messersEditBillTextField.getEditText()).getText().toString().toLowerCase());
                bill.setAddress(Objects.requireNonNull(addressEditBillTextField.getEditText()).getText().toString());
                bill.setPurchaserGst(Objects.requireNonNull(purchaserGSTEditBillTextField.getEditText()).getText().toString());
                bill.setContractNo(Objects.requireNonNull(contractNoEditBillTextField.getEditText()).getText().toString());
                bill.setDated(Objects.requireNonNull(datedEditBillTextField.getEditText()).getText().toString());
                bill.setBroker(Objects.requireNonNull(brokerEditBillTextField.getEditText()).getText().toString());
                bill.seteWayBillNo(Objects.requireNonNull(eWayBillNoEditBillTextField.getEditText()).getText().toString());
                bill.setDescription(Objects.requireNonNull(descriptionEditBillTextField.getEditText()).getText().toString());
                bill.setNoOfPieces(Objects.requireNonNull(noOfPiecesEditBillTextField.getEditText()).getText().toString());
                bill.setQuantity(Objects.requireNonNull(quantityEditBillTextField.getEditText()).getText().toString());
                bill.setDiscountOnQuantity(Objects.requireNonNull(quantityDiscountEditBillTextField.getEditText()).getText().toString());
                bill.setQuantityAfterDiscount(Objects.requireNonNull(quantityAfterDiscountEditBillTextField.getEditText()).getText().toString());
                bill.setRate(Objects.requireNonNull(rateEditBillTextField.getEditText()).getText().toString());
                bill.setAmount(Objects.requireNonNull(amountEditBillTextField.getEditText()).getText().toString());
                bill.setTotal(Objects.requireNonNull(totalEditBillTextField.getEditText()).getText().toString());
                bill.setDiscount(Objects.requireNonNull(discountEditBillTextField.getEditText()).getText().toString());
                bill.setDiscountAmount(Objects.requireNonNull(discountAmountEditBillTextField.getEditText()).getText().toString());
                bill.setNetAmount(Objects.requireNonNull(netAmountEditBillTextField.getEditText()).getText().toString());
                bill.setSGSTPercent(SGST25RadioButton.isChecked() ? "2.5" : "6");
                bill.setCGSTPercent(CGST25RadioButton.isChecked() ? "2.5" : "6");
                bill.setIGSTPercent(IGST5RadioButton.isChecked() ? "5" : "12");
                bill.setSGSTAmount(Objects.requireNonNull(SGSTAmountEditBillTextField.getEditText()).getText().toString());
                bill.setCGSTAmount(Objects.requireNonNull(CGSTAmountEditBillTextField.getEditText()).getText().toString());
                bill.setIGSTAmount(Objects.requireNonNull(IGSTAmountEditBillTextField.getEditText()).getText().toString());
                String amountAfterTaxString = Objects.requireNonNull(amountAfterTaxEditBillTextField.getEditText()).getText().toString();
                if (!amountAfterTaxString.isEmpty()) {
                    double amountAfterTaxDouble = Double.parseDouble(amountAfterTaxString);
                    amountAfterTaxString = String.valueOf(Math.round(amountAfterTaxDouble));
                }
                bill.setAmountAfterTax(amountAfterTaxString);

                //add bill object in database
                getBillListDatabaseReference().child(bill.getBillNo()).setValue(bill);

                //get all challan no list from challan object
                HashSet<String> designNoHashSet = new HashSet<>();
                for (String challanNo : bill.getChallanNo()) {
                    Challan challan = challanHashMap.get(challanNo);

                    if (challan != null) {
                        designNoHashSet.addAll(challan.getDesign_no_list());
                    }
                }

                //set designNoHashSet in string
                StringBuilder designNoListString = new StringBuilder();
                for (String designNo : designNoHashSet) {
                    designNoListString.append(designNo).append(", ");
                }

                //add report object in database
                createNewReportDataInDatabase(bill, lotNo, deliveryAddress, designNoListString.toString());

                Toast.makeText(this, "Bill Edited", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //method to calculate amount, total, netAmount
    private void calculateAmount( String s) {
        double quantityAfterDiscount, rate;
        String amount;

        //remove previous error
        rateEditBillTextField.setErrorEnabled(false);

        //get quantityAfterDiscount and rate from EditTexts
        String quantityAfterDiscountString = Objects.requireNonNull(quantityAfterDiscountEditBillTextField.getEditText()).getText().toString();
        if (!quantityAfterDiscountString.isEmpty()) {
            quantityAfterDiscount = Double.parseDouble(quantityAfterDiscountString);
            rate = Double.parseDouble(s);

            //calculate amount
            amount = String.valueOf(quantityAfterDiscount * rate);

            //set amount in amountEditText, totalEditText, netAmountEditText
            Objects.requireNonNull(amountEditBillTextField.getEditText()).setText(amount);
            Objects.requireNonNull(totalEditBillTextField.getEditText()).setText(amount);

            //get discount percent from discount EditText
            String discountPercent = Objects.requireNonNull(discountEditBillTextField.getEditText()).getText().toString();
            //calculate discount if discountPercent is not empty
            if (!discountPercent.isEmpty()) {
                calculateDiscount(amount, discountPercent, discountAmountEditBillTextField, netAmountEditBillTextField);
            }
            //else statement will be executed if discount percent is empty
            else {
                Objects.requireNonNull(netAmountEditBillTextField.getEditText()).setText(amount);
            }

            //perform click on radio button to calculate GST amount
            SGST25RadioButton.performClick();
        }
    }

    //RadioButton onClick method to set GST 2.5% twice or 5% once
    public void setGST25Or5(View view) {

        //select all the RadioButton with 2.5% and 5% value
        SGST25RadioButton.setChecked(true);
        CGST25RadioButton.setChecked(true);
        IGST5RadioButton.setChecked(true);

        //identify GST number
        //if GST number starts with 27 calculate SGST and CGST else calculate IGST
        String purchaserGST = Objects.requireNonNull(purchaserGSTEditBillTextField.getEditText()).getText().toString();
        if (!purchaserGST.isEmpty()) {
            if (purchaserGST.trim().startsWith("27")) {
                //calculate SGST and CGST
                calculateSGSTAndCGST(2.5, SGSTAmountEditBillTextField, CGSTAmountEditBillTextField, netAmountEditBillTextField, amountAfterTaxEditBillTextField);

                //set dash character in IGST EditText
                Objects.requireNonNull(IGSTAmountEditBillTextField.getEditText()).setText("-");
            }
            else {
                //calculate IGST
                calculateIGST(5, IGSTAmountEditBillTextField, netAmountEditBillTextField, amountAfterTaxEditBillTextField);

                //set dash character in SGST and CGST
                Objects.requireNonNull(SGSTAmountEditBillTextField.getEditText()).setText("-");
                Objects.requireNonNull(CGSTAmountEditBillTextField.getEditText()).setText("-");
            }
        }
    }

    //RadioButton onCLick method to set GST 6% twice or 12% once
    public void setGST6Or12(View view) {

        //select all the RadioButton with 6% and 12% value
        SGST6RadioButton.setChecked(true);
        CGST6RadioButton.setChecked(true);
        IGST12RadioButton.setChecked(true);

        //identify GST number
        //if GST number starts with 27 calculate SGST and CGST else calculate IGST
        String purchaserGST = Objects.requireNonNull(purchaserGSTEditBillTextField.getEditText()).getText().toString();
        if (!purchaserGST.isEmpty()) {
            if (purchaserGST.trim().startsWith("27")) {
                //calculate SGST and CGST
                calculateSGSTAndCGST(6, SGSTAmountEditBillTextField, CGSTAmountEditBillTextField, netAmountEditBillTextField, amountAfterTaxEditBillTextField);

                //set dash character in IGST EditText
                Objects.requireNonNull(IGSTAmountEditBillTextField.getEditText()).setText("-");
            }
            else {
                //calculate IGST
                calculateIGST(12, IGSTAmountEditBillTextField, netAmountEditBillTextField, amountAfterTaxEditBillTextField);

                //set dash character in SGST and CGST
                Objects.requireNonNull(SGSTAmountEditBillTextField.getEditText()).setText("-");
                Objects.requireNonNull(CGSTAmountEditBillTextField.getEditText()).setText("-");
            }
        }
    }

    public void selectDate(View view) {
        //initialize calender
        calendar = Calendar.getInstance();

        //create DatePicker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditBillActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setButton(BUTTON_POSITIVE, "Set", datePickerDialog);
        datePickerDialog.show();
    }

    //datedTextField onClickListener
    public void selectDated(View view) {
        //initialize calender
        calendar = Calendar.getInstance();

        //create DatePicker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditBillActivity.this, datedSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setButton(BUTTON_POSITIVE, "Set", datePickerDialog);
        datePickerDialog.show();
    }

    //method to check for empty field in edit bill form
    //return true if any mandatory field is found empty
    //return false if all mandatory fields are filled
    private boolean checkForEmptyField() {

        //remove all the previous errors
        billNoEditBillTextField.setErrorEnabled(false);
        challanNoEditBillTextField.setErrorEnabled(false);
        dateEditBillTextField.setErrorEnabled(false);
        messersEditBillTextField.setErrorEnabled(false);
        addressEditBillTextField.setErrorEnabled(false);
        purchaserGSTEditBillTextField.setErrorEnabled(false);
        descriptionEditBillTextField.setErrorEnabled(false);
        noOfPiecesEditBillTextField.setErrorEnabled(false);
        quantityEditBillTextField.setErrorEnabled(false);
        quantityAfterDiscountEditBillTextField.setErrorEnabled(false);
        rateEditBillTextField.setErrorEnabled(false);
        amountEditBillTextField.setErrorEnabled(false);
        totalEditBillTextField.setErrorEnabled(false);
        netAmountEditBillTextField.setErrorEnabled(false);
        SGSTAmountEditBillTextField.setErrorEnabled(false);
        CGSTAmountEditBillTextField.setErrorEnabled(false);
        IGSTAmountEditBillTextField.setErrorEnabled(false);
        amountAfterTaxEditBillTextField.setErrorEnabled(false);

        String data = Objects.requireNonNull(billNoEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(billNoEditBillTextField, "Bill No. required");
            return true;
        }

        //check for empty challan no table
        if (editBillTableLayout.getChildCount() < 2) {
            showErrorInTextField(challanNoEditBillTextField, "Challan No. required");
            return true;
        }

        data = Objects.requireNonNull(dateEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(dateEditBillTextField, "Date required");
            return true;
        }

        data = Objects.requireNonNull(messersEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(messersEditBillTextField, "Messers required");
            return true;
        }

        data = Objects.requireNonNull(addressEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(addressEditBillTextField, "Address required");
            return true;
        }

        data = Objects.requireNonNull(purchaserGSTEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(purchaserGSTEditBillTextField, "Purchaser GST required");
            return true;
        }

        data = Objects.requireNonNull(descriptionEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(descriptionEditBillTextField, "Description required");
            return true;
        }

        data = Objects.requireNonNull(noOfPiecesEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(noOfPiecesEditBillTextField, "No. of Pieces required");
            return true;
        }

        data = Objects.requireNonNull(quantityEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(quantityEditBillTextField, "Quantity required");
            return true;
        }

        data = Objects.requireNonNull(quantityAfterDiscountEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(quantityAfterDiscountEditBillTextField, "Quantity required");
            return true;
        }

        data = Objects.requireNonNull(rateEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(rateEditBillTextField, "Rate required");
            return true;
        }

        data = Objects.requireNonNull(amountEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(amountEditBillTextField, "Amount required");
            return true;
        }

        data = Objects.requireNonNull(totalEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(totalEditBillTextField, "Total required");
            return true;
        }

        data = Objects.requireNonNull(netAmountEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(netAmountEditBillTextField, "Net Amount required");
            return true;
        }

        data = Objects.requireNonNull(SGSTAmountEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(SGSTAmountEditBillTextField, "SGST required");
            return true;
        }

        data = Objects.requireNonNull(CGSTAmountEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(CGSTAmountEditBillTextField, "CGST required");
            return true;
        }

        data = Objects.requireNonNull(IGSTAmountEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(IGSTAmountEditBillTextField, "IGST required");
            return true;
        }

        data = Objects.requireNonNull(amountAfterTaxEditBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(amountAfterTaxEditBillTextField, "Amount required");
            return true;
        }

        return false;
    }

    //add challan number to table
    @SuppressLint("ClickableViewAccessibility")
    private void addChallanNoToTable(TableLayout tableLayout, String challanNo, ArrayList<String> challanNoSelectedList, ArrayAdapter<String> challanNoAdapter) {
        //inflate row
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.add_challan_table_row, editBillParent, false);

        //initialize TableRow text views
        TextView SNoTableColumn = tableRow.findViewById(R.id.SNoTableColumn);
        TextView challanNoTableColumn = tableRow.findViewById(R.id.challanNoTableColumn);

        //add data to table row
        SNoTableColumn.setText(String.valueOf(tableLayout.getChildCount()));
        challanNoTableColumn.setText(challanNo);

        //delete row onClick
        challanNoTableColumn.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= challanNoTableColumn.getRight() - challanNoTableColumn.getTotalPaddingRight()) {
                    //get TableRow to be removed
                    TableRow tableRow1 = (TableRow) challanNoTableColumn.getParent();

                    //get challanNo from TableRow
                    String challanNoToBeRemoved = challanNoTableColumn.getText().toString();

                    //remove challanNo from challanNoSelectedList
                    challanNoSelectedList.remove(challanNoToBeRemoved);

                    //add deleted challanNo in challanNoAdapter
                    challanNoAdapter.add(challanNoToBeRemoved);
                    challanNoAdapter.notifyDataSetChanged();

                    //remove TableRow from TableLayout
                    tableLayout.removeView(tableRow1);

                    //update text fields accordingly
                    updateTextFieldAfterChallanRemoved(challanNoToBeRemoved);
                }
            }

            return true;
        });

        //add row in table
        tableLayout.addView(tableRow);
    }

    //method to update TextFields after challan is removed from challan no list
    private void updateTextFieldAfterChallanRemoved(String challanNoToBeRemoved) {
        //get child count of challan no table
        int childCount = editBillTableLayout.getChildCount();

        //if statement will be executed when only table is left in table
        if (childCount == 1) {
            //clear all text fields
            Objects.requireNonNull(messersEditBillTextField.getEditText()).getText().clear();
            Objects.requireNonNull(addressEditBillTextField.getEditText()).getText().clear();
            Objects.requireNonNull(purchaserGSTEditBillTextField.getEditText()).getText().clear();
            Objects.requireNonNull(descriptionEditBillTextField.getEditText()).getText().clear();
            Objects.requireNonNull(noOfPiecesEditBillTextField.getEditText()).getText().clear();
            Objects.requireNonNull(quantityEditBillTextField.getEditText()).getText().clear();
            Objects.requireNonNull(rateEditBillTextField.getEditText()).getText().clear();
            Objects.requireNonNull(discountEditBillTextField.getEditText()).getText().clear();

            //empty all the default strings
            messers = " ";
            address = " ";
            purchaserGST = " ";
            description = " ";
        }
        //else statement will be executed when more than one child is in the table
        else if (childCount > 1) {
            Challan challanObject = challanHashMap.get(challanNoToBeRemoved);

            //for number of pieces text field
            //get previous total pieces and subtract with current total pieces
            String previousTotalPiecesString = Objects.requireNonNull(noOfPiecesEditBillTextField.getEditText()).getText().toString();

            if (challanObject != null) {
                int previousTotalPiecesInt = Integer.parseInt(previousTotalPiecesString);
                int currentTotalPiecesInt = Integer.parseInt(challanObject.getTotal_pieces());
                int remainingPieces = Math.abs(previousTotalPiecesInt - currentTotalPiecesInt);

                //add number of pieces data in text field after adding current pieces
                Objects.requireNonNull(noOfPiecesEditBillTextField.getEditText()).setText(String.valueOf(remainingPieces));
            }

            //for total quantity text field
            //get previous quantity and subtract with current quantity
            String previousQuantityString = Objects.requireNonNull(quantityEditBillTextField.getEditText()).getText().toString();

            if (challanObject != null) {
                double previousQuantity = Double.parseDouble(previousQuantityString);
                double currentQuantity = Double.parseDouble(challanObject.getTotal_meters());
                double remainingQuantity = Math.abs(previousQuantity - currentQuantity);

                //add quantity data in text field after adding current quantity
                Objects.requireNonNull(quantityEditBillTextField.getEditText()).setText(String.valueOf(remainingQuantity));
            }
        }
    }

    //method to put required challan data to TextFields in bill form
    private void setChallanDataInTextFields(@NonNull Challan challan) {

        //for number of pieces text field
        //get previous total pieces and add with current total pieces
        String previousTotalPiecesString = Objects.requireNonNull(noOfPiecesEditBillTextField.getEditText()).getText().toString();

        //add number of pieces data in text field if text field is empty
        if (previousTotalPiecesString.isEmpty()) {
            Objects.requireNonNull(noOfPiecesEditBillTextField.getEditText()).setText(challan.getTotal_pieces());
        }
        else {
            int previousTotalPiecesInt = Integer.parseInt(previousTotalPiecesString);
            int currentTotalPiecesInt = Integer.parseInt(challan.getTotal_pieces());

            //add number of pieces data in text field after adding current pieces
            Objects.requireNonNull(noOfPiecesEditBillTextField.getEditText()).setText(String.valueOf(previousTotalPiecesInt + currentTotalPiecesInt));
        }

        //for total quantity text field
        //get previous quantity and add with current quantity
        String previousQuantityString = Objects.requireNonNull(quantityEditBillTextField.getEditText()).getText().toString();

        //add quantity data in text field if text field is empty
        if (previousQuantityString.isEmpty()) {
            Objects.requireNonNull(quantityEditBillTextField.getEditText()).setText(challan.getTotal_meters());
        }
        else {
            double previousQuantity = Double.parseDouble(previousQuantityString);
            double currentQuantity = Double.parseDouble(challan.getTotal_meters());

            //add quantity data in text field after adding current quantity
            Objects.requireNonNull(quantityEditBillTextField.getEditText()).setText(String.valueOf(previousQuantity + currentQuantity));
        }
    }
}

class InputFilterMinMax implements InputFilter{

    private final double min;
    private final double max;

    public  InputFilterMinMax(String min, String max) {
        this.min = Double.parseDouble(min);
        this.max = Double.parseDouble(max);
    }

    public InputFilterMinMax(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {
        try {
            // Remove the string out of destination that is to be replaced
            String newVal = dest.toString().substring(0, dStart) + dest.toString().substring(dEnd);
            // Add the new string in
            newVal = newVal.substring(0, dStart) + source.toString() + newVal.substring(dStart);
            double input = Double.parseDouble(newVal);
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException ignored) { }
        return "";
    }

    private boolean isInRange(double a, double b, double c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}