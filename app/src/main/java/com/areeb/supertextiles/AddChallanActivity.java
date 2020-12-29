package com.areeb.supertextiles;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public class AddChallanActivity extends AppCompatActivity {

    Calendar calendar;
    TextInputLayout challanNoTextField, dateTextField, purchaserTextField, lotNoTextField, LRNoTextField,
            deliveryAtTextField, purchaserGSTField, qualityTextField, totalPiecesTextField, totalMetersTextField, foldTextField;
    Button addMetersButton, createChallanButton;

    //DatePicker dialog listener
    DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
        //set date
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //update date EditText
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dateTextField.getEditText().setText(simpleDateFormat.format(calendar.getTime()));
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

        //inflate purchaserTextField drop down
        String[] purchaserArray = {"Karni Saree", "Naresh textiles", "Shivdeep Textiles"};
        ArrayList<String> purchaserList = new ArrayList<>(Arrays.asList(purchaserArray));
        ArrayAdapter<String> purchaserAdapter = new ArrayAdapter<>(this, R.layout.drop_down_list_item, purchaserList);
        AutoCompleteTextView purchaserAutoCompleteTextView = (AutoCompleteTextView) purchaserTextField.getEditText();
        if (purchaserAutoCompleteTextView != null)
            purchaserAutoCompleteTextView.setAdapter(purchaserAdapter);

        //inflate deliveryAtTextField drop down
        String[] addressArray = {"Bhiwandi", "Thane", "Mumbai"};
        ArrayList<String> addressList = new ArrayList<>(Arrays.asList(addressArray));
        ArrayAdapter<String> addressAdapter = new ArrayAdapter<>(this, R.layout.drop_down_list_item, addressList);
        AutoCompleteTextView addressAutoCompleteTextView = (AutoCompleteTextView) deliveryAtTextField.getEditText();
        if (addressAutoCompleteTextView != null)
            addressAutoCompleteTextView.setAdapter(addressAdapter);
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
        Intent intent = new Intent(AddChallanActivity.this, AddMetersActivity.class);
        startActivity(intent);
    }

    //createChallanButton onClick Method
    public void createChallan(View view) {

        //empty form
        clearForm();

        //show success Toast
        Toast.makeText(this, "Challan created successfully", Toast.LENGTH_LONG).show();
    }

    //clear AddChallan form
    private void clearForm() {
        challanNoTextField.getEditText().getText().clear();
        dateTextField.getEditText().getText().clear();
        purchaserTextField.getEditText().getText().clear();
        lotNoTextField.getEditText().getText().clear();
        LRNoTextField.getEditText().getText().clear();
        deliveryAtTextField.getEditText().getText().clear();
        purchaserGSTField.getEditText().getText().clear();
        qualityTextField.getEditText().getText().clear();
        totalPiecesTextField.getEditText().getText().clear();
        totalMetersTextField.getEditText().getText().clear();
        foldTextField.getEditText().getText().clear();
    }
}