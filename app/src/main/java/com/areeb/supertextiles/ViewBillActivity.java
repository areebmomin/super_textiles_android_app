package com.areeb.supertextiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import static com.areeb.supertextiles.AboutUs.getTextViewLongClickListener;

public class ViewBillActivity extends AppCompatActivity {

    TextView billNoValueTextView, billDateValueTextView, billChallanNoValueTextView, messersValueTextView,
            addressValueTextView, purchaserGSTValueTextView, contractNoBillValueTextView, billDatedValueTextView,
            brokerNameBillTextView, descriptionBillValueTextView, noOfPiecesValueTextView, quantityValueBillTextView,
            rateValueBillTextView, amountValueBillTextView, totalValueBillTextView, discountBillValueTextView,
            discountAmountValueBillTextView, netAmountValueBillTextView, SGSTValueBillTextView, CGSTValueBillTextView,
            IGSTValueBillTextView, amountAfterTaxValueBillTextView, EWayBillValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("View Bill");
        setContentView(R.layout.activity_view_bill);

        //initializing xml view
        billNoValueTextView = findViewById(R.id.billNoValueTextView);
        billDateValueTextView = findViewById(R.id.billDateValueTextView);
        billChallanNoValueTextView = findViewById(R.id.billChallanNoValueTextView);
        messersValueTextView = findViewById(R.id.messersValueTextView);
        addressValueTextView = findViewById(R.id.addressValueTextView);
        purchaserGSTValueTextView = findViewById(R.id.purchaserGSTValueTextView);
        contractNoBillValueTextView = findViewById(R.id.contractNoBillValueTextView);
        billDatedValueTextView = findViewById(R.id.billDatedValueTextView);
        brokerNameBillTextView = findViewById(R.id.brokerNameBillTextView);
        descriptionBillValueTextView = findViewById(R.id.descriptionBillValueTextView);
        noOfPiecesValueTextView = findViewById(R.id.noOfPiecesValueTextView);
        quantityValueBillTextView = findViewById(R.id.quantityValueBillTextView);
        rateValueBillTextView = findViewById(R.id.rateValueBillTextView);
        amountValueBillTextView = findViewById(R.id.amountValueBillTextView);
        totalValueBillTextView = findViewById(R.id.totalValueBillTextView);
        discountBillValueTextView = findViewById(R.id.discountBillValueTextView);
        discountAmountValueBillTextView = findViewById(R.id.discountAmountValueBillTextView);
        netAmountValueBillTextView = findViewById(R.id.netAmountValueBillTextView);
        SGSTValueBillTextView = findViewById(R.id.SGSTValueBillTextView);
        CGSTValueBillTextView = findViewById(R.id.CGSTValueBillTextView);
        IGSTValueBillTextView = findViewById(R.id.IGSTValueBillTextView);
        amountAfterTaxValueBillTextView = findViewById(R.id.amountAfterTaxValueBillTextView);
        EWayBillValueTextView = findViewById(R.id.EWayBillValueTextView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //copy text to clipboard when onLongPress on TextView
        billNoValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        billDateValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        billChallanNoValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        messersValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        addressValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        purchaserGSTValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        contractNoBillValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        billDatedValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        brokerNameBillTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        descriptionBillValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        noOfPiecesValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        quantityValueBillTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        rateValueBillTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        amountValueBillTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        totalValueBillTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        discountBillValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        discountAmountValueBillTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        netAmountValueBillTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        SGSTValueBillTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        CGSTValueBillTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        IGSTValueBillTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        amountAfterTaxValueBillTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        EWayBillValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_bill_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();

        if (title.equals(getString(R.string.bill_pdf))) {
            Toast.makeText(this, "PDF Downloaded", Toast.LENGTH_SHORT).show();
        }
        else if (title.equals(getString(R.string.edit_bill))) {
            //goto EditBillActivity
            Intent intent = new Intent(ViewBillActivity.this, EditBillActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}