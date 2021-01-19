package com.areeb.supertextiles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.areeb.supertextiles.R;
import com.areeb.supertextiles.models.Report;
import com.google.gson.Gson;

import static com.areeb.supertextiles.activities.AboutUs.getTextViewLongClickListener;

public class ViewReportActivity extends AppCompatActivity {

    public static final String REPORT_OBJECT = "REPORT_OBJECT";
    TextView billNoReportValueTextView, dateReportValueTextView, reportChallanNoValueTextView, lotNoReportValueTextView,
            transportReportValueTextView, partyReportValueTextView, deliveryAddressReportValueTextView,
            piecesReportValueTextView, metersReportValueTextView, designNoReportValueTextView, amountReportValueTextView,
            rateReportValueTextView, GSTReportValueTextView, GSTAmountReportValueTextView, totalGSTReportValueTextView,
            commissionReportValueTextView, netAmountReportValueTextView, finalAmountReportValueTextView,
            receivedAmountReportValueTextView, chequeNoReportValueTextView, chequeDateReportValueTextView, discountPercentReportValueTextView;
    Report report;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("View Report");
        setContentView(R.layout.activity_view_report);

        //initializing xml views
        billNoReportValueTextView = findViewById(R.id.billNoReportValueTextView);
        dateReportValueTextView = findViewById(R.id.dateReportValueTextView);
        reportChallanNoValueTextView = findViewById(R.id.reportChallanNoValueTextView);
        lotNoReportValueTextView = findViewById(R.id.lotNoReportValueTextView);
        transportReportValueTextView = findViewById(R.id.transportReportValueTextView);
        partyReportValueTextView = findViewById(R.id.partyReportValueTextView);
        deliveryAddressReportValueTextView = findViewById(R.id.deliveryAddressReportValueTextView);
        piecesReportValueTextView = findViewById(R.id.piecesReportValueTextView);
        metersReportValueTextView = findViewById(R.id.metersReportValueTextView);
        designNoReportValueTextView = findViewById(R.id.designNoReportValueTextView);
        amountReportValueTextView = findViewById(R.id.amountReportValueTextView);
        rateReportValueTextView = findViewById(R.id.rateReportValueTextView);
        GSTReportValueTextView = findViewById(R.id.GSTReportValueTextView);
        GSTAmountReportValueTextView = findViewById(R.id.GSTAmountReportValueTextView);
        totalGSTReportValueTextView = findViewById(R.id.totalGSTReportValueTextView);
        commissionReportValueTextView = findViewById(R.id.commissionReportValueTextView);
        netAmountReportValueTextView = findViewById(R.id.netAmountReportValueTextView);
        finalAmountReportValueTextView = findViewById(R.id.finalAmountReportValueTextView);
        receivedAmountReportValueTextView = findViewById(R.id.receivedAmountReportValueTextView);
        chequeNoReportValueTextView = findViewById(R.id.chequeNoReportValueTextView);
        chequeDateReportValueTextView = findViewById(R.id.chequeDateReportValueTextView);
        discountPercentReportValueTextView = findViewById(R.id.discountPercentReportValueTextView);

        //initialize Gson object
        gson = new Gson();

        //initialize Report object
        report = new Report();

        //get data from intent
        if (getIntent() != null) {
            report = gson.fromJson(getIntent().getStringExtra(REPORT_OBJECT), Report.class);

            if (report != null) {
                billNoReportValueTextView.setText(report.getBillNo());
                dateReportValueTextView.setText(report.getDate());
                reportChallanNoValueTextView.setText(report.getChallanNo());
                lotNoReportValueTextView.setText(report.getLotNo());
                transportReportValueTextView.setText(report.getTransport());
                partyReportValueTextView.setText(report.getParty());
                deliveryAddressReportValueTextView.setText(report.getDeliveryAddress());
                piecesReportValueTextView.setText(report.getPieces());
                metersReportValueTextView.setText(report.getMeters());
                designNoReportValueTextView.setText(report.getDesignNo());
                rateReportValueTextView.setText(report.getRate());
                amountReportValueTextView.setText(report.getAmount());
                discountPercentReportValueTextView.setText(report.getDiscountPercent());
                commissionReportValueTextView.setText(report.getCommission());
                netAmountReportValueTextView.setText(report.getNetAmount());
                GSTAmountReportValueTextView.setText(report.getGstAmount());
                GSTReportValueTextView.setText(report.getGstPercent());
                totalGSTReportValueTextView.setText(report.getTotalGSTAmount());
                finalAmountReportValueTextView.setText(report.getFinalAmount());
                receivedAmountReportValueTextView.setText(report.getReceivedAmount());
                chequeNoReportValueTextView.setText(report.getChequeNo());
                chequeDateReportValueTextView.setText(report.getChequeDate());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //copy text to clipboard when onLongPress on TextView
        billNoReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        dateReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        reportChallanNoValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        lotNoReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        transportReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        partyReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        deliveryAddressReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        piecesReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        metersReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        designNoReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        amountReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        rateReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        GSTReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        GSTAmountReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        totalGSTReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        commissionReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        netAmountReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        finalAmountReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        receivedAmountReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        chequeNoReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        chequeDateReportValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_report_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();

        if (title.equals(getString(R.string.report_pdf))) {
            Toast.makeText(this, "PDF Downloaded", Toast.LENGTH_SHORT).show();
        }
        else if (title.equals(getString(R.string.edit_report))) {
            //goto EditReportActivity
            Intent intent = new Intent(ViewReportActivity.this, EditReportActivity.class);

            //send intent data if report object is not null
            if (report != null) {
                String reportJson = gson.toJson(report);
                intent.putExtra(REPORT_OBJECT, reportJson);
            }

            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}