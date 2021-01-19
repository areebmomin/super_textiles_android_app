package com.areeb.supertextiles.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
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
import com.areeb.supertextiles.models.Report;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.areeb.supertextiles.activities.AddChallanActivity.showErrorInTextField;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.CHALLAN_NO;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getBillListDatabaseReference;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getBillNoDatabaseReference;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getChallanListDatabaseReference;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.setBillNoInDatabase;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.setReportDataInReportList;

public class AddBillActivity extends AppCompatActivity {

    RadioButton SGST25RadioButton, SGST6RadioButton, CGST25RadioButton, CGST6RadioButton, IGST5RadioButton, IGST12RadioButton;
    TextInputLayout billNoAddBillTextField, challanNoAddBillTextField, dateAddBillTextField, messersAddBillTextField,
            addressAddBillTextField, purchaserGSTAddBillTextField, contractNoAddBillTextField, datedAddBillTextField,
            brokerAddBillTextField, eWayBillNoAddBillTextField, descriptionAddBillTextField, noOfPiecesAddBillTextField,
            quantityAddBillTextField, quantityDiscountAddBillTextField, quantityAfterDiscountAddBillTextField,
            rateAddBillTextField, amountAddBillTextField, totalAddBillTextField, discountAddBillTextField,
            discountAmountAddBillTextField, netAmountAddBillTextField, SGSTAmountAddBillTextField,
            CGSTAmountAddBillTextField, IGSTAmountAddBillTextField, amountAfterTaxAddBillTextField;
    Calendar calendar;
    DatabaseReference billNoDatabaseReference;
    DatabaseReference challanListDatabaseReference;
    long billNumber;
    HashMap<String, Challan> challanHashMap;
    AutoCompleteTextView challanNoAutoCompleteTextView;
    ArrayList<String> challanNoList = new ArrayList<>();
    ArrayList<String> challanNoSelectedList;
    ConstraintLayout addBillParent;
    TableLayout addBillTableLayout;
    ArrayAdapter<String> challanNoAdapter;
    String messers, address, purchaserGST, description, deliveryAddress, lotNo;

    //DatePicker dialog listener for dateTextField
    DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
        //set date
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //update date EditText
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Objects.requireNonNull(dateAddBillTextField.getEditText()).setText(simpleDateFormat.format(calendar.getTime()));
    };

    //DatePicker dialog listener for datedTextField
    DatePickerDialog.OnDateSetListener datedSetListener = (view, year, month, dayOfMonth) -> {
        //set date
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //update date EditText
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Objects.requireNonNull(datedAddBillTextField.getEditText()).setText(simpleDateFormat.format(calendar.getTime()));
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Bill");
        setContentView(R.layout.activity_add_bill);

        //initialize xml views
        SGST25RadioButton = findViewById(R.id.SGST25RadioButton);
        SGST6RadioButton = findViewById(R.id.SGST6RadioButton);
        CGST25RadioButton = findViewById(R.id.CGST25RadioButton);
        CGST6RadioButton = findViewById(R.id.CGST6RadioButton);
        IGST5RadioButton = findViewById(R.id.IGST5RadioButton);
        IGST12RadioButton = findViewById(R.id.IGST12RadioButton);
        billNoAddBillTextField = findViewById(R.id.billNoAddBillTextField);
        challanNoAddBillTextField = findViewById(R.id.challanNoAddBillTextField);
        dateAddBillTextField = findViewById(R.id.dateAddBillTextField);
        messersAddBillTextField = findViewById(R.id.messersAddBillTextField);
        addressAddBillTextField = findViewById(R.id.addressAddBillTextField);
        purchaserGSTAddBillTextField = findViewById(R.id.purchaserGSTAddBillTextField);
        contractNoAddBillTextField = findViewById(R.id.contractNoAddBillTextField);
        datedAddBillTextField = findViewById(R.id.datedAddBillTextField);
        brokerAddBillTextField = findViewById(R.id.brokerAddBillTextField);
        eWayBillNoAddBillTextField = findViewById(R.id.eWayBillNoAddBillTextField);
        descriptionAddBillTextField = findViewById(R.id.descriptionAddBillTextField);
        noOfPiecesAddBillTextField = findViewById(R.id.noOfPiecesAddBillTextField);
        quantityAddBillTextField = findViewById(R.id.quantityAddBillTextField);
        quantityDiscountAddBillTextField = findViewById(R.id.quantityDiscountAddBillTextField);
        quantityAfterDiscountAddBillTextField = findViewById(R.id.quantityAfterDiscountAddBillTextField);
        rateAddBillTextField = findViewById(R.id.rateAddBillTextField);
        amountAddBillTextField = findViewById(R.id.amountAddBillTextField);
        totalAddBillTextField = findViewById(R.id.totalAddBillTextField);
        discountAddBillTextField = findViewById(R.id.discountAddBillTextField);
        discountAmountAddBillTextField = findViewById(R.id.discountAmountAddBillTextField);
        netAmountAddBillTextField = findViewById(R.id.netAmountAddBillTextField);
        SGSTAmountAddBillTextField = findViewById(R.id.SGSTAmountAddBillTextField);
        CGSTAmountAddBillTextField = findViewById(R.id.CGSTAmountAddBillTextField);
        IGSTAmountAddBillTextField = findViewById(R.id.IGSTAmountAddBillTextField);
        amountAfterTaxAddBillTextField = findViewById(R.id.amountAfterTaxAddBillTextField);
        challanNoAutoCompleteTextView = (AutoCompleteTextView) challanNoAddBillTextField.getEditText();
        addBillTableLayout = findViewById(R.id.addBillTableLayout);
        addBillParent = findViewById(R.id.addBillParent);

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

        //fetch bill number from database when new challan is created
        //get BILL_NO
        billNoDatabaseReference = getBillNoDatabaseReference();
        billNoDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get bill number
                billNumber = Long.parseLong(String.valueOf(snapshot.getValue()));

                //set bill no to billNoEditText
                Objects.requireNonNull(billNoAddBillTextField.getEditText()).setText(String.valueOf(billNumber));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });

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

                challanNoAdapter.setNotifyOnChange(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });

        //set filters for discount percent EditText
        Objects.requireNonNull(discountAddBillTextField.getEditText()).setFilters(new InputFilter[]{new InputFilterMinMax(0, 100)});
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
            int rowCount = addBillTableLayout.getChildCount();

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
                    addChallanNoToTable(addBillTableLayout, selectedChallanNo, challanNoSelectedList, challanNoAdapter);

                    //add messers data in text field
                    Objects.requireNonNull(messersAddBillTextField.getEditText()).setText(challan.getPurchaser());

                    //add address in text field
                    Objects.requireNonNull(addressAddBillTextField.getEditText()).setText(challan.getPurchaser_address());

                    //add purchaser GST in text field
                    Objects.requireNonNull(purchaserGSTAddBillTextField.getEditText()).setText(challan.getPurchaser_gst());

                    //add description data in text field
                    Objects.requireNonNull(descriptionAddBillTextField.getEditText()).setText(challan.getQuality());

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
                        addChallanNoToTable(addBillTableLayout, selectedChallanNo, challanNoSelectedList, challanNoAdapter);

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
        Objects.requireNonNull(quantityAddBillTextField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //remove value in discount quantity text field
                Objects.requireNonNull(quantityDiscountAddBillTextField.getEditText()).getText().clear();

                //add quantity in quantity after discount text field
                Objects.requireNonNull(quantityAfterDiscountAddBillTextField.getEditText()).setText(s.toString());

                //set filter on discountOnQuantity
                String totalQuantityString = s.toString();
                if (!totalQuantityString.isEmpty()) {
                    double totalQuantityDouble = Double.parseDouble(totalQuantityString);

                    Objects.requireNonNull(quantityDiscountAddBillTextField.getEditText()).setFilters(new InputFilter[]{new InputFilterMinMax(0, totalQuantityDouble)});
                }
            }
        });

        //discountOnQuantity EditText onTextChange listener
        Objects.requireNonNull(quantityDiscountAddBillTextField.getEditText()).addTextChangedListener(new TextWatcher() {
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
                String quantityString = quantityAddBillTextField.getEditText().getText().toString();

                //calculate quantityAfterDiscount if quantity is not null
                if (!quantityString.isEmpty() && !s.toString().isEmpty()) {
                    quantity = Double.parseDouble(quantityString);
                    discount = Double.parseDouble(s.toString());

                    //calculate quantity after discount value
                    quantityAfterDiscount = new DecimalFormat("##.#").format(quantity - discount);

                    //set remaining quantity to quantityAfterDiscount EditText
                    Objects.requireNonNull(quantityAfterDiscountAddBillTextField.getEditText()).setText(quantityAfterDiscount);
                }
                //set quantity data to quantityAfterDiscount EditText if anything is null
                else if (!quantityString.isEmpty()) {
                    Objects.requireNonNull(quantityAfterDiscountAddBillTextField.getEditText()).setText(quantityString);
                }
            }
        });

        //quantityAfterDiscount EditText onTextChange listener
        Objects.requireNonNull(quantityAfterDiscountAddBillTextField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String rateString = Objects.requireNonNull(rateAddBillTextField.getEditText()).getText().toString();

                if (!rateString.isEmpty()) {
                    //calculate amount, total, netAmount
                    calculateAmount(rateString);
                }
            }
        });

        //rate EditText onTextChange listener
        Objects.requireNonNull(rateAddBillTextField.getEditText()).addTextChangedListener(new TextWatcher() {
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
                    Objects.requireNonNull(amountAddBillTextField.getEditText()).getText().clear();
                    Objects.requireNonNull(totalAddBillTextField.getEditText()).getText().clear();
                    Objects.requireNonNull(netAmountAddBillTextField.getEditText()).getText().clear();
                    Objects.requireNonNull(SGSTAmountAddBillTextField.getEditText()).getText().clear();
                    Objects.requireNonNull(CGSTAmountAddBillTextField.getEditText()).getText().clear();
                    Objects.requireNonNull(IGSTAmountAddBillTextField.getEditText()).getText().clear();
                    Objects.requireNonNull(amountAfterTaxAddBillTextField.getEditText()).getText().clear();
                    Objects.requireNonNull(discountAmountAddBillTextField.getEditText()).getText().clear();
                }
            }
        });

        //discountEditBillTextField onTextChange listener
        Objects.requireNonNull(discountAddBillTextField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String amountString = Objects.requireNonNull(amountAddBillTextField.getEditText()).getText().toString();

                //check if percent is not empty
                if (!s.toString().isEmpty()) {
                    if (!amountString.isEmpty()) {

                        //calculate discount
                        calculateDiscount(amountString, s.toString(), discountAmountAddBillTextField, netAmountAddBillTextField);

                        //perform click on radio button to calculate GST amount
                        SGST25RadioButton.performClick();
                    }
                    //else statement will be executed if user enters discount percent before rate
                    else {
                        //goto rateEditText
                        showErrorInTextField(rateAddBillTextField, "Rate is required");

                        //clear all the text in discountEditText
                        discountAddBillTextField.getEditText().getText().clear();
                    }
                }

                //else statement will be executed if discountEditText is empty
                else {
                    //set amountEditText value to netAmountEditText and clear discountAmountEditText
                    Objects.requireNonNull(netAmountAddBillTextField.getEditText()).setText(amountString);
                    Objects.requireNonNull(discountAmountAddBillTextField.getEditText()).getText().clear();

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
                bill.setBillNo(Objects.requireNonNull(billNoAddBillTextField.getEditText()).getText().toString());
                bill.setChallanNo(challanNoSelectedList);
                bill.setDate(Objects.requireNonNull(dateAddBillTextField.getEditText()).getText().toString());
                bill.setMessers(Objects.requireNonNull(messersAddBillTextField.getEditText()).getText().toString());
                bill.setMessersLowerCase(Objects.requireNonNull(messersAddBillTextField.getEditText()).getText().toString().toLowerCase());
                bill.setAddress(Objects.requireNonNull(addressAddBillTextField.getEditText()).getText().toString());
                bill.setPurchaserGst(Objects.requireNonNull(purchaserGSTAddBillTextField.getEditText()).getText().toString());
                bill.setContractNo(Objects.requireNonNull(contractNoAddBillTextField.getEditText()).getText().toString());
                bill.setDated(Objects.requireNonNull(datedAddBillTextField.getEditText()).getText().toString());
                bill.setBroker(Objects.requireNonNull(brokerAddBillTextField.getEditText()).getText().toString());
                bill.seteWayBillNo(Objects.requireNonNull(eWayBillNoAddBillTextField.getEditText()).getText().toString());
                bill.setDescription(Objects.requireNonNull(descriptionAddBillTextField.getEditText()).getText().toString());
                bill.setNoOfPieces(Objects.requireNonNull(noOfPiecesAddBillTextField.getEditText()).getText().toString());
                bill.setQuantity(Objects.requireNonNull(quantityAddBillTextField.getEditText()).getText().toString());
                bill.setDiscountOnQuantity(Objects.requireNonNull(quantityDiscountAddBillTextField.getEditText()).getText().toString());
                bill.setQuantityAfterDiscount(Objects.requireNonNull(quantityAfterDiscountAddBillTextField.getEditText()).getText().toString());
                bill.setRate(Objects.requireNonNull(rateAddBillTextField.getEditText()).getText().toString());
                bill.setAmount(Objects.requireNonNull(amountAddBillTextField.getEditText()).getText().toString());
                bill.setTotal(Objects.requireNonNull(totalAddBillTextField.getEditText()).getText().toString());
                bill.setDiscount(Objects.requireNonNull(discountAddBillTextField.getEditText()).getText().toString());
                bill.setDiscountAmount(Objects.requireNonNull(discountAmountAddBillTextField.getEditText()).getText().toString());
                bill.setNetAmount(Objects.requireNonNull(netAmountAddBillTextField.getEditText()).getText().toString());
                bill.setSGSTPercent(SGST25RadioButton.isChecked() ? "2.5" : "6");
                bill.setCGSTPercent(CGST25RadioButton.isChecked() ? "2.5" : "6");
                bill.setIGSTPercent(IGST5RadioButton.isChecked() ? "5" : "12");
                bill.setSGSTAmount(Objects.requireNonNull(SGSTAmountAddBillTextField.getEditText()).getText().toString());
                bill.setCGSTAmount(Objects.requireNonNull(CGSTAmountAddBillTextField.getEditText()).getText().toString());
                bill.setIGSTAmount(Objects.requireNonNull(IGSTAmountAddBillTextField.getEditText()).getText().toString());
                String amountAfterTaxString = Objects.requireNonNull(amountAfterTaxAddBillTextField.getEditText()).getText().toString();
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

                //increase bill no in database
                setBillNoInDatabase(billNumber + 1);

                Toast.makeText(this, "Bill Added", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //method to check for empty field in edit bill form
    //return true if any mandatory field is found empty
    //return false if all mandatory fields are filled
    private boolean checkForEmptyField() {

        //remove all the previous errors
        billNoAddBillTextField.setErrorEnabled(false);
        dateAddBillTextField.setErrorEnabled(false);
        messersAddBillTextField.setErrorEnabled(false);
        addressAddBillTextField.setErrorEnabled(false);
        purchaserGSTAddBillTextField.setErrorEnabled(false);
        descriptionAddBillTextField.setErrorEnabled(false);
        noOfPiecesAddBillTextField.setErrorEnabled(false);
        quantityAddBillTextField.setErrorEnabled(false);
        quantityAfterDiscountAddBillTextField.setErrorEnabled(false);
        rateAddBillTextField.setErrorEnabled(false);
        amountAddBillTextField.setErrorEnabled(false);
        totalAddBillTextField.setErrorEnabled(false);
        netAmountAddBillTextField.setErrorEnabled(false);
        SGSTAmountAddBillTextField.setErrorEnabled(false);
        CGSTAmountAddBillTextField.setErrorEnabled(false);
        IGSTAmountAddBillTextField.setErrorEnabled(false);
        amountAfterTaxAddBillTextField.setErrorEnabled(false);

        String data = Objects.requireNonNull(billNoAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(billNoAddBillTextField, "Bill No. required");
            return true;
        }

        //check for empty challan no table
        if (addBillTableLayout.getChildCount() < 2) {
            showErrorInTextField(challanNoAddBillTextField, "Challan No. required");
            return true;
        }

        data = Objects.requireNonNull(dateAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(dateAddBillTextField, "Date required");
            return true;
        }

        data = Objects.requireNonNull(messersAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(messersAddBillTextField, "Messers required");
            return true;
        }

        data = Objects.requireNonNull(addressAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(addressAddBillTextField, "Address required");
            return true;
        }

        data = Objects.requireNonNull(purchaserGSTAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(purchaserGSTAddBillTextField, "Purchaser GST required");
            return true;
        }

        data = Objects.requireNonNull(descriptionAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(descriptionAddBillTextField, "Description required");
            return true;
        }

        data = Objects.requireNonNull(noOfPiecesAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(noOfPiecesAddBillTextField, "No. of Pieces required");
            return true;
        }

        data = Objects.requireNonNull(quantityAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(quantityAddBillTextField, "Quantity required");
            return true;
        }

        data = Objects.requireNonNull(quantityAfterDiscountAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(quantityAfterDiscountAddBillTextField, "Quantity required");
            return true;
        }

        data = Objects.requireNonNull(rateAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(rateAddBillTextField, "Rate required");
            return true;
        }

        data = Objects.requireNonNull(amountAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(amountAddBillTextField, "Amount required");
            return true;
        }

        data = Objects.requireNonNull(totalAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(totalAddBillTextField, "Total required");
            return true;
        }

        data = Objects.requireNonNull(netAmountAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(netAmountAddBillTextField, "Net Amount required");
            return true;
        }

        data = Objects.requireNonNull(SGSTAmountAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(SGSTAmountAddBillTextField, "SGST required");
            return true;
        }

        data = Objects.requireNonNull(CGSTAmountAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(CGSTAmountAddBillTextField, "CGST required");
            return true;
        }

        data = Objects.requireNonNull(IGSTAmountAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(IGSTAmountAddBillTextField, "IGST required");
            return true;
        }

        data = Objects.requireNonNull(amountAfterTaxAddBillTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(amountAfterTaxAddBillTextField, "Amount required");
            return true;
        }

        return false;
    }

    //dateTextField onClickListener
    public void selectDate(View view) {
        //initialize calender
        calendar = Calendar.getInstance();

        //create DatePicker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddBillActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setButton(BUTTON_POSITIVE, "Set", datePickerDialog);
        datePickerDialog.show();
    }

    //datedTextField onClickListener
    public void selectDated(View view) {
        //initialize calender
        calendar = Calendar.getInstance();

        //create DatePicker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddBillActivity.this, datedSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setButton(BUTTON_POSITIVE, "Set", datePickerDialog);
        datePickerDialog.show();
    }

    //add challan number to table
    @SuppressLint("ClickableViewAccessibility")
    private void addChallanNoToTable(TableLayout tableLayout, String challanNo, ArrayList<String> challanNoSelectedList, ArrayAdapter<String> challanNoAdapter) {
        //inflate row
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.add_challan_table_row, addBillParent, false);

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

    //method to put required challan data to TextFields in bill form
    private void setChallanDataInTextFields(@NonNull Challan challan) {

        //for number of pieces text field
        //get previous total pieces and add with current total pieces
        String previousTotalPiecesString = Objects.requireNonNull(noOfPiecesAddBillTextField.getEditText()).getText().toString();

        //add number of pieces data in text field if text field is empty
        if (previousTotalPiecesString.isEmpty()) {
            Objects.requireNonNull(noOfPiecesAddBillTextField.getEditText()).setText(challan.getTotal_pieces());
        }
        else {
            int previousTotalPiecesInt = Integer.parseInt(previousTotalPiecesString);
            int currentTotalPiecesInt = Integer.parseInt(challan.getTotal_pieces());

            //add number of pieces data in text field after adding current pieces
            Objects.requireNonNull(noOfPiecesAddBillTextField.getEditText()).setText(String.valueOf(previousTotalPiecesInt + currentTotalPiecesInt));
        }

        //for total quantity text field
        //get previous quantity and add with current quantity
        String previousQuantityString = Objects.requireNonNull(quantityAddBillTextField.getEditText()).getText().toString();

        //add quantity data in text field if text field is empty
        if (previousQuantityString.isEmpty()) {
            Objects.requireNonNull(quantityAddBillTextField.getEditText()).setText(challan.getTotal_meters());
        }
        else {
            double previousQuantity = Double.parseDouble(previousQuantityString);
            double currentQuantity = Double.parseDouble(challan.getTotal_meters());

            //add quantity data in text field after adding current quantity
            Objects.requireNonNull(quantityAddBillTextField.getEditText()).setText(String.valueOf(previousQuantity + currentQuantity));
        }
    }

    //method to update TextFields after challan is removed from challan no list
    private void updateTextFieldAfterChallanRemoved(String challanNoToBeRemoved) {
        //get child count of challan no table
        int childCount = addBillTableLayout.getChildCount();

        //if statement will be executed when only table is left in table
        if (childCount == 1) {
            //clear all text fields
            Objects.requireNonNull(messersAddBillTextField.getEditText()).getText().clear();
            Objects.requireNonNull(addressAddBillTextField.getEditText()).getText().clear();
            Objects.requireNonNull(purchaserGSTAddBillTextField.getEditText()).getText().clear();
            Objects.requireNonNull(descriptionAddBillTextField.getEditText()).getText().clear();
            Objects.requireNonNull(noOfPiecesAddBillTextField.getEditText()).getText().clear();
            Objects.requireNonNull(quantityAddBillTextField.getEditText()).getText().clear();
            Objects.requireNonNull(rateAddBillTextField.getEditText()).getText().clear();
            Objects.requireNonNull(discountAddBillTextField.getEditText()).getText().clear();

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
            String previousTotalPiecesString = Objects.requireNonNull(noOfPiecesAddBillTextField.getEditText()).getText().toString();

            if (challanObject != null) {
                int previousTotalPiecesInt = Integer.parseInt(previousTotalPiecesString);
                int currentTotalPiecesInt = Integer.parseInt(challanObject.getTotal_pieces());
                int remainingPieces = Math.abs(previousTotalPiecesInt - currentTotalPiecesInt);

                //add number of pieces data in text field after adding current pieces
                Objects.requireNonNull(noOfPiecesAddBillTextField.getEditText()).setText(String.valueOf(remainingPieces));
            }

            //for total quantity text field
            //get previous quantity and subtract with current quantity
            String previousQuantityString = Objects.requireNonNull(quantityAddBillTextField.getEditText()).getText().toString();

            if (challanObject != null) {
                double previousQuantity = Double.parseDouble(previousQuantityString);
                double currentQuantity = Double.parseDouble(challanObject.getTotal_meters());
                double remainingQuantity = Math.abs(previousQuantity - currentQuantity);

                //add quantity data in text field after adding current quantity
                Objects.requireNonNull(quantityAddBillTextField.getEditText()).setText(String.valueOf(remainingQuantity));
            }
        }
    }

    //method to calculate amount, total, netAmount
    private void calculateAmount(String s) {
        double quantityAfterDiscount, rate;
        String amount;

        //remove previous error
        rateAddBillTextField.setErrorEnabled(false);

        //get quantityAfterDiscount and rate from EditTexts
        String quantityAfterDiscountString = Objects.requireNonNull(quantityAfterDiscountAddBillTextField.getEditText()).getText().toString();
        if (!quantityAfterDiscountString.isEmpty()) {
            quantityAfterDiscount = Double.parseDouble(quantityAfterDiscountString);
            rate = Double.parseDouble(s);

            //calculate amount
            amount = String.valueOf(quantityAfterDiscount * rate);

            //set amount in amountEditText, totalEditText, netAmountEditText
            Objects.requireNonNull(amountAddBillTextField.getEditText()).setText(amount);
            Objects.requireNonNull(totalAddBillTextField.getEditText()).setText(amount);

            //get discount percent from discount EditText
            String discountPercent = Objects.requireNonNull(discountAddBillTextField.getEditText()).getText().toString();
            //calculate discount if discountPercent is not empty
            if (!discountPercent.isEmpty()) {
                calculateDiscount(amount, discountPercent, discountAmountAddBillTextField, netAmountAddBillTextField);
            }
            //else statement will be executed if discount percent is empty
            else {
                Objects.requireNonNull(netAmountAddBillTextField.getEditText()).setText(amount);
            }

            //perform click on radio button to calculate GST amount
            SGST25RadioButton.performClick();
        }
    }

    //method to calculate discount
    public static void calculateDiscount(String amountString, String discountPercentString, TextInputLayout discountAmountTextField, TextInputLayout netAmountTextField) {
        double amount, discountPercent;
        String discountAmount, netAmount;

        //get amount and discount percent
        amount = Double.parseDouble(amountString);
        discountPercent = Double.parseDouble(discountPercentString);

        //calculate discount amount
        discountAmount = new DecimalFormat("##.##").format((discountPercent / 100) * amount);

        //calculate net amount
        netAmount = new DecimalFormat("##.##").format( amount - Double.parseDouble(discountAmount));

        //set discount amount and net amount in EditText
        Objects.requireNonNull(discountAmountTextField.getEditText()).setText(discountAmount);
        Objects.requireNonNull(netAmountTextField.getEditText()).setText(netAmount);
    }

    //RadioButton onClick method to set GST 2.5% twice or 5% once
    public void setGST25Or5(View view) {

        //select all the RadioButton with 2.5% and 5% value
        SGST25RadioButton.setChecked(true);
        CGST25RadioButton.setChecked(true);
        IGST5RadioButton.setChecked(true);

        //identify GST number
        //if GST number starts with 27 calculate SGST and CGST else calculate IGST
        String purchaserGST = Objects.requireNonNull(purchaserGSTAddBillTextField.getEditText()).getText().toString();
        if (!purchaserGST.isEmpty()) {
            if (purchaserGST.trim().startsWith("27")) {
                //calculate SGST and CGST
                calculateSGSTAndCGST(2.5, SGSTAmountAddBillTextField, CGSTAmountAddBillTextField, netAmountAddBillTextField, amountAfterTaxAddBillTextField);

                //set dash character in IGST EditText
                Objects.requireNonNull(IGSTAmountAddBillTextField.getEditText()).setText("-");
            }
            else {
                //calculate IGST
                calculateIGST(5, IGSTAmountAddBillTextField, netAmountAddBillTextField, amountAfterTaxAddBillTextField);

                //set dash character in SGST and CGST
                Objects.requireNonNull(SGSTAmountAddBillTextField.getEditText()).setText("-");
                Objects.requireNonNull(CGSTAmountAddBillTextField.getEditText()).setText("-");
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
        String purchaserGST = Objects.requireNonNull(purchaserGSTAddBillTextField.getEditText()).getText().toString();
        if (!purchaserGST.isEmpty()) {
            if (purchaserGST.trim().startsWith("27")) {
                //calculate SGST and CGST
                calculateSGSTAndCGST(6, SGSTAmountAddBillTextField, CGSTAmountAddBillTextField, netAmountAddBillTextField, amountAfterTaxAddBillTextField);

                //set dash character in IGST EditText
                Objects.requireNonNull(IGSTAmountAddBillTextField.getEditText()).setText("-");
            }
            else {
                //calculate IGST
                calculateIGST(12, IGSTAmountAddBillTextField, netAmountAddBillTextField, amountAfterTaxAddBillTextField);

                //set dash character in SGST and CGST
                Objects.requireNonNull(SGSTAmountAddBillTextField.getEditText()).setText("-");
                Objects.requireNonNull(CGSTAmountAddBillTextField.getEditText()).setText("-");
            }
        }
    }

    //method to calculate SGST and CGST
    public static void calculateSGSTAndCGST(double GSTPercent, TextInputLayout SGSTAmountTextField, TextInputLayout CGSTAmountTextField,
                                      TextInputLayout netAmountTextField, TextInputLayout amountAfterTaxTextField) {
        double netAmountDouble, GSTAmount, amountAfterTax;

        //get netAmount from EditText
        String netAmount = Objects.requireNonNull(netAmountTextField.getEditText()).getText().toString();

        //calculate GST amount if netAmount is available
        if (!netAmount.isEmpty()) {
            netAmountDouble = Double.parseDouble(netAmount);
            GSTAmount = (GSTPercent / 100) * netAmountDouble;

            //set GSTAmount in SGST and CGST EditText
            String GSTAmountString = new DecimalFormat("##.##").format(GSTAmount);
            Objects.requireNonNull(SGSTAmountTextField.getEditText()).setText(GSTAmountString);
            Objects.requireNonNull(CGSTAmountTextField.getEditText()).setText(GSTAmountString);

            //calculate amount after tax
            amountAfterTax = netAmountDouble + GSTAmount + GSTAmount;
            String amountAfterTaxString = new DecimalFormat("##.##").format(amountAfterTax);

            //set amountAfterTaxString in amountAfterTax EditText
            Objects.requireNonNull(amountAfterTaxTextField.getEditText()).setText(amountAfterTaxString);
        }
    }

    //method to calculate IGST
    public static void calculateIGST(double IGSTPercent, TextInputLayout IGSTAmountTextField,
                               TextInputLayout netAmountTextField, TextInputLayout amountAfterTaxTextField) {
        double netAmountDouble, IGSTAmount, amountAfterTax;

        //get netAmount from EditText
        String netAmount = Objects.requireNonNull(netAmountTextField.getEditText()).getText().toString();

        //calculate GST amount if netAmount is available
        if (!netAmount.isEmpty()) {
            netAmountDouble = Double.parseDouble(netAmount);
            IGSTAmount = (IGSTPercent / 100) * netAmountDouble;

            //set GSTAmount in SGST and CGST EditText
            String IGSTAmountString = new DecimalFormat("##.##").format(IGSTAmount);
            Objects.requireNonNull(IGSTAmountTextField.getEditText()).setText(IGSTAmountString);

            //calculate amount after tax
            amountAfterTax = netAmountDouble + IGSTAmount;
            String amountAfterTaxString = new DecimalFormat("##.##").format(amountAfterTax);

            //set amountAfterTaxString in amountAfterTax EditText
            Objects.requireNonNull(amountAfterTaxTextField.getEditText()).setText(amountAfterTaxString);
        }
    }

    //method to add new report data in database
    public static void createNewReportDataInDatabase(Bill bill, String lotNo, String deliveryAddress, String designNoList) {
        //initialize Report object
        Report report = new Report();

        //add bill data in Report object
        report.setBillNo(bill.getBillNo());
        StringBuilder challanNoList = new StringBuilder();
        for (String challanNo : bill.getChallanNo()) {
            challanNoList.append(challanNo).append(", ");
        }
        report.setChallanNo(challanNoList.toString());
        report.setDate(bill.getDate());
        report.setLotNo(lotNo);
        report.setParty(bill.getMessers());
        report.setPartyLowerCase(bill.getMessersLowerCase());
        report.setPartyLowerCase(bill.getMessersLowerCase());
        report.setDeliveryAddress(deliveryAddress);
        report.setPieces(bill.getNoOfPieces());
        report.setMeters(bill.getQuantity());
        report.setDesignNo(designNoList);
        report.setRate(bill.getRate());
        report.setAmount(bill.getAmount());
        report.setDiscountPercent(bill.getDiscount());
        report.setCommission(bill.getDiscountAmount());
        report.setNetAmount(bill.getNetAmount());

        if (bill.getPurchaserGst().trim().startsWith("27")) {
            report.setGstPercent(bill.getSGSTPercent());
            report.setGstAmount(bill.getSGSTAmount()  + " + " + bill.getCGSTAmount());
            String totalGstAmount = String.valueOf(Double.parseDouble(bill.getSGSTAmount()) + Double.parseDouble(bill.getCGSTAmount()));
            report.setTotalGSTAmount(totalGstAmount);
        }
        else {
            report.setGstPercent(bill.getIGSTPercent());
            report.setGstAmount(bill.getIGSTAmount());
            report.setTotalGSTAmount(bill.getIGSTAmount());
        }

        report.setFinalAmount(bill.getAmountAfterTax());

        //send report object to database
        setReportDataInReportList(bill.getBillNo(), report);
    }
}