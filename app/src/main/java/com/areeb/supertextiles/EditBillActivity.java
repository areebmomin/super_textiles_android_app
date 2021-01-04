package com.areeb.supertextiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public class EditBillActivity extends AppCompatActivity {

    RadioButton SGST25RadioButton, SGST6RadioButton, CGST25RadioButton, CGST6RadioButton, IGST5RadioButton, IGST12RadioButton;
    TextInputLayout billNoEditBillTextField, challanNoEditBillTextField, dateEditBillTextField, messersEditBillTextField,
            addressEditBillTextField, purchaserGSTEditBillTextField, contractNoEditBillTextField, datedEditBillTextField,
            brokerEditBillTextField, eWayBillNoEditBillTextField, descriptionEditBillTextField, noOfPiecesEditBillTextField,
            quantityEditBillTextField, quantityDiscountEditBillTextField, quantityAfterDiscountEditBillTextField,
            rateEditBillTextField, amountEditBillTextField, totalEditBillTextField, discountEditBillTextField,
            discountAmountEditBillTextField, netAmountEditBillTextField, SGSTAmountEditBillTextField,
            CGSTAmountEditBillTextField, IGSTAmountEditBillTextField, amountAfterTaxEditBillTextField;
    Calendar calendar;

    //DatePicker dialog listener
    DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
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
            Toast.makeText(this, "Bill Edited", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    //RadioButton onClick method to set GST 2.5% twice or 5% once
    public void setGST25Or5(View view) {

        //select all the RadioButton with 2.5% and 5% value
        SGST25RadioButton.setChecked(true);
        CGST25RadioButton.setChecked(true);
        IGST5RadioButton.setChecked(true);
    }

    //RadioButton onCLick method to set GST 6% twice or 12% once
    public void setGST6Or12(View view) {

        //select all the RadioButton with 6% and 12% value
        SGST6RadioButton.setChecked(true);
        CGST6RadioButton.setChecked(true);
        IGST12RadioButton.setChecked(true);
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
}