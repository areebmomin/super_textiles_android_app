package com.areeb.supertextiles.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.areeb.supertextiles.R;
import com.areeb.supertextiles.models.Report;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.areeb.supertextiles.activities.AddChallanActivity.showErrorInTextField;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.CHEQUE_DATE;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.CHEQUE_NO;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.RECEIVED_AMOUNT;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.TRANSPORT;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getReportListDatabaseReference;
import static com.areeb.supertextiles.activities.ViewReportActivity.REPORT_OBJECT;

public class EditReportActivity extends AppCompatActivity {

    TextInputLayout dateEditReportTextField, partyEditReportTextField, deliveryAddressEditReportTextField,
            piecesEditReportTextField, metersEditReportTextField, rateEditReportTextField, challanNoEditReportTextField,
            designNoEditReportTextField, lotNoEditReportTextField, amountEditReportTextField, GSTEditReportTextField,
            commissionEditReportTextField, netAmountEditReportTextField, GSTAmountEditReportTextField,
            totalGSTAmountEditReportTextField, finalAmountEditReportTextField, receivedAmountEditReportTextField,
            chequeNoEditReportTextField, chequeDateEditReportTextField, billNoEditReportTextField, transportEditReportTextField,
            discountPercentEditReportTextField;
    DatabaseReference reportListDatabaseReference;
    Calendar calendar;
    Gson gson;
    Report report;

    //DatePicker dialog listener
    DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
        //set date
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //update date EditText
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Objects.requireNonNull(chequeDateEditReportTextField.getEditText()).setText(simpleDateFormat.format(calendar.getTime()));
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Report");
        setContentView(R.layout.activity_edit_report);

        //initializing xml views
        dateEditReportTextField = findViewById(R.id.dateEditReportTextField);
        partyEditReportTextField = findViewById(R.id.partyEditReportTextField);
        deliveryAddressEditReportTextField = findViewById(R.id.deliveryAddressEditReportTextField);
        piecesEditReportTextField = findViewById(R.id.piecesEditReportTextField);
        metersEditReportTextField = findViewById(R.id.metersEditReportTextField);
        rateEditReportTextField = findViewById(R.id.rateEditReportTextField);
        challanNoEditReportTextField = findViewById(R.id.challanNoEditReportTextField);
        designNoEditReportTextField = findViewById(R.id.designNoEditReportTextField);
        lotNoEditReportTextField = findViewById(R.id.lotNoEditReportTextField);
        amountEditReportTextField = findViewById(R.id.amountEditReportTextField);
        GSTEditReportTextField = findViewById(R.id.GSTEditReportTextField);
        commissionEditReportTextField = findViewById(R.id.commissionEditReportTextField);
        netAmountEditReportTextField = findViewById(R.id.netAmountEditReportTextField);
        GSTAmountEditReportTextField = findViewById(R.id.GSTAmountEditReportTextField);
        totalGSTAmountEditReportTextField = findViewById(R.id.totalGSTAmountEditReportTextField);
        finalAmountEditReportTextField = findViewById(R.id.finalAmountEditReportTextField);
        receivedAmountEditReportTextField = findViewById(R.id.receivedAmountEditReportTextField);
        chequeNoEditReportTextField = findViewById(R.id.chequeNoEditReportTextField);
        chequeDateEditReportTextField = findViewById(R.id.chequeDateEditReportTextField);
        billNoEditReportTextField = findViewById(R.id.billNoEditReportTextField);
        transportEditReportTextField = findViewById(R.id.transportEditReportTextField);
        discountPercentEditReportTextField = findViewById(R.id.discountPercentEditReportTextField);

        //initialize report object
        report = new Report();

        if (getIntent() != null) {
            //initialize Gson object
            gson = new Gson();

            //get Report object from intent
            report = gson.fromJson(getIntent().getStringExtra(REPORT_OBJECT), Report.class);

            //set data to EditText if report object not null
            if (report != null) {
                Objects.requireNonNull(dateEditReportTextField.getEditText()).setText(report.getDate().trim());
                Objects.requireNonNull(partyEditReportTextField.getEditText()).setText(report.getParty().trim());
                Objects.requireNonNull(deliveryAddressEditReportTextField.getEditText()).setText(report.getDeliveryAddress().trim());
                Objects.requireNonNull(piecesEditReportTextField.getEditText()).setText(report.getPieces().trim());
                Objects.requireNonNull(metersEditReportTextField.getEditText()).setText(report.getMeters().trim());
                Objects.requireNonNull(rateEditReportTextField.getEditText()).setText(report.getRate().trim());
                Objects.requireNonNull(challanNoEditReportTextField.getEditText()).setText(report.getChallanNo().trim());
                Objects.requireNonNull(designNoEditReportTextField.getEditText()).setText(report.getDesignNo().trim());
                Objects.requireNonNull(lotNoEditReportTextField.getEditText()).setText(report.getLotNo().trim());
                Objects.requireNonNull(amountEditReportTextField.getEditText()).setText(report.getAmount().trim());
                Objects.requireNonNull(discountPercentEditReportTextField.getEditText()).setText(report.getDiscountPercent().trim());
                Objects.requireNonNull(commissionEditReportTextField.getEditText()).setText(report.getCommission().trim());
                Objects.requireNonNull(netAmountEditReportTextField.getEditText()).setText(report.getNetAmount().trim());
                Objects.requireNonNull(GSTEditReportTextField.getEditText()).setText(report.getGstPercent().trim());
                Objects.requireNonNull(GSTAmountEditReportTextField.getEditText()).setText(report.getGstAmount().trim());
                Objects.requireNonNull(totalGSTAmountEditReportTextField.getEditText()).setText(report.getTotalGSTAmount().trim());
                Objects.requireNonNull(finalAmountEditReportTextField.getEditText()).setText(report.getFinalAmount().trim());
                Objects.requireNonNull(receivedAmountEditReportTextField.getEditText()).setText(report.getReceivedAmount().trim());
                Objects.requireNonNull(chequeNoEditReportTextField.getEditText()).setText(report.getChequeNo().trim());
                Objects.requireNonNull(chequeDateEditReportTextField.getEditText()).setText(report.getChequeDate().trim());
                Objects.requireNonNull(billNoEditReportTextField.getEditText()).setText(report.getBillNo().trim());
                Objects.requireNonNull(transportEditReportTextField.getEditText()).setText(report.getTransport().trim());

                //set filter in receivedAmountEditReportTextField
                //to not allow user to enter amount more than final amount
                if (!report.getFinalAmount().trim().isEmpty()) {
                    receivedAmountEditReportTextField.getEditText().setFilters(new InputFilterMinMax[]{new InputFilterMinMax("0", report.getFinalAmount())});
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_report_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();

        if (title.equals(getString(R.string.done))) {

            //check for empty field in edit bill form
            boolean isAnyFieldEmpty = checkForEmptyField();

            if (!isAnyFieldEmpty) {
                String receivedAmount = Objects.requireNonNull(receivedAmountEditReportTextField.getEditText()).getText().toString();
                String chequeNo = Objects.requireNonNull(chequeNoEditReportTextField.getEditText()).getText().toString();
                String chequeDate = Objects.requireNonNull(chequeDateEditReportTextField.getEditText()).getText().toString();
                String transport = Objects.requireNonNull(transportEditReportTextField.getEditText()).getText().toString();
                String billNo = Objects.requireNonNull(billNoEditReportTextField.getEditText()).getText().toString();

                //initialize database reference
                reportListDatabaseReference = getReportListDatabaseReference().child(billNo);

                //send receivedAmount to database if not empty
                if (!receivedAmount.isEmpty()) {
                    reportListDatabaseReference.child(RECEIVED_AMOUNT).setValue(receivedAmount);
                }

                //send chequeNo to database if not empty
                if (!chequeNo.isEmpty()) {
                    reportListDatabaseReference.child(CHEQUE_NO).setValue(chequeNo);
                }

                //send chequeDate to database if not empty
                if (!chequeDate.isEmpty()) {
                    reportListDatabaseReference.child(CHEQUE_DATE).setValue(chequeDate);
                }

                //send transport to database if not empty
                if (!transport.isEmpty()) {
                    reportListDatabaseReference.child(TRANSPORT).setValue(transport);
                }

                Toast.makeText(this, "Report Edited", Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                Toast.makeText(this, "Enter required fields", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectDate(View view) {
        //initialize calender
        calendar = Calendar.getInstance();

        //create DatePicker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditReportActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setButton(BUTTON_POSITIVE, "Set", datePickerDialog);
        datePickerDialog.show();
    }

    //method to check for empty field in edit report form
    //return true if any mandatory field is found empty
    //return false if all mandatory fields are filled
    private boolean checkForEmptyField() {

        //remove all the previous errors
        dateEditReportTextField.setErrorEnabled(false);
        partyEditReportTextField.setErrorEnabled(false);
        deliveryAddressEditReportTextField.setErrorEnabled(false);
        piecesEditReportTextField.setErrorEnabled(false);
        metersEditReportTextField.setErrorEnabled(false);
        rateEditReportTextField.setErrorEnabled(false);
        challanNoEditReportTextField.setErrorEnabled(false);
        designNoEditReportTextField.setErrorEnabled(false);
        amountEditReportTextField.setErrorEnabled(false);
        GSTEditReportTextField.setErrorEnabled(false);
        netAmountEditReportTextField.setErrorEnabled(false);
        GSTAmountEditReportTextField.setErrorEnabled(false);
        totalGSTAmountEditReportTextField.setErrorEnabled(false);
        finalAmountEditReportTextField.setErrorEnabled(false);
        billNoEditReportTextField.setErrorEnabled(false);

        String data = Objects.requireNonNull(dateEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(dateEditReportTextField, "Date required");
            return true;
        }

        data = Objects.requireNonNull(partyEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(partyEditReportTextField, "Party required");
            return true;
        }

        data = Objects.requireNonNull(deliveryAddressEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(deliveryAddressEditReportTextField, "Delivery Address required");
            return true;
        }

        data = Objects.requireNonNull(piecesEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(piecesEditReportTextField, "Pieces required");
            return true;
        }

        data = Objects.requireNonNull(metersEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(metersEditReportTextField, "Meters required");
            return true;
        }

        data = Objects.requireNonNull(rateEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(rateEditReportTextField, "Rate required");
            return true;
        }

        data = Objects.requireNonNull(challanNoEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(challanNoEditReportTextField, "Challan No. required");
            return true;
        }

        data = Objects.requireNonNull(designNoEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(designNoEditReportTextField, "Design No. required");
            return true;
        }

        data = Objects.requireNonNull(amountEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(amountEditReportTextField, "Amount required");
            return true;
        }

        data = Objects.requireNonNull(GSTEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(GSTEditReportTextField, "GST % required");
            return true;
        }

        data = Objects.requireNonNull(netAmountEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(netAmountEditReportTextField, "Net Amount required");
            return true;
        }

        data = Objects.requireNonNull(GSTAmountEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(GSTAmountEditReportTextField, "GST Amount required");
            return true;
        }

        data = Objects.requireNonNull(totalGSTAmountEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(totalGSTAmountEditReportTextField, "Total GST Amount required");
            return true;
        }

        data = Objects.requireNonNull(finalAmountEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(finalAmountEditReportTextField, "Final Amount required");
            return true;
        }

        data = Objects.requireNonNull(billNoEditReportTextField.getEditText()).getText().toString();
        if (data.isEmpty()) {
            showErrorInTextField(billNoEditReportTextField, "Bill No. required");
            return true;
        }

        return false;
    }
}