package com.areeb.supertextiles.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.areeb.supertextiles.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileOutputStream;
import java.io.IOException;

import static com.areeb.supertextiles.activities.ChallanDetailsActivity.CREATE_FILE;

public class AboutUs extends AppCompatActivity {

    TextView businessGstNo, bankNameValue, branchName, accountNoValue, ifscCodeValue, typeOfAccountValue;
    PdfDocument document;
    ConstraintLayout aboutUsParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("About Us");
        setContentView(R.layout.activity_about_us);

        //initialize xml view
        businessGstNo = findViewById(R.id.businessGstNo);
        bankNameValue = findViewById(R.id.bankNameValue);
        branchName = findViewById(R.id.branchName);
        accountNoValue = findViewById(R.id.accountNoValue);
        ifscCodeValue = findViewById(R.id.ifscCodeValue);
        typeOfAccountValue = findViewById(R.id.typeOfAccountValue);
        aboutUsParent = findViewById(R.id.aboutUsParent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //businessGstNo TextView long Press Listeners
        businessGstNo.setOnLongClickListener(getTextViewLongClickListener(this));

        //bankNameValue TextView long Press Listeners
        bankNameValue.setOnLongClickListener(getTextViewLongClickListener(this));

        //branchName TextView long Press Listeners
        branchName.setOnLongClickListener(getTextViewLongClickListener(this));

        //accountNoValue TextView long Press Listeners
        accountNoValue.setOnLongClickListener(getTextViewLongClickListener(this));

        //ifscCodeValue TextView long Press Listeners
        ifscCodeValue.setOnLongClickListener(getTextViewLongClickListener(this));

        //typeOfAccountValue TextView long Press Listeners
        typeOfAccountValue.setOnLongClickListener(getTextViewLongClickListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_us_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();

        if (title.equals(getString(R.string.business_card_pdf_option))) {
            createBusinessCardPDF();
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

            document.close();
        }
    }

    //this method is used to copy EditText data to clipboard
    public static void copyDataToClipBoard(String label, String data, Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(label, data);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    //method to get OnLongClickListener on TextView
    public static View.OnLongClickListener getTextViewLongClickListener(Context context) {
        return v -> {
            //cast view object to TextView
            TextView textView = (TextView) v;
            copyDataToClipBoard(textView.getTag().toString(), textView.getText().toString(), context);

            return true;
        };
    }

    private void createBusinessCardPDF() {
        document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1000, 500, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        //initialize Paint to draw text
        Paint paint = new Paint();
        paint.setTextSize(30);

        //Design business card
        designBusinessCard(page, paint);

        //close the document
        document.finishPage(page);

        //create new PDF file
        createFile(Uri.parse(Environment.DIRECTORY_DOCUMENTS));
    }

    // Request code for creating a PDF document.
    private void createFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name) + " Card.pdf");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, CREATE_FILE);
    }

    private void designBusinessCard(PdfDocument.Page page, Paint paint) {
        //initialize Y positions
        int y = 70;

        //write proprietors name
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        String proprietorsName = "Faiz H. Momin";
        paint.setColor(Color.BLUE);
        page.getCanvas().drawText(proprietorsName, 50, y, paint);

        //write contact numbers
        String mobileNumber1 = "Mob. 9324808070";
        String mobileNumber2 = "9822789100";
        int mobileNumber1XPosition = 950 - (int) (paint.measureText(mobileNumber1));
        page.getCanvas().drawText(mobileNumber1, mobileNumber1XPosition, y, paint);
        y += (paint.descent() - paint.ascent());
        int mobileNumber2XPosition = 950 - (int) (paint.measureText(mobileNumber2));
        page.getCanvas().drawText(mobileNumber2, mobileNumber2XPosition, y, paint);

        //write business name heading
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        y += ((paint.descent() - paint.ascent()) * 2) + 45;
        paint.setFlags(0);
        paint.setLetterSpacing(0.1f);
        paint.setTextSize(80);
        paint.setShadowLayer(12, 15, 15, Color.LTGRAY);
        String businessNameString = getString(R.string.app_name);
        int businessNameXPosition = 500 - (int) (paint.measureText(businessNameString) / 2);
        page.getCanvas().drawText(businessNameString, businessNameXPosition, 250, paint);

        //draw rectangle for manufacturing details
        paint.clearShadowLayer();
        paint.setStyle(Paint.Style.FILL);
        page.getCanvas().drawRect(2, 355, 998, 405, paint);

        //write manufacturing details
        paint.setLetterSpacing(0);
        paint.setTextSize(28);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String manufacturingDetailsString = "Manufacturers of : GREY COTTON FABRICS & COTTON JACQUET FABRICS";
        int manufacturingXPosition = 500 - (int) (paint.measureText(manufacturingDetailsString) / 2);
        page.getCanvas().drawText(manufacturingDetailsString, manufacturingXPosition, 390, paint);

        //draw line
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
        page.getCanvas().drawLine(2, 415, 998, 415, paint);

        //write business address
        paint.setTextSize(25);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        String businessAddress = getString(R.string.business_address);
        int businessAddressXPosition = 500 - (int) (paint.measureText(businessAddress) / 2);
        page.getCanvas().drawText(businessAddress, businessAddressXPosition, 450, paint);

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
        Snackbar snackbar = Snackbar.make(aboutUsParent, "PDF Created", Snackbar.LENGTH_LONG)
                .setAction("View", v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                });
        snackbar.show();
    }
}