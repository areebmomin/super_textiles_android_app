package com.areeb.supertextiles.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.areeb.supertextiles.R;
import com.areeb.supertextiles.models.Challan;
import com.areeb.supertextiles.models.Design;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static com.areeb.supertextiles.activities.AboutUs.getTextViewLongClickListener;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.DESIGN_1;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.DESIGN_2;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.DESIGN_3;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.DESIGN_4;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.DESIGN_COLOR;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.DESIGN_NO;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.METER_LIST;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getDesignDataDatabaseReference;

public class ChallanDetailsActivity extends AppCompatActivity {

    public static final String TOTAL_PIECES = "TOTAL_PIECES";
    public static final String TOTAL_METERS = "TOTAL_METERS";
    public static final String NO_OF_DESIGNS = "NO_OF_DESIGNS";
    public static final String CHALLAN_OBJECT = "CHALLAN_OBJECT";
    public static final String DESIGN_1_OBJECT = "DESIGN_1_OBJECT";
    public static final String DESIGN_2_OBJECT = "DESIGN_2_OBJECT";
    public static final String DESIGN_3_OBJECT = "DESIGN_3_OBJECT";
    public static final String DESIGN_4_OBJECT = "DESIGN_4_OBJECT";
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 101;
    TextView challanNoValueTextView, dateValueTextView, purchaserNameTextView, lotNoValueTextView, LRNoValueTextView,
            deliveryAtValueTextView, purchaserGSTValueTextView, qualityValueTextView, totalPiecesValueTextView,
            totalMetersValueTextView, foldValueTextView, designNo1ValueTextView, design1ColorValueTextView,
            designNo2ValueTextView, design2ColorValueTextView, designNo3ValueTextView, design3ColorValueTextView,
            designNo4ValueTextView, design4ColorValueTextView;
    ConstraintLayout design1ConstraintLayout, design2ConstraintLayout, design3ConstraintLayout, design4ConstraintLayout;
    TableLayout designOneTableLayout, designTwoTableLayout, designThreeTableLayout, designFourTableLayout;
    ConstraintLayout challanDetailsConstraintLayout;
    Challan challan;
    DatabaseReference designDataDatabaseReference;
    Design design1, design2, design3, design4;
    Gson gson;

    //method to print challan structure and challan data in PDF file
    public static void createStructureAndAddDataInChallanPDF(@NonNull PdfDocument.Page page, Paint paint, Context context, Challan challan,
                                                             Design design1, Design design2, Design design3, Design design4) {

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
        String businessGSTNoString = "GSTIN : " + context.getString(R.string.business_gst_no);
        int leftPositionBusinessGST = 650 - (int) (paint.measureText(businessGSTNoString) / 2) - 10;
        int rightPositionBusinessGST = 650 + (int) (paint.measureText(businessGSTNoString) / 2) + 10;
        page.getCanvas().drawRoundRect(leftPositionBusinessGST, 315, rightPositionBusinessGST, 365, 10, 10, paint);

        //draw rectangle for manufacturing details
        paint.setStyle(Paint.Style.FILL);
        page.getCanvas().drawRect(52, 205, 1248, 255, paint);

        //write delivery_challan
        y += (paint.descent() - paint.ascent());
        String deliveryChallan = "DELIVERY CHALLAN";
        int deliveryChallanXPosition = 650 - (int) (paint.measureText(deliveryChallan) / 2);
        paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);
        page.getCanvas().drawText(deliveryChallan, deliveryChallanXPosition, y, paint);

        //write business name heading
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        y += ((paint.descent() - paint.ascent()) * 2) + 15;
        paint.setFlags(0);
        paint.setLetterSpacing(0.1f);
        paint.setTextSize(80);
        paint.setShadowLayer(12, 15, 15, Color.LTGRAY);
        String businessNameString = context.getString(R.string.app_name);
        int superTextilesXPosition = 650 - (int) (paint.measureText(businessNameString) / 2);
        page.getCanvas().drawText(businessNameString, superTextilesXPosition, y, paint);

        //write manufacturing details
        paint.clearShadowLayer();
        paint.setLetterSpacing(0);
        paint.setStyle(Paint.Style.FILL);
        y += (paint.descent() - paint.ascent()) - 22;
        paint.setTextSize(35);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String manufacturingDetailsString = "Manufacturers of : GREY COTTON FABRICS";
        int manufacturingXPosition = 650 - (int) (paint.measureText(manufacturingDetailsString) / 2);
        page.getCanvas().drawText(manufacturingDetailsString, manufacturingXPosition, y, paint);

        //write business address
        paint.setColor(Color.BLACK);
        paint.setTextSize(25);
        y += (paint.descent() - paint.ascent()) + 15;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        String businessAddress = context.getString(R.string.business_address);
        int businessAddressXPosition = 650 - (int) (paint.measureText(businessAddress) / 2);
        page.getCanvas().drawText(businessAddress, businessAddressXPosition, y, paint);

        //write business GST number
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        y += (paint.descent() - paint.ascent()) + 30;
        int businessGSTXPosition = 650 - (int) (paint.measureText(businessGSTNoString) / 2);
        page.getCanvas().drawText(businessGSTNoString, businessGSTXPosition, y, paint);

        //write challan no sub-heading and value
        y += 15;
        paint.setStrokeWidth(2);
        String challanNoString = "Challan No.";
        page.getCanvas().drawText(challanNoString, 70, y, paint);
        int challanNoValueXPosition = 70 + (int) (paint.measureText(challanNoString) + 10);
        String challanNoValueString = challan.getChallan_no();
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        page.getCanvas().drawText(challanNoValueString, challanNoValueXPosition + 10, y - 3, paint);
        int challanValueLineEndX = challanNoValueXPosition + (int) (paint.measureText(challanNoValueString) + 20);
        page.getCanvas().drawLine(challanNoValueXPosition, y + 5, challanValueLineEndX, y + 5, paint);

        //write date sub-heading and value
        String dateValue = challan.getDate();
        int dateValueXPosition = 1210 - (int) (paint.measureText(dateValue));
        page.getCanvas().drawText(dateValue, dateValueXPosition + 10, y - 3, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String dateString = "Date";
        page.getCanvas().drawText(dateString, dateValueXPosition - 80, y, paint);
        page.getCanvas().drawLine(dateValueXPosition, y + 5, 1230, y + 5, paint);

        //write purchaser name sub-heading and value
        y += (paint.descent() - paint.ascent()) + 30;
        String purchaserString = "Purchaser";
        int purchaserStringWidth = (int) paint.measureText(purchaserString);
        int purchaserNamePositionX = 80 + purchaserStringWidth;
        String purchaserNameString = challan.getPurchaser();
        if (purchaserNameString.length() > 65)
            purchaserNameString = purchaserNameString.substring(0, 65) + "...";
        page.getCanvas().drawText(purchaserString, 70, y, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        page.getCanvas().drawText(purchaserNameString, purchaserNamePositionX + 10, y - 3, paint);
        page.getCanvas().drawLine(purchaserNamePositionX, y + 5, 1230, y + 5, paint);

        //write lot no sub-heading and value
        y += (paint.descent() - paint.ascent()) + 30;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String lotNoString = "Lot No.";
        page.getCanvas().drawText(lotNoString, 70, y, paint);
        int lotNoValueXPosition = 70 + (int) (paint.measureText(lotNoString) + 10);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        String lotNoValueString = challan.getLot_no();
        page.getCanvas().drawText(lotNoValueString, lotNoValueXPosition + 10, y - 3, paint);
        page.getCanvas().drawLine(lotNoValueXPosition, y + 5, 630, y + 5, paint);

        //write l.r no sub-heading and value
        String lrString = "L.R. No.";
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(lrString, 650, y, paint);
        int lrStringWidth = (int) paint.measureText(lrString);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        String LrValue = challan.getLr_no();
        int lrValueXPosition = 670 + lrStringWidth;
        page.getCanvas().drawText(LrValue, lrValueXPosition, y - 3, paint);
        page.getCanvas().drawLine(660 + lrStringWidth, y + 5, 1230, y + 5, paint);

        //write delivery at sub-heading and value
        y += (paint.descent() - paint.ascent()) + 30;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String deliveryAtString = "Delivery At";
        int deliveryAtStringWidth = (int) paint.measureText(deliveryAtString);
        int deliveryAtValuePositionX = 80 + deliveryAtStringWidth;
        String deliveryAtValueString = challan.getDelivery_at();
        if (deliveryAtValueString.length() > 65)
            deliveryAtValueString = deliveryAtValueString.substring(0, 65) + "...";
        page.getCanvas().drawText(deliveryAtString, 70, y, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        page.getCanvas().drawText(deliveryAtValueString, deliveryAtValuePositionX + 10, y - 3, paint);
        page.getCanvas().drawLine(deliveryAtValuePositionX, y + 5, 1230, y + 5, paint);

        //write purchaser GSTIN sub-heading and value
        y += (paint.descent() - paint.ascent()) + 30;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        String purchaserGSTString = "Purchaser GSTIN";
        int purchaserGStStringWidth = (int) paint.measureText(purchaserGSTString);
        int purchaserGSTValuePositionX = 80 + purchaserGStStringWidth;
        String purchaserGSTValueString = challan.getPurchaser_gst();
        if (purchaserGSTValueString.length() > 65)
            purchaserGSTValueString = purchaserGSTValueString.substring(0, 65) + "...";
        page.getCanvas().drawText(purchaserGSTString, 70, y, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        page.getCanvas().drawText(purchaserGSTValueString, purchaserGSTValuePositionX + 10, y - 3, paint);
        page.getCanvas().drawLine(purchaserGSTValuePositionX, y + 5, 1230, y + 5, paint);

        //draw table for quality, total pieces, total meters and remark
        y += (paint.descent() - paint.ascent());
        page.getCanvas().drawLine(50, y, 1250, y, paint);
        page.getCanvas().drawLine(50, y + 140, 1250, y + 140, paint);
        page.getCanvas().drawLine(500, y, 500, y + 140, paint);
        page.getCanvas().drawLine(950, y, 950, y + 140, paint);
        page.getCanvas().drawLine(500, y + 70, 950, y + 70, paint);

        //write quality heading and value
        y += (paint.descent() - paint.ascent());
        String qualityString = "Quality";
        String qualityValueString = challan.getQuality();
        int qualityValueXPosition = 275 - (int) (paint.measureText(qualityValueString) / 2);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(qualityString, 70, y, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        page.getCanvas().drawText(qualityValueString, qualityValueXPosition, y + 80, paint);

        //write total pieces heading and value
        String totalPiecesString = "T. Pcs.";
        String totalPiecesValueString = challan.getTotal_pieces();
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(totalPiecesString, 520, y + 10, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        page.getCanvas().drawText(totalPiecesValueString, 630, y + 10, paint);

        //write total meters heading and value
        String totalMetersString = "T. Mtrs.";
        String totalMetersValueString = challan.getTotal_meters();
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(totalMetersString, 520, y + 80, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        page.getCanvas().drawText(totalMetersValueString, 640, y + 80, paint);

        //write fold heading and value
        String foldString = "Fold/Guaranteed";
        String foldValueString = challan.getFold();
        int foldValueXPosition = 1075 - (int) (paint.measureText(foldValueString) / 2);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(foldString, 970, y, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        page.getCanvas().drawText(foldValueString, foldValueXPosition, y + 80, paint);

        //draw three vertical line
        page.getCanvas().drawLine(350, y + 105, 350, y + 970, paint);
        page.getCanvas().drawLine(650, y + 105, 650, y + 970, paint);
        page.getCanvas().drawLine(950, y + 105, 950, y + 970, paint);

        //draw horizontal line for design no and color
        page.getCanvas().drawLine(50, y + 165, 1250, y + 165, paint);
        page.getCanvas().drawLine(50, y + 225, 1250, y + 225, paint);

        //set design 1 data
        y += 105 + (paint.descent() - paint.ascent());
        paint.setTextSize(25);
        String designNoString = "D. No.";
        String colorString = "Color";
        int designNoStringWidth = (int) paint.measureText(designNoString);
        int colorStringWidth = (int) paint.measureText(colorString);
        String design1NoString = design1.getDesign_no();
        String design1ColorString = design1.getDesign_color();
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(designNoString, 60, y + 5, paint);
        page.getCanvas().drawText(colorString, 60, y + 65, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setColor(context.getColor(R.color.design_1_challan_pdf));
        page.getCanvas().drawText(design1NoString, 70 + designNoStringWidth, y + 5, paint);
        page.getCanvas().drawText(design1ColorString, 75 + colorStringWidth, y + 65, paint);

        //set design 2 data
        String design2NoString = design2.getDesign_no();
        String design2ColorString = design2.getDesign_color();
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(designNoString, 360, y + 5, paint);
        page.getCanvas().drawText(colorString, 360, y + 65, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setColor(context.getColor(R.color.design_2_challan_pdf));
        page.getCanvas().drawText(design2NoString, 370 + designNoStringWidth, y + 5, paint);
        page.getCanvas().drawText(design2ColorString, 375 + colorStringWidth, y + 65, paint);

        //set design 3 data
        String design3NoString = design3.getDesign_no();
        String design3ColorString = design3.getDesign_color();
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(designNoString, 660, y + 5, paint);
        page.getCanvas().drawText(colorString, 660, y + 65, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setColor(context.getColor(R.color.design_3_challan_pdf));
        page.getCanvas().drawText(design3NoString, 670 + designNoStringWidth, y + 5, paint);
        page.getCanvas().drawText(design3ColorString, 675 + colorStringWidth, y + 65, paint);

        //set design 4 data
        String design4NoString = design4.getDesign_no();
        String design4ColorString = design4.getDesign_color();
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(designNoString, 960, y + 5, paint);
        page.getCanvas().drawText(colorString, 960, y + 65, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setColor(context.getColor(R.color.design_4_challan_pdf));
        page.getCanvas().drawText(design4NoString, 970 + designNoStringWidth, y + 5, paint);
        page.getCanvas().drawText(design4ColorString, 975 + colorStringWidth, y + 65, paint);

        //draw horizontal lines for meters table
        int tempY = y + 80;
        int YPositionForMeterValue = tempY + 50;
        int counter = 1;
        paint.setTextSize(24);
        paint.setColor(Color.BLACK);
        String totalString = "Total";
        for (int i = 1; i <= 15; i++) {
            tempY += 50;
            if (i == 15) paint.setStrokeWidth(3);
            page.getCanvas().drawLine(50, tempY, 1250, tempY, paint);

            //enter serial number in serial column
            if (i != 1 && i != 15) {
                int tempCounter = counter;

                //write serial number for design 1
                int design1SerialXPosition = 75 - (int) (paint.measureText(String.valueOf(tempCounter)) / 2);
                page.getCanvas().drawText(String.valueOf(tempCounter), design1SerialXPosition, tempY - 13, paint);

                //write serial number for design 2
                tempCounter += 13;
                int design2SerialXPosition = 375 - (int) (paint.measureText(String.valueOf(tempCounter)) / 2);
                page.getCanvas().drawText(String.valueOf(tempCounter), design2SerialXPosition, tempY - 13, paint);

                //write serial number for design 3
                tempCounter += 13;
                int design3SerialXPosition = 675 - (int) (paint.measureText(String.valueOf(tempCounter)) / 2);
                page.getCanvas().drawText(String.valueOf(tempCounter), design3SerialXPosition, tempY - 13, paint);

                //write serial number for design 4
                tempCounter += 13;
                if (tempCounter != 51 && tempCounter != 52) {
                    int design4SerialXPosition = 975 - (int) (paint.measureText(String.valueOf(tempCounter)) / 2);
                    page.getCanvas().drawText(String.valueOf(tempCounter), design4SerialXPosition, tempY - 13, paint);
                }
                counter++;
            } else if (i == 15) {
                paint.setTextSize(20);
                page.getCanvas().drawText(totalString, 55, tempY - 15, paint);
                page.getCanvas().drawText(totalString, 355, tempY - 15, paint);
                page.getCanvas().drawText(totalString, 655, tempY - 15, paint);
                page.getCanvas().drawText(totalString, 955, tempY - 15, paint);
            }
        }

        //draw vertical margin for serial number
        paint.setStrokeWidth(2);
        page.getCanvas().drawLine(110, y + 85, 110, y + 830, paint);
        page.getCanvas().drawLine(410, y + 85, 410, y + 830, paint);
        page.getCanvas().drawLine(710, y + 85, 710, y + 830, paint);
        page.getCanvas().drawLine(1010, y + 85, 1010, y + 830, paint);

        //set table heading in meters table
        y += 90 + (paint.descent() - paint.ascent());
        String serialNumberString = "S.No";
        String meterString = "Meter";
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(20);
        page.getCanvas().drawText(serialNumberString, 58, y, paint);
        page.getCanvas().drawText(serialNumberString, 358, y, paint);
        page.getCanvas().drawText(serialNumberString, 658, y, paint);
        page.getCanvas().drawText(serialNumberString, 958, y, paint);
        paint.setTextSize(25);
        int design1MiddleX = 230 - (int) (paint.measureText(meterString) / 2);
        int design2MiddleX = 530 - (int) (paint.measureText(meterString) / 2);
        int design3MiddleX = 830 - (int) (paint.measureText(meterString) / 2);
        int design4MiddleX = 1130 - (int) (paint.measureText(meterString) / 2);
        page.getCanvas().drawText(meterString, design1MiddleX, y + 3, paint);
        page.getCanvas().drawText(meterString, design2MiddleX, y + 3, paint);
        page.getCanvas().drawText(meterString, design3MiddleX, y + 3, paint);
        page.getCanvas().drawText(meterString, design4MiddleX, y + 3, paint);

        //add meter data in meter table
        int meterSerialNumber = 1;
        double column1Total = 0, column2Total = 0, column3Total = 0, column4Total = 0;
        int dynamicYPositionForMeterValue = YPositionForMeterValue;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(24);
        //get meter list from design 1 and print in table
        for (String meter : design1.getMeterList()) {
            paint.setColor(context.getColor(R.color.design_1_challan_pdf));
            printMeterInPDF(meterSerialNumber, meter, paint, page, dynamicYPositionForMeterValue);

            if (meterSerialNumber >= 1 && meterSerialNumber <= 13)
                column1Total += Double.parseDouble(meter);
            else if (meterSerialNumber >= 14 && meterSerialNumber <= 26)
                column2Total += Double.parseDouble(meter);
            else if (meterSerialNumber >= 27 && meterSerialNumber <= 39)
                column3Total += Double.parseDouble(meter);
            else if (meterSerialNumber >= 40 && meterSerialNumber <= 50)
                column4Total += Double.parseDouble(meter);

            meterSerialNumber++;
            dynamicYPositionForMeterValue += 50;
            if (dynamicYPositionForMeterValue > 1565)
                dynamicYPositionForMeterValue = YPositionForMeterValue;
        }
        //get meter list from design 2 and print in table
        for (String meter : design2.getMeterList()) {
            paint.setColor(context.getColor(R.color.design_2_challan_pdf));
            printMeterInPDF(meterSerialNumber, meter, paint, page, dynamicYPositionForMeterValue);

            if (meterSerialNumber >= 1 && meterSerialNumber <= 13)
                column1Total += Double.parseDouble(meter);
            else if (meterSerialNumber >= 14 && meterSerialNumber <= 26)
                column2Total += Double.parseDouble(meter);
            else if (meterSerialNumber >= 27 && meterSerialNumber <= 39)
                column3Total += Double.parseDouble(meter);
            else if (meterSerialNumber >= 40 && meterSerialNumber <= 50)
                column4Total += Double.parseDouble(meter);

            meterSerialNumber++;
            dynamicYPositionForMeterValue += 50;
            if (dynamicYPositionForMeterValue > 1565)
                dynamicYPositionForMeterValue = YPositionForMeterValue;
        }
        //get meter list from design 3 and print in table
        for (String meter : design3.getMeterList()) {
            paint.setColor(context.getColor(R.color.design_3_challan_pdf));
            printMeterInPDF(meterSerialNumber, meter, paint, page, dynamicYPositionForMeterValue);

            if (meterSerialNumber >= 1 && meterSerialNumber <= 13)
                column1Total += Double.parseDouble(meter);
            else if (meterSerialNumber >= 14 && meterSerialNumber <= 26)
                column2Total += Double.parseDouble(meter);
            else if (meterSerialNumber >= 27 && meterSerialNumber <= 39)
                column3Total += Double.parseDouble(meter);
            else if (meterSerialNumber >= 40 && meterSerialNumber <= 50)
                column4Total += Double.parseDouble(meter);

            meterSerialNumber++;
            dynamicYPositionForMeterValue += 50;
            if (dynamicYPositionForMeterValue > 1565)
                dynamicYPositionForMeterValue = YPositionForMeterValue;
        }
        //get meter list from design 4 and print in table
        for (String meter : design4.getMeterList()) {
            paint.setColor(context.getColor(R.color.design_4_challan_pdf));
            printMeterInPDF(meterSerialNumber, meter, paint, page, dynamicYPositionForMeterValue);

            if (meterSerialNumber >= 1 && meterSerialNumber <= 13)
                column1Total += Double.parseDouble(meter);
            else if (meterSerialNumber >= 14 && meterSerialNumber <= 26)
                column2Total += Double.parseDouble(meter);
            else if (meterSerialNumber >= 27 && meterSerialNumber <= 39)
                column3Total += Double.parseDouble(meter);
            else if (meterSerialNumber >= 40 && meterSerialNumber <= 50)
                column4Total += Double.parseDouble(meter);

            meterSerialNumber++;
            dynamicYPositionForMeterValue += 50;
            if (dynamicYPositionForMeterValue > 1565)
                dynamicYPositionForMeterValue = YPositionForMeterValue;
        }

        //print total of meter table columns
        paint.setColor(Color.BLACK);
        if (column1Total > 0) {
            int column1TotalX = 230 - (int) (paint.measureText(String.valueOf(column1Total)) / 2);
            page.getCanvas().drawText(String.valueOf(column1Total), column1TotalX, tempY - 13, paint);
        }

        if (column2Total > 0) {
            int column2TotalX = 530 - (int) (paint.measureText(String.valueOf(column2Total)) / 2);
            page.getCanvas().drawText(String.valueOf(column2Total), column2TotalX, tempY - 13, paint);
        }

        if (column3Total > 0) {
            int column3TotalX = 830 - (int) (paint.measureText(String.valueOf(column3Total)) / 2);
            page.getCanvas().drawText(String.valueOf(column3Total), column3TotalX, tempY - 13, paint);
        }

        if (column4Total > 0) {
            int column4TotalX = 1130 - (int) (paint.measureText(String.valueOf(column4Total)) / 2);
            page.getCanvas().drawText(String.valueOf(column4Total), column4TotalX, tempY - 13, paint);
        }

        //draw vertical line at bottom
        y = tempY;
        page.getCanvas().drawLine(600, tempY, 600, y + 186, paint);

        //write bottom description
        String bottomDescriptionHeading = context.getString(R.string.bottom_description_heading_challan);
        String bottomDescriptionLine1String = context.getString(R.string.bottom_description_line_1_challan);
        String bottomDescriptionLine2String = context.getString(R.string.bottom_description_line_2_challan);
        String bottomDescriptionLine3String = context.getString(R.string.bottom_description_line_3_challan);
        String bottomDescriptionLine4String = context.getString(R.string.bottom_description_line_4_challan);
        paint.setTextSize(18);
        y += (paint.descent() - paint.ascent());
        int YPositionRight = y;
        page.getCanvas().drawText(bottomDescriptionHeading, 60, y, paint);
        paint.setTextSize(16);
        y += (paint.descent() - paint.ascent() + 5);
        page.getCanvas().drawText(bottomDescriptionLine1String, 60, y, paint);
        y += (paint.descent() - paint.ascent());
        page.getCanvas().drawText(bottomDescriptionLine2String, 60, y, paint);
        y += (paint.descent() - paint.ascent());
        page.getCanvas().drawText(bottomDescriptionLine3String, 60, y, paint);
        y += (paint.descent() - paint.ascent());
        page.getCanvas().drawText(bottomDescriptionLine4String, 60, y, paint);

        //write bottom sign portion
        String bottomDateString = "Date";
        String receiversSignString = "Receiver's Sign:";
        String statusString = "Status:";
        String forSuperTextilesString = "For SUPER TEXTILES";
        String proprietorSign = "Prop./Auth. Sign";
        paint.setTextSize(25);
        y = YPositionRight + 20;
        page.getCanvas().drawText(bottomDateString, 620, y, paint);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        page.getCanvas().drawText(forSuperTextilesString, 930, y + 10, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(25);
        y += (paint.descent() - paint.ascent() + 20);
        page.getCanvas().drawText(receiversSignString, 620, y, paint);
        y += (paint.descent() - paint.ascent() + 20);
        page.getCanvas().drawText(proprietorSign, 970, y, paint);
        page.getCanvas().drawText(statusString, 620, y, paint);

    }

    //print meter in meter table in challan PDF
    public static void printMeterInPDF(int meterSerialNumber, String meter, Paint paint, PdfDocument.Page page, int YPositionForMeterValue) {
        //for column 1 in meter table in PDF
        if (meterSerialNumber >= 1 && meterSerialNumber <= 13) {
            int design1MiddleX = 230 - (int) (paint.measureText(meter) / 2);
            page.getCanvas().drawText(meter, design1MiddleX, YPositionForMeterValue + 37, paint);
        }
        //for column 2 in meter table in PDF
        else if (meterSerialNumber >= 14 && meterSerialNumber <= 26) {
            int design2MiddleX = 530 - (int) (paint.measureText(meter) / 2);
            page.getCanvas().drawText(meter, design2MiddleX, YPositionForMeterValue + 37, paint);
        }
        //for column 3 in meter table in PDF
        else if (meterSerialNumber >= 27 && meterSerialNumber <= 39) {
            int design3MiddleX = 830 - (int) (paint.measureText(meter) / 2);
            page.getCanvas().drawText(meter, design3MiddleX, YPositionForMeterValue + 37, paint);
        }
        //for column 4 in meter table in PDF
        else if (meterSerialNumber >= 40 && meterSerialNumber <= 50) {
            int design4MiddleX = 1130 - (int) (paint.measureText(meter) / 2);
            page.getCanvas().drawText(meter, design4MiddleX, YPositionForMeterValue + 37, paint);
        }
    }

    //add image to gallery
    public static void galleryAddFile(String currPDFPath, Context context) {
        File f = new File(currPDFPath);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(f);
            intent.setData(contentUri);
            context.sendBroadcast(intent);
        } else {
            MediaScannerConnection.scanFile(context, new String[]{f.toString()}, new String[]{f.getName()}, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Challan Details");
        setContentView(R.layout.activity_challan_details);

        //initializing xml views
        challanNoValueTextView = findViewById(R.id.challanNoValueTextView);
        dateValueTextView = findViewById(R.id.billDateValueTextView);
        purchaserNameTextView = findViewById(R.id.purchaserNameTextView);
        lotNoValueTextView = findViewById(R.id.lotNoValueTextView);
        LRNoValueTextView = findViewById(R.id.LRNoValueTextView);
        deliveryAtValueTextView = findViewById(R.id.deliveryAtValueTextView);
        purchaserGSTValueTextView = findViewById(R.id.purchaserGSTValueTextView);
        qualityValueTextView = findViewById(R.id.qualityValueTextView);
        totalPiecesValueTextView = findViewById(R.id.totalPiecesValueTextView);
        totalMetersValueTextView = findViewById(R.id.totalMetersValueTextView);
        foldValueTextView = findViewById(R.id.foldValueTextView);
        designNo1ValueTextView = findViewById(R.id.designNo1ValueTextView);
        design1ColorValueTextView = findViewById(R.id.design1ColorValueTextView);
        designNo2ValueTextView = findViewById(R.id.designNo2ValueTextView);
        design2ColorValueTextView = findViewById(R.id.design2ColorValueTextView);
        designNo3ValueTextView = findViewById(R.id.designNo3ValueTextView);
        design3ColorValueTextView = findViewById(R.id.design3ColorValueTextView);
        designNo4ValueTextView = findViewById(R.id.designNo4ValueTextView);
        design4ColorValueTextView = findViewById(R.id.design4ColorValueTextView);
        design1ConstraintLayout = findViewById(R.id.design1ConstraintLayout);
        design2ConstraintLayout = findViewById(R.id.design2ConstraintLayout);
        design3ConstraintLayout = findViewById(R.id.design3ConstraintLayout);
        design4ConstraintLayout = findViewById(R.id.design4ConstraintLayout);
        designOneTableLayout = findViewById(R.id.designOneTableLayout);
        designTwoTableLayout = findViewById(R.id.designTwoTableLayout);
        designThreeTableLayout = findViewById(R.id.designThreeTableLayout);
        designFourTableLayout = findViewById(R.id.designFourTableLayout);
        challanDetailsConstraintLayout = findViewById(R.id.challanDetailsConstraintLayout);

        //initialize design object
        design1 = new Design();
        design2 = new Design();
        design3 = new Design();
        design4 = new Design();

        //initializing Gson
        gson = new Gson();

        //set challan details in TextView's
        if (getIntent() != null) {

            //initialize Challan Object
            challan = new Challan();

            //convert Json string to Challan object
            challan = gson.fromJson(getIntent().getStringExtra(CHALLAN_OBJECT), Challan.class);

            if (challan != null) {

                //set Challan details in TextView
                challanNoValueTextView.setText(challan.getChallan_no());
                dateValueTextView.setText(challan.getDate());
                purchaserNameTextView.setText(challan.getPurchaser());
                lotNoValueTextView.setText(challan.getLot_no());
                LRNoValueTextView.setText(challan.getLr_no());
                deliveryAtValueTextView.setText(challan.getDelivery_at());
                purchaserGSTValueTextView.setText(challan.getPurchaser_gst());
                qualityValueTextView.setText(challan.getQuality());
                totalPiecesValueTextView.setText(challan.getTotal_pieces());
                totalMetersValueTextView.setText(challan.getTotal_meters());
                foldValueTextView.setText(challan.getFold());

                //show design fields based on number_of_designs
                switch (challan.getNo_of_designs()) {
                    case "2":
                        design2ConstraintLayout.setVisibility(View.VISIBLE);
                        break;
                    case "3":
                        design2ConstraintLayout.setVisibility(View.VISIBLE);
                        design3ConstraintLayout.setVisibility(View.VISIBLE);
                        break;
                    case "4":
                        design2ConstraintLayout.setVisibility(View.VISIBLE);
                        design3ConstraintLayout.setVisibility(View.VISIBLE);
                        design4ConstraintLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }

        //get design data from database
        if (challan != null && challan.getChallan_no() != null && !challan.getChallan_no().isEmpty()) {
            //initialize design_data DatabaseReference
            designDataDatabaseReference = getDesignDataDatabaseReference(challan.getChallan_no());

            //get design data from database
            designDataDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() == 1) {
                        setDataInDesignField(snapshot, DESIGN_1, designNo1ValueTextView, design1ColorValueTextView, design1, designOneTableLayout);
                    } else if (snapshot.getChildrenCount() == 2) {
                        setDataInDesignField(snapshot, DESIGN_1, designNo1ValueTextView, design1ColorValueTextView, design1, designOneTableLayout);
                        setDataInDesignField(snapshot, DESIGN_2, designNo2ValueTextView, design2ColorValueTextView, design2, designTwoTableLayout);
                    } else if (snapshot.getChildrenCount() == 3) {
                        setDataInDesignField(snapshot, DESIGN_1, designNo1ValueTextView, design1ColorValueTextView, design1, designOneTableLayout);
                        setDataInDesignField(snapshot, DESIGN_2, designNo2ValueTextView, design2ColorValueTextView, design2, designTwoTableLayout);
                        setDataInDesignField(snapshot, DESIGN_3, designNo3ValueTextView, design3ColorValueTextView, design3, designThreeTableLayout);
                    } else if (snapshot.getChildrenCount() == 4) {
                        setDataInDesignField(snapshot, DESIGN_1, designNo1ValueTextView, design1ColorValueTextView, design1, designOneTableLayout);
                        setDataInDesignField(snapshot, DESIGN_2, designNo2ValueTextView, design2ColorValueTextView, design2, designTwoTableLayout);
                        setDataInDesignField(snapshot, DESIGN_3, designNo3ValueTextView, design3ColorValueTextView, design3, designThreeTableLayout);
                        setDataInDesignField(snapshot, DESIGN_4, designNo4ValueTextView, design4ColorValueTextView, design4, designFourTableLayout);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    error.toException().printStackTrace();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //copy text to clipboard when onLongPress on TextView
        challanNoValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        dateValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        purchaserNameTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        lotNoValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        LRNoValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        deliveryAtValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        purchaserGSTValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        qualityValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        totalPiecesValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        totalMetersValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        foldValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        designNo1ValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        design1ColorValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        designNo2ValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        design2ColorValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        designNo3ValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        design3ColorValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        designNo4ValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        design4ColorValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.challan_details_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String title = item.getTitle().toString();

        if (title.equals(getString(R.string.challan_pdf))) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    createChallanPdf();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
                }
            } else {
                createChallanPdf();
            }
        } else if (title.equals(getString(R.string.edit_challan))) {
            if (challan != null) {
                Intent intent = new Intent(ChallanDetailsActivity.this, AddChallanActivity.class);
                intent.putExtra(CHALLAN_OBJECT, gson.toJson(challan));

                //add design data in intent based on number of designs
                addDesignDataInIntent(intent, challan.getNo_of_designs());

                startActivity(intent);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_STORAGE) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    createChallanPdf();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
            } else {
                createChallanPdf();
            }
        }
    }

    //method to set data in design field
    private void setDataInDesignField(DataSnapshot snapshot, String designNoConst, TextView designNoTextView, TextView designColorTextView,
                                      Design designObject, TableLayout tableLayout) {
        //get designNo and designColor from snapshot
        String designNo = snapshot.child(designNoConst).child(DESIGN_NO).getValue(String.class);
        String designColor = snapshot.child(designNoConst).child(DESIGN_COLOR).getValue(String.class);

        //set designNo and designColor to TextView
        designNoTextView.setText(designNo);
        designColorTextView.setText(designColor);

        //initialize designMeterList
        ArrayList<String> meterList = new ArrayList<>();

        //get meter list from snapshot
        for (DataSnapshot meter : snapshot.child(designNoConst).child(METER_LIST).getChildren()) {
            String meterString = meter.getValue(String.class);
            addRowInTableLayout(tableLayout, meter.getValue(String.class));
            meterList.add(meterString);
        }

        //initialize design1 object and add data to it
        designObject.setDesign_no(designNo);
        designObject.setDesign_color(designColor);
        designObject.setMeterList(meterList);
    }

    //method to add row in TableLayout
    private void addRowInTableLayout(TableLayout tableLayout, String meter) {
        //initialize new row in table
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.view_challan_table_row, null);

        //initialize TextView as Table column
        TextView SRNOTableColumn = tableRow.findViewById(R.id.SNoTableColumn);
        TextView meterTableColumn = tableRow.findViewById(R.id.meterTableColumn);

        //add data in new row
        SRNOTableColumn.setText(String.valueOf(tableLayout.getChildCount()));
        meterTableColumn.setText(meter);

        //add new TableRow to TableLayout
        tableLayout.addView(tableRow);
    }

    //method to add design data in intent based on number of designs
    private void addDesignDataInIntent(Intent intent, String numberOfDesigns) {
        switch (numberOfDesigns) {
            case "1":
                intent.putExtra(DESIGN_1_OBJECT, gson.toJson(design1));
                break;
            case "2":
                intent.putExtra(DESIGN_1_OBJECT, gson.toJson(design1));
                intent.putExtra(DESIGN_2_OBJECT, gson.toJson(design2));
                break;
            case "3":
                intent.putExtra(DESIGN_1_OBJECT, gson.toJson(design1));
                intent.putExtra(DESIGN_2_OBJECT, gson.toJson(design2));
                intent.putExtra(DESIGN_3_OBJECT, gson.toJson(design3));
                break;
            case "4":
                intent.putExtra(DESIGN_1_OBJECT, gson.toJson(design1));
                intent.putExtra(DESIGN_2_OBJECT, gson.toJson(design2));
                intent.putExtra(DESIGN_3_OBJECT, gson.toJson(design3));
                intent.putExtra(DESIGN_4_OBJECT, gson.toJson(design4));
                break;
        }
    }

    private void createChallanPdf() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1300, 1900, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        //initialize Paint to draw text
        Paint paint = new Paint();
        paint.setTextSize(30);

        //add data in challan PDF
        createStructureAndAddDataInChallanPDF(page, paint, ChallanDetailsActivity.this, challan, design1, design2, design3, design4);

        document.finishPage(page);

        try {
            //create file directory
            File superTextilesDirectory;
            boolean isDirectoryCreated = true;
            File challanPDFFile;
            Uri fileUri = null;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                superTextilesDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Super Textiles/Challans");

                isDirectoryCreated = superTextilesDirectory.exists();
                if (!isDirectoryCreated) {
                    isDirectoryCreated = superTextilesDirectory.mkdirs();
                }

                if (isDirectoryCreated) {
                    challanPDFFile = new File(superTextilesDirectory, "Challan No. " + challan.getChallan_no() + ".pdf");
                    fileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", challanPDFFile);
                    //create PDF file
                    galleryAddFile(challanPDFFile.getPath(), this);
                    document.writeTo(new FileOutputStream(challanPDFFile));
                }
            } else {
                ContentResolver contentResolver = getContentResolver();
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, "Challan No. " + challan.getChallan_no());
                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/Super Textiles/Challans/");
                fileUri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                challanPDFFile = new File(String.valueOf(fileUri));
                //create PDF file
                galleryAddFile(challanPDFFile.getPath(), this);
                document.writeTo(getContentResolver().openOutputStream(fileUri));
            }

            //create PDF file if directory is created successfully
            if (isDirectoryCreated && challan != null && challan.getChallan_no() != null) {
                //show SnackBar on success
                Uri finalFileUri = fileUri;
                Snackbar snackbar = Snackbar.make(challanDetailsConstraintLayout, "PDF Created", Snackbar.LENGTH_LONG)
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