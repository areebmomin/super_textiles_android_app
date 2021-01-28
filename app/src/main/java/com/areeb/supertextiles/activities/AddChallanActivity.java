package com.areeb.supertextiles.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.areeb.supertextiles.R;
import com.areeb.supertextiles.models.Challan;
import com.areeb.supertextiles.models.Design;
import com.areeb.supertextiles.utilities.PreferenceManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.CHALLAN_OBJECT;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.DESIGN_1_OBJECT;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.DESIGN_2_OBJECT;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.DESIGN_3_OBJECT;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.DESIGN_4_OBJECT;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.NO_OF_DESIGNS;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.TOTAL_METERS;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.TOTAL_PIECES;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.ADDRESS;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.DESIGN_1;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.DESIGN_2;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.DESIGN_3;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.DESIGN_4;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.GST_NO;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.ID;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.NAME;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getAllCustomersReference;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getDeliveryAddressDatabaseReference;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getDesignDataDatabaseReference;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getQualityListDatabaseReference;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.setChallanDataInChallanList;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.setDesignDataInDatabase;

public class AddChallanActivity extends AppCompatActivity {

    Calendar calendar;
    TextInputLayout challanNoTextField, dateTextField, purchaserTextField, lotNoTextField, LRNoTextField,
            deliveryAtTextField, purchaserGSTField, qualityTextField, totalPiecesTextField, totalMetersTextField, foldTextField;
    Button addMetersButton, createChallanButton;
    PreferenceManager preferenceManager;
    DatabaseReference customersDatabaseReference;
    DatabaseReference deliveryAddressDatabaseReference;
    AutoCompleteTextView purchaserAutoCompleteTextView, addressAutoCompleteTextView, qualityAutoCompleteTextView;
    ArrayList<String> purchaserGSTNoList, purchaserIDList, purchaserAddressList;
    int numberOfDesigns = 0;
    String totalMeters, totalMetersFromIntent, purchaserAddress;
    Design design1, design2, design3, design4;
    Gson gson;
    Challan challan;
    boolean isThisEditChallan = false;

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
        qualityAutoCompleteTextView = (AutoCompleteTextView) qualityTextField.getEditText();

        //initialize preferenceManager
        preferenceManager = new PreferenceManager(this);

        //initialize DatabaseReference
        customersDatabaseReference = getAllCustomersReference();
        deliveryAddressDatabaseReference = getDeliveryAddressDatabaseReference();

        //initialize ArrayList
        purchaserGSTNoList = new ArrayList<>();
        purchaserIDList = new ArrayList<>();
        purchaserAddressList = new ArrayList<>();

        //initialize design objects
        design1 = new Design();
        design2 = new Design();
        design3 = new Design();
        design4 = new Design();

        //initializing Gson
        gson = new Gson();

        //initializing Challan object
        challan = new Challan();

        //get intent data and set in EditText's
        if (getIntent() != null) {

            //convert Json string to Challan object
            challan = gson.fromJson(getIntent().getStringExtra(CHALLAN_OBJECT), Challan.class);

            if (challan != null) {
                //get challan number from intent
                String challanNoString = challan.getChallan_no();
                if (challanNoString != null) {
                    isThisEditChallan = true;
                }

                //set intent data in EditText's
                Objects.requireNonNull(challanNoTextField.getEditText()).setText(challanNoString);
                Objects.requireNonNull(dateTextField.getEditText()).setText(challan.getDate());
                Objects.requireNonNull(purchaserTextField.getEditText()).setText(challan.getPurchaser());
                Objects.requireNonNull(lotNoTextField.getEditText()).setText(challan.getLot_no());
                Objects.requireNonNull(LRNoTextField.getEditText()).setText(challan.getLr_no());
                Objects.requireNonNull(deliveryAtTextField.getEditText()).setText(challan.getDelivery_at());
                Objects.requireNonNull(purchaserGSTField.getEditText()).setText(challan.getPurchaser_gst());
                Objects.requireNonNull(qualityTextField.getEditText()).setText(challan.getQuality());
                Objects.requireNonNull(totalPiecesTextField.getEditText()).setText(challan.getTotal_pieces());
                Objects.requireNonNull(totalMetersTextField.getEditText()).setText(challan.getTotal_meters());
                Objects.requireNonNull(foldTextField.getEditText()).setText(challan.getFold());

                //store intent data in variables
                totalMeters = challan.getTotal_meters();
                totalMetersFromIntent = totalMeters;
                String noOfDesignsString = challan.getNo_of_designs();
                if (noOfDesignsString != null) {
                    numberOfDesigns = Integer.parseInt(noOfDesignsString);
                }

                //get design data from intent and assign to design object
                getDesignObjectFromIntent(getIntent());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

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
                    purchaserAddressList.add(String.valueOf(customer.child(ADDRESS).getValue()));
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

        //purchaserAutoCompleteTextView onItemClickListener
        purchaserAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {

            //set GST number of selected purchaser
            Objects.requireNonNull(purchaserGSTField.getEditText()).setText(purchaserGSTNoList.get(position));

            //empty deliveryAtTextField
            Objects.requireNonNull(deliveryAtTextField.getEditText()).getText().clear();

            //set selected customer address in variable
            purchaserAddress = purchaserAddressList.get(position);

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

        //get totalMeter from preferenceManager if totalMeters from intent is null or empty
        if (totalMetersFromIntent == null || totalMetersFromIntent.isEmpty()) {
            String totalMeterString = preferenceManager.getTotalMeters();

            //set totalMeters to totalMetersTextField if greater than 0
            double totalMetersDouble = Double.parseDouble(totalMeterString);
            if (totalMetersDouble > 0) {
                //assign data to totalMeters
                totalMeters = totalMeterString;

                //set totalMeters in EditText
                Objects.requireNonNull(totalMetersTextField.getEditText()).setText(totalMeters);

                //get design data from preferenceManager
                getDesignDataFromPreferenceManager();
            }
        } else {
            totalMetersFromIntent = null;
        }

        //get quality list from database
        getQualityListDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //inflate deliveryAtTextField drop down
                ArrayList<String> qualityList = new ArrayList<>();

                //add all the address to address list
                for (DataSnapshot qualitySnapShot : snapshot.getChildren()) {
                    String quality = qualitySnapShot.getValue(String.class);
                    qualityList.add(quality);
                }

                //initialize and set adapter
                ArrayAdapter<String> addressAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.drop_down_list_item, qualityList);
                if (qualityAutoCompleteTextView != null)
                    qualityAutoCompleteTextView.setAdapter(addressAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
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

        //check if total pieces is less than 1
        long totalPiecesLong = Long.parseLong(totalPieces);
        if (totalPiecesLong < 1) {
            showErrorInTextField(totalPiecesTextField, "Cannot be less than 1");
            return;
        }

        //check if total pieces is more than 50
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

        //put design data in intent based on number of designs
        addDesignDataInIntent(intent);

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
            boolean isChallanSaved = sendChallanDataToDatabase();

            if (isChallanSaved) {
                //empty form
                clearForm();

                //show success Toast
                Toast.makeText(this, "Challan created successfully", Toast.LENGTH_LONG).show();

                //finish AddChallanActivity
                finish();
            }
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
            design1 = preferenceManager.getDesign1Object();
        }
        else if (numberOfDesigns == 2) {
            design1 = preferenceManager.getDesign1Object();
            design2 = preferenceManager.getDesign2Object();
        }
        else if (numberOfDesigns == 3) {
            design1 = preferenceManager.getDesign1Object();
            design2 = preferenceManager.getDesign2Object();
            design3 = preferenceManager.getDesign3Object();
        }
        else if (numberOfDesigns == 4) {
            design1 = preferenceManager.getDesign1Object();
            design2 = preferenceManager.getDesign2Object();
            design3 = preferenceManager.getDesign3Object();
            design4 = preferenceManager.getDesign4Object();
        }
    }

    //method to add challan data to database
    //return true if data has successfully saved in database or false otherwise
    private boolean sendChallanDataToDatabase() {

        //declare and initialize Challan object
        Challan challan = new Challan();

        //convert challan number to String
        String challanNumberString = Objects.requireNonNull(challanNoTextField.getEditText()).getText().toString();

        //add data to challan object
        challan.setChallan_no(challanNumberString);
        challan.setDate(Objects.requireNonNull(dateTextField.getEditText()).getText().toString());
        challan.setPurchaser(purchaserAutoCompleteTextView.getText().toString());
        challan.setPurchaser_lower_case(purchaserAutoCompleteTextView.getText().toString().toLowerCase());
        challan.setLot_no(Objects.requireNonNull(lotNoTextField.getEditText()).getText().toString());
        challan.setLr_no(Objects.requireNonNull(LRNoTextField.getEditText()).getText().toString());
        challan.setDelivery_at(addressAutoCompleteTextView.getText().toString());
        challan.setPurchaser_gst(Objects.requireNonNull(purchaserGSTField.getEditText()).getText().toString());
        challan.setQuality(Objects.requireNonNull(qualityTextField.getEditText()).getText().toString());
        challan.setTotal_pieces(Objects.requireNonNull(totalPiecesTextField.getEditText()).getText().toString());
        challan.setTotal_meters(Objects.requireNonNull(totalMetersTextField.getEditText()).getText().toString());
        challan.setFold(Objects.requireNonNull(foldTextField.getEditText()).getText().toString());
        challan.setNo_of_designs(String.valueOf(numberOfDesigns));
        challan.setPurchaser_address(purchaserAddress);

        //get totalPiecesTextField data and convert it into int
        String totalPiecesString = totalPiecesTextField.getEditText().getText().toString();
        int totalPiecesInt = Integer.parseInt(totalPiecesString);

        //initialize Design object and set data
        if (numberOfDesigns == 1) {
            //check that meters are added for every piece
            if (totalPiecesInt > design1.getMeterList().size()) {
                Toast.makeText(this, "Add meter for every pieces", Toast.LENGTH_LONG).show();
                return false;
            }
            //check if total pieces are less than meter count
            if (totalPiecesInt < design1.getMeterList().size()) {
                Toast.makeText(this, "Total pieces is less than meter", Toast.LENGTH_LONG).show();
                return false;
            }

            //remove design2, design3 and design4 data from database
            getDesignDataDatabaseReference(challanNumberString).child(DESIGN_2).removeValue();
            getDesignDataDatabaseReference(challanNumberString).child(DESIGN_3).removeValue();
            getDesignDataDatabaseReference(challanNumberString).child(DESIGN_4).removeValue();

            //store design 1 no in challan object
            challan.setDesign_no_list(new ArrayList<>(Collections.singleton(design1.getDesign_no())));

            //send design data to database
            setDesignDataInDatabase(challanNumberString, DESIGN_1, design1);
        }
        else if (numberOfDesigns == 2) {
            //check that meters are added for every piece
            if (totalPiecesInt > (design1.getMeterList().size() + design2.getMeterList().size())) {
                Toast.makeText(this, "Add meter for every pieces", Toast.LENGTH_LONG).show();
                return false;
            }
            //check if total pieces are less than meter count
            if (totalPiecesInt < (design1.getMeterList().size() + design2.getMeterList().size())) {
                Toast.makeText(this, "Total pieces is less than meter", Toast.LENGTH_LONG).show();
                return false;
            }

            //remove design3 and design4 data from database
            getDesignDataDatabaseReference(challanNumberString).child(DESIGN_3).removeValue();
            getDesignDataDatabaseReference(challanNumberString).child(DESIGN_4).removeValue();

            //store design 1 and 2 no in challan object
            challan.setDesign_no_list(new ArrayList<>(Arrays.asList(design1.getDesign_no(), design2.getDesign_no())));

            //send design data to database
            setDesignDataInDatabase(challanNumberString, DESIGN_1, design1);
            setDesignDataInDatabase(challanNumberString, DESIGN_2, design2);
        }
        else if (numberOfDesigns == 3) {
            //check that meters are added for every piece
            if (totalPiecesInt > (design1.getMeterList().size() + design2.getMeterList().size() + design3.getMeterList().size())) {
                Toast.makeText(this, "Add meter for every pieces", Toast.LENGTH_LONG).show();
                return false;
            }
            //check if total pieces are less than meter count
            if (totalPiecesInt < (design1.getMeterList().size() + design2.getMeterList().size() + design3.getMeterList().size())) {
                Toast.makeText(this, "Total pieces is less than meter", Toast.LENGTH_LONG).show();
                return false;
            }

            //remove and design4 data from database
            getDesignDataDatabaseReference(challanNumberString).child(DESIGN_4).removeValue();

            //store design 1, 2 and 3 no in challan object
            challan.setDesign_no_list(new ArrayList<>(Arrays.asList(design1.getDesign_no(), design2.getDesign_no(), design3.getDesign_no())));

            //send design data to database
            setDesignDataInDatabase(challanNumberString, DESIGN_1, design1);
            setDesignDataInDatabase(challanNumberString, DESIGN_2, design2);
            setDesignDataInDatabase(challanNumberString, DESIGN_3, design3);
        }
        else if (numberOfDesigns == 4) {
            //check that meters are added for every piece
            if (totalPiecesInt > (design1.getMeterList().size() + design2.getMeterList().size() + design3.getMeterList().size() + design4.getMeterList().size())) {
                Toast.makeText(this, "Add meter for every pieces", Toast.LENGTH_LONG).show();
                return false;
            }
            //check if total pieces are less than meter count
            if (totalPiecesInt < (design1.getMeterList().size() + design2.getMeterList().size() + design3.getMeterList().size() + design4.getMeterList().size())) {
                Toast.makeText(this, "Total pieces is less than meter", Toast.LENGTH_LONG).show();
                return false;
            }

            //store design 1, 2, 3 and 4 no in challan object
            challan.setDesign_no_list(new ArrayList<>(Arrays.asList(design1.getDesign_no(), design2.getDesign_no(), design3.getDesign_no(), design4.getDesign_no())));

            //send design data to database
            setDesignDataInDatabase(challanNumberString, DESIGN_1, design1);
            setDesignDataInDatabase(challanNumberString, DESIGN_2, design2);
            setDesignDataInDatabase(challanNumberString, DESIGN_3, design3);
            setDesignDataInDatabase(challanNumberString, DESIGN_4, design4);
        }

        //send challan object to database
        setChallanDataInChallanList(challanNumberString, challan);

        return true;
    }

    //method to add design data in intent
    private void addDesignDataInIntent(Intent intent) {
        if (numberOfDesigns == 1) {
            intent.putExtra(DESIGN_1_OBJECT, gson.toJson(design1));
        } else if (numberOfDesigns == 2) {
            intent.putExtra(DESIGN_1_OBJECT, gson.toJson(design1));
            intent.putExtra(DESIGN_2_OBJECT, gson.toJson(design2));
        } else if (numberOfDesigns == 3) {
            intent.putExtra(DESIGN_1_OBJECT, gson.toJson(design1));
            intent.putExtra(DESIGN_2_OBJECT, gson.toJson(design2));
            intent.putExtra(DESIGN_3_OBJECT, gson.toJson(design3));
        } else if (numberOfDesigns == 4) {
            intent.putExtra(DESIGN_1_OBJECT, gson.toJson(design1));
            intent.putExtra(DESIGN_2_OBJECT, gson.toJson(design2));
            intent.putExtra(DESIGN_3_OBJECT, gson.toJson(design3));
            intent.putExtra(DESIGN_4_OBJECT, gson.toJson(design4));
        }
    }

    //method to get design data from intent and assign to object based on numberOfDesigns
    private void getDesignObjectFromIntent(Intent intent) {
        if (numberOfDesigns == 1) {
            design1 = gson.fromJson(intent.getStringExtra(DESIGN_1_OBJECT), Design.class);
        } else if (numberOfDesigns == 2) {
            design1 = gson.fromJson(intent.getStringExtra(DESIGN_1_OBJECT), Design.class);
            design2 = gson.fromJson(intent.getStringExtra(DESIGN_2_OBJECT), Design.class);
        } else if (numberOfDesigns == 3) {
            design1 = gson.fromJson(intent.getStringExtra(DESIGN_1_OBJECT), Design.class);
            design2 = gson.fromJson(intent.getStringExtra(DESIGN_2_OBJECT), Design.class);
            design3 = gson.fromJson(intent.getStringExtra(DESIGN_3_OBJECT), Design.class);
        } else if (numberOfDesigns == 4) {
            design1 = gson.fromJson(intent.getStringExtra(DESIGN_1_OBJECT), Design.class);
            design2 = gson.fromJson(intent.getStringExtra(DESIGN_2_OBJECT), Design.class);
            design3 = gson.fromJson(intent.getStringExtra(DESIGN_3_OBJECT), Design.class);
            design4 = gson.fromJson(intent.getStringExtra(DESIGN_4_OBJECT), Design.class);
        }
    }
}