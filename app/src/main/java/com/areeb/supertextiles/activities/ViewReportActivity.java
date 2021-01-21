package com.areeb.supertextiles.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.areeb.supertextiles.R;
import com.areeb.supertextiles.models.Report;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;

import static com.areeb.supertextiles.activities.AboutUs.getTextViewLongClickListener;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.CREATE_FILE;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getReportListDatabaseReference;

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
    MenuItem PDFMenuItem;
    ConstraintLayout viewReportParent;
    int startVerticalLineYPosition;
    final PdfDocument.Page[] page = {null};
    View PDFOptionView;
    PdfDocument document;
    int yPosition;

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
        viewReportParent = findViewById(R.id.viewReportParent);

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

            //create report
            createReportPdf();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();

                //enter challan data in PDF file
                alterDocument(uri);

                //show SnackBar on success
                showPDFCreatedSnackBar(uri);
            } else {
                Toast.makeText(this, "Directory not created", Toast.LENGTH_SHORT).show();
            }

            //hide top progress bar
            PDFMenuItem.setActionView(PDFOptionView);

            document.close();
        }
    }

    private void createReportPdf() {
        document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1300, 1900, 1).create();
        page[0] = document.startPage(pageInfo);

        //initialize Paint to draw text
        Paint paint = new Paint();
        paint.setTextSize(30);

        //initialize Y positions
        yPosition = 50;

        //draw heading for PDF file
        //draw rectangle for side margin
        paint.setTextSize(30);
        page[0].getCanvas().drawRGB(255, 255, 255);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        page[0].getCanvas().drawRect(50, 50, 1250, 1850, paint);
        paint.setStrokeWidth(2);

        //draw rectangle for business GSTNo
        String businessGSTNoString = "GSTIN : " + getString(R.string.business_gst_no);
        int leftPositionBusinessGST = 650 - (int) (paint.measureText(businessGSTNoString) / 2) - 10;
        int rightPositionBusinessGST = 650 + (int) (paint.measureText(businessGSTNoString) / 2) + 10;
        page[0].getCanvas().drawRoundRect(leftPositionBusinessGST, 315, rightPositionBusinessGST, 365, 10, 10, paint);

        //draw rectangle for manufacturing details
        paint.setStyle(Paint.Style.FILL);
        page[0].getCanvas().drawRect(52, 205, 1248, 255, paint);

        //write delivery_challan
        yPosition += (paint.descent() - paint.ascent());
        String deliveryChallan = "Business Report";
        int deliveryChallanXPosition = 650 - (int) (paint.measureText(deliveryChallan) / 2);
        paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);
        page[0].getCanvas().drawText(deliveryChallan, deliveryChallanXPosition, yPosition, paint);

        //write business name heading
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        yPosition += ((paint.descent() - paint.ascent()) * 2) + 15;
        paint.setFlags(0);
        paint.setLetterSpacing(0.1f);
        paint.setTextSize(80);
        paint.setShadowLayer(12, 15, 15, Color.LTGRAY);
        String businessNameString = getString(R.string.app_name);
        int superTextilesXPosition = 650 - (int) (paint.measureText(businessNameString) / 2);
        page[0].getCanvas().drawText(businessNameString, superTextilesXPosition, yPosition, paint);

        //write manufacturing details
        paint.clearShadowLayer();
        paint.setLetterSpacing(0);
        paint.setStyle(Paint.Style.FILL);
        yPosition += (paint.descent() - paint.ascent()) - 22;
        paint.setTextSize(35);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String manufacturingDetailsString = "Manufacturers of : GREY COTTON FABRICS";
        int manufacturingXPosition = 650 - (int) (paint.measureText(manufacturingDetailsString) / 2);
        page[0].getCanvas().drawText(manufacturingDetailsString, manufacturingXPosition, yPosition, paint);

        //write business address
        paint.setColor(Color.BLACK);
        paint.setTextSize(25);
        yPosition += (paint.descent() - paint.ascent()) + 15;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        String businessAddress = getString(R.string.business_address);
        int businessAddressXPosition = 650 - (int) (paint.measureText(businessAddress) / 2);
        page[0].getCanvas().drawText(businessAddress, businessAddressXPosition, yPosition, paint);

        //write business GST number
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        yPosition += (paint.descent() - paint.ascent()) + 30;
        int businessGSTXPosition = 650 - (int) (paint.measureText(businessGSTNoString) / 2);
        page[0].getCanvas().drawText(businessGSTNoString, businessGSTXPosition, yPosition, paint);

        //draw table heading for report list
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.FILL);
        page[0].getCanvas().drawRect(70, 405, 1230, 475, paint);
        paint.setColor(Color.BLACK);

        //write table heading
        String serialNumberString = "S.R. No.";
        String reportsHeadingString = "Reports";
        yPosition += (paint.descent() - paint.ascent()) + 65;
        int serialNumberXPosition = 135 - (int) (paint.measureText(serialNumberString) / 2);
        page[0].getCanvas().drawText(serialNumberString, serialNumberXPosition, yPosition, paint);
        int reportsXPosition = 715 - (int) (paint.measureText(reportsHeadingString) / 2);
        page[0].getCanvas().drawText(reportsHeadingString, reportsXPosition, yPosition, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        yPosition += 25;

        //get report list from database and add to PDF file
        getReportListDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long numberOfChild = snapshot.getChildrenCount();
                Log.d("Areeb", String.valueOf(numberOfChild));
                long childCounter = 0;
                for (DataSnapshot report : snapshot.getChildren()) {
                    childCounter++;

                    //get report object
                    Report reportObject = report.getValue(Report.class);

                    //check for last report
                    boolean isThisLastReport = numberOfChild == childCounter;

                    //print report data in PDF file
                    if (reportObject != null)
                        writeReportDataInPDFFile(paint, isThisLastReport, reportObject, childCounter, pageInfo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
    }

    private void writeReportDataInPDFFile(Paint paint, boolean isThisLastReport,
                                          @NonNull Report report, long serialNumber, PdfDocument.PageInfo pageInfo) {

        //save startYPosition
        startVerticalLineYPosition = yPosition;

        //write serial number in first column
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String serialNumberString = String.valueOf(serialNumber);
        int serialNumberXPosition = 135 - (int) (paint.measureText(serialNumberString) / 2);
        page[0].getCanvas().drawText(serialNumberString, serialNumberXPosition, yPosition, paint);

        //write report data in Reports column
        //write bill number
        String billNoString = "Bill No: " + report.getBillNo();
        page[0].getCanvas().drawText(billNoString, 220, yPosition, paint);

        //write date
        String dateString = "Date: " + report.getDate();
        int dateXPosition = 1210 - (int) (paint.measureText(dateString));
        page[0].getCanvas().drawText(dateString, dateXPosition, yPosition, paint);

        //write challan no list
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String challanNoString = "Challan No: " + report.getChallanNo();
        page[0].getCanvas().drawText(challanNoString, 220, yPosition, paint);

        //write lot number
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String lotNoString = "Lot No: " + report.getLotNo();
        page[0].getCanvas().drawText(lotNoString, 220, yPosition, paint);

        //write transport
        String transportString = "Transport: " + report.getTransport();
        int transportXPosition = 1210 - (int) (paint.measureText(transportString));
        page[0].getCanvas().drawText(transportString, transportXPosition, yPosition, paint);

        //draw first divider
        yPosition += (paint.descent() - paint.ascent());
        createNewPageAndResetValues(pageInfo, paint);
        paint.setStrokeWidth(1);
        page[0].getCanvas().drawLine(220, yPosition, 1210, yPosition, paint);
        paint.setStrokeWidth(2);

        //write party name
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String partyName = "Party: " + report.getParty();
        page[0].getCanvas().drawText(partyName, 220, yPosition, paint);

        //write delivery address
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String deliveryAddressString = "Delivery Address: " + report.getDeliveryAddress();
        page[0].getCanvas().drawText(deliveryAddressString, 220, yPosition, paint);

        //write total pieces
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String piecesString = "Pieces: " + report.getPieces();
        page[0].getCanvas().drawText(piecesString, 220, yPosition, paint);

        //write total meters
        String metersString = "Meters: " + report.getMeters();
        int metersXPosition = 1210 - (int) (paint.measureText(metersString));
        page[0].getCanvas().drawText(metersString, metersXPosition, yPosition, paint);

        //write design numbers
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String designNoString = "Design No: " + report.getDesignNo();
        page[0].getCanvas().drawText(designNoString, 220, yPosition, paint);

        //draw second divider
        yPosition += (paint.descent() - paint.ascent());
        createNewPageAndResetValues(pageInfo, paint);
        paint.setStrokeWidth(1);
        page[0].getCanvas().drawLine(220, yPosition, 1210, yPosition, paint);
        paint.setStrokeWidth(2);

        //write rate
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String rateString = "Rate: " + report.getRate();
        page[0].getCanvas().drawText(rateString, 220, yPosition, paint);

        //write amount
        String amountString = "Amount: " + report.getAmount();
        int amountXPosition = 1210 - (int) (paint.measureText(amountString));
        page[0].getCanvas().drawText(amountString, amountXPosition, yPosition, paint);

        //write discount percent
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String discountPercentString = "Discount %: " + report.getDiscountPercent();
        page[0].getCanvas().drawText(discountPercentString, 220, yPosition, paint);

        //write commission
        String commissionString = "Commission: " + report.getCommission();
        int commissionXPosition = 1210 - (int) (paint.measureText(commissionString));
        page[0].getCanvas().drawText(commissionString, commissionXPosition, yPosition, paint);

        //write net amount
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String netAmountString = "Net Amount: " + report.getNetAmount();
        page[0].getCanvas().drawText(netAmountString, 220, yPosition, paint);

        //write GST amount
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String GSTAmountString = "GST Amount: " + report.getGstAmount();
        page[0].getCanvas().drawText(GSTAmountString, 220, yPosition, paint);

        //write GST percent
        String GSTPercentString = "GST %: " + report.getGstPercent();
        int GSTPercentXPosition = 1210 - (int) (paint.measureText(GSTPercentString));
        page[0].getCanvas().drawText(GSTPercentString, GSTPercentXPosition, yPosition, paint);

        //write total GST amount
        yPosition += (paint.descent() - paint.ascent()) + 10;
        Log.d("Areeb", "Y Position " + yPosition);
        createNewPageAndResetValues(pageInfo, paint);
        String totalGSTAmountString = "Total GST Amount: " + report.getTotalGSTAmount();
        page[0].getCanvas().drawText(totalGSTAmountString, 220, yPosition, paint);

        //write final amount
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String finalAmountString = "Final Amount: " + report.getFinalAmount();
        page[0].getCanvas().drawText(finalAmountString, 220, yPosition, paint);

        //write received amount
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String receivedAmountString = "Received Amount: " + report.getReceivedAmount();
        page[0].getCanvas().drawText(receivedAmountString, 220, yPosition, paint);

        //write cheque number
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String chequeNumberString = "Cheque No: " + report.getChequeNo();
        page[0].getCanvas().drawText(chequeNumberString, 220, yPosition, paint);

        //write cheque date
        yPosition += (paint.descent() - paint.ascent()) + 10;
        createNewPageAndResetValues(pageInfo, paint);
        String chequeDateString = "Cheque Date: " + report.getChequeDate();
        page[0].getCanvas().drawText(chequeDateString, 220, yPosition, paint);

        //draw middle vertical line
        yPosition += 20;
        createNewPageAndResetValues(pageInfo, paint);
        page[0].getCanvas().drawLine(200, startVerticalLineYPosition, 200, yPosition, paint);

        //draw left and right vertical line
        page[0].getCanvas().drawLine(71, startVerticalLineYPosition, 71, yPosition, paint);
        page[0].getCanvas().drawLine(1229, startVerticalLineYPosition, 1229, yPosition, paint);

        //draw bottom line
        page[0].getCanvas().drawLine(70, yPosition, 1230, yPosition, paint);

        if (isThisLastReport) {
            document.finishPage(page[0]);

            //create new PDF file
            createFile(Uri.parse(Environment.DIRECTORY_DOCUMENTS));
        }
    }

    // Request code for creating a PDF document.
    private void createFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name) + " Report.pdf");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, CREATE_FILE);
    }

    private void alterDocument(Uri uri) {
        try {
            ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
            document.writeTo(fileOutputStream);
            // Let the document provider know you're done by closing the stream.
            fileOutputStream.close();
            pfd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPDFCreatedSnackBar(Uri uri) {
        Snackbar snackbar = Snackbar.make(viewReportParent, "PDF Created", Snackbar.LENGTH_LONG)
                .setAction("View", v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                });
        snackbar.show();
    }

    //method to create new page in PDF and to reset value to default
    private void createNewPageAndResetValues(PdfDocument.PageInfo pageInfo, Paint paint) {
        //return if yPosition is less than 1850
        if (yPosition < 1850) return;

        //draw left and right vertical line
        page[0].getCanvas().drawLine(71, startVerticalLineYPosition, 71, 1850, paint);
        page[0].getCanvas().drawLine(1229, startVerticalLineYPosition, 1229, 1850, paint);

        //draw middle vertical line
        page[0].getCanvas().drawLine(200, startVerticalLineYPosition, 200, 1850, paint);

        //finish the current page
        document.finishPage(page[0]);

        //create new page
        page[0] = document.startPage(pageInfo);

        //reset yPosition value
        yPosition = (int) (60 + (paint.descent() - paint.ascent()));
        startVerticalLineYPosition = yPosition - 20;

        //draw rectangle for side margin
        paint.setTextSize(30);
        page[0].getCanvas().drawRGB(255, 255, 255);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        page[0].getCanvas().drawRect(50, 50, 1250, 1850, paint);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);

    }
}