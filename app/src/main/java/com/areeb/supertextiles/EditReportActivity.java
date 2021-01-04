package com.areeb.supertextiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public class EditReportActivity extends AppCompatActivity {

    TextInputLayout dateEditReportTextField, partyEditReportTextField, deliveryAddressEditReportTextField,
            piecesEditReportTextField, metersEditReportTextField, rateEditReportTextField, challanNoEditReportTextField,
            designNoEditReportTextField, lotNoEditReportTextField, amountEditReportTextField, GSTEditReportTextField,
            commissionEditReportTextField, netAmountEditReportTextField, GSTAmountEditReportTextField,
            totalGSTAmountEditReportTextField, finalAmountEditReportTextField, receivedAmountEditReportTextField,
            chequeNoEditReportTextField, chequeDateEditReportTextField, billNoEditReportTextField, transportEditReportTextField;
    Calendar calendar;

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
            Toast.makeText(this, "Report Edited", Toast.LENGTH_SHORT).show();
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
}