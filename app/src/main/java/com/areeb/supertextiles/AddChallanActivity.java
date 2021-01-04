package com.areeb.supertextiles;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.areeb.supertextiles.models.Challan;
import com.areeb.supertextiles.models.Design;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.DESIGN_1;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.DESIGN_2;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.DESIGN_3;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.DESIGN_4;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.GST_NO;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.ID;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.NAME;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.getAllCustomersReference;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.getChallanNoDatabaseReference;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.getDeliveryAddressDatabaseReference;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.setChallanDataInChallanList;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.setChallanNoInDatabase;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.setDesignDataInDatabase;

import static com.areeb.supertextiles.ChallanDetailsActivity.CHALLAN_NO;
import static com.areeb.supertextiles.ChallanDetailsActivity.CHALLAN_DATE;
import static com.areeb.supertextiles.ChallanDetailsActivity.PURCHASER;
import static com.areeb.supertextiles.ChallanDetailsActivity.LOT_NO;
import static com.areeb.supertextiles.ChallanDetailsActivity.LR_NO;
import static com.areeb.supertextiles.ChallanDetailsActivity.DELIVERY_AT;
import static com.areeb.supertextiles.ChallanDetailsActivity.PURCHASER_GST;
import static com.areeb.supertextiles.ChallanDetailsActivity.QUALITY;
import static com.areeb.supertextiles.ChallanDetailsActivity.TOTAL_PIECES;
import static com.areeb.supertextiles.ChallanDetailsActivity.TOTAL_METERS;
import static com.areeb.supertextiles.ChallanDetailsActivity.FOLD;
import static com.areeb.supertextiles.ChallanDetailsActivity.NO_OF_DESIGNS;

public class AddChallanActivity extends AppCompatActivity {

    Calendar calendar;
    TextInputLayout challanNoTextField, dateTextField, purchaserTextField, lotNoTextField, LRNoTextField,
            deliveryAtTextField, purchaserGSTField, qualityTextField, totalPiecesTextField, totalMetersTextField, foldTextField;
    Button addMetersButton, createChallanButton;
    PreferenceManager preferenceManager;
    DatabaseReference customersDatabaseReference, deliveryAddressDatabaseReference, challanNumberDatabaseReference;
    AutoCompleteTextView purchaserAutoCompleteTextView, addressAutoCompleteTextView;
    ArrayList<String> purchaserGSTNoList, purchaserIDList;
    long challanNumber;
    int numberOfDesigns = 0;
    String totalMeters, design1No, design2No, design3No, design4No, design1Color, design2Color, design3Color, design4Color;
    ArrayList<String> design1MeterList, design2MeterList, design3MeterList, design4MeterList;

    //DatePicker dialog listener
    DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
        //set date
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //update date EditText
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Objects.requireNonNull(dateTextField.getEditText()).setText(simpleDateFormat.format(calendar.getTime()));
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Challan");
        setContentView(R.layout.activity_add_challan);

        //initializing xml views
        challanNoTextField = findViewById(R.id.challanNoTextField);
        dateTextField = findViewById(R.id.dateTextField);
        purchaserTextField = findViewById(R.id.purchaserTextField);
        lotNoTextField = findViewById(R.id.lotNoTextField);
        LRNoTextField = findViewById(R.id.LRNoTextField);
        deliveryAtTextField = findViewById(R.id.deliveryAtTextField);
        purchaserGSTField = findViewById(R.id.purchaserGSTField);
        qualityTextField = findViewById(R.id.qualityTextField);
        totalPiecesTextField = findViewById(R.id.totalPiecesTextField);
        totalMetersTextField = findViewById(R.id.totalMetersTextField);
        foldTextField = findViewById(R.id.foldTextField);
        addMetersButton = findViewById(R.id.addMetersButton);
        createChallanButton = findViewById(R.id.createChallanButton);
        purchaserAutoCompleteTextView = (AutoCompleteTextView) purchaserTextField.getEditText();
        addressAutoCompleteTextView  = (AutoCompleteTextView) deliveryAtTextField.getEditText();

        //initialize preferenceManager
        preferenceManager = new PreferenceManager(this);

        //initialize DatabaseReference
        customersDatabaseReference = getAllCustomersReference();
        deliveryAddressDatabaseReference = getDeliveryAddressDatabaseReference();
        challanNumberDatabaseReference = getChallanNoDatabaseReference();

        //initialize ArrayList
        purchaserGSTNoList = new ArrayList<>();
        purchaserIDList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //fetch challan number from database
        if (getIntent().getStringExtra(CHALLAN_NO) == null) {
            challanNumberDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //get challan number
                    challanNumber = Long.parseLong(String.valueOf(snapshot.getValue()));

                    //set challan number in EditText
                    Objects.requireNonNull(challanNoTextField.getEditText()).setText(String.valueOf(snapshot.getValue()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    error.toException().printStackTrace();
                }
            });
        }

        //get all the customers from database
        customersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //initialize purchaserList
                ArrayList<String> purchaserList = new ArrayList<>();

                //add name to purchaserList
                for (DataSnapshot customer : snapshot.getChildren()) {
                    purchaserList.add(String.valueOf(customer.child(NAME).getValue()));
                    purchaserGSTNoList.add(String.valueOf(customer.child(GST_NO).getValue()));
                    purchaserIDList.add(String.valueOf(customer.child(ID).getValue()));
                }

                //inflate purchaserTextField drop down
                ArrayAdapter<String> purchaserAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_list_item, purchaserList);
                if (purchaserAutoCompleteTextView != null)
                    purchaserAutoCompleteTextView.setAdapter(purchaserAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });

        //purchaserAutoCompleteTextView onItemSelectedListener
        purchaserAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {

            //set GST number of selected purchaser
            Objects.requireNonNull(purchaserGSTField.getEditText()).setText(purchaserGSTNoList.get(position));

            //empty deliveryAtTextField
            Objects.requireNonNull(deliveryAtTextField.getEditText()).getText().clear();

            //fetch all the delivery address of selected purchaser
            String purchaserId = purchaserIDList.get(position);
            deliveryAddressDatabaseReference.child(purchaserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //inflate deliveryAtTextField drop down
                    ArrayList<String> addressList = new ArrayList<>();

                    //add all the address to address list
                    for (int i = 0; i < snapshot.getChildrenCount(); i++) {
                        String address = Objects.requireNonNull(snapshot.child(String.valueOf(i)).getValue()).toString();
                        addressList.add(address);
                    }

                    //initialize and set adapter
                    ArrayAdapter<String> addressAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_list_item, addressList);
                    if (addressAutoCompleteTextView != null)
                        addressAutoCompleteTextView.setAdapter(addressAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    error.toException().printStackTrace();
                }
            });
        });

        //get totalMeter from preferenceManager
        totalMeters = preferenceManager.getTotalMeters();

        //set totalMeters to totalMetersTextField if greater than 0
        double totalMetersDouble = Double.parseDouble(totalMeters);
        if (totalMetersDouble > 0) {
            Objects.requireNonNull(totalMetersTextField.getEditText()).setText(totalMeters);

            //get design data from preferenceManager
            getDesignDataFromPreferenceManager();
        }

        //get intent data and set in EditText's
        if (getIntent() != null) {

            //set intent data in EditText's
            Objects.requireNonNull(challanNoTextField.getEditText()).setText(getIntent().getStringExtra(CHALLAN_NO));
            Objects.requireNonNull(dateTextField.getEditText()).setText(getIntent().getStringExtra(CHALLAN_DATE));
            Objects.requireNonNull(purchaserTextField.getEditText()).setText(getIntent().getStringExtra(PURCHASER));
            Objects.requireNonNull(lotNoTextField.getEditText()).setText(getIntent().getStringExtra(LOT_NO));
            Objects.requireNonNull(LRNoTextField.getEditText()).setText(getIntent().getStringExtra(LR_NO));
            Objects.requireNonNull(deliveryAtTextField.getEditText()).setText(getIntent().getStringExtra(DELIVERY_AT));
            Objects.requireNonNull(purchaserGSTField.getEditText()).setText(getIntent().getStringExtra(PURCHASER_GST));
            Objects.requireNonNull(qualityTextField.getEditText()).setText(getIntent().getStringExtra(QUALITY));
            Objects.requireNonNull(totalPiecesTextField.getEditText()).setText(getIntent().getStringExtra(TOTAL_PIECES));
            Objects.requireNonNull(totalMetersTextField.getEditText()).setText(getIntent().getStringExtra(TOTAL_METERS));
            Objects.requireNonNull(foldTextField.getEditText()).setText(getIntent().getStringExtra(FOLD));

            //store intent data in variables
            totalMeters = getIntent().getStringExtra(TOTAL_METERS);
            String noOfDesignsString = getIntent().getStringExtra(NO_OF_DESIGNS);
            if (noOfDesignsString != null) {
                numberOfDesigns = Integer.parseInt(noOfDesignsString);
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        //clear meter data stored in sharedPreference
        preferenceManager.clearDesignAndMetersFromSharedPreference();
    }

    //dateTextField onClick method
    public void selectDate(View view) {
        //initialize calender
        calendar = Calendar.getInstance();

        //create DatePicker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddChallanActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setButton(BUTTON_POSITIVE, "Set", datePickerDialog);
                datePickerDialog.show();
    }

    //addMetersButton onClick method
    public void goToAddMetersActivity(View view) {

        //get total pieces
        String totalPieces = Objects.requireNonNull(totalPiecesTextField.getEditText()).getText().toString();

        //check if total pieces is empty
        if (totalPieces.isEmpty()) {
            showErrorInTextField(totalPiecesTextField, "Enter total pieces");
            return;
        }

        //check if total pieces is more than 50
        long totalPiecesLong = Long.parseLong(totalPieces);
        if (totalPiecesLong > 50) {
            showErrorInTextField(totalPiecesTextField, "Cannot be more than 50");
            return;
        }

        //remove error is value is entered
        totalPiecesTextField.setErrorEnabled(false);

        //goto AddMetersActivity
        Intent intent = new Intent(AddChallanActivity.this, AddMetersActivity.class);
        intent.putExtra(TOTAL_PIECES, totalPieces);
        intent.putExtra(TOTAL_METERS, totalMeters);
        intent.putExtra(NO_OF_DESIGNS, numberOfDesigns);
        startActivity(intent);
    }

    //createChallanButton onClick Method
    public void createChallan(View view) {

        //check if any field is empty
        boolean isAnyFieldEmpty = checkForEmptyField();

        //form will be submitted if every field is filled
        if (!isAnyFieldEmpty) {

            //clear meter data stored in sharedPreference
            preferenceManager.clearDesignAndMetersFromSharedPreference();

            //send challan data to database
            sendChallanDataToDatabase();

            //set next challan number
            setChallanNoInDatabase(challanNumber + 1);

            //empty form
            clearForm();

            //show success Toast
            Toast.makeText(this, "Challan created successfully", Toast.LENGTH_LONG).show();

            //finish AddChallanActivity
            finish();
        }
    }

    //clear AddChallan form
    private void clearForm() {
        Objects.requireNonNull(challanNoTextField.getEditText()).getText().clear();
        Objects.requireNonNull(dateTextField.getEditText()).getText().clear();
        Objects.requireNonNull(purchaserTextField.getEditText()).getText().clear();
        Objects.requireNonNull(lotNoTextField.getEditText()).getText().clear();
        Objects.requireNonNull(LRNoTextField.getEditText()).getText().clear();
        Objects.requireNonNull(deliveryAtTextField.getEditText()).getText().clear();
        Objects.requireNonNull(purchaserGSTField.getEditText()).getText().clear();
        Objects.requireNonNull(qualityTextField.getEditText()).getText().clear();
        Objects.requireNonNull(totalPiecesTextField.getEditText()).getText().clear();
        Objects.requireNonNull(totalMetersTextField.getEditText()).getText().clear();
        Objects.requireNonNull(foldTextField.getEditText()).getText().clear();
    }

    //method to check if any field is empty
    //return true if any field is empty
    //return false if all the fields are filled
    private boolean checkForEmptyField() {

        //remove all the previous errors
        challanNoTextField.setErrorEnabled(false);
        dateTextField.setErrorEnabled(false);
        purchaserTextField.setErrorEnabled(false);
        lotNoTextField.setErrorEnabled(false);
        LRNoTextField.setErrorEnabled(false);
        deliveryAtTextField.setErrorEnabled(false);
        purchaserGSTField.setErrorEnabled(false);
        qualityTextField.setErrorEnabled(false);
        totalPiecesTextField.setErrorEnabled(false);
        totalMetersTextField.setErrorEnabled(false);
        foldTextField.setErrorEnabled(false);

        String data = Objects.requireNonNull(challanNoTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(challanNoTextField, "Challan No. required");
            return true;
        }

        data = Objects.requireNonNull(dateTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(dateTextField, "Date required");
            return true;
        }

        data = Objects.requireNonNull(purchaserTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(purchaserTextField, "Purchaser required");
            return true;
        }

        data = Objects.requireNonNull(deliveryAtTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(deliveryAtTextField, "Delivery At required");
            return true;
        }

        data = Objects.requireNonNull(purchaserGSTField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(purchaserGSTField, "Purchaser GST required");
            return true;
        }

        data = Objects.requireNonNull(qualityTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(qualityTextField, "Quality required");
            return true;
        }

        data = Objects.requireNonNull(totalPiecesTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(totalPiecesTextField, "Total pieces required");
            return true;
        }

        //check if total pieces is more than 50
        long totalPiecesLong = Long.parseLong(data);
        if (totalPiecesLong > 50) {
            showErrorInTextField(totalPiecesTextField, "Cannot be more than 50");
            return true;
        }

        data = Objects.requireNonNull(totalMetersTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(totalMetersTextField, "Total Meters required");
            return true;
        }

        data = Objects.requireNonNull(foldTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(foldTextField, "Fold required");
            return true;
        }

        return false;
    }

    //method to show error in TextField
    public static void showErrorInTextField(TextInputLayout textInputLayout, String error) {
        textInputLayout.requestFocus();
        textInputLayout.setError(error);
    }

    //method to fetch design data from preferenceManager
    private void getDesignDataFromPreferenceManager() {
        numberOfDesigns = preferenceManager.getNumberOfDesigns();

        if (numberOfDesigns == 1) {
            getDesign1DataFromPreferenceManager();
        }
        else if (numberOfDesigns == 2) {
            getDesign1DataFromPreferenceManager();
            getDesign2DataFromPreferenceManager();
        }
        else if (numberOfDesigns == 3) {
            getDesign1DataFromPreferenceManager();
            getDesign2DataFromPreferenceManager();
            getDesign3DataFromPreferenceManager();
        }
        else if (numberOfDesigns == 4) {
            getDesign1DataFromPreferenceManager();
            getDesign2DataFromPreferenceManager();
            getDesign3DataFromPreferenceManager();
            getDesign4DataFromPreferenceManager();
        }
    }

    //method to get design 1 data from preferenceManager
    private void getDesign1DataFromPreferenceManager() {
        design1No = preferenceManager.getDesign1No();
        design1Color = preferenceManager.getDesign1Color();
        design1MeterList = new ArrayList<>(preferenceManager.getDesign1MeterList());
    }

    //method to get design 2 data from preferenceManager
    private void getDesign2DataFromPreferenceManager() {
        design2No = preferenceManager.getDesign2No();
        design2Color = preferenceManager.getDesign2Color();
        design2MeterList = new ArrayList<>(preferenceManager.getDesign2MeterList());
    }

    //method to get design 3 data from preferenceManager
    private void getDesign3DataFromPreferenceManager() {
        design3No = preferenceManager.getDesign3No();
        design3Color = preferenceManager.getDesign3Color();
        design3MeterList = new ArrayList<>(preferenceManager.getDesign3MeterList());
    }

    //method to get design 4 data from preferenceManager
    private void getDesign4DataFromPreferenceManager() {
        design4No = preferenceManager.getDesign4No();
        design4Color = preferenceManager.getDesign4Color();
        design4MeterList = new ArrayList<>(preferenceManager.getDesign4MeterList());
    }

    //method to add challan data to database
    private void sendChallanDataToDatabase() {

        //declare and initialize Challan object
        Challan challan = new Challan();

        //add data to challan object
        challan.setChallan_no(String.valueOf(challanNumber));
        challan.setDate(Objects.requireNonNull(dateTextField.getEditText()).getText().toString());
        challan.setPurchaser(purchaserAutoCompleteTextView.getText().toString());
        challan.setLot_no(Objects.requireNonNull(lotNoTextField.getEditText()).getText().toString());
        challan.setLr_no(Objects.requireNonNull(LRNoTextField.getEditText()).getText().toString());
        challan.setDelivery_at(addressAutoCompleteTextView.getText().toString());
        challan.setPurchaser_gst(Objects.requireNonNull(purchaserGSTField.getEditText()).getText().toString());
        challan.setQuality(Objects.requireNonNull(qualityTextField.getEditText()).getText().toString());
        challan.setTotal_pieces(Objects.requireNonNull(totalPiecesTextField.getEditText()).getText().toString());
        challan.setTotal_meters(Objects.requireNonNull(totalMetersTextField.getEditText()).getText().toString());
        challan.setFold(Objects.requireNonNull(foldTextField.getEditText()).getText().toString());
        challan.setNo_of_designs(String.valueOf(numberOfDesigns));

        //send challan object to database
        setChallanDataInChallanList(String.valueOf(challanNumber), challan);

        //initialize Design object and set data
        if (numberOfDesigns == 1) {
            setDesignDataInDatabase(String.valueOf(challanNumber), DESIGN_1, getDesign1Object());
        } else if (numberOfDesigns == 2) {
            setDesignDataInDatabase(String.valueOf(challanNumber), DESIGN_1, getDesign1Object());
            setDesignDataInDatabase(String.valueOf(challanNumber), DESIGN_2, getDesign2Object());
        } else if (numberOfDesigns == 3) {
            setDesignDataInDatabase(String.valueOf(challanNumber), DESIGN_1, getDesign1Object());
            setDesignDataInDatabase(String.valueOf(challanNumber), DESIGN_2, getDesign2Object());
            setDesignDataInDatabase(String.valueOf(challanNumber), DESIGN_3, getDesign3Object());
        } else if (numberOfDesigns == 4) {
            setDesignDataInDatabase(String.valueOf(challanNumber), DESIGN_1, getDesign1Object());
            setDesignDataInDatabase(String.valueOf(challanNumber), DESIGN_2, getDesign2Object());
            setDesignDataInDatabase(String.valueOf(challanNumber), DESIGN_3, getDesign3Object());
            setDesignDataInDatabase(String.valueOf(challanNumber), DESIGN_4, getDesign4Object());
        }
    }

    //method to initialize Design object for design 1
    private Design getDesign1Object() {
        Design design = new Design();
        design.setDesign_no(design1No);
        design.setDesign_color(design1Color);
        design.setMeterList(design1MeterList);
        return design;
    }

    //method to initialize Design object for design 2
    private Design getDesign2Object() {
        Design design = new Design();
        design.setDesign_no(design2No);
        design.setDesign_color(design2Color);
        design.setMeterList(design2MeterList);
        return design;
    }

    //method to initialize Design object for design 3
    private Design getDesign3Object() {
        Design design = new Design();
        design.setDesign_no(design3No);
        design.setDesign_color(design3Color);
        design.setMeterList(design3MeterList);
        return design;
    }

    //method to initialize Design object for design 4
    private Design getDesign4Object() {
        Design design = new Design();
        design.setDesign_no(design4No);
        design.setDesign_color(design4Color);
        design.setMeterList(design4MeterList);
        return design;
    }
}