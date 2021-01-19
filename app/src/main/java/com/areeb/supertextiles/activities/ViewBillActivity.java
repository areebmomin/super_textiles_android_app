package com.areeb.supertextiles.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.areeb.supertextiles.R;
import com.areeb.supertextiles.models.Bill;
import com.areeb.supertextiles.models.Challan;
import com.areeb.supertextiles.models.Design;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import static com.areeb.supertextiles.activities.AboutUs.getTextViewLongClickListener;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.MY_PERMISSIONS_REQUEST_STORAGE;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.createStructureAndAddDataInChallanPDF;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.galleryAddFile;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.DESIGN_1;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.DESIGN_2;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.DESIGN_3;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.DESIGN_4;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getChallanListDatabaseReference;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getDesignDataDatabaseReference;

public class ViewBillActivity extends AppCompatActivity {

    public static final String BILL_OBJECT = "BILL_OBJECT";
    TextView billNoValueTextView, billDateValueTextView, billChallanNoValueTextView, messersValueTextView,
            addressValueTextView, purchaserGSTValueTextView, contractNoBillValueTextView, billDatedValueTextView,
            brokerNameBillTextView, descriptionBillValueTextView, noOfPiecesValueTextView, quantityValueBillTextView,
            rateValueBillTextView, amountValueBillTextView, totalValueBillTextView, discountBillValueTextView,
            discountAmountValueBillTextView, netAmountValueBillTextView, SGSTValueBillTextView, CGSTValueBillTextView,
            IGSTValueBillTextView, amountAfterTaxValueBillTextView, EWayBillValueTextView, discountOnQuantityValueBillTextView,
            quantityAfterDiscountValueBillTextView, SGSTBillTextView, CGSTBillTextView, IGSTBillTextView;
    ConstraintLayout viewBillConstraintLayout;
    Bill bill;
    Gson gson;
    MenuItem PDFMenuItem;
    View PDFOptionView;

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
        discountOnQuantityValueBillTextView = findViewById(R.id.discountOnQuantityValueBillTextView);
        quantityAfterDiscountValueBillTextView = findViewById(R.id.quantityAfterDiscountValueBillTextView);
        SGSTBillTextView = findViewById(R.id.SGSTBillTextView);
        CGSTBillTextView = findViewById(R.id.CGSTBillTextView);
        IGSTBillTextView = findViewById(R.id.IGSTBillTextView);
        viewBillConstraintLayout = findViewById(R.id.viewBillConstraintLayout);

        //initialize bill object
        bill = new Bill();

        //initialize Gson object
        gson = new Gson();

        //get intent data in bill object
        if (getIntent() != null) {
            bill = gson.fromJson(getIntent().getStringExtra(BILL_OBJECT), Bill.class);

            //set intent data to TextView's if not null
            if (bill != null) {
                new Thread(() -> {
                    billNoValueTextView.setText(bill.getBillNo());
                    billDateValueTextView.setText(bill.getDate());
                    StringBuilder challanNoList = new StringBuilder();
                    Iterator<String> challanNoIterator = bill.getChallanNo().iterator();
                    while (challanNoIterator.hasNext()) {
                        String challanNo = challanNoIterator.next();
                        if (challanNoIterator.hasNext()) {
                            challanNoList.append(challanNo).append(", ");
                        } else {
                            challanNoList.append(challanNo);
                        }
                    }

                    billChallanNoValueTextView.setText(challanNoList.toString());
                    EWayBillValueTextView.setText(bill.geteWayBillNo());
                    messersValueTextView.setText(bill.getMessers());
                    addressValueTextView.setText(bill.getAddress());
                    purchaserGSTValueTextView.setText(bill.getPurchaserGst());
                    contractNoBillValueTextView.setText(bill.getContractNo());
                    billDatedValueTextView.setText(bill.getDated());
                    brokerNameBillTextView.setText(bill.getBroker());
                    descriptionBillValueTextView.setText(bill.getDescription());
                    noOfPiecesValueTextView.setText(bill.getNoOfPieces());
                    quantityValueBillTextView.setText(bill.getQuantity());
                    rateValueBillTextView.setText(bill.getRate());
                    amountValueBillTextView.setText(bill.getAmount());
                    totalValueBillTextView.setText(bill.getTotal());
                    discountBillValueTextView.setText(bill.getDiscount());
                    discountAmountValueBillTextView.setText(bill.getDiscountAmount());
                    netAmountValueBillTextView.setText(bill.getNetAmount());
                    SGSTValueBillTextView.setText(bill.getSGSTAmount());
                    CGSTValueBillTextView.setText(bill.getCGSTAmount());
                    IGSTValueBillTextView.setText(bill.getIGSTAmount());
                    amountAfterTaxValueBillTextView.setText(bill.getAmountAfterTax());
                    discountOnQuantityValueBillTextView.setText(bill.getDiscountOnQuantity());
                    quantityAfterDiscountValueBillTextView.setText(bill.getQuantityAfterDiscount());
                    SGSTBillTextView.setText(getString(R.string.sgst_bill_text_view, bill.getSGSTPercent()));
                    CGSTBillTextView.setText(getString(R.string.cgst_bill_text_view, bill.getCGSTPercent()));
                    IGSTBillTextView.setText(getString(R.string.igst_bill_text_view, bill.getIGSTPercent()));
                }).start();
            }
        }
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
        discountOnQuantityValueBillTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        quantityAfterDiscountValueBillTextView.setOnLongClickListener(getTextViewLongClickListener(this));
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
            //store item and item action view
            PDFMenuItem = item;
            PDFOptionView = item.getActionView();

            //initialize progress bar
            ProgressBar progressBar = new ProgressBar(this);
            progressBar.setIndeterminate(true);
            progressBar.setScaleX(0.7f);
            progressBar.setScaleY(0.7f);

            //replace PDF icon with progress bar
            item.setActionView(progressBar);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    createBillPdf();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
                }
            } else {
                createBillPdf();
            }
        } else if (title.equals(getString(R.string.edit_bill))) {
            //goto EditBillActivity
            Intent intent = new Intent(ViewBillActivity.this, EditBillActivity.class);

            //send intent data if bill object is not null
            if (bill != null) {
                String billJson = gson.toJson(bill);
                intent.putExtra(BILL_OBJECT, billJson);
            }

            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_STORAGE) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    createBillPdf();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
            } else {
                createBillPdf();
            }
        }
    }

    private void createBillPdf() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1300, 1900, 1).create();
        final PdfDocument.Page[] page = {null};

        //initialize Paint to draw text
        Paint paint = new Paint();
        paint.setTextSize(30);

        //get challan array list
        ArrayList<String> challanList = bill.getChallanNo();
        int numberOfPages = challanList.size();

        //iterate through challan list and create each challan in new page
        new Thread(() -> {
            for (int i = 0; i < numberOfPages; i++) {
                int finalI = i;
                final Challan[] challan = {new Challan()};
                final Design[] designs = {new Design(), new Design(), new Design(), new Design()};

                //get challan object from database
                getChallanListDatabaseReference().child(challanList.get(finalI)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        challan[0] = snapshot.getValue(Challan.class);

                        //get design data from database
                        getDesignDataDatabaseReference(challanList.get(finalI)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child(DESIGN_1).exists()) {
                                    designs[0] = snapshot.child(DESIGN_1).getValue(Design.class);
                                }
                                if (snapshot.child(DESIGN_2).exists()) {
                                    designs[1] = snapshot.child(DESIGN_2).getValue(Design.class);
                                }
                                if (snapshot.child(DESIGN_3).exists()) {
                                    designs[2] = snapshot.child(DESIGN_3).getValue(Design.class);
                                }
                                if (snapshot.child(DESIGN_4).exists()) {
                                    designs[3] = snapshot.child(DESIGN_4).getValue(Design.class);
                                }

                                //add data in challan PDF
                                page[0] = document.startPage(pageInfo);
                                createStructureAndAddDataInChallanPDF(page[0], paint, getApplicationContext(), challan[0], designs[0], designs[1], designs[2], designs[3]);
                                document.finishPage(page[0]);

                                if (finalI == numberOfPages - 1) {
                                    //create new page
                                    page[0] = document.startPage(pageInfo);
                                    createStructureAndAddDataInBillPDF(paint, page[0], document);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                error.toException().printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        error.toException().printStackTrace();
                    }
                });
            }
        }).start();
    }

    //method to print challan structure and challan data in PDF file
    private void createStructureAndAddDataInBillPDF(Paint paint, PdfDocument.Page page, PdfDocument document) {
        //initialize Y positions
        int y = 50;

        //draw rectangle for side margin
        paint.setTextSize(30);
        page.getCanvas().drawRGB(255, 255, 255);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        page.getCanvas().drawRect(50, 50, 1250, 1850, paint);
        paint.setStrokeWidth(1);

        //draw rectangle for business GSTNo
        String businessGSTNoString = "GSTIN : " + getString(R.string.business_gst_no);
        int leftPositionBusinessGST = 650 - (int) (paint.measureText(businessGSTNoString) / 2) - 10;
        int rightPositionBusinessGST = 650 + (int) (paint.measureText(businessGSTNoString) / 2) + 10;
        page.getCanvas().drawRoundRect(leftPositionBusinessGST, 270, rightPositionBusinessGST, 335, 10, 10, paint);

        //draw rectangle for manufacturing details
        paint.setStyle(Paint.Style.FILL);
        page.getCanvas().drawRect(52, 170, 1248, 220, paint);

        //write business name heading
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        y += ((paint.descent() - paint.ascent()) * 2) + 15;
        paint.setFlags(0);
        paint.setLetterSpacing(0.1f);
        paint.setTextSize(80);
        paint.setShadowLayer(12, 15, 15, Color.LTGRAY);
        String businessNameString = getString(R.string.app_name);
        int superTextilesXPosition = 650 - (int) (paint.measureText(businessNameString) / 2);
        page.getCanvas().drawText(businessNameString, superTextilesXPosition, y, paint);

        //write manufacturing details
        paint.clearShadowLayer();
        paint.setLetterSpacing(0);
        paint.setStyle(Paint.Style.FILL);
        y += (paint.descent() - paint.ascent()) - 20;
        paint.setTextSize(35);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String manufacturingDetailsString = "Manufacturers of : GREY COTTON FABRICS";
        int manufacturingXPosition = 650 - (int) (paint.measureText(manufacturingDetailsString) / 2);
        page.getCanvas().drawText(manufacturingDetailsString, manufacturingXPosition, y, paint);

        //write business address
        paint.setColor(Color.BLACK);
        paint.setTextSize(25);
        y += (paint.descent() - paint.ascent()) + 10;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        String businessAddress = getString(R.string.business_address);
        int businessAddressXPosition = 650 - (int) (paint.measureText(businessAddress) / 2);
        page.getCanvas().drawText(businessAddress, businessAddressXPosition, y, paint);

        //write business GST number
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        y += (paint.descent() - paint.ascent()) + 30;
        int businessGSTXPosition = 650 - (int) (paint.measureText(businessGSTNoString) / 2);
        page.getCanvas().drawText(businessGSTNoString, businessGSTXPosition, y, paint);

        //write date sub-heading and value
        String dateValue = bill.getDate();
        y += (paint.descent() - paint.ascent());
        int dateValueXPosition = 1210 - (int) (paint.measureText(dateValue));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        page.getCanvas().drawText(dateValue, dateValueXPosition + 10, y - 3, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String dateString = "Date";
        page.getCanvas().drawText(dateString, dateValueXPosition - 80, y, paint);
        page.getCanvas().drawLine(dateValueXPosition, y + 5, 1230, y + 5, paint);

        //write bill no sub-heading and value
        paint.setStrokeWidth(2);
        String billNoString = "Bill No.";
        page.getCanvas().drawText(billNoString, 80, y, paint);
        int billNoValueXPosition = 80 + (int) (paint.measureText(billNoString) + 10);
        String billNoValueString = bill.getBillNo();
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        page.getCanvas().drawText(billNoValueString, billNoValueXPosition + 10, y, paint);
        int billNoRectangleRightPosition = billNoValueXPosition + (int) (paint.measureText(billNoValueString)) + 20;
        paint.setStyle(Paint.Style.STROKE);
        page.getCanvas().drawRect(70, y - 40, billNoRectangleRightPosition, y + 20, paint);
        paint.setStyle(Paint.Style.FILL);

        //write challan no sub-heading and value
        y += (paint.descent() - paint.ascent()) + 40;
        String challanNoString = "Challan No.";
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(challanNoString, 70, y, paint);
        int challanNoValueXPosition = 70 + (int) (paint.measureText(challanNoString) + 10);
        StringBuilder challanNoValueString = new StringBuilder();
        Iterator<String> challanNoIterator = bill.getChallanNo().iterator();
        while (challanNoIterator.hasNext()) {
            String challanNo = challanNoIterator.next();
            if (challanNoIterator.hasNext()) {
                challanNoValueString.append(challanNo).append(", ");
            } else {
                challanNoValueString.append(challanNo);
            }
        }

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        if (challanNoValueString.toString().length() > 65) {
            String firstLineOfChallanNo = challanNoValueString.toString().substring(0, 65);
            String secondLineOfChallanNo = challanNoValueString.toString().substring(65);
            if (secondLineOfChallanNo.length() > 65)
                secondLineOfChallanNo = secondLineOfChallanNo.substring(0, 65) + "...";

            //print line 1 of challan no list
            page.getCanvas().drawText(firstLineOfChallanNo, challanNoValueXPosition + 10, y - 3, paint);
            int challanValueLine1EndX = challanNoValueXPosition + (int) (paint.measureText(firstLineOfChallanNo) + 20);
            page.getCanvas().drawLine(challanNoValueXPosition, y + 5, challanValueLine1EndX, y + 5, paint);

            //print line 2 of challan no list
            y += (paint.descent() - paint.ascent()) + 20;
            page.getCanvas().drawText(secondLineOfChallanNo, challanNoValueXPosition + 10, y - 3, paint);
            int challanValueLine2EndX = challanNoValueXPosition + (int) (paint.measureText(secondLineOfChallanNo) + 20);
            page.getCanvas().drawLine(challanNoValueXPosition, y + 5, challanValueLine2EndX, y + 5, paint);

        } else {
            page.getCanvas().drawText(challanNoValueString.toString(), challanNoValueXPosition + 10, y - 3, paint);
            int challanValueLineEndX = challanNoValueXPosition + (int) (paint.measureText(challanNoValueString.toString()) + 20);
            page.getCanvas().drawLine(challanNoValueXPosition, y + 5, challanValueLineEndX, y + 5, paint);
        }

        //write purchaser name sub-heading and value
        y += (paint.descent() - paint.ascent()) + 30;
        String messersString = "Messers";
        int messersStringWidth = (int) paint.measureText(messersString);
        int messersNamePositionX = 80 + messersStringWidth;
        String messersNameString = bill.getMessers();
        if (messersNameString.length() > 65)
            messersNameString = messersNameString.substring(0, 65) + "...";
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(messersString, 70, y, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        page.getCanvas().drawText(messersNameString, messersNamePositionX + 10, y - 3, paint);
        page.getCanvas().drawLine(messersNamePositionX, y + 5, 1230, y + 5, paint);

        //write address sub-heading and value
        y += (paint.descent() - paint.ascent()) + 30;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String addressString = "Address";
        int addressStringWidth = (int) paint.measureText(addressString);
        int addressValuePositionX = 80 + addressStringWidth;
        String addressValueString = bill.getAddress();
        if (addressValueString.length() > 65)
            addressValueString = addressValueString.substring(0, 65) + "...";
        page.getCanvas().drawText(addressString, 70, y, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        page.getCanvas().drawText(addressValueString, addressValuePositionX + 10, y - 3, paint);
        page.getCanvas().drawLine(addressValuePositionX, y + 5, 1230, y + 5, paint);

        //write contract no sub-heading and value
        y += (paint.descent() - paint.ascent()) + 30;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String contractNoString = "Contract No.";
        page.getCanvas().drawText(contractNoString, 70, y, paint);
        int contractNoValueXPosition = 70 + (int) (paint.measureText(contractNoString) + 10);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        String contractNoValueString = bill.getContractNo();
        page.getCanvas().drawText(contractNoValueString, contractNoValueXPosition + 10, y - 3, paint);
        page.getCanvas().drawLine(contractNoValueXPosition, y + 5, 430, y + 5, paint);

        //write dated no sub-heading and value
        String datedString = "Dated";
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(datedString, 450, y, paint);
        int datedStringWidth = (int) paint.measureText(datedString);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        String datedValue = bill.getDated();
        int datedValueXPosition = 470 + datedStringWidth;
        page.getCanvas().drawText(datedValue, datedValueXPosition, y - 3, paint);
        page.getCanvas().drawLine(460 + datedStringWidth, y + 5, 830, y + 5, paint);

        //write broker no sub-heading and value
        String brokerString = "Broker";
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(brokerString, 850, y, paint);
        int brokerStringWidth = (int) paint.measureText(brokerString);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        String brokerValue = bill.getBroker();
        int brokerValueXPosition = 870 + brokerStringWidth;
        page.getCanvas().drawText(brokerValue, brokerValueXPosition, y - 3, paint);
        page.getCanvas().drawLine(860 + brokerStringWidth, y + 5, 1230, y + 5, paint);

        //write purchaser GSTIN sub-heading and value
        y += (paint.descent() - paint.ascent()) + 30;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String purchaserGSTString = "Purchaser GSTIN";
        int purchaserGStStringWidth = (int) paint.measureText(purchaserGSTString);
        int purchaserGSTValuePositionX = 80 + purchaserGStStringWidth;
        String purchaserGSTValueString = bill.getPurchaserGst();
        if (purchaserGSTValueString.length() > 65)
            purchaserGSTValueString = purchaserGSTValueString.substring(0, 65) + "...";
        page.getCanvas().drawText(purchaserGSTString, 70, y, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        page.getCanvas().drawText(purchaserGSTValueString, purchaserGSTValuePositionX + 10, y - 3, paint);
        page.getCanvas().drawLine(purchaserGSTValuePositionX, y + 5, 1230, y + 5, paint);

        //draw table header lines and column lines
        y += (paint.descent() - paint.ascent());
        page.getCanvas().drawLine(50, y, 1250, y, paint);
        page.getCanvas().drawLine(50, y + 80, 1250, y + 80, paint);
        page.getCanvas().drawLine(450, y, 450, y + 784, paint);
        page.getCanvas().drawLine(575, y, 575, y + 784, paint);
        page.getCanvas().drawLine(825, y, 825, y + 439, paint);
        page.getCanvas().drawLine(950, y, 950, y + 784, paint);

        //write table heading
        paint.setTextSize(25);
        y += (paint.descent() - paint.ascent());
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String descriptionString = "Description";
        String noOfPiecesLine1String = "No. of";
        String noOfPiecesLine2String = "Pieces";
        String quantityLine1String = "Quantity";
        String quantityLine2String = "Mts./Kgs.";
        String rateLine1String = "Rate per";
        String rateLine2String = "Mts./Kgs.";
        String amountLine1String = "Amount";
        String amountLine2StringRupees = "Rs.";
        String amountLine2StringPaise = "Ps.";

        int descriptionMiddleXPosition = 250 - (int) (paint.measureText(descriptionString) / 2);
        page.getCanvas().drawText(descriptionString, descriptionMiddleXPosition, y + 15, paint);

        int noOfPiecesLine1MiddleXPosition = 512 - (int) (paint.measureText(noOfPiecesLine1String) / 2);
        page.getCanvas().drawText(noOfPiecesLine1String, noOfPiecesLine1MiddleXPosition, y + 2, paint);

        int quantityLine1MiddleXPosition = 700 - (int) (paint.measureText(quantityLine1String) / 2);
        page.getCanvas().drawText(quantityLine1String, quantityLine1MiddleXPosition, y + 2, paint);

        int rateLine1MiddleXPosition = 887 - (int) (paint.measureText(rateLine1String) / 2);
        page.getCanvas().drawText(rateLine1String, rateLine1MiddleXPosition, y + 1, paint);

        int amountLine1MiddleXPosition = 1125 - (int) (paint.measureText(amountLine1String) / 2);
        page.getCanvas().drawText(amountLine1String, amountLine1MiddleXPosition, y, paint);

        y += (paint.descent() - paint.ascent());

        int noOfPiecesLine2MiddleXPosition = 512 - (int) (paint.measureText(noOfPiecesLine2String) / 2);
        page.getCanvas().drawText(noOfPiecesLine2String, noOfPiecesLine2MiddleXPosition, y + 2, paint);

        int quantityLine2MiddleXPosition = 700 - (int) (paint.measureText(quantityLine2String) / 2);
        page.getCanvas().drawText(quantityLine2String, quantityLine2MiddleXPosition, y + 2, paint);

        int rateLine2MiddleXPosition = 887 - (int) (paint.measureText(rateLine2String) / 2);
        page.getCanvas().drawText(rateLine2String, rateLine2MiddleXPosition, y + 1, paint);

        int amountLine2MiddleXPosition = 1050 - (int) (paint.measureText(amountLine2StringRupees) / 2);
        page.getCanvas().drawText(amountLine2StringRupees, amountLine2MiddleXPosition, y + 10, paint);

        page.getCanvas().drawText(amountLine2StringPaise, 1201, y + 10, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        //draw vertical line for Paise
        y += (paint.descent() - paint.ascent()) - 6;
        page.getCanvas().drawLine(1200, y, 1200, y + 701, paint);

        //write bill data in table
        int tableDataYPosition = y + 60;
        paint.setTextSize(30);
        String descriptionValueString = bill.getDescription();
        String noOfPiecesValueString = bill.getNoOfPieces();
        String quantityValueString = bill.getQuantity();
        String discountOnQuantityString = bill.getDiscountOnQuantity();
        String quantityAfterDiscount = bill.getQuantityAfterDiscount();
        String rateValueString = bill.getRate();
        String amountValueString = bill.getAmount();

        descriptionMiddleXPosition = 250 - (int) (paint.measureText(descriptionValueString) / 2);
        page.getCanvas().drawText(descriptionValueString, descriptionMiddleXPosition, tableDataYPosition, paint);
        noOfPiecesLine1MiddleXPosition = 512 - (int) (paint.measureText(noOfPiecesValueString) / 2);
        page.getCanvas().drawText(noOfPiecesValueString, noOfPiecesLine1MiddleXPosition, tableDataYPosition, paint);
        quantityLine1MiddleXPosition = 700 - (int) (paint.measureText(quantityValueString) / 2);
        page.getCanvas().drawText(quantityValueString, quantityLine1MiddleXPosition, tableDataYPosition, paint);
        if (!discountOnQuantityString.isEmpty()) {
            double discountOnQuantityDouble = Double.parseDouble(discountOnQuantityString);
            if (discountOnQuantityDouble > 0) {
                quantityLine1MiddleXPosition = 700 - (int) (paint.measureText("- " + discountOnQuantityString) / 2);
                page.getCanvas().drawText("- " + discountOnQuantityString, quantityLine1MiddleXPosition, tableDataYPosition + 45, paint);
                page.getCanvas().drawLine(575, tableDataYPosition + 60, 825, tableDataYPosition + 60, paint);
                quantityLine1MiddleXPosition = 700 - (int) (paint.measureText(quantityAfterDiscount) / 2);
                page.getCanvas().drawText(quantityAfterDiscount, quantityLine1MiddleXPosition, tableDataYPosition + 100, paint);
            }
        }
        rateLine1MiddleXPosition = 887 - (int) (paint.measureText(rateValueString) / 2);
        page.getCanvas().drawText(rateValueString, rateLine1MiddleXPosition, tableDataYPosition, paint);
        amountLine2MiddleXPosition = 1050 - (int) (paint.measureText(amountLine2StringRupees) / 2);
        page.getCanvas().drawText(amountValueString, amountLine2MiddleXPosition, tableDataYPosition, paint);

        //draw horizontal line in middle of table right side
        page.getCanvas().drawLine(575, y + 357, 1250, y + 357, paint);

        //write e way bill no
        paint.setTextSize(30);
        page.getCanvas().drawText("E-Way Bill:" + bill.geteWayBillNo(), 60, y + 530, paint);

        //draw horizontal line in middle of table left side for bank details
        page.getCanvas().drawLine(50, y + 550, 450, y + 550, paint);

        //write bank details of business in left bottom corner
        paint.setTextSize(25);
        int bankDetailsYPosition = y + 545 + (int) (paint.descent() - paint.ascent());
        String bankNameString = "Bank Name : " + getString(R.string.business_bank_name_value);
        String branchString = "Branch : " + getString(R.string.business_branch_name_value);
        String accountNoString = "A/c No.: " + getString(R.string.business_account_no_value);
        String IFSCCodeString = "IFSC Code : " + getString(R.string.business_ifsc_code_value);
        String typeOfAccountString = "Type of A/c : " + getString(R.string.business_bank_account_type_value);

        page.getCanvas().drawText(bankNameString, 60, bankDetailsYPosition, paint);
        bankDetailsYPosition += (int) (paint.descent() - paint.ascent());
        page.getCanvas().drawText(branchString, 60, bankDetailsYPosition, paint);
        bankDetailsYPosition += (int) (paint.descent() - paint.ascent());
        page.getCanvas().drawText(accountNoString, 60, bankDetailsYPosition, paint);
        bankDetailsYPosition += (int) (paint.descent() - paint.ascent());
        page.getCanvas().drawText(IFSCCodeString, 60, bankDetailsYPosition, paint);
        bankDetailsYPosition += (int) (paint.descent() - paint.ascent());
        page.getCanvas().drawText(typeOfAccountString, 60, bankDetailsYPosition, paint);

        //draw line for amount, total amount, GST amount in bottom right corner
        int amountLineYPosition = y + 357;
        for (int i = 1; i <= 6; i++) {
            amountLineYPosition += 50;
            page.getCanvas().drawLine(950, amountLineYPosition, 1250, amountLineYPosition, paint);
        }

        //write total, amount, GST text in bottom right corner of table
        paint.setTextSize(30);
        int amountStringYPosition = y + 357 + (int) (paint.descent() - paint.ascent()) + 3;
        String totalString = "Total";
        String lessDiscountString = "Less Discount " + bill.getDiscount() + "%";
        String netAmountString = "Net Amount";
        String SGSTString = "SGST @ " + bill.getSGSTPercent() + "% " + " Total";
        String CGSTString = "CGST @ " + bill.getCGSTPercent() + "% " + " Total";
        String IGSTString = "IGST @ " + bill.getIGSTPercent() + "% " + " Total";
        String amountAfterTaxString = "Total Amt. after Tax";

        int tempXForString = 950 - (int) paint.measureText(totalString) - 10;
        page.getCanvas().drawText(totalString, tempXForString, amountStringYPosition, paint);
        page.getCanvas().drawText(bill.getTotal(), 960, amountStringYPosition, paint);

        tempXForString = 950 - (int) paint.measureText(lessDiscountString) - 10;
        amountStringYPosition += (int) (paint.descent() - paint.ascent()) + 12;
        page.getCanvas().drawText(lessDiscountString, tempXForString, amountStringYPosition, paint);
        page.getCanvas().drawText(bill.getDiscount(), 960, amountStringYPosition, paint);

        tempXForString = 950 - (int) paint.measureText(netAmountString) - 10;
        amountStringYPosition += (int) (paint.descent() - paint.ascent()) + 15;
        page.getCanvas().drawText(netAmountString, tempXForString, amountStringYPosition, paint);
        page.getCanvas().drawText(bill.getNetAmount(), 960, amountStringYPosition, paint);

        tempXForString = 950 - (int) paint.measureText(SGSTString) - 10;
        amountStringYPosition += (int) (paint.descent() - paint.ascent()) + 15;
        page.getCanvas().drawText(bill.getSGSTAmount(), 960, amountStringYPosition, paint);
        page.getCanvas().drawText(SGSTString, tempXForString, amountStringYPosition, paint);

        tempXForString = 950 - (int) paint.measureText(CGSTString) - 10;
        amountStringYPosition += (int) (paint.descent() - paint.ascent()) + 15;
        page.getCanvas().drawText(CGSTString, tempXForString, amountStringYPosition, paint);
        page.getCanvas().drawText(bill.getCGSTAmount(), 960, amountStringYPosition, paint);

        tempXForString = 950 - (int) paint.measureText(IGSTString) - 10;
        amountStringYPosition += (int) (paint.descent() - paint.ascent()) + 16;
        page.getCanvas().drawText(IGSTString, tempXForString, amountStringYPosition, paint);
        page.getCanvas().drawText(bill.getIGSTAmount(), 960, amountStringYPosition, paint);

        tempXForString = 950 - (int) paint.measureText(amountAfterTaxString) - 10;
        amountStringYPosition += (int) (paint.descent() - paint.ascent()) + 13;
        page.getCanvas().drawText(amountAfterTaxString, tempXForString, amountStringYPosition, paint);
        page.getCanvas().drawText(bill.getAmountAfterTax(), 960, amountStringYPosition, paint);

        //draw bottom line of table
        paint.setStrokeWidth(3);
        page.getCanvas().drawLine(50, y + 702, 1250, y + 702, paint);
        paint.setStrokeWidth(2);

        //write bottom heading and business name string
        String bottomBusinessNameString = "For SUPER TEXTILES";
        String bottomHeadingString = "E. & O.E.";
        page.getCanvas().drawText(bottomHeadingString, 70, 1600, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(bottomBusinessNameString, 930, 1600, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        //draw bottom rectangle
        paint.setStyle(Paint.Style.STROKE);
        page.getCanvas().drawRect(70, 1620, 850, 1685, paint);
        paint.setStyle(Paint.Style.FILL);

        //write text in rectangle and draw line
        String rupeesInWordString = "Rupees in Words";
        page.getCanvas().drawText(rupeesInWordString, 80, 1660, paint);
        page.getCanvas().drawLine(320, 1660, 840, 1660, paint);

        //write received by, checked by, sign
        paint.setTextSize(30);
        String receivedByString = "Received By :";
        String checkedByString = "Checked By :";
        String proprietorSignString = "Prop./Auth. Signatory";

        int receivedByYPosition = 1865 - (int) ((paint.descent() - paint.ascent()) * 4);
        page.getCanvas().drawText(receivedByString, 70, receivedByYPosition, paint);
        page.getCanvas().drawText(checkedByString, 500, receivedByYPosition, paint);
        page.getCanvas().drawText(proprietorSignString, 950, receivedByYPosition, paint);

        //write bottom description for bill
        paint.setTextSize(25);
        String bottomDescriptionBillLine1 = getString(R.string.bill_bottom_description_line_1);
        String bottomDescriptionBillLine2 = getString(R.string.bill_bottom_description_line_2);
        String bottomDescriptionBillLine3 = getString(R.string.bill_bottom_description_line_3);
        int bottomDescriptionYPosition = 1855 - (int) ((paint.descent() - paint.ascent()) * 3);
        page.getCanvas().drawText(bottomDescriptionBillLine1, 70, bottomDescriptionYPosition, paint);
        bottomDescriptionYPosition += (int) (paint.descent() - paint.ascent());
        page.getCanvas().drawText(bottomDescriptionBillLine2, 70, bottomDescriptionYPosition, paint);
        bottomDescriptionYPosition += (int) (paint.descent() - paint.ascent());
        page.getCanvas().drawText(bottomDescriptionBillLine3, 70, bottomDescriptionYPosition, paint);

        document.finishPage(page);

        try {
            //create file directory
            File superTextilesDirectory;
            boolean isDirectoryCreated = true;
            File challanPDFFile;
            Uri fileUri = null;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                superTextilesDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Super Textiles/Bills");

                isDirectoryCreated = superTextilesDirectory.exists();
                if (!isDirectoryCreated) {
                    isDirectoryCreated = superTextilesDirectory.mkdirs();
                }

                if (isDirectoryCreated) {
                    challanPDFFile = new File(superTextilesDirectory, "Bill No. " + bill.getBillNo() + ".pdf");
                    fileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", challanPDFFile);
                    //create PDF file
                    galleryAddFile(challanPDFFile.getPath(), this);
                    document.writeTo(new FileOutputStream(challanPDFFile));
                }
            } else {
                ContentResolver contentResolver = getContentResolver();
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, "Bill No. " + bill.getBillNo());
                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/Super Textiles/Bills/");
                fileUri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                challanPDFFile = new File(String.valueOf(fileUri));
                //create PDF file
                galleryAddFile(challanPDFFile.getPath(), this);
                document.writeTo(getContentResolver().openOutputStream(fileUri));
            }

            //create PDF file if directory is created successfully
            if (isDirectoryCreated && bill != null && bill.getBillNo() != null) {

                //hide top progress bar
                PDFMenuItem.setActionView(PDFOptionView);

                //show SnackBar on success
                Uri finalFileUri = fileUri;
                Snackbar snackbar = Snackbar.make(viewBillConstraintLayout, "PDF Created", Snackbar.LENGTH_LONG)
                        .setAction("View", v -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(finalFileUri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
                        });
                snackbar.show();
            } else {
                Toast.makeText(this, "Directory not created", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "PDF not created", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        document.close();
    }
}